require 'solr_wrapper'

desc 'Setup a local Solr using the build .jar for testing'
task :setup_server do
  `mvn clean package`
  version = `unzip -q -c target/CJKFilterUtils-*.jar META-INF/maven/edu.stanford/CJKFilterUtils/pom.properties`
            .split("\n").select{ |s| s =~ /^version/ }.first.split('=').last
  SolrWrapper.wrap do |solr|
    FileUtils.cp(
      File.join(__dir__, 'target', "CJKFilterUtils-#{version}.jar"),
      File.join(solr.instance_dir, 'contrib')
    )
    solr.with_collection(name: 'test') do
    end
  end
end

task :fixtures do
  system 'curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/test/update/" --data-binary @example/fixtures.json'
  system 'curl http://localhost:8983/solr/test/update?stream.body=%3Ccommit/%3E'
end
