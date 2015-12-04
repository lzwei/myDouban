package com.samli.douban.netUtils;

import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import com.samli.douban.Utils.AESHelper;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by zhy on 15/11/6.
 */
public abstract class OkHttpRequest
{
    protected OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient;

    protected RequestBody requestBody;
    protected Request request;

    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    protected OkHttpRequest(String url, Object tag,
                            Map<String, String> params, Map<String, String> headers)
    {
        mOkHttpClient = mOkHttpClientManager.getOkHttpClient();
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
    }

    protected abstract Request buildRequest();

    protected abstract RequestBody buildRequestBody();

    protected void prepareInvoked(ResultCallback callback)
    {
        requestBody = buildRequestBody();
        requestBody = wrapRequestBody(requestBody, callback);
        request = buildRequest();
    }


    public void invokeAsyn(ResultCallback callback)
    {
        prepareInvoked(callback);
        mOkHttpClientManager.execute(request, callback);
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback)
    {
        return requestBody;
    }


    public <T> T invoke(Class<T> clazz) throws IOException
    {
        requestBody = buildRequestBody();
        Request request = buildRequest();
        return mOkHttpClientManager.execute(request, clazz);
    }


    protected void appendHeaders(Request.Builder builder, Map<String, String> headers)
    {
        if (builder == null)
        {
            throw new IllegalArgumentException("builder can not be empty!");
        }

        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet())
        {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public void cancel()
    {
        if (tag != null)
            mOkHttpClientManager.cancelTag(tag);
    }


    public static class Builder
    {
        private String url;
        private Object tag;
        private Map<String, String> headers;
        private Map<String, String> params;
        private Pair<String, File>[] files;
        private MediaType mediaType;

        private String destFileDir;
        private String destFileName;

        private ImageView imageView;
        private int errorResId = -1;

        //for post
        private String content;
        private byte[] bytes;
        private File file;

        public Builder url(String url)
        {
            this.url = url;
            return this;
        }

        public Builder tag(Object tag)
        {
            this.tag = tag;
            return this;
        }

        public Builder params(Map<String, String> params)
        {
            this.params = params;
            return this;
        }
        public Builder encodeParams(Map<String, String> params){
            return encodeParams(params,true);
        }

        public Builder encodeParams(Map<String, String> params,boolean addAccessToken)
        {
            if (/*Session.getInstance(applicationContext).getAccess_token() != null &&*/ addAccessToken) {
                params.put("access_token", "");
            }
            StringBuffer args = new StringBuffer();
            if(params!=null){
                for (String key : params.keySet()) {
                    args.append(key + "=" + params.get(key) + "&");
                }
            }
            if (args.length() > 0) {
                args.deleteCharAt(args.length() - 1);
            }

            String result = null;
            byte[] aesData = null;
            try {
                //加密的key
                String keyValue = "1111d9cc94df4f278d8c5373fe831111";

//            String content = "username=12345678911&client_secret=e990d4529057afda274bb014c95fc7bd&grant_type=password&client_id=IOS&password=1234567";

                byte[] data = args.toString().replaceAll("%", URLEncoder.encode("%", "utf-8")).getBytes("UTF-8");
                aesData = AESHelper.encrypt(data, keyValue);

                //    LogUtils.i("AESEncryption:" +  new String(aesData,"UTF-8"));
                result = new String(Base64.encode(aesData, Base64.DEFAULT), "UTF-8");

                //  LogUtils.i("decode:" +  new String(Base64.decode(result.getBytes("UTF-8"),Base64.DEFAULT)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("result", result);
            Map<String,String> param=new HashMap<String,String>();
            param.put("args",result);

            this.params = param;
            return this;
        }

        public Builder addParams(String key, String val)
        {
            if (this.params == null)
            {
                params = new IdentityHashMap<>();
            }
            params.put(key, val);
            return this;
        }

        public Builder headers(Map<String, String> headers)
        {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String val)
        {
            if (this.headers == null)
            {
                headers = new IdentityHashMap<>();
            }
            headers.put(key, val);
            return this;
        }


        public Builder files(Pair<String, File>... files)
        {
            this.files = files;
            return this;
        }

        public Builder destFileName(String destFileName)
        {
            this.destFileName = destFileName;
            return this;
        }

        public Builder destFileDir(String destFileDir)
        {
            this.destFileDir = destFileDir;
            return this;
        }


        public Builder imageView(ImageView imageView)
        {
            this.imageView = imageView;
            return this;
        }

        public Builder errResId(int errorResId)
        {
            this.errorResId = errorResId;
            return this;
        }

        public Builder content(String content)
        {
            this.content = content;
            return this;
        }

        public Builder mediaType(MediaType mediaType)
        {
            this.mediaType = mediaType;
            return this;
        }

        public <T> T get(Class<T> clazz) throws IOException
        {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            return request.invoke(clazz);
        }

        public OkHttpRequest get(ResultCallback callback)
        {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T post(Class<T> clazz) throws IOException
        {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, mediaType, content, bytes, file);
            return request.invoke(clazz);
        }

        public OkHttpRequest post(ResultCallback callback)
        {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, mediaType, content, bytes, file);
            request.invokeAsyn(callback);
            return request;
        }

        public OkHttpRequest upload(ResultCallback callback)
        {
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T upload(Class<T> clazz) throws IOException
        {
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            return request.invoke(clazz);
        }


        public OkHttpRequest download(ResultCallback callback)
        {
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
            request.invokeAsyn(callback);
            return request;
        }

        public String download() throws IOException
        {
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
            return request.invoke(String.class);
        }

        /*public void displayImage(ResultCallback callback)
        {
            OkHttpRequest request = new OkHttpDisplayImgRequest(url, tag, params, headers, imageView, errorResId);
            request.invokeAsyn(callback);
        }*/


    }


}
