package cn.jerryzone.proindicator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProIndicatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProIndicatorApplication.class, args);
    }

}
