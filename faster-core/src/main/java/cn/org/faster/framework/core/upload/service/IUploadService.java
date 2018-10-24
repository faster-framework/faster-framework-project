package cn.org.faster.framework.core.upload.service;

import cn.org.faster.framework.core.upload.model.UploadRequest;
import cn.org.faster.framework.core.upload.model.UploadSuccess;
import cn.org.faster.framework.core.upload.model.UploadToken;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhangbowen
 */
public abstract class IUploadService {
    protected static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");

    protected IUploadService() {
    }

    /**
     * 预上传，获取token
     *
     * @param uploadRequest 上传请求实体
     * @return 签名
     */
    public abstract UploadToken preload(UploadRequest uploadRequest);

    /**
     * 通过multipartFile上传文件，无token
     *
     * @param uploadFile    文件
     * @param uploadRequest 请求实体
     * @return 上传成功实体
     * @throws IOException ioexception
     */
    public UploadSuccess upload(MultipartFile uploadFile, UploadRequest uploadRequest) throws IOException {
        return this.upload(uploadFile, uploadRequest, null);
    }

    /**
     * 通过multipartFile上传文件，有token
     *
     * @param uploadFile    文件
     * @param uploadRequest 请求实体
     * @param token         签名字符串
     * @return 上传成功实体
     * @throws IOException ioexception
     */
    public abstract UploadSuccess upload(MultipartFile uploadFile, UploadRequest uploadRequest, String token) throws IOException;


    /**
     * 通过stream上传文件，无token
     *
     * @param uploadStream  文件流
     * @param uploadRequest 请求实体
     * @return 上传成功实体
     * @throws IOException ioexception
     */
    public UploadSuccess upload(InputStream uploadStream, UploadRequest uploadRequest) throws IOException {
        return this.upload(uploadStream, uploadRequest, null);
    }

    /**
     * 通过stream上传文件，有token
     *
     * @param uploadStream  上传的stream流
     * @param uploadRequest 请求实体
     * @param token         签名字符串
     * @return 上传成功实体
     * @throws IOException ioexception
     */
    public abstract UploadSuccess upload(InputStream uploadStream, UploadRequest uploadRequest, String token) throws IOException;

    /**
     * 通过字节数组上传文件，无token
     *
     * @param uploadByte    字节数组
     * @param uploadRequest 请求实体
     * @return 上传成功实体
     * @throws IOException ioexception
     */
    public UploadSuccess upload(byte[] uploadByte, UploadRequest uploadRequest) throws IOException {
        return this.upload(uploadByte, uploadRequest, null);
    }

    /**
     * 通过字节数组上传文件，有token
     *
     * @param uploadByte    上传的字节数组
     * @param uploadRequest 请求实体
     * @param token         签名字符串
     * @return 上传成功实体
     * @throws IOException ioexception
     */
    public abstract UploadSuccess upload(byte[] uploadByte, UploadRequest uploadRequest, String token) throws IOException;

    /**
     * 根据上传参数获取文件名称
     *
     * @param uploadRequest 上传参数
     * @return 文件名
     */
    protected String getFileName(UploadRequest uploadRequest) {
        String fileName = StringUtils.isEmpty(uploadRequest.getFileName()) ? LocalDateTime.now().format(FILE_NAME_FORMATTER) : uploadRequest.getFileName();
        //如果不覆盖,在文件后增加时间后缀
        if (uploadRequest.getIsCover() == 0) {
            fileName = fileName.concat(LocalDateTime.now().format(FILE_NAME_FORMATTER));
        }
        return fileName;
    }

    /**
     * 预览、下载上传文件
     *
     * @param fileName 文件名称
     * @return 字节流
     */
    public byte[] files(String fileName) {
        return new byte[]{};
    }
}
