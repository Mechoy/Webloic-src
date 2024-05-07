package weblogic.entitlement.engine.cache;

import javax.security.auth.Subject;
import weblogic.security.spi.Resource;
import weblogic.utils.HashCodeUtil;

public class Key {
   private Resource resource;
   private Subject subject;

   public Key(Resource var1, Subject var2) {
      this.resource = var1;
      this.subject = var2;
   }

   public Resource getResource() {
      return this.resource;
   }

   void setResource(Resource var1) {
      this.resource = var1;
   }

   public Subject getSubject() {
      return this.subject;
   }

   void setSubject(Subject var1) {
      this.subject = var1;
   }

   void setValues(Resource var1, Subject var2) {
      this.resource = var1;
      this.subject = var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Key)) {
         return false;
      } else {
         Key var2 = (Key)var1;
         return (this.resource == var2.resource || this.resource != null && this.resource.equals(var2.resource)) && this.subject == var2.subject;
      }
   }

   public int hashCode() {
      int var1 = 23;
      var1 = HashCodeUtil.hash(var1, this.resource);
      var1 = HashCodeUtil.hash(var1, System.identityHashCode(this.subject));
      return var1;
   }
}
