package gov.nih.nci.caintegrator.application.mail.test;

import gov.nih.nci.caintegrator.application.mail.MailManager;
import junit.framework.TestCase;
import java.util.ArrayList;

public class MailTest extends TestCase {

	public MailTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testMail() {
		
		 //MailManager mailMgr = new MailManager();
		 //System.out.println(mailMgr);
		 try {
			 
			 
             ArrayList<String> al = new ArrayList<String>();
             al.add("test.zip");
            String mailProperties = "gov.nih.nci.cgems.mail.properties";
            System.setProperty(mailProperties,"C:/local/content/cgems/cgems/config/mail.properties");
			MailManager mailManager = new MailManager(mailProperties);

			mailManager.sendFTPMail("sahnih@mail.nih.gov", al, null);
			mailManager.sendFTPErrorMail("sahnih@mail.nih.gov", null);
		 } catch (Exception e1) {
            e1.printStackTrace();
        }
	}

}
