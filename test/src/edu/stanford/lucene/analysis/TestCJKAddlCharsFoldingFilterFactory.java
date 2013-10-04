package edu.stanford.lucene.analysis;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.analysis.util.BaseTokenStreamFactoryTestCase;

/**
 * Simple tests to ensure the CJKAddlCharsFoldingFilter factory is working.
 * @author Naomi Dushay
 */
public class TestCJKAddlCharsFoldingFilterFactory extends BaseTokenStreamFactoryTestCase
{
	public void testDefaults() throws Exception
	{
		Reader reader = new StringReader("多くの学生が試験に落ちた。");
		TokenStream stream = tokenizerFactory("standard").create(reader);
		stream = tokenFilterFactory("CJKAddlCharsFoldingFilter").create(stream);
		assertTokenStreamContents(stream, new String[] { "多く", "くの", "の学", "学生", "生が", "が試", "試験", "験に", "に落", "落ち", "ちた" });
	}

}
