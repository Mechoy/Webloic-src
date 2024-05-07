package weblogic.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteLifeCycleOperations extends Remote {
   void shutdown() throws ServerLifecycleException, RemoteException;

   void shutdown(int var1, boolean var2) throws ServerLifecycleException, RemoteException;

   void forceShutdown() throws ServerLifecycleException, RemoteException;

   void suspend() throws ServerLifecycleException, RemoteException;

   void suspend(int var1, boolean var2) throws ServerLifecycleException, RemoteException;

   void forceSuspend() throws ServerLifecycleException, RemoteException;

   void resume() throws ServerLifecycleException, RemoteException;

   String getState() throws RemoteException;

   void setState(String var1, String var2) throws RemoteException;

   String getWeblogicHome() throws RemoteException;

   String getMiddlewareHome() throws RemoteException;
}
