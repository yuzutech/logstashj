require 'java'
require 'logstash/agent'

require 'logstash/inputs/generator'
require 'logstash/filters/base'
require 'logstash/filters/mutate'
require 'logstash/outputs/stdout'
require 'logstash/pipeline'
require 'logstash/event'
require 'stud/trap'

class LogstashModule
  java_implements Java::LogstashModule

  def logstashVersion()
    args = ['--version']
    agent = LogStash::Agent.new('/bin/logstash', $0)
    agent.parse(args)
    agent.execute
  end

  def logstashAgent(config)
    args = ['-f', config]
    agent = LogStash::Agent.new('/bin/logstash agent', $0)
    agent.parse(args)
    agent.execute
  end

  def sample(config, sample_event)
    pipeline = LogStash::Pipeline.new(config)
    sample_event = {'message' => sample_event} if sample_event.is_a?(String)
    event = LogStash::Event.new(sample_event)

    results = []
    pipeline.instance_eval { @filters.each(&:register) }

    # filter call the block on all filtered events, included new events added by the filter
    pipeline.filter(event) { |filtered_event| results << filtered_event }

    # flush makes sure to empty any buffered events in the filter
    pipeline.flush_filters(:final => true) { |flushed_event| results << flushed_event }

    results.select { |e| !e.cancelled? }

    results.length > 1 ? results : results.first
  end
end
