package util.android.ys.com.util;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * compile 'com.loopj.android:android-async-http:1.4.9'
 * Created by 18758 on 2018/5/4.
 */

public class HttpClientUtil {

    public interface HttpClientEventListener {
        void onSuccess(int status_code, byte[] bytes);

        void onFailure(int status_code, byte[] bytes);

        void onTimeout();
    }

    //使用异步Http发送get请求
    static public void get(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams(params);
        client.get(url, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (httpClientEventListener != null) {
                    httpClientEventListener.onSuccess(i, bytes);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (httpClientEventListener != null) {
                    if (throwable instanceof ConnectTimeoutException) {
                        httpClientEventListener.onTimeout();
                    } else if (throwable instanceof SocketTimeoutException) {
                        httpClientEventListener.onTimeout();
                    } else {
                        httpClientEventListener.onFailure(i, bytes);
                    }
                }
            }
        });
    }

    //使用异步Http发送post请求
    static public void post(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener, int timeout) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams rp = new RequestParams(params);
        client.setTimeout(timeout);
        client.post(url, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (httpClientEventListener != null) {
                    httpClientEventListener.onSuccess(i, bytes);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (httpClientEventListener != null) {
                    if (throwable instanceof ConnectTimeoutException) {
                        httpClientEventListener.onTimeout();
                    } else if (throwable instanceof SocketTimeoutException) {
                        httpClientEventListener.onTimeout();
                    } else {
                        httpClientEventListener.onFailure(i, bytes);
                    }
                }
            }
        });
    }

//    // 同步的方式发送post请求
//    static public void postSync(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener, int timeout) {
//
//        SyncHttpClient client = new SyncHttpClient();
//        RequestParams rp = new RequestParams(params);
//
//        rp.setUseJsonStreamer(false);
//        rp.setHttpEntityIsRepeatable(true);
//        client.setTimeout(timeout);
//        client.post(Table_Data.getContext(), url, rp, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.e("success", "postSync->成功");
//                if (httpClientEventListener != null) {
//                    httpClientEventListener.onSuccess(statusCode, responseBody);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                if (httpClientEventListener != null) {
//                    if (error instanceof ConnectTimeoutException) {
//                        httpClientEventListener.onTimeout();
//                    } else if (error instanceof SocketTimeoutException) {
//                        httpClientEventListener.onTimeout();
//                    } else {
//                        httpClientEventListener.onFailure(statusCode, responseBody);
//                    }
//                }
//            }
//        });
//    }


    // 附件上传
    static public void testPostJsonSync(Context context, String url, Map<String, Object> params, final HttpClientEventListener httpClientEventListener, int timeout) {

        SyncHttpClient client = new SyncHttpClient();
        RequestParams rp = new RequestParams();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof File) {
                try {
                    rp.put(entry.getKey(), (File) obj, RequestParams.APPLICATION_OCTET_STREAM);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                rp.put(entry.getKey(), obj);
            }
        }

        rp.setUseJsonStreamer(false);
        rp.setHttpEntityIsRepeatable(true);
        client.setTimeout(timeout);
        client.post(context, url, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("success", "postSync->成功");
                if (httpClientEventListener != null) {
                    httpClientEventListener.onSuccess(statusCode, responseBody);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (httpClientEventListener != null) {
                    if (error instanceof ConnectTimeoutException) {
                        httpClientEventListener.onTimeout();
                    } else if (error instanceof SocketTimeoutException) {
                        httpClientEventListener.onTimeout();
                    } else {
                        httpClientEventListener.onFailure(statusCode, responseBody);
                    }
                }
            }
        });
    }


    /**
     * 以下为android 自带 Http请求
     */

    static class PostSync2Task implements Runnable {

        private String url;
        private Map<String, String> params;
        private int timeout;
        private HttpClientEventListener httpClientEventListener;

        PostSync2Task(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener, int timeout) {

            this.url = url;
            this.params = params;
            this.timeout = timeout;
            this.httpClientEventListener = httpClientEventListener;

        }

        @Override
        public void run() {
            postSync2(url, params, httpClientEventListener, timeout);
        }

    }


    /**
     * http 异步post请求
     *
     * @param url
     * @param params
     * @param httpClientEventListener
     * @param timeout
     */
    static public void post2(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener, int timeout) {
        PostSync2Task postSync2Task = new PostSync2Task(url, params, httpClientEventListener, timeout);
        new Thread(postSync2Task).start();
    }


    /**
     * http 同步post请求
     *
     * @param url
     * @param params
     * @param httpClientEventListener
     * @param timeout
     */
    static public void postSync2(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener, int timeout) {
        try {
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> param : params.entrySet()) {
                pairList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }

            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, "UTF-8");
            // URL使用基本URL即可，其中不需要加参数
            HttpPost httpPost = new HttpPost(url);
            // 将请求体内容加入请求中
            httpPost.setEntity(requestHttpEntity);

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout)       // 设置连接超时时间 5秒钟
                    .setSocketTimeout(timeout)        // 设置读取超时时间5秒钟
                    .build();
            httpPost.setConfig(config);

            // 需要客户端对象来发送请求
            HttpClient httpClient = new DefaultHttpClient();
            // 发送请求
            HttpResponse response = httpClient.execute(httpPost);

            String data = EntityUtils.toString(response.getEntity(), "UTF-8");

            httpClientEventListener.onSuccess(200, data.getBytes());

            Log.d("response", response.toString());

        } catch (Exception error) {

            if (error instanceof SocketException) {
                httpClientEventListener.onTimeout();
            } else if (error instanceof ConnectException) {
                httpClientEventListener.onTimeout();
            } else {
                httpClientEventListener.onFailure(100, null);
            }

        }

    }


    /**
     * 同步Http请求
     */
    static class GetStnc2Task implements Runnable {

        private String url;
        private Map<String, String> params;
        private int timeout;
        private HttpClientEventListener httpClientEventListener;

        GetStnc2Task(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener, int timeout) {

            this.url = url;
            this.params = params;
            this.timeout = timeout;
            this.httpClientEventListener = httpClientEventListener;

        }

        @Override
        public void run() {
            getSync2(url, params, httpClientEventListener, timeout);
        }

    }


    /**
     * Http get 异步请求
     *
     * @param url
     * @param params
     * @param httpClientEventListener
     * @param timeout
     */
    static public void get2(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener, int timeout) {
        GetStnc2Task getStnc2Task = new GetStnc2Task(url, params, httpClientEventListener, timeout);
        new Thread(getStnc2Task).start();
    }

    /**
     * HttpGet 同步请求
     *
     * @param url
     * @param params
     * @param httpClientEventListener
     * @param timeout
     */
    static public void getSync2(String url, Map<String, String> params, final HttpClientEventListener httpClientEventListener, int timeout) {
        try {
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> param : params.entrySet()) {
                pairList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }

            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, "UTF-8");
            // URL使用基本URL即可，其中不需要加参数
            HttpGet httpGet = new HttpGet(url);
            // 将请求体内容加入请求中
            httpGet.setEntity(requestHttpEntity);

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout)       // 设置连接超时时间 5秒钟
                    .setSocketTimeout(timeout)        // 设置读取超时时间5秒钟
                    .build();
            httpGet.setConfig(config);

            // 需要客户端对象来发送请求
            HttpClient httpClient = new DefaultHttpClient();
            // 发送请求
            HttpResponse response = httpClient.execute(httpGet);

            String data = EntityUtils.toString(response.getEntity(), "UTF-8");

            httpClientEventListener.onSuccess(200, data.getBytes());

            Log.d("response", response.toString());

        } catch (Exception error) {

            if (error instanceof SocketException) {
                httpClientEventListener.onTimeout();
            } else if (error instanceof ConnectException) {
                httpClientEventListener.onTimeout();
            } else {
                httpClientEventListener.onFailure(100, null);
            }

        }
    }

    /**
     * 附件上传 同步的方式
     *
     * @param context
     * @param url
     * @param params
     * @param httpClientEventListener
     * @param timeout
     */
    static public void PostJsonSync(Context context, String url, Map<String, Object> params, final HttpClientEventListener httpClientEventListener, int timeout) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
            httppost.setConfig(requestConfig);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                Object val = param.getValue();
                if(val instanceof File){
                    builder.addPart(param.getKey(),new FileBody((File) val));
                }else{
                    builder.addPart(param.getKey(),new StringBody(param.getValue().toString(),ContentType.TEXT_PLAIN));
                }
            }
            HttpEntity reqEntity = builder.build();
            httppost.setEntity(reqEntity);
            long s = System.currentTimeMillis();
            Log.e("test","begin ->" + s);
            CloseableHttpResponse response = httpclient.execute(httppost);
            long e = System.currentTimeMillis();
            Log.e("test","time cost ->" + (e -s));
            try {
                HttpEntity resEntity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                Log.e("test","statusCode -> " + statusCode);
                if (statusCode == 200 && resEntity != null) {
                    String data = EntityUtils.toString(response.getEntity(), "UTF-8");
                    httpClientEventListener.onSuccess(200, data.getBytes());
                    Log.e("test","onSuccess --> " + data);
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (Exception error) {
            if (error instanceof SocketException) {
                httpClientEventListener.onTimeout();
            } else if (error instanceof ConnectException) {
                httpClientEventListener.onTimeout();
            } else {
                httpClientEventListener.onFailure(100, null);
            }
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                Log.e("test",e.getMessage(),e);
            }
            Log.e("test","上传成功");
        }
    }

}
