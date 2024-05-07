package weblogic.management.mbeanservers;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
import weblogic.management.mbeanservers.internal.SecurityInterceptor;

public final class SecurityUtil {
   public static final int DOMAIN_RUNTIME = 1;
   public static final int RUNTIME = 2;

   public static boolean isGetAccessAllowed(int var0, ObjectName var1, String var2) throws AttributeNotFoundException, InstanceNotFoundException {
      String var3 = null;
      switch (var0) {
         case 1:
            var3 = "weblogic.management.mbeanservers.domainruntime";
            break;
         case 2:
            var3 = "weblogic.management.mbeanservers.runtime";
            break;
         default:
            throw new IllegalArgumentException("Unknown MBean Server type");
      }

      return SecurityInterceptor.isGetAccessAllowed(var3, var1, var2);
   }
}
