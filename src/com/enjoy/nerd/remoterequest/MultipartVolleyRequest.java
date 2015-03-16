package com.enjoy.nerd.remoterequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.enjoy.nerd.utils.LogWrapper;

public class MultipartVolleyRequest extends StringRequest {
	private static final String TAG = "MultipartVolleyRequest";
	
	
    private HashMap<String, String> mStringParams;
    private HashMap<String, StreamWrapper> mStreamParams;
    private HashMap<String, FileWrapper> mFileParams; 
    private String mContentType;
      
    public MultipartVolleyRequest(String url, Listener<String> listener, ErrorListener errorListener) {  
        super(Method.POST, url, listener, errorListener); 
        setShouldCache(false);
    }  
    
    
    /**
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
        	mStringParams.put(key, value);
        }
    }

    /**
     * Adds a file to the request.
     *
     * @param key  the key name for the new param.
     * @param file the file to add.
     * @throws java.io.FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file) throws FileNotFoundException {
        put(key, file, null);
    }

    /**
     * Adds a file to the request.
     *
     * @param key         the key name for the new param.
     * @param file        the file to add.
     * @param contentType the content type of the file, eg. application/json
     * @throws java.io.FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file, String contentType) throws FileNotFoundException {
        if (key != null && file != null) {
            mFileParams.put(key, new FileWrapper(file, contentType));
        }
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key    the key name for the new param.
     * @param stream the input stream to add.
     */
    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key    the key name for the new param.
     * @param stream the input stream to add.
     * @param name   the name of the stream.
     */
    public void put(String key, InputStream stream, String name) {
        put(key, stream, name, null);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key         the key name for the new param.
     * @param stream      the input stream to add.
     * @param name        the name of the stream.
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, InputStream stream, String name, String contentType) {
        if (key != null && stream != null) {
            mStreamParams.put(key, new StreamWrapper(stream, name, contentType));
        }
    }
    private HttpEntity createFormEntity() {
        try {
            List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

            for (ConcurrentHashMap.Entry<String, String> entry : mStringParams.entrySet()) {
                lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            return new UrlEncodedFormEntity(lparams, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            return null; // Actually cannot happen when using utf-8
        }
    }
    
    private HttpEntity createMultipartEntity() throws IOException {
        SimpleMultipartEntity entity = new SimpleMultipartEntity();

        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : mStringParams.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }


        // Add stream params
        for (ConcurrentHashMap.Entry<String, StreamWrapper> entry : mStreamParams.entrySet()) {
            StreamWrapper stream = entry.getValue();
            if (stream.inputStream != null) {
                entity.addPart(entry.getKey(), stream.name, stream.inputStream,
                        stream.contentType);
            }
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : mFileParams.entrySet()) {
            FileWrapper fileWrapper = entry.getValue();
            entity.addPart(entry.getKey(), fileWrapper.file, fileWrapper.contentType);
        }

        return entity;
    }
    
  
    @Override  
    public byte[] getBody() throws AuthFailureError {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        HttpEntity httpEntity;

        try {  
            if(mStreamParams.isEmpty() && mFileParams.isEmpty()){
            	httpEntity = createFormEntity();
            }else{
            	httpEntity = createMultipartEntity();
            }
            mContentType = httpEntity.getContentType().getValue();
            httpEntity.writeTo(baos);  
        } catch (IOException e) {  
            e.printStackTrace();  
            LogWrapper.d(TAG, "IOException writing to ByteArrayOutputStream");  
        }  
        
        LogWrapper.d(TAG, "body size is :" + baos.size());  
        return baos.toByteArray();  
    }
    
    @Override  
    public String getBodyContentType() {  
        if(mContentType != null){
        	return mContentType;
        }
        return "multipart/form-data; boundary=XXXX";  
    }  
    
    
    private static class FileWrapper {
        public File file;
        public String contentType;

        public FileWrapper(File file, String contentType) {
            this.file = file;
            this.contentType = contentType;
        }
    }

    private static class StreamWrapper {
        public InputStream inputStream;
        public String name;
        public String contentType;

        public StreamWrapper(InputStream inputStream, String name, String contentType) {
            this.inputStream = inputStream;
            this.name = name;
            this.contentType = contentType;
        }
    }

}
