package weblogic.rmi.cluster;

import java.rmi.RemoteException;
import weblogic.cluster.migration.Migratable;
import weblogic.cluster.migration.MigratableActivatingException;
import weblogic.cluster.migration.MigratableInactiveException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.OutboundResponse;
import weblogic.utils.AssertionError;

public final class MigratableServerRef extends ClusterableServerRef {
   public MigratableServerRef(Object var1) throws RemoteException {
      super(var1);
   }

   public MigratableServerRef(int var1, Object var2) throws RemoteException {
      super(var1, var2);
   }

   public final void invoke(RuntimeMethodDescriptor var1, InboundRequest var2, OutboundResponse var3) throws Exception {
      boolean var4 = false;

      int var7;
      try {
         var7 = MigrationManager.singleton().getMigratableState((Migratable)this.getImplementation());
      } catch (ClassCastException var6) {
         throw new AssertionError("Implementation is not of type migratable");
      }

      switch (var7) {
         case 0:
            throw new MigratableInactiveException("Service migrated");
         case 1:
            super.invoke(var1, var2, var3);
            return;
         case 2:
            throw new MigratableActivatingException("Service in the process of migration");
         default:
            throw new AssertionError("Migratable service in unknown state " + var7);
      }
   }
}
