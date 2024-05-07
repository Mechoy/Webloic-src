package weblogic.wsee.monitoring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeOperationConfigurationRuntimeMBean;
import weblogic.management.runtime.WseePortConfigurationRuntimeMBean;
import weblogic.t3.srvr.ServerRuntime;

public class WseePortConfigurationRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseePortConfigurationRuntimeMBean, WseePortConfigurationRuntimeData> implements WseePortConfigurationRuntimeMBean {
   private Set<WseeOperationConfigurationRuntimeMBeanImpl> operations = new HashSet();

   public WseePortConfigurationRuntimeMBeanImpl(String var1, String var2) throws ManagementException {
      super(var1, (RuntimeMBean)null, (WseeRuntimeMBeanDelegate)null, false);
      this.setData(new WseePortConfigurationRuntimeData(var1, var2));
   }

   private WseePortConfigurationRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate<WseePortConfigurationRuntimeMBean, WseePortConfigurationRuntimeData> var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   void addOperation(WseeOperationConfigurationRuntimeMBeanImpl var1) {
      var1.setParent(this);
      this.operations.add(var1);
      if (this.isRegistered()) {
         try {
            var1.register();
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }

   }

   protected WseeRuntimeMBeanDelegate<WseePortConfigurationRuntimeMBean, WseePortConfigurationRuntimeData> internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseePortConfigurationRuntimeMBeanImpl var3 = new WseePortConfigurationRuntimeMBeanImpl(var1, var2, this);
      var3.setData(this.getData());
      Iterator var4 = this.operations.iterator();

      while(var4.hasNext()) {
         WseeOperationConfigurationRuntimeMBeanImpl var5 = (WseeOperationConfigurationRuntimeMBeanImpl)var4.next();
         WseeOperationConfigurationRuntimeMBeanImpl var6 = (WseeOperationConfigurationRuntimeMBeanImpl)var5.createProxy(var1, var3);
         var3.addOperation(var6);
      }

      return var3;
   }

   public WseeOperationConfigurationRuntimeMBean[] getOperations() {
      return (WseeOperationConfigurationRuntimeMBean[])this.operations.toArray(new WseeOperationConfigurationRuntimeMBean[this.operations.size()]);
   }

   public String getPolicyAttachmentSupport() {
      return "binding.client.soap.http";
   }

   public String getPolicySubjectName() {
      return ((WseePortConfigurationRuntimeData)this.getData()).getSubjectName();
   }

   public String getPolicySubjectResourcePattern() {
      return ((WseePortConfigurationRuntimeData)this.getData()).getResourcePattern();
   }

   public String getPolicySubjectType() {
      return "WLSWSCLIENT";
   }

   public void register() throws ManagementException {
      if (!this.isRegistered()) {
         super.register();
         Iterator var1 = this.operations.iterator();

         while(var1.hasNext()) {
            WseeOperationConfigurationRuntimeMBeanImpl var2 = (WseeOperationConfigurationRuntimeMBeanImpl)var1.next();
            var2.register();
         }

      }
   }

   public void unregister() throws ManagementException {
      super.unregister();
      Iterator var1 = this.operations.iterator();

      while(var1.hasNext()) {
         WseeOperationConfigurationRuntimeMBeanImpl var2 = (WseeOperationConfigurationRuntimeMBeanImpl)var1.next();

         try {
            var2.unregister();
            ServerRuntime.theOne().removeChild(var2);
         } catch (ManagementException var4) {
            var4.printStackTrace();
         }
      }

      this.operations.clear();
      this.operations = null;
   }
}
