package weblogic.diagnostics.harvester;

import com.bea.adaptive.harvester.HarvestCallback;
import com.bea.adaptive.harvester.Harvester;
import com.bea.adaptive.harvester.WatchedValues;
import com.bea.adaptive.harvester.WatchedValues.ContextItem.AttributeTermType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.management.JMException;
import javax.management.ObjectName;
import weblogic.diagnostics.harvester.internal.AttributeNormalizerUtil;
import weblogic.diagnostics.harvester.internal.BeanTreeHarvesterImpl;
import weblogic.diagnostics.harvester.internal.MetricArchiver;
import weblogic.diagnostics.i18n.DiagnosticsHarvesterLogger;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;

public class WLDFHarvesterUtils {
   private static WLDFHarvester harvesterInstance;

   private WLDFHarvesterUtils() {
   }

   private static WLDFHarvester getHarvesterInstance() {
      Class var0 = WLDFHarvesterUtils.class;
      synchronized(WLDFHarvesterUtils.class) {
         if (harvesterInstance == null) {
            harvesterInstance = WLDFHarvesterManager.getInstance().getHarvesterSingleton();
         }
      }

      return harvesterInstance;
   }

   public static Object getValue(String var0, String var1, String var2, String var3) throws JMException, InvalidHarvesterInstanceNameException {
      WLDFHarvester var4 = getHarvesterInstance();
      byte var5 = 1;
      WatchedValues var6 = MetricArchiver.getInstance().createWatchedValues("HarvesterUtils_getValue()");
      InstanceNameNormalizer var7 = new InstanceNameNormalizer(var2);
      var6.addMetric(var0, var1, var7.translateHarvesterSpec(), var3, false, var7.isPattern(), false, true, var5);
      boolean var8 = true;

      int var18;
      try {
         var18 = var4.addWatchedValues(var6.getName(), var6, (HarvestCallback)null);
      } catch (IOException var17) {
         throw new HarvesterRuntimeException(var17);
      }

      HashMap var9 = new HashMap(1);
      HashSet var10 = new HashSet(1);
      var10.add(Integer.valueOf(var5));
      var9.put(var18, var10);
      var4.harvest(var9);
      Object var11 = null;
      WatchedValues.Values var12 = var6.getMetric(var5);
      List var13 = var12.getValues().getRawValues();
      if (var13.size() == 1) {
         var11 = ((WatchedValues.Values.RawValueData)var13.get(0)).getValue();
      } else {
         ArrayList var14 = new ArrayList();
         Iterator var15 = var13.iterator();

         while(var15.hasNext()) {
            WatchedValues.Values.RawValueData var16 = (WatchedValues.Values.RawValueData)var15.next();
            var14.add(var16.getValue());
         }

         var11 = var14;
      }

      var4.deleteWatchedValues(var6);
      return var11;
   }

   public static ArrayList<String> validateWatchedValues(Harvester var0, WatchedValues var1) {
      Collection var2 = var0.validateWatchedValues(var1);
      ArrayList var3 = new ArrayList();
      processValidationResults(var1.getName(), var2, var3);
      return var3;
   }

   public static void processValidationResults(String var0, Collection<WatchedValues.Validation> var1) {
      processValidationResults(var0, var1, (ArrayList)null);
   }

   public static void processValidationResults(String var0, Collection<WatchedValues.Validation> var1, ArrayList<String> var2) {
      boolean var3 = false;
      String var4 = "";
      int var5 = 0;
      Iterator var6 = var1.iterator();

      while(true) {
         Set var8;
         do {
            WatchedValues.Validation var7;
            do {
               if (!var6.hasNext()) {
                  if (var3) {
                     var4 = DiagnosticsTextTextFormatter.getInstance().getErrorsOccurredValidatingWatchedValues(var4, var5);
                     DiagnosticsHarvesterLogger.logValidationErrors(var0, var4);
                  }

                  return;
               }

               var7 = (WatchedValues.Validation)var6.next();
            } while(var7.getStatus() != -1);

            if (var2 != null) {
               var2.add(var7.getMetric().getInstanceName());
            }

            var8 = var7.getIssues();
         } while(var8.size() <= 0);

         if (!var3) {
            var3 = true;
         }

         for(Iterator var9 = var8.iterator(); var9.hasNext(); ++var5) {
            String var10 = (String)var9.next();
            var4 = var4 + var10;
         }
      }
   }

   public static ArrayList<String> validateWatchedValues(WatchedValues var0) {
      return validateWatchedValues(getHarvesterInstance(), var0);
   }

   public static void validateNamespace(String var0) throws InvalidHarvesterNamespaceException {
      weblogic.diagnostics.harvester.internal.Validators.validateNamespace(var0);
   }

   public static String normalizeInstanceName(String var0) throws InvalidHarvesterInstanceNameException {
      InstanceNameNormalizer var1 = new InstanceNameNormalizer(var0);
      return var1.translateHarvesterSpec();
   }

   public static String normalizeAttributeSpecification(String var0, String var1) {
      return AttributeNormalizerUtil.getNormalizedAttributeName(var0, var1);
   }

   public static String[] normalizeAttributeSpecs(String var0, String[] var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = normalizeAttributeSpecification(var0, var1[var3]);
         }

         return var2;
      }
   }

   public static String normalizeAttributeForInstance(String var0, String var1) {
      String var2 = getTypeForObjectName(var0);
      return var2 != null ? normalizeAttributeSpecification(var2, var1) : var1;
   }

   public static String getTypeForInstance(String var0) {
      BeanTreeHarvesterImpl var1 = BeanTreeHarvesterImpl.getInstance();
      String var2 = var1.getTypeForInstance(var0);
      if (var2 == null) {
         String var3 = getTypeForObjectName(var0);
         if (var3 != null && var1.isTypeHandled(var3) == 2) {
            var2 = var3;
         }
      }

      return var2;
   }

   private static String getTypeForObjectName(String var0) {
      String var1 = null;

      try {
         ObjectName var2 = new ObjectName(var0);
         String var3 = var2.getKeyProperty("Type");
         if (var3 != null && var3.indexOf(42) < 0 && var3.indexOf(63) < 0) {
            var1 = "weblogic.management.runtime." + var3 + "MBean";
         }
      } catch (Exception var4) {
      }

      return var1;
   }

   public static String buildDataContextString(List<WatchedValues.ContextItem> var0) {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         WatchedValues.ContextItem var3 = (WatchedValues.ContextItem)var2.next();
         WatchedValues.ContextItem.AttributeTermType var4 = var3.getAttributeTermType();
         if (var4 == AttributeTermType.ARRAY_OR_LIST) {
            var1.append('[');
         } else if (var4 == AttributeTermType.MAP) {
            var1.append('(');
         } else if (var4 == AttributeTermType.SIMPLE) {
            var1.append('.');
         }

         var1.append(var3.getContext().toString());
         if (var4 == AttributeTermType.ARRAY_OR_LIST) {
            var1.append(']');
         } else if (var4 == AttributeTermType.MAP) {
            var1.append(')');
         }
      }

      int var5 = var1.length();
      if (var5 > 0) {
         char var6 = var1.charAt(0);
         if (var6 == '.') {
            var1.deleteCharAt(0);
         }
      }

      return var1.toString();
   }

   public static Object[] getLeafValues(Object[] var0) {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         addItems(var1, var0);
      }

      return var1.toArray();
   }

   private static void addItems(List<Object> var0, Object[] var1) {
      Object[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         if (var5 != null) {
            if (var5 instanceof WatchedValues.AttributeTrackedDataItem) {
               var5 = ((WatchedValues.AttributeTrackedDataItem)var5).getData();
               if (var5 == null) {
                  continue;
               }
            }

            Class var6 = var5.getClass();
            if (var6.isArray()) {
               addItems(var0, (Object[])((Object[])var5));
            } else {
               var0.add(var5);
            }
         }
      }

   }
}
