package download;

import gui.GuiJob;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class Job implements Runnable{
	DownloadController parent;
	GuiJob jgui;
	Thread jobThread;
	boolean stopped = false;
	boolean done = false;
	int id;
	String url;
	String status;
	File temp_dir;
	File jobs_dir;
	File base_dir;

	File downloadedFile;

	ProcessBuilder makeDlProc;
	ProcessBuilder makeConvProc;
	Process dl_proc;
	Process conv_proc;
	
	boolean dlFail = false;
	boolean convFail = false;

	public Job(DownloadController dc, GuiJob jgui, File base_dir, File jobs_dir, String url, int id) {
		this.parent = dc;
		this.jgui = jgui;
		this.base_dir = base_dir;
		this.url = url;
		this.jobs_dir = jobs_dir;
		this.id = id;
		jgui.id = id;
		jgui.url = url;
		jobThread = new Thread(this);
		jobThread.start();
		jgui.repaint();
		
		
	}
	void stop(boolean shutdown){
		
		if(stopped) return;
		stopped = true;
		if(dl_proc != null){
			dl_proc.destroyForcibly();
		}
		if(conv_proc != null){
			conv_proc.destroyForcibly();
		}
		cleanup();
		if(!dlFail && !convFail){
			setStatus("FINISHED");
		}
		if(!shutdown){
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		
		parent.wm.removeGuiJob(jgui);
		parent.jobs.remove(this);
	}

	void cleanup() {
		System.out.println("Cleaning up job workspace.");
		if (downloadedFile != null) {
			downloadedFile.delete();
		}
		temp_dir.delete();
	}

	void createTempDir() {
		temp_dir = new File(jobs_dir.getAbsolutePath() + "/job_" + id + "/");
		if(!temp_dir.exists()){
			temp_dir.mkdir();
		}
	}

	void findDownloadedFile() {
		if (temp_dir.listFiles().length > 0) {
			downloadedFile = temp_dir.listFiles()[0];
			jgui.name = downloadedFile.getName().replace(".mp4", "");
			jgui.repaint();
		} else {
			done = true;
			dlFail = true;
		}
	}

	public void download() {

		try {
			//Uncomment to redirect process output to console.
			makeDlProc.redirectError(Redirect.INHERIT);
			makeDlProc.redirectOutput(Redirect.INHERIT);
			dl_proc = makeDlProc.start();
			dl_proc.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void convert() {
		try {
			//Uncomment to redirect process output to console.
			//makeConvProc.redirectError(Redirect.INHERIT);
			//makeConvProc.redirectOutput(Redirect.INHERIT);
			conv_proc = makeConvProc.start();
			conv_proc.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
	public void setStatus(String s){
		status = s;
		jgui.status = status;
		jgui.repaint();

	}
	public void checkFail(){
		convFail = !(new File(base_dir+"/out/"+downloadedFile.getName().replace(".mp4", ".mp3")).exists());
	}

	@Override
	public void run() {
		makeDlProc = new ProcessBuilder("java", "-jar", "../../res/runytd2.jar",
				url);
		createTempDir();
		makeDlProc.directory(temp_dir);
		setStatus("DOWNLOADING");
		download();
		findDownloadedFile();
		if(dlFail){
			System.err.println("Failed to download: job=" + id);
			setStatus("DOWNLOAD FAILED");
			stop(false);
			return;
		}else{
			setStatus("DOWNLOADED");
		}
		makeConvProc = new ProcessBuilder(base_dir + "/dev/res/ffmpeg", "-i",
				temp_dir.getAbsolutePath() + "/" + downloadedFile.getName(), "-vn","-y", "-acodec", "libmp3lame",
				base_dir + "/out/"
						+ downloadedFile.getName().replace(".mp4", ".mp3"));
		if(!new File(base_dir+"/out/"+downloadedFile.getName().replace(".mp4", ".mp3")).exists()){
			setStatus("CONVERTING");
			convert();
		}else{
			System.out.println("Converted file already exists! Skipping...");
		}
		checkFail();
		if(convFail){
			System.err.println("Failed to convert: job=" + id);
			setStatus("CONVERSION FAILED");
		}else{
			setStatus("CONVERTED");
		}
		done = true;
		System.out.println(downloadedFile.getName() + " has been completed.");
		stop(false);
	}
}
