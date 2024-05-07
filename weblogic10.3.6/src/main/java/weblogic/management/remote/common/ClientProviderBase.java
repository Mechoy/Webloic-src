package weblogic.management.remote.common;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import javax.management.remote.rmi.RMIServer;
import javax.naming.AuthenticationException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.security.auth.Subject;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.security.Security;

public class ClientProviderBase implements JMXConnectorProvider {
   public static final String LOCALE_KEY = "weblogic.management.remote.locale";
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXCore");

   public JMXConnector newJMXConnector(JMXServiceURL var1, Map var2) throws IOException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Create new JMX connector for " + var1);
      }

      HashMap var3 = new HashMap();
      var3.putAll(var2);
      var3.put("jmx.remote.x.notification.fetch.timeout", new Long(1000L));
      boolean var4 = true;
      if (var1.getProtocol().startsWith("iiop")) {
         String var5 = System.getProperty("weblogic.system.iiop.enableClient");
         if (var5 != null && var5.equals("false")) {
            var4 = false;
         }
      }

      if (var4 && !var3.containsKey("jmx.remote.x.client.connection.check.period")) {
         var3.put("jmx.remote.x.client.connection.check.period", new Long(0L));
      }

      return this.makeConnection(var1, var3);
   }

   private RMIConnector makeConnection(JMXServiceURL var1, Map var2) throws IOException {
      String var3 = var1.getURLPath();
      if (!var3.startsWith("/jndi/")) {
         throw new MalformedURLException("URL path must begin with /jndi/");
      } else {
         if (!var2.containsKey("java.naming.provider.url") && System.getProperty("java.naming.provider.url") == null) {
            String var4 = var1.getHost();
            boolean var5 = var4.indexOf(58) >= 0;
            String var6 = var1.getProtocol() + "://" + (var5 ? "[" : "") + var1.getHost() + (var5 ? "]" : "") + ":" + var1.getPort();
            var2.put("java.naming.provider.url", var6);
         }

         if (!var2.containsKey("java.naming.factory.initial")) {
            var2.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         }

         Object var25;
         if (!var2.containsKey("jmx.remote.credentials") && var2.containsKey("java.naming.security.principal") && var2.containsKey("java.naming.security.credentials")) {
            var25 = var2.get("java.naming.security.principal");
            Object var27 = var2.get("java.naming.security.credentials");
            if (var25 instanceof String && var27 instanceof String) {
               String[] var30 = new String[]{(String)var25, (String)var27};
               var2.put("jmx.remote.credentials", var30);
            }
         }

         if (!var2.containsKey("java.naming.security.principal") && var2.containsKey("jmx.remote.credentials")) {
            var25 = var2.get("jmx.remote.credentials");
            if (var25 != null && var25 instanceof String[]) {
               String[] var28 = (String[])String[].class.cast(var25);
               if (var28.length == 2) {
                  var2.put("java.naming.security.principal", var28[0]);
                  var2.put("java.naming.security.credentials", var28[1]);
               }
            }
         }

         int var26 = 0;
         String var29 = "jmx.remote.x.request.waiting.timeout";
         if (var2.containsKey(var29)) {
            Object var31 = var2.get(var29);
            if (var31 instanceof Long) {
               var26 = ((Long)var31).intValue();
               if (var26 > 0) {
                  var2.put("weblogic.jndi.requestTimeout", var31);
                  var2.put("weblogic.rmi.clientTimeout", var31);
               } else {
                  var26 = Integer.MAX_VALUE;
                  var2.put("weblogic.rmi.clientTimeout", new Long((long)var26));
               }
            }
         }

         Locale var32 = (Locale)var2.remove("weblogic.management.remote.locale");
         if (var32 == null) {
            var32 = getSystemPropertyLocale();
         }

         Hashtable var7 = mapToHashtable(var2);
         InitialContext var9 = null;
         Subject var10 = null;
         boolean var11 = true;

         Object var8;
         try {
            IOException var13;
            try {
               var9 = new InitialContext(var7);
               var8 = var9.lookup(var3.substring(6));
               var10 = Security.getCurrentSubject();
               if (var26 > 0) {
                  var11 = false;
               }
            } catch (NamingException var22) {
               if (var22 instanceof AuthenticationException) {
                  if (var22.getCause() != null && var22.getCause() instanceof SecurityException) {
                     throw (SecurityException)var22.getCause();
                  }

                  throw new SecurityException(var22.getCause());
               }

               if (var22 instanceof NoPermissionException) {
                  throw new SecurityException("Anonymous attempt to get to a JNDI resource");
               }

               var13 = new IOException(var22.getMessage());
               var13.initCause(var22);
               throw var13;
            } catch (Exception var23) {
               if (var23 instanceof IOException) {
                  throw (IOException)var23;
               }

               var13 = new IOException(var23.getMessage());
               var13.initCause(var23);
               throw var13;
            }
         } finally {
            if (var9 != null && var11) {
               try {
                  var9.close();
               } catch (NamingException var21) {
               }

               var9 = null;
            }

         }

         RMIServerWrapper var12 = new RMIServerWrapper(narrowServer(var8), var10, var9, var32);
         return new WLSRMIConnector(var12, var2, var10);
      }
   }

   private static RMIServer narrowServer(Object var0) {
      try {
         return (RMIServer)PortableRemoteObject.narrow(var0, RMIServer.class);
      } catch (ClassCastException var2) {
         return null;
      }
   }

   private static Hashtable mapToHashtable(Map var0) {
      Hashtable var1 = new Hashtable(var0.size());
      Iterator var2 = var0.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         Object var4 = var3.getKey();
         Object var5 = var3.getValue();
         if (var4 != null && var5 != null && var5 instanceof Serializable) {
            var1.put(var4, var5);
         }
      }

      return var1;
   }

   protected static Locale getSystemPropertyLocale() throws IllegalArgumentException {
      String var0 = System.getProperty("weblogic.management.remote.locale");
      if (var0 == null) {
         return null;
      } else {
         String[] var1 = var0.split("-");
         if (var1.length == 1) {
            return new Locale(var1[0]);
         } else if (var1.length == 2) {
            return new Locale(var1[0], var1[1]);
         } else if (var1.length == 3) {
            return new Locale(var1[0], var1[1], var1[2]);
         } else {
            throw new IllegalArgumentException("Invalid value for -Dweblogic.management.remote.locale: " + var0 + " " + "Valid values are of the form lowercase two-letter ISO-639 code or " + "(lowercase two-letter ISO-639 code)-(upper-case, two-letter codes ISO-3166) or " + "(lowercase two-letter ISO-639 code)-(upper-case, two-letter codes ISO-3166)-(variant code) " + "For instance es or es-ES or es-ES-Traditional_WIN");
         }
      }
   }
}
