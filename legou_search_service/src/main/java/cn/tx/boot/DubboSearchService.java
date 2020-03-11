package cn.tx.boot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
public class DubboSearchService {

    public static void main(String[] args) throws IOException {
        // 只需要加载Spring的配置文件即可
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        System.in.read();
    }
}
