package weblogic.wsee.jaxrpc;

import java.rmi.RemoteException;

public interface Dispatch {
   JAXRPCContext getContext();

   Object invoke(Object var1) throws RemoteException;
}
