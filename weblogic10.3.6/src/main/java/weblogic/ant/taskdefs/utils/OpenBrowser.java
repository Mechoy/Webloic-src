package weblogic.ant.taskdefs.utils;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class OpenBrowser extends Task {
   private String url = "";
   private String unixBrowser = "";
   private static final String WIN_ID = "Windows";
   private static final String WIN_PATH = "rundll32";
   private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
   private static final String UNIX_DEFAULT_BROWSER = "netscape";
   private static final String UNIX_FLAG = "-remote openURL";

   public void setUrl(String var1) {
      this.url = var1;
   }

   public void setUnixBrowser(String var1) {
      this.unixBrowser = var1;
   }

   public void execute() throws BuildException {
      boolean var1 = isWindowsPlatform();
      String var2 = null;

      try {
         if (var1) {
            var2 = "rundll32 url.dll,FileProtocolHandler " + this.url;
            Process var3 = Runtime.getRuntime().exec(var2);
         } else {
            String var8 = this.unixBrowser != null && !this.unixBrowser.equals("") ? this.unixBrowser : "netscape";
            var2 = var8 + " " + "-remote openURL" + "(" + this.url + ")";
            Process var4 = Runtime.getRuntime().exec(var2);

            try {
               int var5 = var4.waitFor();
               if (var5 != 0) {
                  var2 = var8 + " " + this.url;
                  var4 = Runtime.getRuntime().exec(var2);
               }
            } catch (InterruptedException var6) {
               throw new BuildException("Error bringing up browser, cmd='" + var2 + "'.  Please make sure that 'netscape' can open (use unixBrowser attribute to change browser on Unix, ie unixBrowser=\"mozilla\").\n" + var6);
            }
         }

      } catch (IOException var7) {
         throw new BuildException("Could not invoke browser, command=" + var2 + "'.  Windows: Please make sure that default browser can open.  Unix: Please make sure that 'netscape' can open (use unixBrowser attribute to change browser on Unix, ie unixBrowser=\"mozilla\").\n" + var7);
      }
   }

   public static boolean isWindowsPlatform() {
      String var0 = System.getProperty("os.name");
      return var0 != null && var0.startsWith("Windows");
   }
}
