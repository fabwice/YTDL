import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class Job {

	public boolean done = false;
	public boolean failed = false;
	public int id;
	public String url;
	public String status;
	public File temp_dir;
	public File jobs_dir;
	public File base_dir;

	public File downloadedFile;

	public ProcessBuilder makeDlProc;
	public ProcessBuilder makeConvProc;
	public Process dl_proc;
	public Process conv_proc;

	public Job(File base_dir, File jobs_dir, String url, int id) {
		this.base_dir = base_dir;
		this.url = url;
		this.jobs_dir = jobs_dir;
		this.id = id;
		makeDlProc = new ProcessBuilder("java", "-jar", "../../res/runytd2.jar",
				url);
		createTempDir();
		makeDlProc.directory(temp_dir);
		download();
		findDownloadedFile();
		makeConvProc = new ProcessBuilder(base_dir + "/res/ffmpeg", "-i",
				temp_dir.getAbsolutePath() + "/" + downloadedFile.getName(), "-vn", "-acodec", "libmp3lame",
				base_dir + "/out/"
						+ downloadedFile.getName().replace(".mp4", ".mp3"));
		if (!failed) {
			convert();
		}
		done = true;
		cleanup();
	}

	void cleanup() {
		System.out.println("Cleaning workspace...");
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
			status = "DOWNLOADED";
		} else {
			done = true;
			failed = true;
		}
	}

	public void download() {

		try {
			
			dl_proc = makeDlProc.start();
			dl_proc.waitFor();
			System.out.println("Download process exited with value: "
					+ dl_proc.exitValue());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void convert() {
		try {
			makeConvProc.redirectError(Redirect.INHERIT);
			makeConvProc.redirectOutput(Redirect.INHERIT);
			conv_proc = makeConvProc.start();
			conv_proc.waitFor();
			System.out.println("Conversion process exited with value: "
					+ conv_proc.exitValue());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}
}
