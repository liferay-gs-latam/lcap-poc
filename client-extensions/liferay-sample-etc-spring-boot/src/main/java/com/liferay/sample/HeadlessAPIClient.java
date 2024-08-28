package com.liferay.sample;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import org.json.JSONObject;

@Service
public class HeadlessAPIClient{

    private final RestTemplate restTemplate;


    @Value("${com.liferay.lxc.dxp.mainDomain}")
	public String lxcDXPMainDomain;

	@Value("${com.liferay.lxc.dxp.server.protocol}")
	public String lxcDXPServerProtocol;
    
    public HeadlessAPIClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public JSONObject get(String path, String token){

        if(!path.startsWith("/"))
            path = "/"+path;

        String url = lxcDXPServerProtocol+"://"+ lxcDXPMainDomain + path;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return new JSONObject(restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        ).getBody());
    }


}