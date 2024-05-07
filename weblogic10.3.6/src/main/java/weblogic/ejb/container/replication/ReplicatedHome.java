package weblogic.ejb.container.replication;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReplicatedHome extends Remote {
   void becomePrimary(Object var1) throws RemoteException;

   Object createSecondary(Object var1) throws RemoteException;

   Object createSecondaryForBI(Object var1, String var2) throws RemoteException;

   void removeSecondary(Object var1) throws RemoteException;

   void updateSecondary(Object var1, Serializable var2) throws RemoteException;
}
