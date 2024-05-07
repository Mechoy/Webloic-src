package weblogic.diagnostics.watch;

import com.bea.adaptive.harvester.WatchedValues;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.harvester.WLDFHarvesterUtils;
import weblogic.diagnostics.query.UnknownVariableException;
import weblogic.diagnostics.query.VariableInstance;
import weblogic.diagnostics.query.VariableResolver;

public class HarvesterVariablesImpl implements VariableResolver {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private WatchedValues watchedValues;

   HarvesterVariablesImpl(WatchConfiguration var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created HarvesterVariableImpl " + this);
      }

      if (var1 != null) {
         this.watchedValues = var1.getWatchedValues();
      }

   }

   public Object resolveVariable(String var1) throws UnknownVariableException {
      throw new UnsupportedOperationException("Variable resolution is only supported by index");
   }

   public Object resolveVariable(int var1) throws UnknownVariableException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Getting value for variable at index" + var1);
      }

      WatchedValues.Values var2 = this.watchedValues.getMetric(var1);
      WatchedValues.Values.ValuesData var3 = var2.getValues();
      Object var4 = null;
      if (var3 != null) {
         List var5 = var3.getRawValues();
         Object[] var6 = new Object[var5.size()];
         Iterator var7 = var5.iterator();
         int var8 = 0;

         while(var7.hasNext()) {
            WatchedValues.Values.RawValueData var9 = (WatchedValues.Values.RawValueData)var7.next();
            Object var10 = var9.getValue();
            if (var10 != null) {
               if (var10 instanceof WatchedValues.AttributeTrackedDataItem) {
                  WatchedValues.AttributeTrackedDataItem var11 = (WatchedValues.AttributeTrackedDataItem)var10;
                  var10 = new HarvesterVariableValueInstance(var9.getInstanceName(), var11);
               } else if (var10.getClass().isArray()) {
                  var10 = getLeafValues(var9, (Object[])((Object[])var10));
               }
            }

            var6[var8++] = var10;
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Raw value for (" + var9.getInstanceName() + "//" + var9.getAttributeName() + "): " + var10);
            }
         }

         if (var6 != null && var6.length == 1) {
            var4 = var6[0];
         } else {
            var4 = var6;
         }

         if (debugLogger.isDebugEnabled()) {
            if (var4 != null && var4.getClass().isArray()) {
               debugLogger.debug("Outgoing array of values (" + ((Object[])((Object[])var4)).length + " total): " + Arrays.toString((Object[])((Object[])var4)));
            } else {
               debugLogger.debug("Outgoing scalar value: " + var4);
            }
         }
      }

      return var4;
   }

   public static Object[] getLeafValues(WatchedValues.Values.RawValueData var0, Object[] var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         addItems(var0, var2, var1);
      }

      return var2.toArray();
   }

   private static void addItems(WatchedValues.Values.RawValueData var0, List<Object> var1, Object[] var2) {
      Object[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if (var6 != null) {
            if (var6 instanceof WatchedValues.AttributeTrackedDataItem) {
               WatchedValues.AttributeTrackedDataItem var7 = (WatchedValues.AttributeTrackedDataItem)var6;
               if (var7.getData() == null) {
                  continue;
               }

               var6 = new HarvesterVariableValueInstance(var0.getInstanceName(), var7);
            }

            Class var8 = var6.getClass();
            if (var8.isArray()) {
               addItems(var0, var1, (Object[])((Object[])var6));
            } else {
               var1.add(var6);
            }
         }
      }

   }

   public int resolveInteger(int var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public long resolveLong(int var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public float resolveFloat(int var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public double resolveDouble(int var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public String resolveString(int var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public int resolveInteger(String var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public long resolveLong(String var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public float resolveFloat(String var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public double resolveDouble(String var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   public String resolveString(String var1) throws UnknownVariableException {
      throw new UnsupportedOperationException();
   }

   private static class HarvesterVariableValueInstance implements VariableInstance {
      String instanceName;
      WatchedValues.AttributeTrackedDataItem item;

      public HarvesterVariableValueInstance(String var1, WatchedValues.AttributeTrackedDataItem var2) {
         this.instanceName = var1;
         this.item = var2;
      }

      public String getAttributeName() {
         return WLDFHarvesterUtils.buildDataContextString(this.item.getDataContext());
      }

      public String getInstanceName() {
         return this.instanceName;
      }

      public Object getInstanceValue() {
         return this.item.getData();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(128);
         var1.append(this.instanceName);
         var1.append("//");
         var1.append(this.getAttributeName());
         var1.append(":");
         var1.append(this.getInstanceValue());
         return var1.toString();
      }
   }
}
