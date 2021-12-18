package top.lpepsi.lchatserver.service.user;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.lpepsi.lchatserver.core.util.LcIdGenerate;
import top.lpepsi.lchatserver.dao.mapper.FriendMapper;
import top.lpepsi.lchatserver.dao.mapper.UserMapper;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.entity.ResponseCode;
import top.lpepsi.lchatserver.entity.UserInfo;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 林北
 * @description
 * @date 2021-08-08 10:27
 */
@Service
@Slf4j
public class UserService {

    @Value("${endpoint}")
    private String endPoint;
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;
    @Value("${bucket}")
    private String bucket;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FriendMapper friendMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LcIdGenerate lcIdGenerate;


    public Response<UserInfo> save(UserInfo userInfo) {
        if (!StringUtils.hasText(userInfo.getUserName()) || !StringUtils.hasText(userInfo.getPassword())) {
            return Response.error(ResponseCode.PARAM_EMPTY, "用户名或密码不能为空");
        }
        if (!StringUtils.hasText(userInfo.getAvatar())) {
            //设置默认头像
            userInfo.setAvatar("https://lchat-server.oss-cn-shenzhen.aliyuncs.com/avatar/default/avatar.jpg");
        }
        if (!StringUtils.hasText(userInfo.getBackground())) {
            //设置默认背景图
            userInfo.setBackground("https://lchat-server.oss-cn-shenzhen.aliyuncs.com/background/default/background.jpg");
        }
        if (StringUtils.hasText(userInfo.getLcid())) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcid", userInfo.getLcid());
            final UserInfo old = userMapper.selectOne(queryWrapper);
            if (old == null) {
                return Response.error(ResponseCode.PARAM_FAIL, "该用户不存在");
            }
        }
        if (!StringUtils.hasText(userInfo.getLcid())) {
            userInfo.setLcid(lcIdGenerate.generateUserId());
        }
        userMapper.insert(userInfo);
        return Response.success();
    }

    public Response<UserInfo> login(UserInfo userInfo) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userInfo.getUserName());
        final UserInfo one = userMapper.selectOne(queryWrapper);
        if (one == null || !one.getPassword().equals(userInfo.getPassword())) {
            return Response.error(ResponseCode.PARAM_FAIL, "用户名或者密码错误");
        }
        log.info(one.toString());
        calDays(one);
        setCache(one);
        return Response.success(one);
    }

    public Response<List<UserInfo>> searchUser(String key) {
        if (!StringUtils.hasText(key)) {
            return Response.error(ResponseCode.ERROR, "关键字不能为空");
        }
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", key);
        final List<UserInfo> list = userMapper.selectList(queryWrapper);
        return Response.success(list);
    }

    public Response<List<UserInfo>> getFriendInfo(String lcid) {
        final List<String> lcids = friendMapper.findFriendsByLcid(lcid);
        if (ObjectUtils.isEmpty(lcids)) {
            return Response.success(null);
        }
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("lcid", lcids);
        final List<UserInfo> userInfos = userMapper.selectList(queryWrapper);
        return Response.success(userInfos);
    }

    /**
     * @description 把登录的用户ID存入redis中
     * @author 林北
     * @date 2021-09-11 15:46
     **/
    private void setCache(UserInfo info) {
        stringRedisTemplate.opsForValue().set(info.getLcid(), "1");
    }

    private void calDays(UserInfo userInfo) {
        long betweenDay = DateUtil.between(new Date(userInfo.getCreated()), new Date(), DateUnit.DAY);
        userInfo.setDays(betweenDay == 0 ? 1 : (int) betweenDay);
    }

    public Response<UserInfo> uploadFile(MultipartFile file, String lcid, boolean flag) {
        String url = handlerFile(file, flag);
        if (!StringUtils.hasText(url)) {
            return Response.error(ResponseCode.ERROR, flag ? "背景图" : "头像" + "上传失败");
        }
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lcid", lcid);
        final UserInfo userInfo = userMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(userInfo)) {
            return Response.error(ResponseCode.PARAM_FAIL, "该用户不存在");
        }
        if (flag) {
            userInfo.setBackground(url);
        } else {
            userInfo.setAvatar(url);
        }
        return save(userInfo);
    }

    private String handlerFile(MultipartFile file, boolean flag) {
        String url = "";
        if (file != null) {
            String originalFilename = "";
            if (file.getOriginalFilename() != null && !"".equals(originalFilename = file.getOriginalFilename())) {
                File localFile = new File(originalFilename);
                try (FileOutputStream outputStream = new FileOutputStream(localFile)) {
                    outputStream.write(file.getBytes());
                    file.transferTo(localFile);
                    url = uploadLocalFileToOSS(localFile, flag);
                } catch (IOException e) {
                    log.error("handlerFileList cause {}", e.getMessage());
                } finally {
                    if (!localFile.delete()) {
                        log.error("本地文件删除失败: {}", localFile.getName());
                    }
                }
            }
        }
        return url;
    }

    private String uploadLocalFileToOSS(File localFile, boolean flag) {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId,
                accessKeySecret);
        boolean isImage = true;
        try {
            BufferedImage image = ImageIO.read(localFile);
            isImage = image != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        String filePre = flag ? "background" : "avatar";
        String fileAddress = filePre + "/" + dateStr + "/"
                + UUID.randomUUID().toString().replace("-", "")
                + "-" + localFile.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileAddress, localFile);
        String fileUrl;
        if (isImage) {
            fileUrl = "https://" + bucket + "." + endPoint + "/" + fileAddress;
        } else {
            fileUrl = "非图片 不可预览 文件路径为: " + fileAddress;
        }
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        ossClient.setBucketAcl(bucket, CannedAccessControlList.PublicRead);
        if (result != null) {
            log.info("OSS文件上传成功，URL: {}", fileUrl);
        }
        ossClient.shutdown();
        return fileUrl;
    }


}
