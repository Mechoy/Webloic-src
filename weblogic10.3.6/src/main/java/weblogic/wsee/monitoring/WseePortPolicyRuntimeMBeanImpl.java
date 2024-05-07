package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseePortPolicyRuntimeMBean;

public class WseePortPolicyRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseePortPolicyRuntimeMBean, WseePortPolicyRuntimeData> implements WseePortPolicyRuntimeMBean, WsspStats {
   WseePortPolicyRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   public WseePortPolicyRuntimeMBeanImpl(String var1, WseeBasePortRuntimeMBeanImpl var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      WseePortPolicyRuntimeData var3 = new WseePortPolicyRuntimeData(var1, var2.getData());
      this.setData(var3);
   }

   protected WseePortPolicyRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseePortPolicyRuntimeMBeanImpl var3 = new WseePortPolicyRuntimeMBeanImpl(var1, var2, this);
      return var3;
   }

   public int getAuthenticationSuccesses() {
      return ((WseePortPolicyRuntimeData)this.getData()).getAuthenticationSuccesses();
   }

   public int getAuthenticationViolations() {
      return ((WseePortPolicyRuntimeData)this.getData()).getAuthenticationViolations();
   }

   public int getAuthorizationSuccesses() {
      return ((WseePortPolicyRuntimeData)this.getData()).getAuthorizationSuccesses();
   }

   public int getAuthorizationViolations() {
      return ((WseePortPolicyRuntimeData)this.getData()).getAuthorizationViolations();
   }

   public int getConfidentialitySuccesses() {
      return ((WseePortPolicyRuntimeData)this.getData()).getConfidentialitySuccesses();
   }

   public int getConfidentialityViolations() {
      return ((WseePortPolicyRuntimeData)this.getData()).getConfidentialityViolations();
   }

   public int getIntegritySuccesses() {
      return ((WseePortPolicyRuntimeData)this.getData()).getIntegritySuccesses();
   }

   public int getIntegrityViolations() {
      return ((WseePortPolicyRuntimeData)this.getData()).getIntegrityViolations();
   }

   public int getPolicyFaults() {
      return ((WseePortPolicyRuntimeData)this.getData()).getPolicyFaults();
   }

   /** @deprecated */
   @Deprecated
   public int getTotalFaults() {
      return ((WseePortPolicyRuntimeData)this.getData()).getTotalFaults();
   }

   public int getTotalSecurityFaults() {
      return ((WseePortPolicyRuntimeData)this.getData()).getTotalSecurityFaults();
   }

   public int getTotalViolations() {
      return ((WseePortPolicyRuntimeData)this.getData()).getTotalViolations();
   }

   public void reportAuthenticationSuccess() {
      ((WseePortPolicyRuntimeData)this.getData()).reportAuthenticationSuccess();
   }

   public void reportAuthenticationViolation() {
      ((WseePortPolicyRuntimeData)this.getData()).reportAuthenticationViolation();
   }

   public void reportAuthorizationSuccess() {
      ((WseePortPolicyRuntimeData)this.getData()).reportAuthorizationSuccess();
   }

   public void reportAuthorizationViolation() {
      ((WseePortPolicyRuntimeData)this.getData()).reportAuthorizationViolation();
   }

   public void reportConfidentialitySuccess() {
      ((WseePortPolicyRuntimeData)this.getData()).reportConfidentialitySuccess();
   }

   public void reportConfidentialityViolation() {
      ((WseePortPolicyRuntimeData)this.getData()).reportConfidentialityViolation();
   }

   public void reportIntegritySuccess() {
      ((WseePortPolicyRuntimeData)this.getData()).reportIntegritySuccess();
   }

   public void reportIntegrityViolation() {
      ((WseePortPolicyRuntimeData)this.getData()).reportIntegrityViolation();
   }

   public void reportPolicyFault() {
      ((WseePortPolicyRuntimeData)this.getData()).reportPolicyFault();
   }

   public void reportGeneralFault() {
      ((WseePortPolicyRuntimeData)this.getData()).reportGeneralFault();
   }
}
