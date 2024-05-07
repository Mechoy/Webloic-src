package weblogic.cluster.replication;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import weblogic.rmi.spi.HostID;

public interface ReplicationServices {
   ROInfo register(Replicatable var1);

   ROInfo add(ROID var1, Replicatable var2);

   Replicatable registerLocally(HostID var1, ROID var2, Object var3) throws RemoteException;

   Replicatable lookup(ROID var1, Object var2) throws NotFoundException;

   Replicatable invalidationLookup(ROID var1, Object var2) throws NotFoundException;

   Object getSecondaryInfo(ROID var1) throws NotFoundException;

   void unregister(ROID var1, Object var2);

   void unregister(ROID[] var1, Object var2);

   void removeOrphanedSecondary(ROID var1, Object var2);

   Object updateSecondary(ROID var1, Serializable var2, Object var3) throws NotFoundException;

   Iterator ids();
}
