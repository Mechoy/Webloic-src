package weblogic.cluster.migration.example;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MigratableVariable extends Remote {
   void set(Serializable var1) throws RemoteException;

   Serializable get() throws RemoteException;

   String whereAmI() throws RemoteException;
}
