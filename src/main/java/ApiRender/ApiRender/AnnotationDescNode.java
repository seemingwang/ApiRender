package ApiRender.ApiRender;
import com.sun.javadoc.*;
import com.sun.javadoc.AnnotationDesc.*;
public class AnnotationDescNode extends BaseNode {
	static {
		freeMarkerPath.put(AnnotationDescNode.class,"ftl/AnnotationDescNode/AnnotationDescNode.ftl");
		cssPath.put(AnnotationDescNode.class,"ftl/AnnotationDescNode/AnnotationDescNode.css");		
	}
	private AnnotationDesc a;
	AnnotationDescNode(AnnotationDesc a){
		this.a = a;
	}
	public String annotationType(){
		if(a != null){
			String s = a.annotationType().toString();
			int last = s.lastIndexOf(".");
			if(last < 0 || last >= s.length()){
				return s;
			}
			return s.substring(last + 1);
		}
		return "";
	}
	public String value(){
		if(a != null){
			ElementValuePair[] s = a.elementValues();
			if(s.length > 0) {
				return s[0].value().toString();
			}
			return "";
		}
		return "";
	}
	
}
