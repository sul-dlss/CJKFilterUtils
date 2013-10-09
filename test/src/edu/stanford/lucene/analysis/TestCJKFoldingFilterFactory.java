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
		Reader reader = new StringReader("多の学生が試に落ちた。");
		TokenStream stream = tokenizerFactory("standard").create(reader);
		CJKFoldingFilterFactory factory = new CJKFoldingFilterFactory(new HashMap<String,String>());
		stream = factory.create(stream);
		assertTokenStreamContents(stream, new String[] { "多", "の", "学", "生", "が", "試", "に", "落", "ち", "た" });
	}

	public void testCJKFolded() throws Exception
	{
		Reader reader = new StringReader("亜亞悪惡梅梅応應foo");
		TokenStream stream = tokenizerFactory("standard").create(reader);
		CJKFoldingFilterFactory factory = new CJKFoldingFilterFactory(new HashMap<String,String>());
		stream = factory.create(stream);
		assertTokenStreamContents(stream, new String[] { "亞", "亞", "惡", "惡", "梅", "梅", "應", "應", "foo" });
	}
}
