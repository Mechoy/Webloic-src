package weblogic.ejb.container;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public final class EJBUnDeploymentException extends RemoteException {
   private static final long serialVersionUID = 1326360028294935864L;

   public EJBUnDeploymentException(String var1) {
      super(var1);
   }

   public EJBUnDeploymentException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      if (this.detail != null) {
         Localizer var2 = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.ejb.container.EJBTextTextLocalizer");
         var1.println(var2.get("NestedException"));
         this.detail.printStackTrace(var1);
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("weblogic.ejb.container.UnDeploymentException: ");
      var1.append(this.getMessage());
      return var1.toString();
   }
}
