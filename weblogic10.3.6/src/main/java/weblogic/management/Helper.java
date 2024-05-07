package weblogic.management;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;

/** @deprecated */
public final class Helper {
   public static final MBeanHome getAdminMBeanHome(String var0, String var1, String var2) throws IllegalArgumentException {
      return getMBeanHomeForName(var0, var1, var2, "weblogic.management.adminhome");
   }

   public static final MBeanHome getMBeanHome(String var0, String var1, String var2, String var3) throws IllegalArgumentException {
      return getMBeanHomeForName(var0, var1, var2, "weblogic.management.home." + var3);
   }

   private static MBeanHome getMBeanHomeForName(String var0, String var1, String var2, String var3) throws IllegalArgumentException {
      Context var4 = null;
      MBeanHome var5 = null;
      if (var0 != null && var1 != null && var2 != null) {
         MBeanHome var7;
         try {
            Environment var6 = new Environment();
            var6.setProviderUrl(var2);
            var6.setSecurityPrincipal(var0);
            var6.setSecurityCredentials(var1);
            var6.setEnableDefaultUser(true);
            var4 = var6.getInitialContext();
            var5 = (MBeanHome)var4.lookup(var3);
            var7 = var5;
         } catch (AuthenticationException var18) {
            throw new IllegalArgumentException("Invalid user name or password, " + var18);
         } catch (CommunicationException var19) {
            throw new IllegalArgumentException("Failed to contact " + var2 + ", " + var19);
         } catch (NamingException var20) {
            throw new IllegalArgumentException("JNDI naming exception: " + var20);
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (NamingException var17) {
               }
            }

         }

         return var7;
      } else {
         throw new IllegalArgumentException("All arguments must be non null");
      }
   }
}
