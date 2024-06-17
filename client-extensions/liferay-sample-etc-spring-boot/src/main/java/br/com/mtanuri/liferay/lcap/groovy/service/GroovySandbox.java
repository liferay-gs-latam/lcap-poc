package br.com.mtanuri.liferay.lcap.groovy.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.stereotype.Service;

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
        private Map<String, Object> inputs;

        public ScriptExecutionBuilder(CompilerConfiguration config) {
            this.config = config;
        }

        public ScriptExecutionBuilder script(String script) {
            this.script = script;
            return this;
        }

        public ScriptExecutionBuilder inputs(Map<String, Object> inputs) {
            this.inputs = inputs;
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
