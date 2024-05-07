package weblogic.j2ee.dd.xml.validator.annotation;

import javax.ejb.EJB;

public class EJBAnnotationWrapper extends InjectionAnnotationWrapper {
   private String description;
   private String beanName;
   private Class beanInterface;
   private String mappedName;

   public EJBAnnotationWrapper(EJB var1) {
      this.name = var1.name();
      this.beanName = var1.beanName();
      this.beanInterface = var1.beanInterface();
      this.mappedName = var1.mappedName();
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getBeanName() {
      return this.beanName;
   }

   public void setBeanName(String var1) {
      this.beanName = var1;
   }

   public Class getBeanInterface() {
      return this.beanInterface;
   }

   public void setBeanInterface(Class var1) {
      this.beanInterface = var1;
   }

   public String getMappedName() {
      return this.mappedName;
   }

   public void setMappedName(String var1) {
      this.mappedName = var1;
   }

   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + (this.beanInterface == null ? 0 : this.beanInterface.hashCode());
      var2 = 31 * var2 + (this.beanName == null ? 0 : this.beanName.hashCode());
      var2 = 31 * var2 + (this.mappedName == null ? 0 : this.mappedName.hashCode());
      var2 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
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
         EJBAnnotationWrapper var2 = (EJBAnnotationWrapper)var1;
         if (this.beanInterface == null) {
            if (var2.beanInterface != null) {
               return false;
            }
         } else if (!this.beanInterface.getName().equals(var2.beanInterface.getName())) {
            return false;
         }

         if (this.beanName == null) {
            if (var2.beanName != null) {
               return false;
            }
         } else if (!this.beanName.equals(var2.beanName)) {
            return false;
         }

         if (this.mappedName == null) {
            if (var2.mappedName != null) {
               return false;
            }
         } else if (!this.mappedName.equals(var2.mappedName)) {
            return false;
         }

         if (this.name == null) {
            if (var2.name != null) {
               return false;
            }
         } else if (!this.name.equals(var2.name)) {
            return false;
         }

         return true;
      }
   }
}
