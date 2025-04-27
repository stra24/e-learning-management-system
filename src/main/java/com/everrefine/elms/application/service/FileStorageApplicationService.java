package com.everrefine.elms.application.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageApplicationService {

  String saveImage(MultipartFile file) throws IOException;
}
