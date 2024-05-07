package weblogic.wsee.policy.mbean;

import java.util.Map;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseePolicyRuntimeMBean;
import weblogic.wsee.monitoring.WseeBaseRuntimeData;
import weblogic.wsee.monitoring.WseePolicyRuntimeData;
import weblogic.wsee.monitoring.WseeRuntimeMBeanDelegate;
import weblogic.wsee.monitoring.WseeRuntimeMBeanManager;
import weblogic.wsee.monitoring.WseeV2RuntimeMBeanImpl;
import weblogic.wsee.policy.runtime.BuiltinPolicyFinder;

public class WseePolicyRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseePolicyRuntimeMBean, WseePolicyRuntimeData> implements WseePolicyRuntimeMBean {
   public WseePolicyRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   public WseePolicyRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      String[] var3 = BuiltinPolicyFinder.getAllCannedPolicyNames();
      if (var2 instanceof WseeV2RuntimeMBeanImpl) {
         String var4 = ((WseeV2RuntimeMBeanImpl)var2).getImplementationType();
         if (var4 != null && var4.startsWith(WseeRuntimeMBeanManager.Type.JAXWS.toString())) {
            var3 = BuiltinPolicyFinder.getAllNon92CannedPolicyNames();
         }
      }

      WseeBaseRuntimeData var6 = null;
      if (var2 instanceof WseeRuntimeMBeanDelegate) {
         var6 = ((WseeRuntimeMBeanDelegate)var2).getData();
      }

      WseePolicyRuntimeData var5 = new WseePolicyRuntimeData(var1, var3, var6);
      this.setData(var5);
   }

   protected WseeRuntimeMBeanDelegate<WseePolicyRuntimeMBean, WseePolicyRuntimeData> internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      return new WseePolicyRuntimeMBeanImpl(var1, var2, this);
   }

   public static WseePolicyRuntimeMBeanImpl create(String var0, RuntimeMBean var1) throws ManagementException {
      WseePolicyRuntimeMBeanImpl var2 = new WseePolicyRuntimeMBeanImpl(var0, var1);
      var2.register();
      return var2;
   }

   public String[] getAvailablePolicies() {
      return ((WseePolicyRuntimeData)this.getData()).getAvailablePolicies();
   }

   public void addPolicy(String var1) {
      ((WseePolicyRuntimeData)this.getData()).addPolicy(var1);
   }

   public void addPolicies(Map var1) {
      ((WseePolicyRuntimeData)this.getData()).addPolicies(var1);
   }

   public void removePolicy(String var1) {
      ((WseePolicyRuntimeData)this.getData()).removePolicy(var1);
   }
}
