package org.yuzutech.logstash;

import org.jruby.RubyObject;
import org.yuzutech.logstash.internal.JRubyLogstash;

import java.util.List;

public interface Logstash {

    /**
     * Output to the console the Logstash version which is being used..
     */
    void logstashVersion();

    /**
     * Run Logstash agent with the config file
     *
     * @param configPath path to the config
     */
    void logstashAgent(String configPath);


    /**
     * Send an event against the config
     * FIXME: Return a "Java" LogstashEvent
     *
     * @param config config
     * @param event event
     */
    RubyObject sample(String config, String event);

    /**
     * Factory for creating a new instance of Logstash interface.
     */
    public static class Factory {

        /**
         * Creates a new instance of Logstash.
         *
         * @return Logstash instance which uses JRuby to wraps Logstash Ruby calls.
         */
        public static Logstash create() {
            return JRubyLogstash.create();
        }

        /**
         * Creates a new instance of Logstash and sets GEM_PATH environment
         * variable to provided gemPath. This method is mostly used in OSGi
         * environments.
         *
         * @param gemPath where gems are located.
         * @return Logstash instance which uses JRuby to wraps Logstash Ruby calls.
         */
        public static Logstash create(String gemPath) {
            return JRubyLogstash.create(gemPath);
        }

        /**
         * Creates a new instance of Logstash and sets loadPath to provided paths. This method is mostly used in OSGi
         * environments.
         *
         * @param loadPaths where Ruby libraries are located.
         * @return Logstash instance which uses JRuby to wraps Logstash
         * Ruby calls.
         */
        public static Logstash create(List<String> loadPaths) {
            return JRubyLogstash.create(loadPaths);
        }

        /**
         * Creates a new instance of Logstash and sets a specific classloader for the  JRuby runtime to use. This method is mostly
         * used in environments where different threads may have different classloaders, like build tools sbt or ANT.
         *
         * @param classloader
         * @return Logstash instance which uses JRuby to wraps Logstash
         * Ruby calls.
         */
        public static Logstash create(ClassLoader classloader) {
            return JRubyLogstash.create(classloader);
        }

        /**
         * Cerates a new instance of Logstash and sets a specific classloader and gempath for the JRuby runtime to use.
         *
         * @param classloader
         * @param gemPath
         * @return Logstash instance which uses JRuby to wraps Logstash
         */
        public static Logstash create(ClassLoader classloader, String gemPath) {
            return JRubyLogstash.create(classloader, gemPath);
        }

    }


}
