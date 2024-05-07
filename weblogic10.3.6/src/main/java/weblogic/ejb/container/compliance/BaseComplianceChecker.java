package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import weblogic.utils.Debug;

public abstract class BaseComplianceChecker {
   protected static Log log;
   protected EJBComplianceTextFormatter fmt;

   public BaseComplianceChecker() {
      log = new Log();
      this.fmt = new EJBComplianceTextFormatter();
   }

   protected String methodSig(String var1) {
      return this.methodSig(var1, (Class[])null);
   }

   protected String methodSig(Method var1) {
      String var2 = null;
      Class[] var3 = null;
      if (null != var1) {
         var2 = var1.getName();
         var3 = var1.getParameterTypes();
      }

      return this.methodSig(var2, var3);
   }

   protected String methodSig(String var1, Class[] var2) {
      Debug.assertion(var1 != null);
      StringBuffer var3 = new StringBuffer();
      var3.append(var1 + "(");
      if (var2 != null && var2.length > 0) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var4 != 0) {
               var3.append(",");
            }

            var3.append(var2[var4].getName());
         }
      }

      var3.append(")");
      return var3.toString();
   }
}
