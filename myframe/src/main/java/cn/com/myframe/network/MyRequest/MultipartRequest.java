package cn.com.myframe.network.MyRequest;

import cn.com.myframe.MyUtils;
import cn.com.myframe.network.httpmime_4_2_6.apache.http.entity.mime.MultipartEntity;
import cn.com.myframe.network.httpmime_4_2_6.apache.http.entity.mime.content.FileBody;
import cn.com.myframe.network.httpmime_4_2_6.apache.http.entity.mime.content.StringBody;
import cn.com.myframe.network.volley.AuthFailureError;
import cn.com.myframe.network.volley.NetworkResponse;
import cn.com.myframe.network.volley.Request;
import cn.com.myframe.network.volley.Response;
import cn.com.myframe.network.volley.VolleyLog;
import cn.com.myframe.network.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pizhuang on 2015/7/17.
 */
public class MultipartRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();

    private final Response.Listener<String> mListener;

    private Map<String, String> mParams;

    private Map<String, File> fileMap;
    /**
     * 单个文件
     * @param url
     * @param errorListener
     * @param listener
     * @param filePartName
     * @param file
     * @param params
     */
    public MultipartRequest(String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener, String filePartName, File file,
                            Map<String, String> params) {
        super(Method.POST, url, errorListener);
        fileMap = new HashMap<String, File>();
        fileMap.put(filePartName, file);
        mListener = listener;
        mParams = params;
        buildMultipartEntity();
    }

    public MultipartRequest(String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener,Map<String,File> fileMap, Map<String, String> params) {
        super(Method.POST, url, errorListener);
        this.fileMap = fileMap;
        mListener = listener;
        mParams = params;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        Set<String> fileKeySet = fileMap.keySet();
        Iterator<String> iterator = fileKeySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            File file = fileMap.get(key);
            entity.addPart(key,new FileBody(file));
        }
        MyUtils.log(MultipartRequest.class,fileKeySet.size()+"个，长度："+entity.getContentLength());

        try {
            if (mParams != null && mParams.size() > 0) {
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    entity.addPart(
                            entry.getKey(),
                            new StringBody(entry.getValue(), Charset
                                    .forName("UTF-8")));
                }
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        MyUtils.log(MultipartRequest.class,"parseNetworkResponse");
        if (VolleyLog.DEBUG) {
            if (response.headers != null) {
                for (Map.Entry<String, String> entry : response.headers
                        .entrySet()) {
                    VolleyLog.d(entry.getKey() + "=" + entry.getValue());
                }
            }
        }

        String parsed;
        try {
            parsed = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }


    /*
     * (non-Javadoc)
     *
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        VolleyLog.d("getHeaders");
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }


        return headers;
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}