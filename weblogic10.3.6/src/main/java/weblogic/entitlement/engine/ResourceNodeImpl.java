package weblogic.entitlement.engine;

import java.util.ArrayList;
import weblogic.security.spi.Resource;

public class ResourceNodeImpl implements ResourceNode {
   private String name;
   private String[] namePath;
   private Resource resource;

   public ResourceNodeImpl(Resource var1) {
      if (var1 == null) {
         throw new NullPointerException("null resource");
      } else {
         this.resource = var1;
      }
   }

   public Resource getResource() {
      return this.resource;
   }

   public String getName() {
      if (this.name == null) {
         this.name = this.resource.toString();
      }

      return this.name;
   }

   public ResourceNode getParent() {
      Resource var1 = this.resource.getParentResource();
      return var1 == null ? null : new ResourceNodeImpl(var1);
   }

   public String[] getNamePathToRoot() {
      if (this.namePath == null) {
         ArrayList var1 = new ArrayList();
         var1.add(this.getName());

         for(Resource var2 = this.resource.getParentResource(); var2 != null; var2 = var2.getParentResource()) {
            var1.add(var2.toString());
         }

         this.namePath = (String[])((String[])var1.toArray(new String[var1.size()]));
      }

      return this.namePath;
   }
}
