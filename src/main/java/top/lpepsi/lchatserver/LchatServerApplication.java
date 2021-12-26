package top.lpepsi.lchatserver;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author 林北
 */
@SpringBootApplication
@MapperScan(basePackages = "top.lpepsi.lchatserver.dao.mapper")
public class LchatServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LchatServerApplication.class, args);
    }

}
