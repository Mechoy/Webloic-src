package weblogic.management.configuration;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteEnumeration extends Remote {
   Object[] getNextBatch() throws RemoteException;

   void close() throws RemoteException;
}
