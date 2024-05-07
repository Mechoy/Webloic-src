package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.utils.Debug;

public class MigratableTargetConfigProcessor implements ConfigurationProcessor {
   public void updateConfiguration(DomainMBean var1) {
      ServerMBean[] var2 = var1.getServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ServerMBean var4 = var2[var3];
         if (var4.getCluster() != null) {
            createDefaultMigratableTargets(var1, var4);
         }
      }

   }

   public static void destroyDefaultMigratableTarget(ServerMBean var0) {
      String var1 = getNameOfDefaultMigratableTargetFor(var0);
      DomainMBean var2 = (DomainMBean)var0.getParent();
      MigratableTargetMBean var3 = var2.lookupMigratableTarget(var1);
      if (var3 != null) {
         var2.destroyMigratableTarget(var3);
      }

   }

   public static void createDefaultMigratableTargets(DomainMBean var0, ServerMBean var1) {
      if (var1.getJTAMigratableTarget() == null) {
         JTAMigratableTargetMBean var2 = var1.createJTAMigratableTarget();
         var2.setCluster(var1.getCluster());
         var2.setUserPreferredServer(var1);
      }

      String var7 = getNameOfDefaultMigratableTargetFor(var1);
      MigratableTargetMBean var3 = var0.lookupMigratableTarget(var7);
      if (var3 == null) {
         var3 = var0.createMigratableTarget(var7);
      }

      try {
         var3.setUserPreferredServer(var1);
         var3.setNotes(ManagementTextTextFormatter.getInstance().getDefaultServerMigratableTargetNote());
      } catch (InvalidAttributeValueException var5) {
         Debug.assertion(false, var5.toString());
      } catch (DistributedManagementException var6) {
         ManagementLogger.logDomainSaveFailed(var6);
      }

   }

   public static String getNameOfDefaultMigratableTargetFor(ServerMBean var0) {
      return var0.getName() + " (" + ManagementTextTextFormatter.getInstance().getDefaultMigratableSuffix() + ")";
   }
}
