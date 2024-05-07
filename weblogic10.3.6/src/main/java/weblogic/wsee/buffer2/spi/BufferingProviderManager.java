package weblogic.wsee.buffer2.spi;

import weblogic.wsee.buffer2.exception.BufferingException;

public class BufferingProviderManager {
   private static volatile BufferingProvider _theBufferingProvider;
   private static String PROVIDER_WLS = "weblogic.wsee.buffer2.provider.wls.BufferingProvider_WLS";
   private static String PROVIDER_J2EE = "weblogic.wsee.buffer2.provider.j2ee.BufferingProvider_J2EE";

   public static BufferingProvider getBufferingProvider() throws BufferingException {
      if (_theBufferingProvider == null) {
         Class var0 = BufferingProviderManager.class;
         synchronized(BufferingProviderManager.class) {
            if (_theBufferingProvider == null) {
               App_Server_Platform[] var1 = BufferingProviderManager.App_Server_Platform.values();
               int var2 = var1.length;

               for(int var3 = 0; var3 < var2; ++var3) {
                  App_Server_Platform var4 = var1[var3];

                  try {
                     _theBufferingProvider = var4.getBufferingProvider();
                  } catch (BufferingException var7) {
                  }
               }
            }

            if (_theBufferingProvider == null) {
               throw new BufferingException("Unable to load BufferingProvider");
            }
         }
      }

      return _theBufferingProvider;
   }

   public static BufferingProvider getBufferingProvider(App_Server_Platform var0) throws BufferingException {
      if (var0 == null) {
         throw new BufferingException("Error.  Cannot Load using NULL platform.");
      } else {
         if (_theBufferingProvider == null) {
            Class var1 = BufferingProviderManager.class;
            synchronized(BufferingProviderManager.class) {
               if (_theBufferingProvider == null) {
                  _theBufferingProvider = var0.getBufferingProvider();
               }
            }
         }

         return _theBufferingProvider;
      }
   }

   public static void setBufferingProvider(BufferingProvider var0) {
      Class var1 = BufferingProviderManager.class;
      synchronized(BufferingProviderManager.class) {
         _theBufferingProvider = var0;
      }
   }

   public static enum App_Server_Platform {
      WLS("WLS", BufferingProviderManager.PROVIDER_WLS) {
      },
      J2EE("J2EE", BufferingProviderManager.PROVIDER_J2EE) {
      };

      private String name;
      private String providerClassName;

      private App_Server_Platform(String var3, String var4) {
         this.name = var3;
         this.providerClassName = var4;
      }

      public String getName() {
         return this.name;
      }

      public String getProviderClassName() {
         return this.providerClassName;
      }

      public String toString() {
         return this.name;
      }

      BufferingProvider getBufferingProvider() throws BufferingException {
         BufferingProvider var1 = null;

         try {
            Class var2 = Class.forName(this.providerClassName);
            var1 = (BufferingProvider)var2.newInstance();
            return var1;
         } catch (Exception var3) {
            throw new BufferingException("Could not load BufferingProvider for App Server Platform '" + this.name + "'");
         }
      }

      // $FF: synthetic method
      App_Server_Platform(String var3, String var4, Object var5) {
         this(var3, var4);
      }
   }
}
