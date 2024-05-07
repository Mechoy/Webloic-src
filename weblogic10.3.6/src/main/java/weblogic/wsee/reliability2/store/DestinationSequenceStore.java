package weblogic.wsee.reliability2.store;

import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.reliability2.sequence.DestinationSequence;

public class DestinationSequenceStore extends SequenceStore<DestinationSequence> {
   private static final String CONNECTION_NAME = DestinationSequenceStore.class.getName();
   private static final DestinationSequenceStore _instance = new DestinationSequenceStore();

   public static DestinationSequenceStore getOrCreateStore(String var0, DestinationSequenceMap var1) throws StoreException {
      if (storeExists(var0, CONNECTION_NAME)) {
         return (DestinationSequenceStore)_instance.getOrCreateLogicalStore(var0, CONNECTION_NAME);
      } else {
         DestinationSequenceStore var2 = new DestinationSequenceStore(var0, false);
         var2.setParentMap(var1);
         var2.addAvailablePhysicalStores();
         addStore(var0, CONNECTION_NAME, var2);
         return var2;
      }
   }

   private DestinationSequenceStore() {
   }

   DestinationSequenceStore(String var1) throws StoreException {
      super(var1, CONNECTION_NAME);
   }

   private DestinationSequenceStore(String var1, boolean var2) throws StoreException {
      super(var1, CONNECTION_NAME, var2);
   }

   public DestinationSequenceStore createLogicalStore(String var1, String var2) throws StoreException {
      return new DestinationSequenceStore(var1);
   }

   static {
      _instance.toString();
   }
}
