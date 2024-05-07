package weblogic.ejb.spi;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import weblogic.ejb20.portable.PortableEJBMetaDataImpl;
import weblogic.ejb20.portable.PortableHandleImpl;
import weblogic.ejb20.portable.PortableHomeHandleImpl;

public final class EJBPortableReplacer {
   private static boolean DEBUG = false;

   public static EJBPortableReplacer getReplacer() {
      return EJBPortableReplacer.ReplacerMaker.PORTABLE_REPLACER;
   }

   public Serializable replaceObject(Serializable var1) throws IOException {
      if (DEBUG) {
         System.out.println("<EJBPortableReplacer> replaceObject: " + (var1 == null ? "null" : var1.getClass().getName()));
      }

      if (var1 instanceof PortableReplaceable) {
         if (var1 instanceof Handle) {
            Handle var6 = (Handle)var1;
            EJBObject var7 = var6.getEJBObject();
            PortableHandleImpl var8 = new PortableHandleImpl(var7);
            return var8;
         }

         if (var1 instanceof HomeHandle) {
            HomeHandle var5 = (HomeHandle)var1;
            EJBHome var3 = var5.getEJBHome();
            PortableHomeHandleImpl var4 = new PortableHomeHandleImpl(var3);
            return var4;
         }

         if (var1 instanceof EJBMetaData) {
            PortableEJBMetaDataImpl var2 = new PortableEJBMetaDataImpl((EJBMetaData)var1);
            return var2;
         }
      }

      if (DEBUG) {
         System.out.println("<EJBPortableReplacer> replaced with: " + (var1 == null ? "null" : var1.getClass().getName()));
      }

      return var1;
   }

   private static final class ReplacerMaker {
      private static final EJBPortableReplacer PORTABLE_REPLACER = new EJBPortableReplacer();
   }
}
