package edu.stanford.lucene.analysis;

import org.apache.lucene.tests.analysis.BaseTokenStreamFactoryTestCase;
import org.apache.lucene.util.ClasspathResourceLoader;
import org.apache.lucene.analysis.TokenStream;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class TestICUCustomTransformCharFilterFactory extends BaseTokenStreamFactoryTestCase {
    /** ensure the transform is working */
    public void testCJKStuff() throws Exception {
        String text = "両";
        Map<String,String> args = new HashMap<>();
        args.put("id", "stanford_cjk_transliterations.txt");
        ICUCustomTransformCharFilterFactory factory = new ICUCustomTransformCharFilterFactory(args);
        factory.inform(new ClasspathResourceLoader(getClass()));
        Reader cs = factory.create(new StringReader(text));
        TokenStream ts = whitespaceMockTokenizer(cs);

        assertTokenStreamContents(ts, new String[] { "兩" });
    }
}
