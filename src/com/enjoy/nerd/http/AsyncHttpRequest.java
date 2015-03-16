/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.enjoy.nerd.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

/**
 * Internal class, representing the HttpRequest, done in asynchronous manner
 */
class AsyncHttpRequest implements Runnable {
    private final AbstractHttpClient client;
    private final HttpContext context;
    private HttpUriRequest request;
    private final ResponseHandlerInterface responseHandler;
    private int executionCount;


    public AsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request, ResponseHandlerInterface responseHandler) {
        this.client = client;
        this.context = context;
        this.request = request;
        this.responseHandler = responseHandler;
    }

    @Override
    public void run() {
        if (responseHandler != null) {
            responseHandler.sendStartMessage();
        }
        try {
            makeRequestWithRetries();
        } catch (IOException e) {
            if (responseHandler != null) {
                responseHandler.sendFailureMessage(0, null, null, e);
            } else {
                Log.e("AsyncHttpRequest", "makeRequestWithRetries returned error, but handler is null", e);
            }
        }

        if (responseHandler != null) {
            responseHandler.sendFinishMessage();
        }
    }

    private void makeRequest() throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            // Fixes #115
            if (request.getURI().getScheme() == null) {
                // subclass of IOException so processed in the caller
                throw new MalformedURLException("No valid URI scheme was provided");
            }
            
            HttpResponse response = client.execute(request, context);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
            	request.abort();
            	throw  new HttpResponseException(status.getStatusCode(), status.getReasonPhrase());
            }
            if (!Thread.currentThread().isInterrupted()) {
                if (responseHandler != null) {
                    responseHandler.sendResponseMessage(response);
                }
            }
        }
    }

    //private void make
    private void makeRequestWithRetries() throws IOException {
        boolean retry = true;
        IOException cause = null;
        executionCount=0;
        HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
        try {
            while (retry) {
                try {
                    makeRequest();
                    return;
                } catch (UnknownHostException e) {
                    // switching between WI-FI and mobile data networks can cause a retry which then results in an UnknownHostException
                    // while the WI-FI is initialising. The retry logic will be invoked here, if this is NOT the first retry
                    // (to assist in genuine cases of unknown host) which seems better than outright failure
                    cause = e;//new IOException("UnknownHostException exception: " + e.getMessage());
                    retry = retryHandler.retryRequest(cause, ++executionCount, context);
                } catch (NullPointerException e) {
                    // there's a bug in HttpClient 4.0.x that on some occasions causes
                    // DefaultRequestExecutor to throw an NPE, see
                    // http://code.google.com/p/android/issues/detail?id=5255
                    cause = new IOException("NPE in HttpClient: " + e.getMessage());
                    retry = retryHandler.retryRequest(cause, ++executionCount, context);
                } catch (IOException e) {
                    cause = e;
                    retry = retryHandler.retryRequest(cause, ++executionCount, context);
                }

                if (retry && responseHandler != null) {
                    responseHandler.sendRetryMessage(executionCount);
                }
               
            }
        } catch (Exception e) {
            // catch anything else to ensure failure message is propagated
            Log.e("AsyncHttpRequest", "Unhandled exception origin cause", e);
            cause = new IOException("Unhandled exception: " + e.getMessage());
        }

        // cleaned up to throw IOException
        throw (cause);
    }
}
