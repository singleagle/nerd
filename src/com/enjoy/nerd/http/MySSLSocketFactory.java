package com.enjoy.nerd.http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

/**
 * This file is introduced to fix HTTPS Post bug on API &lt; ICS see
 * http://code.google.com/p/android/issues/detail?id=13117#c14 <p>&nbsp;</p> Warning! This omits SSL
 * certificate validation on every device, use with caution
 */
public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore)
            throws GeneralSecurityException,NoSuchProviderException, IOException{
        super(truststore);
        TrustManager tm = new MyX509TrustManager();
        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    public static KeyStore getKeystore() {
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return trustStore;
    }

    public static SSLSocketFactory getFixedSocketFactory() {
        SSLSocketFactory socketFactory;
        try {
            socketFactory = new MySSLSocketFactory(getKeystore());
            //socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);        
        } catch (Throwable t) {
            t.printStackTrace();
            socketFactory = SSLSocketFactory.getSocketFactory();
        }
        //修复https漏洞
        if(socketFactory != null){
        	socketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        }
        return socketFactory;
    }
    
    private class MyX509TrustManager implements X509TrustManager {
    	private X509TrustManager mSunJSSEX509TrustManager;
    	
    	MyX509TrustManager() throws GeneralSecurityException,NoSuchProviderException, IOException {
    	       // create a "default" JSSE X509TrustManager.
    	       KeyStore ks = KeyStore.getInstance("JKS");
    	       ks.load(new FileInputStream("trustedCerts"),  "passphrase".toCharArray());
    	       TrustManagerFactory tmf =TrustManagerFactory.getInstance("SunX509", "SunJSSE");
    	       tmf.init(ks);
    	       TrustManager tms [] = tmf.getTrustManagers();
    	       
    	       for (int i = 0; i < tms.length; i++) {
    	           if (tms[i] instanceof X509TrustManager) {
    	        	   mSunJSSEX509TrustManager = (X509TrustManager) tms[i];
    	           }
    	       }
    	}
    	
    	
    	
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {   
        	
            try {
            	
            	mSunJSSEX509TrustManager.checkClientTrusted(chain, authType);
            }catch (CertificateException excep) {
        	   
           }
       }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
            try {
            	mSunJSSEX509TrustManager.checkServerTrusted(chain, authType);
            }catch (CertificateException excep) {
        	   
           }
        	
        }
    };

}