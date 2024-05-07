package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.Harvester;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.diagnostics.debug.DebugLogger;

class DelegateHarvesterManagerImpl extends ArrayList<DelegateHarvesterControl> implements DelegateHarvesterManager {
   private static DebugLogger debugLogger = DebugSupport.getDebugLogger();
   private HashMap<String, DelegateHarvesterControl> delegatesByName = new HashMap();
   private int modifiedCount;

   private DelegateHarvesterManagerImpl() {
   }

   static DelegateHarvesterManager createDelegateHarvesterManager() {
      return new DelegateHarvesterManagerImpl();
   }

   public void addDelegateHarvester(DelegateHarvesterControl var1) {
      this.add(var1);
      ++this.modifiedCount;
      this.delegatesByName.put(var1.getName(), var1);
      if (var1.getActivationPolicy() == DelegateHarvesterControl.ActivationPolicy.IMMEDIATE) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Activating delegate: " + var1.getName());
         }

         var1.activate();
      }

   }

   public void removeDelegateHarvesterByName(String var1) {
      DelegateHarvesterControl var2 = (DelegateHarvesterControl)this.delegatesByName.remove(var1);
      if (var2 != null || var1.equals(var2.getName())) {
         this.remove(var2);
         ++this.modifiedCount;
         var2.deactivate();
      }

   }

   public void removeAll() {
      Iterator var1 = this.delegatesByName.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         this.removeDelegateHarvesterByName(var2);
      }

   }

   public Harvester getDefaultDelegate() {
      Harvester var1 = null;
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         DelegateHarvesterControl var3 = (DelegateHarvesterControl)var2.next();
         if (var3.isDefaultDelegate()) {
            var1 = var3.getDelegate();
         }
      }

      return var1;
   }

   public Iterator<Harvester> harvesterIterator() {
      return new ActivatingIterator(DelegateHarvesterControl.ActivationPolicy.ON_REGISTRATION);
   }

   public Iterator<Harvester> activeOnlyIterator() {
      return new ActiveControlIterator();
   }

   public Iterator<Harvester> activatingIterator() {
      return new ActivatingIterator(DelegateHarvesterControl.ActivationPolicy.ON_REGISTRATION);
   }

   public Iterator<Harvester> activatingIterator(DelegateHarvesterControl.ActivationPolicy var1) {
      return new ActivatingIterator(var1);
   }

   public int getConfiguredHarvestersCount() {
      return this.size();
   }

   public int getActiveHarvestersCount() {
      int var1 = 0;
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         DelegateHarvesterControl var3 = (DelegateHarvesterControl)var2.next();
         if (var3.isActive()) {
            ++var1;
         }
      }

      return var1;
   }

   public Harvester getHarvesterByName(String var1) {
      DelegateHarvesterControl var2 = (DelegateHarvesterControl)this.delegatesByName.get(var1);
      return var2 != null ? var2.getDelegate() : null;
   }

   protected int getModifiedCount() {
      return this.modifiedCount;
   }

   abstract class ControlIterator implements Iterator<Harvester> {
      protected DelegateHarvesterControl next;
      protected DelegateHarvesterControl current;
      protected int currentIndex = -1;
      protected int modcount = DelegateHarvesterManagerImpl.this.getModifiedCount();

      public ControlIterator() {
      }

      public boolean hasNext() {
         synchronized(this) {
            this.checkModification();
            if (this.next == null) {
               this.next = this.findNextNode();
            }
         }

         return this.next != null;
      }

      public Harvester next() {
         this.nextControl();
         return this.current.getDelegate();
      }

      protected void nextControl() {
         synchronized(this) {
            this.checkModification();
            this.current = this.next;
            this.currentIndex = DelegateHarvesterManagerImpl.this.indexOf(this.current);
            this.next = this.findNextNode();
         }
      }

      protected abstract DelegateHarvesterControl findNextNode();

      private final void checkModification() {
         if (DelegateHarvesterManagerImpl.this.getModifiedCount() != this.modcount) {
            throw new ConcurrentModificationException();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   class ActiveControlIterator extends ControlIterator {
      public ActiveControlIterator() {
         super();
      }

      protected DelegateHarvesterControl findNextNode() {
         DelegateHarvesterControl var1 = null;

         for(int var2 = this.currentIndex + 1; var2 < DelegateHarvesterManagerImpl.this.size(); ++var2) {
            DelegateHarvesterControl var3 = (DelegateHarvesterControl)DelegateHarvesterManagerImpl.this.get(var2);
            if (var3.isActive()) {
               var1 = var3;
               break;
            }
         }

         return var1;
      }
   }

   class ActivatingIterator extends ControlIterator {
      private DelegateHarvesterControl.ActivationPolicy threshold;

      public ActivatingIterator(DelegateHarvesterControl.ActivationPolicy var2) {
         super();
         this.threshold = var2;
      }

      protected DelegateHarvesterControl findNextNode() {
         DelegateHarvesterControl var1 = null;
         int var2 = this.currentIndex + 1;
         if (var2 < DelegateHarvesterManagerImpl.this.size()) {
            var1 = (DelegateHarvesterControl)DelegateHarvesterManagerImpl.this.get(var2);
         }

         return var1;
      }

      public Harvester next() {
         super.nextControl();
         if (this.current != null) {
            if (!this.current.isActive() && this.threshold.compareTo(this.current.getActivationPolicy()) >= 0) {
               if (DelegateHarvesterManagerImpl.debugLogger.isDebugEnabled()) {
                  DelegateHarvesterManagerImpl.debugLogger.debug("Activating delegate: " + this.current.getName());
               }

               this.current.activate();
            }

            return this.current.getDelegate();
         } else {
            return null;
         }
      }
   }
}
