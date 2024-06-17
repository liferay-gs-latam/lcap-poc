package br.com.mtanuri.liferay.lcap.groovy.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import br.com.mtanuri.liferay.lcap.groovy.service.GroovySandbox;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.mtanuri.liferay.lcap.BaseRestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;


import org.json.JSONObject;


@RestController
@RequestMapping("/low-code")
public class LowCodeRestController extends BaseRestController{

    private final GroovySandbox groovySandbox;

    public LowCodeRestController(GroovySandbox groovySandbox) {
        this.groovySandbox = groovySandbox;
    }

    @PostMapping
    public ResponseEntity<String> execute(@AuthenticationPrincipal Jwt jwt, @RequestBody String jsonString) {

		log(jwt, _log, jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> json = new HashMap<>();
        
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            json.put(key, value);
        }

        Map<String, Object> inputs = new HashMap<>();
        inputs.put("objectEntry", json);

        Map<String, Object> execute = groovySandbox.scriptExecution()
                .script("a=123456")
                .inputs(inputs)
                .execute();


        return ResponseEntity.ok( execute.toString());
    }

	private static final Log _log = LogFactory.getLog(
		LowCodeRestController.class);

}