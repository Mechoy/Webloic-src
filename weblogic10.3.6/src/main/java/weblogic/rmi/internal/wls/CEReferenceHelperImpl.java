package weblogic.rmi.internal.wls;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.naming.NamingException;
import weblogic.rmi.extensions.server.ReferenceHelper;
import weblogic.rmi.extensions.server.ServerHelper;

public class CEReferenceHelperImpl extends ReferenceHelper {
   public void exportObject(Remote var1) throws RemoteException {
      ServerHelper.exportObject(var1);
   }

   public Object narrow(Object var1, Class var2) throws NamingException {
      return var1;
   }

   public Object replaceObject(Object var1) throws IOException {
      return var1;
   }

   public Remote toStub(Remote var1) throws NoSuchObjectException {
      return var1;
   }

   public void unexportObject(Remote var1) throws NoSuchObjectException {
      ServerHelper.unexportObject(var1, true, false);
   }
}
