package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.util.HashMap;
import weblogic.management.runtime.WseeBaseRuntimeMBean;
import weblogic.management.runtime.WseeClientOperationRuntimeMBean;
import weblogic.management.runtime.WseeClientPortRuntimeMBean;
import weblogic.management.runtime.WseeClientRuntimeMBean;
import weblogic.management.runtime.WseeOperationRuntimeMBean;
import weblogic.management.runtime.WseePortRuntimeMBean;
import weblogic.wsee.monitoring.OperationStats;

public class MonitoringStatMap extends HashMap<String, OperationStats> {
   private static final long serialVersionUID = 8442249201205998700L;

   public MonitoringStatMap(WseeBaseRuntimeMBean var1, WSEndpoint<?> var2) {
      WseePortRuntimeMBean var3 = var1 != null ? this.getPort(var1, var2.getPort()) : null;
      if (var3 != null) {
         WseeOperationRuntimeMBean[] var4 = var3.getOperations();
         WseeOperationRuntimeMBean[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            WseeOperationRuntimeMBean var8 = var5[var7];
            this.put(var8.getOperationName(), (OperationStats)var8);
         }
      }

   }

   public MonitoringStatMap(WseeClientRuntimeMBean var1) {
      WseeClientPortRuntimeMBean var2 = var1.getPort();
      if (var2 != null) {
         WseeClientOperationRuntimeMBean[] var3 = var2.getOperations();
         WseeClientOperationRuntimeMBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            WseeClientOperationRuntimeMBean var7 = var4[var6];
            this.put(var7.getOperationName(), (OperationStats)var7);
         }
      }

   }

   private WseePortRuntimeMBean getPort(WseeBaseRuntimeMBean var1, WSDLPort var2) {
      if (var2 != null) {
         WseePortRuntimeMBean[] var3 = var1.getPorts();
         WseePortRuntimeMBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            WseePortRuntimeMBean var7 = var4[var6];
            if (var7.getPortName().equals(var2.getName().getLocalPart())) {
               return var7;
            }
         }
      }

      return null;
   }
}
