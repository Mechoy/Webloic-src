package weblogic.factories.iiop;

import weblogic.iiop.IOR;
import weblogic.iiop.IORFactory;
import weblogic.iiop.ObjectKey;

public final class IORFactoryImpl implements IORFactory {
   public IOR createIOR(String var1, int var2, String var3, int var4, int var5) {
      return new IOR(ObjectKey.getTypeId(var3), var1, var2, ObjectKey.getBootstrapKey(var3), (byte)var4, (byte)var5);
   }
}
