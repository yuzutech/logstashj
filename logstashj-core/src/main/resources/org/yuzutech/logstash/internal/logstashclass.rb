require 'java'
require 'logstash/agent'

require 'logstash/inputs/generator'
require 'logstash/filters/base'
require 'logstash/filters/mutate'
require 'logstash/outputs/stdout'

class LogstashModule
  java_implements Java::LogstashModule

  def logstashVersion()
    args = ['--version']
    agent = LogStash::Agent.new('/bin/logstash', $0)
    agent.parse(args)
    return agent.execute
  end

  def logstashAgent(file_path)
    args = ['-f', file_path]
    agent = LogStash::Agent.new('/bin/logstash agent', $0)
    agent.parse(args)
    return agent.execute
  end
end