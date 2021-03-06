package cn.zxJava.boot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 拓薪教育 -- 腾讯课堂认证机构
 * 樱木老师
 */
public class GoodsBoot {

    public static void main(String[] args) throws IOException {
        // 需要去加载不同的配置文件 dao  service  tx 配置文件
        // classpath*:  表示加载当前项目及其该项目中jar包里面的配置文件
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        // 阻塞
        System.in.read();
    }

}

