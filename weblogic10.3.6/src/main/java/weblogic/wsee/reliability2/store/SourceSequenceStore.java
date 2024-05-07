package weblogic.wsee.reliability2.store;

import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.reliability2.sequence.SourceSequence;

public class SourceSequenceStore extends SequenceStore<SourceSequence> {
   private static final String CONNECTION_NAME = SourceSequenceStore.class.getName();
   private static final SourceSequenceStore _instance = new SourceSequenceStore();

   public static SourceSequenceStore getOrCreateStore(String var0, SourceSequenceMap var1) throws StoreException {
      if (storeExists(var0, CONNECTION_NAME)) {
         return (SourceSequenceStore)_instance.getOrCreateLogicalStore(var0, CONNECTION_NAME);
      } else {
         SourceSequenceStore var2 = new SourceSequenceStore(var0, false);
         var2.setParentMap(var1);
         var2.addAvailablePhysicalStores();
         addStore(var0, CONNECTION_NAME, var2);
         return var2;
      }
   }

   private SourceSequenceStore() {
   }

   SourceSequenceStore(String var1) throws StoreException {
      super(var1, CONNECTION_NAME);
   }

   private SourceSequenceStore(String var1, boolean var2) throws StoreException {
      super(var1, CONNECTION_NAME, var2);
   }

   public SourceSequenceStore createLogicalStore(String var1, String var2) throws StoreException {
      return new SourceSequenceStore(var1);
   }

   static {
      _instance.toString();
   }
}
