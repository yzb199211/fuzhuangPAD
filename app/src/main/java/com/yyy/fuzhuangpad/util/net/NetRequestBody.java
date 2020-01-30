package com.yyy.fuzhuangpad.util.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class NetRequestBody {

    public static MultipartBody getMultipartBody(List<NetParams> params) {
        if (params != null & params.size() > 0) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < params.size(); i++)
                builder.addFormDataPart(params.get(i).getKey(), params.get(i).getValue());
            return builder.build();
        }
        return null;
    }

    public static MultipartBody getMultipartBody(HashMap<String, String> params) {
        if (params != null & params.size() > 0) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                builder.addFormDataPart((String) key, (String) val);
            }
            return builder.build();
        }
        return null;
    }

    public static RequestBody getRequestBody(List<NetParams> params) {
        StringBuffer param = new StringBuffer();
        if (params != null & params.size() > 0)
            for (int i = 0; i < params.size(); i++) {
                param.append(params.get(i).getKey()).append("=").append(params).append("&");
            }
        String jso = param.substring(0, param.length() - 1);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), jso);
        return requestBody;
    }

    public static RequestBody getRequestBody(HashMap<String, String> params) {
        StringBuffer data = new StringBuffer();
        if (params != null && params.size() > 0) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                data.append(key).append("=").append(val).append("&");
            }
        }
        String jso = data.substring(0, data.length() - 1);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), jso);
        return requestBody;
    }
}
