package ApiRender.ApiRender;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import freemarker.template.Configuration;  
import freemarker.template.Template;  
import freemarker.template.TemplateException;


import com.sun.javadoc.*;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;
import com.sun.jdi.Field;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javadoc.JavadocTool;
import com.sun.tools.javadoc.Messager;
import com.sun.tools.javadoc.ModifierFilter;
import org.codehaus.jackson.map.ObjectMapper;

import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
/**
 * Hello world!
 *
 */
class testFile {
	public HashSet<String> folders;
	public String handlePath(String path){
		if(path == null || path.length() == 0){
			return path;
		}
		String [] p = path.split(":");
		folders = new HashSet<String>();
		for(String s: p){
			handleSubPath(s);
		}
		String res = "";
		for(String s: folders){
			if(res.length() > 0){
				res += ":";
			}
			res += s;
		}
		System.out.println("returning " + res);
		return res;
	}
	public void handleSubPath(String path){
		if(path == null || path.length() == 0) {
			return;
		}
		traverseFolder2(path);
	}
//	public void traverseFolder2(String path) {
//        File file = new File(path);
//        if (file.exists()) {
//            File[] files = file.listFiles();
//            if (files.length == 0) {
//                return;
//            } else {
//                for (File file2 : files) {
//                    if (file2.isDirectory()) {
//                        traverseFolder2(file2.getAbsolutePath());
//                        String str = file2.toString();
//                        if(str.indexOf("com") == str.length() - 3){
//                        	folders.add(file2.getParentFile().toString());
//                        	System.out.println("add " + file2.getParentFile().toString());
//                        }
//                    } 
//                }
//            }
//        }
//    }
	public void traverseFolder2(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
        	folders.add(file.toString());
        	System.out.println("add " + file.toString());
            File[] files = file.listFiles();
            if (files.length == 0) {
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder2(file2.getAbsolutePath());
                    } 
                }
            }
        }
    }
}
public class App 
{
    public static RootDoc fromPath(String path, String subpackage) throws IOException {
        final Context context = new Context();
        Options.instance(context).put("-sourcepath", path);
        Messager.preRegister(context, "Messager!");
        final ListBuffer<String> subPackages = new ListBuffer<String>();
        subPackages.append(subpackage);

        final JavadocTool javaDoc = JavadocTool.make0(context);
        javaDoc.keepComments = true;
        javaDoc.encoding = "UTF-8";
        return javaDoc.getRootDocImpl(
                "",
                null,
                new ModifierFilter(ModifierFilter.ALL_ACCESS),
                new ListBuffer<String>().toList(),
                new ListBuffer<String[]>().toList(),
                false,
                subPackages.toList(),
                new ListBuffer<String>().toList(),
                false, false, false
        );
    }
    public static String outputPath;
    public static void test1(String filePath, String packageName){
		try {
			//RootDoc root = fromPath("/Users/zsasuke/www/thor/thor-server/src/main/java;/Users/zsasuke/www/thor/thor-api/src/main/java/com/haoyayi/thor/api/newChargePay/dto","com.haoyayi.thor.server.controller");
			//RootDoc root = fromPath("/Users/zsasuke/www/thor/thor-server/src/main/java:/Users/zsasuke/www/thor/thor-api/src/main/java","com.haoyayi.thor.api.newChargePay.dto");
			//RootDoc root = fromPath(new testFile().handlePath("/Users/zsasuke/www/thor/thor-api/src/main/java/*:/Users/zsasuke/www/thor/thor-server/src/main/java/*"),"com.haoyayi.thor.server.controller");
			//RootDoc root = fromPath(new testFile().handlePath(filePath),packageName);	
			RootDoc root = fromPath(filePath,packageName);
			test(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static HashMap<String,ClassDoc> docMap = new HashMap<String, ClassDoc>();
    public static boolean start(RootDoc doc) {
    	test(doc);
    	return true;
    }
    
	
	public static void test(RootDoc root) {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
//		for(ClassDoc c: root.classes()){
//			searchDoc(c,0);
//		}
		ClassDisplayNode.rootDoc = root;
		try {
			
			System.out.println(root != null);
			if(root != null){
				ClassDoc[] classes = root.classes();
				System.out.println("class length " + classes.length);
				if(classes.length > 0){
					for(ClassDoc c: classes){		
						System.out.println("class name " + c.name());
						//searchDoc(c,0);
						//if(c.toString().equals("com.haoyayi.thor.server.controller.BookController") ){
							try {
								ClassDocNode c1 = new ClassDocNode(c);
								System.out.println(c.toString() + "hehe");
								//System.out.println("update &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + "/Users/zsasuke/myPro/myJava/ListClass/src/web/" + c.name()+ ".html");
								Writer out = new OutputStreamWriter(new FileOutputStream(outputPath + c.name()+ ".html"),"UTF-8");
								try {
									String res = c1.getHtml();
									//System.out.println(res);
									out.write(res);
									out.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
						}
					//}
				}
			}
	        

			//Thread.sleep(100000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static void main( String[] args ){
    	System.out.println(args.length);
    	for(int i = 0;i < args.length;i++){
    		System.out.println(args[i]);
    	}
    	if(args.length < 3) {
    		System.out.println("parameters are not enough");
    		return;
    	}
    	outputPath = args[2];
		if(outputPath.length() == 0){
			outputPath = "./";
		} else if(outputPath.charAt(outputPath.length() - 1) != '/') {
			outputPath = outputPath + "/";
		}  
    	test1(args[0],args[1]);
    }
}
