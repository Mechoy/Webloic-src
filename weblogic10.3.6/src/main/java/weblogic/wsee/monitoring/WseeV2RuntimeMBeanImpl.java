package weblogic.wsee.monitoring;

import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.servlet.internal.WebAppRuntimeMBeanImpl;

public class WseeV2RuntimeMBeanImpl extends WseeBaseRuntimeMBeanImpl implements WseeV2RuntimeMBean {
   public WseeV2RuntimeMBeanImpl() throws ManagementException {
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseeV2RuntimeMBeanImpl(String var1, RuntimeMBean var2, String var3, String var4, String var5, String var6, String var7, String var8) throws ManagementException {
      super(var1, var2, var3, var4, var5, var6, var7, var8, true);
   }

   protected WseeRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeRuntimeMBeanImpl var3 = new WseeRuntimeMBeanImpl(var1, var2, this);
      return (WseeRuntimeMBeanImpl)this.internalInitProxy(var3);
   }

   public String getContextPath() {
      return ((WseeRuntimeData)this.getData()).getContextPath();
   }

   public void unregister() throws ManagementException {
      super.unregister();
      if (this.parent instanceof WebAppComponentRuntimeMBean) {
         ((WebAppRuntimeMBeanImpl)this.parent).removeWseeV2Runtime(this);
      } else if (this.parent instanceof EJBComponentRuntimeMBean) {
         ((EJBComponentRuntimeMBeanImpl)this.parent).removeWseeV2Runtime(this);
      } else if (this.parent instanceof J2EEApplicationRuntimeMBeanImpl) {
         ((J2EEApplicationRuntimeMBeanImpl)this.parent).removeWseeV2Runtime(this);
      }

   }
}
