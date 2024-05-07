package weblogic.ejb.container.internal;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import weblogic.ejb20.internal.HandleImpl;
import weblogic.ejb20.internal.HomeHandleImpl;
import weblogic.ejb20.portable.PortableHandleImpl;
import weblogic.ejb20.portable.PortableHomeHandleImpl;

public final class HandleReplacer {
   private static HandleReplacer handleReplacer = null;

   public static HandleReplacer getReplacer() {
      if (handleReplacer == null) {
         handleReplacer = new HandleReplacer();
      }

      return handleReplacer;
   }

   public Serializable replaceObject(Serializable var1) throws IOException {
      if (var1 instanceof HandleImpl) {
         HandleImpl var5 = (HandleImpl)var1;
         EJBObject var6 = var5.getEJBObject();
         PortableHandleImpl var7 = new PortableHandleImpl(var6);
         return var7;
      } else if (var1 instanceof HomeHandleImpl) {
         HomeHandleImpl var2 = (HomeHandleImpl)var1;
         EJBHome var3 = var2.getEJBHome();
         PortableHomeHandleImpl var4 = new PortableHomeHandleImpl(var3);
         return var4;
      } else {
         return var1;
      }
   }
}
