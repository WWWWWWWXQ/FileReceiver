package com.wxq.receiver.service.impl;

import com.wxq.receiver.service.ReceiverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class DefaultReceiverService implements ReceiverService {

    @Value("${parentPath}")
    private String parentPath;

    public String store(MultipartFile uploadedFile) {
        // 获取文件名
        String originalFilename = uploadedFile.getOriginalFilename();
        // 如果同一张图片怕覆盖，也可以加个时间戳
        //String fileOrigName = System.currentTimeMillis() + file.getOriginalFilename();

        String filepath = parentPath + originalFilename;//文件存入当前项目的resources/static/目录下
        log.info("接收到[{}], 将储存在[{}]下", originalFilename, filepath);
        try {
            File targetFile = new File(filepath);
            if (targetFile.exists()) {
                log.info("文件[{}]在[{}]目录下已存在", originalFilename, parentPath);
                return filepath;
            }

            //第一次上传时，如果目录不存在，创建目录
            if (!targetFile.getParentFile().exists()) {
                log.info("生成目录[{}]中...", targetFile.getParentFile().toString());
                targetFile.getParentFile().mkdirs();
            }

            //将文件输出到本地
            File files = new File(filepath);
            if (!files.exists()) {				//如果文件不存在则新建文件
                log.info("[{}]文件创建中...", files.toString());
                files.createNewFile();
            }
            try( FileOutputStream output = new FileOutputStream(files)) {
                byte[] bytes = uploadedFile.getBytes();
                output.write(bytes);                //将数组的信息写入文件中
                output.close();
            }
            log.info("[{}]创建完成", files.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return filepath;
    }
}
