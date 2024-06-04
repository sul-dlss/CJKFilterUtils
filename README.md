# CJKFilterUtils

This is a Lucene filter and filter factory (see http://lucene.apache.org )
to fold certain CJK characters to improve recall.  You should put it in your
analysis chain BEFORE ICUTransforms from Traditional->Simplified Han, as it
converts modern Japanese Kanji to their traditional equivalents.

This is the YUL fork of https://github.com/sul-dlss/CJKFilterUtils

## YUL Customizations
YUL added some mappings. YUL mappings are in `src/main/resources/edu/stanford/lucene/analysis/variantmap.xml`

## TODO
Check tests with `@Ignore` to see if we should update our mappings.



## Usage

- clone the project
```bash
git clone git@github.com:yalelibrary/CJKFoldingFilter.git
cd CJKFoldingFilter
```

- run the maven installation
```bash
mvn clean install
```
- put the `CJKFilterUtils*.jar` file found in the target directory into your Solr lib directory
- utilize the Solr CJKFoldingFilterFactory in your schema.xml file.

 <fieldType name="text_cjk" class="solr.TextField" positionIncrementGap="10000" autoGeneratePhraseQueries="false">
   <analyzer>
   <charFilter class="edu.stanford.lucene.analysis.ICUTransformCharFilterFactory" id="Traditional-Simplified" />
     <tokenizer class="solr.ICUTokenizerFactory" />
     <filter class="solr.CJKWidthFilterFactory"/>
     <filter class="edu.stanford.lucene.analysis.CJKFoldingFilterFactory"/>
     <charFilter class="edu.stanford.lucene.analysis.ICUCustomTransformCharFilterFactory" id="edu/stanford/lucene/analysis/stanford_cjk_transliterations.txt" />
     <filter class="solr.ICUTransformFilterFactory" id="Traditional-Simplified"/>
     <filter class="solr.ICUTransformFilterFactory" id="Katakana-Hiragana"/>
     <filter class="solr.ICUFoldingFilterFactory"/>
     <filter class="solr.CJKBigramFilterFactory" han="true" hiragana="true" katakana="true" hangul="true" outputUnigrams="true" />
   </analyzer>
 </fieldType>
 
 ## Checking example locally
 
 (Uses Ruby)
 
 Install Ruby dependencies
 
 ```sh
 $ bundle install
 ```

Setup Solr with CJKFilterUtils and config/schema

```sh
$ bundle exec rake setup_server
```

Run solr_wrapper

```sh
$ solr_wrapper
```

In another shell, index fixtures

```sh
$ bundle exec rake fixtures
```

Run some queries (these should return results):

```sh
$ curl http://127.0.0.1:8983/solr/test/select?debugQuery=on&indent=on&q=cjk_test:呂思勉两晋南北朝&wt=json

$ curl http://127.0.0.1:8983/solr/test/select?debugQuery=on&indent=on&q=cjk_test:俞平伯红楼梦&wt=json

$ curl http://127.0.0.1:8983/solr/test/select?debugQuery=on&indent=on&q=cjk_test:南洋&wt=json

```
