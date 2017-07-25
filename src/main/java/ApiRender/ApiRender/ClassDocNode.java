package ApiRender.ApiRender;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javadoc.*;
import com.sun.javadoc.AnnotationDesc.*;
public class ClassDocNode extends BaseNode{
	static {
		freeMarkerPath.put(ClassDocNode.class,"ftl/ClassDocNode/ClassDocNode.ftl");
		cssPath.put(ClassDocNode.class,"ftl/ClassDocNode/ClassDocNode.css");
	}
	private ArrayList<MethodDocNode> methodList;
	public ArrayList<MethodDocNode> getMethodList() {
		return methodList;
	}
	public void setMethodList(ArrayList<MethodDocNode> methodList) {
		this.methodList = methodList;
	}
	private ClassDoc classDoc;
	public ClassDoc getClassDoc() {
		return classDoc;
	}
	public void setClassDoc(ClassDoc classDoc) {
		this.classDoc = classDoc;
	}
	private String basePath;
	public String getBasePath(){
		if(basePath != null){
			return basePath;
		}
		for(AnnotationDesc a: classDoc.annotations()){
			if(a.annotationType().toString().equals("javax.ws.rs.Path") && a.toString().indexOf("@javax.ws.rs.Path(") == 0) {
				int firstIndex = a.toString().indexOf("/");
				if(firstIndex != -1 && firstIndex < a.toString().length() - 2) {
					basePath = a.toString().substring(firstIndex, a.toString().length() - 2);
					return basePath;
				}
			}
		}
		return "/";
	}
	ClassDocNode(ClassDoc classDoc){
        Class<?> objClass = null;
		try {
			
			objClass = Class.forName(classDoc.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			objClass = null;
			e.printStackTrace();
		}
        //System.out.println(objClass);
		methodList = new ArrayList<MethodDocNode>();
		MethodDoc[] methods = classDoc.methods();
        if(objClass != null){
	        java.lang.reflect.Field[] fields = objClass.getDeclaredFields();
	        MethodDoc[] md3 = classDoc.methods();
	        java.lang.reflect.Method [] md4 = objClass.getDeclaredMethods();
	        HashMap<String, ArrayList<java.lang.reflect.Method>> hpp = new HashMap<String, ArrayList<java.lang.reflect.Method>>();
	        
	        for(java.lang.reflect.Method m: objClass.getDeclaredMethods()){
	          
	          if(hpp.containsKey(m.getName())){
	            hpp.get(m.getName()).add(m);
	          } else {
	            ArrayList<java.lang.reflect.Method> ja = new ArrayList<java.lang.reflect.Method> ();
	            ja.add(m);
	            hpp.put(m.getName(), ja);
	          }
	        }
	          for(MethodDoc mq3: md3){
	              Parameter [] p = mq3.parameters();
	              java.lang.reflect.Method matchedMethod = null;
	              if(hpp != null && hpp.get(mq3.name()) != null){
	                for(java.lang.reflect.Method m: hpp.get(mq3.name())){
	                  Class<?> cp [] =  m.getParameterTypes();
	                  if(cp.length != p.length){
	                    continue;
	                  }
	                  boolean matched = true;
	                  for(int e = 0;e < p.length && matched;e++){
	                    String [] e11 = p[e].toString().split(" ");
	                    String [] e22 = cp[e].toString().split(" ");
	                    String e1 = e11[0];
	                    String e2 = e22[e22.length - 1];
	                    if(e1.indexOf(e2.toString()) == -1 && e2.toString().indexOf(e1) == -1){
	                      matched = false;
	                    }
	                    //System.out.println("compare parameters " + e1 + "*****" + cp[e]);
	                  }
	                  if(matched){
	                    matchedMethod = m;
	                    break;
	                  }
	                }
	              }
	            MethodDocNode mdoc = new MethodDocNode(mq3,matchedMethod);
	            mdoc.setFather(this);
	  			methodList.add(mdoc);
				this.getChildren().add(methodList.get(methodList.size() - 1));	              
	          }
        } else {
			for(MethodDoc md : methods){
	            MethodDocNode mdoc = new MethodDocNode(md);
	            mdoc.setFather(this);
	  			methodList.add(mdoc);				
				this.getChildren().add(methodList.get(methodList.size() - 1));
			}        	
        }
        
		this.classDoc = classDoc;
	}
}
