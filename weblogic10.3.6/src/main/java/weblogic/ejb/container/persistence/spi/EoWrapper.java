package weblogic.ejb.container.persistence.spi;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import weblogic.utils.StackTraceUtils;

public final class EoWrapper {
   private EJBObject eo = null;

   public EoWrapper(EJBObject var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.eo = var1;
      }
   }

   public EJBObject getEJBObject() {
      return this.eo;
   }

   public Object getPrimaryKey() {
      try {
         return this.eo.getPrimaryKey();
      } catch (RemoteException var2) {
         throw new IllegalArgumentException(StackTraceUtils.throwable2StackTrace(var2));
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof EoWrapper)) {
         return false;
      } else {
         EoWrapper var2 = (EoWrapper)var1;
         return this.getPrimaryKey().equals(var2.getPrimaryKey());
      }
   }

   public int hashCode() {
      return this.getPrimaryKey().hashCode();
   }
}
