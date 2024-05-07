package weblogic.ejb20.portable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.HomeHandle;

public final class PortableHomeHandleImpl implements HomeHandle, Serializable {
   static final long serialVersionUID = -7735381439647377868L;
   private EJBHome ejbHome;
   private static final boolean DEBUG = false;

   public PortableHomeHandleImpl(EJBHome var1) {
      this.ejbHome = var1;
   }

   public EJBHome getEJBHome() throws RemoteException {
      return this.ejbHome;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      PortableHandleImpl.delegate().writeEJBHome(this.ejbHome, var1);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.ejbHome = PortableHandleImpl.delegate().readEJBHome(var1);
   }

   private static final void p(String var0) {
      System.err.println("<PortableHomeHandleImpl>: " + var0);
   }
}
