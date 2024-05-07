package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.WatchedValues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.harvester.HarvesterDataSample;
import weblogic.diagnostics.harvester.WLDFHarvesterUtils;

final class HarvesterSnapshot {
   private static final DebugLogger DBG_DATA = DebugSupport.getLowLevelDebugLogger();
   private Collection<HarvesterDataSample> harvestedSamples;
   private long snapshotTimeMillis;
   private long snapshotElapsedTimeNanos;

   private static Set<String> createSupportedTypeNameSet() {
      HashSet var0 = new HashSet();
      Class[] var1 = HarvesterSnapshot.SupportedTypesHolder.supportedClasses;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Class var4 = var1[var3];
         var0.add(var4.getName());
      }

      return var0;
   }

   private static Set<Class> createSupportedTypesSet() {
      HashSet var0 = new HashSet();
      Class[] var1 = HarvesterSnapshot.SupportedTypesHolder.supportedClasses;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Class var4 = var1[var3];
         var0.add(var4);
      }

      return var0;
   }

   HarvesterSnapshot(long var1) {
      this.snapshotTimeMillis = var1;
   }

   int getDataSampleCount() {
      return this.harvestedSamples.size();
   }

   long getSnapshotStartTimeMillis() {
      return this.snapshotTimeMillis;
   }

   long getSnapshotElapsedTimeNanos() {
      return this.snapshotElapsedTimeNanos;
   }

   void setSnapshotElapsedTimeNanos(long var1) {
      this.snapshotElapsedTimeNanos = var1;
   }

   Collection<HarvesterDataSample> getHarvesterDataSamples() {
      return this.harvestedSamples;
   }

   synchronized void setDataSamples(Collection<WatchedValues.Values> var1) {
      this.harvestedSamples = this.buildSamplesSet(var1);
   }

   private List<HarvesterDataSample> buildSamplesSet(Collection<WatchedValues.Values> var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         WatchedValues.Values var4 = (WatchedValues.Values)var3.next();
         List var5 = var4.getValues().getRawValues();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            WatchedValues.Values.RawValueData var7 = (WatchedValues.Values.RawValueData)var6.next();
            Object var8 = var7.getValue();
            if (var8 != null) {
               Class var9 = var8.getClass();
               if (var9.isArray()) {
                  this.addSamples(var2, var7, (Object[])((Object[])var8));
               } else {
                  this.addSample(var2, var7, var8);
               }
            }
         }
      }

      return var2;
   }

   private void addSamples(List<HarvesterDataSample> var1, WatchedValues.Values.RawValueData var2, Object[] var3) {
      Object[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Object var7 = var4[var6];
         if (var7 != null) {
            if (var7.getClass().isArray()) {
               this.addSamples(var1, var2, (Object[])((Object[])var7));
            } else {
               this.addSample(var1, var2, var7);
            }
         }
      }

   }

   private void addSample(List<HarvesterDataSample> var1, WatchedValues.Values.RawValueData var2, Object var3) {
      Class var4 = var3.getClass();
      if (isSupportedValueType(var4)) {
         var1.add(new HarvesterDataSample(this.snapshotTimeMillis, var2.getTypeName(), var2.getInstanceName(), var2.getAttributeName(), var2.getValue()));
      } else if (var3 instanceof WatchedValues.AttributeTrackedDataItem) {
         WatchedValues.AttributeTrackedDataItem var5 = (WatchedValues.AttributeTrackedDataItem)var3;
         String var6 = WLDFHarvesterUtils.buildDataContextString(var5.getDataContext());
         Object var7 = var5.getData();
         if (var7 != null && isSupportedValueType(var7.getClass())) {
            var1.add(new HarvesterDataSample(this.snapshotTimeMillis, var2.getTypeName(), var2.getInstanceName(), var6, var7));
         } else if (DBG_DATA.isDebugEnabled()) {
            this.debugInvalidDataType(var6, var2.getInstanceName(), var2.getTypeName());
         }
      } else if (DBG_DATA.isDebugEnabled()) {
         this.debugInvalidDataType(var2.getAttributeName(), var2.getInstanceName(), var2.getTypeName());
      }

   }

   private void debugInvalidDataType(String var1, String var2, String var3) {
      DBG_DATA.debug("Data value for attribute " + var1 + " of instance " + var2 + " is of type " + var3 + ", which is not supported for harvesting");
   }

   static boolean isSupportedValueType(String var0) {
      if (DBG_DATA.isDebugEnabled()) {
         DBG_DATA.debug("checking if value of type " + var0 + " is supported");
      }

      return HarvesterSnapshot.SupportedTypesHolder.SUPPORTED_TYPE_NAMES.contains(var0);
   }

   static boolean isSupportedValueType(Class var0) {
      if (DBG_DATA.isDebugEnabled()) {
         DBG_DATA.debug("checking if value of type " + var0.getName() + " is supported");
      }

      return HarvesterSnapshot.SupportedTypesHolder.SUPPORTED_TYPES.contains(var0);
   }

   private static class SupportedTypesHolder {
      private static final Class[] supportedClasses;
      private static final Set<Class> SUPPORTED_TYPES;
      private static final Set<String> SUPPORTED_TYPE_NAMES;

      static {
         supportedClasses = new Class[]{String.class, Integer.class, Integer.TYPE, Boolean.class, Boolean.TYPE, Long.class, Long.TYPE, Double.class, Double.TYPE, Character.class, Character.TYPE, Float.TYPE, Float.class, Byte.TYPE, Byte.class};
         SUPPORTED_TYPES = HarvesterSnapshot.createSupportedTypesSet();
         SUPPORTED_TYPE_NAMES = HarvesterSnapshot.createSupportedTypeNameSet();
      }
   }
}
