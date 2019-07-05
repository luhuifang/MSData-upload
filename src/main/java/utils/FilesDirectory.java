package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import org.apache.commons.io.FileUtils;

public class FilesDirectory {
	
	private SimpleDateFormat df = null;
	
	public FilesDirectory() {
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public FilesDirectory(SimpleDateFormat df ) {
		this.df = df;
	}
	
	private String getRelativePath(String path , String root) {
    	String relative_path = path.replaceAll("\\\\", "/").replaceAll(root, "");
    	return relative_path;
    }
    
    private boolean mkdir(File new_dir) {
		if(new_dir.exists() == false) {
        	try {
				FileUtils.forceMkdir(new_dir);
			} catch (IOException e) {
				//e.printStackTrace();
				return false;
			}
        }
		return true;
	}
    
    public String copyFile(File file, String sourcepath, String despath) {
    	String loginfo = null;
		File new_dir = new File(despath + getRelativePath(file.getParent(),sourcepath));
    	if(mkdir(new_dir)){
    		try {
    			FileUtils.copyFileToDirectory(file, new_dir);
    			loginfo = df.format(new Date()) + " Successful copy:\n" + file.getAbsolutePath() + " to " + new_dir.toString() + " ! \n";
    		} catch (FileNotFoundException e) {
    			loginfo = df.format(new Date()) + " File is writing:\nThe " + file.getAbsolutePath() + ", wait to complete! \n";
    			//e.printStackTrace();
    		} catch (IOException e) {
    			loginfo = df.format(new Date()) + " Failed copy:\n" + file.getAbsolutePath() + " to " + new_dir.toString() + "! Please check it! \n";
    			//e.printStackTrace();
    		}
    	}else {
    		loginfo = df.format(new Date()) + " Failed copy:\n" + file.getAbsolutePath() + " to " + new_dir.toString() + "!\n"
    				+ "Because can't make directory " + new_dir + "\n";
    	}
    	
    	return loginfo;
	}
    
    public int copyFile(File file, String sourcepath, String despath, JTextPane logText) {
		File new_dir = new File(despath + getRelativePath(file.getParent(),sourcepath));
    	if(mkdir(new_dir)){
    		
    		File new_file = new File(new_dir.getAbsolutePath() + file.getName());
    		if(new_file.exists())
    			new_file.delete();
    		
    		try {
    			FileUtils.copyFileToDirectory(file, new_dir);
    			String loginfo = df.format(new Date()) + " Successful copy:\n" + file.getAbsolutePath() + " to " + new_dir.toString() + " ! \n";
    			try {
					logText.getDocument().insertString(0, loginfo, logText.getStyle("normal"));
				} catch (BadLocationException e) {
					//e.printStackTrace();
				}
    			return 0;
    		} catch (FileNotFoundException e) {
    			String loginfo = df.format(new Date()) + " File is writing:\nThe " + file.getAbsolutePath() + ", wait to complete! \n";
    			try {
					logText.getDocument().insertString(0, loginfo, logText.getStyle("red"));
				} catch (BadLocationException e1) {
					//e1.printStackTrace();
				}
    			//e.printStackTrace();
    			return 256;
    		} catch (IOException e2) {
    			String loginfo = df.format(new Date()) + " Failed copy:\n" + file.getAbsolutePath() + " to " + new_dir.toString() + "! Please check it! \n";
    			try {
					logText.getDocument().insertString(0, loginfo, logText.getStyle("red"));
				} catch (BadLocationException e) {
				}
    			return 256;
    		}
    	}else {
    		String loginfo = df.format(new Date()) + " Failed copy:\n" + file.getAbsolutePath() + " to " + new_dir.toString() + "!\n"
    				+ "Because can't make directory " + new_dir + "\n";
    		try {
				logText.getDocument().insertString(0, loginfo, logText.getStyle("red"));
			} catch (BadLocationException e) {
				//e.printStackTrace();
			}
    		return 256;
    	}
	}
    
    public Map<String, Integer> readSampleList(String sam_lst) {
		//read sample.list
    	Map<String, Integer> sampleMap = new TreeMap<String, Integer>();
    	BufferedReader br = null;
    	try {
    		br = new BufferedReader(new FileReader(sam_lst));
	    	String line = null;
	    	while((line = br.readLine()) != null) {
	    		String[] sline = line.split("\\s+");
	    		sampleMap.put(sline[0], Integer.parseInt(sline[1]));
	    	}
	    	
    	}catch(Exception e) {
    		//e.printStackTrace();
    	}finally {
    		if(br != null) {
    			try {
					br.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
    		}
    	}
    	
    	return sampleMap;
	}
    
    public ArrayList<String> readSamples(String sam_lst) {
		//read sample.list
    	BufferedReader br = null;
    	ArrayList<String> sample = new ArrayList<String>();
    	try {
    		br = new BufferedReader(new FileReader(sam_lst));
	    	String line = null;
	    	while((line = br.readLine()) != null) {
	    		String[] sline = line.split("\\s+");
	    		sample.add(sline[0]);
	    	}
    	}catch(Exception e) {
    	}finally {
    		if(br != null) {
    			try {
					br.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
    		}
    	}
    	return sample;
    	
	}
	
	public void listProjectFile(File f, ArrayList<String> list, ArrayList<File> files){
		
		if(f.isDirectory()) {
			for(File subf : f.listFiles()) {
				listProjectFile(subf, list, files);
			}
		}else {
			for(String l : list) {
				if(f.toString().contains(l) && f.getParent().endsWith("RAWdata")) {
					files.add(f);
				}
			}
		}
		
	}
	
	public ArrayList<File> compareDirectories(ArrayList<File> files, String sourceRootDir, String desRootDir) {
		ArrayList<File> file = new ArrayList<File>();
		for(File f : files) {
			File targetf = new File(f.toString().replaceAll("\\\\", "/").replaceAll(sourceRootDir.replaceAll("\\\\", "/"), desRootDir.replaceAll("\\\\", "/")));
			if(targetf.exists() == false) {
				file.add(f);
			}
		}
		return file;
	}

	public int copy3times(File file, String sourcepath, String despath, JTextPane logText) {
		int times = 0;
		int code = -1;
		do {
			times ++;
			code = copyFile(file, sourcepath, despath, logText);
			if(code == 0)
				break;
		}while(times < 3);
	    	
	    return code;
    }
	
}
