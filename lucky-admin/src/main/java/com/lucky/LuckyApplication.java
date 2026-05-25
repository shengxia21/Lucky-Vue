package com.lucky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动程序
 *
 * @author lucky
 */
@SpringBootApplication
public class LuckyApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Lucky-Vue启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
