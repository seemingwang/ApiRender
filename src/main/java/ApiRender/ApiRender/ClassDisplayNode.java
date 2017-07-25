package ApiRender.ApiRender;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import com.sun.tools.javac.util.List;
import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

public class ClassDisplayNode extends BaseNode{
	public static RootDoc rootDoc;
	static {
		freeMarkerPath.put(ClassDisplayNode.class,"ftl/ClassDisplayNode/ClassDisplayNode.ftl");
		cssPath.put(ClassDisplayNode.class,"ftl/ClassDisplayNode/ClassDisplayNode.css");
	}
	public Class<?> myClass;

	public static String replaceTypeParam(String p, HashMap<String,String> map){
		if(p == null) {
			return "";
		}
		if(map == null){
			return p;
		}
		p = p.replaceAll(" ", "");
		String res = "";
		int start = 0;
		int end;
		boolean ready = true;
		while(start < p.length()){
			if((start == 0 || p.charAt(start - 1) == '<' || p.charAt(start -1) == ',' ) && p.charAt(start) >= 'A' && p.charAt(start) <= 'Z' && (start == p.length() - 1 || p.charAt(start + 1) == ',' || p.charAt(start + 1) == '>')){
				String key = "";
				key += p.charAt(start);
				if(map.get(key) != null){
					res += map.get(key);
				} else {
					res += key;
				}
			} else {
				res += p.charAt(start);
			}
			start++;
		}
		return res;
	}
	public static String getClassNameFromParam(String a){
		if(a == null){
			return "";
		}
		int index = a.indexOf("<");
		if(index == -1){
			return a;
		}
		return a.substring(0,index);
	}
	public static ArrayList<String> parseParam(String pattern, HashMap<String, String> map){
		ArrayList<String> res = new ArrayList<String> ();
		if(pattern == null){
			return res;
		}
		int st = pattern.indexOf('<');
		int ed = pattern.lastIndexOf('>');
		if(st == -1 || ed == -1 || st > ed){
			res.add(pattern);
			return res;
		}
		pattern = pattern.substring(st + 1, ed);
		if(map != null){
			pattern = replaceTypeParam(pattern, map);
		} else {
			pattern = pattern.replace(" ", "");
		}
		int start = 0;
		int end;
		while(start < pattern.length()){
			end = start;
			int count = 0;
			while(end < pattern.length()) {
				char c = pattern.charAt(end);
				if(c == ',') {
					if(count == 0){
						res.add(pattern.substring(start, end));
						break;
					}
				} else if(c == '<') {
					count++;
				} else if(c == '>') {
					count--;
				}
				end++;
			}
			if(end == pattern.length()){
				if(count == 0){
					res.add(pattern.substring(start, end));
				}
				break;
			}
			start = end + 1;
		}
//		for(int i = 0;i < res.size();i++) {
//			System.out.println("parseResult " + res.get(i));
//		}
		return res;
	}
	public static HashMap<String,String> createMapFromParam(Class<?> c, String p){
		
		if(c == null || p == null || p.length() == 0){
			return null;
		}
		java.lang.reflect.TypeVariable<?>[] tv = c.getTypeParameters();
		ArrayList<String> res = parseParam(p, null);
		if(c.isArray()) {
			tv = c.getComponentType().getTypeParameters();
		}		
		if(res.size() > 0) {
			if(tv.length == res.size()){
				HashMap<String,String> hm = new HashMap<String,String>();
				for(int i = 0;i < tv.length;i++){
					hm.put(tv[i].toString(), res.get(i));
				}
				return hm;
			} else {	
				return null;
			}
		}		
		return null;
	}
	public static ArrayList<ClassDisplayNode> createParams(Method mc2){
		java.lang.reflect.Type [] tp = mc2.getGenericParameterTypes();
		Class<?>[] cl = mc2.getParameterTypes();
		ArrayList<ClassDisplayNode> res = new ArrayList<ClassDisplayNode>();
		for(int i = 0;i < tp.length;i++){
			res.add(createNodeByTypeInfo(tp[i],cl[i]));
		}
		return res;
		
	}
	public static ClassDisplayNode createNodeByTypeInfo(java.lang.reflect.Type tp, Class<?> cl){
		String s = tp.toString();
		java.lang.reflect.TypeVariable<?>[] tv2 = cl.getTypeParameters();	
				if(s.length() > 0 && s.charAt(s.length() - 1) == '>' && s.contains("<")) {
					String[] subString = s.substring(s.lastIndexOf('<') + 1,s.length() - 1).split(",");
					if(tv2.length == subString.length){
						HashMap<String,String> hm = new HashMap<String,String>();
						for(int i1 = 0;i1 < tv2.length;i1++){
							hm.put(tv2[i1].toString(), subString[i1].replace(" ", ""));
						}
						return new ClassDisplayNode(cl,hm,false,null,null);
					} else {
						return new ClassDisplayNode(cl,null,false,null,null);
					}
				} else {
					return new ClassDisplayNode(cl,null,false,null,null);
				}
	}
	public static ClassDisplayNode createByField(Method mc2){
		return createNodeByTypeInfo(mc2.getGenericReturnType(),mc2.getReturnType());
//		String s = mc2.getGenericReturnType().toString();
//		java.lang.reflect.TypeVariable<?>[] tv2 = mc2.getReturnType().getTypeParameters();
//		if(s.length() > 0 && s.charAt(s.length() - 1) == '>' && s.contains("<")) {
//			String[] subString = s.substring(s.lastIndexOf('<') + 1,s.length() - 1).split(",");
//			if(tv2.length == subString.length){
//				HashMap<String,String> hm = new HashMap<String,String>();
//				for(int i1 = 0;i1 < tv2.length;i1++){
//					hm.put(tv2[i1].toString(), subString[i1].replace(" ", ""));
//				}
//				return new ClassDisplayNode(mc2.getReturnType(),hm,false,null,null);
//			} else {
//				return new ClassDisplayNode(mc2.getReturnType(),null,false,null,null);
//			}
//		} else {
//			return new ClassDisplayNode(mc2.getReturnType(),null,false,null,null);
//		}
	}
	ClassDisplayNode(Class<?> c, HashMap<String,String> map, boolean ends, String comment, String fieldName){
		//System.out.println("in classDisplay Node " + c.getName() + " ends " + ends);
		myClass = c;
		ClassDoc cd = null;
		if(rootDoc != null){
			cd = rootDoc.classNamed(c.getName());
		}
		HashMap<String, String> fieldComment = new HashMap<String, String> ();
		if(cd != null){
			for(FieldDoc dd :cd.fields(false)) {
				if(!dd.getRawCommentText().equals("")){
					fieldComment.put(dd.name(), dd.getRawCommentText());
				}
			}
			if(comment == null && cd.getRawCommentText().equals("") == false){
				comment = cd.getRawCommentText();
			}
		}
		if(comment != null){	
			getNamedChildren().put("comment", comment);
		}
		this.getNamedChildren().put("name", c.getName());
		if(fieldName != null){
			this.getNamedChildren().put("fieldName", fieldName);
			//System.out.println("set field name " + fieldName);
		}
		
		//System.out.println("class name " + c.getName());
//		if(cd!=null) {
//			System.out.println("document name " + cd.name());
//		}
		getNamedChildren().put("separator", ",");
		getNamedChildren().put("startWrapper", "{");
		getNamedChildren().put("endWrapper", "}");
		if(ends == true){
			getNamedChildren().put("final", "true");
			return;
		}
		getNamedChildren().put("final", "false");
		if(c.isPrimitive() || c == Object.class || c == String.class || c == Character.class || c == Boolean.class || c == Byte.class || c == Short.class || c == Integer.class || c == Long.class || c == Float.class || c == Double.class || 
				c == java.math.BigDecimal.class || c == java.util.Date.class || c.isEnum()) {
			//System.out.println("base Class");
			
			getNamedChildren().put("final", "true");
		}
		else if(c.isArray()){
			getNamedChildren().put("final", "false");
			getNamedChildren().put("startWrapper", "[");
			getNamedChildren().put("endWrapper", "]");
			java.lang.reflect.TypeVariable<?>[] tv = c.getComponentType().getTypeParameters();
//			for(java.lang.reflect.TypeVariable<?> t : tv){
//				System.out.println(" typeVariable " + t);
//			}
//			System.out.println("iterate map");
//			if(map != null){
//				for(String k : map.keySet()){
//					System.out.println(k + " ---- " + map.get(k));
//				}				
//			}
			getChildren().add(new ClassDisplayNode(c.getComponentType(),map,ends,comment,null));
//			if(tv.length == 0){
//				getChildren().add(new ClassDisplayNode(c.getComponentType(),null,false,null));
//			} else {
//				
//			}
			//c.getComponentType().getTypeParameters();
		}
		else if(java.util.List.class.isAssignableFrom(c)) {
			//System.out.println("from List");
			getNamedChildren().put("final", "false");
			getNamedChildren().put("startWrapper", "[");
			getNamedChildren().put("endWrapper", "]");
			if(map == null){
				getNamedChildren().put("final", "true");
			} else {
				getNamedChildren().put("separator", ":");
				java.lang.reflect.TypeVariable<?>[] tv = c.getTypeParameters();
				for(java.lang.reflect.TypeVariable<?> t : tv){
//					System.out.println(t);
//					System.out.println(t.getClass());
//					java.lang.reflect.Type[] sss = t.getBounds();
//					for(java.lang.reflect.Type s:sss){
//						
//						System.out.println("type s" + s);
//					}
					
						try {
							if(map.get(t.toString()) != null){
								//System.out.println("get for name " + map.get(t.toString()));
								Class<?> c1;
								try {
									c1 = Class.forName(getClassNameFromParam(map.get(t.toString())));
								} catch(Exception e) {
									c1 = Object.class;
								}
								getChildren().add(new ClassDisplayNode(c1,createMapFromParam(c1, map.get(t.toString())),false,null,null));
							} else {
								getChildren().add(new ClassDisplayNode(Object.class,null,false,null,null));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
				}			
		}
		else if( java.util.Map.class.isAssignableFrom(c)) {
			getNamedChildren().put("final", "false");
			if(map == null){
				getNamedChildren().put("final", "true");
				
			} else {
				getNamedChildren().put("separator", ":");
				java.lang.reflect.TypeVariable<?>[] tv = c.getTypeParameters();
				for(java.lang.reflect.TypeVariable<?> t : tv){
						try {
							if(map.get(t.toString()) != null){
								Class<?> c1;
								try {
									c1 = Class.forName(getClassNameFromParam(map.get(t.toString())));
								} catch(Exception e) {
									c1 = Object.class;
								}
								getChildren().add(new ClassDisplayNode(c1,createMapFromParam(c1, map.get(t.toString())),false,null,null));
							} else {
								getChildren().add(new ClassDisplayNode(Object.class,null,false,null,null));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} 
				}

		}
		else if(java.util.Set.class.isAssignableFrom(c)) {
			//System.out.println("from Set");
			getNamedChildren().put("final", "false");
			if(map == null){
				getNamedChildren().put("final", "true");
			} else {
				getNamedChildren().put("separator", ":");
				java.lang.reflect.TypeVariable<?>[] tv = c.getTypeParameters();
				for(java.lang.reflect.TypeVariable<?> t : tv){
					//System.out.println(t);
					
						try {
							if(map.get(t.toString()) != null){
								Class<?> c1;
								try {
									c1 = Class.forName(getClassNameFromParam(map.get(t.toString())));
								} catch(Exception e) {
									c1 = Object.class;
								}
								getChildren().add(new ClassDisplayNode(c1,createMapFromParam(c1, map.get(t.toString())),false,null,null));
							} else {
								getChildren().add(new ClassDisplayNode(Object.class,null,false,null,null));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
				}
		} else {
			//System.out.println("complex");
			java.lang.reflect.Field[] fields = c.getDeclaredFields();
			if(c.getName().indexOf("Headers") >= 0) {
				System.out.println("***********************field "+ fields.length);
			}
			for(java.lang.reflect.Field f : fields) {
				if(c.getName().indexOf("Headers") >= 0) {
					System.out.println("***********************field "+ f.getName() + "   " + f.toString());
				}
				if(f.toString().indexOf("static") != -1){
					continue;
				}
				String gt = f.getGenericType().toString();
				if(gt.length() == 1 && gt.charAt(0) >= 'A' && gt.charAt(0) <= 'Z'){
					try {
						if(map != null && map.get(gt) != null){
							Class<?> c1;
							try {
								c1 = Class.forName(getClassNameFromParam(map.get(gt.toString())));
							} catch(Exception e) {
								c1 = Object.class;
							}
							getChildren().add(new ClassDisplayNode(c1,createMapFromParam(c1, map.get(gt.toString())),false,fieldComment.get(f.getName()),f.getName()));
						} else {
							getChildren().add(new ClassDisplayNode(f.getClass(),null,true,fieldComment.get(f.getName()),f.getName()));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					continue;
				}
				Class<?> child = f.getType();
				java.lang.reflect.TypeVariable<?>[] tv = child.getTypeParameters();
				String s = f.getGenericType().toString();
				ArrayList<String> res = parseParam(s, map);
				if(child.isArray()) {
					tv = child.getComponentType().getTypeParameters();
				}
				if(res.size() > 0) {
					if(tv.length == res.size()){
						HashMap<String,String> hm = new HashMap<String,String>();
						for(int i = 0;i < tv.length;i++){
							hm.put(tv[i].toString(), res.get(i));
						}
						getChildren().add(new ClassDisplayNode(child,hm,false,fieldComment.get(f.getName()),f.getName()));
					} else {
//						System.out.println("length not equal");
//						for(int sss = 0;sss < tv.length;sss++){
//						System.out.println(sss + " tv " + tv[sss]);
//						}
//						for(int sss = 0;sss < res.size();sss++){
//						System.out.println(sss+ " res  " + res.get(sss));
//						}
						getChildren().add(new ClassDisplayNode(child,null,true,fieldComment.get(f.getName()),f.getName()));
					}
				} else {
					getChildren().add(new ClassDisplayNode(child,null,false,fieldComment.get(f.getName()),f.getName()));
				}
			}
		}
	}
}
