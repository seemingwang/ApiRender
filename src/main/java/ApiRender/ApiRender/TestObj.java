package ApiRender.ApiRender;

public class TestObj {
	private String message,name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}
	
	public String getTestString(String t){
		if(t == null){
			t = "";
		}
		return t + "Func---Test";
	}
	public void setMessage(String message) {
		this.message = message;
	}

}