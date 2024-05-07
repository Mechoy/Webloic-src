package weblogic.wsee.policy.mbean;

import weblogic.management.ManagementException;
import weblogic.management.runtime.OwsmSecurityPolicyRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.wsee.monitoring.OwsmSecurityPolicyRuntimeData;
import weblogic.wsee.monitoring.WseeBaseRuntimeData;
import weblogic.wsee.monitoring.WseeRuntimeMBeanDelegate;

public class OwsmSecurityPolicyRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<OwsmSecurityPolicyRuntimeMBean, OwsmSecurityPolicyRuntimeData> implements OwsmSecurityPolicyRuntimeMBean {
   public OwsmSecurityPolicyRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   public OwsmSecurityPolicyRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      WseeBaseRuntimeData var3 = null;
      if (var2 instanceof WseeRuntimeMBeanDelegate) {
         var3 = ((WseeRuntimeMBeanDelegate)var2).getData();
      }

      OwsmSecurityPolicyRuntimeData var4 = new OwsmSecurityPolicyRuntimeData(var1, var3);
      this.setData(var4);
   }

   public static OwsmSecurityPolicyRuntimeMBean create(String var0, RuntimeMBean var1) throws ManagementException {
      OwsmSecurityPolicyRuntimeMBeanImpl var2 = new OwsmSecurityPolicyRuntimeMBeanImpl(var0, var1);
      var2.register();
      return var2;
   }

   protected WseeRuntimeMBeanDelegate<OwsmSecurityPolicyRuntimeMBean, OwsmSecurityPolicyRuntimeData> internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      return new OwsmSecurityPolicyRuntimeMBeanImpl(var1, var2, this);
   }

   public String[] getAvailablePolicies() throws ManagementException {
      return ((OwsmSecurityPolicyRuntimeData)this.getData()).getAvailablePolicies();
   }
}
