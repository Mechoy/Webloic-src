package weblogic.ejb.container.cmp11.rdbms.finders;

import java.lang.reflect.Method;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.logging.Loggable;

public final class FinderNotFoundException extends Exception {
   private static final long serialVersionUID = -3377809480870557550L;
   private Method failedMethod;
   private String ejbName = null;
   private String fileName = null;

   public FinderNotFoundException(Method var1, String var2, String var3) {
      this.failedMethod = var1;
      this.ejbName = var2;
      this.fileName = var3;
   }

   public String getMessage() {
      String var1 = DDUtils.getMethodSignature(this.failedMethod);
      Loggable var2 = EJBLogger.logfinderNotFound11MessageLoggable(var1, this.ejbName, this.fileName);
      return var2.getMessage();
   }
}
