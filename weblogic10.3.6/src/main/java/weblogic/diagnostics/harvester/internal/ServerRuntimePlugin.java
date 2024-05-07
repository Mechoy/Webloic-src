package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.mbean.typing.MBeanCategorizerPlugins;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

class ServerRuntimePlugin extends MBeanCategorizerPlugins.WLSPlugin {
   public boolean handles(MBeanServerConnection var1, ObjectName var2) {
      try {
         return var1.isInstanceOf(var2, "weblogic.management.runtime.RuntimeMBean");
      } catch (InstanceNotFoundException var4) {
         return false;
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }
}
