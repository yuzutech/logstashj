package org.yuzutech.logstash.internal;

import java.io.InputStream;

import org.jruby.Ruby;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.javasupport.JavaEmbedUtils;

class JRubyLogstashModuleFactory {

    protected RubyRuntimeAdapter evaler;
    private Ruby runtime;

    public JRubyLogstashModuleFactory(Ruby runtime) {
        this.runtime = runtime;
        this.evaler = JavaEmbedUtils.newRuntimeAdapter();
    }

    public LogstashModule createLogstashModule() {
        String script = loadLogstashRubyClass();
        evaler.eval(runtime, script);

        Object rfj = evaler.eval(runtime, "LogstashModule.new()");

        return RubyUtils.rubyToJava(runtime, (org.jruby.runtime.builtin.IRubyObject) rfj, LogstashModule.class);

    }

    private String loadLogstashRubyClass() {
        InputStream inputStream = JRubyLogstashModuleFactory.class.getResourceAsStream("logstashclass.rb");
        return IOUtils.readFull(inputStream);
    }
}
