package baseClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import com.relevantcodes.extentreports.LogStatus;
import com.sun.mail.util.MailSSLSocketFactory;

public class ReadingAndWritingTextFile {
	// Program for reading and writing text file

	FileReader filename;
	BufferedReader inputFile;
	String userCounter;
	String ucount;
	FileWriter writefile;
	BufferedWriter outputFile;
	String emailID;
	public String exitWhile;
	public Boolean emailFlag;
	public int tCounter;
	public LocalTime startTime;
	public LocalTime endTime;
	public Duration elapsed;
	public double totalSeconds;
	public int elapsedInSecs;
	protected static GlobalVariables g = new GlobalVariables();

	public void writeToTextLog(String filePath, String updateCount, String mode) {
		try {
			try {
				if (mode.trim().equalsIgnoreCase("append")) { 
					writefile = new FileWriter(filePath, true);
					outputFile = new BufferedWriter(writefile);
					outputFile.write(updateCount);
					outputFile.newLine();

				} else if (mode.trim().equalsIgnoreCase("appendNoNewLine")) {
					writefile = new FileWriter(filePath, true);
					outputFile = new BufferedWriter(writefile);
					outputFile.write(updateCount);

				} else if (mode.trim().equalsIgnoreCase("newNoNewLine")) {
					writefile = new FileWriter(filePath);
					outputFile = new BufferedWriter(writefile);
					outputFile.write(updateCount);
				} else {
					writefile = new FileWriter(filePath);
					outputFile = new BufferedWriter(writefile);
					outputFile.write(updateCount);
					outputFile.newLine();
				}
				// outputFile = new BufferedWriter(writefile);
				// outputFile.write(updateCount);
				// outputFile.newLine();
			} catch (FileNotFoundException e) {
				Report.LogInfo("Exception", "Exception in writeToTextLog " + e.getMessage(), "FAIL");
			} finally {
				outputFile.close();
				writefile.close();
			}
		} catch (IOException ex) {
			System.out.println("Exception while writing a file");
		}
	}

	public String readEntireFile(String filename) throws IOException {
		@SuppressWarnings("resource")
		FileReader in = new FileReader(filename);
		StringBuilder contents = new StringBuilder();
		char[] buffer = new char[4096];
		int read = 0;
		do {
			contents.append(buffer, 0, read);
			read = in.read(buffer);
		} while (read >= 0);
		return contents.toString();
	}

	public void readMail(String username, String password, String Subject, String timeCounter) throws Exception {
		// Thread.sleep(70000);
		

	
		int i = 0;
		try {
			Properties props = System.getProperties();

			// sf.setTrustAllHosts(true);
			props.setProperty("mail.imaps.host", "imap.gmail.com");
			props.setProperty("mail.imaps.user", username);
			props.setProperty("mail.imaps.password", password);
			props.setProperty("mail.imaps.port", "993");
			props.setProperty("mail.imaps.auth", "true");
			props.setProperty("mail.debug", "true");

			props.setProperty("mail.store.protocol", "imap");
			props.put("mail.imap.ssl.enable", "true"); // required for Gmail
			props.put("mail.imap.sasl.enable", "true");
			props.put("mail.imap.sasl.mechanisms", "XOAUTH2");
			props.put("mail.imap.auth.login.disable", "true");
			props.put("mail.imap.auth.plain.disable", "true");
			MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
			socketFactory.setTrustAllHosts(true);
			props.put("mail.imaps.ssl.socketFactory", socketFactory);

			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", username, password);
			//System.out.println(store);

			exitWhile = "False";
			emailFlag = false;
			tCounter = Integer.valueOf(timeCounter);

			LocalDateTime now = LocalDateTime.now();
			startTime = now.toLocalTime();

			while (exitWhile.equals("False")) {

				Folder inbox = store.getFolder("Inbox");
				inbox.open(Folder.READ_WRITE);

				FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
				Message messages[] = inbox.search(ft);
				Thread.sleep(5000);
				//for (Message message : messages) 
				for(int b = 0; b < messages.length; b++)
				{
					messages[b].setFlag(Flags.Flag.ANSWERED, true);
					String subject = messages[b].getSubject();
					messages[b].setFlag(Flags.Flag.SEEN, true);
					Address[] from = messages[b].getFrom();
					messages = inbox.search(ft);

					if (subject.contains(Subject)) {
						Report.LogInfo("Email Verification", "Receipt email received on email ID", "PASS");
						ExtentTestManager.getTest().log(LogStatus.PASS, "Receipt email received on email ID");
						
						Message[] messages1 = inbox.getMessages();
						for (int m = 0; m < messages1.length; m++) {
							// System.out.println( messages1[m]);
							messages1[m].setFlag(Flags.Flag.DELETED, true);

						}
						
						Thread.sleep(2000);
						System.out.println(username);
						exitWhile = "True";
						break;

					} 					

				}
				
				//DateTime now1 = DateTime.Now;
                LocalDateTime now1 = LocalDateTime.now();     
                //endTime = now1.TimeOfDay;
                endTime = now1.toLocalTime();
                elapsed = Duration.between(startTime, endTime);
                totalSeconds = elapsed.getSeconds();
                elapsedInSecs = (int)totalSeconds;
                System.out.println(elapsedInSecs +" Value is of duration" + tCounter);
                
                if(elapsedInSecs >= tCounter)
                {
                    exitWhile = "True";
                    Report.LogInfo("Email Verification", "Receipt email Not received on email ID in expected time", "FAIL");
					ExtentTestManager.getTest().log(LogStatus.FAIL, "Receipt email Not received on email ID in expected time");
					break;
                }
			}

		} catch (Exception e) {
			Report.LogInfo("Exception", "Receipt email Not received on email ID in expected time", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, "Receipt email Not received on email ID in expected time" + e.getMessage());
		}
		Thread.sleep(2000);
	}
	
			
	// ============================================================================


}
