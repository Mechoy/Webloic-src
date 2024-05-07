package weblogic.management.commandline.tools;

import java.io.File;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeOperationsException;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.common.T3User;
import weblogic.jndi.Environment;
import weblogic.kernel.Kernel;
import weblogic.management.MBeanHome;
import weblogic.management.commandline.CommandLineArgs;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;
import weblogic.security.internal.BootProperties;
import weblogic.security.internal.SerializedSystemIni;

public class AdminToolHelper {
   private static final boolean debug = false;
   public static boolean printDone = false;
   public static boolean shutdownCommand = false;
   static CommandLineArgs params = null;
   static PrintStream printStream;
   private static boolean CONTINUE;
   private boolean EXIT = false;

   public AdminToolHelper(CommandLineArgs var1) throws Exception {
      params = var1;
   }

   public void evaluateCredentials() {
      if (params.getOperation() != 41) {
         if (params.getUsername() == null || params.getPassword() == null) {
            UsernameAndPassword var1 = null;
            if (params.getUserConfig() == null && params.getUserKey() == null) {
               var1 = UserConfigFileManager.getUsernameAndPassword("weblogic.management");
            } else {
               var1 = UserConfigFileManager.getUsernameAndPassword(params.getUserConfig(), params.getUserKey(), "weblogic.management");
            }

            if (var1 != null && var1.isUsernameSet() && var1.isPasswordSet()) {
               params.setUsername(var1.getUsername());
               params.setPassword(new String(var1.getPassword()));
            }

            String var2 = System.getProperty("weblogic.system.BootIdentityFile", "boot.properties");
            File var3 = new File(var2);
            if (!var3.exists()) {
               String var4 = params.getServerName();
               if (var4 == null) {
                  return;
               }

               var3 = new File("./servers/" + var4 + "/boot.properties");
            }

            if (var3.exists() && SerializedSystemIni.exists()) {
               BootProperties.load(var2, false);
               BootProperties var6 = BootProperties.getBootProperties();
               if (var6.getOneClient().length() == 0) {
                  ManagementTextTextFormatter var5 = new ManagementTextTextFormatter();
                  printStream.println(var5.getNoUserNameNoPassword());
                  printStream.println(CommandLineArgs.getUsageString());
                  return;
               }

               params.setUsername(var6.getOneClient());
               params.setPassword(var6.getTwoClient());
               params.setUsingBootProperties(true);
               BootProperties.unload(false);
            }

         }
      }
   }

   public static void printErrorMessage(String var0, boolean var1) {
      if (null != var0) {
         System.err.println("\n" + var0);
      }

      if (var1) {
         ;
      }
   }

   public static void printException(String var0, Exception var1, boolean var2) {
      if (var2) {
         printStream.println((null == var0 ? "" : var0) + var1.getClass().getName() + ": " + var1);
      } else {
         String var3 = var1.getMessage();
         if (var3 != null) {
            int var4 = var3.indexOf("Start server side stack trace:");
            if (var4 != -1) {
               var3 = var3.substring(0, var4);
            }
         }

         if (null == var0) {
            printErrorMessage(var3, CONTINUE);
         } else {
            printErrorMessage(var0 + " : " + var3, CONTINUE);
         }
      }

      ManagementTextTextFormatter var6;
      if (var1 instanceof RuntimeErrorException) {
         RuntimeErrorException var5 = (RuntimeErrorException)var1;
         var6 = new ManagementTextTextFormatter();
         printStream.println(var6.getPrintExceptionErr() + var5.getTargetError());
      } else if (var1 instanceof RuntimeOperationsException) {
         RuntimeOperationsException var7 = (RuntimeOperationsException)var1;
         var6 = new ManagementTextTextFormatter();
         printStream.println(var6.getPrintExceptionExp() + var7.getTargetException());
      }

      if (var2) {
         var1.printStackTrace();
      }

   }

   public static void printException(String var0, Exception var1) {
      printException(var0, var1, false);
   }

   public static void printException(Exception var0, boolean var1) {
      printException((String)null, var0, var1);
   }

   public static void printException(Exception var0) {
      printException((String)null, var0);
   }

   public int nextArg(int var1, int var2) {
      try {
         return Integer.parseInt(this.nextArg("" + var1, var2));
      } catch (NumberFormatException var4) {
         return var1;
      }
   }

   public String nextArg(String var1, int var2) {
      try {
         return params.getPositionalArg(var2);
      } catch (ArrayIndexOutOfBoundsException var4) {
         return var1;
      }
   }

   public boolean nextArg(boolean var1, int var2) {
      String var3 = this.nextArg("" + var1, var2);
      if (var3.equalsIgnoreCase("true")) {
         return true;
      } else if (var3.equalsIgnoreCase("1")) {
         return true;
      } else {
         return var3.equalsIgnoreCase("yes");
      }
   }

   public static Context getInitialContext(String var0, T3User var1) throws NamingException {
      Environment var2 = new Environment();
      var2.setProviderUrl(var0);
      var2.setSecurityCredentials(var1);
      return var2.getInitialContext();
   }

   public static Context getInitialContext(String var0, String var1, String var2) throws Exception {
      Object var3 = null;

      try {
         Environment var4 = new Environment();
         var4.setProviderUrl(var0);
         var4.setSecurityPrincipal(var1);
         var4.setSecurityCredentials(var2);
         return var4.getInitialContext();
      } catch (Exception var7) {
         if (var7 instanceof CommunicationException) {
            printDone = true;
            CommunicationException var5 = (CommunicationException)var7;
            ManagementTextTextFormatter var6 = new ManagementTextTextFormatter();
            printErrorMessage(var6.getFailedConnect(var0) + " [" + var5.getRootCause().getMessage() + "]", CONTINUE);
         }

         throw var7;
      }
   }

   public static Context getInitialContext(CommandLineArgs var0) throws Exception {
      Context var1 = null;
      Environment var2 = new Environment();
      String var3 = var0.getURL();

      try {
         if (var0.isIIOP()) {
            if (System.getProperty("weblogic.system.iiop.enableClient") == null) {
               System.setProperty("weblogic.system.iiop.enableClient", "false");
            }

            Hashtable var9 = new Hashtable();
            var9.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
            var9.put("java.naming.provider.url", var3);
            var9.put("java.naming.security.principal", var0.getUsername());
            var9.put("java.naming.security.credentials", var0.getPassword());
            long var10 = getTimeout(var0);
            if (var10 > 0L) {
               var9.put("weblogic.jndi.requestTimeout", var10);
               var9.put("weblogic.rmi.clientTimeout", var10);
            }

            InitialContext var8 = new InitialContext(var9);
            return var8;
         } else {
            if (!Kernel.isServer()) {
               var2.setProviderUrl(var3);
               var2.setSecurityPrincipal(var0.getUsername());
               var2.setSecurityCredentials(var0.getPassword());
               long var4 = getTimeout(var0);
               if (var4 > 0L) {
                  var2.setRequestTimeout(var4);
                  var2.setRMIClientTimeout(var4);
               }
            }

            var1 = var2.getInitialContext();
            return var1;
         }
      } catch (Exception var7) {
         if (var7 instanceof CommunicationException) {
            printDone = true;
            CommunicationException var5 = (CommunicationException)var7;
            ManagementTextTextFormatter var6 = new ManagementTextTextFormatter();
            printErrorMessage(var6.getFailedConnect(var3) + " [" + var5.getRootCause().getMessage() + "]", CONTINUE);
         }

         throw var7;
      }
   }

   public static MBeanHome getMBeanHome(String var0, String var1, String var2, boolean var3) throws Exception {
      MBeanHome var4 = null;

      ManagementTextTextFormatter var6;
      Throwable var12;
      try {
         Environment var5 = new Environment();
         var6 = null;
         Object var13;
         if (params.isIIOP()) {
            if (System.getProperty("weblogic.system.iiop.enableClient") == null) {
               System.setProperty("weblogic.system.iiop.enableClient", "false");
            }

            Hashtable var14 = new Hashtable();
            var14.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
            var14.put("java.naming.provider.url", var0);
            var14.put("java.naming.security.principal", var1);
            var14.put("java.naming.security.credentials", var2);
            var13 = new InitialContext(var14);
         } else {
            if (!Kernel.isServer()) {
               var5.setProviderUrl(var0);
               var5.setSecurityPrincipal(var1);
               var5.setSecurityCredentials(var2);
            }

            var13 = var5.getInitialContext();
         }

         if (var3) {
            var4 = (MBeanHome)((Context)var13).lookup("weblogic.management.home.localhome");
         } else if (params.getAdminUrl() != null) {
            var4 = (MBeanHome)((Context)var13).lookup("weblogic.management.adminhome");
         } else {
            var4 = (MBeanHome)((Context)var13).lookup("weblogic.management.home.localhome");
         }

         return var4;
      } catch (AuthenticationException var9) {
         var12 = var9.getRootCause();
         if (var12 != null) {
            throw (Exception)var12;
         } else {
            throw var9;
         }
      } catch (CommunicationException var10) {
         var12 = var10.getRootCause();
         if (var12 != null) {
            ManagementTextTextFormatter var7 = new ManagementTextTextFormatter();
            String var8 = var12.getMessage();
            if (var8 != null && var8.length() > 0) {
               System.out.println("\n" + var7.getFailtedToConnect2(var8));
               printDone = true;
            }

            throw (Exception)var12;
         } else {
            throw var10;
         }
      } catch (NamingException var11) {
         var6 = new ManagementTextTextFormatter();
         throw new IllegalArgumentException(var6.getJndiException() + var11);
      }
   }

   public static MBeanHome getLocalMBeanHome(CommandLineArgs var0) throws Exception {
      MBeanHome var1 = null;

      ManagementTextTextFormatter var3;
      String var5;
      Throwable var13;
      try {
         Environment var2 = new Environment();
         var3 = null;
         String var14 = var0.getURL();
         var5 = var0.getUsername();
         String var6 = var0.getPassword();
         Object var15;
         if (var0.isIIOP()) {
            if (System.getProperty("weblogic.system.iiop.enableClient") == null) {
               System.setProperty("weblogic.system.iiop.enableClient", "false");
            }

            Hashtable var7 = new Hashtable();
            var7.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
            var7.put("java.naming.provider.url", var14);
            var7.put("java.naming.security.principal", var5);
            var7.put("java.naming.security.credentials", var6);
            long var8 = getTimeout(var0);
            if (var8 > 0L) {
               var7.put("weblogic.jndi.requestTimeout", var8);
               var7.put("weblogic.rmi.clientTimeout", var8);
            }

            var15 = new InitialContext(var7);
         } else {
            if (!Kernel.isServer()) {
               var2.setProviderUrl(var14);
               var2.setSecurityPrincipal(var5);
               var2.setSecurityCredentials(var6);
               long var16 = getTimeout(var0);
               if (var16 > 0L) {
                  var2.setRequestTimeout(var16);
                  var2.setRMIClientTimeout(var16);
               }
            }

            var15 = var2.getInitialContext();
         }

         var1 = (MBeanHome)((Context)var15).lookup("weblogic.management.home.localhome");
         return var1;
      } catch (AuthenticationException var10) {
         var13 = var10.getRootCause();
         if (var13 != null) {
            throw (Exception)var13;
         } else {
            throw var10;
         }
      } catch (CommunicationException var11) {
         var13 = var11.getRootCause();
         if (var13 != null) {
            ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
            var5 = var13.getMessage();
            if (var5 != null && var5.length() > 0) {
               System.out.println("\n" + var4.getFailtedToConnect2(var5));
               printDone = true;
            }

            throw (Exception)var13;
         } else {
            throw var11;
         }
      } catch (NamingException var12) {
         var3 = new ManagementTextTextFormatter();
         throw new IllegalArgumentException(var3.getJndiException() + var12);
      }
   }

   public static MBeanHome getMBeanHome(CommandLineArgs var0) throws Exception {
      MBeanHome var1 = null;

      Throwable var3;
      ManagementTextTextFormatter var4;
      try {
         Environment var2 = new Environment();
         var3 = null;
         String var13 = var0.getURL();
         Object var12;
         if (var0.isIIOP()) {
            if (System.getProperty("weblogic.system.iiop.enableClient") == null) {
               System.setProperty("weblogic.system.iiop.enableClient", "false");
            }

            Hashtable var14 = new Hashtable();
            var14.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
            var14.put("java.naming.provider.url", var13);
            var14.put("java.naming.security.principal", var0.getUsername());
            var14.put("java.naming.security.credentials", var0.getPassword());
            long var6 = getTimeout(var0);
            if (var6 > 0L) {
               var14.put("weblogic.jndi.requestTimeout", var6);
               var14.put("weblogic.rmi.clientTimeout", var6);
            }

            var12 = new InitialContext(var14);
         } else {
            if (!Kernel.isServer()) {
               var2.setProviderUrl(var13);
               var2.setSecurityPrincipal(var0.getUsername());
               var2.setSecurityCredentials(var0.getPassword());
               long var15 = getTimeout(var0);
               if (var15 > 0L) {
                  var2.setRequestTimeout(var15);
                  var2.setRMIClientTimeout(var15);
               }
            }

            var12 = var2.getInitialContext();
         }

         var1 = (MBeanHome)((Context)var12).lookup("weblogic.management.adminhome");
         return var1;
      } catch (AuthenticationException var8) {
         var3 = var8.getRootCause();
         if (var3 != null) {
            throw (Exception)var3;
         } else {
            throw var8;
         }
      } catch (CommunicationException var9) {
         var3 = var9.getRootCause();
         if (var3 != null) {
            var4 = new ManagementTextTextFormatter();
            String var5 = var3.getMessage();
            if (var5 != null && var5.length() > 0) {
               System.out.println("\n" + var4.getFailtedToConnect2(var5));
               printDone = true;
            }

            throw (Exception)var3;
         } else {
            throw var9;
         }
      } catch (NamingException var10) {
         var3 = var10.getCause();
         if (var3 != null && var3 instanceof RemoteException) {
            RemoteException var11 = (RemoteException)var3;
            new ManagementTextTextFormatter();
            System.out.println("\nFailed to connect: " + var3.getMessage());
            printDone = true;
            throw var10;
         } else {
            var4 = new ManagementTextTextFormatter();
            throw new IllegalArgumentException(var4.getJndiException() + var10);
         }
      }
   }

   public static MBeanHome getAdminMBeanHome(CommandLineArgs var0) throws Exception {
      MBeanHome var1 = null;

      ManagementTextTextFormatter var3;
      String var5;
      Throwable var12;
      try {
         Environment var2 = new Environment();
         var3 = null;
         String var13 = var0.getAdminUrl();
         var5 = null;
         if (var13 != null) {
            var5 = var13;
         } else {
            var5 = var0.getURL();
         }

         Object var14;
         if (var0.isIIOP()) {
            if (System.getProperty("weblogic.system.iiop.enableClient") == null) {
               System.setProperty("weblogic.system.iiop.enableClient", "false");
            }

            Hashtable var6 = new Hashtable();
            var6.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
            var6.put("java.naming.provider.url", var5);
            var6.put("java.naming.security.principal", var0.getUsername());
            var6.put("java.naming.security.credentials", var0.getPassword());
            long var7 = getTimeout(var0);
            if (var7 > 0L) {
               var6.put("weblogic.jndi.requestTimeout", var7);
               var6.put("weblogic.rmi.clientTimeout", var7);
            }

            var14 = new InitialContext(var6);
         } else {
            if (!Kernel.isServer()) {
               var2.setProviderUrl(var5);
               var2.setSecurityPrincipal(var0.getUsername());
               var2.setSecurityCredentials(var0.getPassword());
               long var15 = getTimeout(var0);
               if (var15 > 0L) {
                  var2.setRequestTimeout(var15);
                  var2.setRMIClientTimeout(var15);
               }
            }

            var14 = var2.getInitialContext();
         }

         var1 = (MBeanHome)((Context)var14).lookup("weblogic.management.home.localhome");
         Iterator var16 = var1.getMBeansByType("ServerRuntime").iterator();
         ServerRuntimeMBean var17 = (ServerRuntimeMBean)var16.next();
         if (!var17.isAdminServer()) {
            ManagementTextTextFormatter var8 = new ManagementTextTextFormatter();
            System.out.println("\n" + var8.getInvalidadminurl(var5));
            throw new Exception();
         } else {
            var1 = (MBeanHome)((Context)var14).lookup("weblogic.management.adminhome");
            return var1;
         }
      } catch (AuthenticationException var9) {
         var12 = var9.getRootCause();
         if (var12 != null) {
            throw (Exception)var12;
         } else {
            throw var9;
         }
      } catch (CommunicationException var10) {
         var12 = var10.getRootCause();
         if (var12 != null) {
            ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
            var5 = var12.getMessage();
            if (var5 != null && var5.length() > 0) {
               System.out.println("\n" + var4.getFailtedToConnect2(var5));
               printDone = true;
            }

            throw (Exception)var12;
         } else {
            throw var10;
         }
      } catch (NamingException var11) {
         var3 = new ManagementTextTextFormatter();
         throw new IllegalArgumentException(var3.getJndiException() + var11);
      }
   }

   public static MBeanHome getAdminMBeanHome(String var0, String var1, String var2) throws Exception {
      MBeanHome var3 = null;

      Throwable var12;
      try {
         Environment var4 = new Environment();
         if (!Kernel.isServer()) {
            var4.setProviderUrl(var0);
            var4.setSecurityPrincipal(var1);
            var4.setSecurityCredentials(var2);
         }

         Context var13 = var4.getInitialContext();
         var3 = (MBeanHome)var13.lookup("weblogic.management.home.localhome");
         Iterator var14 = var3.getMBeansByType("ServerRuntime").iterator();
         ServerRuntimeMBean var15 = (ServerRuntimeMBean)var14.next();
         if (!var15.isAdminServer()) {
            ManagementTextTextFormatter var8 = new ManagementTextTextFormatter();
            System.out.println("\n" + var8.getInvalidadminurl(var0));
            throw new Exception();
         } else {
            var3 = (MBeanHome)var13.lookup("weblogic.management.adminhome");
            return var3;
         }
      } catch (AuthenticationException var9) {
         var12 = var9.getRootCause();
         if (var12 != null) {
            throw (Exception)var12;
         } else {
            throw var9;
         }
      } catch (CommunicationException var10) {
         var12 = var10.getRootCause();
         if (var12 != null) {
            ManagementTextTextFormatter var6 = new ManagementTextTextFormatter();
            String var7 = var12.getMessage();
            if (var7 != null && var7.length() > 0) {
               System.out.println("\n" + var6.getFailtedToConnect2(var7));
               printDone = true;
            }

            throw (Exception)var12;
         } else {
            throw var10;
         }
      } catch (NamingException var11) {
         ManagementTextTextFormatter var5 = new ManagementTextTextFormatter();
         throw new IllegalArgumentException(var5.getJndiException() + var11);
      }
   }

   public boolean isNextArgInt(int var1) {
      try {
         int var2 = Integer.parseInt(this.nextArg("", var1));
         return true;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   private static long getTimeout(CommandLineArgs var0) {
      long var1 = 0L;

      try {
         switch (var0.getOperation()) {
            case 2:
               if (var0.isTimeoutSet()) {
                  var1 = (long)var0.getTimeout();
               } else {
                  var1 = 30L;
               }
         }
      } catch (Exception var4) {
      }

      return var1 * 1000L;
   }

   static {
      printStream = System.out;
      CONTINUE = true;
   }
}
