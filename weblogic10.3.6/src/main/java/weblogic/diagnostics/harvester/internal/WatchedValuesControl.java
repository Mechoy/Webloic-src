package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.HarvestCallback;
import com.bea.adaptive.harvester.Harvester;
import com.bea.adaptive.harvester.WatchedValues;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.diagnostics.harvester.HarvesterRuntimeException;

public class WatchedValuesControl {
   private WatchedValues watchedValues;
   private List<WatchedValuesDelegateMap> mapList;
   private HarvestCallback harvestCallback;

   public WatchedValuesControl(WatchedValues var1, HarvestCallback var2, List<WatchedValuesDelegateMap> var3) {
      this.setMapList(var3);
      this.setWatchedValues(var1);
      this.harvestCallback = var2;
   }

   public HarvestCallback getHarvestCallback() {
      return this.harvestCallback;
   }

   public ArrayList<Integer> extendDelegateMap(Harvester var1, WatchedValues var2, ArrayList<Integer> var3) {
      List var4 = this.getWatchedValues().extendValues(var2, var3);
      ArrayList var5 = new ArrayList(var4.size());
      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         WatchedValues.Values var7 = (WatchedValues.Values)var6.next();
         var5.add(var7.getVID());
      }

      try {
         int var9 = var1.addWatchedValues(this.getWatchedValues().getName(), this.getWatchedValues(), this.harvestCallback);
         WatchedValuesDelegateMap var10 = new WatchedValuesDelegateMap(var1, var5, var9, this.getWatchedValues());
         this.getMapList().add(var10);
         return var5;
      } catch (Exception var8) {
         throw new HarvesterRuntimeException(var8);
      }
   }

   public WatchedValuesDelegateMap findDelegateMap(Harvester var1) {
      WatchedValuesDelegateMap var2 = null;
      Iterator var3 = this.getMapList().iterator();

      while(var3.hasNext()) {
         WatchedValuesDelegateMap var4 = (WatchedValuesDelegateMap)var3.next();
         if (var4.getDelegateHarvester() == var1) {
            var2 = var4;
            break;
         }
      }

      return var2;
   }

   public void resolveMetrics(Set<Integer> var1) {
      Iterator var2 = this.getMapList().iterator();

      while(var2.hasNext()) {
         WatchedValuesDelegateMap var3 = (WatchedValuesDelegateMap)var2.next();
         var3.resolveMetrics(var1);
      }

   }

   public void setWatchedValues(WatchedValues var1) {
      this.watchedValues = var1;
   }

   public WatchedValues getWatchedValues() {
      return this.watchedValues;
   }

   public void setMapList(List<WatchedValuesDelegateMap> var1) {
      this.mapList = var1;
   }

   public List<WatchedValuesDelegateMap> getMapList() {
      return this.mapList;
   }
}
