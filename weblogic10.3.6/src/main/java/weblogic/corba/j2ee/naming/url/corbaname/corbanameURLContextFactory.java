package weblogic.corba.j2ee.naming.url.corbaname;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;
import weblogic.corba.j2ee.naming.ContextImpl;
import weblogic.corba.j2ee.naming.InitialContextFactoryImpl;
import weblogic.corba.j2ee.naming.NameParser;

public class corbanameURLContextFactory implements ObjectFactory {
   private static final boolean DEBUG = false;

   private static final void p(String var0) {
      System.err.println("<corbanameURLContextFactory> " + var0);
   }

   public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws Exception {
      String var5 = null;
      Object var6 = null;
      if (var1 instanceof String) {
         var5 = (String)var1;
      } else if (var1 instanceof Reference) {
         RefAddr var7 = ((Reference)var1).get("nns");
         if (var7 == null) {
            var7 = ((Reference)var1).get("URL");
         }

         if (var7 instanceof StringRefAddr) {
            var5 = (String)var7.getContent();
         }
      }

      if (var1 == null) {
         var6 = new ContextImpl(var4);
      } else if (NameParser.getProtocol(var5) != null) {
         var6 = InitialContextFactoryImpl.getInitialContext(var4, var5);
      }

      return var6;
   }
}
