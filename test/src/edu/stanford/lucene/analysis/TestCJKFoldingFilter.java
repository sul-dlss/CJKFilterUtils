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
		checkOneTerm(analyzer, "奥", "奧"); // 奥 U+5965 =>（奧）U+5967
		checkOneTerm(analyzer, "横", "橫"); // 横 6A2A =>（橫） 6A6B
		checkOneTerm(analyzer, "温", "溫"); // 温 6E29 =>（溫） 6EAB
		checkOneTerm(analyzer, "穏", "穩"); // 穏 7A4F =>（穩） 7A69
		checkOneTerm(analyzer, "仮", "假"); // 仮 4EEE =>（假）5047
		checkOneTerm(analyzer, "価", "價"); // 価 4FA1 =>（價）50F9
		checkOneTerm(analyzer, "禍", "禍"); // 禍 798D =>（禍） FA52
		checkOneTerm(analyzer, "画", "畫"); // 画 753B =>（畫） 756B
		checkOneTerm(analyzer, "悔", "悔"); // 悔 6094 =>（悔） FA3D
		checkOneTerm(analyzer, "海", "海"); // 海 6D77 =>（海） FA45
		checkOneTerm(analyzer, "絵", "繪"); // 絵 7D75 =>（繪） 7E6A
		checkOneTerm(analyzer, "壊", "壞"); // 壊 58CA =>（壞） 58DE
		checkOneTerm(analyzer, "懐", "懷"); // 懐 61D0 =>（懷） 61F7
		checkOneTerm(analyzer, "慨", "慨"); // 慨 6168 =>（慨） FA3E
		checkOneTerm(analyzer, "拡", "擴"); // 拡 62E1 =>（擴） 64F4
		checkOneTerm(analyzer, "殻", "殼"); // 殻 6BBB =>（殼） 6BBC
		checkOneTerm(analyzer, "覚", "覺"); // 覚 899A =>（覺） 89BA
		checkOneTerm(analyzer, "楽", "樂"); // 楽 697D =>（樂） 6A02
		checkOneTerm(analyzer, "喝", "喝"); // 喝 559D =>（喝） FA36
		checkOneTerm(analyzer, "渇", "渴"); // 渇 6E07 =>（渴） 6E34

		//		checkOneTerm(analyzer, "", "");
	}



	void assertTermEquals(String expected, TokenStream stream, CharTermAttribute termAtt) throws Exception
	{
		assertTrue(stream.incrementToken());
		assertEquals(expected, termAtt.toString());
	}


// FIXME: this fails sometimes in undetermined ways, presumably because random chars sometimes include the ones we change
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
