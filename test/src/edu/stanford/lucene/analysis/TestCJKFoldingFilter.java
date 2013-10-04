package edu.stanford.lucene.analysis;

import java.io.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.junit.Test;

/**
 * Tests to ensure CJKFoldingFilter works properly
 * @author Naomi Dushay
 */
public class TestCJKFoldingFilter extends BaseTokenStreamTestCase
{

@Test
	public void testNonCJKtokens() throws Exception
	{
		TokenStream stream = new MockTokenizer(new StringReader
			("Des mot"), MockTokenizer.WHITESPACE, false);
		CJKFoldingFilter filter = new CJKFoldingFilter(stream);

		CharTermAttribute termAtt = filter.getAttribute(CharTermAttribute.class);
		filter.reset();
		assertTermEquals("Des", filter, termAtt);
		assertTermEquals("mot", filter, termAtt);
		assertFalse(filter.incrementToken());
	}



	void assertTermEquals(String expected, TokenStream stream, CharTermAttribute termAtt) throws Exception
	{
		assertTrue(stream.incrementToken());
		assertEquals(expected, termAtt.toString());
	}


	/** blast some random strings through the analyzer */
@Test
	public void testRandomStrings() throws Exception
	{
		Analyzer a = new Analyzer()
		{
			@Override
			protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
				Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
				return new TokenStreamComponents(tokenizer, new CJKFoldingFilter(tokenizer));
			}
		};
		checkRandomData(random(), a, 1000*RANDOM_MULTIPLIER);
	}

@Test
	public void testEmptyTerm() throws IOException
	{
		Analyzer a = new Analyzer()
		{
			@Override
			protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
				Tokenizer tokenizer = new KeywordTokenizer(reader);
				return new TokenStreamComponents(tokenizer, new CJKFoldingFilter(tokenizer));
			}
		};
		checkOneTermReuse(a, "", "");
	}

}
