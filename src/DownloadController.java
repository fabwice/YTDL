import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Scanner;

public class DownloadController {
	/*
	 * Redesign plans: 1 object for conversion job each job object handles
	 * status: download and conversion gui with one row per job
	 */

	static final String dir_path = System.getProperty("user.home")
			+ "/ytdl_dir/";
	static final File dir = new File(System.getProperty("user.home")
			+ "/ytdl_dir");
	static final File tempdir = new File(System.getProperty("user.home")
			+ "/ytdl_dir/temp");
	static final File output = new File(System.getProperty("user.home")
			+ "/ytdl_dir/out");
	static final File res = new File(System.getProperty("user.home")
			+ "/ytdl_dir/res");
	static final File jobs = new File(System.getProperty("user.home")
			+ "/ytdl_dir/jobs");

	Runtime rt = Runtime.getRuntime();
	Scanner scan = new Scanner(System.in);

	public DownloadController() {
		setup();
		//set up gui in the future...
		System.out.println("please enter url...");
		String url = scan.nextLine().trim();
		new Job(dir, jobs, url, 1);
	}

	void setup() {
		// setup required program
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!tempdir.exists()) {
			tempdir.mkdir();
		}
		if (!output.exists()) {
			output.mkdir();
		}
		if (!res.exists()) {
			res.mkdir();
		}
		if (!jobs.exists()) {
			jobs.mkdir();
		}
		for (File f : jobs.listFiles()) {
			f.delete();
		}

	}

	public static void main(String[] args) {
		new DownloadController();

	}

}
