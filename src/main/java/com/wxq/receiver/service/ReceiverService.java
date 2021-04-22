package com.wxq.receiver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiverService {

    String store(MultipartFile uploadedFile);

}
