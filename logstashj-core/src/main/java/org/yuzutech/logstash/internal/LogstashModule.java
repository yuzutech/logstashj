package org.yuzutech.logstash.internal;

import org.jruby.RubyObject;

public interface LogstashModule {

    void logstashVersion();

    void logstashAgent(String configPath);

    RubyObject sample(String config, String event);
}
