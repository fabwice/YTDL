package gui;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import download.DownloadController;

public class WindowManager {
	GuiTaskList tasklist;
	JFrame window;
	JTextField textField;
	DownloadController parent;
	JButton start;
	public WindowManager(DownloadController parent){
		this.parent = parent;
		window = new JFrame();
		window.setVisible(true);
		window.setSize(450, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setTitle("Youtube MP3 Downloader  -  Kyle Spencer");
		window.setResizable(false);
		
		textField = new JTextField();
		textField.setToolTipText("Enter URL here");
		textField.setFont(new Font("Verdana", Font.PLAIN, 12));
		textField.setBounds(12, 10, 291, 20);
		window.getContentPane().add(textField);
		textField.setColumns(10);
		
		start = new JButton("Start");
		start.setFont(new Font("Verdana", Font.PLAIN, 12));
		start.setBounds(315, 10, 107, 20);
		window.add(start);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 41, 410, 2);
		window.add(separator);
		
		tasklist = new GuiTaskList();
		
		window.add(tasklist);
		window.repaint();
		//window.pack();
		createButtonListener();
		createKeyListener();
	
	}
	void createKeyListener(){
		window.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					System.out.println("Enter");
					if(!textField.getText().equals("")){
						parent.createJob(textField.getText());
					}
					textField.setText("");
				}
				if(e.getKeyCode() == KeyEvent.VK_V){
					try {
						textField.setText((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
						//System.out.println((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));

					} catch (HeadlessException | UnsupportedFlavorException
							| IOException e1) {
						e1.printStackTrace();
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					textField.setText("");
				}
				
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
	}
	void createButtonListener(){
		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().equals("")){
					parent.createJob(textField.getText());
				}
				textField.setText("");
			}});
		textField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(!textField.getText().equals("")){
						parent.createJob(textField.getText());
					}
					textField.setText("");
				}
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					textField.setText("");
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
	}
	
	public void addGuiJob(GuiJob guijob){
		tasklist.jobs.add(guijob);
		window.repaint();
	}
	public void removeGuiJob(GuiJob guijob){
		tasklist.jobs.remove(guijob);
		window.repaint();
	}
	public void repaint2(){
		window.repaint();
	}
}
