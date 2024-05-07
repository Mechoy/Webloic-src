package weblogic.management.commandline.tools;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.util.HashSet;
import java.util.Iterator;
import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.RuntimeOperationsException;
import javax.naming.Context;
import weblogic.common.T3Client;
import weblogic.common.T3ExecuteException;
import weblogic.common.T3User;
import weblogic.management.MBeanHome;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commandline.CommandLineArgs;
import weblogic.management.commandline.OutputFormatter;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleTaskRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.server.ServerLifecycleException;

public final class ServerAdminCommandLineInvoker {
   static final String OK_STRING = "Ok";
   static CommandLineArgs params = null;
   static PrintWriter printLog = null;
   static T3Client t3 = null;
   static AdminToolHelper toolHelper = null;
   MBeanHome adminHome;
   Context ctx;
   OutputFormatter out;
   private static boolean CONTINUE = true;
   private static PrintStream printStream;
   private boolean EXIT;

   public ServerAdminCommandLineInvoker(CommandLineArgs var1, PrintStream var2) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;
      this.EXIT = false;

      try {
         params = var1;
         if (var2 != null) {
            printStream = var2;
         }

         toolHelper = new AdminToolHelper(var1);
         this.doCommandline();
      } catch (Exception var4) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            AdminToolHelper.printException(var4);
         }

         throw var4;
      }
   }

   public ServerAdminCommandLineInvoker(String[] var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;
      this.EXIT = false;
      params = new CommandLineArgs(var1);
      if (var2 != null) {
         printStream = var2;
      }

      this.adminHome = var3;
      toolHelper = new AdminToolHelper(params);
      this.doCommandline();
   }

   public ServerAdminCommandLineInvoker(CommandLineArgs var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;
      this.EXIT = false;
      params = var1;
      if (var2 != null) {
         printStream = var2;
      }

      this.adminHome = var3;
      toolHelper = new AdminToolHelper(params);
      this.doCommandline();
   }

   public ServerAdminCommandLineInvoker(String[] var1, PrintStream var2) throws Exception {
      this(new CommandLineArgs(var1), var2);
   }

   public static void main(String[] var0) throws Exception {
      new ServerAdminCommandLineInvoker(var0, System.out);
   }

   static void connect(String var0, T3User var1) throws IOException, T3ExecuteException {
      ManagementTextTextFormatter var3;
      try {
         t3 = new T3Client(var0, var1);
         t3.connect();
      } catch (UnknownHostException var4) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            var3 = new ManagementTextTextFormatter();
            AdminToolHelper.printErrorMessage(var3.getUnknownHost(var0), CONTINUE);
         }

         throw var4;
      } catch (ConnectException var5) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            var3 = new ManagementTextTextFormatter();
            AdminToolHelper.printErrorMessage(var3.getConnectFailedError(var0), CONTINUE);
         }

         throw var5;
      } catch (T3ExecuteException var6) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            var3 = new ManagementTextTextFormatter();
            AdminToolHelper.printErrorMessage(var3.getFailedConnect(var0) + "[" + var6.getMessage() + "]", CONTINUE);
         }

         throw var6;
      } catch (IOException var7) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            var3 = new ManagementTextTextFormatter();
            AdminToolHelper.printErrorMessage(var3.getFailedConnect(var0) + " [" + var7.getMessage() + "]", CONTINUE);
         }

         throw var7;
      } catch (SecurityException var8) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            var3 = new ManagementTextTextFormatter();
            AdminToolHelper.printErrorMessage(var3.getGuestDisabledException() + "\n", CONTINUE);
         }

         throw var8;
      }
   }

   static void executeShutdownCommand(CommandLineArgs var0) throws Exception {
      if (var0.getOperation() == 26 && toolHelper.isNextArgInt(0)) {
         executeShutdownCommandOldSyntax(var0);
      }

      AdminToolHelper.shutdownCommand = true;
      Context var2 = null;
      Object var3 = null;
      MBeanHome var4 = null;
      MBeanHome var5 = null;
      boolean var6 = false;
      ServerMBean var7 = null;
      ServerMBean var8 = null;
      Object var9 = null;
      Object var10 = null;
      ServerRuntimeMBean var11 = null;
      Object var12 = null;
      ServerLifeCycleRuntimeMBean var13 = null;
      ServerLifeCycleTaskRuntimeMBean var14 = null;
      boolean var15 = false;
      String var17 = null;
      boolean var18 = false;
      String var20 = toolHelper.nextArg("", 0);

      Iterator var1;
      WebLogicObjectName var16;
      ManagementTextTextFormatter var19;
      try {
         try {
            var2 = getInitialContext(var0);
         } catch (Exception var29) {
            var19 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var19.getFailedConnect(var0.getURL()) + "[" + var29 + "]");
            }

            throw var29;
         }

         var4 = (MBeanHome)var2.lookup("weblogic.management.home.localhome");
         var1 = var4.getMBeansByType("ServerRuntime").iterator();
         var11 = (ServerRuntimeMBean)var1.next();
         var17 = var11.getName();
         var16 = new WebLogicObjectName(var17, "ServerConfig", var4.getDomainName(), var17);
         var8 = (ServerMBean)var4.getMBean(var16);
      } catch (Exception var30) {
         var19 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var19.getExceptionMsg(var30.getMessage()));
         }

         throw var30;
      }

      try {
         if (!var20.equals("") && !var20.equals("-ignoreExistingSessions") && !var20.equals("-timeout")) {
            var15 = false;
         } else {
            var15 = true;
         }

         if (var11.isAdminServer()) {
            var18 = true;
            var5 = var4;
         }

         if (!var18 && !var15 && !var20.equals(var17)) {
            var19 = new ManagementTextTextFormatter();
            String var37 = var19.getShuttingViaNonAdminNotAllowed();
            System.err.println(var37);
            AdminToolHelper.printDone = true;
            throw new Exception(var37);
         } else if (!var15 && !var20.equals(var17)) {
            if (!var18) {
               int var22 = var0.getURL().indexOf(58);
               String var23 = var0.getURL().substring(0, var22);
               String var24 = var11.getAdminServerHost();
               int var25 = var11.getAdminServerListenPort();

               try {
                  var2 = getInitialContext(var23 + "://" + var24 + ":" + var25, var0.getUsername(), var0.getPassword());
               } catch (Exception var33) {
                  var19 = new ManagementTextTextFormatter();
                  if (!AdminToolHelper.printDone) {
                     System.err.println(var19.getShuttingdownServerFailed(var17));
                     AdminToolHelper.printDone = true;
                  }

                  throw var33;
               }

               var5 = (MBeanHome)var2.lookup("weblogic.management.home.localhome");
               var1 = var5.getMBeansByType("ServerRuntime").iterator();
               ServerRuntimeMBean var21 = (ServerRuntimeMBean)var1.next();
               var5 = (MBeanHome)var2.lookup("weblogic.management.adminhome");
               var16 = new WebLogicObjectName(var17, "Server", var5.getDomainName());

               try {
                  var7 = (ServerMBean)var5.getMBean(var16);
                  var20 = var17;
               } catch (InstanceNotFoundException var28) {
                  var19 = new ManagementTextTextFormatter();
                  System.err.println(var19.getServerNotConfigured(var17, var5.getDomainName()));
                  AdminToolHelper.printDone = true;
                  throw var28;
               }
            } else {
               try {
                  var5 = (MBeanHome)var2.lookup("weblogic.management.adminhome");
                  if (var15) {
                     var7 = var8;
                  } else {
                     var16 = new WebLogicObjectName(var20, "Server", var5.getDomainName());
                     var7 = (ServerMBean)var5.getMBean(var16);
                  }
               } catch (InstanceNotFoundException var27) {
                  var19 = new ManagementTextTextFormatter();
                  System.err.println(var19.getServerNotConfigured(var20, var5.getDomainName()));
                  AdminToolHelper.printDone = true;
                  throw var27;
               }
            }

            var13 = (ServerLifeCycleRuntimeMBean)var5.getMBean(var7.getName(), "ServerLifeCycleRuntime", var5.getDomainName());
            if (var0.getOperation() == 26) {
               var19 = new ManagementTextTextFormatter();
               System.out.println("\n" + var19.getShutdownSuccess(var20));
               var14 = var13.shutdown(var0.getTimeout(), var0.getIgnoreSessions());
            } else {
               var14 = var13.forceShutdown();
            }

            if (var14 != null && var14.isRunning()) {
               do {
                  Thread.sleep(1000L);
               } while(var14.isRunning());
            }

            printLog = new PrintWriter(System.out);
            if (var14.getStatus().equals("TASK COMPLETED")) {
               if (var0.getOperation() == 26) {
                  var19 = new ManagementTextTextFormatter();
                  System.out.println("\n" + var19.getServerShutdownSuccessfully(var20));
               } else {
                  var19 = new ManagementTextTextFormatter();
                  System.out.println(var19.getServerForceShutdownSuccessfully(var20));
               }

            } else {
               String var38 = "";
               if (var14.getError() != null) {
                  var19 = new ManagementTextTextFormatter();
                  var38 = var19.getExceptionMsg(var14.getError().getMessage());
               }

               if (var0.getOperation() == 26) {
                  var19 = new ManagementTextTextFormatter();
                  System.err.println(var19.getShuttingdownServerFailed(var20));
                  AdminToolHelper.printDone = true;
                  System.err.println(var38);
                  throw new Exception();
               } else {
                  var19 = new ManagementTextTextFormatter();
                  System.err.println(var19.getForceShuttingdownServerFailed(var20));
                  System.err.println(var38);
                  AdminToolHelper.printDone = true;
                  throw new Exception();
               }
            }
         } else if (var0.getOperation() == 26) {
            try {
               var19 = new ManagementTextTextFormatter();
               System.out.println("\n" + var19.getShutdownSuccess(var17));
               System.out.flush();
               var11.shutdown(var0.getTimeout(), var0.getIgnoreSessions());
               System.out.println("\n" + var19.getServerShutdownSuccessfully(var17));
            } catch (RemoteRuntimeException var31) {
               var19 = new ManagementTextTextFormatter();
               System.out.println("\n" + var19.getServerShutdownSuccessfully(var17));
               System.out.flush();
            } catch (ServerLifecycleException var32) {
               var19 = new ManagementTextTextFormatter();
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  System.err.println(var19.getShuttingdownServerFailed(var17));
                  System.out.flush();
               }

               throw var32;
            }
         } else {
            try {
               var11.forceShutdown();
               var19 = new ManagementTextTextFormatter();
               System.out.println(var19.getServerForceShutdownSuccessfully(var17));
            } catch (RemoteRuntimeException var34) {
               var19 = new ManagementTextTextFormatter();
               System.out.println(var19.getServerForceShutdownSuccessfully(var17));
            } catch (ServerLifecycleException var35) {
               var19 = new ManagementTextTextFormatter();
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  System.err.println(var19.getShuttingdownServerFailed(var17));
                  System.out.flush();
               }

               throw var35;
            }
         }
      } catch (Exception var36) {
         if (!AdminToolHelper.printDone) {
            var19 = new ManagementTextTextFormatter();
            System.err.println(var19.getExceptionMsg(var36.getMessage()));
            AdminToolHelper.printDone = true;
         }

         throw var36;
      }
   }

   static void executeShutdownCommandOldSyntax(CommandLineArgs var0) throws Exception {
      AdminToolHelper.shutdownCommand = true;
      String var1 = "";
      String var2 = "";
      int var3 = toolHelper.nextArg(-1, 0);
      var1 = toolHelper.nextArg("", 1);

      ManagementTextTextFormatter var5;
      try {
         connect(var0.getURL(), new T3User(var0.getUsername(), var0.getPassword()));
         ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
         if (var0.isUsingBootProperties()) {
            AdminToolHelper var10000 = toolHelper;
            MBeanHome var9 = AdminToolHelper.getMBeanHome(var0);
         }

         System.out.println(var4.getShutdownSequenceInitiated());
         t3.services.admin().shut(var1, var3);
         t3 = null;
      } catch (T3ExecuteException var6) {
         if (var6.getNestedException() instanceof SecurityException) {
            var5 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var5.getConnSecException());
            }

            throw var6;
         } else {
            var5 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var5.getFailShutdown(var0.getURL()) + var6.getNestedException());
            }

            throw var6;
         }
      } catch (ServerLifecycleException var7) {
         var5 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var5.getShuttingdownServerFailed(var0.getURL()));
         }

         throw var7;
      } catch (SecurityException var8) {
         var5 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.out.println(var5.getConnSecException());
         }

         throw var8;
      }
   }

   static HashSet setServerPropertiesFiles(String[] var0) {
      HashSet var1 = new HashSet();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(var0[var2]);
      }

      return var1;
   }

   static HashSet setClusterPropertiesFiles(String[] var0) {
      HashSet var1 = new HashSet();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(var0[var2]);
      }

      return var1;
   }

   static String[] getServerPropertiesFiles(HashSet var0) {
      if (var0 == null) {
         return new String[0];
      } else {
         String[] var1 = new String[var0.size()];
         var1 = (String[])((String[])var0.toArray(var1));
         return var1;
      }
   }

   static String[] getClusterPropertiesFiles(HashSet var0) {
      if (var0 == null) {
         return new String[0];
      } else {
         String[] var1 = new String[var0.size()];
         var1 = (String[])((String[])var0.toArray(var1));
         return var1;
      }
   }

   static String executeCancelShutCommand(CommandLineArgs var0) throws Exception {
      System.out.println("This command has been removed.");
      String var1 = "This command has been removed";
      return var1;
   }

   static String executeShutCommand(CommandLineArgs var0) throws Exception {
      AdminToolHelper.shutdownCommand = true;
      int var1 = toolHelper.nextArg(-1, 0);
      String var2 = "";
      String var3 = "";
      var2 = toolHelper.nextArg("", 1);

      ManagementTextTextFormatter var5;
      try {
         connect(var0.getURL(), new T3User(var0.getUsername(), var0.getPassword()));
         if (var0.isUsingBootProperties()) {
            AdminToolHelper var10000 = toolHelper;
            MBeanHome var4 = AdminToolHelper.getMBeanHome(var0);
         }

         ManagementTextTextFormatter var8 = new ManagementTextTextFormatter();
         System.out.println(var8.getShutdownSequenceInitiated());
         var3 = t3.services.admin().shut(var2, var1);
         t3 = null;
         return var3;
      } catch (T3ExecuteException var6) {
         if (var6.getNestedException() instanceof SecurityException) {
            var5 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var5.getConnSecException());
            }

            throw var6;
         } else {
            var5 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var5.getFailShutdown(var0.getURL()) + var6.getNestedException());
            }

            throw var6;
         }
      } catch (SecurityException var7) {
         var5 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.out.println(var5.getConnSecException());
         }

         throw var7;
      }
   }

   static void executeLockCommand(CommandLineArgs var0) throws Exception {
      Context var2 = null;
      MBeanHome var3 = null;
      boolean var4 = false;
      ServerMBean var5 = null;
      ServerLifeCycleRuntimeMBean var6 = null;
      ServerLifeCycleTaskRuntimeMBean var7 = null;
      ServerRuntimeMBean var8 = null;
      String var9 = null;

      ManagementTextTextFormatter var10;
      String var11;
      try {
         try {
            var2 = getInitialContext(var0);
         } catch (Exception var12) {
            var10 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var10.getFailedConnect(var0.getURL()) + "[" + var12 + "]");
            }

            throw var12;
         }

         var3 = (MBeanHome)var2.lookup("weblogic.management.home.localhome");
         var11 = var3.getMBeanServer().getServerName();
         var8 = (ServerRuntimeMBean)var3.getMBean(var11, "ServerRuntime", var3.getDomainName(), var11);
         var5 = (ServerMBean)var3.getMBean(var11, "Server");
         var6 = (ServerLifeCycleRuntimeMBean)var3.getMBean(var5.getName(), "ServerLifeCycleRuntime", var3.getActiveDomain().getName());
         var9 = var8.getName();
         var9 = var6.getName();
      } catch (Exception var13) {
         new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            AdminToolHelper.printException(var13);
         }

         throw var13;
      }

      try {
         var7 = var6.suspend();
         if (var7 != null && var7.isRunning()) {
            do {
               Thread.sleep(1000L);
            } while(var7.isRunning());
         }

         if (var7.getStatus().equals("TASK COMPLETED")) {
            var10 = new ManagementTextTextFormatter();
            System.out.println(var10.getLockSuccess(var9));
         } else {
            var11 = "";
            if (var7.getError() != null) {
               var10 = new ManagementTextTextFormatter();
               var11 = var10.getExceptionMsg(var7.getError().getMessage());
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  AdminToolHelper.printErrorMessage(var11, false);
               }

               throw new Exception();
            }
         }
      } catch (Exception var14) {
         var10 = new ManagementTextTextFormatter();
         System.err.println(var10.getLockFailed(var9));
         var10 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var10.getExceptionMsg(var14.getMessage()));
         }

         throw var14;
      }
   }

   static void executeOldLockCommand(CommandLineArgs var0) throws Exception {
      System.out.println("This command has been deprecated and removed. Please use suspend instead");
   }

   static void executeOldUnLockCommand(CommandLineArgs var0) throws Exception {
      System.out.println("This command has been deprecated and removed. Please use resume instead");
   }

   static void executeUnLockCommand(CommandLineArgs var0) throws Exception {
      Context var2 = null;
      MBeanHome var3 = null;
      boolean var4 = false;
      Object var5 = null;
      ServerRuntimeMBean var6 = null;
      ServerLifeCycleRuntimeMBean var7 = null;
      ServerLifeCycleTaskRuntimeMBean var8 = null;
      String var9 = null;

      ManagementTextTextFormatter var10;
      String var11;
      try {
         try {
            var2 = getInitialContext(var0);
         } catch (Exception var13) {
            var10 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var10.getFailedConnect(var0.getURL()) + "[" + var13 + "]");
            }

            throw var13;
         }

         var3 = (MBeanHome)var2.lookup("weblogic.management.home.localhome");
         var11 = var3.getMBeanServer().getServerName();
         var6 = (ServerRuntimeMBean)var3.getMBean(var11, "ServerRuntime", var3.getDomainName(), var11);
         var7 = (ServerLifeCycleRuntimeMBean)var3.getMBean(var11, "ServerLifeCycleRuntime");
         var9 = var7.getName();
      } catch (Exception var14) {
         var10 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var10.getExceptionMsg(var14.getMessage()));
         }

         throw var14;
      }

      try {
         var8 = var7.resume();
         if (var8.getStatus().equals("TASK COMPLETED")) {
            var10 = new ManagementTextTextFormatter();
            System.out.println(var10.getUnLockSuccess(var9));
         } else {
            var11 = "";
            if (var8.getError() != null) {
               var10 = new ManagementTextTextFormatter();
               var11 = var10.getExceptionMsg(var8.getError().getMessage());
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  AdminToolHelper.printErrorMessage(var11, false);
               }

               throw new Exception();
            }
         }
      } catch (Exception var12) {
         var10 = new ManagementTextTextFormatter();
         System.err.println(var10.getUnlockFail(var9));
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            AdminToolHelper.printException(var12);
         }

         throw var12;
      }
   }

   static void executeStartCommand(CommandLineArgs var0) throws Exception {
      Context var1 = null;
      ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
      String var5 = toolHelper.nextArg("", 0);

      try {
         if (var5.equals("")) {
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var4.getServerNameArgRequired());
            }

            throw new Exception();
         } else {
            try {
               var1 = getInitialContext(var0);
            } catch (Exception var12) {
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  System.err.println(var4.getFailedConnect(var0.getURL()) + "[" + var12 + "]");
               }

               throw var12;
            }

            MBeanHome var6 = (MBeanHome)var1.lookup("weblogic.management.home.localhome");
            Iterator var2 = var6.getMBeansByType("ServerRuntime").iterator();
            ServerRuntimeMBean var7 = (ServerRuntimeMBean)var2.next();
            String var8 = var7.getName();
            if (!var7.isAdminServer()) {
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  System.err.println(var4.getAdminServerUrlRequired(var0.getURL()));
               }

               throw new Exception();
            } else if (var8.equals(var5)) {
               System.err.println(var4.getServerAlreadyRunning(var5));
            } else {
               boolean var9 = false;
               ServerMBean var10 = null;
               var2 = var6.getMBeansByType("Server").iterator();

               while(var2.hasNext()) {
                  var10 = (ServerMBean)var2.next();
                  if (var10.getName().equals(var5)) {
                     var9 = true;
                     break;
                  }
               }

               if (!var9) {
                  if (!AdminToolHelper.printDone) {
                     AdminToolHelper.printDone = true;
                     System.err.println(var4.getServerNotConfigured(var5, var6.getDomainName()));
                  }

                  throw new Exception();
               } else {
                  ServerLifeCycleRuntimeMBean var11 = (ServerLifeCycleRuntimeMBean)var6.getMBean(var10.getName(), "ServerLifeCycleRuntime", var6.getActiveDomain().getName());
                  ServerLifeCycleTaskRuntimeMBean var3;
                  if (var0.getOperation() == 21) {
                     var3 = var11.start();
                  } else {
                     System.out.println(var4.getStartInStandbyModeDeperecated());
                     var3 = var11.start();
                  }

                  if (var3 != null && var3.isRunning()) {
                     do {
                        Thread.sleep(1000L);
                     } while(var3.isRunning());
                  }

                  printLog = new PrintWriter(System.out);
                  if (var3.getStatus().equals("TASK COMPLETED")) {
                     System.out.println(var4.getServerStartedSuccessfully(var5));
                  } else {
                     if (!AdminToolHelper.printDone) {
                        AdminToolHelper.printDone = true;
                        System.out.println(var4.getStartingServerFailed(var5));
                        if (var3.getError() != null) {
                           System.err.println(var4.getExceptionMsg(var3.getError().getMessage()));
                        }
                     }

                     throw new Exception();
                  }
               }
            }
         }
      } catch (InstanceNotFoundException var13) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getExceptionMsg(var13.getMessage()));
         }

         throw var13;
      } catch (SecurityException var14) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getExceptionMsg(var14.getMessage()));
         }

         throw var14;
      } catch (Exception var15) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var4.getExceptionMsg(var15.getMessage()));
         }

         throw var15;
      }
   }

   static void executeResumeCommand(CommandLineArgs var0) throws Exception {
      Context var2 = null;
      MBeanHome var3 = null;
      boolean var4 = false;
      ServerMBean var5 = null;
      ServerRuntimeMBean var6 = null;
      ServerLifeCycleTaskRuntimeMBean var7 = null;
      String var8 = null;
      String var10 = toolHelper.nextArg("", 0);

      ManagementTextTextFormatter var9;
      try {
         try {
            var2 = getInitialContext(var0);
         } catch (Exception var14) {
            var9 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var9.getFailedConnect(var0.getURL()) + "[" + var14 + "]");
            }

            throw var14;
         }

         var3 = (MBeanHome)var2.lookup("weblogic.management.home.localhome");
         Iterator var1 = var3.getMBeansByType("ServerRuntime").iterator();
         var6 = (ServerRuntimeMBean)var1.next();
         var8 = var6.getName();
      } catch (Exception var15) {
         var9 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var9.getExceptionMsg(var15.getMessage()));
         }

         throw var15;
      }

      try {
         if (!var10.equals("") && !var10.equals(var8)) {
            if (!var6.isAdminServer()) {
               var9 = new ManagementTextTextFormatter();
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  System.err.println(var9.getUrlOfAdminServerRequired(var0.getURL()));
               }

               throw new Exception();
            } else {
               try {
                  var5 = (ServerMBean)var3.getMBean(var10, "Server", var3.getDomainName());
               } catch (InstanceNotFoundException var13) {
                  var9 = new ManagementTextTextFormatter();
                  if (!AdminToolHelper.printDone) {
                     AdminToolHelper.printDone = true;
                     System.err.println(var9.getServerNotConfigured(var10, var3.getDomainName()));
                  }

                  throw var13;
               }

               ServerLifeCycleRuntimeMBean var11 = (ServerLifeCycleRuntimeMBean)var3.getMBean(var5.getName(), "ServerLifeCycleRuntime", var3.getActiveDomain().getName());
               var7 = var11.resume();
               if (var7 != null && var7.isRunning()) {
                  do {
                     Thread.sleep(1000L);
                  } while(var7.isRunning());
               }

               printLog = new PrintWriter(System.out);
               if (var7.getStatus().equals("TASK COMPLETED")) {
                  var9 = new ManagementTextTextFormatter();
                  System.out.println(var9.getServerResumedSuccessfully(var10));
               } else {
                  if (!AdminToolHelper.printDone) {
                     AdminToolHelper.printDone = true;
                     var9 = new ManagementTextTextFormatter();
                     System.err.println(var9.getResumingServerFailed(var10));
                     if (var7.getError() != null) {
                        var9 = new ManagementTextTextFormatter();
                        System.err.println(var9.getExceptionMsg(var7.getError().getMessage()));
                     }
                  }

                  throw new Exception();
               }
            }
         } else {
            try {
               var6.resume();
               var9 = new ManagementTextTextFormatter();
               System.out.println(var9.getServerResumedSuccessfully(var8));
            } catch (Exception var12) {
               var9 = new ManagementTextTextFormatter();
               System.err.println(var9.getResumingServerFailed(var8));
               var9 = new ManagementTextTextFormatter();
               if (!AdminToolHelper.printDone) {
                  AdminToolHelper.printDone = true;
                  System.err.println(var9.getExceptionMsg(var12.getMessage()));
               }

               throw var12;
            }
         }
      } catch (SecurityException var16) {
         var9 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var9.getExceptionMsg(var16.getMessage()));
         }

         throw var16;
      } catch (Exception var17) {
         var9 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var9.getExceptionMsg(var17.getMessage()));
         }

         throw var17;
      }
   }

   void doOperation() throws Exception {
      String var1 = "";
      switch (params.getOperation()) {
         case 4:
            var1 = executeShutCommand(params);
         case 5:
         case 6:
         case 7:
         case 10:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 23:
         case 24:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         default:
            break;
         case 8:
            executeLockCommand(params);
            break;
         case 9:
            executeOldUnLockCommand(params);
            break;
         case 11:
            var1 = executeCancelShutCommand(params);
            break;
         case 21:
         case 22:
            executeStartCommand(params);
            break;
         case 25:
            executeResumeCommand(params);
            break;
         case 26:
         case 27:
            executeShutdownCommand(params);
            break;
         case 43:
            throw new UnsupportedOperationException(" Discover Managed Server is not supported in WLS 9.0.0.0");
      }

      System.out.println(var1);
   }

   private void doCommandline() throws Exception {
      ManagementTextTextFormatter var10;
      try {
         if (this.adminHome == null) {
            if (params.getAdminUrl() != null) {
               params.setUrl(params.getAdminUrl());
               this.adminHome = AdminToolHelper.getAdminMBeanHome(params);
            } else {
               this.adminHome = AdminToolHelper.getMBeanHome(params);
            }
         }

         this.out = new OutputFormatter(printStream, params.isPretty());
         this.doOperation();
      } catch (IllegalArgumentException var3) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var3);
            AdminToolHelper.printDone = true;
         }

         throw var3;
      } catch (MalformedObjectNameException var4) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var4, true);
            AdminToolHelper.printDone = true;
         }

         printStream.println("Usage:\n" + CommandLineArgs.getUsageString());
         throw var4;
      } catch (InstanceNotFoundException var5) {
         var10 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            printStream.println(var10.getCouldNotFindInstance(params.getMBeanObjName()));
            AdminToolHelper.printDone = true;
         }

         throw var5;
      } catch (RemoteRuntimeException var6) {
         Throwable var11 = var6.getNestedException();
         if (var11 instanceof PeerGoneException) {
            return;
         }

         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var6, true);
            AdminToolHelper.printDone = true;
         }

         throw var6;
      } catch (java.net.ConnectException var7) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException("java.net.ConnectException", var7);
            AdminToolHelper.printDone = true;
         }

         throw var7;
      } catch (IOException var8) {
         var10 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var10.getErrorWriting(), var8);
            AdminToolHelper.printDone = true;
         }

         throw var8;
      } catch (Exception var9) {
         if (!(var9 instanceof RuntimeOperationsException)) {
            if (var9 instanceof ClassCastException) {
               return;
            }

            if (!params.showNoMessages() && !AdminToolHelper.printDone) {
               AdminToolHelper.printException(var9);
               AdminToolHelper.printDone = true;
            }

            throw var9;
         }

         RuntimeOperationsException var2 = (RuntimeOperationsException)var9;
         if (var2.getTargetException() instanceof RemoteRuntimeException) {
         }
      }

   }

   private static Context getInitialContext(String var0, String var1, String var2) throws Exception {
      return AdminToolHelper.getInitialContext(var0, var1, var2);
   }

   private static Context getInitialContext(CommandLineArgs var0) throws Exception {
      return AdminToolHelper.getInitialContext(var0);
   }

   static {
      printStream = System.out;
   }
}
