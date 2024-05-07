package weblogic.webservice.async;

import java.rmi.RemoteException;

/** @deprecated */
public interface FutureResult {
   boolean isCompleted();

   void waitFor(long var1);

   void abort();

   Object getResult() throws RemoteException;
}
