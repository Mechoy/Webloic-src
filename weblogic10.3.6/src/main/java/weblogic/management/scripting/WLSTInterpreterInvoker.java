package weblogic.management.scripting;

import java.io.File;
import org.python.core.PyList;
import org.python.core.PyString;
import weblogic.management.scripting.utils.WLSTInterpreter;
import weblogic.management.scripting.utils.WLSTUtil;
import weblogic.utils.StringUtils;

public class WLSTInterpreterInvoker {
   private String propertiesFile;
   private String fileName;
   private String arguments = "";
   private String scriptTempFile;
   private boolean failOnError = true;
   private boolean executeScriptBeforeFile = true;
   private boolean debug = false;

   public static void main(String[] arg) throws Throwable {
      WLSTInterpreterInvoker invoker = new WLSTInterpreterInvoker();
      invoker.parseArgs(arg);
      invoker.executePyScript();
   }

   private void parseArgs(String[] arg) {
      for(int i = 0; i < arg.length; ++i) {
         this.arguments = this.arguments + " " + arg[i];
      }

      this.fileName = System.getProperty("fileName");
      this.scriptTempFile = System.getProperty("scriptTempFile");
      if (this.scriptTempFile == null) {
         String error;
         if (this.fileName == null) {
            error = "Error: The fileName attribute is required if no nested script is used.";
            this.printError(error);
         }

         if (this.fileName != null && !(new File(this.fileName)).exists()) {
            error = "Error: File specified " + this.fileName + " does not exist.";
            this.printError(error);
         }
      }

      this.failOnError = Boolean.valueOf(System.getProperty("failOnError"));
      this.debug = Boolean.valueOf(System.getProperty("debug"));
      this.executeScriptBeforeFile = Boolean.valueOf(System.getProperty("executeScriptBeforeFile"));
      this.propertiesFile = System.getProperty("propertiesFile");
   }

   private void executePyScript() {
      try {
         WLSTInterpreter interp = new WLSTInterpreter();
         if (this.propertiesFile != null) {
            WLSTUtil.setProperties(this.propertiesFile, interp);
            this.printDebug("Loaded and set the properties from " + this.propertiesFile);
         }

         PyList sysArgs = new PyList();
         if (this.fileName != null) {
            sysArgs.append(new PyString(this.fileName));
         }

         if (this.arguments != null) {
            String[] args = StringUtils.splitCompletely(this.arguments, " ");

            for(int i = 0; i < args.length; ++i) {
               this.printDebug("Adding " + args[i] + " to sys.argv");
               sysArgs.append(new PyString(args[i].trim()));
            }

            interp.exec("sys.argv=" + sysArgs);
            this.printDebug("sys.argv is " + sysArgs);
         }

         File f;
         if (this.fileName != null) {
            f = new File(this.fileName);
            if (!f.exists()) {
               String error = "File specified " + this.fileName + " does not exist";
               this.printError(error);
            }
         }

         if (this.executeScriptBeforeFile) {
            if (this.scriptTempFile != null) {
               f = new File(this.scriptTempFile);
               interp.execfile(f.getPath());
            }

            if (this.fileName != null) {
               f = new File(this.fileName);
               interp.execfile(f.getPath());
            }
         } else {
            if (this.fileName != null) {
               f = new File(this.fileName);
               interp.execfile(f.getPath());
            }

            if (this.scriptTempFile != null) {
               f = new File(this.scriptTempFile);
               interp.execfile(f.getPath());
            }
         }
      } catch (Exception var5) {
         this.printError(var5.toString());
      }

   }

   private void printError(String error) {
      System.out.println(error);
      if (this.failOnError) {
         throw new IllegalStateException(error);
      }
   }

   private void printDebug(String s) {
      if (this.debug) {
         System.out.println("<WLSTTask> " + s);
      }

   }
}
