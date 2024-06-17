package br.com.mtanuri.liferay.lcap.groovy.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class GroovyScriptEngineFactory {
    public static ScriptEngine createGroovyScriptEngine() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");

        if (engine == null) {
            throw new RuntimeException("Groovy Script Engine not found. Make sure you have the necessary dependencies.");
        }

        return engine;
    }
}
