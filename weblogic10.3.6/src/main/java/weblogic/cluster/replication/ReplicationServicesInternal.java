package weblogic.cluster.replication;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.rmi.spi.HostID;

public interface ReplicationServicesInternal extends Remote {
   Object create(HostID var1, int var2, ROID var3, Replicatable var4) throws RemoteException;

   void update(ROID var1, int var2, Serializable var3, Object var4) throws NotFoundException, RemoteException;

   void updateOneWay(ROID var1, int var2, Serializable var3, Object var4) throws NotFoundException, RemoteException;

   ReplicationManager.ROObject fetch(ROID var1) throws RemoteException, NotFoundException;

   void remove(ROID[] var1, Object var2) throws RemoteException;

   void removeOneWay(ROID[] var1, Object var2) throws RemoteException;

   void remove(ROID[] var1) throws RemoteException;

   void update(AsyncBatch var1) throws RemoteException;
}
