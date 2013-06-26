/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.security.genepattern;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptionUtil
{
  static final byte[] salt = { -87, -101, -56, 
    50, 86, 53, -29, 3 };
  static final int iterationCount = 19;
  static final DesEncrypter encrypter = new DesEncrypter("My Really Long Key");

  public static String encrypt(String message)
  {
    return encrypter.encrypt(message);
  }

  public static String decrypt(String message)
  {
    return encrypter.decrypt(message);
  }

  public static void main(String[] args) {
    String[] toEncrypt = { "RBTuser", "harrismic", "guruswamis", "liuh", "sahnih", "tester", "newuser" };
    for (String s : toEncrypt) {
      String e = encrypt(s + ":GP30:RBT");
      System.out.println("The secret message " + s + " encrypted:" + e);
      try {
        System.out.println("URL encoded message:" + URLEncoder.encode(e, "UTF-8"));
      } catch (UnsupportedEncodingException e1) {
        e1.printStackTrace();
      }
      System.out.println("The secret message decoded:" + decrypt(e));
    }
  }

  private static class DesEncrypter
  {
    Cipher ecipher;
    Cipher dcipher;

    DesEncrypter(String passPhrase)
    {
      try
      {
    	  PBEKeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), 
          EncryptionUtil.salt, 19);
        SecretKey key = 
          SecretKeyFactory.getInstance("PBEWithMD5AndDES")
          .generateSecret(keySpec);
        this.ecipher = Cipher.getInstance(key.getAlgorithm());
        this.dcipher = Cipher.getInstance(key.getAlgorithm());

        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(EncryptionUtil.salt, 
          19);

        this.ecipher.init(1, key, paramSpec);
        this.dcipher.init(2, key, paramSpec);
      } catch (java.security.InvalidAlgorithmParameterException keySpec) {
      } catch (java.security.spec.InvalidKeySpecException keySpec) {
      } catch (javax.crypto.NoSuchPaddingException keySpec) {
      } catch (java.security.NoSuchAlgorithmException keySpec) {
      }
      catch (java.security.InvalidKeyException keySpec) {
      }
    }

    public String encrypt(String str) {
      try {
    	byte[] utf8 = str.getBytes("UTF8");

        byte[] enc = this.ecipher.doFinal(utf8);

        return new BASE64Encoder().encode(enc);
      } catch (javax.crypto.BadPaddingException utf8) {
      } catch (javax.crypto.IllegalBlockSizeException utf8) {
      } catch (UnsupportedEncodingException utf8) {
      }
      return null;
    }

    public String decrypt(String str)
    {
      try {
    	  byte[]  dec = new BASE64Decoder().decodeBuffer(str);

        byte[] utf8 = this.dcipher.doFinal(dec);

        return new String(utf8, "UTF8");
      } catch (javax.crypto.BadPaddingException dec) {
      } catch (javax.crypto.IllegalBlockSizeException dec) {
      } catch (UnsupportedEncodingException dec) {
      } catch (java.io.IOException dec) {
      }
      return null;
    }
  }
}