package ApiRender.ApiRender;

import java.util.ArrayList;

public interface FreeNode {
	public boolean isRoot();
	public ArrayList<FreeNode> getChildren();
	public String getCssName(String name);
	public String getField(String name);
	public String getNodeId();
	public String getCss();
	public String getHtml();
}
