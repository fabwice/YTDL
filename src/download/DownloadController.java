package download;


import gui.GuiJob;
import gui.WindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DownloadController {
	/*
	 * Redesign plans: 1 object for conversion job each job object handles
	 * status: download and conversion gui with one row per job
	 */

	static final String dir_path = System.getProperty("user.home") + "/ytdl_dir/";
	static final File dir = new File(dir_path);
	static final File dev = new File(dir + "/dev");
	static final File output = new File(dir + "/out");
	static final File res = new File(dev + "/res");
	static final File job_dir = new File(dev + "/jobs");

	Scanner scan = new Scanner(System.in);
	List<Job> jobs = new ArrayList<Job>();
	WindowManager wm;
	public DownloadController() {
		wm = new WindowManager(this);
		setup();
		//createJob("https://www.youtube.com/watch?v=uv9tXFrTLtI");
		//createJob("https://www.youtube.com/watch?v=clRjbYa4UWQ");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){

			@Override
			public void run() {
				//clean up all files on shutdown...
				System.out.println("Stopping all actions and cleaning workspace...");
				for(Job j: jobs){
					j.stop(true);
				}
				
			}}));
	}
	public void deleteDir(File f){
		if(f.isDirectory()){
			for(File t: f.listFiles()){
				deleteDir(t);
			}
			f.delete();
		}else{
			f.delete();
		}
	}
	
	public void createJob(String url){
		GuiJob gj = new GuiJob(wm, -1, "loading", "loading", "loading");
		jobs.add(new Job(this, gj, dir, job_dir, url, jobs.size()));
		wm.addGuiJob(gj);
	}
	
	void setup() {
		// setup required program
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		if (!output.exists()) {
			output.mkdir();
		}
		if (!res.exists()) {
			res.mkdir();
		}
		if (!job_dir.exists()) {
			job_dir.mkdir();
		}
		for (File f : job_dir.listFiles()) {
			
			deleteDir(f);
		}

	}

	public static void main(String[] args) {
		new DownloadController();
		
	}

}
