package weblogic.wsee.monitoring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.management.runtime.WseeClientConfigurationRuntimeMBean;
import weblogic.management.runtime.WseePortConfigurationRuntimeMBean;
import weblogic.servlet.internal.WebAppRuntimeMBeanImpl;
import weblogic.t3.srvr.ServerRuntime;

public class WseeClientConfigurationRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseeClientConfigurationRuntimeMBean, WseeClientConfigurationRuntimeData> implements WseeClientConfigurationRuntimeMBean {
   private Set<WseePortConfigurationRuntimeMBeanImpl> ports = new HashSet();

   public WseeClientConfigurationRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      this.setData(new WseeClientConfigurationRuntimeData(var1));
      if (var2 instanceof WebAppComponentRuntimeMBean) {
         ((WebAppRuntimeMBeanImpl)var2).addWseeClientConfigurationRuntime(this);
      } else if (var2 instanceof EJBComponentRuntimeMBean) {
         ((EJBComponentRuntimeMBeanImpl)var2).addWseeClientConfigurationRuntime(this);
      }

   }

   private WseeClientConfigurationRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate<WseeClientConfigurationRuntimeMBean, WseeClientConfigurationRuntimeData> var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   void addPort(WseePortConfigurationRuntimeMBeanImpl var1) {
      var1.setParent(this);
      this.ports.add(var1);
      if (this.isRegistered()) {
         try {
            var1.register();
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }

   }

   public void register() throws ManagementException {
      if (!this.isRegistered()) {
         super.register();
         Iterator var1 = this.ports.iterator();

         while(var1.hasNext()) {
            WseePortConfigurationRuntimeMBeanImpl var2 = (WseePortConfigurationRuntimeMBeanImpl)var1.next();
            var2.register();
         }

      }
   }

   public void unregister() throws ManagementException {
      super.unregister();
      Iterator var1 = this.ports.iterator();

      while(var1.hasNext()) {
         WseePortConfigurationRuntimeMBeanImpl var2 = (WseePortConfigurationRuntimeMBeanImpl)var1.next();
         var2.unregister();
         ServerRuntime.theOne().removeChild(var2);
      }

      if (this.parent instanceof WebAppComponentRuntimeMBean) {
         ((WebAppRuntimeMBeanImpl)this.parent).removeWseeClientConfigurationRuntime(this);
      } else if (this.parent instanceof EJBComponentRuntimeMBean) {
         ((EJBComponentRuntimeMBeanImpl)this.parent).removeWseeClientConfigurationRuntime(this);
      }

      ServerRuntime.theOne().removeChild(this);
   }

   protected WseeRuntimeMBeanDelegate<WseeClientConfigurationRuntimeMBean, WseeClientConfigurationRuntimeData> internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeClientConfigurationRuntimeMBeanImpl var3 = new WseeClientConfigurationRuntimeMBeanImpl(var1, var2, this);
      var3.setData(this.getData());
      Iterator var4 = this.ports.iterator();

      while(var4.hasNext()) {
         WseePortConfigurationRuntimeMBeanImpl var5 = (WseePortConfigurationRuntimeMBeanImpl)var4.next();
         WseePortConfigurationRuntimeMBeanImpl var6 = (WseePortConfigurationRuntimeMBeanImpl)var5.createProxy(var1, var3);
         var3.addPort(var6);
      }

      return var3;
   }

   public WseePortConfigurationRuntimeMBean[] getPorts() {
      return (WseePortConfigurationRuntimeMBean[])this.ports.toArray(new WseePortConfigurationRuntimeMBeanImpl[this.ports.size()]);
   }

   public String getServiceReferenceName() {
      return ((WseeClientConfigurationRuntimeData)this.getData()).getServiceRefName();
   }
}
