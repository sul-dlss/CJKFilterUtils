package edu.stanford.lucene.analysis;

import com.ibm.icu.text.Transliterator;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class ICUCustomTransformCharFilterFactory extends CharFilterFactory implements MultiTermAwareComponent, ResourceLoaderAware {
    private Transliterator transliterator;
    private final String id;
    private final int dir;

    /** Creates a new ICUCustomTransformCharFilterFactory */
    public ICUCustomTransformCharFilterFactory(Map<String,String> args) {
        super(args);
        id = require(args, "id");
        String direction = get(args, "direction", Arrays.asList("forward", "reverse"), "forward", false);
        dir = "forward".equals(direction) ? Transliterator.FORWARD : Transliterator.REVERSE;
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }

    @Override
    public Reader create(Reader input) {
        return transliterator == null ? input : new ICUTransformCharFilter(input, transliterator);
    }

    @Override
    public AbstractAnalysisFactory getMultiTermComponent() {
        return this;
    }

    @Override
    public void inform(ResourceLoader loader) throws IOException {
        if(id != null) {
            InputStream stream = loader.openResource(id.trim());
            String rules = IOUtils.toString(stream, StandardCharsets.UTF_8);
            transliterator = Transliterator.createFromRules(id, rules, dir);
        }
    }
}
