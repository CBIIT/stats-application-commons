/**
 * 
 */
package gov.nih.nci.caintegrator.application.util;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author sahnih
 * checks whether a web page exists or not
 * from http://www.java-tips.org/java-se-tips/java.net/check-if-a-page-exists-2.html 
 * 
 */
public class URLChecker {

	static boolean exists(String URLName){
	  try {
	    HttpURLConnection.setFollowRedirects(false);
	    // note : you may also need
	    //        HttpURLConnection.setInstanceFollowRedirects(false)
	    HttpURLConnection con =
	       (HttpURLConnection) new URL(URLName).openConnection();
	    con.setRequestMethod("HEAD");
	    return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	  catch (Exception e) {
	       e.printStackTrace();
	       return false;
	       }
	}
	
	public static void main(String s[]) {
	    System.out.println(exists("http://www.rgagnon.com/howto.html"));
	    System.out.println(exists("http://www.rgagnon.com/pagenotfound.html"));
	   }
}
