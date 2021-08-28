package com.controller;

import com.result.Result;
import com.result.ResultFactory;
import com.service.OssService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OssController {
    @Autowired
    private OssService ossService;

    //上传图片的方法
    @PostMapping("/oss/uploadAvator")
    public Result uploadOssFile(MultipartFile file) {
        if (file.getSize() > 50 * 1024 * 1024) {
            return ResultFactory.buildFailResult("文件大小不得超过50M");
        }
        InputStream inputStream = null;
        try {
            Thumbnails.of(file.getInputStream()).size(500, 600);
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //module可以用前端给的用户id之类的
        String module = "hello";
        String url = ossService.uploadFileAvatar(inputStream, module, file.getOriginalFilename());
        return ResultFactory.buildSuccessResult("文件上传成功", url);
    }

    //上传多个图片的方法
    @PostMapping("/oss/uploadPictures")
    public Result uploadOssFiles(List<MultipartFile> files) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (MultipartFile file : files) {
            i++;
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String module = "hello";
            String url = ossService.uploadFileAvatar(inputStream, module, file.getOriginalFilename());
            if (i == files.size()) {
                stringBuilder.append(url);
            } else stringBuilder.append(url + ",");
        }
        String urls = stringBuilder.toString();
        return ResultFactory.buildSuccessResult("多个文件上传成功", urls);
    }

}
