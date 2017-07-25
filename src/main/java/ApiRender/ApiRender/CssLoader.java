package ApiRender.ApiRender;

import java.util.HashMap;

public class CssLoader {
	/*
	 * 0,1,0,0
	 * 1,1,2,1
	 *
	 */
	public static HashMap<Object, String> cssStyleForClass = new HashMap<Object, String>();
	public static HashMap<Object, String> cssMap = new HashMap<Object,String>();
	public static HashMap<Object, HashMap<String,String> > classMap = new HashMap<Object, HashMap<String,String> >();
	public static String getClassName(Object o,String name){
		Class c = o.getClass();
		if(classMap.get(c) == null){
			classMap.put(c, new HashMap<String,String>());
		}
		HashMap<String,String> hm = classMap.get(c);
		if(hm.get(name) == null) {
			hm.put(name, name + IdGenerator.getNewId());
		}
		return hm.get(name);
		
	}
	public static int convert[][] = {{0,1,0,0},
			{1,1,2,1}};
	static public int getType(char t){
		if(t == '{') {
			return 1;
		} else if(t == '}') {
			return 2;
		} else {
			return 0;
		}
	}
	static public boolean isBlank(char t){
		if(t == ' ' || t == '\t' || t == '\n' || t == '\r'){
			return true;
		}
		return false;
	}
	public static String convert(String ids,String cssStr){
		String res = "";
		if(ids == null || ids.equals("") == true || cssStr == null || cssStr.equals("") == true){
			return res;
		}
		int stat = 0;
		int ind = 0;
		int st = -1;
		while(ind < cssStr.length()){
			if(st == -1){
				st = ind;
			}
			stat = convert[stat][getType(cssStr.charAt(ind))];
			if(stat == 2){
				res += ids + " " + cssStr.substring(st,ind + 1) + "\n";
				st = -1;
				stat = 0;
				ind++;
				while(ind < cssStr.length() && isBlank(cssStr.charAt(ind))){
					ind++;
				}
			} else {
				ind++;
			}
		}
		return res;		
	}
	public static String convert2(BaseNode o, String cssStr){
		String res = "";
		String buf = "";
		int ind = 0;
		int get = 0;
		while(ind < cssStr.length()){
			if(get == 0){
				if(cssStr.charAt(ind) != '.'){
					res += cssStr.charAt(ind);
				} else {
					get = 1;
					buf = "";
				}
				ind ++;
			} else {
				char t = cssStr.charAt(ind);
				if(isBlank(t) || t == '{' || t == '[' || t == '>' || t == ':') {
					res += "." + getClassName(o,buf)  + t;
					get = 0;
				} else {
					buf += t;
				}
				ind++;
			}
		}
		return res;
	}
}
