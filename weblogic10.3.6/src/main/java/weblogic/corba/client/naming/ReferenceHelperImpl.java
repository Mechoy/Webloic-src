package weblogic.corba.client.naming;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import weblogic.corba.j2ee.naming.ContextImpl;
import weblogic.rmi.extensions.server.ReferenceHelper;

public class ReferenceHelperImpl extends ReferenceHelper {
   private static final boolean DEBUG = false;

   public Object narrow(Object var1, Class var2) throws NamingException {
      return PortableRemoteObject.narrow(var1, var2);
   }

   public Object replaceObject(Object var1) throws IOException {
      Object var2 = var1;
      if (var1 instanceof ContextImpl && ((ContextImpl)var1).getContext() != null) {
         var2 = ((ContextImpl)var1).getContext();
      } else if (!(var1 instanceof org.omg.CORBA.Object) && var1 instanceof Remote) {
         var2 = PortableRemoteObject.toStub((Remote)var1);
      }

      return var2;
   }

   public void exportObject(Remote var1) throws RemoteException {
      PortableRemoteObject.exportObject(var1);
   }

   public void unexportObject(Remote var1) throws NoSuchObjectException {
      PortableRemoteObject.unexportObject(var1);
   }

   public Remote toStub(Remote var1) throws NoSuchObjectException {
      return PortableRemoteObject.toStub(var1);
   }

   static void p(String var0) {
      System.err.println("<ReferenceHelperImpl> " + var0);
   }
}
