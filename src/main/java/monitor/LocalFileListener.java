package monitor;

import java.io.File;
import java.util.Map;

import javax.swing.JTextPane;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import computer.MSData_deal;
import utils.FilesDirectory;

public class LocalFileListener implements FileAlterationListener {
	
    FileMonitor monitor = null;
    private Map<String, Integer> sample_lst = null;
    private String localpath = null;
    private String samplePath = null;
    private JTextPane logText = null;
    private String perl_exe = null;
    private FilesDirectory fd = null;
    
    
    /*
    public LocalFileListener(String lpath, String rpath, String perl_exe, Map<String, Integer> sample_lst) {
    	this.localpath = lpath.replaceAll("\\\\", "/");
    	this.remotepath = rpath.replaceAll("\\\\", "/");
    	this.sample_lst = sample_lst;
    	this.logText = new JTextPane();
    	this.perl_exe = perl_exe;
    }
    
    public LocalFileListener(String lpath, String rpath, String perl_exe, Map<String, Integer> sample_lst, JTextPane logText) {
    	this.localpath = lpath.replaceAll("\\\\", "/");
    	this.remotepath = rpath.replaceAll("\\\\", "/");
    	this.sample_lst = sample_lst;
    	this.logText = logText;
    	this.perl_exe = perl_exe;
    }
    */
    
    
    public LocalFileListener(String lpath, String perl_exe, String samplePath, JTextPane logText, FilesDirectory fd) {
    	this.localpath = lpath.replaceAll("\\\\", "/");
    	this.logText = logText;
    	this.perl_exe = perl_exe;
    	this.samplePath = samplePath;
    	this.fd = fd;
    }
   
    public void onStart(FileAlterationObserver observer) {
    	sample_lst = fd.readSampleList(samplePath);
    }
 
    public void onDirectoryCreate(File directory) {}

    public void onDirectoryChange(File directory) {}
 
    public void onDirectoryDelete(File directory) {}

    public void onFileCreate(File file) {
    	deal_file(file);
    }

    public void onFileChange(File file) {
    	deal_file(file);
    }
 
    public void onFileDelete(File file) {}
 

    public void onStop(FileAlterationObserver observer) {
    	//System.out.println("Local Stop!");
    }
    
    private void deal_file(File file) {
    	MSData_deal ms = new MSData_deal(localpath, perl_exe, file, sample_lst, logText);
    	ms.start();
    }
 
    
}

