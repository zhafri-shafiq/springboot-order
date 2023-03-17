package com.usermanagement.services;

import com.usermanagement.services.NationalityWebClientService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class NationalityWebClientServiceTest {

    private static MockWebServer webServer;

    private NationalityWebClientService nationalityService;

    @BeforeAll
    public static void init() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        webServer.shutdown();
    }

    @BeforeEach
    public void initialize() {
        nationalityService = new NationalityWebClientService();
    }

    @Test
    public void givenWebServerIsMocked_whenPredictNationality_thenReceiveTheResponse() throws InterruptedException {
        //access the web server locally at 127.0.0.1
        String baseUrl = String.format("http://127.0.0.1:%s", webServer.getPort());

        String expectedResp = "{'name':'anshul','country':[{'country_id':'IN','probability':1}]}";

        //given - enqueue the canned response for the local web server
        webServer.enqueue(new MockResponse()
                .setBody(expectedResp)
                .addHeader("Content-Type", "application/json"));

        //when
        Mono<String> stringMono = nationalityService.predictNationality(baseUrl, "anshul");

        //then
        StepVerifier.create(stringMono)
                .expectNextMatches(resp -> resp.toLowerCase().equals(expectedResp.toLowerCase()))
                .verifyComplete();

        //take the recorded request on the web server
        RecordedRequest recordedRequest = webServer.takeRequest();

        //assertions
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/?name=anshul", recordedRequest.getPath());
    }
}