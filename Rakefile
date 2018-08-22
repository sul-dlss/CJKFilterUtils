require 'solr_wrapper'

desc 'Setup a local Solr using the build .jar for testing'
task :setup_server do
  SolrWrapper.wrap do |solr|
    FileUtils.cp File.join(__dir__, 'target', 'CJKFilterUtils-v2.1.jar'), File.join(solr.instance_dir, 'contrib')
    solr.with_collection(name: 'test') do
    end
  end
end

task :fixtures do
  system 'curl -X POST -H "Content-Type: application/json" "http://localhost:8983/solr/test/update/" --data-binary @example/fixtures.json'
  system 'curl http://localhost:8983/solr/test/update?stream.body=%3Ccommit/%3E'
end
