package ApiRender.ApiRender;

public class Square extends BaseNode {

	static {
		freeMarkerPath.put(Square.class,"ftl/square/square.ftl");
		cssPath.put(Square.class,"ftl/square/square.css");
	}
	public Square() {
	}

}
