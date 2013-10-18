package edu.stanford.lucene.analysis;

import java.io.IOException;
import java.util.*;

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
	 private final CharTermAttribute charTermAttr = addAttribute(CharTermAttribute.class);
	 private char[] output = new char[512];
	 private int outputPos;

	/**
	 * @param input
	 */
	protected CJKFoldingFilter(TokenStream input)
	{
		super(input);
	}


	@Override
	public final boolean incrementToken() throws IOException
	{
		if (input.incrementToken())
		{
			final char[] buffer = charTermAttr.buffer();
			final int bufferLen = charTermAttr.length();
			for (int i = 0 ; i < bufferLen ; ++i)
			{
				final char c = buffer[i];
				// If no characters actually require rewriting then we
				// just return token as-is:
				if (c >= '\u3000')
				{
					mapUnicode(buffer, bufferLen);
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
			if (c < '\u3000')
				output[outputPos++] = c;
			else
			{
				// check for Japanese Modern chars that aren't the same as Han Simplified
				if (variant2Trad.containsKey(String.valueOf(c)))
				{
					String mapped = variant2Trad.get(String.valueOf(c));
					int mappedLen = mapped.length();
					for (int i = 0; i < mappedLen; i++)
					{
						output[outputPos++] = mapped.charAt(i);
					}
				}
				else
					output[outputPos++] = c;
			}
		}
		return outputPos;
	}

	private static AbstractMap<String, String> variant2Trad = new HashMap<String, String>();
	{
		variant2Trad.put(String.valueOf('\u4E21'), String.valueOf('\u5169')); // modern 両 => trad 兩
		variant2Trad.put(String.valueOf('\u4E26'), String.valueOf('\u7ADD')); // modern 並 => trad 竝
		variant2Trad.put(String.valueOf('\u4E57'), String.valueOf('\u4E58')); // modern 乗 => trad 乘
		variant2Trad.put(String.valueOf('\u4E88'), String.valueOf('\u8C6B')); // modern 予 => trad 豫
		variant2Trad.put(String.valueOf('\u4E9C'), String.valueOf('\u4E9E')); // modern 亜 => trad 亞
		variant2Trad.put(String.valueOf('\u4FA1'), String.valueOf('\u50F9')); // modern 価 => trad 價
		variant2Trad.put(String.valueOf('\u4ECF'), String.valueOf('\u4F5B')); // modern 仏 => trad 佛
		variant2Trad.put(String.valueOf('\u4EEE'), String.valueOf('\u5047')); // modern 仮 => trad 假
		variant2Trad.put(String.valueOf('\u4F1D'), String.valueOf('\u50B3')); // modern 伝 => trad 傳
		variant2Trad.put(String.valueOf('\u4F75'), String.valueOf('\u5002')); // modern 併 => trad 倂
		variant2Trad.put(String.valueOf('\u4FAE'), String.valueOf('\uFA30')); // modern 侮 => trad 侮

		variant2Trad.put(String.valueOf('\u5039'), String.valueOf('\u5109')); // modern 倹 => trad 儉
		variant2Trad.put(String.valueOf('\u507D'), String.valueOf('\u50DE')); // modern 偽 => trad 僞
		variant2Trad.put(String.valueOf('\u514D'), String.valueOf('\uFA32')); // modern 免 => trad 免
		variant2Trad.put(String.valueOf('\u5150'), String.valueOf('\u5152')); // modern 児 => trad 兒
		variant2Trad.put(String.valueOf('\u5186'), String.valueOf('\u5713')); // modern 円 => trad 圓
		variant2Trad.put(String.valueOf('\u51E6'), String.valueOf('\u8655')); // modern 処 => trad 處
		variant2Trad.put(String.valueOf('\u520A'), String.valueOf('\u520B')); // modern 刊 => trad 刋
		variant2Trad.put(String.valueOf('\u5263'), String.valueOf('\u528D')); // modern 剣 => trad 劍
		variant2Trad.put(String.valueOf('\u5264'), String.valueOf('\u5291')); // modern 剤 => trad 劑
		variant2Trad.put(String.valueOf('\u5270'), String.valueOf('\u5269')); // modern 剰 => trad 剩
		variant2Trad.put(String.valueOf('\u52B1'), String.valueOf('\u52F5')); // modern 励 => trad 勵
		variant2Trad.put(String.valueOf('\u52B4'), String.valueOf('\u52DE')); // modern 労 => trad 勞
		variant2Trad.put(String.valueOf('\u52B9'), String.valueOf('\u6548')); // modern 効 => trad 效
		variant2Trad.put(String.valueOf('\u52C9'), String.valueOf('\uFA33')); // modern 勉 => trad 勉
		variant2Trad.put(String.valueOf('\u52E4'), String.valueOf('\uFA34')); // modern 勤 => trad 勤
		variant2Trad.put(String.valueOf('\u52E7'), String.valueOf('\u52F8')); // modern 勧 => trad 勸
		variant2Trad.put(String.valueOf('\u52F2'), String.valueOf('\u52F3')); // modern 勲 => trad 勳
		variant2Trad.put(String.valueOf('\u5351'), String.valueOf('\uFA35')); // modern 卑 => trad 卑
		variant2Trad.put(String.valueOf('\u5358'), String.valueOf('\u55AE')); // modern 単 => trad 單
		variant2Trad.put(String.valueOf('\u5373'), String.valueOf('\u537D')); // modern 即 => trad 卽
		variant2Trad.put(String.valueOf('\u53B3'), String.valueOf('\u56B4')); // modern 厳 => trad 嚴
		variant2Trad.put(String.valueOf('\u53CE'), String.valueOf('\u6536')); // modern 収 => trad 收
		variant2Trad.put(String.valueOf('\u53D9'), String.valueOf('\u654D')); // modern 叙 => trad 敍
		variant2Trad.put(String.valueOf('\u559D'), String.valueOf('\uFA36')); // modern 喝 => trad 喝
		variant2Trad.put(String.valueOf('\u55B6'), String.valueOf('\u71DF')); // modern 営 => trad 營
		variant2Trad.put(String.valueOf('\u5606'), String.valueOf('\uFA37')); // modern 嘆 => trad 嘆
		variant2Trad.put(String.valueOf('\u5668'), String.valueOf('\uFA38')); // modern 器 => trad 器
		variant2Trad.put(String.valueOf('\u56E3'), String.valueOf('\u5718')); // modern 団 => trad 團
		variant2Trad.put(String.valueOf('\u56EF'), String.valueOf('\u570B')); // variant 囯 => trad 國
		variant2Trad.put(String.valueOf('\u56F2'), String.valueOf('\u570D')); // modern 囲 => trad 圍
		variant2Trad.put(String.valueOf('\u56F3'), String.valueOf('\u5716')); // modern 図 => trad 圖
		variant2Trad.put(String.valueOf('\u570F'), String.valueOf('\u5708')); // modern 圏 => trad 圈
		variant2Trad.put(String.valueOf('\u5727'), String.valueOf('\u58D3')); // modern 圧 => trad 壓
		variant2Trad.put(String.valueOf('\u5840'), String.valueOf('\uFA39')); // modern 塀 => trad 塀
		variant2Trad.put(String.valueOf('\u5841'), String.valueOf('\u58D8')); // modern 塁 => trad 壘
		variant2Trad.put(String.valueOf('\u585A'), String.valueOf('\uFA10')); // modern 塚 => trad 塚
		variant2Trad.put(String.valueOf('\u5869'), String.valueOf('\u9E7D')); // modern 塩 => trad 鹽
		variant2Trad.put(String.valueOf('\u5897'), String.valueOf('\u589E')); // modern 増 => trad 增
		variant2Trad.put(String.valueOf('\u58A8'), String.valueOf('\uFA3A')); // modern 墨 => trad 墨
		variant2Trad.put(String.valueOf('\u58CA'), String.valueOf('\u58DE')); // modern 壊 => trad 壞
		variant2Trad.put(String.valueOf('\u58CC'), String.valueOf('\u58E4')); // modern 壌 => trad 壤
		variant2Trad.put(String.valueOf('\u58F1'), String.valueOf('\u58F9')); // modern 壱 => trad 壹
		variant2Trad.put(String.valueOf('\u58F2'), String.valueOf('\u8CE3')); // modern 売 => trad 賣
		variant2Trad.put(String.valueOf('\u5909'), String.valueOf('\u8B8A')); // modern 変 => trad 變
		variant2Trad.put(String.valueOf('\u5965'), String.valueOf('\u5967')); // modern 奥 => trad 奧
		variant2Trad.put(String.valueOf('\u5968'), String.valueOf('\u596C')); // modern 奨 => trad 奬
		variant2Trad.put(String.valueOf('\u5B22'), String.valueOf('\u5B43')); // modern 嬢 => trad 孃
		variant2Trad.put(String.valueOf('\u5B9F'), String.valueOf('\u5BE6')); // modern 実 => trad 實
		variant2Trad.put(String.valueOf('\u5BDB'), String.valueOf('\u5BEC')); // modern 寛 => trad 寬
		variant2Trad.put(String.valueOf('\u5BFE'), String.valueOf('\u5C0D')); // modern 対 => trad 對
		variant2Trad.put(String.valueOf('\u5C02'), String.valueOf('\u5C08')); // modern 専 => trad 專
		variant2Trad.put(String.valueOf('\u5C4A'), String.valueOf('\u5C46')); // modern 届 => trad 屆
		variant2Trad.put(String.valueOf('\u5C64'), String.valueOf('\uFA3B')); // modern 層 => trad 層
		variant2Trad.put(String.valueOf('\u5DBD'), String.valueOf('\u5CB3')); // trad 嶽 => simp 岳 (not in ICU translation)
		variant2Trad.put(String.valueOf('\u5DDE'), String.valueOf('\u6D32')); // modern 州 => trad 洲
		variant2Trad.put(String.valueOf('\u5DE3'), String.valueOf('\u5DE2')); // modern 巣 => trad 巢
		variant2Trad.put(String.valueOf('\u5DFB'), String.valueOf('\u5377')); // modern 巻 => trad 卷
		variant2Trad.put(String.valueOf('\u5E2F'), String.valueOf('\u5E36')); // modern 帯 => trad 帶
		variant2Trad.put(String.valueOf('\u5E30'), String.valueOf('\u6B78')); // modern 帰 => trad 歸
		variant2Trad.put(String.valueOf('\u5E81'), String.valueOf('\u5EF3')); // modern 庁 => trad 廳
		variant2Trad.put(String.valueOf('\u5E83'), String.valueOf('\u5EE3')); // modern 広 => trad 廣
		variant2Trad.put(String.valueOf('\u5EC3'), String.valueOf('\u5EE2')); // modern 廃 => trad 廢
		variant2Trad.put(String.valueOf('\u5ECA'), String.valueOf('\uF928')); // modern 廊 => trad 廊
		variant2Trad.put(String.valueOf('\u5F10'), String.valueOf('\u8CB3')); // modern 弐 => trad 貳
		variant2Trad.put(String.valueOf('\u5F3E'), String.valueOf('\u5F48')); // modern 弾 => trad 彈
		variant2Trad.put(String.valueOf('\u5F93'), String.valueOf('\u5F9E')); // modern 従 => trad 從
		variant2Trad.put(String.valueOf('\u5FB3'), String.valueOf('\u5FB7')); // modern 徳 => trad 德
		variant2Trad.put(String.valueOf('\u5FB4'), String.valueOf('\u5FB5')); // modern 徴 => trad 徵
		variant2Trad.put(String.valueOf('\u5FDC'), String.valueOf('\u61C9')); // modern 応 => trad 應

		variant2Trad.put(String.valueOf('\u604B'), String.valueOf('\u6200')); // modern 恋 => trad 戀
		variant2Trad.put(String.valueOf('\u6052'), String.valueOf('\u6046')); // modern 恒 => trad 恆
		variant2Trad.put(String.valueOf('\u6075'), String.valueOf('\u60E0')); // modern 恵 => trad 惠
		variant2Trad.put(String.valueOf('\u6094'), String.valueOf('\uFA3D')); // modern 悔 => trad 悔
		variant2Trad.put(String.valueOf('\u60A9'), String.valueOf('\u60F1')); // modern 悩 => trad 惱
		variant2Trad.put(String.valueOf('\u60AA'), String.valueOf('\u60E1')); // modern 悪 => trad 惡
		variant2Trad.put(String.valueOf('\u6168'), String.valueOf('\uFA3E')); // modern 慨 => trad 慨
		variant2Trad.put(String.valueOf('\u618E'), String.valueOf('\uFA3F')); // modern 憎 => trad 憎
		variant2Trad.put(String.valueOf('\u61D0'), String.valueOf('\u61F7')); // modern 懐 => trad 懷
		variant2Trad.put(String.valueOf('\u61F2'), String.valueOf('\uFA40')); // modern 懲 => trad 懲
		variant2Trad.put(String.valueOf('\u6226'), String.valueOf('\u6230')); // modern 戦 => trad 戰
		variant2Trad.put(String.valueOf('\u6231'), String.valueOf('\u6232')); // variant 戯 => trad 戲
		variant2Trad.put(String.valueOf('\u622F'), String.valueOf('\u6232')); // modern 戯 => trad 戲
		variant2Trad.put(String.valueOf('\u6238'), String.valueOf('\u6236')); // modern 戸 => trad 戶
		variant2Trad.put(String.valueOf('\u623B'), String.valueOf('\u623E')); // modern 戻 => trad 戾
		variant2Trad.put(String.valueOf('\u6255'), String.valueOf('\u62C2')); // modern 払 => trad 拂
		variant2Trad.put(String.valueOf('\u629C'), String.valueOf('\u62D4')); // modern 抜 => trad 拔
		variant2Trad.put(String.valueOf('\u629E'), String.valueOf('\u64C7')); // modern 択 => trad 擇
		variant2Trad.put(String.valueOf('\u62DD'), String.valueOf('\u62DC')); // modern 拝 => trad 拜
		variant2Trad.put(String.valueOf('\u62E0'), String.valueOf('\u64DA')); // modern 拠 => trad 據
		variant2Trad.put(String.valueOf('\u62E1'), String.valueOf('\u64F4')); // modern 拡 => trad 擴
		variant2Trad.put(String.valueOf('\u6319'), String.valueOf('\u64E7')); // modern 挙 => trad 擧
		variant2Trad.put(String.valueOf('\u633F'), String.valueOf('\u63D2')); // modern 挿 => trad 插
		variant2Trad.put(String.valueOf('\u635C'), String.valueOf('\u641C')); // modern 捜 => trad 搜
		variant2Trad.put(String.valueOf('\u63B2'), String.valueOf('\u63ED')); // modern 掲 => trad 揭
		variant2Trad.put(String.valueOf('\u63FA'), String.valueOf('\u6416')); // modern 揺 => trad 搖
		variant2Trad.put(String.valueOf('\u6442'), String.valueOf('\u651D')); // modern 摂 => trad 攝
		variant2Trad.put(String.valueOf('\u6483'), String.valueOf('\u64CA')); // modern 撃 => trad 擊
		variant2Trad.put(String.valueOf('\u654F'), String.valueOf('\uFA41')); // modern 敏 => trad 敏
		variant2Trad.put(String.valueOf('\u6589'), String.valueOf('\u9F4A')); // modern 斉 => trad 齊
		variant2Trad.put(String.valueOf('\u658E'), String.valueOf('\u9F4B')); // modern 斎 => trad 齋
		variant2Trad.put(String.valueOf('\u65E2'), String.valueOf('\u65E3')); // modern 既 => trad 旣
		variant2Trad.put(String.valueOf('\u6669'), String.valueOf('\u665A')); // modern 晩 => trad 晚
		variant2Trad.put(String.valueOf('\u6681'), String.valueOf('\u66C9')); // modern 暁 => trad 曉
		variant2Trad.put(String.valueOf('\u6691'), String.valueOf('\uFA43')); // modern 暑 => trad 暑
		variant2Trad.put(String.valueOf('\u66A6'), String.valueOf('\u66C6')); // modern 暦 => trad 曆
		variant2Trad.put(String.valueOf('\u6717'), String.valueOf('\uF929')); // modern 朗 => trad 朗
		variant2Trad.put(String.valueOf('\u67FB'), String.valueOf('\u67E5')); // modern 査 => trad 查
		variant2Trad.put(String.valueOf('\u6804'), String.valueOf('\u69AE')); // modern 栄 => trad 榮
		variant2Trad.put(String.valueOf('\u685C'), String.valueOf('\u6AFB')); // modern 桜 => trad 櫻
		variant2Trad.put(String.valueOf('\u685F'), String.valueOf('\u68E7')); // modern 桟 => trad 棧
		variant2Trad.put(String.valueOf('\u6885'), String.valueOf('\uFA44')); // modern 梅 => trad 梅
		variant2Trad.put(String.valueOf('\u691C'), String.valueOf('\u6AA2')); // modern 検 => trad 檢
		variant2Trad.put(String.valueOf('\u697D'), String.valueOf('\u6A02')); // modern 楽 => trad 樂
		variant2Trad.put(String.valueOf('\u69D8'), String.valueOf('\u6A23')); // modern 様 => trad 樣
		variant2Trad.put(String.valueOf('\u6A29'), String.valueOf('\u6B0A')); // modern 権 => trad 權
		variant2Trad.put(String.valueOf('\u6A2A'), String.valueOf('\u6A6B')); // modern 横 => trad 橫
		variant2Trad.put(String.valueOf('\u6B04'), String.valueOf('\uF91D')); // modern 欄 => trad 欄
		variant2Trad.put(String.valueOf('\u6B20'), String.valueOf('\u7F3A')); // modern 欠 => trad 缺
		variant2Trad.put(String.valueOf('\u6B53'), String.valueOf('\u6B61')); // modern 歓 => trad 歡
		variant2Trad.put(String.valueOf('\u6B69'), String.valueOf('\u6B65')); // modern 歩 => trad 步
		variant2Trad.put(String.valueOf('\u6B6F'), String.valueOf('\u9F52')); // modern 歯 => trad 齒
		variant2Trad.put(String.valueOf('\u6B74'), String.valueOf('\u6B77')); // modern 歴 => trad 歷
		variant2Trad.put(String.valueOf('\u6B8B'), String.valueOf('\u6B98')); // modern 残 => trad 殘
		variant2Trad.put(String.valueOf('\u6BBA'), String.valueOf('\uF970')); // modern 殺 => trad 殺
		variant2Trad.put(String.valueOf('\u6BBB'), String.valueOf('\u6BBC')); // modern 殻 => trad 殼
		variant2Trad.put(String.valueOf('\u6BCE'), String.valueOf('\u6BCF')); // modern 毎 => trad 每
		variant2Trad.put(String.valueOf('\u6C17'), String.valueOf('\u6C23')); // modern 気 => trad 氣
		variant2Trad.put(String.valueOf('\u6CA2'), String.valueOf('\u6FA4')); // modern 沢 => trad 澤
		variant2Trad.put(String.valueOf('\u6D5C'), String.valueOf('\u6FF1')); // modern 浜 => trad 濱
		variant2Trad.put(String.valueOf('\u6D77'), String.valueOf('\uFA45')); // modern 海 => trad 海
		variant2Trad.put(String.valueOf('\u6D99'), String.valueOf('\u6DDA')); // modern 涙 => trad 淚
		variant2Trad.put(String.valueOf('\u6E07'), String.valueOf('\u6E34')); // modern 渇 => trad 渴
		variant2Trad.put(String.valueOf('\u6E08'), String.valueOf('\u6FDF')); // modern 済 => trad 濟
		variant2Trad.put(String.valueOf('\u6E09'), String.valueOf('\u6D89')); // modern 渉 => trad 涉
		variant2Trad.put(String.valueOf('\u6E0B'), String.valueOf('\u6F81')); // modern 渋 => trad 澁
		variant2Trad.put(String.valueOf('\u6E13'), String.valueOf('\u6EAA')); // modern 渓 => trad 溪
		variant2Trad.put(String.valueOf('\u6E29'), String.valueOf('\u6EAB')); // modern 温 => trad 溫
		variant2Trad.put(String.valueOf('\u6E7F'), String.valueOf('\u6FD5')); // modern 湿 => trad 濕
		variant2Trad.put(String.valueOf('\u6E80'), String.valueOf('\u6EFF')); // modern 満 => trad 滿
		variant2Trad.put(String.valueOf('\u6EDD'), String.valueOf('\u7027')); // modern 滝 => trad 瀧
		variant2Trad.put(String.valueOf('\u6EDE'), String.valueOf('\u6EEF')); // modern 滞 => trad 滯
		variant2Trad.put(String.valueOf('\u6F22'), String.valueOf('\uFA47')); // modern 漢 => trad 漢

		variant2Trad.put(String.valueOf('\u702C'), String.valueOf('\u7028')); // modern 瀬 => trad 瀨
		variant2Trad.put(String.valueOf('\u713C'), String.valueOf('\u71D2')); // modern 焼 => trad 燒
		variant2Trad.put(String.valueOf('\u716E'), String.valueOf('\uFA48')); // modern 煮 => trad 煮
		variant2Trad.put(String.valueOf('\u72A0'), String.valueOf('\u72A7')); // modern 犠 => trad 犧
		variant2Trad.put(String.valueOf('\u731F'), String.valueOf('\u7375')); // modern 猟 => trad 獵
		variant2Trad.put(String.valueOf('\u7363'), String.valueOf('\u7378')); // modern 獣 => trad 獸
		variant2Trad.put(String.valueOf('\u74F6'), String.valueOf('\u7501')); // modern 瓶 => trad 甁
		variant2Trad.put(String.valueOf('\u752F'), String.valueOf('\u5BE7')); // variant 甯 => trad 寧
		variant2Trad.put(String.valueOf('\u753B'), String.valueOf('\u756B')); // modern 画 => trad 畫
		variant2Trad.put(String.valueOf('\u7573'), String.valueOf('\u758A')); // modern 畳 => trad 疊
		variant2Trad.put(String.valueOf('\u767A'), String.valueOf('\u767C')); // modern 発 => trad 發
		variant2Trad.put(String.valueOf('\u770C'), String.valueOf('\u7E23')); // modern 県 => trad 縣
		variant2Trad.put(String.valueOf('\u7815'), String.valueOf('\u788E')); // modern 砕 => trad 碎
		variant2Trad.put(String.valueOf('\u784F'), String.valueOf('\u7814')); // variant 硏 => trad 研
		variant2Trad.put(String.valueOf('\u7891'), String.valueOf('\uFA4B')); // modern 碑 => trad 碑
		variant2Trad.put(String.valueOf('\u7949'), String.valueOf('\uFA4D')); // modern 祉 => trad 祉
		variant2Trad.put(String.valueOf('\u7965'), String.valueOf('\uFA1A')); // modern 祥 => trad 祥
		variant2Trad.put(String.valueOf('\u798D'), String.valueOf('\uFA52')); // modern 禍 => trad 禍
		variant2Trad.put(String.valueOf('\u798F'), String.valueOf('\uFA1B')); // modern 福 => trad 福
		variant2Trad.put(String.valueOf('\u79D8'), String.valueOf('\u7955')); // modern 秘 => trad 祕
		variant2Trad.put(String.valueOf('\u7A32'), String.valueOf('\u7A3B')); // modern 稲 => trad 稻
		variant2Trad.put(String.valueOf('\u7A40'), String.valueOf('\uFA54')); // modern 穀 => trad 穀
		variant2Trad.put(String.valueOf('\u7A42'), String.valueOf('\u7A57')); // modern 穂 => trad 穗
		variant2Trad.put(String.valueOf('\u7A4F'), String.valueOf('\u7A69')); // modern 穏 => trad 穩
		variant2Trad.put(String.valueOf('\u7A81'), String.valueOf('\uFA55')); // modern 突 => trad 突
		variant2Trad.put(String.valueOf('\u7ADC'), String.valueOf('\u9F8D')); // modern 竜 => trad 龍
		variant2Trad.put(String.valueOf('\u7BC0'), String.valueOf('\uFA56')); // modern 節 => trad 節
		variant2Trad.put(String.valueOf('\u7C8B'), String.valueOf('\u7CB9')); // modern 粋 => trad 粹
		variant2Trad.put(String.valueOf('\u7C9B'), String.valueOf('\u8085')); // modern 粛 => trad 肅
		variant2Trad.put(String.valueOf('\u7CF8'), String.valueOf('\u7D72')); // modern 糸 => trad 絲
		variant2Trad.put(String.valueOf('\u7D4C'), String.valueOf('\u7D93')); // modern 経 => trad 經
		variant2Trad.put(String.valueOf('\u7D75'), String.valueOf('\u7E6A')); // modern 絵 => trad 繪
		variant2Trad.put(String.valueOf('\u7D99'), String.valueOf('\u7E7C')); // modern 継 => trad 繼
		variant2Trad.put(String.valueOf('\u7D9A'), String.valueOf('\u7E8C')); // modern 続 => trad 續
		variant2Trad.put(String.valueOf('\u7DCF'), String.valueOf('\u7E3D')); // modern 総 => trad 總
		variant2Trad.put(String.valueOf('\u7DD1'), String.valueOf('\u7DA0')); // modern 緑 => trad 綠
		variant2Trad.put(String.valueOf('\u7DD6'), String.valueOf('\u7DD2')); // variant 緖 => trad 緒
		variant2Trad.put(String.valueOf('\u7DF4'), String.valueOf('\uFA57')); // modern 練 => trad 練
		variant2Trad.put(String.valueOf('\u7E01'), String.valueOf('\u7DE3')); // modern 縁 => trad 緣
		variant2Trad.put(String.valueOf('\u7E04'), String.valueOf('\u7E69')); // modern 縄 => trad 繩
		variant2Trad.put(String.valueOf('\u7E26'), String.valueOf('\u7E31')); // modern 縦 => trad 縱
		variant2Trad.put(String.valueOf('\u7E41'), String.valueOf('\uFA59')); // modern 繁 => trad 繁
		variant2Trad.put(String.valueOf('\u7E4A'), String.valueOf('\u7E96')); // modern 繊 => trad 纖
		variant2Trad.put(String.valueOf('\u7F36'), String.valueOf('\u7F50')); // modern 缶 => trad 罐
		variant2Trad.put(String.valueOf('\u7F72'), String.valueOf('\uFA5A')); // modern 署 => trad 署
		variant2Trad.put(String.valueOf('\u7FFB'), String.valueOf('\u98DC')); // modern 翻 => trad 飜

		variant2Trad.put(String.valueOf('\u8005'), String.valueOf('\uFA5B')); // modern 者 => trad 者
		variant2Trad.put(String.valueOf('\u8074'), String.valueOf('\u807D')); // modern 聴 => trad 聽
		variant2Trad.put(String.valueOf('\u8133'), String.valueOf('\u8166')); // modern 脳 => trad 腦
		variant2Trad.put(String.valueOf('\u81D3'), String.valueOf('\u81DF')); // modern 臓 => trad 臟
		variant2Trad.put(String.valueOf('\u81ED'), String.valueOf('\uFA5C')); // modern 臭 => trad 臭
		variant2Trad.put(String.valueOf('\u82B8'), String.valueOf('\u85DD')); // modern 芸 => trad 藝
		variant2Trad.put(String.valueOf('\u8358'), String.valueOf('\u838A')); // modern 荘 => trad 莊
		variant2Trad.put(String.valueOf('\u8457'), String.valueOf('\uFA5F')); // modern 著 => trad 著
		variant2Trad.put(String.valueOf('\u8535'), String.valueOf('\u85CF')); // modern 蔵 => trad 藏
		variant2Trad.put(String.valueOf('\u85AB'), String.valueOf('\u85B0')); // modern 薫 => trad 薰
		variant2Trad.put(String.valueOf('\u85AC'), String.valueOf('\u85E5')); // modern 薬 => trad 藥
		variant2Trad.put(String.valueOf('\u865A'), String.valueOf('\u865B')); // modern 虚 => trad 虛
		variant2Trad.put(String.valueOf('\u865C'), String.valueOf('\uF936')); // modern 虜 => trad 虜
		variant2Trad.put(String.valueOf('\u86CD'), String.valueOf('\u87A2')); // modern 蛍 => trad 螢
		variant2Trad.put(String.valueOf('\u86EE'), String.valueOf('\u883B')); // modern 蛮 => trad 蠻
		variant2Trad.put(String.valueOf('\u885B'), String.valueOf('\u885E')); // modern 衛 => trad 衞
		variant2Trad.put(String.valueOf('\u8910'), String.valueOf('\uFA60')); // modern 褐 => trad 褐
		variant2Trad.put(String.valueOf('\u8912'), String.valueOf('\u8943')); // modern 褒 => trad 襃
		variant2Trad.put(String.valueOf('\u8987'), String.valueOf('\u9738')); // modern 覇 => trad 霸
		variant2Trad.put(String.valueOf('\u8996'), String.valueOf('\uFA61')); // modern 視 => trad 視
		variant2Trad.put(String.valueOf('\u899A'), String.valueOf('\u89BA')); // modern 覚 => trad 覺
		variant2Trad.put(String.valueOf('\u89A7'), String.valueOf('\u89BD')); // modern 覧 => trad 覽
		variant2Trad.put(String.valueOf('\u89B3'), String.valueOf('\u89C0')); // modern 観 => trad 觀
		variant2Trad.put(String.valueOf('\u8A33'), String.valueOf('\u8B6F')); // modern 訳 => trad 譯
		variant2Trad.put(String.valueOf('\u8A3C'), String.valueOf('\u8B49')); // modern 証 => trad 證
		variant2Trad.put(String.valueOf('\u8A89'), String.valueOf('\u8B7D')); // modern 誉 => trad 譽
		variant2Trad.put(String.valueOf('\u8AAD'), String.valueOf('\u8B80')); // modern 読 => trad 讀
		variant2Trad.put(String.valueOf('\u8AF8'), String.valueOf('\uFA22')); // modern 諸 => trad 諸
		variant2Trad.put(String.valueOf('\u8B01'), String.valueOf('\uFA62')); // modern 謁 => trad 謁
		variant2Trad.put(String.valueOf('\u8B21'), String.valueOf('\u8B20')); // modern 謡 => trad 謠
		variant2Trad.put(String.valueOf('\u8B39'), String.valueOf('\uFA63')); // modern 謹 => trad 謹
		variant2Trad.put(String.valueOf('\u8B72'), String.valueOf('\u8B93')); // modern 譲 => trad 讓
		variant2Trad.put(String.valueOf('\u8C4A'), String.valueOf('\u8C50')); // modern 豊 => trad 豐
		variant2Trad.put(String.valueOf('\u8CD3'), String.valueOf('\uFA64')); // modern 賓 => trad 賓
		variant2Trad.put(String.valueOf('\u8CDB'), String.valueOf('\u8D0A')); // modern 賛 => trad 贊
		variant2Trad.put(String.valueOf('\u8D08'), String.valueOf('\uFA65')); // modern 贈 => trad 贈
		variant2Trad.put(String.valueOf('\u8EE2'), String.valueOf('\u8F49')); // modern 転 => trad 轉
		variant2Trad.put(String.valueOf('\u8EFD'), String.valueOf('\u8F15')); // modern 軽 => trad 輕
		variant2Trad.put(String.valueOf('\u8FBA'), String.valueOf('\u908A')); // modern 辺 => trad 邊

		variant2Trad.put(String.valueOf('\u9013'), String.valueOf('\u905E')); // modern 逓 => trad 遞
		variant2Trad.put(String.valueOf('\u9038'), String.valueOf('\uFA67')); // modern 逸 => trad 逸
		variant2Trad.put(String.valueOf('\u9045'), String.valueOf('\u9072')); // modern 遅 => trad 遲
		variant2Trad.put(String.valueOf('\u90CE'), String.valueOf('\u90DE')); // modern 郎 => trad 郞
		variant2Trad.put(String.valueOf('\u90F7'), String.valueOf('\u9115')); // modern 郷 => trad 鄕
		variant2Trad.put(String.valueOf('\u90FD'), String.valueOf('\uFA26')); // modern 都 => trad 都
		variant2Trad.put(String.valueOf('\u9154'), String.valueOf('\u9189')); // modern 酔 => trad 醉
		variant2Trad.put(String.valueOf('\u91B8'), String.valueOf('\u91C0')); // modern 醸 => trad 釀
		variant2Trad.put(String.valueOf('\u91C8'), String.valueOf('\u91CB')); // modern 釈 => trad 釋
		variant2Trad.put(String.valueOf('\u9244'), String.valueOf('\u9435')); // modern 鉄 => trad 鐵
		variant2Trad.put(String.valueOf('\u9271'), String.valueOf('\u945B')); // modern 鉱 => trad 鑛
		variant2Trad.put(String.valueOf('\u92AD'), String.valueOf('\u9322')); // modern 銭 => trad 錢
		variant2Trad.put(String.valueOf('\u932C'), String.valueOf('\u934A')); // modern 錬 => trad 鍊
		variant2Trad.put(String.valueOf('\u9332'), String.valueOf('\u9304')); // modern 録 => trad 錄
		variant2Trad.put(String.valueOf('\u93AE'), String.valueOf('\u93AD')); // modern 鎮 => trad 鎭
		variant2Trad.put(String.valueOf('\u95A2'), String.valueOf('\u95DC')); // modern 関 => trad 關
		variant2Trad.put(String.valueOf('\u95D8'), String.valueOf('\u9B2A')); // modern 闘 => trad 鬪
		variant2Trad.put(String.valueOf('\u9665'), String.valueOf('\u9677')); // modern 陥 => trad 陷
		variant2Trad.put(String.valueOf('\u967A'), String.valueOf('\u96AA')); // modern 険 => trad 險
		variant2Trad.put(String.valueOf('\u9686'), String.valueOf('\uF9DC')); // modern 隆 => trad 隆
		variant2Trad.put(String.valueOf('\u96A0'), String.valueOf('\u96B1')); // modern 隠 => trad 隱
		variant2Trad.put(String.valueOf('\u96D1'), String.valueOf('\u96DC')); // modern 雑 => trad 雜
		variant2Trad.put(String.valueOf('\u96E3'), String.valueOf('\uFA68')); // modern 難 => trad 難
		variant2Trad.put(String.valueOf('\u970A'), String.valueOf('\u9748')); // modern 霊 => trad 靈
		variant2Trad.put(String.valueOf('\u97FF'), String.valueOf('\uFA69')); // modern 響 => trad 響
		variant2Trad.put(String.valueOf('\u983B'), String.valueOf('\uFA6A')); // modern 頻 => trad 頻
		variant2Trad.put(String.valueOf('\u983C'), String.valueOf('\u8CF4')); // modern 頼 => trad 賴
		variant2Trad.put(String.valueOf('\u9855'), String.valueOf('\u986F')); // modern 顕 => trad 顯
		variant2Trad.put(String.valueOf('\u985E'), String.valueOf('\uF9D0')); // modern 類 => trad 類
		variant2Trad.put(String.valueOf('\u99C5'), String.valueOf('\u9A5B')); // modern 駅 => trad 驛
		variant2Trad.put(String.valueOf('\u99C6'), String.valueOf('\u9A45')); // modern 駆 => trad 驅
		variant2Trad.put(String.valueOf('\u9A12'), String.valueOf('\u9A37')); // modern 騒 => trad 騷
		variant2Trad.put(String.valueOf('\u9A13'), String.valueOf('\u9A57')); // modern 験 => trad 驗
		variant2Trad.put(String.valueOf('\u9AEA'), String.valueOf('\u9AEE')); // modern 髪 => trad 髮
		variant2Trad.put(String.valueOf('\u9D8F'), String.valueOf('\u9DC4')); // modern 鶏 => trad 鷄
		variant2Trad.put(String.valueOf('\u9EA6'), String.valueOf('\u9EA5')); // modern 麦 => trad 麥
		variant2Trad.put(String.valueOf('\u9EC4'), String.valueOf('\u9EC3')); // modern 黄 => trad 黃
		variant2Trad.put(String.valueOf('\u9ED2'), String.valueOf('\u9ED1')); // modern 黒 => trad 黑
		variant2Trad.put(String.valueOf('\u9ED9'), String.valueOf('\u9ED8')); // modern 黙 => trad 默
		variant2Trad.put(String.valueOf('\u9F62'), String.valueOf('\u9F61')); // modern 齢 => trad 齡
	}
}
