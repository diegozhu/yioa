package com.yioa.sys.service;

import com.google.common.collect.ImmutableMap;
import com.yioa.common.util.CommonUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by tao on 2017-07-09.
 */
@Service
public class FileUploadService {


    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);

//    private String upPath = "E:/work/myGitHubProj/yi_oa/wap/up";

    private String upPath = "/home/ubuntu/demo/yioa_static/b/up";


    /**
     * 存放在服务器的固定文件夹上，因为都是图片，直接返回链接给前台构造url，通过Nginx转发
     *
     * @return
     */
    public Map<String ,String> saveFile(MultipartFile uploadFile, String userId) throws UnsupportedEncodingException {

        String folder = String.valueOf(CommonUtil.getFileFolderName(userId));

        File tagDir = new File(upPath+ File.separator + folder);

        if (!tagDir.exists()) {
            tagDir.mkdir();
        }
        String fileName = uploadFile.getOriginalFilename();

        String newFileName = CommonUtil.genFileName() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);

        String fileStorePath = tagDir + File.separator + newFileName;

        File destFile = new File(fileStorePath);
        String httpPath = folder + "/" + newFileName;

        try {
            FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), destFile);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
            return null;
        }
        Map<String, String> map = ImmutableMap.of("name", newFileName,"path",httpPath);

        return map;
    }


}

