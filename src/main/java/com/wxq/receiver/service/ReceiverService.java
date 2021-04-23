package com.wxq.receiver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ReceiverService {

    String store(MultipartFile uploadedFile);

    String download(HttpServletResponse response, String filename);
}
