package weblogic.management.commandline;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.util.Locale;
import weblogic.common.T3Client;
import weblogic.kernel.AuditableThreadLocal;
import weblogic.kernel.AuditableThreadLocalFactory;
import weblogic.kernel.Kernel;
import weblogic.management.NoAccessException;
import weblogic.management.commandline.tools.AdminConfigCommandLineInvoker;
import weblogic.management.commandline.tools.AdminToolHelper;
import weblogic.management.commandline.tools.ClusterAdminCommandLineInvoker;
import weblogic.management.commandline.tools.JDBCCommandLineInvoker;
import weblogic.management.commandline.tools.MBeanCommandLineInvoker;
import weblogic.management.commandline.tools.ServerAdminCommandLineInvoker;
import weblogic.management.commandline.tools.ServerInfoCommandLineInvoker;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.security.internal.encryption.EncryptionServiceException;

public class AdminMain {
   private static final AuditableThreadLocal outputStream = AuditableThreadLocalFactory.createThreadLocal();
   static CommandLineArgs params = null;
   static AdminToolHelper toolHelper = null;
   static T3Client t3 = null;

   public static void main(String[] var0) throws Throwable {
      int var16;
      ManagementTextTextFormatter var2;
      try {
         params = new CommandLineArgs(var0);
         toolHelper = new AdminToolHelper(params);
         if (var0.length == 0) {
            usage();
            throw new Exception();
         }

         if (!Kernel.isServer() && params.getOperation() != 41) {
            toolHelper.evaluateCredentials();
            if (params.getUsername() == null & params.getPassword() == null || params.getUsername().length() == 0 & params.getPassword().length() == 0) {
               var16 = params.getOperation();
               if (var16 != 0 && var16 != 1 && var16 != 20 && var16 <= 99) {
                  var2 = new ManagementTextTextFormatter();
                  getOutputStream().println(var2.getNoUserNameNoPassword());
                  usage();
                  throw new Exception();
               }

               usage();
               throw new Exception();
            }

            if (params.getUsername() == null) {
               ManagementTextTextFormatter var17 = new ManagementTextTextFormatter();
               getOutputStream().println(var17.getNoUserNameNoPassword());
               usage();
               throw new Exception();
            }
         }
      } catch (IllegalArgumentException var14) {
         AdminToolHelper.printErrorMessage(var14.getMessage(), true);
         usage();
         throw var14;
      } catch (EncryptionServiceException var15) {
         var2 = new ManagementTextTextFormatter();
         getOutputStream().println(var2.getEncryptionError());
         userconfigusage();
         throw var15;
      }

      boolean var1 = true;
      var16 = params.getOperation();

      try {
         switch (var16) {
            case 2:
            case 5:
            case 6:
            case 7:
            case 10:
            case 18:
            case 19:
            case 28:
               new ServerInfoCommandLineInvoker(params, getOutputStream());
               return;
            case 3:
            case 23:
            case 24:
            case 30:
            case 49:
            default:
               usage();
               return;
            case 4:
            case 8:
            case 9:
            case 11:
            case 21:
            case 22:
            case 25:
            case 26:
            case 27:
            case 42:
            case 43:
               new ServerAdminCommandLineInvoker(params, getOutputStream());
               return;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 38:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
               new JDBCCommandLineInvoker(params, getOutputStream());
               return;
            case 20:
               showHelp();
               return;
            case 29:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
               new MBeanCommandLineInvoker(params, getOutputStream());
               return;
            case 37:
            case 39:
            case 40:
            case 41:
            case 50:
            case 52:
               new ClusterAdminCommandLineInvoker(params, getOutputStream());
               return;
            case 51:
               new AdminConfigCommandLineInvoker(params, getOutputStream());
               return;
         }
      } catch (UndeclaredThrowableException var11) {
         Throwable var3 = var11.getUndeclaredThrowable();
         if (var3 instanceof NoAccessException) {
            ManagementTextTextFormatter var4 = new ManagementTextTextFormatter();
            if (!AdminToolHelper.printDone) {
               System.out.println(var4.getAuthError());
               AdminToolHelper.printDone = true;
            }
         }

         if (params.isNoExit()) {
            return;
         }

         throw var11;
      } catch (Throwable var12) {
         if (!(var12 instanceof ClassCastException)) {
            if (params.isVerbose()) {
               var12.printStackTrace();
            } else if (!AdminToolHelper.printDone) {
               if (var12.getMessage() != null && var12.getMessage().length() != 0) {
                  AdminToolHelper.printErrorMessage(var12.getMessage(), true);
               } else {
                  var12.printStackTrace();
               }
            }

            if (params.isNoExit()) {
               return;
            }

            throw var12;
         }
      } finally {
         if (t3 != null && t3.isConnected()) {
            t3.disconnect();
         }

      }

   }

   static void usage() {
      getOutputStream().println(CommandLineArgs.getUsageString(false));
   }

   static void userconfigusage() {
      getOutputStream().println(CommandLineArgs.getUsageString(true));
   }

   private static void showHelp() throws IllegalArgumentException, MalformedURLException {
      String var0 = toolHelper.nextArg("", 0);
      if (var0.equals("")) {
         usage();
      } else {
         var0 = var0.toUpperCase(Locale.US);
         System.out.println(CommandLineArgs.getUsageString(var0, false));
      }
   }

   public static final void setOutputStream(OutputStream var0) {
      outputStream.set(new PrintStream(var0));
   }

   private static final PrintStream getOutputStream() {
      PrintStream var0;
      return (var0 = (PrintStream)outputStream.get()) == null ? System.out : var0;
   }
}
