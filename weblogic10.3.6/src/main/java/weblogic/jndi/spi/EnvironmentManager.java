package weblogic.jndi.spi;

import java.util.Locale;
import javax.naming.NamingException;
import weblogic.jndi.WLInitialContextFactoryDelegate;
import weblogic.utils.collections.ConcurrentHashMap;

public final class EnvironmentManager {
   public static final ConcurrentHashMap registeredFactories = new ConcurrentHashMap(11);
   private static final Object NULL_FACTORY = new Object();

   public static EnvironmentFactory getInstance(String var0) throws NamingException {
      var0 = var0.toLowerCase(Locale.ENGLISH);
      Object var1 = registeredFactories.get(var0);
      if (var1 == null) {
         try {
            var1 = Class.forName("weblogic.factories." + var0 + "." + var0 + "EnvironmentFactory", true, Thread.currentThread().getContextClassLoader()).newInstance();
         } catch (ClassNotFoundException var3) {
            var1 = EnvironmentManager.DefaultFactoryMaker.DEFAULT_FACTORY;
         } catch (InstantiationException var4) {
            var1 = NULL_FACTORY;
         } catch (IllegalAccessException var5) {
            var1 = NULL_FACTORY;
         }

         registeredFactories.put(var0, var1);
      }

      if (var1 == NULL_FACTORY) {
         throw new NamingException("No registered factory for " + var0);
      } else {
         return (EnvironmentFactory)var1;
      }
   }

   private static final class DefaultFactoryMaker {
      private static final EnvironmentFactory DEFAULT_FACTORY = WLInitialContextFactoryDelegate.theOne();
   }
}
