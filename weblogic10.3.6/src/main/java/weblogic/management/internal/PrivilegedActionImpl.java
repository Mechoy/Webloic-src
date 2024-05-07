package weblogic.management.internal;

import java.security.PrivilegedAction;
import javax.management.ObjectName;
import weblogic.management.ManagementLogger;

class PrivilegedActionImpl implements PrivilegedAction {
   ObjectName name;
   String action;
   String target;
   String methodName;

   PrivilegedActionImpl(ObjectName var1, String var2, String var3, String var4) {
      this.name = var1;
      this.action = var2;
      this.target = var3;
      this.methodName = var4;
   }

   public Object run() {
      if (this.target == null) {
         ManagementLogger.logNoMBeanAccess(this.name, this.action, this.methodName);
      } else {
         ManagementLogger.logNoAccess(this.name, this.action, this.target, this.methodName);
      }

      return null;
   }
}
