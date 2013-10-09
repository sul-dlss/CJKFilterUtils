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
		checkOneTerm(analyzer, "亜", "亞"); // 亜 4E9C => (亞) 4E9E
		checkOneTerm(analyzer, "仮", "假"); // 仮 4EEE => (假) 5047
		checkOneTerm(analyzer, "価", "價"); // 価 4FA1 => (價) 50F9
		checkOneTerm(analyzer, "両", "兩"); // 両 4E21 => (兩) 5169
		checkOneTerm(analyzer, "並", "竝"); // 並 4E26 => (竝) 7ADD
		checkOneTerm(analyzer, "乗", "乘"); // 乗 4E57 => (乘) 4E58
		checkOneTerm(analyzer, "予", "豫"); // 予 4E88 => (豫) 8C6B
		checkOneTerm(analyzer, "仏", "佛"); // 仏 4ECF => (佛) 4F5B
		checkOneTerm(analyzer, "伝", "傳"); // 伝 4F1D => (傳) 50B3
		checkOneTerm(analyzer, "併", "倂"); // 併 4F75 => (倂) 5002
		checkOneTerm(analyzer, "侮", "侮"); // 侮 4FAE => (侮) FA30
		checkOneTerm(analyzer, "倹", "儉"); // 倹 5039 => (儉) 5109
		checkOneTerm(analyzer, "偽", "僞"); // 偽 507D => (僞) 50DE
		checkOneTerm(analyzer, "免", "免"); // 免 514D => (免) FA32
		checkOneTerm(analyzer, "児", "兒"); // 児 5150 => (兒) 5152
		checkOneTerm(analyzer, "円", "圓"); // 円 5186 => (圓) 5713
		checkOneTerm(analyzer, "処", "處"); // 処 51E6 => (處) 8655
		checkOneTerm(analyzer, "剣", "劍"); // 剣 5263 => (劍) 528D
		checkOneTerm(analyzer, "剤", "劑"); // 剤 5264 => (劑) 5291
		checkOneTerm(analyzer, "剰", "剩"); // 剰 5270 => (剩) 5269
		checkOneTerm(analyzer, "勤", "勤"); // 勤 52E4 => (勤) FA34
		checkOneTerm(analyzer, "勧", "勸"); // 勧 52E7 => (勸) 52F8
		checkOneTerm(analyzer, "励", "勵"); // 励 52B1 => (勵) 52F5
		checkOneTerm(analyzer, "労", "勞"); // 労 52B4 => (勞) 52DE
		checkOneTerm(analyzer, "効", "效"); // 効 52B9 => (效) 6548
		checkOneTerm(analyzer, "勉", "勉"); // 勉 52C9 => (勉) FA33
		checkOneTerm(analyzer, "勲", "勳"); // 勲 52F2 => (勳) 52F3
		checkOneTerm(analyzer, "卑", "卑"); // 卑 5351 => (卑) FA35
		checkOneTerm(analyzer, "単", "單"); // 単 5358 => (單) 55AE
		checkOneTerm(analyzer, "即", "卽"); // 即 5373 => (卽) 537D
		checkOneTerm(analyzer, "厳", "嚴"); // 厳 53B3 => (嚴) 56B4
		checkOneTerm(analyzer, "収", "收"); // 収 53CE => (收) 6536
		checkOneTerm(analyzer, "叙", "敍"); // 叙 53D9 => (敍) 654D
		checkOneTerm(analyzer, "喝", "喝"); // 喝 559D => (喝) FA36
		checkOneTerm(analyzer, "営", "營"); // 営 55B6 => (營) 71DF
		checkOneTerm(analyzer, "嘆", "嘆"); // 嘆 5606 => (嘆) FA37
		checkOneTerm(analyzer, "器", "器"); // 器 5668 => (器) FA38
		checkOneTerm(analyzer, "囲", "圍"); // 囲 56F2 => (圍) 570D
		checkOneTerm(analyzer, "団", "團"); // 団 56E3 => (團) 5718
		checkOneTerm(analyzer, "図", "圖"); // 図 56F3 => (圖) 5716
		checkOneTerm(analyzer, "圏", "圈"); // 圏 570F => (圈) 5708
		checkOneTerm(analyzer, "圧", "壓"); // 圧 5727 => (壓) 58D3
		checkOneTerm(analyzer, "塀", "塀"); // 塀 5840 => (塀) FA39
		checkOneTerm(analyzer, "塁", "壘"); // 塁 5841 => (壘) 58D8
		checkOneTerm(analyzer, "塚", "塚"); // 塚 585A => (塚) FA10
		checkOneTerm(analyzer, "塩", "鹽"); // 塩 5869 => (鹽) 9E7D
		checkOneTerm(analyzer, "増", "增"); // 増 5897 => (增) 589E
		checkOneTerm(analyzer, "壊", "壞"); // 壊 58CA => (壞) 58DE
		checkOneTerm(analyzer, "壱", "壹"); // 壱 58F1 => (壹) 58F9
		checkOneTerm(analyzer, "墨", "墨"); // 墨 58A8 => (墨) FA3A
		checkOneTerm(analyzer, "壊", "壞"); // 壊 58CA => (壞) 58DE
		checkOneTerm(analyzer, "壌", "壤"); // 壌 58CC => (壤) 58E4
		checkOneTerm(analyzer, "売", "賣"); // 売 58F2 => (賣) 8CE3
		checkOneTerm(analyzer, "変", "變"); // 変 5909 => (變) 8B8A
		checkOneTerm(analyzer, "奥", "奧"); // 奥 5965 => (奧) 5967
		checkOneTerm(analyzer, "奨", "奬"); // 奨 5968 => (奬) 596C
		checkOneTerm(analyzer, "寛", "寬"); // 寛 5BDB => (寬) 5BEC
		checkOneTerm(analyzer, "巻", "卷"); // 巻 5DFB => (卷) 5377
		checkOneTerm(analyzer, "帰", "歸"); // 帰 5E30 => (歸) 6B78
		checkOneTerm(analyzer, "応", "應"); // 応 5FDC => (應) 61C9
		checkOneTerm(analyzer, "嬢", "孃"); // 嬢 5B22 => (孃) 5B43
		checkOneTerm(analyzer, "実", "實"); // 実 5B9F => (實) 5BE6
		checkOneTerm(analyzer, "対", "對"); // 対 5BFE => (對) 5C0D
		checkOneTerm(analyzer, "専", "專"); // 専 5C02 => (專) 5C08
		checkOneTerm(analyzer, "届", "屆"); // 届 5C4A => (屆) 5C46
		checkOneTerm(analyzer, "層", "層"); // 層 5C64 => (層) FA3B
		checkOneTerm(analyzer, "巣", "巢"); // 巣 5DE3 => (巢) 5DE2
		checkOneTerm(analyzer, "帯", "帶"); // 帯 5E2F => (帶) 5E36
		checkOneTerm(analyzer, "庁", "廳"); // 庁 5E81 => (廳) 5EF3
		checkOneTerm(analyzer, "広", "廣"); // 広 5E83 => (廣) 5EE3
		checkOneTerm(analyzer, "廃", "廢"); // 廃 5EC3 => (廢) 5EE2
		checkOneTerm(analyzer, "廊", "廊"); // 廊 5ECA => (廊) F928
		checkOneTerm(analyzer, "弐", "貳"); // 弐 5F10 => (貳) 8CB3
		checkOneTerm(analyzer, "弾", "彈"); // 弾 5F3E => (彈) 5F48
		checkOneTerm(analyzer, "従", "從"); // 従 5F93 => (從) 5F9E
		checkOneTerm(analyzer, "徳", "德"); // 徳 5FB3 => (德) 5FB7
		checkOneTerm(analyzer, "徴", "徵"); // 徴 5FB4 => (徵) 5FB5
		checkOneTerm(analyzer, "応", "應"); // 応 5FDC => (應) 61C9
		checkOneTerm(analyzer, "恋", "戀"); // 恋 604B => (戀) 6200
		checkOneTerm(analyzer, "恒", "恆"); // 恒 6052 => (恆) 6046
		checkOneTerm(analyzer, "恵", "惠"); // 恵 6075 => (惠) 60E0
		checkOneTerm(analyzer, "悔", "悔"); // 悔 6094 =>（悔） FA3D
		checkOneTerm(analyzer, "悪", "惡"); // 悪 60AA => (惡) 60E1
		checkOneTerm(analyzer, "悩", "惱"); // 悩 60A9 => (惱) 60F1
		checkOneTerm(analyzer, "慨", "慨"); // 慨 6168 => (慨) FA3E
		checkOneTerm(analyzer, "憎", "憎"); // 憎 618E => (憎) FA3F
		checkOneTerm(analyzer, "懐", "懷"); // 懐 61D0 => (懷) 61F7
		checkOneTerm(analyzer, "懲", "懲"); // 懲 61F2 => (懲) FA40
		checkOneTerm(analyzer, "戦", "戰"); // 戦 6226 => (戰) 6230
		checkOneTerm(analyzer, "戯", "戲"); // 戯 622F => (戲) 6232
		checkOneTerm(analyzer, "戸", "戶"); // 戸 6238 => (戶) 6236
		checkOneTerm(analyzer, "戻", "戾"); // 戻 623B => (戾) 623E
		checkOneTerm(analyzer, "払", "拂"); // 払 6255 => (拂) 62C2
		checkOneTerm(analyzer, "抜", "拔"); // 抜 629C => (拔) 62D4
		checkOneTerm(analyzer, "択", "擇"); // 択 629E => (擇) 64C7
		checkOneTerm(analyzer, "拠", "據"); // 拠 62E0 => (據) 64DA
		checkOneTerm(analyzer, "拡", "擴"); // 拡 62E1 => (擴) 64F4
		checkOneTerm(analyzer, "拝", "拜"); // 拝 62DD => (拜) 62DC
		checkOneTerm(analyzer, "挙", "擧"); // 挙 6319 => (擧) 64E7
		checkOneTerm(analyzer, "挿", "插"); // 挿 633F => (插) 63D2
		checkOneTerm(analyzer, "捜", "搜"); // 捜 635C => (搜) 641C
		checkOneTerm(analyzer, "掲", "揭"); // 掲 63B2 => (揭) 63ED
		checkOneTerm(analyzer, "揺", "搖"); // 揺 63FA => (搖) 6416
		checkOneTerm(analyzer, "摂", "攝"); // 摂 6442 => (攝) 651D
		checkOneTerm(analyzer, "撃", "擊"); // 撃 6483 => (擊) 64CA
		checkOneTerm(analyzer, "敏", "敏"); // 敏 654F => (敏) FA41
		checkOneTerm(analyzer, "斉", "齊"); // 斉 6589 => (齊) 9F4A
		checkOneTerm(analyzer, "斎", "齋"); // 斎 658E => (齋) 9F4B
		checkOneTerm(analyzer, "既", "旣"); // 既 65E2 => (旣) 65E3
		checkOneTerm(analyzer, "晩", "晚"); // 晩 6669 => (晚) 665A
		checkOneTerm(analyzer, "暁", "曉"); // 暁 6681 => (曉) 66C9
		checkOneTerm(analyzer, "暑", "暑"); // 暑 6691 => (暑) FA43
		checkOneTerm(analyzer, "暦", "曆"); // 暦 66A6 => (曆) 66C6
		checkOneTerm(analyzer, "朗", "朗"); // 朗 6717 => (朗) F929
		checkOneTerm(analyzer, "査", "查"); // 査 67FB => (查) 67E5
		checkOneTerm(analyzer, "栄", "榮"); // 栄 6804 => (榮) 69AE
		checkOneTerm(analyzer, "桜", "櫻"); // 桜 685C => (櫻) 6AFB
		checkOneTerm(analyzer, "桟", "棧"); // 桟 685F => (棧) 68E7
		checkOneTerm(analyzer, "梅", "梅"); // 梅 6885 => (梅) FA44
		checkOneTerm(analyzer, "検", "檢"); // 検 691C => (檢) 6AA2
		checkOneTerm(analyzer, "楽", "樂"); // 楽 697D => (樂) 6A02
		checkOneTerm(analyzer, "様", "樣"); // 様 69D8 => (樣) 6A23
		checkOneTerm(analyzer, "横", "橫"); // 横 6A2A => (橫) 6A6B
		checkOneTerm(analyzer, "歓", "歡"); // 歓 6B53 => (歡) 6B61
		checkOneTerm(analyzer, "殻", "殼"); // 殻 6BBB => (殼) 6BBC
		checkOneTerm(analyzer, "気", "氣"); // 気 6C17 => (氣) 6C23
		checkOneTerm(analyzer, "海", "海"); // 海 6D77 => (海) FA45
		checkOneTerm(analyzer, "渇", "渴"); // 渇 6E07 => (渴) 6E34
		checkOneTerm(analyzer, "温", "溫"); // 温 6E29 => (溫) 6EAB
		checkOneTerm(analyzer, "漢", "漢"); // 漢 6F22 => (漢) FA47
		checkOneTerm(analyzer, "権", "權"); // 権 6A29 => (權) 6B0A
		checkOneTerm(analyzer, "横", "橫"); // 横 6A2A =>（橫） 6A6B
		checkOneTerm(analyzer, "欄", "欄"); // 欄 6B04 => (欄) F91D
		checkOneTerm(analyzer, "欠", "缺"); // 欠 6B20 => (缺) 7F3A
		checkOneTerm(analyzer, "歩", "步"); // 歩 6B69 => (步) 6B65
		checkOneTerm(analyzer, "歯", "齒"); // 歯 6B6F => (齒) 9F52
		checkOneTerm(analyzer, "歴", "歷"); // 歴 6B74 => (歷) 6B77
		checkOneTerm(analyzer, "残", "殘"); // 残 6B8B => (殘) 6B98
		checkOneTerm(analyzer, "殺", "殺"); // 殺 6BBA => (殺) F970
		checkOneTerm(analyzer, "殻", "殼"); // 殻 6BBB =>（殼） 6BBC
		checkOneTerm(analyzer, "毎", "每"); // 毎 6BCE => (每) 6BCF
		checkOneTerm(analyzer, "沢", "澤"); // 沢 6CA2 => (澤) 6FA4
		checkOneTerm(analyzer, "浜", "濱"); // 浜 6D5C => (濱) 6FF1
		checkOneTerm(analyzer, "海", "海"); // 海 6D77 => (海) FA45
		checkOneTerm(analyzer, "涙", "淚"); // 涙 6D99 => (淚) 6DDA
		checkOneTerm(analyzer, "渇", "渴"); // 渇 6E07 => (渴) 6E34
		checkOneTerm(analyzer, "済", "濟"); // 済 6E08 => (濟) 6FDF
		checkOneTerm(analyzer, "渉", "涉"); // 渉 6E09 => (涉) 6D89
		checkOneTerm(analyzer, "渋", "澁"); // 渋 6E0B => (澁) 6F81
		checkOneTerm(analyzer, "渓", "溪"); // 渓 6E13 => (溪) 6EAA
		checkOneTerm(analyzer, "温", "溫"); // 温 6E29 => (溫) 6EAB
		checkOneTerm(analyzer, "湿", "濕"); // 湿 6E7F => (濕) 6FD5
		checkOneTerm(analyzer, "満", "滿"); // 満 6E80 => (滿) 6EFF
		checkOneTerm(analyzer, "滝", "瀧"); // 滝 6EDD => (瀧) 7027
		checkOneTerm(analyzer, "滞", "滯"); // 滞 6EDE => (滯) 6EEF
		checkOneTerm(analyzer, "瀬", "瀨"); // 瀬 702C => (瀨) 7028
		checkOneTerm(analyzer, "焼", "燒"); // 焼 713C => (燒) 71D2
		checkOneTerm(analyzer, "煮", "煮"); // 煮 716E => (煮) FA48
		checkOneTerm(analyzer, "犠", "犧"); // 犠 72A0 => (犧) 72A7
		checkOneTerm(analyzer, "猟", "獵"); // 猟 731F => (獵) 7375
		checkOneTerm(analyzer, "獣", "獸"); // 獣 7363 => (獸) 7378
		checkOneTerm(analyzer, "瓶", "甁"); // 瓶 74F6 => (甁) 7501
		checkOneTerm(analyzer, "画", "畫"); // 画 753B => (畫) 756B
		checkOneTerm(analyzer, "畳", "疊"); // 畳 7573 => (疊) 758A
		checkOneTerm(analyzer, "発", "發"); // 発 767A => (發) 767C
		checkOneTerm(analyzer, "県", "縣"); // 県 770C => (縣) 7E23
		checkOneTerm(analyzer, "研", "硏"); // 研 7814 => (硏) 784F
		checkOneTerm(analyzer, "砕", "碎"); // 砕 7815 => (碎) 788E
		checkOneTerm(analyzer, "碑", "碑"); // 碑 7891 => (碑) FA4B
		checkOneTerm(analyzer, "祉", "祉"); // 祉 7949 => (祉) FA4D
		checkOneTerm(analyzer, "祥", "祥"); // 祥 7965 => (祥) FA1A
		checkOneTerm(analyzer, "禍", "禍"); // 禍 798D => (禍) FA52
		checkOneTerm(analyzer, "福", "福"); // 福 798F => (福) FA1B
		checkOneTerm(analyzer, "秘", "祕"); // 秘 79D8 => (祕) 7955
		checkOneTerm(analyzer, "穏", "穩"); // 穏 7A4F => (穩) 7A69
		checkOneTerm(analyzer, "絵", "繪"); // 絵 7D75 => (繪) 7E6A
		checkOneTerm(analyzer, "縁", "緣"); // 縁 7E01 => (緣) 7DE3
		checkOneTerm(analyzer, "缶", "罐"); // 缶 7F36 => (罐) 7F50
		checkOneTerm(analyzer, "稲", "稻"); // 稲 7A32 => (稻) 7A3B
		checkOneTerm(analyzer, "穀", "穀"); // 穀 7A40 => (穀) FA54
		checkOneTerm(analyzer, "穂", "穗"); // 穂 7A42 => (穗) 7A57
		checkOneTerm(analyzer, "突", "突"); // 突 7A81 => (突) FA55
		checkOneTerm(analyzer, "竜", "龍"); // 竜 7ADC => (龍) 9F8D
		checkOneTerm(analyzer, "節", "節"); // 節 7BC0 => (節) FA56
		checkOneTerm(analyzer, "粋", "粹"); // 粋 7C8B => (粹) 7CB9
		checkOneTerm(analyzer, "粛", "肅"); // 粛 7C9B => (肅) 8085
		checkOneTerm(analyzer, "糸", "絲"); // 糸 7CF8 => (絲) 7D72
		checkOneTerm(analyzer, "経", "經"); // 経 7D4C => (經) 7D93
		checkOneTerm(analyzer, "絵", "繪"); // 絵 7D75 =>（繪） 7E6A
		checkOneTerm(analyzer, "継", "繼"); // 継 7D99 => (繼) 7E7C
		checkOneTerm(analyzer, "続", "續"); // 続 7D9A => (續) 7E8C
		checkOneTerm(analyzer, "総", "總"); // 総 7DCF => (總) 7E3D
		checkOneTerm(analyzer, "緑", "綠"); // 緑 7DD1 => (綠) 7DA0
		checkOneTerm(analyzer, "緒", "緖"); // 緒 7DD2 => (緖) 7DD6
		checkOneTerm(analyzer, "練", "練"); // 練 7DF4 => (練) FA57
		checkOneTerm(analyzer, "縄", "繩"); // 縄 7E04 => (繩) 7E69
		checkOneTerm(analyzer, "縦", "縱"); // 縦 7E26 => (縱) 7E31
		checkOneTerm(analyzer, "繁", "繁"); // 繁 7E41 => (繁) FA59
		checkOneTerm(analyzer, "繊", "纖"); // 繊 7E4A => (纖) 7E96
		checkOneTerm(analyzer, "署", "署"); // 署 7F72 => (署) FA5A
		checkOneTerm(analyzer, "翻", "飜"); // 翻 7FFB => (飜) 98DC
		checkOneTerm(analyzer, "者", "者"); // 者 8005 => (者) FA5B
		checkOneTerm(analyzer, "聴", "聽"); // 聴 8074 => (聽) 807D
		checkOneTerm(analyzer, "脳", "腦"); // 脳 8133 => (腦) 8166
		checkOneTerm(analyzer, "臓", "臟"); // 臓 81D3 => (臟) 81DF
		checkOneTerm(analyzer, "臭", "臭"); // 臭 81ED => (臭) FA5C
		checkOneTerm(analyzer, "芸", "藝"); // 芸 82B8 => (藝) 85DD
		checkOneTerm(analyzer, "荘", "莊"); // 荘 8358 => (莊) 838A
		checkOneTerm(analyzer, "著", "著"); // 著 8457 => (著) FA5F
		checkOneTerm(analyzer, "蔵", "藏"); // 蔵 8535 => (藏) 85CF
		checkOneTerm(analyzer, "薫", "薰"); // 薫 85AB => (薰) 85B0
		checkOneTerm(analyzer, "薬", "藥"); // 薬 85AC => (藥) 85E5
		checkOneTerm(analyzer, "虚", "虛"); // 虚 865A => (虛) 865B
		checkOneTerm(analyzer, "虜", "虜"); // 虜 865C => (虜) F936
		checkOneTerm(analyzer, "蛍", "螢"); // 蛍 86CD => (螢) 87A2
		checkOneTerm(analyzer, "蛮", "蠻"); // 蛮 86EE => (蠻) 883B
		checkOneTerm(analyzer, "衛", "衞"); // 衛 885B => (衞) 885E
		checkOneTerm(analyzer, "褐", "褐"); // 褐 8910 => (褐) FA60
		checkOneTerm(analyzer, "褒", "襃"); // 褒 8912 => (襃) 8943
		checkOneTerm(analyzer, "覇", "霸"); // 覇 8987 => (霸) 9738
		checkOneTerm(analyzer, "視", "視"); // 視 8996 => (視) FA61
		checkOneTerm(analyzer, "覚", "覺"); // 覚 899A => (覺) 89BA
		checkOneTerm(analyzer, "観", "觀"); // 観 89B3 => (觀) 89C0
		checkOneTerm(analyzer, "覧", "覽"); // 覧 89A7 => (覽) 89BD
		checkOneTerm(analyzer, "謁", "謁"); // 謁 8B01 => (謁) FA62
		checkOneTerm(analyzer, "訳", "譯"); // 訳 8A33 => (譯) 8B6F
		checkOneTerm(analyzer, "証", "證"); // 証 8A3C => (證) 8B49
		checkOneTerm(analyzer, "誉", "譽"); // 誉 8A89 => (譽) 8B7D
		checkOneTerm(analyzer, "読", "讀"); // 読 8AAD => (讀) 8B80
		checkOneTerm(analyzer, "諸", "諸"); // 諸 8AF8 => (諸) FA22
		checkOneTerm(analyzer, "謡", "謠"); // 謡 8B21 => (謠) 8B20
		checkOneTerm(analyzer, "謹", "謹"); // 謹 8B39 => (謹) FA63
		checkOneTerm(analyzer, "譲", "讓"); // 譲 8B72 => (讓) 8B93
		checkOneTerm(analyzer, "豊", "豐"); // 豊 8C4A => (豐) 8C50
		checkOneTerm(analyzer, "賓", "賓"); // 賓 8CD3 => (賓) FA64
		checkOneTerm(analyzer, "賛", "贊"); // 賛 8CDB => (贊) 8D0A
		checkOneTerm(analyzer, "贈", "贈"); // 贈 8D08 => (贈) FA65
		checkOneTerm(analyzer, "転", "轉"); // 転 8EE2 => (轉) 8F49
		checkOneTerm(analyzer, "軽", "輕"); // 軽 8EFD => (輕) 8F15
		checkOneTerm(analyzer, "辺", "邊"); // 辺 8FBA => (邊) 908A
		checkOneTerm(analyzer, "逓", "遞"); // 逓 9013 => (遞) 905E
		checkOneTerm(analyzer, "逸", "逸"); // 逸 9038 => (逸）FA67
		checkOneTerm(analyzer, "遅", "遲"); // 遅 9045 => (遲) 9072
		checkOneTerm(analyzer, "郷", "鄕"); // 郷 90F7 => (鄕) 9115
		checkOneTerm(analyzer, "郎", "郞"); // 郎 90CE => (郞) 90DE
		checkOneTerm(analyzer, "都", "都"); // 都 90FD => (都) FA26
		checkOneTerm(analyzer, "酔", "醉"); // 酔 9154 => (醉) 9189
		checkOneTerm(analyzer, "醸", "釀"); // 醸 91B8 => (釀) 91C0
		checkOneTerm(analyzer, "釈", "釋"); // 釈 91C8 => (釋) 91CB
		checkOneTerm(analyzer, "鉄", "鐵"); // 鉄 9244 => (鐵) 9435
		checkOneTerm(analyzer, "鉱", "鑛"); // 鉱 9271 => (鑛) 945B
		checkOneTerm(analyzer, "銭", "錢"); // 銭 92AD => (錢) 9322
		checkOneTerm(analyzer, "錬", "鍊"); // 錬 932C => (鍊) 934A
		checkOneTerm(analyzer, "録", "錄"); // 録 9332 => (錄) 9304
		checkOneTerm(analyzer, "鎮", "鎭"); // 鎮 93AE => (鎭) 93AD
		checkOneTerm(analyzer, "関", "關"); // 関 95A2 => (關) 95DC
		checkOneTerm(analyzer, "闘", "鬪"); // 闘 95D8 => (鬪) 9B2A
		checkOneTerm(analyzer, "陥", "陷"); // 陥 9665 => (陷) 9677
		checkOneTerm(analyzer, "険", "險"); // 険 967A => (險) 96AA
		checkOneTerm(analyzer, "隆", "隆"); // 隆 9686 => (隆) F9DC
		checkOneTerm(analyzer, "隠", "隱"); // 隠 96A0 => (隱) 96B1
		checkOneTerm(analyzer, "雑", "雜"); // 雑 96D1 => (雜) 96DC
		checkOneTerm(analyzer, "難", "難"); // 難 96E3 => (難) FA68
		checkOneTerm(analyzer, "霊", "靈"); // 霊 970A => (靈) 9748
		checkOneTerm(analyzer, "響", "響"); // 響 97FF => (響) FA69
		checkOneTerm(analyzer, "頻", "頻"); // 頻 983B => (頻) FA6A
		checkOneTerm(analyzer, "頼", "賴"); // 頼 983C => (賴) 8CF4
		checkOneTerm(analyzer, "顕", "顯"); // 顕 9855 => (顯) 986F
		checkOneTerm(analyzer, "類", "類"); // 類 985E => (類) F9D0
		checkOneTerm(analyzer, "駅", "驛"); // 駅 99C5 => (驛) 9A5B
		checkOneTerm(analyzer, "駆", "驅"); // 駆 99C6 => (驅) 9A45
		checkOneTerm(analyzer, "騒", "騷"); // 騒 9A12 => (騷) 9A37
		checkOneTerm(analyzer, "験", "驗"); // 験 9A13 => (驗) 9A57
		checkOneTerm(analyzer, "髪", "髮"); // 髪 9AEA => (髮) 9AEE
		checkOneTerm(analyzer, "鶏", "鷄"); // 鶏 9D8F => (鷄) 9DC4
		checkOneTerm(analyzer, "麦", "麥"); // 麦 9EA6 => (麥) 9EA5
		checkOneTerm(analyzer, "黄", "黃"); // 黄 9EC4 => (黃) 9EC3
		checkOneTerm(analyzer, "黒", "黑"); // 黒 9ED2 => (黑) 9ED1
		checkOneTerm(analyzer, "黙", "默"); // 黙 9ED9 => (默) 9ED8
		checkOneTerm(analyzer, "齢", "齡"); // 齢 9F62 => (齡) 9F61
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

	void assertTermEquals(String expected, TokenStream stream, CharTermAttribute termAtt) throws Exception
	{
		assertTrue(stream.incrementToken());
		assertEquals(expected, termAtt.toString());
	}



}
