package weblogic.ejb.container.cmp11.rdbms;

import java.util.Iterator;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.PlatformConstants;

public final class InvalidBeanException extends ErrorCollectionException implements PlatformConstants {
   private static final long serialVersionUID = -4596225274545228810L;

   public InvalidBeanException() {
      super("Invalid specifications for a WebLogic RDBMS CMP EJB.");
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer();
      var1.append(" Bean provided to WebLogic RDBMS CMP system is invalid." + PlatformConstants.EOL);
      var1.append("Please examine the following exceptions for specific problems: " + PlatformConstants.EOL);
      Iterator var2 = this.getExceptions().iterator();

      for(int var3 = 0; var2.hasNext(); ++var3) {
         Exception var4 = (Exception)var2.next();
         var1.append("" + var3 + ":");
         String var5 = var4.getMessage();
         if (var5 != null) {
            var1.append("  " + var5 + PlatformConstants.EOL);
         } else {
            var1.append(" " + var4.toString() + PlatformConstants.EOL);
         }
      }

      return var1.toString();
   }
}
