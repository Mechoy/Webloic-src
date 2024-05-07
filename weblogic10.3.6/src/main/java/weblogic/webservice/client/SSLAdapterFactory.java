package weblogic.webservice.client;

import weblogic.kernel.KernelStatus;

/** @deprecated */
public class SSLAdapterFactory {
   private static boolean verbose = false;
   private static SSLAdapterFactory defaultFactory = new SSLAdapterFactory();
   private SSLAdapter defaultAdapter = null;
   private boolean useDefault = false;
   private boolean sslUnavailable = false;
   private Class adapterClass = null;
   private static final String[] adapterClasses = new String[]{"weblogic.webservice.client.WLSSLAdapter", "weblogic.webservice.client.JSSEAdapter", "weblogic.webservice.client.CDC.WLSSLAdapter", "weblogic.webservice.client.NullSSLAdapter"};
   public static final String SSL_ADAPTER_PROPERTY = "weblogic.webservice.client.ssl.adapterclass";
   private static final String userClassName = getUserClassNameProp();
   private String adapterClassName = null;

   private static String getUserClassNameProp() {
      try {
         return System.getProperty("weblogic.webservice.client.ssl.adapterclass");
      } catch (SecurityException var1) {
         return null;
      }
   }

   public static final void setDefaultFactory(SSLAdapterFactory var0) {
      if (var0 != null) {
         defaultFactory = var0;
      } else {
         throw new IllegalArgumentException("DefaultFactory cannot be set to null");
      }
   }

   public static final SSLAdapterFactory getDefaultFactory() {
      return defaultFactory;
   }

   public final SSLAdapter getDefaultAdapter() {
      if (this.defaultAdapter == null) {
         this.defaultAdapter = defaultFactory.createSSLAdapter();
      }

      return this.defaultAdapter;
   }

   public final void setDefaultAdapter(SSLAdapter var1) {
      this.defaultAdapter = var1;
   }

   public final void setUseDefaultAdapter(boolean var1) {
      this.useDefault = var1;
   }

   public final SSLAdapter getSSLAdapter() {
      return this.useDefault ? this.getDefaultAdapter() : this.createSSLAdapter();
   }

   public SSLAdapter createSSLAdapter() {
      if (this.sslUnavailable) {
         throw new SSLConfigurationException("No SSLAdapter class could be found.  The likely cause of this is an incomplete web service client libarary.  If no SSL implementation is available, the client should use NullSSLAdapter");
      } else {
         Class var1 = this.getAdapterClass();
         if (verbose) {
            System.out.println("Using SSLAdapter class " + this.adapterClassName);
         }

         Object var2 = null;

         try {
            var2 = var1.newInstance();
            return (SSLAdapter)var2;
         } catch (ClassCastException var4) {
            this.sslUnavailable = true;
            throw new IllegalArgumentException("Adapter " + this.adapterClassName + " did not return an instance of" + " SSLAdapter as required (returned " + var2.getClass().getName() + " instead)");
         } catch (InstantiationException var5) {
            this.sslUnavailable = true;
            throw new IllegalArgumentException("Adapter " + this.adapterClassName + " failed to return an instance" + " of SSLAdapter as required: " + var5.getMessage());
         } catch (IllegalAccessException var6) {
            this.sslUnavailable = true;
            throw new IllegalArgumentException("Adapter " + this.adapterClassName + " failed to return an instance" + " of SSLAdapter as required: " + var6.getMessage());
         }
      }
   }

   private Class getAdapterClass() {
      if (this.adapterClass != null) {
         return this.adapterClass;
      } else if (userClassName != null) {
         try {
            this.adapterClassName = userClassName;
            this.adapterClass = Class.forName(userClassName);
            return this.adapterClass;
         } catch (ClassNotFoundException var3) {
            throw new SSLConfigurationException("Could not find user specified SSLAdapter class " + this.adapterClassName);
         }
      } else {
         if (KernelStatus.isApplet()) {
            String var1 = "weblogic.webservice.client.JSSEAdapter";

            try {
               this.adapterClass = Class.forName(var1);
               return this.adapterClass;
            } catch (ClassNotFoundException var6) {
               if (verbose) {
                  System.out.println("failed to load " + var1);
               }
            } catch (NoClassDefFoundError var7) {
               if (verbose) {
                  System.out.println("failed to load " + var1);
               }
            }
         }

         for(int var8 = 0; var8 < adapterClasses.length; ++var8) {
            try {
               this.adapterClassName = adapterClasses[var8];
               this.adapterClass = Class.forName(this.adapterClassName);
               return this.adapterClass;
            } catch (ClassNotFoundException var4) {
               if (verbose) {
                  System.out.println("failed to load " + this.adapterClassName);
               }
            } catch (NoClassDefFoundError var5) {
               if (verbose) {
                  System.out.println("failed to load " + this.adapterClassName);
               }
            }
         }

         this.sslUnavailable = true;
         this.adapterClassName = null;
         throw new SSLConfigurationException("No SSLAdapter class could be found.  The likely cause of this is an incomplete web service client libarary.  If no SSL implementation is available, the client should use NullSSLAdapter");
      }
   }

   public static void main(String[] var0) {
      SSLAdapterFactory var1 = getDefaultFactory();
      System.out.println("Got default factory: " + var1);
      if (var1 == getDefaultFactory()) {
         System.out.println("Got the same adapter the second time");
      } else {
         System.out.println("!! Did not get same adapter the second time");
      }

      SSLAdapter var2 = var1.getDefaultAdapter();
      if (var2 == null) {
         System.out.println("!! Got null for adapter!");
      }

      System.out.println("Got defaultAdapter: " + var2);
      if (var2 == var1.getDefaultAdapter()) {
         System.out.println("Got the same adapter the second time");
      } else {
         System.out.println("!! Did not get same adapter the second time");
      }

      var1.setUseDefaultAdapter(true);
      System.out.println("Factory set to use DefaultAdapter always");
      if (var2 == var1.getSSLAdapter()) {
         System.out.println("Got the same adapter when 'useDefault' enabled.");
      } else {
         System.out.println("!! Did not get same adapter with useDefault");
      }

      setDefaultFactory(new SSLAdapterFactory());
      var1 = getDefaultFactory();
      System.out.println("Re-set the default factory");
      if (var2 != var1.getSSLAdapter()) {
         System.out.println("Got a new adapter from the new factory");
      } else {
         System.out.println("!! Got the old Adapter instead of one from the new factory");
      }

      var2 = var1.getDefaultAdapter();
      if (var2 == null) {
         System.out.println("!! Got null for adapter!");
      }

      System.out.println("Got defaultAdapter: " + var2);
      if (var2 == var1.getDefaultAdapter()) {
         System.out.println("Got the same adapter the second time");
      } else {
         System.out.println("!! Did not get same adapter the second time");
      }

      var1.setUseDefaultAdapter(false);
      System.out.println("Factory set to create new Adapters each time");
      var2 = var1.getDefaultAdapter();
      System.out.println("Got defaultAdapter: " + var2);
      if (var2 != var1.getSSLAdapter()) {
         System.out.println("Did not get same adapter the second time");
      } else {
         System.out.println("!! Got the same adapter the second time");
      }

   }
}
