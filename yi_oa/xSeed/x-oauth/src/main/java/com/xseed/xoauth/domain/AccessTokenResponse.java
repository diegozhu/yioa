package com.xseed.xoauth.domain;

/**
 * Created by tao on 2017-05-21.
 *
 *  see http://dev.yixin.im/wiki/doc/index.html?title=%E6%98%93%E4%BF%A1%E7%BD%91%E9%A1%B5%E6%8E%88%E6%9D%83%E6%8E%A5%E5%8F%A3
 *  response like {
                     "access_token":"ACCESS_TOKEN",
                     "expires_in":7200,
                     "refresh_token":"REFRESH_TOKEN",
                     "openid":"OPENID",
                     "scope":"SCOPE"
                }
 */
public class AccessTokenResponse {
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String openid;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "AccessTokenResponse{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
