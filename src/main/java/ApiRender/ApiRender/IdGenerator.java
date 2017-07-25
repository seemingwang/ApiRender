package ApiRender.ApiRender;
import java.util.HashSet;
import java.util.Random;

public class IdGenerator {
	public static HashSet<String> ids = new HashSet<String>();
	public static int length = 8;
	public static int base[] = {65,97};
	public static Random r = new Random();
	public static String getNewId(){
		String res;
		do {
			res = "";
			for(int i = 0;i < length;i++){
				res = res + (char)(base[r.nextInt(2)] + r.nextInt(26));
			}
		}while(ids.contains(res));
		ids.add(res);
		return res;
	}
}
