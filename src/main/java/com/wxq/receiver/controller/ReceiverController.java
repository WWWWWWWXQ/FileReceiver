package com.wxq.receiver.controller;

import com.wxq.receiver.service.impl.DefaultReceiverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class ReceiverController {

    @Autowired
    private DefaultReceiverService receiverService;

    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public String store(@RequestParam MultipartFile uploadedFile){
        String filepath = receiverService.store(uploadedFile);
        return filepath;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String download(HttpServletResponse response,@RequestParam("filename") String filename){
        return receiverService.download(response, filename);
//        return "success";
    }

}

