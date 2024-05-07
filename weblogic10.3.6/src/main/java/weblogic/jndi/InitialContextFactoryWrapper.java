package weblogic.jndi;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Properties;
import java.util.WeakHashMap;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.spi.InitialContextFactory;

public class InitialContextFactoryWrapper implements InitialContextFactory {
   public static final String DEFAULT_INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   private static final String APP_RESOURCE_FILE_NAME = "jndi.properties";
   private static final WeakHashMap factoryCache = new WeakHashMap(11);

   private ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   private Properties getProperties(final ClassLoader var1, final String var2) throws IOException {
      InputStream var3 = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return var1 == null ? ClassLoader.getSystemResourceAsStream(var2) : var1.getResourceAsStream(var2);
         }
      });
      Properties var4 = new Properties();
      if (var3 != null) {
         var4.load(var3);
      }

      return var4;
   }

   private String getInitialContextFactory() throws NamingException {
      ClassLoader var1 = this.getContextClassLoader();
      synchronized(factoryCache) {
         String var3 = (String)factoryCache.get(var1);
         if (var3 != null) {
            return var3;
         } else if (factoryCache.containsKey(var1)) {
            return null;
         } else {
            try {
               var3 = this.getProperties(var1, "jndi.properties").getProperty("java.naming.factory.initial");
            } catch (IOException var7) {
               ConfigurationException var5 = new ConfigurationException("Error reading application resource file");
               var5.setRootCause(var7);
               throw var5;
            }

            factoryCache.put(var1, var3);
            return var3;
         }
      }
   }

   public final Context getInitialContext(Hashtable var1) throws NamingException {
      InitialContextFactory var2 = null;
      String var3 = this.getInitialContextFactory();
      if (var3 == null) {
         var3 = "weblogic.jndi.WLInitialContextFactory";
      }

      try {
         ClassLoader var4 = this.getContextClassLoader();
         var2 = (InitialContextFactory)Class.forName(var3, true, var4).newInstance();
      } catch (Exception var6) {
         NoInitialContextException var5 = new NoInitialContextException("Cannot instantiate class: " + var3);
         var5.setRootCause(var6);
         throw var5;
      }

      return var2.getInitialContext(var1);
   }
}
