//Code for sending mail taken from https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
//And modified for our purposes.

//package com.sendemail;
//
//import java.util.Properties;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//public class SendMail {
//
//    public static void main(String[] args) {
//
//        // Recipient's email ID needs to be mentioned.
//        String to = "Hemanth.Chitti@iiitb.ac.in";
//
//        // Sender's email ID needs to be mentioned
//        String from = "geartrackertesting486@gmail.com";
//
//        // Assuming you are sending email from through gmails smtp
//        String host = "smtp.gmail.com";
//
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.auth", "true");
//
//        // Get the Session object.// and pass username and password
//        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
//
//            protected PasswordAuthentication getPasswordAuthentication() {
//
//                return new PasswordAuthentication("geartrackertesting486@gmail.com", "geartrackertesting684");
//
//            }
//
//        });
//
//        // Used to debug SMTP issues
//        session.setDebug(true);
//
//        try {
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//            // Set Subject: header field
//            message.setSubject("Demo using Gmail SMTP");
//
//            // Now set the actual message
//            message.setText("This has been sent using Gmail SMTP client.");
//
//            System.out.println("sending...");
//            // Send message
//            Transport.send(message);
//            System.out.println("Sent message successfully....");
//        } catch (MessagingException mex) {
//            mex.printStackTrace();
//        }
//
//    }
//
//}