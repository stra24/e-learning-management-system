package com.everrefine.elms.presentation;

import com.everrefine.elms.application.service.FileStorageApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

  private final FileStorageApplicationService fileStorageApplicationService;

  public FileUploadController(FileStorageApplicationService fileStorageApplicationService) {
    this.fileStorageApplicationService = fileStorageApplicationService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
    try {
      String filePath = fileStorageApplicationService.saveImage(file);
      return ResponseEntity.ok(filePath); // 保存先のパスを返す
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("ファイルアップロード失敗: " + e.getMessage());
    }
  }
}