package com.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssService {

    //想设详细一下的权限没找到很好的办法，最后加了源域名限制orz

    @Value("${aliyun.oss.file.endpoint}")
    private String endpoints;

    @Value("${aliyun.oss.file.keyid}")
    private String keyid;

    @Value("${aliyun.oss.file.keysecret}")
    private String keysecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketname;


    public String uploadFileAvatar(InputStream inputStream, String module, String originalFilename) {

        String endpoint = endpoints;
        String accessKeyId = keyid;
        String accessKeySecret = keysecret;
        String bucketName = bucketname;

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String folder = new DateTime().toString("yyyy/MM/dd");
        String fileName = UUID.randomUUID().toString();

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = module + "/" + folder + "/" + fileName + fileExtension;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");

        ossClient.putObject(bucketName, objectName, inputStream,objectMetadata);
        ossClient.shutdown();

        String url = "http://"+bucketName+"."+endpoint+"/"+objectName;

        return url;

    }

}
