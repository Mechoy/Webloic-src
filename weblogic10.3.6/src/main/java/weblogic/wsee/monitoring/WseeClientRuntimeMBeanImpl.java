package weblogic.wsee.monitoring;

import java.util.logging.Logger;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.management.runtime.WseeClientPortRuntimeMBean;
import weblogic.management.runtime.WseeClientRuntimeMBean;
import weblogic.servlet.internal.WebAppRuntimeMBeanImpl;
import weblogic.wsee.jaxws.MonitoringStatMap;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;

public final class WseeClientRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseeClientRuntimeMBean, WseeClientRuntimeData> implements WseeClientRuntimeMBean {
   private static Logger LOGGER = Logger.getLogger(WseeClientRuntimeMBeanImpl.class.getName());
   private WseeClientPortRuntimeMBeanImpl _port;
   private MonitoringStatMap _statMap;

   public WseeClientRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   public WseeClientRuntimeMBeanImpl(String var1, String var2, RuntimeMBean var3) throws ManagementException {
      super(var1, var3, (WseeRuntimeMBeanDelegate)null, false);
      if (var3 instanceof WebAppComponentRuntimeMBean) {
         ((WebAppRuntimeMBeanImpl)var3).addWseeClientRuntime(this);
      } else if (var3 instanceof EJBComponentRuntimeMBean) {
         ((EJBComponentRuntimeMBeanImpl)var3).addWseeClientRuntime(this);
      }

      WseeClientRuntimeData var4 = new WseeClientRuntimeData(var1, var2);
      this.setData(var4);
   }

   public void register() throws ManagementException {
      if (!this.isRegistered()) {
         super.register();
         if (this._port != null) {
            this._port.register();
         }

      }
   }

   protected WseeRuntimeMBeanDelegate<WseeClientRuntimeMBean, WseeClientRuntimeData> internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeClientRuntimeMBeanImpl var3 = new WseeClientRuntimeMBeanImpl(var1, var2, this);
      WseeClientPortRuntimeMBeanImpl var4 = (WseeClientPortRuntimeMBeanImpl)this._port.createProxy(var1, var2);
      var3.setPort(var4);
      var3.setStatMap(this._statMap);
      return var3;
   }

   public void unregister() throws ManagementException {
      super.unregister();
      ClientIdentityRegistry.removeClientRuntimeMBean(this.getClientID());
      if (this._port != null) {
         this._port.unregister();
         this._port = null;
      }

      if (this.parent instanceof WebAppComponentRuntimeMBean) {
         ((WebAppRuntimeMBeanImpl)this.parent).removeWseeClientRuntime(this);
      } else if (this.parent instanceof EJBComponentRuntimeMBean) {
         ((EJBComponentRuntimeMBeanImpl)this.parent).removeWseeClientRuntime(this);
      }

   }

   public WseeClientPortRuntimeMBean getPort() {
      return this._port;
   }

   public void setPort(WseeClientPortRuntimeMBeanImpl var1) {
      this._port = var1;
      this._port.setParent(this);
      ((WseeClientRuntimeData)this.getData()).setPort((WseeClientPortRuntimeData)var1.getData());
      if (this.isRegistered()) {
         try {
            var1.register();
         } catch (Exception var3) {
            throw new RuntimeException(var3.toString(), var3);
         }
      }

      this.setStatMap(new MonitoringStatMap(this));
   }

   public String getClientID() {
      return ((WseeClientRuntimeData)this.getData()).getClientId();
   }

   private void setStatMap(MonitoringStatMap var1) {
      this._statMap = var1;
   }

   public MonitoringStatMap getStatMap() {
      return this._statMap;
   }
}
