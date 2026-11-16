package com.takeaway.controller;

import com.takeaway.common.exception.BaseException;
import com.takeaway.common.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 通用接口：图片上传（模拟本地存储）
 */
@RestController
@RequestMapping("/api/admin/common")
public class CommonController {

    @Value("${takeaway.upload-dir:./upload/}")
    private String uploadDir;

    /**
     * 上传图片，返回可访问的相对路径
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BaseException("上传文件不能为空");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BaseException("仅支持上传图片文件");
        }
        String suffix = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString().replace("-", "")
                + (StringUtils.hasText(suffix) ? "." + suffix : "");

        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        file.transferTo(dir.resolve(fileName));
        return Result.success("/upload/" + fileName);
    }
}
