package weblogic.wsee.persistence;

import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreRecord;

public final class StoreRecord {
   private final PersistentStoreRecord record;
   private StoreRecord next;

   public StoreRecord(PersistentStoreRecord var1) {
      this.record = var1;
   }

   public Object getStoreObject() throws PersistentStoreException {
      return this.record.getData();
   }

   public PersistentHandle getHandle() {
      return this.record.getHandle();
   }

   public final void setNext(StoreRecord var1) {
      this.next = var1;
   }

   public final StoreRecord getNext() {
      return this.next;
   }
}
