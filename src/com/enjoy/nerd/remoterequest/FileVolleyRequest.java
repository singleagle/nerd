package com.enjoy.nerd.remoterequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

public class FileVolleyRequest extends StringRequest {
	private static final String TAG = "FileVolleyRequest";
	
    private InputStream mInputStream;
    public String mName;
    public String mContentType;
      
    public FileVolleyRequest(String url, Listener<String> listener, ErrorListener errorListener) {  
        super(Method.POST, url, listener, errorListener); 
        setShouldCache(false);
    }  
    


    /**
     * Adds a file to the request.
     *
     * @param key         the key name for the new param.
     * @param file        the file to add.
     * @param contentType the content type of the file, eg. application/json
     * @throws java.io.FileNotFoundException throws if wrong File argument was passed
     */
    public void setFileBody(File file, String contentType) throws FileNotFoundException {
    	setFileBody(new FileInputStream(file), contentType);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key    the key name for the new param.
     * @param stream the input stream to add.
     * @param name   the name of the stream.
     */
    public void setFileBody(InputStream stream, String name) {
    	setFileBody(stream, name, null);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key         the key name for the new param.
     * @param stream      the input stream to add.
     * @param name        the name of the stream.
     * @param contentType the content type of the file, eg. application/json
     */
    public void setFileBody(InputStream stream, String name, String contentType) {
    	mInputStream = stream;
    	mName = name;
    	mContentType = contentType;
    }
    
    
  
    @Override  
    public byte[] getBody() throws AuthFailureError {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  

        try {  
            final byte[] tmp = new byte[4096];
            int l;
            while ((l = mInputStream.read(tmp)) != -1) {
            	baos.write(tmp, 0, l);
            }
        } catch (IOException e) {  
            e.printStackTrace();  
            LogWrapper.d(TAG, "IOException writing to ByteArrayOutputStream");  
        }  
        
        LogWrapper.d(TAG, "body size is :" + baos.size()); 
        return baos.toByteArray();  
    }
    
    @Override  
    public String getBodyContentType() {  
    	if(mContentType == null){
    		return "application/octet-stream";
    	}
        return mContentType;  
    }  

}
