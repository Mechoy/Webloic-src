package weblogic.j2ee.dd.xml.validator.annotation;

import javax.xml.ws.WebServiceRef;

public class WebServiceRefAnnotationWrapper extends InjectionAnnotationWrapper {
   private Class type;
   private String mappedName;
   private Class value;
   private String wsdlLocation;

   public WebServiceRefAnnotationWrapper(WebServiceRef var1) {
      this.name = var1.name();
      this.type = var1.type();
      this.mappedName = var1.mappedName();
      this.value = var1.value();
   }

   public Class getType() {
      return this.type;
   }

   public void setType(Class var1) {
      this.type = var1;
   }

   public String getMappedName() {
      return this.mappedName;
   }

   public void setMappedName(String var1) {
      this.mappedName = var1;
   }

   public Class getValue() {
      return this.value;
   }

   public void setValue(Class var1) {
      this.value = var1;
   }

   public String getWsdlLocation() {
      return this.wsdlLocation;
   }

   public void setWsdlLocation(String var1) {
      this.wsdlLocation = var1;
   }

   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + (this.mappedName == null ? 0 : this.mappedName.hashCode());
      var2 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
      var2 = 31 * var2 + (this.type == null ? 0 : this.type.hashCode());
      var2 = 31 * var2 + (this.value == null ? 0 : this.value.hashCode());
      var2 = 31 * var2 + (this.wsdlLocation == null ? 0 : this.wsdlLocation.hashCode());
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
         WebServiceRefAnnotationWrapper var2 = (WebServiceRefAnnotationWrapper)var1;
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

         if (this.type == null) {
            if (var2.type != null) {
               return false;
            }
         } else if (!this.type.equals(var2.type)) {
            return false;
         }

         if (this.value == null) {
            if (var2.value != null) {
               return false;
            }
         } else if (!this.value.equals(var2.value)) {
            return false;
         }

         if (this.wsdlLocation == null) {
            if (var2.wsdlLocation != null) {
               return false;
            }
         } else if (!this.wsdlLocation.equals(var2.wsdlLocation)) {
            return false;
         }

         return true;
      }
   }
}
