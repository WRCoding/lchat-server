package top.lpepsi.lchatserver.service.user;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.lpepsi.lchatserver.dao.FriendRepository;
import top.lpepsi.lchatserver.dao.UserRepository;
import top.lpepsi.lchatserver.entity.Friend;
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
import java.util.UUID;

/**
 * @author 林北
 * @description
 * @date 2021-08-08 10:27
 */
@Service
@Slf4j
public class UserService {

    public static final String END_POINT = "oss-cn-shenzhen.aliyuncs.com";
    public static final String ACCESS_KEY_ID = "LTAI5tNZL22f5adNW1D5Fnq8";
    public static final String ACCESS_KEY_SECRET = "BV1RqiggdmXcROb06G1hRMKHu8yGeM";
    public static final String BUCKET = "lchat-server";

    @Resource
    private UserRepository userRepository;

    @Resource
    private FriendRepository friendRepository;
    @Resource
    private StringRedisTemplate  stringRedisTemplate;


    public Response<UserInfo> save(UserInfo userInfo){
        if (!StringUtils.hasText(userInfo.getUserName()) || !StringUtils.hasText(userInfo.getPassword())){
            return Response.error(ResponseCode.PARAM_EMPTY,"用户名或密码不能为空");
        }
        if (!StringUtils.hasText(userInfo.getAvatar())){
            //设置默认头像
            userInfo.setAvatar("https://lchat-server.oss-cn-shenzhen.aliyuncs.com/avatar/default/avatar.jpg");
        }
        if (!StringUtils.hasText(userInfo.getBackground())){
            //设置默认背景图
            userInfo.setBackground("https://lchat-server.oss-cn-shenzhen.aliyuncs.com/background/default/background.jpg");
        }
        if (StringUtils.hasText(userInfo.getId())){
            UserInfo old = userRepository.findById(userInfo.getId()).orElse(null);
            if (old == null){
                return Response.error(ResponseCode.PARAM_FAIL,"该用户不存在");
            }
        }
        UserInfo save = userRepository.save(userInfo);
        calDays(save);
        return Response.success(save);
    }

    public Response<UserInfo> login(UserInfo userInfo) {
        UserInfo info = userRepository.findUserInfoByUserName(userInfo.getUserName());
        if (info == null || !info.getPassword().equals(userInfo.getPassword())){
            return Response.error(ResponseCode.PARAM_FAIL,"用户名或者密码错误");
        }
        log.info(info.toString());
        calDays(info);
        setConnect(userInfo);
        setCache(info);
        return Response.success(info);
    }

    private void setConnect(UserInfo userInfo) {

    }

    public Response<String> addFriend(Friend friend) {
        friendRepository.save(friend);
        return Response.success();
    }
    /**
     * @description 把登录的用户ID存入redis中
     * @author 林北
     * @date 2021-09-11 15:46
     **/
    private void setCache(UserInfo info) {
        stringRedisTemplate.opsForValue().set(info.getId(),"1");
    }

    private void calDays(UserInfo userInfo) {
        long betweenDay = DateUtil.between(userInfo.getCreated(), new Date(), DateUnit.DAY);
        userInfo.setDays(betweenDay == 0 ? 1 : (int) betweenDay);
    }

    public Response<UserInfo> uploadFile(MultipartFile file,String userId,boolean flag){
        String url = handlerFile(file, flag);
        if (!StringUtils.hasText(url)){
            return Response.error(ResponseCode.ERROR,flag ? "背景图" : "头像" + "上传失败");
        }
        UserInfo userInfo = userRepository.findById(userId).orElse(null);
        if (ObjectUtils.isEmpty(userInfo)){
            return Response.error(ResponseCode.PARAM_FAIL,"该用户不存在");
        }
        if (flag) {
            userInfo.setBackground(url);
        } else {
            userInfo.setAvatar(url);
        }
        return save(userInfo);
    }

    private String handlerFile(MultipartFile file,boolean flag) {
        String url = "";
        if (file != null) {
            String originalFilename = "";
            if (file.getOriginalFilename() != null && !"".equals(originalFilename=file.getOriginalFilename())) {
                File localFile = new File(originalFilename);
                try (FileOutputStream outputStream = new FileOutputStream(localFile)) {
                    outputStream.write(file.getBytes());
                    file.transferTo(localFile);
                    url = uploadLocalFileToOSS(localFile,flag);
                } catch (IOException e) {
                    log.error("handlerFileList cause {}",e.getMessage());
                }finally {
                    if (!localFile.delete()){
                        log.error("本地文件删除失败: {}",localFile.getName());
                    }
                }
            }
        }
        return url;
    }

    private String uploadLocalFileToOSS(File localFile,boolean flag) {
        OSS ossClient = new OSSClientBuilder().build(END_POINT,ACCESS_KEY_ID,
                ACCESS_KEY_SECRET);
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
        String fileAddress = filePre +"/"+dateStr+"/"
                + UUID.randomUUID().toString().replace("-","")
                +"-"+localFile.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET,fileAddress,localFile);
        String fileUrl;
        if (isImage){
            fileUrl = "https://"+BUCKET+"."+END_POINT+"/"+fileAddress;
        }else{
            fileUrl = "非图片 不可预览 文件路径为: "+fileAddress;
        }
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        ossClient.setBucketAcl(BUCKET, CannedAccessControlList.PublicRead);
        if (result != null){
            log.info("OSS文件上传成功，URL: {}",fileUrl);
        }
        ossClient.shutdown();
        return fileUrl;
    }


}
