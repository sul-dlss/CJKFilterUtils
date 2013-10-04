package edu.stanford.lucene.analysis;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.analysis.util.BaseTokenStreamFactoryTestCase;

/**
 * Simple tests to ensure the CJKFoldingFilter factory is working.
 * @author Naomi Dushay
 */
public class TestCJKFoldingFilterFactory extends BaseTokenStreamFactoryTestCase
{
	public void testNonCJK() throws Exception
	{
		Reader reader = new StringReader("mahler is the bomb");
		TokenStream stream = tokenizerFactory("standard").create(reader);
		CJKFoldingFilterFactory factory = new CJKFoldingFilterFactory(new HashMap<String,String>());
		stream = factory.create(stream);
		assertTokenStreamContents(stream, new String[] { "mahler", "is", "the", "bomb" });
	}

	public void testCJKUnfolded() throws Exception
	{
		Reader reader = new StringReader("多くの学生が試験に落ちた。");
		TokenStream stream = tokenizerFactory("standard").create(reader);
		CJKFoldingFilterFactory factory = new CJKFoldingFilterFactory(new HashMap<String,String>());
		stream = factory.create(stream);
		assertTokenStreamContents(stream, new String[] { "多", "く", "の", "学", "生", "が", "試", "験", "に", "落", "ち", "た" });
	}

	public void testCJKFolded() throws Exception
	{
		Reader reader = new StringReader("多くの学生が試験に落ちた。");
		TokenStream stream = tokenizerFactory("standard").create(reader);
		CJKFoldingFilterFactory factory = new CJKFoldingFilterFactory(new HashMap<String,String>());
		stream = factory.create(stream);
		assertTokenStreamContents(stream, new String[] { "多く", "くの", "の学", "学生", "生が", "が試", "試験", "験に", "に落", "落ち", "ちた" });
	}
}
