package weblogic.jndi.internal;

import java.io.IOException;
import org.omg.PortableServer.Servant;
import weblogic.corba.idl.CorbaStub;
import weblogic.rmi.extensions.server.CBVInputStream;
import weblogic.rmi.extensions.server.CBVOutputStream;

public final class JNDIHelper {
   private JNDIHelper() {
   }

   public static final boolean isCorbaObject(Object var0) {
      return var0 instanceof org.omg.CORBA.Object || var0 instanceof Servant || var0 instanceof CorbaStub;
   }

   public static Object copyObject(Object var0) throws IOException, ClassNotFoundException {
      CBVOutputStream var1 = new CBVOutputStream();
      var1.writeObject(var0);
      var1.flush();
      CBVInputStream var2 = var1.makeCBVInputStream();
      Object var3 = var2.readObject();
      var1.close();
      var2.close();
      return var3;
   }
}
