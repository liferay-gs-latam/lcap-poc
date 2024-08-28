package com.liferay.sample;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;


import java.util.Map;

@Service
public class GroovySandbox {

    private final CompilerConfiguration config;

    public GroovySandbox() {
        config = new CompilerConfiguration();
        config.setScriptBaseClass("br.com.mtanuri.liferay.lcap.script.groovy.BaseScript");
    }

    public static class ScriptExecutionBuilder {
        private final CompilerConfiguration config;
        private String script;

        public ScriptExecutionBuilder(CompilerConfiguration config) {
            this.config = config;
        }

        public ScriptExecutionBuilder script(String script) {
            this.script = script;
            return this;
        }

        private Map<String, Object> inputs = new HashMap<>();

        public ScriptExecutionBuilder inputs(Map<String, Object> inputs) {
            this.inputs = new HashMap<>(inputs);
            return this;
        }

        public ScriptExecutionBuilder inputs(String key, JSONObject jsonObject) {
            Map<String, Object> jsonMap = jsonObject.keySet().stream()
                .collect(Collectors.toMap(
                    k -> k,
                    k -> jsonObject.get(k)
                ));

            if (this.inputs == null) {
                this.inputs = new HashMap<>();
            }

            this.inputs.put(key, jsonMap);
            
            return this;
        }

        public Map<String, Object> execute() {
            Binding binding = new Binding(inputs);
            GroovyShell shell = new GroovyShell(binding, config);
            shell.evaluate(script);
            return binding.getVariables();
        }
    }

    public ScriptExecutionBuilder scriptExecution() {
        return new ScriptExecutionBuilder(config);
    }
}
