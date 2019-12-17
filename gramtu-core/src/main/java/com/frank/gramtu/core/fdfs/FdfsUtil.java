package com.frank.gramtu.core.fdfs;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@Component
public class FdfsUtil {

    @Resource
    private FastFileStorageClient storageClient;

    /**
     * 上传图片文件.
     */
    public String uploadImage(MultipartFile multipartFile) throws Exception {

        String originalFileName = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);

        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                multipartFile.getInputStream(),
                multipartFile.getSize(),
                originalFileName, null);

        return storePath.getFullPath();
    }

    /**
     * 上传文件.
     */
    public String uploadFile(MultipartFile multipartFile) throws Exception {

        String originalFileName = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
        StorePath storePath = this.storageClient.uploadFile(multipartFile.getInputStream(),
                multipartFile.getSize(),
                originalFileName, null);

        return storePath.getFullPath();
    }

    /**
     * 删除文件.
     */
    public boolean deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            log.info("文件路径为空!");
            return false;
        }

        return true;
    }
}
