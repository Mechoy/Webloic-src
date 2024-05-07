package weblogic.factories.tgiop;

import javax.naming.NamingException;
import weblogic.iiop.IOR;
import weblogic.iiop.IORFactory;
import weblogic.tgiop.TGIOPObjectKey;

public final class IORFactoryImpl implements IORFactory {
   public IOR createIOR(String var1, int var2, String var3, int var4, int var5) {
      try {
         return new IOR(TGIOPObjectKey.getInitialRefInterfaceName(var3), "", 0, new TGIOPObjectKey(var3, var1), (byte)var4, (byte)var5);
      } catch (NamingException var7) {
         return null;
      }
   }
}
