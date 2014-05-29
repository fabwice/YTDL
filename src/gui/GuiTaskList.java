package gui;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GuiTaskList extends JPanel{
	List<GuiJob> jobs = new ArrayList<GuiJob>();
	public GuiTaskList(){
		super.setBounds(12, 54, 412, 497);
		
	}
	@Override
	public void paint(Graphics g){
		g.setFont(new Font("Verdana",1,  14));
		for(GuiJob j: jobs){
			int y = jobs.indexOf(j)*75 + 10;
			g.drawString(String.valueOf(j.id),3 , y);
			g.drawString(j.status, 400 - j.status.length()*10, y);
			g.setFont(new Font("Verdana",1,  12));
			g.drawString(j.name, 3,  y + 35);
			g.drawString(j.url, 3, y+55);
			g.setFont(new Font("Verdana",1,  14));
			g.drawLine(0, y+60, 450, y+60);
		}
		g.drawLine(0, 0, 0, 600);

	}

}
