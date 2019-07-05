package utils;

import javax.swing.JTextArea;

public class LogError {
	private JTextArea logText;
	
	public LogError(JTextArea logText) {
		this.logText = logText;
	}
	
	public void printLog(String log) {
		logText.insert(log, 0);
	}
}
