package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeOperationConfigurationRuntimeMBean;

public final class WseeOperationConfigurationRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseeOperationConfigurationRuntimeMBean, WseeOperationConfigurationRuntimeData> implements WseeOperationConfigurationRuntimeMBean {
   public WseeOperationConfigurationRuntimeMBeanImpl(String var1, WseePortConfigurationRuntimeMBeanImpl var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      this.setData(new WseeOperationConfigurationRuntimeData(var1, (WseePortConfigurationRuntimeData)var2.getData()));
   }

   private WseeOperationConfigurationRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeOperationConfigurationRuntimeMBeanImpl var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   protected WseeRuntimeMBeanDelegate<WseeOperationConfigurationRuntimeMBean, WseeOperationConfigurationRuntimeData> internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      return new WseeOperationConfigurationRuntimeMBeanImpl(var1, var2, this);
   }

   public String getPolicyAttachmentSupport() {
      return "binding.client.soap.http";
   }

   public String getPolicySubjectName() {
      return ((WseeOperationConfigurationRuntimeData)this.getData()).getSubjectName();
   }

   public String getPolicySubjectResourcePattern() {
      return ((WseeOperationConfigurationRuntimeData)this.getData()).getResourcePattern();
   }

   public String getPolicySubjectType() {
      return "WLSWSCLIENT";
   }
}
