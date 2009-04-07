package gov.nih.nci.caintegrator.application.mail;


import gov.nih.nci.caintegrator.exceptions.ValidationException;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Mail {
		
	public Mail(){}
	
	public static synchronized void sendMail(MailProps mp) throws ValidationException	{
		try	{
			if(mp.getMailTo()!= null && mp.getSmtp() != null)	{
				//get system properties
				Properties props = System.getProperties();
				
				String to=mp.getMailTo();
				// Set up mail server
				
				props.put("mail.smtp.host",mp.getSmtp());
				
				//Get session
				Session session = Session.getDefaultInstance(props, null);
				
				//Define Message
				MimeMessage message = new MimeMessage(session);
		
				message.setFrom(new InternetAddress(mp.getMailFrom()));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				
				message.setSubject(mp.getSubject());
			    message.setText(mp.getBody());
			    
				//Send Message
				Transport.send(message);
			}
			else	{
				throw new ValidationException("Invalid Email Address");
			}
		}
		catch (Exception e)	{

		}	
	}	
}


