package space.changle.lynnia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 21:59
 * @description
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@MapperScan("space.changle.lynnia.repo.mapper")
public class LynniaStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(LynniaStartApplication.class, args);
    }

}
