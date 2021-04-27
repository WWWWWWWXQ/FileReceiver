package com.wxq.receiver.service.impl;

import com.wxq.receiver.repository.ReceiverRepository;
import com.wxq.receiver.repository.po.Record;
import com.wxq.receiver.service.ReceiverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@Slf4j
@Service
@Transactional
public class DefaultReceiverService implements ReceiverService {

    @Value("${parentPath}")
    private String parentPath;

    @Autowired
    private ReceiverRepository receiverRepository;

    public String store(MultipartFile uploadedFile) {
        // 获取文件名
        String originalFilename = uploadedFile.getOriginalFilename();
        // 如果同一张图片怕覆盖，也可以加个时间戳
        //String fileOrigName = System.currentTimeMillis() + file.getOriginalFilename();

        //文件存入${parentPath}目录下
        String filepath = parentPath + originalFilename;
        log.info("接收到[{}], 将储存在[{}]下", originalFilename, parentPath);
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
            try(FileOutputStream output = new FileOutputStream(files)) {
                byte[] bytes = uploadedFile.getBytes();
                output.write(bytes);                //将数组的信息写入文件中
                output.close();
            }
            log.info("[{}]创建完成", files.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        Record record = new Record();
        record.setFilename(filepath);
        record.setTime(new Date());
        record.setId(0L);
        receiverRepository.save(record);
        return filepath;
    }

    @Override
    public String download(HttpServletResponse response, String filename) {
        log.info("[{}]寻找中...", filename);
        File file = new File(filename);
        //判断文件父目录是否存在
        if (!file.exists()) {
            log.info("[{}] 不存在", filename);
            return "文件不存在";
        }
        //指定返回文件格式
        response.setContentType("application/force-download;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // response.setContentType("application/force-download");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(filename,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        //写文件
        byte[] buffer = new byte[1024];
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            OutputStream os = response.getOutputStream()) {
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("[{}] 已下载", filename);
        return "success";
    }


}
