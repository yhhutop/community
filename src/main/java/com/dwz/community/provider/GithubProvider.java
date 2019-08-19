package com.dwz.community.provider;

import com.alibaba.fastjson.JSON;
import com.dwz.community.dto.AccessTokenDTO;
import com.dwz.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){

       MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                String access_token = string.split("&")[0].split("=")[1];
                return access_token;
            }catch (Exception e){
                e.printStackTrace();
            }
        return null;
    }

    public GithubUser getGitHubUser(String access_token){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+access_token)
                .build();

        try (Response response = client.newCall(request).execute()) {
           String json = response.body().string();
           return JSON.parseObject(json,GithubUser.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
