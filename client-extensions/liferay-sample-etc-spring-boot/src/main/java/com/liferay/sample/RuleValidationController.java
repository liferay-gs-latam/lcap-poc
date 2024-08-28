package com.liferay.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.http.HttpStatus;

import com.liferay.sample.GroovySandbox;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.liferay.sample.BaseRestController;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Value;


@RestController
@RequestMapping("/rule-validation")
public class RuleValidationController extends BaseRestController{

    private final GroovySandbox groovySandbox;
    private final HeadlessAPIClient headlessAPIClient;

    public RuleValidationController(GroovySandbox groovySandbox, HeadlessAPIClient headlessAPIClient) {
        this.groovySandbox = groovySandbox;
        this.headlessAPIClient = headlessAPIClient;
    }

    @PostMapping
    public ResponseEntity<String> post(@AuthenticationPrincipal Jwt jwt, @RequestBody String jsonString) {

		log(jwt, _log, jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);
        String objectDefinitionERC = jsonObject.getJSONObject("objectDefinitionERC").getString("key");
        String path = groovyRepositoryPath+"?filter=objectDefinitionERC eq '" + objectDefinitionERC + "'";
        JSONObject headlessResponse = headlessAPIClient.get(path, jwt.getTokenValue());
        JSONArray jsonArray = headlessResponse.getJSONArray("items");

        boolean validationCriteriaMet = true;
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String groovyScript = item.getString("groovyScript");
            
            Map<String, Object> scriptResult = groovySandbox.scriptExecution()
                    .inputs("objectEntry", jsonObject)
                    .script(groovyScript)
                    .execute();

            if( (Boolean) scriptResult.get("result") == false){
                validationCriteriaMet = false;
                break;
            }
        
        }

        jsonObject.put("validationCriteriaMet", validationCriteriaMet);
        _log.info(jsonObject.toString());

		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

	private static final Log _log = LogFactory.getLog(
		RuleValidationController.class);

    @Value("${rules.groovy.repository.path}")
	private String groovyRepositoryPath;

}