package weblogic.diagnostics.snmp.agent.monfox;

import monfox.toolkit.snmp.agent.SnmpAgent;
import monfox.toolkit.snmp.agent.ext.acm.AppAcm;
import monfox.toolkit.snmp.agent.ext.audit.SnmpAuditTrailLogger;
import weblogic.diagnostics.snmp.agent.SNMPSecurityManager;

public class WLSSnmpSecurityManager implements SNMPSecurityManager {
   private SnmpAgent snmpAgent;

   WLSSnmpSecurityManager(SnmpAgent var1) {
      this.snmpAgent = var1;
   }

   public int getFailedAuthenticationCount() {
      int var1 = 0;
      SnmpAuditTrailLogger var2 = this.snmpAgent.getAuditTrailLogger();
      if (var2 instanceof WLSAuditTrailLogger) {
         var1 = ((WLSAuditTrailLogger)var2).getFailedAuthenticationCount();
      }

      return var1;
   }

   public int getFailedAuthorizationCount() {
      AppAcm var1 = (AppAcm)this.snmpAgent.getAccessControlModel();
      WLSAccessController var2 = (WLSAccessController)var1.getAccessController();
      return var2.getFailedAuthorizationCount();
   }

   public int getFailedEncryptionCount() {
      int var1 = 0;
      SnmpAuditTrailLogger var2 = this.snmpAgent.getAuditTrailLogger();
      if (var2 instanceof WLSAuditTrailLogger) {
         var1 = ((WLSAuditTrailLogger)var2).getFailedEncryptionCount();
      }

      return var1;
   }

   public void invalidateLocalizedKeyCache(String var1) {
      WLSSecurityExtension.getInstance().invalidateLocalizedKeyCache(var1);
   }
}
