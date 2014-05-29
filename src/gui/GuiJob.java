package gui;

public class GuiJob {
	public int id;
	public String status; 
	public String url;
	public String name;
	WindowManager parent;
	public GuiJob(WindowManager parent, int id, String status, String url, String name){
		this.parent = parent;
		this.id = id;
		this.status = status;
		this.url = url;
		this.name = name;
	}
	public void repaint(){
		parent.repaint2();
	}
	
}
