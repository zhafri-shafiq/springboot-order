package com.usermanagement.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(NationalityRestTemplateService.class)
class NationalityRestTemplateServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NationalityRestTemplateService nationalityRestTemplateService;

    private MockRestServiceServer server;

    @BeforeEach
    public void init() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void givenServerIsMocked_whenPredictNationality_thenReceiveTheResponse() {
        //given
        this.server.expect(requestTo("https://api.nationalize.io?name=anshul"))
                .andRespond(withSuccess("{'name':'anshul','country':[{'country_id':'IN','probability':1}]}", MediaType.APPLICATION_JSON));

        //when
        String userServiceResponse = nationalityRestTemplateService.predictNationality("anshul");

        //then
        Assertions.assertEquals("{'name':'anshul','country':[{'country_id':'IN','probability':1}]}", userServiceResponse);
    }

}