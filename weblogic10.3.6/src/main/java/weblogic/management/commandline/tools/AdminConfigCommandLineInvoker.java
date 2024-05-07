package weblogic.management.commandline.tools;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.management.InstanceNotFoundException;
import weblogic.management.MBeanHome;
import weblogic.management.commandline.CommandLineArgs;
import weblogic.management.commandline.OutputFormatter;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;

public final class AdminConfigCommandLineInvoker {
   static final String OK_STRING = "Ok";
   static CommandLineArgs params = null;
   static PrintWriter printLog = null;
   static AdminToolHelper toolHelper = null;
   MBeanHome adminHome;
   OutputFormatter out;
   private static boolean CONTINUE = true;
   private static PrintStream printStream;
   private boolean EXIT;

   public AdminConfigCommandLineInvoker(CommandLineArgs var1, PrintStream var2) throws Exception {
      this.adminHome = null;
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

   public AdminConfigCommandLineInvoker(String[] var1, PrintStream var2) throws Exception {
      this(new CommandLineArgs(var1), var2);
   }

   public static void main(String[] var0) throws Exception {
      new AdminConfigCommandLineInvoker(var0, System.out);
   }

   void storeUserConfig(CommandLineArgs var1) throws Exception {
      UsernameAndPassword var2 = new UsernameAndPassword(var1.getUsername(), var1.getPassword().toCharArray());
      if (var1.getUserConfig() == null && var1.getUserKey() == null) {
         UserConfigFileManager.setUsernameAndPassword(var2, "weblogic.management");
      } else {
         UserConfigFileManager.setUsernameAndPassword(var2, var1.getUserConfig(), var1.getUserKey(), "weblogic.management");
      }

   }

   void doOperation() throws Exception {
      String var1 = "";
      switch (params.getOperation()) {
         case 51:
            this.storeUserConfig(params);
         default:
            System.out.println(var1);
      }
   }

   private void doCommandline() throws Exception {
      ManagementTextTextFormatter var2;
      try {
         this.out = new OutputFormatter(printStream, params.isPretty());
         this.doOperation();
      } catch (IllegalArgumentException var3) {
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var3);
            AdminToolHelper.printDone = true;
         }

         throw var3;
      } catch (InstanceNotFoundException var4) {
         var2 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            printStream.println(var2.getCouldNotFindInstance(params.getMBeanObjName()));
            AdminToolHelper.printDone = true;
         }

         throw var4;
      } catch (IOException var5) {
         var2 = new ManagementTextTextFormatter();
         if (!AdminToolHelper.printDone) {
            AdminToolHelper.printException(var2.getErrorWriting(), var5);
            AdminToolHelper.printDone = true;
         }

         throw var5;
      } catch (Exception var6) {
         if (!(var6 instanceof ClassCastException)) {
            if (!params.showNoMessages() && !AdminToolHelper.printDone) {
               AdminToolHelper.printException(var6);
               AdminToolHelper.printDone = true;
            }

            throw var6;
         }
      }
   }

   static {
      printStream = System.out;
   }
}
