package weblogic.wsee.persistence;

public interface LogicalStoreListChangeListener {
   void logicalStoreAdded(String var1);

   void logicalStorePreRemoval(String var1);

   void logicalStoreRemoved(String var1);
}
