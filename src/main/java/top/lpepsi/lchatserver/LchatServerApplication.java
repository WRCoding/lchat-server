package top.lpepsi.lchatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * @author 林北
 */
@SpringBootApplication
@EnableJpaAuditing
public class LchatServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LchatServerApplication.class, args);
    }

}
