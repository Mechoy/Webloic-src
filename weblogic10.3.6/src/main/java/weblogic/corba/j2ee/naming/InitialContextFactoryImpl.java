package weblogic.corba.j2ee.naming;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NamingContext;
import weblogic.corba.client.spi.ServiceManager;

public final class InitialContextFactoryImpl implements InitialContextFactory {
   private static final boolean DEBUG = false;

   public Context getInitialContext(Hashtable var1) throws NamingException {
      String var2 = (String)(var1 == null ? null : var1.remove("java.naming.provider.url"));
      if (var2 == null) {
         var2 = "iiop://localhost:7001";
      }

      return getInitialContext(var1, var2);
   }

   public static Context getInitialContext(Hashtable var0, String var1) throws NamingException {
      NamingContext var2 = null;
      Object var3 = null;
      ORBInfo var4 = null;
      if (var1.startsWith("IOR:")) {
         var4 = ORBHelper.getORBHelper().getCurrent();
         if (var4 == null) {
            ORB var5 = ORBHelper.getORBHelper().getLocalORB();
            var3 = var5.string_to_object(var1);
         } else {
            var3 = var4.getORB().string_to_object(var1);
         }
      } else {
         var3 = ORBHelper.getORBHelper().getORBReference(var1, var0, "NameService");
         var4 = ORBHelper.getORBHelper().getCurrent();
      }

      var2 = Utils.narrowContext(var3);
      if (var2 == null) {
         throw new NamingException("Could not resolve context from: " + var1);
      } else {
         ContextImpl var6 = new ContextImpl(var0, var4, var2);
         ServiceManager.getSecurityManager().pushSubject(var0, var6);
         ORBHelper.getORBHelper().pushTransactionHelper();
         return var6;
      }
   }

   static void p(String var0) {
      System.err.println("<InitialContextFactoryImpl> " + var0);
   }
}
