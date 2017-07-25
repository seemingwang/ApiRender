package ApiRender.ApiRender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

public abstract class BaseNode{

	private HashMap<String,Object> namedChildren; 
	public HashMap<String, Object> getNamedChildren() {
		return namedChildren;
	}
	public static HashMap<Object, String> freeMarkerPath,cssPath;
	static {
		freeMarkerPath = new HashMap<Object,String>();
		cssPath = new HashMap<Object,String>();
	}
	public void setNamedChildren(HashMap<String, Object> namedChildren) {
		this.namedChildren = namedChildren;
	}
	private ArrayList<BaseNode> children;
	private BaseNode father;
	public BaseNode getFather() {
		return father;
	}
	public void setFather(BaseNode father) {
		this.father = father;
	}
	private String cssBuff,htmlBuff;
	private boolean cssSet,htmlSet;
	private String nodeId;
	public BaseNode(){
		namedChildren = new HashMap<String,Object>();
		cssSet = false;
		cssBuff = "";
		nodeId = IdGenerator.getNewId();
		htmlBuff = "";
		htmlSet = false;
		children = new ArrayList<BaseNode>();
	}
	public String getFreeMarkerPath(){
		return freeMarkerPath.get(this.getClass());
	}
	public String getCssPath(){
		return cssPath.get(this.getClass());
	}
	public String getHtml(){
		if(htmlSet){
			return htmlBuff;
		}
		htmlBuff = "";
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
		cfg.setDefaultEncoding("UTF-8");
		try {
			cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), "/");
			Template t = cfg.getTemplate(getFreeMarkerPath());
			StringWriter stringWriter = new StringWriter();  
			BufferedWriter writer = new BufferedWriter(stringWriter);
			t.process(this, writer);
			htmlBuff = stringWriter.toString();
			htmlSet = true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
		return htmlBuff;
	}
	public ArrayList<BaseNode> getChildren() {
		// TODO Auto-generated method stub
		return children;
	}

	public String getNodeId() {
		// TODO Auto-generated method stub
		return nodeId;
	}

	public String getField(String name) {
		// TODO Auto-generated method stub
		Object o = namedChildren.get(name);
		if(o instanceof String){
			return (String)o;
		}
		if(o instanceof BaseNode){
			return ((BaseNode) o).getHtml();
		}
		return "";
	}
	public static void getCssBySearch(BaseNode o, HashMap<Object,String> set){
		if(set.containsKey(o.getClass()) == false) {
			String cssBuff1 = "";
			if(o.getCssPath() != null && o.getCssPath().equals("") == false){
				try {
					String content = "";
					InputStream is= o.getClass().getResourceAsStream("/" + o.getCssPath());
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String data = null;
					while((data = br.readLine())!=null)
					{
						content += data; 
					}
					cssBuff1 = CssLoader.convert2(o, content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				set.put(o.getClass(),cssBuff1);
				for(String t: o.namedChildren.keySet()){
					Object obj = o.namedChildren.get(t);
					if(obj instanceof BaseNode){
						getCssBySearch((BaseNode)obj, set);
					}
				}
				for(BaseNode f: o.children){
					getCssBySearch(f, set);
				}
				
			} else {
				set.put(o, "");
			}
		}
	}
	public String getCss() {
		HashMap<Object,String> hs = new HashMap<Object,String>();
		getCssBySearch(this, hs);
		String res = "";
		for(Object o : hs.keySet()){
			res += hs.get(o);
		}
		return res;
	}
	public String getCssName(String name) {
		
		return CssLoader.getClassName(this, name);
	}

}
