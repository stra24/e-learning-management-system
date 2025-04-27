package com.everrefine.elms.application.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageApplicationServiceImpl implements FileStorageApplicationService {

  @Value("${upload.directory:uploads}")
  private String uploadDirectory;

  public String saveImage(MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new IOException("空のファイルです");
    }

    // ファイル名をランダムにする（衝突防止）
    String originalFilename = file.getOriginalFilename();
    String extension = "";

    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    String randomFileName = UUID.randomUUID() + extension;
    String absoluteUploadDir = new File(uploadDirectory).getAbsolutePath();
    String savePath = absoluteUploadDir + File.separator + randomFileName;

    // 保存先ディレクトリがなければ作成
    File dir = new File(uploadDirectory);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    try {
      // ファイル保存
      file.transferTo(new File(savePath));
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 返すパスは、例えばフロントから参照できるように工夫してもOK
    return "/uploads/" + randomFileName;
  }
}