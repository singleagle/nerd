package com.enjoy.nerd.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class EmotionUtil {

	public static String[] emotion_strs = new String[] { "[微笑]", "[撇嘴]", "[色]",
			"[发呆]", "[得意]", "[流泪]", "[害羞]", "[闭嘴]", "[睡]", "[大哭]", "[尴尬]",
			"[发怒]", "[调皮]", "[呲牙]", "[惊讶]", "[难过]", "[酷]", "[冷汗]", "[抓狂]",
			"[吐]", "[偷笑]", "[愉快]", "[白眼]", "[傲慢]", "[饥饿]", "[困]", "[惊恐]",
			"[流汗]", "[憨笑]", "[悠闲]", "[奋斗]", "[咒骂]", "[疑问]", "[嘘]", "[晕]",
			"[疯了]", "[衰]", "[骷髅]", "[敲打]", "[再见]", "[擦汗]", "[抠鼻]", "[鼓掌]",
			"[糗大了]", "[坏笑]", "[左哼哼]", "[右哼哼]", "[哈欠]", "[鄙视]", "[委屈]", "[快哭了]",
			"[阴险]", "[亲亲]", "[吓]", "[可怜]", "[菜刀]", "[西瓜]", "[啤酒]", "[篮球]",
			"[乒乓]", "[咖啡]", "[饭]", "[猪头]", "[玫瑰]", "[凋谢]", "[嘴唇]", "[爱心]",
			"[心碎]", "[蛋糕]", "[闪电]", "[炸弹]", "[刀]", "[足球]", "[瓢虫]", "[便便]",
			"[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]", "[弱]", "[握手]", "[胜利]",
			"[抱拳]", "[勾引]", "[拳头]", "[差劲]", "[爱你]", "[NO]", "[OK]", "[爱情]",
			"[飞吻]", "[跳跳]", "[发抖]", "[怄火]", "[转圈]", "[磕头]", "[回头]", "[跳绳]",
			"[投降]", "[激动]", "[乱舞]", "献吻", "[左太极]", "[右太极]" };

	public static String[] emotion_encodes = new String[] { "/::)", "/::~",
			"/::B", "/::|", "/:8-)", "/::<", "/::$", "/::X", "/::Z", "/::'(",
			"/::-|", "/::", "/::P", "/::D", "/::O", "/::(", "/::+", "/:--b",
			"/::Q", "/::T", "/:,@P", "/:,@-D", "/::d", "/:,@o", "/::g",
			"/:|-)", "/::!", "/::L", "/::>", "/::,", "/:,@f", "/::-S", "/:?",
			"/:,@x", "/:,@", "/::8", "/:,@!", "/:!!!", "/:xx", "/:bye",
			"/:wipe", "/:dig", "/:handclap", "/:&-(", "/:B-)", "/:<", "/:@>",
			"/::-O", "/:>-|", "/:P-(", "/::'|", "/:X-)", "/::*", "/:@x",
			"/:8*", "/:pd", "/:<W>", "/:beer", "/:basketb", "/:oo", "/:coffee",
			"/:eat", "/:pig", "/:rose", "/:fade", "/:showlove", "/:heart",
			"/:break", "/:cake", "/:li", "/:bome", "/:kn", "/:footb",
			"/:ladybug", "/:shit", "/:moon", "/:sun", "/:gift", "/:hug",
			"/:strong", "/:weak", "/:share", "/:v", "/:@)", "/:jj", "/:@",
			"/:bad", "/:lvu", "/:no", "/:ok", "/:love", "/:<L>", "/:jump",
			"/:shake", "/:<O>", "/:circle", "/:kotow", "/:turn", "/:skip",
			"/:oY", "/:#-0", "/:hiphot", "/:kiss", "/:<&", "/:&>" };
	
	private static HashMap<String, Integer> emotionMap = null;
	
	public static Pattern pattern = Pattern.compile("(\\[[^\\]]+\\])");
	
	public static void initEmotionMap(){
		emotionMap = new HashMap<String, Integer>();
		for (int i = 0; i < emotion_strs.length; i++) {
			emotionMap.put(emotion_strs[i], i+1);
		}
	}
	
	public static int mapEmotionInteger(String key){
		if(emotionMap == null){
			initEmotionMap();
		}
		Integer i = emotionMap.get(key);
		if(i != null){
			return i.intValue();
		}
		return 0;
	}
	
	public static int[] matchString(String content){
		Matcher matcher = pattern.matcher(content);
		int[] result = new int[2];
		while(matcher.find()){
			result[0] = matcher.start();
			result[1] = matcher.end();
		}
		return result;
	}
	
	/*public static int[] matchStringStart(String content){
		Matcher matcher = pattern.matcher(content);
		int[] result = new int[2];
		result[0] = 0;
		result[1] = 0;
		if(matcher.find()){
			result[0] = matcher.start();
			result[1] = matcher.end();
		}
		return result;
	}*/
	
	public static Bitmap getBitmapFromAssets(AssetManager am, String content){
		int index = mapEmotionInteger(content);
		if( index != 0){
			String fileName = "qq/" + index + ".png";
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(am.open(fileName));
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
