/** @note Copyright (c) 2013 by The Board of Trustees of the Leland Stanford Junior University.
 * All rights reserved.  See {file:LICENSE} for details. **/
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
		Reader reader = new StringReader("Des mot");
		TokenStream stream = whitespaceMockTokenizer(reader);
		CJKFoldingFilter filter = new CJKFoldingFilter(stream);

		CharTermAttribute termAtt = filter.getAttribute(CharTermAttribute.class);
		filter.reset();
		assertTermEquals("Des", filter, termAtt);
		assertTermEquals("mot", filter, termAtt);
		assertFalse(filter.incrementToken());
	}

	/**
	 * Han character variants that aren't Japanese Modern and aren't from
	 *  "Jidong's list"
	 */
	public void testAddlHanVariants() throws Exception
	{
		checkOneTerm(analyzer, "嶽", "岳"); // 嶽 5DBD (std trad) => 岳 5CB3 (simp) - not in ICU translation
		checkOneTerm(analyzer, "囯", "國"); // 囯 56EF (variant) => 國 570B (std trad) - not on Jidong's list
		checkOneTerm(analyzer, "戱", "戲"); // 戯 6231 (variant) => 戲 6232 (std trad) - not on Jidong's list
		checkOneTerm(analyzer, "敎", "教"); // 敎 654E (variant) => 教 6559 (std trad) - not on Jidong's list
		checkOneTerm(analyzer, "甯", "寧"); // 甯 752F (variant) => 寧 5BE7 (std trad) - not on Jidong's list
		checkOneTerm(analyzer, "緖", "緒"); // 緖 7DD6 (variant) => 緒 7DD2 (std trad) - backwards in Jidong's list
		checkOneTerm(analyzer, "姫", "姬"); // 姫 59EB (modern Japanese) => 姬 59EC (std (trad?)) - by Mieko's request SW-988
	}

	/**
	 * the chars below are mostly from "Jidong's list" (Stanford University
	 * East Asia Librarian) and may also include chars not used by Modern
	 * Japanese
	 */
@Test
	public void testModernJapaneseToTrad() throws Exception
	{
		checkOneTerm(analyzer, "両", "兩"); // 両 4E21 => 兩 5169
		checkOneTerm(analyzer, "乗", "乘"); // 乗 4E57 => 乘 4E58
		checkOneTerm(analyzer, "予", "豫"); // 予 4E88 => 豫 8C6B
		checkOneTerm(analyzer, "亜", "亞"); // 亜 4E9C => 亞 4E9E
		checkOneTerm(analyzer, "仏", "佛"); // 仏 4ECF => 佛 4F5B
		checkOneTerm(analyzer, "仮", "假"); // 仮 4EEE => 假 5047
		checkOneTerm(analyzer, "伝", "傳"); // 伝 4F1D => 傳 50B3
		checkOneTerm(analyzer, "価", "價"); // 価 4FA1 => 價 50F9

		checkOneTerm(analyzer, "倹", "儉"); // 倹 5039 => 儉 5109
		checkOneTerm(analyzer, "児", "兒"); // 児 5150 => 兒 5152
		checkOneTerm(analyzer, "円", "圓"); // 円 5186 => 圓 5713
		checkOneTerm(analyzer, "処", "處"); // 処 51E6 => 處 8655
		checkOneTerm(analyzer, "刊", "刋"); // 刊 520A => 刋 520B
		checkOneTerm(analyzer, "剣", "劍"); // 剣 5263 => 劍 528D
		checkOneTerm(analyzer, "剤", "劑"); // 剤 5264 => 劑 5291
		checkOneTerm(analyzer, "剰", "剩"); // 剰 5270 => 剩 5269
		checkOneTerm(analyzer, "労", "勞"); // 労 52B4 => 勞 52DE
		checkOneTerm(analyzer, "勧", "勸"); // 勧 52E7 => 勸 52F8
		checkOneTerm(analyzer, "勲", "勳"); // 勲 52F2 => 勳 52F3
		checkOneTerm(analyzer, "単", "單"); // 単 5358 => 單 55AE
		checkOneTerm(analyzer, "即", "卽"); // 即 5373 => 卽 537D
		checkOneTerm(analyzer, "厳", "嚴"); // 厳 53B3 => 嚴 56B4
		checkOneTerm(analyzer, "収", "收"); // 収 53CE => 收 6536
		checkOneTerm(analyzer, "叙", "敍"); // 叙 53D9 => 敍 654D
		checkOneTerm(analyzer, "営", "營"); // 営 55B6 => 營 71DF
		checkOneTerm(analyzer, "嘆", "嘆"); // 嘆 5606 => 嘆 FA37
		checkOneTerm(analyzer, "団", "團"); // 団 56E3 => 團 5718
		checkOneTerm(analyzer, "囲", "圍"); // 囲 56F2 => 圍 570D
		checkOneTerm(analyzer, "図", "圖"); // 図 56F3 => 圖 5716
		checkOneTerm(analyzer, "圏", "圈"); // 圏 570F => 圈 5708
		checkOneTerm(analyzer, "圧", "壓"); // 圧 5727 => 壓 58D3
		checkOneTerm(analyzer, "塁", "壘"); // 塁 5841 => 壘 58D8
		checkOneTerm(analyzer, "塩", "鹽"); // 塩 5869 => 鹽 9E7D
		checkOneTerm(analyzer, "増", "增"); // 増 5897 => 增 589E
		checkOneTerm(analyzer, "壊", "壞"); // 壊 58CA => 壞 58DE
		checkOneTerm(analyzer, "壌", "壤"); // 壌 58CC => 壤 58E4
		checkOneTerm(analyzer, "売", "賣"); // 売 58F2 => 賣 8CE3
		checkOneTerm(analyzer, "変", "變"); // 変 5909 => 變 8B8A
		checkOneTerm(analyzer, "奨", "奬"); // 奨 5968 => 奬 596C
		checkOneTerm(analyzer, "嬢", "孃"); // 嬢 5B22 => 孃 5B43
		checkOneTerm(analyzer, "寛", "寬"); // 寛 5BDB => 寬 5BEC
		checkOneTerm(analyzer, "実", "實"); // 実 5B9F => 實 5BE6
		checkOneTerm(analyzer, "対", "對"); // 対 5BFE => 對 5C0D
		checkOneTerm(analyzer, "専", "專"); // 専 5C02 => 專 5C08
		checkOneTerm(analyzer, "層", "層"); // 層 5C64 => 層 FA3B
		checkOneTerm(analyzer, "州", "洲"); // 州 5DDE => 洲 6D32
		checkOneTerm(analyzer, "巣", "巢"); // 巣 5DE3 => 巢 5DE2
		checkOneTerm(analyzer, "巻", "卷"); // 巻 5DFB => 卷 5377
		checkOneTerm(analyzer, "帯", "帶"); // 帯 5E2F => 帶 5E36
		checkOneTerm(analyzer, "庁", "廳"); // 庁 5E81 => 廳 5EF3
		checkOneTerm(analyzer, "広", "廣"); // 広 5E83 => 廣 5EE3
		checkOneTerm(analyzer, "帰", "歸"); // 帰 5E30 => 歸 6B78
		checkOneTerm(analyzer, "廃", "廢"); // 廃 5EC3 => 廢 5EE2
		checkOneTerm(analyzer, "弾", "彈"); // 弾 5F3E => 彈 5F48
		checkOneTerm(analyzer, "従", "從"); // 従 5F93 => 從 5F9E
		checkOneTerm(analyzer, "徳", "德"); // 徳 5FB3 => 德 5FB7
		checkOneTerm(analyzer, "徴", "徵"); // 徴 5FB4 => 徵 5FB5
		checkOneTerm(analyzer, "応", "應"); // 応 5FDC => 應 61C9

		checkOneTerm(analyzer, "恵", "惠"); // 恵 6075 => 惠 60E0
		checkOneTerm(analyzer, "悩", "惱"); // 悩 60A9 => 惱 60F1
		checkOneTerm(analyzer, "懐", "懷"); // 懐 61D0 => 懷 61F7
		checkOneTerm(analyzer, "懲", "懲"); // 懲 61F2 => 懲 FA40
		checkOneTerm(analyzer, "戦", "戰"); // 戦 6226 => 戰 6230
		checkOneTerm(analyzer, "戯", "戲"); // 戯 622F => 戲 6232
		checkOneTerm(analyzer, "戸", "戶"); // 戸 6238 => 戶 6236
		checkOneTerm(analyzer, "戻", "戾"); // 戻 623B => 戾 623E
		checkOneTerm(analyzer, "払", "拂"); // 払 6255 => 拂 62C2
		checkOneTerm(analyzer, "抜", "拔"); // 抜 629C => 拔 62D4
		checkOneTerm(analyzer, "択", "擇"); // 択 629E => 擇 64C7
		checkOneTerm(analyzer, "拝", "拜"); // 拝 62DD => 拜 62DC
		checkOneTerm(analyzer, "拠", "據"); // 拠 62E0 => 據 64DA
		checkOneTerm(analyzer, "拡", "擴"); // 拡 62E1 => 擴 64F4
		checkOneTerm(analyzer, "捜", "搜"); // 捜 635C => 搜 641C
		checkOneTerm(analyzer, "掲", "揭"); // 掲 63B2 => 揭 63ED
		checkOneTerm(analyzer, "揺", "搖"); // 揺 63FA => 搖 6416
		checkOneTerm(analyzer, "摂", "攝"); // 摂 6442 => 攝 651D
		checkOneTerm(analyzer, "撃", "擊"); // 撃 6483 => 擊 64CA
		checkOneTerm(analyzer, "斎", "齋"); // 斎 658E => 齋 9F4B
		checkOneTerm(analyzer, "既", "旣"); // 既 65E2 => 旣 65E3
		checkOneTerm(analyzer, "晩", "晚"); // 晩 6669 => 晚 665A
		checkOneTerm(analyzer, "暁", "曉"); // 暁 6681 => 曉 66C9
		checkOneTerm(analyzer, "暦", "曆"); // 暦 66A6 => 曆 66C6
		checkOneTerm(analyzer, "査", "查"); // 査 67FB => 查 67E5
		checkOneTerm(analyzer, "栄", "榮"); // 栄 6804 => 榮 69AE
		checkOneTerm(analyzer, "桜", "櫻"); // 桜 685C => 櫻 6AFB
		checkOneTerm(analyzer, "桟", "棧"); // 桟 685F => 棧 68E7
		checkOneTerm(analyzer, "検", "檢"); // 検 691C => 檢 6AA2
		checkOneTerm(analyzer, "楽", "樂"); // 楽 697D => 樂 6A02
		checkOneTerm(analyzer, "様", "樣"); // 様 69D8 => 樣 6A23
		checkOneTerm(analyzer, "権", "權"); // 権 6A29 => 權 6B0A
		checkOneTerm(analyzer, "欠", "缺"); // 欠 6B20 => 缺 7F3A
		checkOneTerm(analyzer, "歩", "步"); // 歩 6B69 => 步 6B65
		checkOneTerm(analyzer, "歯", "齒"); // 歯 6B6F => 齒 9F52
		checkOneTerm(analyzer, "歴", "歷"); // 歴 6B74 => 歷 6B77
		checkOneTerm(analyzer, "殻", "殼"); // 殻 6BBB => 殼 6BBC
		checkOneTerm(analyzer, "毎", "每"); // 毎 6BCE => 每 6BCF
		checkOneTerm(analyzer, "気", "氣"); // 気 6C17 => 氣 6C23
		checkOneTerm(analyzer, "沢", "澤"); // 沢 6CA2 => 澤 6FA4
		checkOneTerm(analyzer, "浜", "濱"); // 浜 6D5C => 濱 6FF1
		checkOneTerm(analyzer, "涙", "淚"); // 涙 6D99 => 淚 6DDA
		checkOneTerm(analyzer, "渇", "渴"); // 渇 6E07 => 渴 6E34
		checkOneTerm(analyzer, "済", "濟"); // 済 6E08 => 濟 6FDF
		checkOneTerm(analyzer, "渉", "涉"); // 渉 6E09 => 涉 6D89
		checkOneTerm(analyzer, "満", "滿"); // 満 6E80 => 滿 6EFF
		checkOneTerm(analyzer, "滝", "瀧"); // 滝 6EDD => 瀧 7027
		checkOneTerm(analyzer, "漢", "漢"); // 漢 6F22 => 漢 FA47

		checkOneTerm(analyzer, "瀬", "瀨"); // 瀬 702C => 瀨 7028
		checkOneTerm(analyzer, "焼", "燒"); // 焼 713C => 燒 71D2
		checkOneTerm(analyzer, "犠", "犧"); // 犠 72A0 => 犧 72A7
		checkOneTerm(analyzer, "猟", "獵"); // 猟 731F => 獵 7375
		checkOneTerm(analyzer, "獣", "獸"); // 獣 7363 => 獸 7378
		checkOneTerm(analyzer, "瓶", "甁"); // 瓶 74F6 => 甁 7501
		checkOneTerm(analyzer, "畳", "疊"); // 畳 7573 => 疊 758A
		checkOneTerm(analyzer, "発", "發"); // 発 767A => 發 767C
		checkOneTerm(analyzer, "県", "縣"); // 県 770C => 縣 7E23
		checkOneTerm(analyzer, "砕", "碎"); // 砕 7815 => 碎 788E
		checkOneTerm(analyzer, "禍", "禍"); // 禍 798D => 禍 FA52
		checkOneTerm(analyzer, "稲", "稻"); // 稲 7A32 => 稻 7A3B
		checkOneTerm(analyzer, "穀", "穀"); // 穀 7A40 => 穀 FA54
		checkOneTerm(analyzer, "穂", "穗"); // 穂 7A42 => 穗 7A57
		checkOneTerm(analyzer, "穏", "穩"); // 穏 7A4F => 穩 7A69
		checkOneTerm(analyzer, "節", "節"); // 節 7BC0 => 節 FA56
		checkOneTerm(analyzer, "粋", "粹"); // 粋 7C8B => 粹 7CB9
		checkOneTerm(analyzer, "粛", "肅"); // 粛 7C9B => 肅 8085
		checkOneTerm(analyzer, "糸", "絲"); // 糸 7CF8 => 絲 7D72
		checkOneTerm(analyzer, "経", "經"); // 経 7D4C => 經 7D93
		checkOneTerm(analyzer, "絵", "繪"); // 絵 7D75 => 繪 7E6A
		checkOneTerm(analyzer, "継", "繼"); // 継 7D99 => 繼 7E7C
		checkOneTerm(analyzer, "続", "續"); // 続 7D9A => 續 7E8C
		checkOneTerm(analyzer, "総", "總"); // 総 7DCF => 總 7E3D
		checkOneTerm(analyzer, "練", "練"); // 練 7DF4 => 練 FA57
		checkOneTerm(analyzer, "縁", "緣"); // 縁 7E01 => 緣 7DE3
		checkOneTerm(analyzer, "縄", "繩"); // 縄 7E04 => 繩 7E69
		checkOneTerm(analyzer, "繊", "纖"); // 繊 7E4A => 纖 7E96
		checkOneTerm(analyzer, "缶", "罐"); // 缶 7F36 => 罐 7F50

		checkOneTerm(analyzer, "聴", "聽"); // 聴 8074 => 聽 807D
		checkOneTerm(analyzer, "脳", "腦"); // 脳 8133 => 腦 8166
		checkOneTerm(analyzer, "臓", "臟"); // 臓 81D3 => 臟 81DF
		checkOneTerm(analyzer, "荘", "莊"); // 荘 8358 => 莊 838A
		checkOneTerm(analyzer, "著", "著"); // 著 8457 => 著 FA5F
		checkOneTerm(analyzer, "蔵", "藏"); // 蔵 8535 => 藏 85CF
		checkOneTerm(analyzer, "薫", "薰"); // 薫 85AB => 薰 85B0
		checkOneTerm(analyzer, "薬", "藥"); // 薬 85AC => 藥 85E5
		checkOneTerm(analyzer, "蛍", "螢"); // 蛍 86CD => 螢 87A2
		checkOneTerm(analyzer, "褒", "襃"); // 褒 8912 => 襃 8943
		checkOneTerm(analyzer, "覇", "霸"); // 覇 8987 => 霸 9738
		checkOneTerm(analyzer, "視", "視"); // 視 8996 => 視 FA61
		checkOneTerm(analyzer, "覧", "覽"); // 覧 89A7 => 覽 89BD
		checkOneTerm(analyzer, "訳", "譯"); // 訳 8A33 => 譯 8B6F
		checkOneTerm(analyzer, "読", "讀"); // 読 8AAD => 讀 8B80
		checkOneTerm(analyzer, "諸", "諸"); // 諸 8AF8 => 諸 FA22
		checkOneTerm(analyzer, "謁", "謁"); // 謁 8B01 => 謁 FA62
		checkOneTerm(analyzer, "謹", "謹"); // 謹 8B39 => 謹 FA63
		checkOneTerm(analyzer, "譲", "讓"); // 譲 8B72 => 讓 8B93
		checkOneTerm(analyzer, "豊", "豐"); // 豊 8C4A => 豐 8C50
		checkOneTerm(analyzer, "賓", "賓"); // 賓 8CD3 => 賓 FA64
		checkOneTerm(analyzer, "贈", "贈"); // 贈 8D08 => 贈 FA65
		checkOneTerm(analyzer, "転", "轉"); // 転 8EE2 => 轉 8F49
		checkOneTerm(analyzer, "軽", "輕"); // 軽 8EFD => 輕 8F15
		checkOneTerm(analyzer, "辺", "邊"); // 辺 8FBA => 邊 908A

		checkOneTerm(analyzer, "逓", "遞"); // 逓 9013 => 遞 905E
		checkOneTerm(analyzer, "遅", "遲"); // 遅 9045 => 遲 9072
		checkOneTerm(analyzer, "郎", "郞"); // 郎 90CE => 郞 90DE
		checkOneTerm(analyzer, "酔", "醉"); // 酔 9154 => 醉 9189
		checkOneTerm(analyzer, "醸", "釀"); // 醸 91B8 => 釀 91C0
		checkOneTerm(analyzer, "釈", "釋"); // 釈 91C8 => 釋 91CB
		checkOneTerm(analyzer, "鉄", "鐵"); // 鉄 9244 => 鐵 9435
		checkOneTerm(analyzer, "銭", "錢"); // 銭 92AD => 錢 9322
		checkOneTerm(analyzer, "錬", "鍊"); // 錬 932C => 鍊 934A
		checkOneTerm(analyzer, "鎮", "鎭"); // 鎮 93AE => 鎭 93AD
		checkOneTerm(analyzer, "陥", "陷"); // 陥 9665 => 陷 9677
		checkOneTerm(analyzer, "険", "險"); // 険 967A => 險 96AA
		checkOneTerm(analyzer, "隠", "隱"); // 隠 96A0 => 隱 96B1
		checkOneTerm(analyzer, "雑", "雜"); // 雑 96D1 => 雜 96DC
		checkOneTerm(analyzer, "難", "難"); // 難 96E3 => 難 FA68
		checkOneTerm(analyzer, "霊", "靈"); // 霊 970A => 靈 9748
		checkOneTerm(analyzer, "響", "響"); // 響 97FF => 響 FA69
		checkOneTerm(analyzer, "頻", "頻"); // 頻 983B => 頻 FA6A
		checkOneTerm(analyzer, "頼", "賴"); // 頼 983C => 賴 8CF4
		checkOneTerm(analyzer, "顕", "顯"); // 顕 9855 => 顯 986F
		checkOneTerm(analyzer, "駅", "驛"); // 駅 99C5 => 驛 9A5B
		checkOneTerm(analyzer, "騒", "騷"); // 騒 9A12 => 騷 9A37
		checkOneTerm(analyzer, "験", "驗"); // 験 9A13 => 驗 9A57
		checkOneTerm(analyzer, "髪", "髮"); // 髪 9AEA => 髮 9AEE
		checkOneTerm(analyzer, "鶏", "鷄"); // 鶏 9D8F => 鷄 9DC4
		checkOneTerm(analyzer, "黒", "黑"); // 黒 9ED2 => 黑 9ED1
		checkOneTerm(analyzer, "黙", "默"); // 黙 9ED9 => 默 9ED8
		checkOneTerm(analyzer, "齢", "齡"); // 齢 9F62 => 齡 9F61
	}
	/**
	 * groups of 3 or more equivalent characters
	 */
@Test
	public void testThreeOrMore() throws Exception
	{
		checkOneTerm(analyzer, "並", "竝"); // 並 4E26 => 竝 7ADD
		checkOneTerm(analyzer, "并", "竝"); // 并 5E76 => 竝 7ADD
		checkOneTerm(analyzer, "併", "竝"); // 併 4F75 => 竝 7ADD
		checkOneTerm(analyzer, "倂", "竝"); // 倂 5002 => 竝 7ADD
		checkOneTerm(analyzer, "幷", "竝"); // 幷 5E77 => 竝 7ADD
		checkOneTerm(analyzer, "縦", "緃"); // 縦 7E26 => 緃 7DC3
		checkOneTerm(analyzer, "纵", "緃"); // 纵 7EB5 => 緃 7DC3
		checkOneTerm(analyzer, "縱", "緃"); // 縱 7E31 => 緃 7DC3
		checkOneTerm(analyzer, "駆", "敺"); // 駆 99C6 => 敺 657A
		checkOneTerm(analyzer, "駈", "敺"); // 駈 99C8 => 敺 657A
		checkOneTerm(analyzer, "驅", "敺"); // 驅 9A45 => 敺 657A
		checkOneTerm(analyzer, "驱", "敺"); // 驱 9A71 => 敺 657A
		checkOneTerm(analyzer, "敽", "敺"); // 敽 657D => 敺 657A
		checkOneTerm(analyzer, "敿", "敺"); // 敿 657F => 敺 657A
		checkOneTerm(analyzer, "賛", "讃"); // 賛 8CDB => 讃 8B83
		checkOneTerm(analyzer, "贊", "讃"); // 贊 8D0A => 讃 8B83
		checkOneTerm(analyzer, "赞", "讃"); // 赞 8D5E => 讃 8B83
		checkOneTerm(analyzer, "讚", "讃"); // 讚 8B9A => 讃 8B83
		checkOneTerm(analyzer, "欗", "栏"); // 欗 6B17 => 栏 680F
		checkOneTerm(analyzer, "欄", "栏"); // 欄 6B04 => 栏 680F
		checkOneTerm(analyzer, "欄", "栏"); // 欄 F91D => 栏 680F
		checkOneTerm(analyzer, "塚", "冢"); // 塚 585A => 冢 51A2
		checkOneTerm(analyzer, "塚", "冢"); // 塚 FA10 => 冢 51A2
		checkOneTerm(analyzer, "衛", "卫"); // 衛 885B => 卫 536B
		checkOneTerm(analyzer, "衞", "卫"); // 衞 885E => 卫 536B
		checkOneTerm(analyzer, "恶", "噁"); // 恶 6076 => 噁 5641
		checkOneTerm(analyzer, "悪", "噁"); // 悪 60AA => 噁 5641
		checkOneTerm(analyzer, "惡", "噁"); // 惡 60E1 => 噁 5641
		checkOneTerm(analyzer, "惡", "噁"); // 惡 F9B9 => 噁 5641
		checkOneTerm(analyzer, "欢", "懽"); // 欢 6B22 => 懽 61FD
		checkOneTerm(analyzer, "歓", "懽"); // 歓 6B53 => 懽 61FD
		checkOneTerm(analyzer, "歡", "懽"); // 歡 6B61 => 懽 61FD
		checkOneTerm(analyzer, "讙", "懽"); // 讙 8B99 => 懽 61FD
		checkOneTerm(analyzer, "驩", "懽"); // 驩 9A69 => 懽 61FD
		checkOneTerm(analyzer, "殺", "杀"); // 殺 6BBA => 杀 6740
		checkOneTerm(analyzer, "殺", "杀"); // 殺 F970 => 杀 6740
		checkOneTerm(analyzer, "濇", "涩"); // 濇 6FC7 => 涩 6DA9
		checkOneTerm(analyzer, "渋", "涩"); // 渋 6E0B => 涩 6DA9
		checkOneTerm(analyzer, "澁", "涩"); // 澁 6F81 => 涩 6DA9
		checkOneTerm(analyzer, "瀒", "涩"); // 瀒 7012 => 涩 6DA9
		checkOneTerm(analyzer, "類", "类"); // 類 985E => 类 7C7B
		checkOneTerm(analyzer, "類", "类"); // 類 F9D0 => 类 7C7B
		checkOneTerm(analyzer, "虜", "虏"); // 虜 865C => 虏 864F
		checkOneTerm(analyzer, "虜", "虏"); // 虜 F936 => 虏 864F
		checkOneTerm(analyzer, "壱", "一"); // 壱 58F1 => 一 4E00
		checkOneTerm(analyzer, "壹", "一"); // 壹 58F9 => 一 4E00
		checkOneTerm(analyzer, "弌", "一"); // 弌 5F0C => 一 4E00
		checkOneTerm(analyzer, "挙", "举"); // 挙 6319 => 举 4E3E
		checkOneTerm(analyzer, "擧", "举"); // 擧 64E7 => 举 4E3E
		checkOneTerm(analyzer, "郷", "乡"); // 郷 90F7 => 乡 4E61
		checkOneTerm(analyzer, "鄊", "乡"); // 鄊 910A => 乡 4E61
		checkOneTerm(analyzer, "鄕", "乡"); // 鄕 9115 => 乡 4E61
		checkOneTerm(analyzer, "弍", "二"); // 弍 5F0D => 二 4E8C
		checkOneTerm(analyzer, "弐", "二"); // 弐 5F10 => 二 4E8C
		checkOneTerm(analyzer, "貮", "二"); // 貮 8CAE => 二 4E8C
		checkOneTerm(analyzer, "貳", "二"); // 貳 8CB3 => 二 4E8C
		checkOneTerm(analyzer, "贰", "二"); // 贰 8D30 => 二 4E8C
		checkOneTerm(analyzer, "斉", "亝"); // 斉 6589 => 亝 4E9D
		checkOneTerm(analyzer, "齊", "亝"); // 齊 9F4A => 亝 4E9D
		checkOneTerm(analyzer, "齐", "亝"); // 齐 9F50 => 亝 4E9D
		checkOneTerm(analyzer, "効", "俲"); // 効 52B9 => 俲 4FF2
		checkOneTerm(analyzer, "效", "俲"); // 效 6548 => 俲 4FF2
		checkOneTerm(analyzer, "傚", "俲"); // 傚 509A => 俲 4FF2
		checkOneTerm(analyzer, "蓺", "兿"); // 蓺 84FA => 兿 517F
		checkOneTerm(analyzer, "秇", "兿"); // 秇 79C7 => 兿 517F
		checkOneTerm(analyzer, "艺", "兿"); // 艺 827A => 兿 517F
		checkOneTerm(analyzer, "芸", "兿"); // 芸 82B8 => 兿 517F
		checkOneTerm(analyzer, "蕓", "兿"); // 蕓 8553 => 兿 517F
		checkOneTerm(analyzer, "藝", "兿"); // 藝 85DD => 兿 517F
		checkOneTerm(analyzer, "揅", "圳"); // 揅 63C5 => 圳 5733
		checkOneTerm(analyzer, "研", "圳"); // 研 7814 => 圳 5733
		checkOneTerm(analyzer, "硎", "圳"); // 硎 784E => 圳 5733
		checkOneTerm(analyzer, "硏", "圳"); // 硏 784F => 圳 5733
		checkOneTerm(analyzer, "竜", "尨"); // 竜 7ADC => 尨 5C28
		checkOneTerm(analyzer, "龍", "尨"); // 龍 9F8D => 尨 5C28
		checkOneTerm(analyzer, "龙", "尨"); // 龙 9F99 => 尨 5C28
		checkOneTerm(analyzer, "龍", "尨"); // 龍 F9C4 => 尨 5C28
		checkOneTerm(analyzer, "谿", "嵠"); // 谿 8C3F => 嵠 5D60
		checkOneTerm(analyzer, "渓", "嵠"); // 渓 6E13 => 嵠 5D60
		checkOneTerm(analyzer, "溪", "嵠"); // 溪 6EAA => 嵠 5D60
		checkOneTerm(analyzer, "挿", "扱"); // 挿 633F => 扱 6271
		checkOneTerm(analyzer, "插", "扱"); // 插 63D2 => 扱 6271
		checkOneTerm(analyzer, "揷", "扱"); // 揷 63F7 => 扱 6271
		checkOneTerm(analyzer, "覚", "斍"); // 覚 899A => 斍 658D
		checkOneTerm(analyzer, "覺", "斍"); // 覺 89BA => 斍 658D
		checkOneTerm(analyzer, "觉", "斍"); // 觉 89C9 => 斍 658D
		checkOneTerm(analyzer, "闘", "斗"); // 闘 95D8 => 斗 6597
		checkOneTerm(analyzer, "鬦", "斗"); // 鬦 9B26 => 斗 6597
		checkOneTerm(analyzer, "鬪", "斗"); // 鬪 9B2A => 斗 6597
		checkOneTerm(analyzer, "鬬", "斗"); // 鬬 9B2C => 斗 6597
		checkOneTerm(analyzer, "砿", "矿"); // 砿 783F => 矿 77FF
		checkOneTerm(analyzer, "鉱", "矿"); // 鉱 9271 => 矿 77FF
		checkOneTerm(analyzer, "鑛", "矿"); // 鑛 945B => 矿 77FF
		checkOneTerm(analyzer, "翻", "繙"); // 翻 7FFB => 繙 7E59
		checkOneTerm(analyzer, "飜", "繙"); // 飜 98DC => 繙 7E59
		checkOneTerm(analyzer, "観", "覌"); // 観 89B3 => 覌 898C
		checkOneTerm(analyzer, "觀", "覌"); // 觀 89C0 => 覌 898C
		checkOneTerm(analyzer, "观", "覌"); // 观 89C2 => 覌 898C

	}

	/**
	 * ensure that when character maps to an Fnnn character, there is no
	 * issue with tokens of multiple characters
	 */
@Test
	public void testFirstByteFTokens() throws Exception
	{
		checkOneTerm(analyzer, "亜", "亞"); // 亜 4E9C => 亞 4E9E
		checkOneTerm(analyzer, "黒", "黑"); // 黒 9ED2 => 黑 9ED1

		checkOneTerm(analyzer, "亜黒亜", "亞黑亞");
		checkOneTerm(analyzer, "黒亜", "黑亞");
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
			protected TokenStreamComponents createComponents(String fieldName) {
				Tokenizer tokenizer = new KeywordTokenizer();
				return new TokenStreamComponents(tokenizer, new CJKFoldingFilter(tokenizer));
			}
		};
		checkOneTerm(a, "", "");
	}

	private Analyzer analyzer = new Analyzer() {
	    @Override
	    protected TokenStreamComponents createComponents(String field) {
	      final Tokenizer tokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, false);
	      final TokenStream stream = new CJKFoldingFilter(tokenizer);
	      return new TokenStreamComponents(tokenizer, stream);
	    }
	};

	void assertTermEquals(String expected, TokenStream stream, CharTermAttribute termAtt) throws Exception
	{
		assertTrue(stream.incrementToken());
		assertEquals(expected, termAtt.toString());
	}



}
