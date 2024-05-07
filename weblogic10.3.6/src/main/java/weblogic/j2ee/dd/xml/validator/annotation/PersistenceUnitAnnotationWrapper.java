package weblogic.j2ee.dd.xml.validator.annotation;

import javax.persistence.PersistenceUnit;

public class PersistenceUnitAnnotationWrapper extends InjectionAnnotationWrapper {
   private String unitName;

   public PersistenceUnitAnnotationWrapper(PersistenceUnit var1) {
      this.name = var1.name();
      this.unitName = var1.unitName();
   }

   public String getUnitName() {
      return this.unitName;
   }

   public void setUnitName(String var1) {
      this.unitName = var1;
   }

   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
      var2 = 31 * var2 + (this.unitName == null ? 0 : this.unitName.hashCode());
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         PersistenceUnitAnnotationWrapper var2 = (PersistenceUnitAnnotationWrapper)var1;
         if (this.name == null) {
            if (var2.name != null) {
               return false;
            }
         } else if (!this.name.equals(var2.name)) {
            return false;
         }

         if (this.unitName == null) {
            if (var2.unitName != null) {
               return false;
            }
         } else if (!this.unitName.equals(var2.unitName)) {
            return false;
         }

         return true;
      }
   }
}
