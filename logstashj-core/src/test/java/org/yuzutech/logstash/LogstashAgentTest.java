package org.yuzutech.logstash;

import org.junit.Test;

public class LogstashAgentTest {

    @Test
    public void should_print_logstash_version() {
        Logstash.Factory.create().logstashVersion();
    }

    @Test
    public void should_run_logstash_agent() {
        Logstash.Factory.create().logstashAgent("test.conf");
    }
}
