package cn.zxJava.controller;

import cn.zxJava.entity.Result;
import cn.zxJava.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    //从配置文件中获取图片服务器的地址
    @Value("${FILE_SERVER_URL}")
    private String path;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){

        try {
            //创建文件上传工具类
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");

            //获取文件后缀名
            //获取文件名
            String filename = file.getOriginalFilename();
            //确定后缀名的位置
            int index = filename.lastIndexOf(".");
            //截取文件后缀名
            String exName = filename.substring(index + 1);

            //上传方法，返回值为文件在服务器群组中的位置
            String s = client.uploadFile(file.getBytes(), exName);

            //拼接成完整路径
            String resultStr = path + s;

            return new Result(true,"上传成功",resultStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
