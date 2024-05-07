package weblogic.wsee.connection.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import weblogic.deployment.jms.ForeignOpaqueReference;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.VerboseInputStream;
import weblogic.xml.saaj.util.CopyUtils;

public class TransportUtil {
   public static InputStream dumpHttpInput(HttpURLConnection var0) throws IOException {
      Verbose.say("   ** S T A R T   R E S P O N S E **");
      Verbose.say(var0.getResponseCode() + " " + var0.getResponseMessage());
      dumpHeaders(var0.getHeaderFields());
      InputStream var1 = null;

      try {
         var1 = getInputStream(var0);
      } catch (IOException var4) {
      }

      if (var1 != null) {
         try {
            var1 = dumpInput(var1);
         } catch (IOException var3) {
            Verbose.log((Object)("IOException during dump http input " + var3));
            Verbose.logException(var3);
         }
      }

      Verbose.say("");
      Verbose.say("   ** E N D   R E S P O N S E **");
      return var1;
   }

   static void dumpHeaders(Map<String, List<String>> var0) {
      Iterator var1 = var0.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         List var3 = (List)var0.get(var2);
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            Verbose.say(var2 + ": " + var5);
         }
      }

      Verbose.say("");
   }

   public static InputStream dumpInput(InputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      VerboseInputStream var2 = new VerboseInputStream(var0);
      CopyUtils.copy(var2, var1);
      var2.close();
      return new ByteArrayInputStream(var1.toByteArray());
   }

   public static InputStream getInputStream(HttpURLConnection var0) throws IOException {
      try {
         return var0.getInputStream();
      } catch (IOException var3) {
         InputStream var2 = var0.getErrorStream();
         if (var2 == null) {
            throw var3;
         } else {
            return var2;
         }
      }
   }

   public static boolean getForeignCredentials(String var0, InitialContext var1, StringBuffer var2, StringBuffer var3) {
      boolean var4 = false;

      try {
         Object var5 = var1.lookupLink(var0);
         if (var5 instanceof ForeignOpaqueReference) {
            ForeignOpaqueReference var6 = (ForeignOpaqueReference)var5;
            boolean var7 = var6.isFactory();
            if (var7) {
               String var8 = var6.getUsername();
               String var9 = var6.getPassword();
               if (var8 != null && var8.length() > 0) {
                  var2.append(var8);
                  var4 = true;
               }

               if (var9 != null && var9.length() > 0) {
                  var4 = true;
                  var3.append(var9);
               }

               if (!var4) {
                  Hashtable var10 = var6.getJNDIEnvironment();
                  Object var11 = var10.get("java.naming.security.principal");
                  if (var11 != null && ((String)var11).length() > 0) {
                     var4 = true;
                     var2.append((String)var11);
                  }

                  Object var12 = var10.get("java.naming.security.credentials");
                  if (var12 != null && ((String)var12).length() > 0) {
                     var4 = true;
                     var3.append((String)var12);
                  }
               }
            }
         }
      } catch (Exception var13) {
         Verbose.log((Object)("Can't get credentials associated with foreign jmsconnection factory: " + var13));
      }

      return var4;
   }
}
