package weblogic.diagnostics.watch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.harvester.InvalidHarvesterInstanceNameException;
import weblogic.diagnostics.harvester.InvalidHarvesterNamespaceException;
import weblogic.diagnostics.harvester.WLDFHarvesterUtils;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;
import weblogic.diagnostics.i18n.DiagnosticsTextWatchTextFormatter;
import weblogic.utils.PlatformConstants;

public class HarvesterVariablesParser {
   private static final DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private static Pattern TYPE_PAT = Pattern.compile("([a-zA-Z_$][a-zA-Z0-9_$]*\\.)*[a-zA-Z_$][a-zA-Z0-9_$]*");

   static HarvesterVariablesParser getInstance() {
      return HarvesterVariablesParser.SingletonWrapper.SINGLETON;
   }

   static String[] parse(String var0, String var1) {
      if (var1 == null) {
         var1 = "Unknown";
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Get properties for " + var0);
      }

      String var2 = "";
      int var3 = 0;
      String var4 = null;
      String var5 = null;
      String var6 = null;
      String[] var7 = var0.trim().split("//");
      if (var7.length < 2) {
         String var17 = DiagnosticsTextTextFormatter.getInstance().getBadHarvesterVariableName(var0, var1);
         throw new IllegalArgumentException(var17);
      } else {
         if (var7.length > 2) {
            var4 = var7[0].trim();
            var6 = var7[1].trim();
            var5 = var7[2].trim();
         } else {
            var6 = var7[0].trim();
            var5 = var7[1].trim();
         }

         int var8 = 0;
         String var9 = null;
         String var10 = null;
         if (var6.startsWith("[")) {
            int var11 = var6.indexOf(93);
            if (var11 > 0) {
               var9 = var6.substring(1, var11).trim();
               var8 = var11 + 1;
            }

            if (var8 > 0 && var8 < var6.length()) {
               var10 = var6.substring(var8).trim();
            }
         } else {
            var10 = var6;
         }

         StringBuilder var10000;
         if (var4 != null && var4.length() > 0) {
            try {
               WLDFHarvesterUtils.validateNamespace(var4);
            } catch (InvalidHarvesterNamespaceException var16) {
               var10000 = (new StringBuilder()).append(var2).append("").append(PlatformConstants.EOL);
               ++var3;
               var2 = var10000.append(var3).append(") ").append(var16.getMessage()).toString();
            }
         }

         String var18 = null;
         String var13;
         if (var10 != null && var10.length() > 0) {
            try {
               WLDFHarvesterUtils.normalizeInstanceName(var10);
            } catch (InvalidHarvesterInstanceNameException var15) {
               var13 = DiagnosticsTextTextFormatter.getInstance().getInvalidObjectName(var10, var0, var1);
               var10000 = (new StringBuilder()).append(var2).append("").append(PlatformConstants.EOL);
               ++var3;
               var2 = var10000.append(var3).append(") ").append(var13).toString();
            }

            var18 = WLDFHarvesterUtils.getTypeForInstance(var10);
            if (var9 == null || var9.length() == 0) {
               var9 = var18;
            }
         }

         if (var9 != null && var9.length() != 0) {
            Matcher var12 = TYPE_PAT.matcher(var9);
            if (!var12.matches()) {
               var13 = DiagnosticsTextTextFormatter.getInstance().getBadHarvesterVariableType(var9, var0, var1);
               var10000 = (new StringBuilder()).append(var2).append("").append(PlatformConstants.EOL);
               ++var3;
               var2 = var10000.append(var3).append(") ").append(var13).toString();
            }
         }

         String var19;
         if (var9 != null && var18 != null && !var9.equals(var18)) {
            var19 = DiagnosticsTextTextFormatter.getInstance().getHarvesterVariableTypeMismatch(var0, var10, var9, var18);
            var10000 = (new StringBuilder()).append(var2).append("").append(PlatformConstants.EOL);
            ++var3;
            var2 = var10000.append(var3).append(") ").append(var19).toString();
         }

         if ((var10 == null || var10.length() == 0) && (var9 == null || var9.length() == 0)) {
            var19 = DiagnosticsTextTextFormatter.getInstance().getMissingBothTypeAndInstanceName(var0, var1);
            var10000 = (new StringBuilder()).append(var2).append("").append(PlatformConstants.EOL);
            ++var3;
            var2 = var10000.append(var3).append(") ").append(var19).toString();
         }

         if (var5 != null && var5.length() != 0) {
            if (var5.length() == 0) {
               var19 = DiagnosticsTextTextFormatter.getInstance().getEmptyAttributeName(var0, var1);
               var10000 = (new StringBuilder()).append(var2).append("").append(PlatformConstants.EOL);
               ++var3;
               var2 = var10000.append(var3).append(") ").append(var19).toString();
            } else {
               try {
                  var5 = normalizeAttributeName(var9, var5, var10);
               } catch (Exception var14) {
                  var13 = DiagnosticsTextTextFormatter.getInstance().getBadHarvesterVariableAttr(var5, var0, var1);
                  var10000 = (new StringBuilder()).append(var2).append("").append(PlatformConstants.EOL);
                  ++var3;
                  var2 = var10000.append(var3).append(") ").append(var13).toString();
               }

               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Normalized attribute: " + var5);
               }
            }
         } else {
            var19 = DiagnosticsTextWatchTextFormatter.getInstance().getNullWatchVariableAttributeNameText(var5, var1);
            var10000 = (new StringBuilder()).append(var2).append("").append(PlatformConstants.EOL);
            ++var3;
            var2 = var10000.append(var3).append(") ").append(var19).toString();
         }

         if (var3 > 0) {
            var19 = DiagnosticsTextTextFormatter.getInstance().getErrorsOcurredParsingHarvesterVariableName(var0, var1, var2, var3);
            throw new IllegalArgumentException(var19);
         } else {
            return new String[]{var4, var9, var10, var5};
         }
      }
   }

   private static String normalizeAttributeName(String var0, String var1, String var2) {
      if (var0 != null) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Normalizing attribute " + var1 + " based on type name " + var0);
         }

         var1 = WLDFHarvesterUtils.normalizeAttributeSpecification(var0, var1);
      } else if (var2 != null) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Normalizing attribute " + var1 + " based on instance name " + var2);
         }

         var1 = WLDFHarvesterUtils.normalizeAttributeForInstance(var2, var1);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Normalizing attribute " + var1 + " using default normalizer");
         }

         var1 = WLDFHarvesterUtils.normalizeAttributeSpecification((String)null, var1);
      }

      return var1;
   }

   private static class SingletonWrapper {
      private static HarvesterVariablesParser SINGLETON = new HarvesterVariablesParser();
   }
}
