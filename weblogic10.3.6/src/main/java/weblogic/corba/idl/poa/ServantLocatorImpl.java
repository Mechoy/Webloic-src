package weblogic.corba.idl.poa;

import org.omg.CORBA_2_3.portable.ObjectImpl;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantLocator;
import org.omg.PortableServer.ServantLocatorHelper;
import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;

public class ServantLocatorImpl extends ObjectImpl implements ServantLocator {
   public String[] _ids() {
      return new String[]{ServantLocatorHelper.id()};
   }

   public void postinvoke(byte[] var1, POA var2, String var3, Object var4, Servant var5) {
   }

   public Servant preinvoke(byte[] var1, POA var2, String var3, CookieHolder var4) throws ForwardRequest {
      return null;
   }
}
