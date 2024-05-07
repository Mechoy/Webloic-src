package weblogic.ejb.container.portable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.spi.HandleDelegate;
import weblogic.rmi.extensions.PortableRemoteObject;

public class HandleDelegateImpl implements HandleDelegate {
   public void writeEJBObject(EJBObject var1, ObjectOutputStream var2) throws IOException {
      this.writeStub(var1, var2);
   }

   public EJBObject readEJBObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      return (EJBObject)this.readStub(var1, EJBObject.class);
   }

   public void writeEJBHome(EJBHome var1, ObjectOutputStream var2) throws IOException {
      this.writeStub(var1, var2);
   }

   public EJBHome readEJBHome(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      return (EJBHome)this.readStub(var1, EJBHome.class);
   }

   protected void writeStub(Remote var1, ObjectOutputStream var2) throws IOException {
      var2.writeObject(var1);
   }

   protected Object readStub(ObjectInputStream var1, Class var2) throws IOException, ClassNotFoundException {
      return PortableRemoteObject.narrow(var1.readObject(), var2);
   }
}
