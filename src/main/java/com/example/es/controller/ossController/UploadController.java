package com.example.es.controller.ossController;

import com.example.es.utils.AliOssUtils;
import com.example.es.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件上传", description = "文件上传相关接口")
@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private AliOssUtils aliOssUtils;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("/upload/image")
    @Operation(summary = "上传图片", description = "上传图片到OSS，返回图片URL")
    public Response uploadImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Response.fail("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png|gif|webp|bmp)$")) {
            return Response.fail("只支持图片格式（jpg、jpeg、png、gif、webp、bmp）");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            return Response.fail("图片大小不能超过5MB");
        }

        try {
            String url = aliOssUtils.upload(file);
            return Response.success(url);
        } catch (Exception e) {
            return Response.fail("图片上传失败：" + e.getMessage());
        }
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload/file")
    @Operation(summary = "上传文件", description = "上传文件到OSS，返回文件URL")
    public Response uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Response.fail("上传文件不能为空");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            return Response.fail("文件大小不能超过10MB");
        }

        try {
            String url = aliOssUtils.upload(file);
            return Response.success(url);
        } catch (Exception e) {
            return Response.fail("文件上传失败：" + e.getMessage());
        }
    }
}
