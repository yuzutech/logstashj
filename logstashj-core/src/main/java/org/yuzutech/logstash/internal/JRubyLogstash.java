package org.yuzutech.logstash.internal;

import org.jruby.RubyObject;
import org.yuzutech.logstash.Logstash;
import org.jruby.CompatVersion;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.RubyInstanceConfig.CompileMode;
import org.jruby.embed.ScriptingContainer;
import org.jruby.javasupport.JavaEmbedUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JRubyLogstash implements Logstash {

    private static final String GEM_PATH = "GEM_PATH";

    private LogstashModule logstashModule;
    protected Ruby rubyRuntime;

    private JRubyLogstash(LogstashModule logstashModule, Ruby rubyRuntime) {
        super();
        this.logstashModule = logstashModule;
        this.rubyRuntime = rubyRuntime;
    }

    public static Logstash create() {
        Map<String, Object> env = new HashMap<String, Object>();
        return createJRubyLogstashInstance(env);
    }

    public static Logstash create(String gemPath) {
        Map<String, Object> env = new HashMap<String, Object>();
        // a null value will clear the GEM_PATH and GEM_HOME
        env.put(GEM_PATH, gemPath);

        return createJRubyLogstashInstance(env);
    }

    public static Logstash create(List<String> loadPaths) {
        return createJRubyLogstashInstance(loadPaths);
    }

    public static Logstash create(ClassLoader classloader) {
        return createJRubyLogstashInstance(classloader, null);
    }

    public static Logstash create(ClassLoader classloader, String gemPath) {
        return createJRubyLogstashInstance(classloader, gemPath);
    }

    private static Logstash createJRubyLogstashInstance(List<String> loadPaths) {

        RubyInstanceConfig config = createOptimizedConfiguration();

        Ruby rubyRuntime = JavaEmbedUtils.initialize(loadPaths, config);
        JRubyLogstashModuleFactory jRubyLogstashModuleFactory = new JRubyLogstashModuleFactory(rubyRuntime);

        LogstashModule logstashModule = jRubyLogstashModuleFactory.createLogstashModule();

        return new JRubyLogstash(logstashModule, rubyRuntime);
    }

    private static Logstash createJRubyLogstashInstance(Map<String, Object> environmentVars) {

        RubyInstanceConfig config = createOptimizedConfiguration();
        injectEnvironmentVariables(config, environmentVars);

        Ruby rubyRuntime = JavaEmbedUtils.initialize(Collections.EMPTY_LIST, config);
        JRubyLogstashModuleFactory jRubyLogstashModuleFactory = new JRubyLogstashModuleFactory(rubyRuntime);

        LogstashModule logstashModule = jRubyLogstashModuleFactory.createLogstashModule();

        return new JRubyLogstash(logstashModule, rubyRuntime);
    }

    private static Logstash createJRubyLogstashInstance(ClassLoader classloader, String gemPath) {

        Map<String, Object> env = new HashMap<String, Object>();
        env.put(GEM_PATH, gemPath);

        ScriptingContainer container = new ScriptingContainer();
        injectEnvironmentVariables(container.getProvider().getRubyInstanceConfig(), env);
        container.setClassLoader(classloader);
        Ruby rubyRuntime = container.getProvider().getRuntime();

        JRubyLogstashModuleFactory jRubyLogstashModuleFactory = new JRubyLogstashModuleFactory(rubyRuntime);

        LogstashModule logstashModule = jRubyLogstashModuleFactory.createLogstashModule();

        return new JRubyLogstash(logstashModule, rubyRuntime);
    }

    private static void injectEnvironmentVariables(RubyInstanceConfig config, Map<String, Object> environmentVars) {
        EnvironmentInjector environmentInjector = new EnvironmentInjector(config);
        environmentInjector.inject(environmentVars);
    }

    private static RubyInstanceConfig createOptimizedConfiguration() {
        RubyInstanceConfig config = new RubyInstanceConfig();
        config.setCompatVersion(CompatVersion.RUBY2_0);
        config.setCompileMode(CompileMode.OFF);

        return config;
    }

    @Override
    public void logstashVersion() {
        this.logstashModule.logstashVersion();
    }

    @Override
    public void logstashAgent(String filePath) {
        this.logstashModule.logstashAgent(filePath);
    }

    @Override
    public RubyObject sample(String config, String event) {
        return this.logstashModule.sample(config, event);
    }
}
