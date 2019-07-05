package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import javax.swing.JTextField;

import monitor.FileMonitor;
import monitor.LocalFileListener;
import monitor.RemoteFileListener;
import utils.FilesDirectory;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class msTools {

	private JFrame frame;
	private JTextField remoteTxt;
	private JTextField localTxt;
	private JTextField perlTxt;
	private JLabel lblProjectSamplesList;
	private JTextField sampleList;
	private JButton btnBrowse_local;
	private JButton btnBrowse_remote;
	private JButton btnBrowse_perl;
	private JButton btnBrowse_sample;
	private JButton btnRun;
	private JButton btnStop;
	private JScrollPane scrollPane;
	private JTextPane logText;
	
	private FileMonitor m ;
	private SimpleDateFormat df;
	private FilesDirectory fd;
	
	private String remote_path ;
	private String local_path ;
	private String perl_exe ;
	private String sam_lst ;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					msTools window = new msTools();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public msTools() throws Exception {
		this.m = new FileMonitor(300000);
		this.df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.fd = new FilesDirectory(df);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("MSData Tools");
		frame.getContentPane().setBackground(SystemColor.inactiveCaptionBorder);
		frame.setBounds(450, 250, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label = new JLabel("Retome Path");
		label.setFont(new Font("Segoe UI Light", Font.BOLD, 16));
		
		remoteTxt = new JTextField();
		remoteTxt.setForeground(Color.LIGHT_GRAY);
		remoteTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		remoteTxt.setText("E:\\01.program\\02.ETL\\04.msdata-upload\\04.test\\test_remote");
		remoteTxt.setColumns(20);
		
		JLabel lblLocalPath = new JLabel("Local Path");
		lblLocalPath.setFont(new Font("Segoe UI Light", Font.BOLD, 16));
		
		localTxt = new JTextField();
		localTxt.setForeground(Color.LIGHT_GRAY);
		localTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		localTxt.setText("E:\\01.program\\02.ETL\\04.msdata-upload\\04.test\\test_local");
		localTxt.setColumns(10);
		
		JLabel lblPerlPath = new JLabel("Perl Path");
		lblPerlPath.setFont(new Font("Segoe UI Light", Font.BOLD, 16));
		
		perlTxt = new JTextField();
		perlTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		perlTxt.setForeground(Color.LIGHT_GRAY);
		perlTxt.setText("D:\\01.software\\perl\\install\\bin\\perl.exe");
		perlTxt.setColumns(10);
		
		lblProjectSamplesList = new JLabel("Samples List");
		lblProjectSamplesList.setFont(new Font("Segoe UI Light", Font.BOLD, 16));
		
		sampleList = new JTextField();
		sampleList.setForeground(Color.LIGHT_GRAY);
		sampleList.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sampleList.setText("E:\\01.program\\02.ETL\\04.msdata-upload\\03.mybin\\sample_list.txt");
		sampleList.setColumns(10);
		
		btnBrowse_local = new JButton("Browse...");
		btnBrowse_local.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnBrowse_remote = new JButton("Browse...");
		btnBrowse_remote.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnBrowse_perl = new JButton("Browse...");
		btnBrowse_perl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnBrowse_sample = new JButton("Browse...");
		btnBrowse_sample.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnRun = new JButton("Run");
		btnRun.setFont(new Font("Segoe UI Light", Font.BOLD, 14));
		
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.setFont(new Font("Segoe UI Light", Font.BOLD, 14));
		
		logText = new JTextPane();
		logText.setText("");
		logText.setEditable(false);
		
		scrollPane = new JScrollPane(logText);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(74)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblPerlPath)
								.addComponent(lblLocalPath)
								.addComponent(label)
								.addComponent(lblProjectSamplesList))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnRun)
									.addGap(49)
									.addComponent(btnStop))
								.addComponent(sampleList, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
								.addComponent(perlTxt, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
								.addComponent(localTxt, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
								.addComponent(remoteTxt, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnBrowse_local)
								.addComponent(btnBrowse_remote)
								.addComponent(btnBrowse_perl)
								.addComponent(btnBrowse_sample))))
					.addGap(90))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(36)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(remoteTxt, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(label)
						.addComponent(btnBrowse_remote))
					.addGap(28)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLocalPath)
						.addComponent(localTxt, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse_local))
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPerlPath)
						.addComponent(perlTxt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse_perl))
					.addGap(29)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectSamplesList)
						.addComponent(sampleList, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse_sample))
					.addGap(32)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnRun)
						.addComponent(btnStop))
					.addGap(11)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
					.addGap(37))
		);
		
		
		logText.setFont(new Font("Segoe UI Historic", Font.BOLD, 14));
		scrollPane.setViewportView(logText);
		frame.getContentPane().setLayout(groupLayout);
		
		Style def = logText.getStyledDocument().addStyle(null, null);
        Style normal = logText.addStyle("normal", def);
        Style s = logText.addStyle("red", normal);
        StyleConstants.setForeground(s, Color.RED);
        logText.setParagraphAttributes(normal, true);
		
		myEvent();
		frame.setVisible(true);
	}
	
	private void changeEnbled(boolean flag) {
		btnRun.setEnabled(flag);
		btnBrowse_local.setEnabled(flag);
		btnBrowse_remote.setEnabled(flag);
		btnBrowse_perl.setEnabled(flag);
		btnBrowse_sample.setEnabled(flag);
		localTxt.setEditable(flag);
		remoteTxt.setEditable(flag);
		perlTxt.setEditable(flag);
		sampleList.setEditable(flag);
		btnStop.setEnabled(!flag);
	}
	
	private void myEvent() {
		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startRun();
			}
		});
		
		btnRun.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				changeEnbled(false);
				enterRun(e);
			}
		});
		
		btnStop.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopRun();	
			}
			
		});
		
		btnStop.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				stopRun();	
			}

		});
		
		btnBrowse_local.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chooseFile(e, localTxt, "Choose local path");
			}
			
		});
		
		
		btnBrowse_remote.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chooseFile(e, remoteTxt, "Choose remote path");
			}
			
		});
	
		btnBrowse_perl.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chooseFile(e, perlTxt, "Choose perl.exe");
			}
			
		});
		
		
		btnBrowse_sample.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chooseFile(e, sampleList, "Choose sample list file");
			}
			
		});
		
		
		remoteTxt.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				remoteTxt.setForeground(Color.BLACK);
			}
		});
		
		localTxt.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				localTxt.setForeground(Color.BLACK);
			}
		});
		
		perlTxt.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				perlTxt.setForeground(Color.BLACK);
			}
		});
		
		sampleList.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				sampleList.setForeground(Color.BLACK);
			}
		});
		
	}
	
	private void enterRun(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			startRun();
		}
	}
	
	private void startRun() {
		getInput();
		if(passCheck()) {
			changeEnbled(false);
			logText.setText(df.format(new Date()) + "  Start running ^_^");
			try {
				run();
				//copyExistsFiles();
				copyExistsFilesThread r = new copyExistsFilesThread();
				r.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void stopRun() {
		
		logText.setText("");
		try {
			m.stop();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		changeEnbled(true);
	}
	
	private void run() throws Exception{
    	//start
        m.monitor(remote_path, new RemoteFileListener(local_path, remote_path, logText, fd));
        //m.monitor(local_path, new LocalFileListener(local_path, remote_path, perl_exe, samples, logText));
        m.monitor(local_path, new LocalFileListener(local_path, perl_exe, sam_lst ,logText, fd));
        m.start();
	}
	
	private void chooseFile(ActionEvent e, JTextField textField, String title){
       
		JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showDialog(new JLabel(), title);
        File file = chooser.getSelectedFile();
        if(file != null) {
        	textField.setForeground(Color.BLACK);
        	textField.setText(file.getAbsoluteFile().toString());
        }
    }
	
	private boolean checkIsNot(File file, JTextPane logTxt, String type) {
		int notSomething = -1;
		if (type == "exists") 
			notSomething = file.exists() ? 1 : 0;
		else if (type == "file")
			notSomething = file.isFile() ? 1 : 0;
		else if (type == "directory")
			notSomething = file.isDirectory() ? 1 : 0;
		 
		if(notSomething == 0) {
			try {
				logTxt.getDocument().insertString(0, df.format(new Date()) + " " + file.getAbsolutePath() + " is not " + type + "! Please check it!\n", logTxt.getStyle("red"));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	private boolean passCheck() {
		//check input
    	if(checkIsNot(new File(remote_path), logText, "exists")) 
    		return false;
    	if(checkIsNot(new File(remote_path), logText, "directory")) 
    		return false;
    	
    	if(checkIsNot(new File(local_path), logText, "exists")) 
    		return false;
    	if(checkIsNot(new File(local_path), logText, "directory")) 
    		return false;
    	
    	if(checkIsNot(new File(perl_exe), logText, "exists")) 
    		return false;
    	if(checkIsNot(new File(perl_exe), logText, "file")) 
    		return false;
    	
    	if(checkIsNot(new File(sam_lst), logText, "exists"))
    		return false;
		if(checkIsNot(new File(sam_lst), logText, "file")) 
    		return false;
    		
    	return true;
	}
	
	private void getInput() {
		remote_path = remoteTxt.getText();
    	local_path = localTxt.getText();
    	perl_exe = perlTxt.getText();
    	sam_lst = sampleList.getText();
	}
	
	private void copyExistsFiles() {
		ArrayList<String> sampleName = fd.readSamples(sam_lst);
		ArrayList<File> existsFiles = new ArrayList<File>();
		fd.listProjectFile(new File(remote_path), sampleName, existsFiles);
		ArrayList<File> copyf = fd.compareDirectories(existsFiles, remote_path, local_path);
		for(File f : copyf) {
			fd.copyFile(f, remote_path.replaceAll("\\\\", "/"), local_path.replaceAll("\\\\", "/"), logText);
		}
		
	}
	
	class copyExistsFilesThread extends Thread{
		@Override
		public void run() {
			copyExistsFiles();
		}
	}
}
