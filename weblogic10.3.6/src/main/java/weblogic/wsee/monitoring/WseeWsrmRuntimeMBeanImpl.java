package weblogic.wsee.monitoring;

import java.util.List;
import javax.management.openmbean.CompositeData;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.WseeWsrmRuntimeMBean;

public final class WseeWsrmRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseeWsrmRuntimeMBean, WseeWsrmRuntimeData> implements WseeWsrmRuntimeMBean {
   public WseeWsrmRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   public WseeWsrmRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      boolean var3 = var2 instanceof ServerRuntimeMBean;
      WseeWsrmRuntimeData var4;
      if (var3) {
         var4 = new WseeWsrmRuntimeData(var1, var3, (WseeBaseRuntimeData)null);
      } else {
         var4 = new WseeWsrmRuntimeData(var1, var3, ((WseeBasePortRuntimeMBeanImpl)var2).getData());
      }

      this.setData(var4);
   }

   protected WseeWsrmRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeWsrmRuntimeMBeanImpl var3 = new WseeWsrmRuntimeMBeanImpl(var1, var2, this);
      return var3;
   }

   public void addSequenceId(String var1) {
      ((WseeWsrmRuntimeData)this.getData()).addSequenceId(var1);
   }

   public void removeSequenceId(String var1) {
      ((WseeWsrmRuntimeData)this.getData()).removeSequenceId(var1);
   }

   public List<String> getSequenceIDList() {
      return ((WseeWsrmRuntimeData)this.getData()).getSequenceIDList();
   }

   public String[] getSequenceIds() {
      return ((WseeWsrmRuntimeData)this.getData()).getSequenceIds();
   }

   public CompositeData getSequenceInfo(String var1) throws ManagementException {
      return ((WseeWsrmRuntimeData)this.getData()).getSequenceInfo(var1);
   }
}
