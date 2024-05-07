package weblogic.management.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.utils.ArrayUtils;

public class WLDFSystemResourceValidator {
   public static void validateWLDFSystemResources(DomainMBean var0) {
      WLDFSystemResourceMBean[] var1 = var0.getWLDFSystemResources();
      if (var1 != null) {
         HashSet var2 = new HashSet();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            TargetMBean[] var4 = var1[var3].getTargets();
            if (var4 != null) {
               ServerMBean[] var7 = getServersInTargets(var4);

               for(int var5 = 0; var5 < var7.length; ++var5) {
                  String var6 = var7[var5].getName();
                  if (var2.contains(var6)) {
                     throw new IllegalArgumentException(DiagnosticsLogger.logTargettingMultipleWLDFSystemResourcesToServerLoggable(var6).getMessage());
                  }

                  var2.add(var6);
               }
            }
         }

      }
   }

   private static ServerMBean[] getServersInTargets(TargetMBean[] var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         TargetMBean var3 = var0[var2];
         if (var3 instanceof ServerMBean) {
            var1.add(var3);
         } else {
            if (!(var3 instanceof ClusterMBean)) {
               throw new AssertionError("The list of targets contained a non-server or a non-cluster member");
            }

            ClusterMBean var4 = (ClusterMBean)var3;
            ArrayUtils.addAll(var1, var4.getServers());
         }
      }

      ServerMBean[] var5 = new ServerMBean[var1.size()];
      var1.toArray(var5);
      return var5;
   }
}
