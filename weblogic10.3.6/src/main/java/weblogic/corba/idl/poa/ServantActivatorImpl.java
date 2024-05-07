package weblogic.corba.idl.poa;

import org.omg.CORBA_2_3.portable.ObjectImpl;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantActivator;
import org.omg.PortableServer.ServantActivatorHelper;

public class ServantActivatorImpl extends ObjectImpl implements ServantActivator {
   public String[] _ids() {
      return new String[]{ServantActivatorHelper.id()};
   }

   public Servant incarnate(byte[] var1, POA var2) throws ForwardRequest {
      return null;
   }

   public void etherealize(byte[] var1, POA var2, Servant var3, boolean var4, boolean var5) {
   }
}
