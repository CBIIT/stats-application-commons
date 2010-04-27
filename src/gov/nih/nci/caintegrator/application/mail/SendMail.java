package gov.nih.nci.caintegrator.application.mail;


import gov.nih.nci.caintegrator.exceptions.ValidationException;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;


public class SendMail {
	
	private static Logger logger = Logger.getLogger(SendMail.class);
	
	private String mailProperties; 
	 
	
	public SendMail(String mailProperties){
		this.mailProperties = mailProperties;
	}
	
	public synchronized void sendMail(String mailTo, String mailCC, String mailBody, String subject )
		throws ValidationException
	{
		try
		{
			if(mailTo != null && EmailValidator.getInstance().isValid(mailTo))
			{
				//get system properties
				Properties props = System.getProperties();
				
				String to=mailTo;
				// Set up mail server
				
				props.put("mail.smtp.host",MailConfig.getInstance(mailProperties).getHost());
				
				//Get session
				Session session = Session.getDefaultInstance(props, null);
				
				//Define Message
				MimeMessage message = new MimeMessage(session);
				MailManager mailManager = new MailManager(mailProperties);
				message.setFrom(new InternetAddress(mailManager.formatFromAddress()));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				if ((mailCC != null) && EmailValidator.getInstance().isValid(mailCC))
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(mailCC));
				message.setSubject(subject);
			    message.setText(mailBody);
			    
				//Send Message
				
				Transport.send(message);
			}
			else
			{
				throw new ValidationException("Invalid Email Address");
			}
		}
		catch (Exception e)
		{
			logger.error("Send Mail error", e);
		}	//catch
	}//send mail
	
}//Sendmail


