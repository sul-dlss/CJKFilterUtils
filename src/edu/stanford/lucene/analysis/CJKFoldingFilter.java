package edu.stanford.lucene.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.RamUsageEstimator;

/**
 * Maps equivalent CJK Unicode characters to a single Unicode character.
 * Meant to supplement ICUNormalization and ICU translation of Han traditional
 * <-> simplified, as there are variants of specific characters that are not
 * mapped by either ICU filter.
 * @author Naomi Dushay
 */
public class CJKFoldingFilter extends TokenFilter
{
	 private CharTermAttribute charTermAttr;
	 private char[] output = new char[512];
	 private int outputPos;

//	 private PositionIncrementAttribute posIncAttr;
//	 private Queue<char[]> terms;

	/**
	 * @param input
	 */
	protected CJKFoldingFilter(TokenStream input)
	{
		super(input);
		this.charTermAttr = addAttribute(CharTermAttribute.class);
//		this.posIncAttr = addAttribute(PositionIncrementAttribute.class);
//		this.terms = new LinkedList<char[]>();
	}


	@Override
	public final boolean incrementToken() throws IOException
	{
		if (input.incrementToken())
		{
			final char[] buffer = charTermAttr.buffer();
			final int length = charTermAttr.length();

			for(int i = 0 ; i < length ; ++i)
			{
				final char c = buffer[i];
				// If no characters actually require rewriting then we
				// just return token as-is:
				if (c >= '\u0080')
				{
					mapUnicode(buffer, length);
					charTermAttr.copyBuffer(output, 0, outputPos);
					break;
				}
			}
			return true;
		}

		return false;
	}

	/**
	* Maps Unicode characters per properties file.
	* @param input The string to fold
	* @param length The number of characters in the input string
	*/
	public void mapUnicode(char[] input, int length)
	{
		// Worst-case length required:
		final int maxSizeNeeded = 4 * length;
		if (output.length < maxSizeNeeded)
			output = new char[ArrayUtil.oversize(maxSizeNeeded, RamUsageEstimator.NUM_BYTES_CHAR)];

	    outputPos = mapUnicode(input, 0, output, 0, length);
	}

	/**
	 * Maps Unicode characters per properties file.
	 * @param input     The characters to map
	 * @param inputPos  Index of the first character to map
	 * @param output    The result of the mapping. Should be of size >= {@code length * 4}.
	 * @param outputPos Index of output where to put the result of the mapping
	 * @param length    The number of characters to map
	 * @return length of output
	 * @lucene.internal
	 */
	public static final int mapUnicode(char input[], int inputPos, char output[], int outputPos, int length)
	{
		final int end = inputPos + length;
		for (int pos = inputPos; pos < end ; ++pos)
		{
			final char c = input[pos];

			// Quick test: if it's not in range then just keep current character
			if (c < '\u0080')
			  output[outputPos++] = c;
			else
			{
				switch (c)
				{
					// Japanese variant modern chars -> traditional
					case '\u4E9C': // modern 亜
						output[outputPos++] = '\u4E9E';  // traditional 亞
						break;
					case '\u60AA': // modern 悪
						output[outputPos++] = '\u60E1';  // traditional 惡
						break;
					case '\u5727': // modern 圧
						output[outputPos++] = '\u58D3';  // traditional 壓
						break;
					case '\u56F2': // modern 囲
						output[outputPos++] = '\u570D';  // traditional 圍
						break;
					case '\u58F1': // modern 壱
						output[outputPos++] = '\u58F9';  // traditional 壹
						break;
					case '\u9038': // modern 逸
						output[outputPos++] = '\uFA67';  // traditional 逸
						break;
					case '\u96A0': // modern 隠
						output[outputPos++] = '\u96B1';  // traditional 隱
						break;
					case '\u6804': // modern 栄
						output[outputPos++] = '\u69AE';  // traditional 榮
						break;
					case '\u55B6': // modern 営
						output[outputPos++] = '\u71DF';  // traditional 營
						break;
					case '\u885B': // modern 衛
						output[outputPos++] = '\u885E';  // traditional 衞
						break;
					case '\u99C5': // modern 駅
						output[outputPos++] = '\u9A5B';  // traditional 驛
						break;
					case '\u8B01': // modern 謁
						output[outputPos++] = '\uFA62';  // traditional 謁
						break;
					case '\u5186': // modern 円
						output[outputPos++] = '\u5713';  // traditional 圓
						break;
					case '\u5869': // modern 塩
						output[outputPos++] = '\u9E7D';  // traditional 鹽
						break;
					case '\u7E01': // modern 縁
						output[outputPos++] = '\u7DE3';  // traditional 緣
						break;
					case '\u5FDC': // modern 応
						output[outputPos++] = '\u61C9';  // traditional 應
						break;
					case '\u685C': // modern 桜
						output[outputPos++] = '\u6AFB';  // traditional 櫻
						break;
					case '\u5965': // modern 奥
						output[outputPos++] = '\u5967'; // traditional 奧
						break;
					case '\u6A2A': // modern 横
						output[outputPos++] = '\u6A6B'; // traditional （橫
						break;
					case '\u6E29': // modern 温
						output[outputPos++] = '\u6EAB'; // traditional （溫）
						break;
					case '\u7A4F': // modern 穏
						output[outputPos++] = '\u7A69'; // traditional （穩）
						break;
					case '\u4EEE': // modern 仮
						output[outputPos++] = '\u5047'; // traditional （假）
						break;
					case '\u4FA1': // modern 価
						output[outputPos++] = '\u50F9'; // traditional （價）
						break;
					case '\u798D': // modern 禍
						output[outputPos++] = '\uFA52'; // traditional （禍）
						break;
					case '\u753B': // modern 画
						output[outputPos++] = '\u756B'; // traditional （畫）
						break;
					case '\u6094': // modern 悔
						output[outputPos++] = '\uFA3D'; // traditional （悔）
						break;
					case '\u6D77': // modern 海
						output[outputPos++] = '\uFA45'; // traditional （海）
						break;
					case '\u7D75': // modern 絵
						output[outputPos++] = '\u7E6A'; // traditional （繪）
						break;
					case '\u58CA': // modern 壊
						output[outputPos++] = '\u58DE'; // traditional （壞）
						break;
					case '\u61D0': // modern 懐
						output[outputPos++] = '\u61F7'; // traditional （懷）
						break;
					case '\u6168': // modern 慨
						output[outputPos++] = '\uFA3E'; // traditional （慨）
						break;
					case '\u62E1': // modern 拡
						output[outputPos++] = '\u64F4'; // traditional （擴）
						break;
					case '\u6BBB': // modern 殻
						output[outputPos++] = '\u6BBC'; // traditional （殼）
						break;
					case '\u899A': // modern 覚
						output[outputPos++] = '\u89BA'; // traditional （覺）
						break;
					case '\u697D': // modern 楽
						output[outputPos++] = '\u6A02'; // traditional （樂）
						break;
					case '\u559D': // modern 喝
						output[outputPos++] = '\uFA36'; // traditional （喝）
						break;
					case '\u6E07': // modern 渇
						output[outputPos++] = '\u6E34'; // traditional （渴）
						break;



					default:
						output[outputPos++] = c;
						break;
				}
			}
		}
		return outputPos;
	}

}
