package weblogic.wsee.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.management.descriptors.webservice.StatelessEJBMBean;

public final class EJBHelper {
   public static Object getEJBHome(StatelessEJBMBean var0) throws NamingException {
      try {
         InitialContext var1 = new InitialContext();
         if (var0.getJNDIName() != null) {
            return var1.lookup(var0.getJNDIName().getPath());
         } else {
            String var2 = var0.getEJBLink().getPath();
            Object var3 = null;

            try {
               var3 = var1.lookup("java:/app/ejb/" + var2 + "/home");
            } catch (NamingException var6) {
            }

            try {
               var3 = var1.lookup("java:/app/ejb/" + var2 + "/local-home");
            } catch (NamingException var5) {
            }

            if (var3 == null) {
               throw new NamingException("Could not lookup EJB home, tried java:/app/ejb/" + var2 + "/home" + " and java:/app/ejb/" + var2 + "/localhome");
            } else {
               return var3;
            }
         }
      } catch (NamingException var7) {
         var7.printStackTrace();
         throw var7;
      }
   }
}
