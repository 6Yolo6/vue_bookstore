package com.example.bookstore.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 上传图片工具类
 */
public class UploadUtils {

    public static String uploadImage(MultipartFile multipartFile){

        try {
            String realfilename = multipartFile.getOriginalFilename();

            String imgSuffix = realfilename.substring(realfilename.lastIndexOf("."));

            String newFilename = UUID.randomUUID() +imgSuffix;
            String path = new File(System.getProperty("user.dir")).getParent();
//            System.out.println(path);
//            System.out.println(new File(path+"\\bookstore_app\\bookstore_app\\public\\image\\"));
            //上传到哪个路径下
            File targetPath = new File(path+"\\bookstore_app\\bookstore_app\\public\\image\\");
//            File targetPath = new File("\\home\\vue_springboot\\dist\\image\\");
//            if (!targetPath.exists()){
//                targetPath.mkdirs();
//            }
            File targetFilename = new File(targetPath, newFilename);
            multipartFile.transferTo(targetFilename);
//            return String.valueOf(targetFilename);//返回文件路径
            return "image/"+newFilename;//资源映射路径
        } catch (IOException e) {
            e.printStackTrace();
            return "失败！";
        }
    }

}