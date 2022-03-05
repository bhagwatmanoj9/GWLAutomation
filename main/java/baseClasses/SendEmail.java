package baseClasses;



import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class SendEmail {
 Utilities util = new Utilities();

 public String getExecutionStartMessage()
 {
  String header="<html><head></head><Body>";
  header+= "<style type=text/css>"+
    "body {"+
    "height:100%;"+
    "background:-moz-linear-gradient(top, #666, #CCC) no-repeat #ccc;"+
    "background:-webkit-gradient(linear, left top, left bottom, from(#666), to(#ccc)) no-repeat #ccc;"+
    "}"+
    "</style>";
  header +="<p align = center><table border=0 id=table1 width=65% height=31>" +
    "<tr>"+
    "<td COLSPAN = 4 bgcolor = #E3F6CE>"+
    "<p align=justify><b><font color=Black size=2 face= Verdana>DATE:"+ util.getCurrentDatenTime("dd MMMMM yyyy 'at' HH:mm:ss")+
    "</td>" +
    "</tr>";
  header+="<tr bgcolor=#D8D8D8 height=123>" +
    "<td align =center><b>Automation script for regression testing is started--This is Auto generated Mail</b></td>"+
    "</tr>"+
    "<tr><td COLSPAN = 4 bgcolor = #E3F6CE>This is Auto generated through Auotomation scripts</td></tr>"; 


  return header;
 }

 public void sendMessageWithAttachment(String subject,String setMessage,String attachment, boolean attachedFlag)throws Exception
 {
  Utilities util = new Utilities();

  final String from = "automation.ctaf@gmail.com";
  final String password = "$Reset123$";
  String to =util.getValue("EmailGroup", "automation.ctaf@gmail.com").trim();

  String temp[] = null;
  if(to.indexOf(",")>0)
  {
   temp = to.split(",");

  }
  else
  {
   temp = to.split(",");
   temp[0]=to;
  }
  String fileAttachment = attachment;
  // Get system properties
  Properties props = System.getProperties();

  // Setup mail server
  props.put("mail.smtp.auth", "true");
  props.put("mail.smtp.starttls.enable", "true");
  props.put("mail.smtp.host", "smtp.gmail.com");
  props.put("mail.smtp.port", "587");

  Session session = Session.getInstance(props,
    new javax.mail.Authenticator() {
   protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(from, password);
   }
  });


  // Define message
  MimeMessage message = new MimeMessage(session);
  message.setFrom(new InternetAddress(from));
  for(int i=0;i<temp.length;i++)
  {
   message.addRecipient(Message.RecipientType.TO, new InternetAddress(temp[i]));
  }

  //message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
  message.setSubject(subject);

  // create the message part 
  MimeBodyPart messageBodyPart =  new MimeBodyPart();
  //fill message
  messageBodyPart.setContent(setMessage,"text/html");
  //messageBodyPart.setText(setMessage);

  Multipart multipart = new MimeMultipart();
  multipart.addBodyPart(messageBodyPart);

  if(attachedFlag)
  {
   // Part two is attachment
   messageBodyPart = new MimeBodyPart();
   DataSource source = new FileDataSource(fileAttachment);
   messageBodyPart.setDataHandler(new DataHandler(source));
   //messageBodyPart.setFileName(fileAttachment);

   String fName[] = fileAttachment.split("//");
   String fileName = fName[fName.length - 1];
   messageBodyPart.setFileName(fileName);

   multipart.addBodyPart(messageBodyPart);
  }
  // Put parts in message
  message.setContent(multipart);

  // Send the message
  Transport.send( message );
  Report.LogInfo("EmailSent", "Email Sent : "+subject, "Info");
 }

}