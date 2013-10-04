package edu.stanford.lucene.analysis;

import java.io.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.junit.Test;

/**
 * @author Naomi Dushay
 *
 */
public class TestCJKAddlCharsFoldingFilter extends BaseTokenStreamTestCase
{

@Test
	public void nonCJKTest() throws Exception
	{
		TokenStream stream = new MockTokenizer(new StringReader
			("Des mot"), MockTokenizer.WHITESPACE, false);
		CJKAddlCharsFoldingFilter filter = new CJKAddlCharsFoldingFilter(stream);

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
	public void testRandomStrings() throws Exception
	{
		Analyzer a = new Analyzer()
		{
			@Override
			protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
				Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
				return new TokenStreamComponents(tokenizer, new CJKAddlCharsFoldingFilter(tokenizer));
			}
		};
		checkRandomData(random(), a, 1000*RANDOM_MULTIPLIER);
	}

	public void testEmptyTerm() throws IOException
	{
		Analyzer a = new Analyzer()
		{
			@Override
			protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
				Tokenizer tokenizer = new KeywordTokenizer(reader);
				return new TokenStreamComponents(tokenizer, new CJKAddlCharsFoldingFilter(tokenizer));
			}
		};
		checkOneTermReuse(a, "", "");
	}

}
