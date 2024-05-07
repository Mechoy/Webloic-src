package weblogic.management.commandline.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.management.InstanceNotFoundException;
import javax.management.RuntimeOperationsException;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import weblogic.common.T3Client;
import weblogic.common.T3ExecuteException;
import weblogic.common.T3User;
import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.commandline.CommandLineArgs;
import weblogic.management.commandline.OutputFormatter;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.extensions.RemoteRuntimeException;

public final class ServerInfoCommandLineInvoker {
   static final String OK_STRING = "Ok";
   private static final int TIME_ROUNDOFF = 1;
   private static final int TIME_TRUNCATE = 2;
   static CommandLineArgs params = null;
   static T3Client t3 = null;
   static AdminToolHelper toolHelper = null;
   MBeanHome adminHome;
   Context ctx;
   OutputFormatter out;
   private static boolean CONTINUE = true;
   private static PrintStream printStream;
   private boolean EXIT;
   private boolean batchMode;

   public ServerInfoCommandLineInvoker(CommandLineArgs var1, PrintStream var2) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;

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

   public ServerInfoCommandLineInvoker(String[] var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;
      params = new CommandLineArgs(var1);
      if (var2 != null) {
         printStream = var2;
      }

      this.adminHome = var3;
      toolHelper = new AdminToolHelper(params);
      this.doCommandline();
   }

   public ServerInfoCommandLineInvoker(CommandLineArgs var1, PrintStream var2, MBeanHome var3) throws Exception {
      this.adminHome = null;
      this.ctx = null;
      this.out = null;
      this.EXIT = false;
      this.batchMode = false;
      params = var1;
      if (var2 != null) {
         printStream = var2;
      }

      this.adminHome = var3;
      toolHelper = new AdminToolHelper(params);
      this.doCommandline();
   }

   public ServerInfoCommandLineInvoker(String[] var1, PrintStream var2) throws Exception {
      this(new CommandLineArgs(var1), var2);
   }

   public static void main(String[] var0) throws Exception {
      new ServerInfoCommandLineInvoker(var0, System.out);
   }

   static String executeCommandConnect(CommandLineArgs var0) throws Exception {
      int var1 = toolHelper.nextArg(1, 0);
      String var2 = "";
      long var3 = System.currentTimeMillis();

      for(int var5 = 0; var5 < var1; ++var5) {
         long var6 = System.currentTimeMillis();
         Context var8 = getInitialContext(var0);
         long var9 = System.currentTimeMillis() - var6;
         ManagementTextTextFormatter var11 = new ManagementTextTextFormatter();
         System.out.println(var11.getConnectOutput(var5, var9));
      }

      long var12 = System.currentTimeMillis() - var3;
      var2 = "  RTT = ~" + var12 + " milliseconds, or ~" + var12 / (long)var1 + " milliseconds/connection";
      return var2;
   }

   static String executePingCommand(CommandLineArgs var0) throws Exception {
      String var1 = "";
      int var2 = toolHelper.nextArg(1, 0);
      int var3 = toolHelper.nextArg(100, 1);
      if (t3 == null) {
         connect(var0.getURL(), new T3User(var0.getUsername(), var0.getPassword()));
      }

      if (var2 == 0) {
         var1 = "0 pings sent";
      } else {
         byte[] var4 = new byte[var3];
         ManagementTextTextFormatter var7 = new ManagementTextTextFormatter();
         String var5;
         if (var2 != 1) {
            var5 = var7.getPluralPing();
         } else {
            var5 = var7.getSingularPing();
         }

         String var6;
         if (var3 != 1) {
            var6 = var7.getPluralByte();
         } else {
            var6 = var7.getSingularByte();
         }

         System.out.println(var7.getPingCount(var2, var5, var3, var6));
         long var8 = System.currentTimeMillis();

         for(int var10 = 0; var10 < var2; ++var10) {
            t3.services.admin().ping(var4);
         }

         long var12 = System.currentTimeMillis() - var8;
         var1 = "  RTT = ~" + var12 + " milliseconds, or ~" + var12 / (long)var2 + " milliseconds/packet";
      }

      return var1;
   }

   static String executeVersionCommand(CommandLineArgs var0) throws Exception {
      String var1 = "";
      Context var3 = null;
      MBeanHome var4 = null;
      ServerRuntimeMBean var5 = null;

      ManagementTextTextFormatter var6;
      try {
         try {
            var3 = getInitialContext(var0);
         } catch (Exception var8) {
            var6 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var6.getFailedConnect(getURL()) + "[" + var8 + "]");
            }

            throw var8;
         }

         var4 = (MBeanHome)var3.lookup("weblogic.management.home.localhome");
         Iterator var2 = var4.getMBeansByType("ServerRuntime").iterator();
         var5 = (ServerRuntimeMBean)var2.next();
         var1 = var5.getWeblogicVersion();
         return var1;
      } catch (Exception var9) {
         var6 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var6.getExceptionMsg(var9.getMessage()));
         }

         throw var9;
      }
   }

   static void executeGetStateCommand(CommandLineArgs var0) throws Exception {
      Context var2 = null;
      MBeanHome var3 = null;
      boolean var4 = false;
      ServerMBean var5 = null;
      ServerRuntimeMBean var6 = null;
      String var7 = null;
      String var9 = toolHelper.nextArg("", 0);

      ManagementTextTextFormatter var8;
      try {
         try {
            var2 = getInitialContext(var0);
         } catch (Exception var11) {
            var8 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var8.getFailedConnect(getURL()) + "[" + var11 + "]");
            }

            throw var11;
         }

         var3 = (MBeanHome)var2.lookup("weblogic.management.home.localhome");
         Iterator var1 = var3.getMBeansByType("ServerRuntime").iterator();
         var6 = (ServerRuntimeMBean)var1.next();
         var7 = var6.getName();
      } catch (Exception var12) {
         var8 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var8.getExceptionMsg(var12.getMessage()));
         }

         throw var12;
      }

      try {
         if (var9.equals("") || var9.equals(var7)) {
            var8 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var8.getCurrentStateOfServer(var7, var6.getState()));
            }

            return;
         }

         if (!var6.isAdminServer()) {
            var8 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var8.getUrlOfAdminServerRequired(getURL()));
            }

            throw new Exception();
         }

         try {
            var5 = (ServerMBean)var3.getMBean(var9, "Server", var3.getDomainName());
         } catch (InstanceNotFoundException var13) {
            var8 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var8.getServerNotConfigured(var9, var3.getDomainName()));
            }

            throw var13;
         } catch (Exception var14) {
            var8 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.err.println(var8.getExceptionMsg(var14.getMessage()));
            }

            throw var14;
         }

         var8 = new ManagementTextTextFormatter();
         ServerLifeCycleRuntimeMBean var10 = (ServerLifeCycleRuntimeMBean)var3.getMBean(var5.getName(), "ServerLifeCycleRuntime", var3.getActiveDomain().getName());
         System.out.println(var8.getCurrentStateOfServer(var9, var10.getState()));
      } catch (Exception var15) {
         if (var5 == null) {
            throw var15;
         }

         if (var5.getExpectedToRun()) {
            var8 = new ManagementTextTextFormatter();
            System.out.println(var8.getCurrentStateOfServer(var9, "UNKNOWN"));
         } else {
            var8 = new ManagementTextTextFormatter();
            System.out.println(var8.getCurrentStateOfServer(var9, "SHUTDOWN"));
         }
      }

   }

   static void executeListCommand(CommandLineArgs var0) throws Exception {
      Environment var1 = new Environment();
      String var2 = toolHelper.nextArg("", 0);
      if (var0.getUsername().length() != 0) {
         var1.setSecurityPrincipal(var0.getUsername());
         var1.setSecurityCredentials(var0.getPassword());
      }

      String var3 = var0.getURL();
      if (var3.startsWith("iiop")) {
         var3 = "t3" + var3.substring(4);
      }

      var1.setProviderUrl(var3);

      ManagementTextTextFormatter var5;
      try {
         Context var4 = var1.getInitialContext();
         NamingEnumeration var13 = var4.list(var2);
         if (var13.hasMoreElements()) {
            String var14;
            if (var2.length() == 0) {
               var14 = "InitialContext";
            } else {
               var14 = var2;
            }

            ManagementTextTextFormatter var7 = new ManagementTextTextFormatter();
            System.out.println(var7.getContents(var14));

            while(var13.hasMoreElements()) {
               System.out.println("  " + var13.nextElement());
            }
         }

      } catch (CommunicationException var8) {
         Throwable var12 = var8.getRootCause();
         ManagementTextTextFormatter var6 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.err.println(var6.getFailedConnect(getURL()) + " [" + var12.getMessage() + "]");
         }

         if (var0.isVerbose()) {
            var8.printStackTrace();
         }

         throw var8;
      } catch (AuthenticationException var9) {
         var5 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.out.println(var5.getAuthException());
         }

         throw var9;
      } catch (SecurityException var10) {
         var5 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.out.println(var5.getSecException());
         }

         throw var10;
      } catch (NamingException var11) {
         var5 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            System.out.println(var5.getFailBinding(var11.getMessage()));
            var11.printStackTrace();
         }

         if (var0.isVerbose()) {
            var11.printStackTrace();
         }

         throw var11;
      }
   }

   static void executeThreadDumpCommand(CommandLineArgs var0) throws Exception {
      connect(var0.getURL(), new T3User(var0.getUsername(), var0.getPassword()));

      try {
         t3.services.admin().threadDump();
         ManagementTextTextFormatter var1 = new ManagementTextTextFormatter();
         System.out.println(var1.getThreadDumpAvailable());
      } catch (T3ExecuteException var3) {
         ManagementTextTextFormatter var2;
         if (var3.getNestedException().toString().indexOf("SecurityException") != -1) {
            var2 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var2.getSecExceptionThreadDump());
            }

            throw var3;
         } else {
            var2 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               AdminToolHelper.printDone = true;
               System.out.println(var2.getFailThreadDump() + var3.getNestedException().toString());
            }

            throw var3;
         }
      }
   }

   static void connect(String var0, T3User var1) throws IOException, T3ExecuteException {
      ManagementTextTextFormatter var3;
      try {
         if (var0.startsWith("iiop")) {
            var0 = "t3" + var0.substring(4);
         }

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

   void doOperation() throws Exception {
      String var1 = "";
      switch (params.getOperation()) {
         case 2:
            var1 = executePingCommand(params);
            break;
         case 6:
            var1 = executeVersionCommand(params);
            break;
         case 7:
            var1 = executeCommandConnect(params);
            break;
         case 10:
            executeListCommand(params);
            break;
         case 18:
            executeThreadDumpCommand(params);
            break;
         case 19:
            executeCommandServerLog();
            break;
         case 28:
            executeGetStateCommand(params);
      }

      System.out.println("\n" + var1);
   }

   private static void executeCommandServerLog() throws Exception {
      String var0 = params.getHttpURL();
      String var1 = toolHelper.nextArg("", 0);
      String var2 = toolHelper.nextArg("", 1);
      Date var3 = convStr2Date(var1, 2);
      Date var4 = convStr2Date(var2, 1);
      String var5 = params.getUsername();
      String var6 = params.getPassword();
      String var7 = "logicalName=ServerLog";
      String var8 = null;
      if (var3 != null) {
         var8 = "beginTimestamp=" + var3.getTime();
      }

      String var9 = null;
      if (var4 != null) {
         var9 = "endTimestamp=" + var4.getTime();
      }

      String var10 = var0 + "/" + "bea_wls_diagnostics" + "/" + "accessor" + "?" + var7;
      if (var8 != null) {
         var10 = var10 + "&" + var8;
      }

      if (var9 != null) {
         var10 = var10 + "&" + var9;
      }

      System.err.println("Query = " + var10);
      URL var11 = new URL(var10);
      HttpURLConnection var12 = (HttpURLConnection)var11.openConnection();
      var12.setRequestProperty("username", var5);
      var12.setRequestProperty("password", var6);
      var12.setDoOutput(false);
      var12.setDoInput(true);
      BufferedReader var13 = null;

      try {
         var13 = new BufferedReader(new InputStreamReader(var12.getInputStream()));
      } catch (Exception var16) {
         ManagementTextTextFormatter var15 = new ManagementTextTextFormatter();
         System.out.println("\n" + var15.getGettingLogFileError() + var16.getMessage());
         AdminToolHelper.printDone = true;
         throw new Exception(var15.getGettingLogFileError() + var16.getMessage());
      }

      String var14 = null;

      try {
         while(null != (var14 = var13.readLine())) {
            System.out.println(var14);
         }
      } catch (IOException var17) {
         var17.printStackTrace();
      }

   }

   private static String augmentDate(String var0, String var1, int var2) {
      Calendar var3 = Calendar.getInstance();
      SimpleDateFormat var4 = new SimpleDateFormat(var1);
      StringTokenizer var5 = new StringTokenizer(var0, ":/- ");
      switch (var5.countTokens()) {
         case 0:
            return var0;
         case 1:
            var3.set(11, Integer.parseInt(var5.nextToken()));
            switch (var2) {
               case 1:
                  var3.set(12, 59);
                  var3.set(13, 59);
                  break;
               case 2:
                  var3.set(12, 0);
                  var3.set(13, 0);
            }

            return var4.format(var3.getTime()).toString();
         case 2:
            if (0 < var0.indexOf("/")) {
               var3.set(2, Integer.parseInt(var5.nextToken()) - 1);
               var3.set(5, Integer.parseInt(var5.nextToken()));
               switch (var2) {
                  case 1:
                     var3.set(11, 23);
                     var3.set(12, 59);
                     var3.set(13, 59);
                     break;
                  case 2:
                     var3.set(11, 0);
                     var3.set(12, 0);
                     var3.set(13, 0);
               }
            } else {
               var3.set(11, Integer.parseInt(var5.nextToken()));
               var3.set(12, Integer.parseInt(var5.nextToken()));
               switch (var2) {
                  case 1:
                     var3.set(13, 59);
                     break;
                  case 2:
                     var3.set(13, 0);
               }
            }

            return var4.format(var3.getTime()).toString();
         default:
            return var0;
      }
   }

   private static Date convStr2Date(String var0, int var1) {
      String[] var2 = new String[]{"yyyy MM dd HH mm ss", "yyyy MM dd HH mm", "yyyy MM dd HH", "yyyy MM dd", "MMM dd yyyy HH mm ss", "MMM dd yyyy HH mm", "MMM dd yyyy HH", "MMM dd yyyy"};
      Date var3 = null;
      String var4 = null;
      var4 = augmentDate(var0, var2[0], var1);
      String var5 = var4.trim();
      if (var5.equals("")) {
         return null;
      } else {
         var5 = var5.replace('/', ' ');
         var5 = var5.replace('-', ' ');
         var5 = var5.replace(':', ' ');
         var5 = var5.replace(',', ' ');
         Calendar var6 = Calendar.getInstance();
         ParsePosition var7 = new ParsePosition(0);

         for(int var8 = 0; var8 < var2.length && null == var3; ++var8) {
            SimpleDateFormat var9 = new SimpleDateFormat(var2[var8]);
            var9.setCalendar(var6);
            var3 = var9.parse(var5, var7);
         }

         return var3;
      }
   }

   private void doCommandline() throws Exception {
      ManagementTextTextFormatter var9;
      try {
         if (params.getOperation() != 41 && this.adminHome == null) {
            if (params.getAdminUrl() != null) {
               this.adminHome = AdminToolHelper.getAdminMBeanHome(params);
               params.setUrl(params.getAdminUrl());
            } else {
               this.adminHome = AdminToolHelper.getMBeanHome(params);
            }
         }

         this.out = new OutputFormatter(printStream, params.isPretty());
         this.doOperation();
      } catch (IllegalArgumentException var3) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            AdminToolHelper.printException(var3);
         }

         throw var3;
      } catch (InstanceNotFoundException var4) {
         var9 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            printStream.println(var9.getCouldNotFindInstance(params.getMBeanObjName()));
            AdminToolHelper.printDone = true;
         }

         throw var4;
      } catch (RemoteRuntimeException var5) {
         Throwable var10 = var5.getNestedException();
         if (var10 instanceof PeerGoneException) {
            return;
         }

         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var5, true);
            AdminToolHelper.printDone = true;
         }

         throw var5;
      } catch (java.net.ConnectException var6) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printDone = true;
            var9 = new ManagementTextTextFormatter();
            AdminToolHelper.printErrorMessage(var9.getConnectFailedError(getURL()), CONTINUE);
         }

         throw var6;
      } catch (IOException var7) {
         var9 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var9.getErrorWriting(), var7);
            AdminToolHelper.printDone = true;
         }

         throw var7;
      } catch (Exception var8) {
         if (!(var8 instanceof RuntimeOperationsException)) {
            if (!params.showNoMessages() && !AdminToolHelper.printDone) {
               AdminToolHelper.printException(var8);
               AdminToolHelper.printDone = true;
            }

            throw var8;
         }

         RuntimeOperationsException var2 = (RuntimeOperationsException)var8;
         if (var2.getTargetException() instanceof RemoteRuntimeException) {
         }
      }

   }

   static boolean isServerAlive(List var0, String var1) {
      boolean var2 = false;
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var4.equals(var1)) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   private static Context getInitialContext(String var0, String var1, String var2) throws Exception {
      return AdminToolHelper.getInitialContext(var0, var1, var2);
   }

   private static Context getInitialContext(CommandLineArgs var0) throws Exception {
      return AdminToolHelper.getInitialContext(var0);
   }

   private static String getURL() {
      String var0 = params.getAdminUrl();
      if (var0 == null) {
         var0 = params.getURL();
      }

      return var0;
   }

   static {
      printStream = System.out;
   }
}
