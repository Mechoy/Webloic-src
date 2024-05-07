package weblogic.ejb20.portable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.spi.HandleDelegate;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class PortableHandleImpl implements Handle, Serializable {
   static final long serialVersionUID = -7104018438195312776L;
   private EJBObject ejbObject;
   private static final boolean DEBUG = false;

   public PortableHandleImpl(EJBObject var1) {
      this.ejbObject = var1;
   }

   public EJBObject getEJBObject() throws RemoteException {
      return this.ejbObject;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      delegate().writeEJBObject(this.ejbObject, var1);
   }

   static HandleDelegate delegate() throws IOException {
      Object var0 = null;

      try {
         var0 = (HandleDelegate)(new InitialContext()).lookup("java:comp/HandleDelegate");
      } catch (NamingException var2) {
         var0 = new HandleDelegateImpl();
      }

      return (HandleDelegate)var0;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.ejbObject = delegate().readEJBObject(var1);
   }

   private static final void p(String var0) {
      System.err.println("<PortableHandleImpl>: " + var0);
   }
}
