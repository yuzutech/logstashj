package org.yuzutech.logstash;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.jruby.RubyHash;
import org.jruby.RubyObject;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class LogstashAgentTest {

    @Test
    public void should_print_logstash_version() {
        Logstash.Factory.create().logstashVersion();
    }

    @Test
    public void should_run_logstash_agent() {
        Logstash.Factory.create().logstashAgent("complete_config.conf");
    }

    @Test
    public void should_send_sample_event() throws IOException {
        URL url = Resources.getResource(LogstashAgentTest.class, "filter_config.conf");
        String config = Resources.toString(url, Charsets.UTF_8);
        RubyObject result = Logstash.Factory.create().sample(config, "I want a DOUBLE cheese!");
        RubyHash hash = (RubyHash) result.varTable[1];
        assertThat(hash.get("message")).isEqualTo("i want a double cheese!");
    }
}
