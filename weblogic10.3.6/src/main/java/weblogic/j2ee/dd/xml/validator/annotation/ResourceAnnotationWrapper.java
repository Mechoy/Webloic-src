package weblogic.j2ee.dd.xml.validator.annotation;

import javax.annotation.Resource;

public class ResourceAnnotationWrapper extends InjectionAnnotationWrapper {
   private Class type;
   private String authenticationType;
   private boolean shareable;
   private String mappedName;
   private String description;

   public ResourceAnnotationWrapper(Resource var1) {
      this.name = var1.name();
      this.type = var1.type();
      this.authenticationType = var1.authenticationType().getClass().getName();
      this.shareable = var1.shareable();
      this.mappedName = var1.mappedName();
      this.description = var1.description();
   }

   public Class getType() {
      return this.type;
   }

   public void setType(Class var1) {
      this.type = var1;
   }

   public String getAuthenticationType() {
      return this.authenticationType;
   }

   public void setAuthenticationType(String var1) {
      this.authenticationType = var1;
   }

   public boolean isShareable() {
      return this.shareable;
   }

   public void setShareable(boolean var1) {
      this.shareable = var1;
   }

   public String getMappedName() {
      return this.mappedName;
   }

   public void setMappedName(String var1) {
      this.mappedName = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public int hashCode() {
      int var2 = 1;
      var2 = 31 * var2 + (this.authenticationType == null ? 0 : this.authenticationType.hashCode());
      var2 = 31 * var2 + (this.mappedName == null ? 0 : this.mappedName.hashCode());
      var2 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
      var2 = 31 * var2 + (this.shareable ? 1231 : 1237);
      var2 = 31 * var2 + (this.type == null ? 0 : this.type.hashCode());
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
         ResourceAnnotationWrapper var2 = (ResourceAnnotationWrapper)var1;
         if (this.authenticationType == null) {
            if (var2.authenticationType != null) {
               return false;
            }
         } else if (!this.authenticationType.equals(var2.authenticationType)) {
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

         if (this.shareable != var2.shareable) {
            return false;
         } else {
            if (this.type == null) {
               if (var2.type != null) {
                  return false;
               }
            } else if (!this.type.equals(var2.type)) {
               return false;
            }

            return true;
         }
      }
   }
}
