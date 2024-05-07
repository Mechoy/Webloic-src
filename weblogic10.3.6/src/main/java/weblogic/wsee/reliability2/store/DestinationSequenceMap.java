package weblogic.wsee.reliability2.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;
import weblogic.kernel.KernelStatus;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.reliability2.saf.DestinationSequenceSAFMap;
import weblogic.wsee.reliability2.sequence.DestinationSequence;

public class DestinationSequenceMap extends TimedSequenceMap<DestinationSequence> {
   private DestinationSequenceSAFMap _safMap;

   public DestinationSequenceMap() throws PersistentStoreException, NamingException {
      if (KernelStatus.isServer()) {
         this._safMap = new DestinationSequenceSAFMap();
      } else {
         this._safMap = null;
      }

   }

   protected SequenceStore<DestinationSequence> getOrCreateSequenceStore(String var1) throws StoreException {
      return DestinationSequenceStore.getOrCreateStore(var1, this);
   }

   public int size() {
      int var1 = super.size();
      if (this._safMap != null) {
         var1 += this._safMap.size();
      }

      return var1;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsKey(Object var1) {
      return super.containsKey(var1) || this._safMap != null && this._safMap.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      if (super.containsValue(var1)) {
         return true;
      } else {
         throw new IllegalStateException("Not supported");
      }
   }

   public DestinationSequence get(Object var1) {
      if (!(var1 instanceof String)) {
         return null;
      } else {
         String var2 = (String)var1;
         if (super.containsKey(var2)) {
            return (DestinationSequence)super.get(var2);
         } else {
            return this._safMap == null ? null : this._safMap.get(var2);
         }
      }
   }

   protected DestinationSequence internalPut(String var1, DestinationSequence var2, SequenceStore<DestinationSequence> var3) {
      if (var2.isNonBuffered()) {
         return (DestinationSequence)super.internalPut(var1, var2, var3);
      } else if (this._safMap == null) {
         throw new IllegalStateException("Persistent DestinationSequence detected, but SAF is not available");
      } else {
         String var4 = var3.getNextPhysicalStoreName();
         var2.setPhysicalStoreName(var4);
         return this._safMap.put(var1, var2);
      }
   }

   public boolean startupSequence(DestinationSequence var1) {
      if (var1.isNonBuffered()) {
         return super.startupSequence(var1);
      } else {
         var1.startup();
         return true;
      }
   }

   public boolean shutdownSequence(DestinationSequence var1) {
      if (var1 != null) {
         if (var1.isNonBuffered()) {
            return super.shutdownSequence(var1);
         } else {
            var1.shutdown();
            return true;
         }
      } else {
         return super.shutdownSequence(var1);
      }
   }

   public DestinationSequence remove(Object var1) {
      String var2 = (String)var1;
      DestinationSequence var3 = this.get(var2);
      return var3 != null && !var3.isNonBuffered() ? this._safMap.remove(var1) : (DestinationSequence)super.remove(var1);
   }

   public void putAll(Map<? extends String, ? extends DestinationSequence> var1) {
      throw new IllegalStateException("Not supported");
   }

   public void clear() {
      throw new IllegalStateException("Not supported");
   }

   public Set<String> keySet() {
      HashSet var1 = new HashSet();
      var1.addAll(super.keySet());
      if (this._safMap != null) {
         var1.addAll(this._safMap.keySet());
      }

      return var1;
   }

   public Collection<DestinationSequence> values() {
      Set var1 = this.keySet();
      ArrayList var2 = new ArrayList(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         DestinationSequence var5 = this.get(var4);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }

   public Set<Map.Entry<String, DestinationSequence>> entrySet() {
      throw new IllegalStateException("Not supported");
   }
}
