package weblogic.servlet.internal.session;

import java.io.IOException;
import java.rmi.UnmarshalException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import weblogic.utils.io.Replacer;

public class SessionObjectReplacer implements Replacer {
   private Replacer nextReplacer;

   public static SessionObjectReplacer getInstance() {
      return SessionObjectReplacer.SingletonMaker.singleton;
   }

   private SessionObjectReplacer() {
   }

   public void insertReplacer(Replacer var1) {
      this.nextReplacer = var1;
   }

   public Object replaceObject(Object var1) throws IOException {
      Object var2;
      if (var1 instanceof EJBHome) {
         EJBHome var3 = (EJBHome)var1;
         var2 = new EJBHomeAttributeWrapper(var3.getHomeHandle());
      } else if (var1 instanceof EJBObject) {
         EJBObject var4 = (EJBObject)var1;
         var2 = new EJBAttributeWrapper(var4.getHandle());
      } else {
         var2 = this.nextReplacer == null ? var1 : this.nextReplacer.replaceObject(var1);
      }

      return var2;
   }

   public Object resolveObject(Object var1) throws IOException {
      Object var2;
      EJBAttributeWrapper var3;
      HomeHandle var4;
      if (var1 instanceof EJBHomeAttributeWrapper) {
         var3 = (EJBAttributeWrapper)var1;
         var4 = null;

         try {
            var4 = (HomeHandle)var3.getObject();
         } catch (ClassNotFoundException var7) {
            throw new UnmarshalException("Failed to unmarshal home handle");
         }

         var2 = var4.getEJBHome();
      } else if (var1 instanceof EJBAttributeWrapper) {
         var3 = (EJBAttributeWrapper)var1;
         var4 = null;

         Handle var8;
         try {
            var8 = (Handle)var3.getObject();
         } catch (ClassNotFoundException var6) {
            throw new UnmarshalException("Failed to unmarshal eo handle");
         }

         var2 = var8.getEJBObject();
      } else {
         var2 = this.nextReplacer == null ? var1 : this.nextReplacer.resolveObject(var1);
      }

      return var2;
   }

   // $FF: synthetic method
   SessionObjectReplacer(Object var1) {
      this();
   }

   private static class SingletonMaker {
      static final SessionObjectReplacer singleton = new SessionObjectReplacer();
   }
}
