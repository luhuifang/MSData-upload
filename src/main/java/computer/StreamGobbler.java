package computer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
	
	private Process proc = null;
  
    public StreamGobbler(Process proc) {  
    	this.proc = proc;
    	
    }  
  
    public void run() {  
        try {  
            InputStreamReader isr = new InputStreamReader(proc.getInputStream());  
            BufferedReader br = new BufferedReader(isr);  
            String line = null;  
            while ((line = br.readLine()) != null)  
                System.out.println("OUTPUT >" + line);
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        }  
    }  
    
    
    

}
