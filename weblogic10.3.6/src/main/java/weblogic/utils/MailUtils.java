package weblogic.utils;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {
   public static void sendMail(String var0, String var1, String var2, String var3, String var4) throws MessagingException {
      Properties var5 = new Properties();
      var5.put("mail.smtp.host", var0);
      Session var6 = Session.getDefaultInstance(var5, (Authenticator)null);
      MimeMessage var7 = new MimeMessage(var6);
      var7.setFrom(new InternetAddress(var1));
      InternetAddress[] var8 = new InternetAddress[]{new InternetAddress(var2)};
      var7.setRecipients(RecipientType.TO, var8);
      var7.setSubject(var3);
      var7.setSentDate(new Date());
      var7.setText(var4);
      Transport.send(var7);
   }
}
