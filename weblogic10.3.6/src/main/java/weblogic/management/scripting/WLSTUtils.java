package weblogic.management.scripting;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import weblogic.management.VersionConstants;
import weblogic.management.scripting.utils.ErrorInformation;
import weblogic.utils.io.TerminalIO;

public class WLSTUtils extends WLScriptConstants {
   boolean isExecutingFromDomainDir() {
      boolean saltFileExists = false;
      boolean configFileExists = false;
      File saltFile = new File("./security/SerializedSystemIni.dat");
      if (saltFile.exists()) {
         saltFileExists = true;
      }

      File configFile = new File("./config/config.xml");
      if (configFile.exists()) {
         configFileExists = true;
      }

      return saltFileExists && configFileExists;
   }

   String getRightType(String mbeanType) {
      if (mbeanType.endsWith("ies")) {
         mbeanType = mbeanType.substring(0, mbeanType.length() - 3) + "y";
      } else if (mbeanType.endsWith("sses")) {
         mbeanType = mbeanType.substring(0, mbeanType.length() - 2);
      } else if (mbeanType.endsWith("s") && !mbeanType.endsWith("ss") && !mbeanType.equals("WTCResources")) {
         mbeanType = mbeanType.substring(0, mbeanType.length() - 1);
      }

      return mbeanType;
   }

   String getPlural(String mbeanType) {
      if (mbeanType.endsWith("y")) {
         mbeanType = mbeanType.substring(0, mbeanType.length() - 1) + "ies";
         return mbeanType;
      } else if (mbeanType.endsWith("ss")) {
         mbeanType = mbeanType + "es";
         return mbeanType;
      } else {
         return !mbeanType.endsWith("s") ? mbeanType + "s" : mbeanType;
      }
   }

   boolean isPlural(String mbeanType) {
      if (mbeanType.endsWith("ies")) {
         return true;
      } else if (mbeanType.endsWith("sses")) {
         return true;
      } else {
         return mbeanType.endsWith("s") && !mbeanType.endsWith("ss");
      }
   }

   boolean isNewFormat(InputStream is) throws ScriptException {
      boolean isNewFormat = false;

      try {
         String line = null;
         BufferedReader in = new BufferedReader(new InputStreamReader(is));

         while(true) {
            while((line = in.readLine()) != null && !isNewFormat) {
               for(int i = 0; i < VersionConstants.KNOWN_NAMESPACE_PREFIXES.length; ++i) {
                  if (line.indexOf(VersionConstants.KNOWN_NAMESPACE_PREFIXES[i]) != -1) {
                     isNewFormat = true;
                     break;
                  }
               }
            }

            return isNewFormat;
         }
      } catch (IOException var6) {
         this.throwWLSTException("Unable to determine configuration format ", var6);
         return isNewFormat;
      }
   }

   public String promptValue(String text_prompt, boolean echo) {
      String returnValue = null;

      try {
         this.print(text_prompt);
         if (!echo && TerminalIO.isNoEchoAvailable()) {
            try {
               returnValue = TerminalIO.readTerminalNoEcho();
               if (returnValue != null) {
                  byte[] b = returnValue.getBytes();
                  if (b.length == 0 || b[0] == 13 && b[1] == 10) {
                     returnValue = "";
                  }
               }

               this.println("");
            } catch (Error var5) {
               System.err.println("Error: Failed to get value from Standard Input: " + var5.getMessage());
            }
         } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            returnValue = reader.readLine();
         }
      } catch (Exception var6) {
         System.err.println("Error: Failed to get value from Standard Input: " + var6.getMessage());
      }

      return returnValue;
   }

   String getListenAddress(String url) {
      int i = url.indexOf("//");
      int j = url.lastIndexOf(":");
      String addr = url.substring(i + 2, j);
      return addr;
   }

   String getListenPort(String url) {
      int j = url.lastIndexOf(":");
      String port = url.substring(j + 1, url.length());
      return port;
   }

   String getURL(String url) {
      int i = url.indexOf("//");
      if (i > 0 && url.charAt(i - 1) == ':') {
         return url;
      } else {
         return i == 0 ? "t3:" + url : "t3://" + url;
      }
   }

   String getProtocol(String url) {
      int i = url.indexOf("://");
      return i > 0 ? url.substring(0, i) : "t3";
   }

   void throwWLSTException(String msg, Throwable th) throws ScriptException {
      this.errorMsg = msg;
      this.errorInfo = new ErrorInformation(th, this.errorMsg);
      this.exceptionHandler.handleException(this.errorInfo);
   }

   void throwWLSTException(String msg) throws ScriptException {
      this.errorMsg = msg;
      this.errorInfo = new ErrorInformation(this.errorMsg);
      this.exceptionHandler.handleException(this.errorInfo);
   }
}
