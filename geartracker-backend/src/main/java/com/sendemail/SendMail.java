//Basic template for mailing from https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/, with improvisation being done as per needs.

package com.sendemail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.lang.Thread;

public class SendMail {
	
	public static void sendmail(String to_address, String from_address, String password,String subject, String body){
        // Assuming you are sending email from through gmails smtp
		Thread thread = new Thread() {
			public void run() {
				String host = "smtp.gmail.com";
				// Get system properties
				Properties properties = System.getProperties();
				// Setup mail server
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.ssl.enable", "true");
				properties.put("mail.smtp.auth", "true");

				// Get the Session object and pass username and password
				Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from_address, password);
					}
				});

				// Used to debug SMTP issues
				session.setDebug(true);

				try {
					// Create a default MimeMessage object.
					MimeMessage message = new MimeMessage(session);

					// Set From: header field of the header.
					message.setFrom(new InternetAddress(from_address));

					// Set To: header field of the header.
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_address));

					// Set Subject: header field
					message.setSubject(subject);

					// Now set the actual message
					message.setText(body);
            
					System.out.println("sending...");
					// Send message
					Transport.send(message);
					System.out.println("Sent message successfully....");
				} catch (MessagingException mex) {
					mex.printStackTrace();
				}
			}
		};
		thread.start();
	}

    public static void main(String[] args) {
    	SendMail.sendmail("Hemanth.Chitti@iiitb.ac.in", "geartrackertesting486@gmail.com", "geartrackertesting684","Demo using Gmail SMTP","This has been sent using Gmail SMTP client.");      
    }

}
