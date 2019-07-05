package computer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

public class MSData_deal extends Thread{
	
	private File file = null;
	//private String localpath = null;
	//private String remotepath = null;
	private String perl = null;
	//private FilesDirectory fd = null;
	private Map<String, String> exists_flag = null;
	private Map<String, Integer> sample_lst = null;
	JTextPane logText = null;
	private SimpleDateFormat df = null;
	private String errLogFile = null;
	
	public MSData_deal(String perl_exe, File file, String localpath, String remotepath, Map<String, Integer> sample_lst, JTextPane logText) {
		this.perl = perl_exe;
		//this.fd = new FilesDirectory();
		this.exists_flag = new TreeMap<String, String>();
		this.file = file;
		//this.localpath = localpath;
		//this.remotepath = remotepath;
		this.sample_lst = sample_lst;
		this.logText = logText;
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public MSData_deal(String localpath, String perl_exe, File file,  Map<String, Integer> sample_lst, JTextPane logText) {
		this.errLogFile = localpath + "/runErr.log";
		this.perl = perl_exe;
		this.exists_flag = new TreeMap<String, String>();
		this.file = file;
		this.sample_lst = sample_lst;
		this.logText = logText;
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	
	public void run() {
		try {
			deal_file();
		}catch(Exception e) {
			writeErrLog(errLogFile, e.getMessage());
			//e.printStackTrace();
		}
	}
	
	public int transToMGF(File file, String out_path, JTextPane logText) {
		String filename = file.getName().split(".", 2)[0];
		File mgf = new File(out_path + "\\MGF\\" + filename + ".mgf");
		if(mgf.exists())
			mgf.delete();
		String[] cmd = {perl, "C:\\MS\\trans.pl", file.getAbsolutePath(), out_path};
		try {
			return runThread(file, logText, cmd, "trans");
		} catch (Exception e) {
			writeErrLog(errLogFile, e.getMessage());
			return 256;
		}
		
	}

	public int transToMGF(File file, JTextPane logText) {
		return transToMGF(file, file.getParent() + "\\..", logText);
	}
	
	public int QC(File file, String out_path, JTextPane logText) {
		String[] cmd = {perl,  "C:\\MS\\QC_reporter_v2.pl", "-ifile", file.getAbsolutePath(), "-odir", out_path };
		try {
			return runThread(file, logText, cmd, "QC");
		} catch (Exception e) {
			writeErrLog(errLogFile, e.getMessage());
			return 256;
		}
	}
	
	public int QC(File file, JTextPane logText) {
		return QC(file, file.getParent(), logText);
	}
	
	public int combine(String in_path, String out_path, String prefix, JTextPane logText) {
		String[] cmd = {"cmd", "/c", "start", "/b",  "C:\\MS\\combine.bat", in_path, out_path, prefix};
		try {
			return runThread(new File(in_path), logText, cmd, "combine");
		} catch (Exception e) {
			writeErrLog(errLogFile, e.getMessage());
			return 256;
		}
	}

	public int combine(String in_path, String out_path, JTextPane logText) {
		return combine(in_path, out_path, "all", logText);
	}
	
	public int lcmsQC(String indir, String odir, JTextPane logText) {
		String[] cmd = {perl,  "C:\\MS\\lcms_qc.pl", "-d", indir, "-o", odir };
		try {
			return runThread(new File(indir), logText, cmd, "lcmsQC");
		}catch (Exception e) {
			writeErrLog(errLogFile, e.getMessage());
			return 256;
		}
	}
	
	public String getProjectName(File file) {
		String project_name = null;
		
		Pattern p = Pattern.compile("(P.*)_.*");
		Matcher matcher = p.matcher(file.getName());
		if(matcher.find()) {
			project_name = matcher.group(1);
		}else {
			project_name = "";
		}
		return project_name;
	}
	
	public boolean isPair(File file) {
		String filename = file.getAbsolutePath();
		File pair = null;
		if(filename.endsWith(".wiff")) 
			pair = new File(filename + ".scan");
		else if (filename.endsWith(".wiff.scan"))
			pair = new File(filename.replaceAll(".scan", ""));
		if(pair != null && pair.exists()) 
			return true;
		
		return false;
	}
	
	private int runThread(File file, JTextPane logText, String[] cmd, String type) {
		Process process = null;
		StreamGobbler outGobbler = null;
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.redirectErrorStream(true);
			process = builder.start(); //run cmd
			outGobbler = new StreamGobbler(process); 
			outGobbler.start(); //new a thread for printing outputStream
			int code = process.waitFor();
			printLog(file, logText, code, type);
			return code;
		} catch (InterruptedException | IOException e) {
			writeErrLog(errLogFile, e.getMessage());
			return 256;
		}finally {
			process.destroy();
		}
	}
	
	public void deal_file() {
    	String file_path = file.getParent();
		if(file_path.contains("project_QC")) {
			deal_projectQC(file, logText);
    	}else {
			deal_RAWdata(file, logText);
    	}
	}

	private void deal_projectQC(File file, JTextPane logText) {
		
		int transCode = deal_WIFF(file, logText, "projectQC"); //trans to mgf
		if(transCode == 0) {
			String basename = file.getName().replaceAll(".wiff.*", "");
			File mgf = new File(file.getParent() + "\\..\\MGF\\" + basename + ".mgf");
			
			//QC
			String qc_path =  file.getParent() + "\\..\\QC";
			
			if(file.getParent().contains("ID_QC")) {  //lcms_qc
				do3times(mgf, qc_path, logText, "LCMS");
				
			}else if(file.getParent().contains("iTRAQ_QC")) {
				do3times(mgf, qc_path, logText, "QC");
				
			}
		}
	}

	private void deal_RAWdata(File file, JTextPane logText) {
		int transCode = deal_WIFF(file, logText, "RAWdata");
		if(transCode == 0) {
			String basename = file.getName().replaceAll(".wiff.*", "");
			File mgf = new File(file.getParent() + "\\..\\MGF\\" + basename + ".mgf");
			
			//single QC
			String qc_path =  file.getParent() + "\\..\\QC_before_combine";
			int qcCode = do3times(mgf, qc_path, logText, "QC");
			
			if(qcCode == 0) {
				
				int mgf_num = mgf.getParentFile().list().length;
				String project_name = getProjectName(file);
				//combine
				if(sample_lst.containsKey(project_name) && sample_lst.get(project_name) == mgf_num) {
					String combine_path = mgf.getParent() + "\\..\\QC";
					int comCode = do3times(mgf, combine_path, logText, "Combine");
					
					if(comCode == 0) {
						//copy result of combine
						File allmgf = new File(combine_path + "\\" + project_name + "_all" + ".mgf");
						//fd.copyFile(allmgf, localpath, remotepath, logText);
						
						//all QC
						do3times(allmgf, combine_path, logText, "QC");
						
					}
				}
			}
		}
	}

	private int deal_WIFF(File file, JTextPane logText, String type) {
		String trans_path = file.getParent() + "\\..";
		
		/*
		if(type.equals("projectQC")) {
			trans_path = file.getParent();
		}else if(type.equals("RAWdata")) {
			trans_path = file.getParent() + "\\..";
		}
		*/
		
		if(file.getName().endsWith(".wiff.scan")) {
			if(isPair(file)) {
				return do3times(file, trans_path, logText, "Trans");
			}else {
				exists_flag.put(file.getAbsolutePath(), "T");
			}
		}else if (file.getName().endsWith(".wiff")) {
			if(exists_flag.containsKey(file.getAbsolutePath() + ".scan")) {
				return do3times(file, trans_path, logText, "Trans");
			}
		}
		return 2;
	}
	
	private int do3times(File file, String out_path, JTextPane logText, String type) {
		int times = 0;
		int code = -1;
		do {
			times ++;
			printTimeLog(file, logText, times, type);
			if(type.equals("Trans")) {
				code = transToMGF(file, out_path, logText);
			}else if(type.equals("QC")) {
				code = QC(file, out_path, logText);
			}else if(type.equals("Combine")) {
				code = combine(file.getParent(), out_path, getProjectName(file)+"_all", logText);
			}else if(type.equals("LCMS")) {
				code = lcmsQC(file.getParent(), out_path, logText);
			}
			
			if(code == 0)
				break;
		}while(times < 3);
		return code;
	}
	
	private void printTimeLog(File file, JTextPane logText, int times, String type ) {
		//print log
		String loginfo = df.format(new Date()) + " " + type +" " + file.getAbsolutePath() + " " + times + " times!\n";
		try {
			logText.getDocument().insertString(0, loginfo, logText.getStyle("normal"));
		} catch (BadLocationException e) {
			writeErrLog(errLogFile, e.getMessage());
		}
	}
	
	private void printLog(File file, JTextPane logText, int code, String type) {
		String loginfo = null;
		if(code == 0) {
			loginfo = df.format(new Date()) + " Successful " + type + ":\n" + file.getAbsolutePath() + "!\n";
			try {
				logText.getDocument().insertString(0, loginfo, logText.getStyle("normal"));
			} catch (BadLocationException e) {
				writeErrLog(errLogFile, e.getMessage());
			}
		}else {
			loginfo = df.format(new Date()) + " Failed " + type + ":\n" + file.getAbsolutePath() + "! Please check it!\n";
			try {
				logText.getDocument().insertString(0, loginfo, logText.getStyle("red"));
			} catch (BadLocationException e) {
				writeErrLog(errLogFile, e.getMessage());
			}
		}
		
	}
	
	private void writeErrLog(String logFile, String msg) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(logFile, true);
			fw.write(msg + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
