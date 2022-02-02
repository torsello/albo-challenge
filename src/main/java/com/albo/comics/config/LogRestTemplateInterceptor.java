package com.albo.comics.config;

import io.micrometer.core.instrument.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LogRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LogRestTemplateInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        traceRequest(request, body);
        StopWatch watch = new StopWatch();
        watch.start();
        ClientHttpResponse response = execution.execute(request, body);
        watch.stop();
        traceResponse(response, watch.getTotalTimeMillis());
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) {
        log.info("Request URI     : {}", request.getURI());
        log.info("Request Method  : {}", request.getMethod());
        log.info("Request Headers : {}", request.getHeaders());
        log.info("Request body    : {}", new String(body, StandardCharsets.UTF_8));
    }

    private void traceResponse(ClientHttpResponse response, long callTime) throws IOException {
        String body = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);

        log.info("Response Status code : {}", response.getStatusCode());
        log.info("Response Status text : {}", response.getStatusText());
        log.info("Response Headers     : {}", response.getHeaders());
        log.info("Response body        : {}", body);
        log.info("Response time        : {}ms", callTime);
    }
}
