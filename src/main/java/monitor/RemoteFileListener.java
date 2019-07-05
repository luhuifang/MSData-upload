package monitor;

import java.io.File;

import javax.swing.JTextPane;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import utils.FilesDirectory;

public class RemoteFileListener implements FileAlterationListener {
	
    FileMonitor monitor = null;
    private String localpath = null;
    private String remotepath = null;
    private FilesDirectory fd = null;
    private JTextPane logText = null;
    
    public RemoteFileListener(String lpath, String rpath, JTextPane Text, FilesDirectory fd) {
    	this.localpath = lpath.replaceAll("\\\\", "/");
    	this.remotepath = rpath.replaceAll("\\\\", "/");
    	this.fd = fd;
    	this.logText = Text;
    }
    
    public RemoteFileListener(String lpath, String rpath, FilesDirectory fd) {
    	this.localpath = lpath.replaceAll("\\\\", "/");
    	this.remotepath = rpath.replaceAll("\\\\", "/");
    	this.fd = new FilesDirectory();
    	this.logText = new JTextPane();
    	this.fd = fd;
    }
   
    public void onStart(FileAlterationObserver observer) { 
    	//System.out.println("Remote Start!");
    }
 
    public void onDirectoryCreate(File directory) {
    	//System.out.println("Remote onDirectoryCreate!");
    }

    public void onDirectoryChange(File directory) {
    	//System.out.println("Remote onDirectoryChange!");
    }
 
    public void onDirectoryDelete(File directory) { 
    	//System.out.println("Remote onDirectoryDelete!");
    }

    public void onFileCreate(File file) {
    	String file_path = file.getParent();
    	if(file_path.endsWith("RAWdata") ) {
    		fd.copyFile(file, remotepath, localpath, logText);
    	}
    }

    public void onFileChange(File file) {
    	String file_path = file.getParent();
    	if(file_path.endsWith("RAWdata") ) {
    		fd.copyFile(file, remotepath, localpath, logText);
    	}
    }
 
    public void onFileDelete(File file) { }
 
    public void onStop(FileAlterationObserver observer) {
    	//System.out.println("Remote Stop!");
    }
 
    class CopyThread extends Thread{
    	File file;
		public void setFile(File file) {
			this.file = file;
		}
		@Override
		public void run() {
			fd.copy3times(file, remotepath, localpath, logText);
		}
    	
    }
    
    /*
    private void copyFile(File file) {
    	String file_path = file.getParent();
    	if(file_path.endsWith("RAWdata") ) {
    		CopyThread t = new CopyThread();
    		t.setFile(file);
    		t.start();
    	}
    }
 
    */
}

