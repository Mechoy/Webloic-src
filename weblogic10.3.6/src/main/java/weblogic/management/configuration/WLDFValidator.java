package weblogic.management.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;
import weblogic.utils.ArrayUtils;

public class WLDFValidator {
   private static final DiagnosticsTextTextFormatter DIAG_TXT_FMT = DiagnosticsTextTextFormatter.getInstance();

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

   public static void validateWLDFServerDiagnosticConfiguration(DomainMBean var0) {
      ServerMBean[] var1 = var0.getServers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         WLDFServerDiagnosticMBean var3 = var1[var2].getServerDiagnosticConfig();
         String var4 = var3.getDiagnosticDataArchiveType();
         if (var4.equals("JDBCArchive")) {
            JDBCSystemResourceMBean var5 = var3.getDiagnosticJDBCResource();
            if (var5 == null) {
               throw new IllegalArgumentException(DIAG_TXT_FMT.getNoJDBCSystemResourceConfiguredText(var1[var2].getName()));
            }

            TargetMBean[] var6 = var5.getTargets();
            if (var6 == null) {
               throw new IllegalArgumentException(DIAG_TXT_FMT.getJDBCSystemResourceNotTargettedToServer(var1[var2].getName(), var5.getName()));
            }

            ServerMBean[] var7 = getServersInTargets(var6);
            HashSet var8 = new HashSet();

            for(int var9 = 0; var9 < var6.length; ++var9) {
               var8.add(var6[var9].getName());
            }

            if (!var8.contains(var1[var2].getName())) {
               throw new IllegalArgumentException(DIAG_TXT_FMT.getJDBCSystemResourceNotTargettedToServer(var1[var2].getName(), var5.getName()));
            }
         }

         validateDataRetirements(var3);
      }

   }

   private static void validateDataRetirements(WLDFServerDiagnosticMBean var0) {
      HashMap var1 = new HashMap();
      WLDFDataRetirementMBean[] var2 = var0.getWLDFDataRetirements();
      int var3 = var2 != null ? var2.length : 0;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4].getArchiveName();
         if (var5 != null) {
            var5 = var5.trim();
         }

         if (var5 != null && var5.length() > 0) {
            Integer var6 = (Integer)var1.get(var5);
            int var7 = var6 != null ? var6 : 0;
            var1.put(var5, new Integer(var7 + 1));
         }
      }

      String var9 = null;
      Iterator var10 = var1.keySet().iterator();

      while(var10.hasNext()) {
         String var11 = (String)var10.next();
         Integer var12 = (Integer)var1.get(var11);
         int var8 = var12 != null ? var12 : 0;
         if (var8 > 1) {
            if (var9 == null) {
               var9 = var11;
            } else {
               var9 = var9 + "," + var11;
            }
         }
      }

      if (var9 != null) {
         throw new IllegalArgumentException(DIAG_TXT_FMT.getDuplicateRetirementsErrorText(var9));
      }
   }

   static ServerMBean[] getServersInTargets(TargetMBean[] var0) {
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

   public static void validateDataRetirementArchiveName(WLDFDataRetirementMBean var0, String var1) throws IllegalArgumentException {
      ConfigurationValidator.validateName(var1);
      WLDFServerDiagnosticMBean var2 = (WLDFServerDiagnosticMBean)var0.getParent();
      WLDFDataRetirementMBean[] var3 = var2 != null ? var2.getWLDFDataRetirements() : null;
      int var4 = var3 != null ? var3.length : 0;

      for(int var5 = 0; var5 < var4; ++var5) {
         WLDFDataRetirementMBean var6 = var3[var5];
         if (!var6.getName().equals(var0.getName()) && var1.equals(var6.getArchiveName())) {
            throw new IllegalArgumentException(DIAG_TXT_FMT.getDuplicateRetirementsErrorText(var1));
         }
      }

      if (!var1.equals("HarvestedDataArchive") && !var1.equals("EventsDataArchive") && !var1.startsWith("CUSTOM/")) {
         throw new IllegalArgumentException(DIAG_TXT_FMT.getInvalidArchiveNameForDataRetirementText(var1, "HarvestedDataArchive | EventsDataArchive | CUSTOM/xxx"));
      }
   }

   public static void validateDataRetirementTime(WLDFDataRetirementMBean var0, int var1) throws IllegalArgumentException {
      if (var1 < 0 || var1 > 23) {
         throw new IllegalArgumentException(DIAG_TXT_FMT.getInvalidRetirementTimeText(var0.getName(), var1));
      }
   }
}
