package baseClasses;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

import org.apache.groovy.parser.antlr4.GroovyParser.SwitchBlockStatementGroupContext;

import java.io.IOException;
import java.util.Properties;

public class MailSender {

	Session getMailSession(Properties properties) {
		Session session = null;

		try {
			session = Session.getDefaultInstance(properties);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return session;
	}

	Folder getFolder(String folderName, String username, String password, Session session) {
		Folder folder = null;
		Store store = null;
		try {
			store = session.getStore("imap");
			store.connect(username, password);
			// opens the inbox folder
			folder = store.getFolder(folderName.toUpperCase());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return folder;
	}

	Message getMessage(String subject, Folder folder) {

		Message message = null;
		// creates a search criterion
		SearchTerm searchCondition = new SearchTerm() {
			@Override
			public boolean match(Message message) {
				try {
					if (message.getSubject().contains(subject)) {
						return true;
					}
				} catch (MessagingException ex) {
					ex.printStackTrace();
				}
				return false;
			}
		};

		// performs search through the folder
		Message[] foundMessages = null;
		try {
			foundMessages = folder.search(searchCondition);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		if (foundMessages.length > 1) {
			message = foundMessages[foundMessages.length - 1];
		}
		return message;
	}

	boolean checkText(String content, Message message) {
		boolean result = false;
		MimeMultipart mimeMultipart = null;

		try {
			System.out.println(message.getContentType());

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return result;
	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) {
		String result = "";
		int count = 0;
		try {
			count = mimeMultipart.getCount();
			for (int i = 0; i < count; i++) {
				BodyPart bodyPart = mimeMultipart.getBodyPart(i);
				System.out.println(bodyPart.getContentType());
				if (bodyPart.isMimeType("TEXT/PLAIN; charset=UTF-8")) {
					result = (String) bodyPart.getContent();
					break;
				}
			}
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	Properties getProp() {
		Properties properties = new Properties();

		// server setting
		properties.put("mail.imap.host", "imap.gmail.com");
		properties.put("mail.imap.port", "993");

		// SSL setting
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port", String.valueOf(993));
		return properties;
	}

	public String fetchMailText(String username, String password, String subject) {
		String body = "";
		try {
			MailSender mailSender = new MailSender();
			Session session = mailSender.getMailSession(mailSender.getProp());
			Folder folder = mailSender.getFolder("INBOX", username, password, session);
			folder.open(Folder.READ_ONLY);

			Message message = mailSender.getMessage(subject, folder);
			if (message == null) {
				System.out.println("No message found ");
			} else {
				System.out.println("Found message #" + ": " + message.getSubject());
				MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
				body = mailSender.getTextFromMimeMultipart(mimeMultipart);
//				System.out.println(body.contains("$743.16"));
//				System.out.println(body.contains("GBFB")); 
			}
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return body;
	}

	public void fetchMail(String dates) throws MessagingException, IOException { 

		MailSender mailSender = new MailSender();
		Session session = mailSender.getMailSession(mailSender.getProp());
		Folder folder = mailSender.getFolder("INBOX", "qatesterng.04@gmail.com", "$Reset123$", session);
		folder.open(Folder.READ_ONLY);

		Message message = mailSender.getMessage("Confirmation for Reservation", folder);
		if (message == null) {
			System.out.println("No message found ");
		} else {
			System.out.println("Found message #" + ": " + message.getSubject());
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			String body = mailSender.getTextFromMimeMultipart(mimeMultipart); 
				//System.out.println(body);
			String[] split = dates.split("-");
				System.out.println(body.contains(split[0].trim()));
				System.out.println(body.contains(split[1].trim()));  
		}

		}
		public void fetchTotalFromMail(String total) throws MessagingException, IOException { 

			MailSender mailSender = new MailSender();
			Session session = mailSender.getMailSession(mailSender.getProp());
			Folder folder = mailSender.getFolder("INBOX", "qatesterng.04@gmail.com", "$Reset123$", session);
			folder.open(Folder.READ_ONLY);

			Message message = mailSender.getMessage("Confirmation for Reservation", folder);
			if (message == null) {
				System.out.println("No message found ");
			} else {
				System.out.println("Found message #" + ": " + message.getSubject());
				MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
				String body = mailSender.getTextFromMimeMultipart(mimeMultipart); 
				
				System.out.println(body.contains(total));

			
					//System.out.println(body.contains(split[0].trim()));
					//System.out.println(body.contains(split[1].trim()));  


			}
	}
	public static void main(String[] args) throws MessagingException, IOException {
		
		new MailSender().fetchMail("Oct. 25, 2022 - Oct. 27, 2022".replace(".", ""));
		
	
		//System.out.println(s1.equals(s2));
		
	}
}
