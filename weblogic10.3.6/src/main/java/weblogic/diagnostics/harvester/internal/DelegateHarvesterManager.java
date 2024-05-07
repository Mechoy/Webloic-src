package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.Harvester;
import java.util.Iterator;

interface DelegateHarvesterManager {
   void addDelegateHarvester(DelegateHarvesterControl var1);

   void removeDelegateHarvesterByName(String var1);

   void removeAll();

   Harvester getDefaultDelegate();

   Iterator<DelegateHarvesterControl> iterator();

   Iterator<Harvester> harvesterIterator();

   Iterator<Harvester> activeOnlyIterator();

   Iterator<Harvester> activatingIterator();

   Iterator<Harvester> activatingIterator(DelegateHarvesterControl.ActivationPolicy var1);

   int getConfiguredHarvestersCount();

   int getActiveHarvestersCount();

   Harvester getHarvesterByName(String var1);
}
