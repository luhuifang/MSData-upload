package monitor;
 
import java.io.File;
 
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
 
public class FileMonitor {
 
    FileAlterationMonitor monitor = null;
 
    public FileMonitor(long interval) throws Exception {
        monitor = new FileAlterationMonitor(interval);
    }
 
    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }
 
    public void stop() throws Exception {
        monitor.stop();
    }
 
    public void start() throws Exception {
        monitor.start();
    }

}

