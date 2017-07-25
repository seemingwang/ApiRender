package ApiRender.ApiRender;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.sun.javadoc.*;
import com.sun.javadoc.AnnotationDesc.*;
public class MethodDocNode extends BaseNode{
	static {
		freeMarkerPath.put(MethodDocNode.class,"ftl/MethodDocNode/MethodDocNode.ftl");
		cssPath.put(MethodDocNode.class,"ftl/MethodDocNode/MethodDocNode.css");
	}
	private ArrayList<ClassDisplayNode> params;
	public ArrayList<ClassDisplayNode> getParams() {
		return params;
	}
	public void setParams(ArrayList<ClassDisplayNode> params) {
		this.params = params;
	}
	private ArrayList<AnnotationDescNode> annotationList; 
	public ArrayList<AnnotationDescNode> getAnnotationList() {
		return annotationList;
	}
	public void setAnnotationList(ArrayList<AnnotationDescNode> annotationList) {
		this.annotationList = annotationList;
	}
	private MethodDoc m;
	public MethodDoc getM() {
		return m;
	}
	public void setM(MethodDoc m) {
		this.m = m;
	}
	private String basePath;
	public String getBasePath(){
		if(basePath != null){
			return basePath;
		}
		ClassDocNode f = (ClassDocNode)getFather();
		if(f == null){
			basePath = "";
			return basePath;
		}
		String t =  f.getBasePath();
		for(AnnotationDescNode a : annotationList){
			String ty = a.annotationType();
			if(ty.equals("Path")){
				String p = a.value();
				if(p.indexOf("\"") == 0){
					p = p.substring(1);
					if(p.length() > 0 && p.charAt(p.length() - 1) == '"') {
						p = p.substring(0, p.length() - 1);
					}
				}
				if(p.indexOf("/") == 0) {
					p = p.substring(1);
				}
				basePath = t + p;
				return basePath;
			}
		}
		basePath = t;
		return t;
			
	}
	MethodDocNode(MethodDoc m, Method m1){
		annotationList = new ArrayList<AnnotationDescNode>();
		if(m == null){
			return;
		}
		AnnotationDesc [] ad = m.annotations();
		for(AnnotationDesc a : ad){
			AnnotationDescNode ac = new AnnotationDescNode(a);
			ac.setFather(this);
			annotationList.add(ac);
			getChildren().add(ac);
		}
		this.m = m;	
		if(m1 != null){
			ClassDisplayNode cd = ClassDisplayNode.createByField(m1);
			getChildren().add(cd);
			getNamedChildren().put("return", cd);
			setParams(ClassDisplayNode.createParams(m1));
			for(ClassDisplayNode c: getParams()){
				getChildren().add(c);
			}
		}
	}
	MethodDocNode(MethodDoc m){
		annotationList = new ArrayList<AnnotationDescNode>();
		if(m == null){
			return;
		}
		AnnotationDesc [] ad = m.annotations();
		for(AnnotationDesc a : ad){
			AnnotationDescNode ac = new AnnotationDescNode(a);
			ac.setFather(this);
			annotationList.add(ac);
			this.getChildren().add(ac);
		}
		this.m = m;
		
	}
}
