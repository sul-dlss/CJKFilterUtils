package edu.stanford.lucene.analysis;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class TestICUCustomTransformCharFilterFactory extends BaseTokenStreamTestCase {
    /** ensure the transform is working */
    public void testCJKStuff() throws Exception {
        Reader reader = new StringReader("両");
        Map<String,String> args = new HashMap<>();
        args.put("id", "stanford_cjk_transliterations.txt");
        ICUCustomTransformCharFilterFactory factory = new ICUCustomTransformCharFilterFactory(args);
        factory.inform(new ClasspathResourceLoader(getClass()));
        reader = factory.create(reader);
        TokenStream stream = whitespaceMockTokenizer(reader);
        assertTokenStreamContents(stream, new String[] { "兩" });
    }
}
