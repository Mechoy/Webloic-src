package weblogic.ejb.spi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BusinessObject extends Remote {
   BusinessHandle _WL_getBusinessObjectHandle() throws RemoteException;
}
