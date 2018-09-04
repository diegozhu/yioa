package com.yioa.common.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by tao on 2017-05-22.
 */
@Service
public class PathService {

    @Value("${yioa.oa.root_path}")
    private String oaRootPath;

    @Value("${yioa.oa.pre_path}")
    private String oaPre;


    public String getOaRootPath() {
        return this.oaRootPath;
    }

    public String getOaPre() {
        return oaPre;
    }
}
