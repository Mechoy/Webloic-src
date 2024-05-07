package weblogic.iiop;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.rmi.CORBA.PortableRemoteObjectDelegate;
import weblogic.rmi.extensions.PortableRemoteObject;

public final class PortableRemoteObjectDelegateImpl implements PortableRemoteObjectDelegate {
   public void exportObject(Remote var1) throws RemoteException {
      PortableRemoteObject.exportObject(var1);
   }

   public Remote toStub(Remote var1) throws NoSuchObjectException {
      return PortableRemoteObject.toStub(var1);
   }

   public void unexportObject(Remote var1) throws NoSuchObjectException {
      PortableRemoteObject.unexportObject(var1);
   }

   public Object narrow(Object var1, Class var2) {
      return PortableRemoteObject.narrow(var1, var2);
   }

   public void connect(Remote var1, Remote var2) throws RemoteException {
   }
}
