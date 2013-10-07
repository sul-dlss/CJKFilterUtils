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

@Test
	public void testModernJapaneseToTrad() throws Exception
	{
		checkOneTerm(analyzer, "亜", "亞"); // 亜 U+4E9C =>（亞）U+4E9E
		checkOneTerm(analyzer, "悪", "惡"); // 悪 U+60AA =>（惡）U+60E1
		checkOneTerm(analyzer, "圧", "壓"); // 圧 U+5727 =>（壓）U+58D3
		checkOneTerm(analyzer, "囲", "圍"); // 囲 U+56F2 =>（圍）U+570D
		checkOneTerm(analyzer, "壱", "壹"); // 壱 U+58F1 =>（壹）U+58F9
		checkOneTerm(analyzer, "逸", "逸"); // 逸 U+9038 =>（逸）U+FA67
		checkOneTerm(analyzer, "隠", "隱"); // 隠 U+96A0 =>（隱）U+96B1
		checkOneTerm(analyzer, "栄", "榮"); // 栄 U+6804 =>（榮）U+69AE
		checkOneTerm(analyzer, "営", "營"); // 営 U+55B6 =>（營）U+71DF
		checkOneTerm(analyzer, "衛", "衞"); // 衛 U+885B =>（衞）U+885E
		checkOneTerm(analyzer, "駅", "驛"); // 駅 U+99C5 =>（驛）U+9A5B
		checkOneTerm(analyzer, "謁", "謁"); // 謁 U+8B01 =>（謁）U+FA62
		checkOneTerm(analyzer, "円", "圓"); // 円 U+5186 =>（圓）U+5713
		checkOneTerm(analyzer, "塩", "鹽"); // 塩 U+5869 =>（鹽）U+9E7D
		checkOneTerm(analyzer, "縁", "緣"); // 縁 U+7E01 =>（緣）U+7DE3
		checkOneTerm(analyzer, "応", "應"); // 応 U+5FDC =>（應）U+61C9
		checkOneTerm(analyzer, "桜", "櫻"); // 桜 685C =>（櫻) 6AFB
//		checkOneTerm(analyzer, "奥", "奧"); // 奥（奧）
//		checkOneTerm(analyzer, "横", "橫"); // 横（橫）
//		checkOneTerm(analyzer, "温", "溫"); // 温（溫）
//		checkOneTerm(analyzer, "穏", "穩"); // 穏（穩）
//		checkOneTerm(analyzer, "仮", "假"); // 仮（假）
//		checkOneTerm(analyzer, "価", "價"); // 価（價）
//		checkOneTerm(analyzer, "禍", "禍"); // 禍（禍）
//		checkOneTerm(analyzer, "画", "畫"); // 画（畫）
//		checkOneTerm(analyzer, "悔", "悔"); // 悔（悔）
//		checkOneTerm(analyzer, "海", "海"); // 海（海）
//		checkOneTerm(analyzer, "絵", "繪"); // 絵（繪）
//		checkOneTerm(analyzer, "壊", "壞"); // 壊（壞）
//		checkOneTerm(analyzer, "懐", "懷"); // 懐（懷）
//		checkOneTerm(analyzer, "慨", "慨"); // 慨（慨）

		//		checkOneTerm(analyzer, "", "");
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
		checkRandomData(random(), analyzer, 1000*RANDOM_MULTIPLIER);
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

	private Analyzer analyzer = new Analyzer() {
	    @Override
	    protected TokenStreamComponents createComponents(String field, Reader reader) {
	      final Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
	      final TokenStream stream = new CJKFoldingFilter(tokenizer);
	      return new TokenStreamComponents(tokenizer, stream);
	    }
	};



}
