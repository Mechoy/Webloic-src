package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeBaseRuntimeMBean;
import weblogic.management.runtime.WseeRuntimeMBean;

public final class WseeRuntimeMBeanImpl extends WseeBaseRuntimeMBeanImpl implements WseeRuntimeMBean {
   public WseeRuntimeMBeanImpl() throws ManagementException {
   }

   public WseeRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeV2RuntimeMBeanImpl var3) throws ManagementException {
      super(var1, var2, var3);
      if (var2 instanceof ApplicationRuntimeMBean) {
         ((ApplicationRuntimeMBean)var2).addWseeRuntime(this);
      }

   }

   public WseeRuntimeMBeanImpl(String var1, RuntimeMBean var2, String var3, String var4, String var5, String var6, String var7) throws ManagementException {
      super(var1, var2, (String)null, var3, var4, var5, var6, var7, false);
      if (var2 instanceof ApplicationRuntimeMBean) {
         ((ApplicationRuntimeMBean)var2).addWseeRuntime(this);
      }

   }

   void setServiceName(String var1) {
      this.serviceName = var1;
   }

   protected WseeRuntimeMBeanDelegate<WseeBaseRuntimeMBean, WseeRuntimeData> internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      throw new UnsupportedOperationException("Should never create proxy from WseeRuntimeMBean");
   }
}
