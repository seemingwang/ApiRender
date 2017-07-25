package ApiRender.ApiRender;

public class SquareWrapper extends BaseNode {

	static {
		freeMarkerPath.put(SquareWrapper.class,"ftl/squareWrapper/squareWrapper.ftl");
		cssPath.put(SquareWrapper.class,"ftl/squareWrapper/squareWrapper.css");
	}

}