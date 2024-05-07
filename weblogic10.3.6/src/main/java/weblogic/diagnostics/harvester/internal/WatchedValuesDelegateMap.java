package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.Harvester;
import com.bea.adaptive.harvester.WatchedValues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;

class WatchedValuesDelegateMap {
   private WatchedValues parentList;
   private int delegateWVID;
   private HashSet<Integer> vids;
   private Harvester delegateHarvester;
   private HashMap<Integer, WatchedValues.Values> vidsToValuesMap;
   private HashMap<Integer, Set<Integer>> allSlotsMap;
   private static DebugLogger dbg = DebugLogger.getDebugLogger("DebugDiagnosticsHarvester");

   public WatchedValuesDelegateMap(Harvester var1, ArrayList<Integer> var2, int var3, WatchedValues var4) {
      this.delegateHarvester = var1;
      this.vids = new HashSet(var2);
      this.delegateWVID = var3;
      this.parentList = var4;
   }

   public WatchedValues.Values getValue(int var1) {
      return this.parentList.getMetric(var1);
   }

   public Collection<WatchedValues.Values> findMatchingValuesSlots(Collection<WatchedValues.Values> var1) {
      ArrayList var2 = new ArrayList(this.vids.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         WatchedValues.Values var4 = (WatchedValues.Values)var3.next();
         WatchedValues.Values var5 = (WatchedValues.Values)this.getVidsToValuesMap().get(var4.getVID());
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }

   public Harvester getDelegateHarvester() {
      return this.delegateHarvester;
   }

   public void setDelegateHarvester(Harvester var1) {
      this.delegateHarvester = var1;
   }

   public int getDelegateWVID() {
      return this.delegateWVID;
   }

   public void setDelegateWVID(int var1) {
      this.delegateWVID = var1;
   }

   public Set<Integer> getVids() {
      return this.vids;
   }

   public void setVids(Set<Integer> var1) {
      this.vids = new HashSet(var1);
   }

   public WatchedValues getParentList() {
      return this.parentList;
   }

   public void setParentList(WatchedValues var1) {
      this.parentList = var1;
   }

   public void harvest(Collection<Integer> var1) {
      if (var1 == null) {
         if (dbg.isDebugEnabled()) {
            dbg.debug("Harvesting all slots for Harvester " + this.delegateHarvester.getName());
         }

         this.delegateHarvester.harvest(this.getAllSlotsMap());
      } else {
         Map var2 = this.getSlotsSubSet(var1);
         if (dbg.isDebugEnabled()) {
            dbg.debug("Harvesting slots {" + var2 + "} of " + this.parentList.getName() + " for Harvester " + this.delegateHarvester.getName());
         }

         this.delegateHarvester.harvest(var2);
      }

   }

   public Map<Integer, Set<Integer>> getSlotsSubSet(Collection<Integer> var1) {
      HashSet var2 = new HashSet(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Integer var4 = (Integer)var3.next();
         if (this.vids.contains(var4)) {
            var2.add(var4);
         }
      }

      HashMap var5 = new HashMap();
      var5.put(this.delegateWVID, var2);
      return var5;
   }

   public List<WatchedValues.Values> getPendingMetrics() {
      WatchedValues.Values[] var1 = this.delegateHarvester.getPendingMetrics(this.delegateWVID);
      ArrayList var2 = new ArrayList(var1.length);
      WatchedValues.Values[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WatchedValues.Values var6 = var3[var5];
         if (this.vids.contains(var6.getVID())) {
            var2.add(var6);
         }
      }

      return var2;
   }

   public List<String> getUnharvestableAttributes(String var1, String var2) {
      return this.delegateHarvester.getUnharvestableAttributes(this.delegateWVID, var1, var2);
   }

   public List<String> getDisabledAttributes(String var1, String var2) {
      return this.delegateHarvester.getDisabledAttributes(this.delegateWVID, var1, var2);
   }

   public List<String> getHarvestedTypes(String var1) {
      return this.delegateHarvester.getHarvestedTypes(this.delegateWVID, var1);
   }

   public int disableMetrics(Integer[] var1) {
      int var2 = 0;
      Set var3 = this.findVidIntersection(var1);
      if (var3.size() > 0) {
         var2 += this.delegateHarvester.disableMetrics(this.delegateWVID, (Integer[])var3.toArray(new Integer[var3.size()]));
      }

      return var2;
   }

   public int enableMetrics(Integer[] var1) {
      int var2 = 0;
      Set var3 = this.findVidIntersection(var1);
      if (var3.size() > 0) {
         var2 += this.delegateHarvester.enableMetrics(this.delegateWVID, (Integer[])var3.toArray(new Integer[var3.size()]));
      }

      return var2;
   }

   private Set<Integer> findVidIntersection(Integer[] var1) {
      HashSet var2 = new HashSet(var1.length);
      Integer[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Integer var6 = var3[var5];
         if (this.vids.contains(var6)) {
            var2.add(var6);
         }
      }

      return var2;
   }

   private HashMap<Integer, WatchedValues.Values> getVidsToValuesMap() {
      if (this.vidsToValuesMap == null) {
         this.vidsToValuesMap = this.constructVidsToValuesMap();
      }

      return this.vidsToValuesMap;
   }

   private HashMap<Integer, WatchedValues.Values> constructVidsToValuesMap() {
      HashMap var1 = new HashMap(this.vids.size());
      Iterator var2 = this.vids.iterator();

      while(var2.hasNext()) {
         Integer var3 = (Integer)var2.next();
         var1.put(var3, this.parentList.getMetric(var3));
      }

      return var1;
   }

   private synchronized HashMap<Integer, Set<Integer>> getAllSlotsMap() {
      if (this.allSlotsMap == null) {
         this.allSlotsMap = new HashMap();
      }

      this.allSlotsMap.put(this.delegateWVID, this.vids);
      return this.allSlotsMap;
   }

   public void extendWatchedValues(WatchedValues var1, ArrayList<Integer> var2) {
      List var3 = this.parentList.extendValues(var1, var2);
      HashSet var4 = new HashSet(var3.size());
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         WatchedValues.Values var6 = (WatchedValues.Values)var5.next();
         var4.add(var6.getVID());
      }

      this.delegateHarvester.resolveMetrics(this.delegateWVID, var4);
      synchronized(this) {
         this.vids.addAll(var4);
         this.allSlotsMap = null;
      }
   }

   public void resolveMetrics(Set<Integer> var1) {
      Object var2 = null;
      if (var1 != null) {
         var2 = this.findVidIntersection((Integer[])var1.toArray(new Integer[0]));
      } else {
         var2 = this.vids;
      }

      this.delegateHarvester.resolveMetrics(this.delegateWVID, (Set)var2);
   }
}
