package weblogic.upgrade.jta;

import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class JTAMigratableTargetConfigProcessor implements ConfigurationProcessor {
   private final boolean DEBUG = false;

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      try {
         ServerMBean[] var2 = var1.getServers();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            JTAMigratableTargetMBean var4 = var2[var3].getJTAMigratableTarget();
            if (var4 != null) {
               ServerMBean[] var5 = var4.getConstrainedCandidateServers();
               if (var5.length != 0) {
                  ServerMBean var6 = var4.getUserPreferredServer();
                  boolean var7 = false;

                  for(int var8 = 0; var8 < var5.length; ++var8) {
                     if (var5[var8].getName().equals(var6.getName())) {
                        var7 = true;
                        break;
                     }
                  }

                  if (!var7) {
                     var4.addConstrainedCandidateServer(var6);
                  }
               }
            }
         }

      } catch (Exception var9) {
         throw new UpdateException(var9);
      }
   }

   private void p(Object var1) {
      System.out.println("<JTAMigratableTargetConfigProcessor> " + var1.toString());
   }
}
