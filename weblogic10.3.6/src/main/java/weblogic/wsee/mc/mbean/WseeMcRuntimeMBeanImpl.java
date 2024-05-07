package weblogic.wsee.mc.mbean;

import java.util.List;
import javax.management.openmbean.CompositeData;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeMcRuntimeMBean;
import weblogic.wsee.monitoring.WseeRuntimeMBeanDelegate;

public class WseeMcRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseeMcRuntimeMBean, WseeMcRuntimeData> implements WseeMcRuntimeMBean {
   public WseeMcRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
      WseeMcRuntimeData var4 = new WseeMcRuntimeData(var1);
      this.setData(var4);
   }

   public WseeMcRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      WseeMcRuntimeData var3 = new WseeMcRuntimeData(var1);
      this.setData(var3);
   }

   protected WseeMcRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      return new WseeMcRuntimeMBeanImpl(var1, var2, this);
   }

   public void addAnonymousEndpointId(String var1) {
      ((WseeMcRuntimeData)this.getData()).addMcAnonId(var1);
   }

   public void removeAnonymousEndpointId(String var1) {
      ((WseeMcRuntimeData)this.getData()).removeMcAnonId(var1);
   }

   public List<String> getAnonymousEndpointIds() {
      return ((WseeMcRuntimeData)this.getData()).getMcAnonymousIds();
   }

   public CompositeData getAnonymousEndpointInfo(String var1) throws ManagementException {
      return ((WseeMcRuntimeData)this.getData()).getIdInfo(var1);
   }
}
