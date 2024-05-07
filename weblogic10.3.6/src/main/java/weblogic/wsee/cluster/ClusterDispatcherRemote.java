package weblogic.wsee.cluster;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClusterDispatcherRemote extends Remote {
   Serializable dispatch(String var1, Serializable var2) throws RemoteException;
}
