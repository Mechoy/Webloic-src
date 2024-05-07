package weblogic.management.j2ee.internal;

import java.security.AccessController;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.j2ee.statistics.JCAStats;
import javax.management.j2ee.statistics.Stats;
import weblogic.management.j2ee.JCAResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class JCAResourceMBeanImpl extends StatsProviderMBeanImpl implements JCAResourceMBean {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String domain;
   private final ObjectName wlsObjectName;

   public JCAResourceMBeanImpl(String var1, ObjectName var2) {
      super(var1);
      this.wlsObjectName = var2;
   }

   public String[] getconnectionFactories() {
      String var1 = JMOTypesHelper.getKeyValue(this.name, "name");
      String var2 = JMOTypesHelper.getKeyValue(this.name, "J2EEServer");
      String var3 = domain + ":j2eeType=" + "JCAConnectionFactory" + "," + "J2EEServer" + "=" + var2 + "," + "ResourceAdapterModule" + "=" + var1 + "," + "JCAResource" + "=" + var1 + ",*";

      try {
         ObjectName var4 = new ObjectName(var3);
         return this.queryNames(var4);
      } catch (MalformedObjectNameException var5) {
         throw new AssertionError(" Malformed ObjectName" + var5);
      }
   }

   public Stats getstats() {
      try {
         JCAStats var1 = (JCAStats)MBeanServerConnectionProvider.getDomainMBeanServerConnection().getAttribute(this.wlsObjectName, "Stats");
         return var1;
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public boolean isstatisticsProvider() {
      return true;
   }

   static {
      domain = ManagementService.getRuntimeAccess(kernelId).getDomainName();
   }
}
