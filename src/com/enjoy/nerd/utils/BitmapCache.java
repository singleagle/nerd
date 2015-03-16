package com.enjoy.nerd.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {    
    private LruCache<String, Bitmap> mCache;   
    private static final int MAX_MEM_CACHE_SIZE = 5 * 1024 * 1024; 
        
    public BitmapCache() {    
           
        mCache = new LruCache<String, Bitmap>(MAX_MEM_CACHE_SIZE) {    
            @Override    
            protected int sizeOf(String key, Bitmap value) {    
                return value.getRowBytes() * value.getHeight();    
            }    
                
        };    
    }    
    
    @Override    
    public Bitmap getBitmap(String url) {    
        return mCache.get(url);    
    }    
    
    @Override    
    public void putBitmap(String url, Bitmap bitmap) {    
        mCache.put(url, bitmap);    
    }    
    
} 