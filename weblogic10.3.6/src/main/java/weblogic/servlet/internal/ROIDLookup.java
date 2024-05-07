package weblogic.servlet.internal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.cluster.replication.ROID;

public interface ROIDLookup extends Remote {
   ROID lookupROID(String var1, String var2, String var3) throws RemoteException;

   void updateLastAccessTimes(ROID[] var1, long[] var2, long var3, String var5) throws RemoteException;

   void unregister(ROID var1, Object[] var2) throws RemoteException;
}
