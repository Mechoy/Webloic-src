package weblogic.wsee.deploy;

import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.WsdlBean;

public class DeployInfoUtil {
   public static boolean exposeWsdl(DeployInfo var0) {
      PortComponentBean var1 = var0.getWlPortComp();
      if (var1 != null) {
         WsdlBean var2 = var1.getWsdl();
         if (var2 != null && !var2.isExposed()) {
            return false;
         }
      }

      return true;
   }
}
