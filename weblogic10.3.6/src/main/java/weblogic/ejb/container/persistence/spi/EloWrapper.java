package weblogic.ejb.container.persistence.spi;

import javax.ejb.EJBLocalObject;

public final class EloWrapper {
   private EJBLocalObject elo = null;

   public EloWrapper(EJBLocalObject var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.elo = var1;
      }
   }

   public EJBLocalObject getEJBLocalObject() {
      return this.elo;
   }

   public Object getPrimaryKey() {
      return this.elo.getPrimaryKey();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof EloWrapper)) {
         return false;
      } else {
         EloWrapper var2 = (EloWrapper)var1;
         return this.getPrimaryKey().equals(var2.getPrimaryKey());
      }
   }

   public int hashCode() {
      return this.getPrimaryKey().hashCode();
   }
}
