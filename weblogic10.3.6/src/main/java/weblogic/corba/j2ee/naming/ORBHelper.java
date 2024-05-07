package weblogic.corba.j2ee.naming;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.net.ssl.SSLContext;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.ORBPackage.InvalidName;
import weblogic.corba.client.transaction.TransactionHelperImpl;
import weblogic.corba.orb.ORBHelperImpl;
import weblogic.jndi.WLInitialContextFactory;
import weblogic.kernel.KernelStatus;
import weblogic.transaction.TransactionHelper;

public class ORBHelper {
   public static final String ORB_INITIAL_HOST = "org.omg.CORBA.ORBInitialHost";
   public static final String ORB_INITIAL_PORT = "org.omg.CORBA.ORBInitialPort";
   public static final String ORB_INITIAL_REF = "org.omg.CORBA.ORBInitRef";
   public static final String ORB_DEFAULT_INITIAL_REF = "org.omg.CORBA.ORBDefaultInitRef";
   public static final String ORB_CLASS_PROP = "org.omg.CORBA.ORBClass";
   public static final String ORB_NAMING_PROP = "java.naming.corba.orb";
   public static final String SUN_SOCKET_FACTORY = "com.sun.CORBA.legacy.connection.ORBSocketFactoryClass";
   public static final String SUN_SOCKET_FACTORY_OLD = "com.sun.CORBA.connection.ORBSocketFactoryClass";
   public static final String SUN_LISTEN_SOCKET = "com.sun.CORBA.connection.ORBListenSocket";
   public static final String SUN_WCHARSET = "com.sun.CORBA.codeset.wcharsets";
   public static final String SUN_FRAGMENT = "com.sun.CORBA.giop.ORBGIOP12BuffMgr";
   public static final String ORB_INITIALIZER = "org.omg.PortableInterceptor.ORBInitializerClass.";
   public static final String BI_DIR_ORBINIT = "weblogic.corba.client.iiop.BiDirORBInitializer";
   public static final String CLIENT_ORBINIT = "weblogic.corba.client.ClientORBInitializer";
   public static final String CLIENT_PACKAGE = "weblogic.corba.client.";
   public static final String CLIENT_PACKAGE_14 = "weblogic.corba.client14.";
   public static final String BI_DIR_SOCKET_FACTORY = "iiop.BiDirORBSocketFactory";
   public static final String BI_DIR_SSL_SOCKET_FACTORY = "iiop.BiDirSSLORBSocketFactory";
   public static final String SSL_SOCKET_FACTORY = "security.SSLORBSocketFactory";
   public static final String TUNNEL_SOCKET_FACTORY = "http.TunneledORBSocketFactory";
   public static final String TUNNEL_SSL_SOCKET_FACTORY = "http.TunneledSSLORBSocketFactory";
   public static final String CLUSTER_SOCKET_FACTORY = "cluster.ORBSocketFactory";
   public static final String NATIVE_TX_HELPER = "weblogic.corba.server.transaction.TransactionHelperImpl";
   public static final String ENABLE_SERVER_AFFINITY = "weblogic.jndi.enableServerAffinity";
   public static final String ORB_PROTOCOL = "weblogic.corba.orb.ORBProtocol";
   public static final String ORB_NAME = "weblogic.corba.orb.ORBName";
   public static final String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   public static final String REQUEST_TIMEOUT = "weblogic.jndi.requestTimeout";
   public static final String RMI_TIMEOUT = "weblogic.rmi.clientTimeout";
   public static final String SUN_RPC_TRANSPORT_LOGGER = "javax.enterprise.resource.corba._DEFAULT_.rpc.transport";
   public static final String SUN_TCP_READ_TIMEOUTS_PROPERTY = "com.sun.CORBA.transport.ORBTCPReadTimeouts";
   private static final boolean DEBUG = false;
   private static final String FALSE_PROP = "false";
   private static ORBHelper singleton;
   private static boolean enableIIOPClient = false;
   private static boolean thinClient = false;
   private boolean dummyORBCreated = false;
   private String orbClassName;
   private static final boolean jdk14 = getJDK14();
   private static final boolean enableBiDir = getBiDir();
   private HashMap orbs = new HashMap();
   private HashMap sslCtxCache = new HashMap();
   private ConcurrentHashMap timeoutCache = new ConcurrentHashMap();
   private HashMap urlCache = new HashMap();
   private ORBInfo currentinfo = null;

   private static final boolean getJDK14() {
      Class var0 = String.class;

      try {
         var0.getMethod("replace", CharSequence.class, CharSequence.class);
         return false;
      } catch (NoSuchMethodException var4) {
      } catch (SecurityException var5) {
      }

      try {
         var0.getMethod("replace", String.class, String.class);
         return false;
      } catch (NoSuchMethodException var2) {
      } catch (SecurityException var3) {
      }

      return true;
   }

   private static final boolean getBiDir() {
      try {
         return Boolean.getBoolean("weblogic.corba.client.bidir");
      } catch (Exception var1) {
         return true;
      }
   }

   public static boolean isThinClient() {
      return thinClient;
   }

   public static ORBHelper getORBHelper() {
      if (singleton == null) {
         if (thinClient) {
            new WLInitialContextFactory();
         } else {
            createORBHelper();
         }
      }

      return singleton;
   }

   public static synchronized void createORBHelper() {
      if (singleton == null) {
         String var0 = "";
         if (KernelStatus.isApplet()) {
            var0 = "false";
         } else {
            var0 = System.getProperty("weblogic.system.iiop.enableClient");
         }

         if (!"false".equals(var0)) {
            enableIIOPClient = true;
            singleton = new ORBHelperImpl();
         } else {
            singleton = new ORBHelper();
         }
      }

   }

   public static synchronized void setORBHelper(ORBHelper var0) {
      singleton = var0;
   }

   private static boolean getServerAffinity(Hashtable var0) {
      return var0 == null ? false : Boolean.valueOf((String)var0.get("weblogic.jndi.enableServerAffinity"));
   }

   public synchronized ORB getORB(String var1, Hashtable var2) throws NamingException {
      NameParser.URLInfo var3 = this.parseURL(var1);
      boolean var4 = getServerAffinity(var2);
      ORBInfo var5 = this.getCachedORB(var4 ? var3.getKey() : var3.getNextKey(), var2);
      if (var5 != null) {
         return var5.getORB();
      } else {
         ORB var6 = null;
         if (var1 != null) {
            EndPointInfo var7 = var3.getAddress();
            var6 = this.createORB(var2, var7, var3.getProtocol(), var3.serviceName + "=" + var3.getURL());
            var5 = this.cacheORBAndCreateURL(var3.getKey(), var6, var3);
         } else {
            var6 = this.createORB(var2, (EndPointInfo)null, (String)null, (String)null);
            var5 = this.createORBInfo(var6, (String)null);
            this.setCurrent(var5);
         }

         return var5.getORB();
      }
   }

   private synchronized ORBInfo getCachedORB(String var1, Hashtable var2) {
      ORBInfo var3 = null;
      if (var1 != null) {
         var3 = (ORBInfo)this.orbs.get(var1);
         if (var3 != null) {
            this.setCurrent(var3);
            return var3;
         }
      }

      if (var2 != null && var2.get("java.naming.corba.orb") != null) {
         var3 = this.cacheORB(var1, (ORB)var2.get("java.naming.corba.orb"));
      }

      return var3;
   }

   private ORBInfo cacheORB(String var1, ORB var2) {
      ORBInfo var3 = this.createORBInfo(var2, var1);
      this.orbs.put(var1, var3);
      this.setCurrent(var3);
      return var3;
   }

   private ORBInfo cacheORBAndCreateURL(String var1, ORB var2, NameParser.URLInfo var3) {
      ORBInfo var4 = this.cacheORB(var1, var2);
      var4.setClusterURL(var3.getClusterURL());
      return var4;
   }

   private ORB createORB(Hashtable var1, EndPointInfo var2, String var3, String var4) {
      Properties var5 = new Properties();
      if (var1 != null) {
         var5.putAll(var1);
      }

      String var6 = jdk14 ? "weblogic.corba.client14." : "weblogic.corba.client.";
      if (var5.getProperty("org.omg.CORBA.ORBClass") == null && this.getORBClass() != null) {
         var5.setProperty("org.omg.CORBA.ORBClass", this.getORBClass());
      }

      if (var2 != null) {
         if (var2.getPort() > 0) {
            var5.put("org.omg.CORBA.ORBInitialPort", Integer.toString(var2.getPort()));
         }

         var5.put("weblogic.corba.orb.ORBProtocol", var3);
         var5.put("com.sun.CORBA.giop.ORBGIOP12BuffMgr", "GROW");
         if (var1 != null && var1.containsKey("weblogic.rmi.clientTimeout")) {
            var5.put("com.sun.CORBA.transport.ORBTCPReadTimeouts", "100:" + var1.get("weblogic.rmi.clientTimeout") + ":300:20");
         }

         var5.put("com.sun.CORBA.codeset.wcharsets", "0x05010001,0x00010109");
         if (var3 == "iiops") {
            var5.put("org.omg.PortableInterceptor.ORBInitializerClass.weblogic.corba.client.ClientORBInitializer", "true");
            var5.put("org.omg.PortableInterceptor.ORBInitializerClass.weblogic.corba.client.iiop.BiDirORBInitializer", "true");
            var5.put("com.sun.CORBA.legacy.connection.ORBSocketFactoryClass", var6 + "iiop.BiDirSSLORBSocketFactory");
            var5.put("com.sun.CORBA.connection.ORBSocketFactoryClass", var6 + "iiop.BiDirSSLORBSocketFactory");
         } else if (var3 == "http") {
            var5.put("org.omg.PortableInterceptor.ORBInitializerClass.weblogic.corba.client.ClientORBInitializer", "true");
            var5.put("org.omg.PortableInterceptor.ORBInitializerClass.weblogic.corba.client.iiop.BiDirORBInitializer", "true");
            var5.put("com.sun.CORBA.legacy.connection.ORBSocketFactoryClass", var6 + "http.TunneledORBSocketFactory");
            var5.put("com.sun.CORBA.connection.ORBSocketFactoryClass", var6 + "http.TunneledORBSocketFactory");
         } else if (var3 == "https") {
            var5.put("org.omg.PortableInterceptor.ORBInitializerClass.weblogic.corba.client.ClientORBInitializer", "true");
            var5.put("org.omg.PortableInterceptor.ORBInitializerClass.weblogic.corba.client.iiop.BiDirORBInitializer", "true");
            var5.put("com.sun.CORBA.legacy.connection.ORBSocketFactoryClass", var6 + "http.TunneledSSLORBSocketFactory");
            var5.put("com.sun.CORBA.connection.ORBSocketFactoryClass", var6 + "http.TunneledSSLORBSocketFactory");
         } else if (thinClient) {
            var5.put("org.omg.PortableInterceptor.ORBInitializerClass.weblogic.corba.client.ClientORBInitializer", "true");
            if (enableBiDir && var3.length() > 0) {
               var5.put("org.omg.PortableInterceptor.ORBInitializerClass.weblogic.corba.client.iiop.BiDirORBInitializer", "true");
               var5.put("com.sun.CORBA.legacy.connection.ORBSocketFactoryClass", var6 + "iiop.BiDirORBSocketFactory");
               var5.put("com.sun.CORBA.connection.ORBSocketFactoryClass", var6 + "iiop.BiDirORBSocketFactory");
            } else {
               var5.put("com.sun.CORBA.legacy.connection.ORBSocketFactoryClass", var6 + "cluster.ORBSocketFactory");
               var5.put("com.sun.CORBA.connection.ORBSocketFactoryClass", var6 + "cluster.ORBSocketFactory");
            }
         }

         var5.put("org.omg.CORBA.ORBInitialHost", var2.getHost());
         var5.put("org.omg.CORBA.ORBInitRef", var4);
      }

      ClassLoader var7 = Thread.currentThread().getContextClassLoader();

      ORB var8;
      try {
         Thread.currentThread().setContextClassLoader(ORBHelper.class.getClassLoader());
         var8 = ORB.init(new String[0], var5);
      } finally {
         Thread.currentThread().setContextClassLoader(var7);
      }

      if (var1 != null) {
         Object var9 = var1.get("java.naming.security.credentials");
         if (var9 != null && var9 instanceof SSLContext) {
            this.sslCtxCache.put(var8, var9);
         }

         if (var1.containsKey("weblogic.jndi.requestTimeout")) {
            this.timeoutCache.put(var8, var1.get("weblogic.jndi.requestTimeout"));
         }
      }

      return var8;
   }

   public final SSLContext getSSLContext(ORB var1) {
      return (SSLContext)this.sslCtxCache.get(var1);
   }

   public int getORBTimeout(ORB var1) {
      return this.timeoutCache.containsKey(var1) ? ((Long)this.timeoutCache.get(var1)).intValue() : 0;
   }

   public String getORBClass() {
      return this.orbClassName;
   }

   public ORB getLocalORB() throws NamingException {
      Properties var1 = new Properties();
      if (this.getORBClass() != null) {
         var1.setProperty("org.omg.CORBA.ORBClass", this.getORBClass());
      }

      return ORB.init(new String[0], var1);
   }

   public ORBInfo getCurrent() {
      return this.currentinfo;
   }

   public void setCurrent(ORBInfo var1) {
      this.currentinfo = var1;
   }

   public ORBInfo createORBInfo(ORB var1, String var2) {
      return new ORBInfo(var1, var2);
   }

   public org.omg.CORBA.Object getORBReference(String var1, Hashtable var2, String var3) throws NamingException {
      NameParser.URLInfo var4 = this.parseURL(var1);
      if (var4 == null) {
         throw new InvalidNameException("url `" + var1 + "' is invalid");
      } else {
         boolean var5 = getServerAffinity(var2);
         int var6 = 0;

         while(true) {
            if (var6 < var4.addressList.length) {
               String var7 = var4.getKey();
               ORBInfo var8 = this.getCachedORB(var7, var2);
               if (var8 != null) {
                  try {
                     org.omg.CORBA.Object var9 = var8.getORB().resolve_initial_references(var3);
                     if (thinClient) {
                        var9._non_existent();
                     }

                     if (!var5) {
                        var4.getNextAddress();
                     }

                     var8.setClusterURL(var4.getClusterURL());
                     return var9;
                  } catch (InvalidName var13) {
                     throw Utils.wrapNamingException(var13, "Couldn't resolve initial reference: " + var3);
                  } catch (SystemException var14) {
                     synchronized(this) {
                        this.orbs.remove(var7);
                        this.setCurrent((ORBInfo)null);
                     }

                     if (thinClient) {
                        this.destroyORB(var8.getORB());
                     }

                     this.timeoutCache.remove(var8.getORB());
                     if (!isRecoverableORBFailure(var14)) {
                        throw Utils.wrapNamingException(var14, "Couldn't resolve initial reference: " + var3);
                     }

                     var4.getNextAddress();
                     ++var6;
                     continue;
                  }
               }
            }

            return this.getORBReferenceWithRetry(var4, var2, var3);
         }
      }
   }

   private org.omg.CORBA.Object getORBReferenceWithRetry(NameParser.URLInfo var1, Hashtable var2, String var3) throws NamingException {
      ORB var4 = null;
      org.omg.CORBA.Object var5 = null;
      int var7 = 0;
      boolean var8 = getServerAffinity(var2);
      boolean var9 = false;

      while(var5 == null) {
         String var6 = var1.getKey();
         String var11;
         if (!var9) {
            EndPointInfo var10 = var1.getAddress();
            var11 = thinClient ? var1.getNeutralURL() : var1.getURL();
            if (thinClient && !this.dummyORBCreated) {
               this.createDummyORB(var2, var10);
            }

            var4 = this.createORB(var2, var10, var1.getProtocol(), var1.serviceName + "=" + var11);
         }

         try {
            var5 = var4.resolve_initial_references(var3);
            if (thinClient) {
               var5._non_existent();
            }

            if (!var8) {
               var1.getNextAddress();
            }

            synchronized(this) {
               if (this.getCachedORB(var6, var2) == null) {
                  this.cacheORBAndCreateURL(var6, var4, var1);
               }
            }
         } catch (InvalidName var14) {
            throw Utils.wrapNamingException(var14, "Couldn't resolve initial reference: " + var3);
         } catch (SystemException var15) {
            var5 = null;
            if (thinClient) {
               this.destroyORB(var4);
            }

            this.timeoutCache.remove(var4);
            if (!var9) {
               if (isRecoverableORBFailure(var15)) {
                  ++var7;
                  if (var7 < var1.addressList.length) {
                     var1.getNextAddress();
                     ORBInfo var16 = this.getCachedORB(var1.getKey(), var2);
                     if (var16 != null) {
                        var4 = var16.getORB();
                        var9 = true;
                     }
                     continue;
                  }
               }

               if (var1.addressList.length == 1) {
                  var11 = this.getRootCauseMessage(var15);
               } else {
                  var11 = "Couldn't connect to any host ";
               }

               throw Utils.wrapNamingException(var15, var11);
            } else {
               this.clearORBFromCache(var1.getKey());
               var9 = false;
               if (!isRecoverableORBFailure(var15)) {
                  throw Utils.wrapNamingException(var15, "Couldn't resolve initial reference: " + var3);
               }
            }
         }
      }

      return var5;
   }

   public void clearORBFromCache(String var1) {
      synchronized(this) {
         if (var1 != null) {
            this.orbs.remove(var1);
         }

         this.setCurrent((ORBInfo)null);
      }
   }

   private final synchronized NameParser.URLInfo parseURL(String var1) throws InvalidNameException {
      NameParser.URLInfo var2 = (NameParser.URLInfo)this.urlCache.get(var1);
      if (var2 == null) {
         var2 = NameParser.parseURL(var1);
         this.urlCache.put(var1, var2);
      }

      return var2;
   }

   public void pushTransactionHelper() {
      if (enableIIOPClient) {
         TransactionHelper.pushTransactionHelper(createTxHelper("weblogic.corba.server.transaction.TransactionHelperImpl"));
      } else {
         TransactionHelper.pushTransactionHelper(new TransactionHelperImpl());
      }

   }

   public void popTransactionHelper() {
      TransactionHelper.popTransactionHelper();
   }

   static boolean isRecoverableORBFailure(SystemException var0) {
      return isRecoverableORBFailure(var0, true);
   }

   public static boolean isRecoverableORBFailure(SystemException var0, boolean var1) {
      return (var0 instanceof COMM_FAILURE || var0 instanceof MARSHAL || var0 instanceof OBJECT_NOT_EXIST || var0 instanceof BAD_PARAM && var0.minor == 1330446344) && (var0.completed.value() == 1 || var0.completed.value() == 2 && var1);
   }

   private static TransactionHelper createTxHelper(String var0) {
      try {
         return (TransactionHelper)Class.forName(var0).newInstance();
      } catch (ClassNotFoundException var2) {
         throw new Error(var2.toString());
      } catch (InstantiationException var3) {
         throw new Error(var3.toString());
      } catch (IllegalAccessException var4) {
         throw new Error(var4.toString());
      }
   }

   protected static void p(String var0) {
      System.err.println("<ORBHelper> " + var0);
   }

   private void createDummyORB(Hashtable var1, EndPointInfo var2) {
      ORB var3 = null;
      EndPointInfo var4 = new EndPointInfo(" ", -1, var2.getMajorVersion(), var2.getMajorVersion());
      String var5 = "NameService=corbaloc::1.1@/NameService";
      synchronized(this) {
         if (!this.dummyORBCreated) {
            try {
               var3 = this.createORB((Hashtable)null, var4, "", var5);
               this.setORBLogLevel();
               org.omg.CORBA.Object var7 = var3.resolve_initial_references(var5);
               var7._non_existent();
            } catch (Throwable var13) {
            } finally {
               this.orbClassName = var3.getClass().getName();
               this.dummyORBCreated = true;
            }
         }

      }
   }

   public void destroyORB(ORB var1) {
      try {
         Object var2 = var1.getClass().getMethod("getTransportManager").invoke(var1);

         try {
            Object var3 = var2.getClass().getMethod("getSelector", Integer.TYPE).invoke(var2, 0);
            var3.getClass().getMethod("close").invoke(var3);
         } catch (Exception var8) {
         }

         Collection var10 = (Collection)var2.getClass().getMethod("getAcceptors").invoke(var2);
         Iterator var4 = var10.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();

            try {
               var5.getClass().getMethod("close").invoke(var5);
            } catch (Exception var7) {
            }
         }

         var1.destroy();
      } catch (Exception var9) {
      }

   }

   private void setORBLogLevel() {
      String var1 = System.getProperty("weblogic.corba.client.ORBLogLevel");
      if (var1 != null) {
         Logger var2 = LogManager.getLogManager().getLogger("javax.enterprise.resource.corba._DEFAULT_.rpc.transport");
         if (var2 != null) {
            Level var3 = null;

            try {
               var3 = Level.parse(var1);
            } catch (Exception var5) {
            }

            if (var3 != null) {
               var2.setLevel(var3);
            }
         }

      }
   }

   private String getRootCauseMessage(Throwable var1) {
      String var2 = "Couldn't connect to the specified host";
      Throwable var3 = var1.getCause();
      return var3 == null ? var2 : var2 + " : " + var3.getMessage();
   }

   static {
      try {
         String var0 = jdk14 ? "weblogic.corba.client14." : "weblogic.corba.client.";
         Class.forName(var0 + "cluster.ORBSocketFactory");
         Class.forName("weblogic.jndi.WLInitialContextFactory");
         if (!KernelStatus.isServer() && KernelStatus.isThinIIOPClient()) {
            thinClient = true;
         } else {
            Class.forName("org.osgi.framework.Bundle");
            thinClient = true;
         }
      } catch (ClassNotFoundException var1) {
      } catch (NoClassDefFoundError var2) {
      }

   }
}
