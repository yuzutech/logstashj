package org.yuzutech.logstash.internal;

public interface LogstashModule {

    void logstashVersion();

    void logstashAgent(String filePath);

}
