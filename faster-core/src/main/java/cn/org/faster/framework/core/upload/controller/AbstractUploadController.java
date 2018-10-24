package cn.org.faster.framework.core.upload.controller;

import cn.org.faster.framework.core.exception.model.BasisErrorCode;
import cn.org.faster.framework.core.exception.model.ErrorResponseEntity;
import cn.org.faster.framework.core.upload.model.UploadRequest;
import cn.org.faster.framework.core.upload.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author zhangbowen
 */
public abstract class AbstractUploadController {
    @Autowired
    protected IUploadService uploadService;


    /**
     * 预上传，返回上传所需参数
     *
     * @param uploadRequest 上传请求
     * @return httpResponse
     */
    @GetMapping("/upload/preload")
    public ResponseEntity preload(UploadRequest uploadRequest) {
        return ResponseEntity.ok(uploadService.preload(uploadRequest));
    }

    /**
     * 上传文件
     *
     * @param uploadFile    文件
     * @param uploadRequest 上传请求
     * @param token         签名字符串
     * @return httpResponse
     */
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile uploadFile, UploadRequest uploadRequest, String token) {
        try {
            return ResponseEntity.ok(uploadService.upload(uploadFile, uploadRequest, token));
        } catch (IOException e) {
            return ErrorResponseEntity.error(BasisErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 预览、下载上传的文件
     *
     * @param fileName 文件名称
     * @return 文件流
     */
    @GetMapping("/media/{fileName}")
    public ResponseEntity preview(@PathVariable String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(uploadService.files(fileName), headers, HttpStatus.OK);
    }
}
