package weblogic.ejb.container.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import javax.rmi.CORBA.Stub;
import weblogic.iiop.IIOPReplacer;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;

public final class HandleDelegateImpl extends weblogic.ejb.container.portable.HandleDelegateImpl {
   protected void writeStub(Remote var1, ObjectOutputStream var2) throws IOException {
      if (var1 instanceof StubInfoIntf && !(var1 instanceof Stub)) {
         StubInfo var4 = ((StubInfoIntf)var1).getStubInfo();
         var2.writeObject(var4);
      } else if (!(var1 instanceof weblogic.corba.rmi.Stub) && var1 instanceof Stub) {
         super.writeStub(var1, var2);
      } else {
         Object var3 = IIOPReplacer.getIIOPReplacer().replaceObject(var1);
         var2.writeObject(var3);
      }

   }

   protected Object readStub(ObjectInputStream var1, Class var2) throws IOException, ClassNotFoundException {
      Object var3 = var1.readObject();
      Object var4 = IIOPReplacer.getIIOPReplacer().resolveObject(var3);
      return PortableRemoteObject.narrow(var4, var2);
   }
}
