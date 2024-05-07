package weblogic.jms.adapter51;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Set;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.SecurityException;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;

public class Util {
   public static PasswordCredential getPasswordCredential(ManagedConnectionFactory var0, final Subject var1, ConnectionRequestInfo var2) throws ResourceException {
      JMSConnectionRequestInfo var3 = (JMSConnectionRequestInfo)var2;
      if (var2 != null && var3.getUser() != null && var3.getPassword() != null) {
         PasswordCredential var8 = new PasswordCredential(var3.getUser(), var3.getPassword().toCharArray());
         var8.setManagedConnectionFactory(var0);
         return var8;
      } else if (var1 == null) {
         return null;
      } else {
         Set var4 = (Set)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               return var1.getPrivateCredentials(PasswordCredential.class);
            }
         });
         PasswordCredential var5 = null;
         Iterator var6 = var4.iterator();

         while(var6.hasNext()) {
            PasswordCredential var7 = (PasswordCredential)var6.next();
            if (var7.getManagedConnectionFactory().equals(var0)) {
               var5 = var7;
               break;
            }
         }

         if (var5 == null) {
            throw new SecurityException("No PasswordCredential found");
         } else {
            return var5;
         }
      }
   }

   public static boolean isEqual(String var0, String var1) {
      if (var0 == null) {
         return var1 == null;
      } else {
         return var0.equals(var1);
      }
   }
}
