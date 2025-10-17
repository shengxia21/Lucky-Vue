package com.lucky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author lucky
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LuckyApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  心云启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
