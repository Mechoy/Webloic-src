package weblogic.wsee.reliability2.store;

import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.Storable;
import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.reliability2.sequence.Sequence;

public abstract class SequenceStore<S extends Sequence> extends LogicalStore<String, S> {
   private static final Logger LOGGER = Logger.getLogger(SequenceStore.class.getName());
   private SequenceMap<S> _parentMap;

   protected SequenceStore() {
   }

   void setParentMap(SequenceMap<S> var1) {
      this._parentMap = var1;
   }

   protected SequenceStore(String var1, String var2) throws StoreException {
      this(var1, var2, true);
   }

   protected SequenceStore(String var1, String var2, boolean var3) throws StoreException {
      super(var1, var2, var3);
   }

   public SequenceStoreConnection createStoreConnection(String var1, String var2) throws StoreException {
      return new SequenceStoreConnection(this.getName(), var1, var2);
   }

   public boolean addPhysicalStore(String var1) throws StoreException {
      return super.addPhysicalStore(var1) && this._parentMap != null ? this.addPhysicalStoreToParentMap(var1) : false;
   }

   private boolean addPhysicalStoreToParentMap(String var1) {
      StoreConnection var2 = this.getStoreConnectionInternal(var1);
      if (var2 == null) {
         return false;
      } else {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Sequence var5 = (Sequence)var2.get(var4);
            if (this._parentMap.get(var5.getId()) != null) {
               this._parentMap.startupSequence(var5);
            }
         }

         return true;
      }
   }

   public StoreConnection<String, S> removePhysicalStore(String var1) throws StoreException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("RM SequenceStore '" + this.getName() + "' Removing physical store '" + var1 + "'");
      }

      StoreConnection var2 = this.getStoreConnectionInternal(var1);
      if (var2 == null) {
         return var2;
      } else {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Sequence var5 = this._parentMap.get(var4);
            if (var5 != null) {
               this._parentMap.shutdownSequence(var5);
            }
         }

         return super.removePhysicalStore(var1);
      }
   }

   public S get(Object var1) {
      return (Sequence)super.get(var1);
   }

   public S put(String var1, S var2) {
      if (var1 == null) {
         String var3 = this.getNextPhysicalStoreName();
         var1 = WsUtil.generateRoutableUUID(var3);
         var2.setId(var1);
         var2.setPhysicalStoreName(var3);
      }

      return (Sequence)super.put((Serializable)var1, (Storable)var2);
   }

   public S remove(Object var1) {
      Sequence var2 = (Sequence)super.remove(var1);
      if (var2 != null) {
         var2.destroy();
      }

      return var2;
   }
}
