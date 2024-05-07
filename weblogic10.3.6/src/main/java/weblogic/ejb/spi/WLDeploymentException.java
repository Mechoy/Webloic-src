package weblogic.ejb.spi;

import java.io.PrintWriter;
import java.util.Locale;
import weblogic.ejb.container.EJBDebugService;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StackTraceUtils;

public final class WLDeploymentException extends Exception {
   private static final long serialVersionUID = 570535192182504339L;
   private String msg;
   private Throwable th;

   public WLDeploymentException(String var1) {
      super(var1);
      this.msg = var1;
   }

   public WLDeploymentException(String var1, Throwable var2) {
      super(var1);
      this.msg = var1;
      this.th = var2;
   }

   public Throwable getEmbeddedThrowable() {
      return this.th;
   }

   public String getErrorMessage() {
      return EJBDebugService.deploymentLogger.isDebugEnabled() && this.th != null ? this.msg + PlatformConstants.EOL + StackTraceUtils.throwable2StackTrace(this.th) : this.msg;
   }

   public String toString() {
      return this.th != null ? this.msg + " NestedException Message is :" + this.th.getMessage() : this.msg;
   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      if (this.th != null) {
         Localizer var2 = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.ejb.container.EJBTextTextLocalizer");
         var1.println(var2.get("NestedException"));
         this.th.printStackTrace(var1);
      }

   }
}
