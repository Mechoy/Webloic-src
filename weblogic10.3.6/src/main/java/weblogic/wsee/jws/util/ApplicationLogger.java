package weblogic.wsee.jws.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import weblogic.logging.NonCatalogLogger;

public class ApplicationLogger implements Logger {
   private static String WL_SUBSYS = "WLW";
   private NonCatalogLogger _wl = null;

   public ApplicationLogger(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this._wl = new NonCatalogLogger(WL_SUBSYS);
      }
   }

   public void debug(String var1) {
      this._wl.debug(var1);
   }

   public void debug(String var1, Throwable var2) {
      String var3 = this.format(var1, var2);
      this._wl.debug(var3);
   }

   public void info(String var1) {
      this._wl.info(var1);
   }

   public void info(String var1, Throwable var2) {
      String var3 = this.format(var1, var2);
      this._wl.info(var3);
   }

   public void warn(String var1) {
      this._wl.warning(var1);
   }

   public void warn(String var1, Throwable var2) {
      String var3 = this.format(var1, var2);
      this._wl.warning(var3);
   }

   public void error(String var1) {
      this._wl.error(var1);
   }

   public void error(String var1, Throwable var2) {
      String var3 = this.format(var1, var2);
      this._wl.error(var3);
   }

   public final boolean isDebugEnabled() {
      return true;
   }

   public final boolean isInfoEnabled() {
      return true;
   }

   public final boolean isErrorEnabled() {
      return true;
   }

   public final boolean isWarnEnabled() {
      return true;
   }

   private String format(String var1, Throwable var2) {
      StringWriter var3 = new StringWriter();
      var2.printStackTrace(new PrintWriter(var3));
      return var1 + "\n\n" + "Throwable: " + var2.toString() + "\nStack Trace:\n" + var3.toString();
   }
}
