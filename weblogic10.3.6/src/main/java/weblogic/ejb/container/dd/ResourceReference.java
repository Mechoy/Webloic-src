package weblogic.ejb.container.dd;

import java.util.Collections;
import java.util.Iterator;

public final class ResourceReference extends BaseDescriptor {
   private static boolean debug = System.getProperty("weblogic.ejb.deployment.debug") != null;
   private String refDesc;
   private String refName;
   private String resType;
   private String resAuthMode;
   private String jndiName;

   public ResourceReference() {
      super((String)null);
   }

   public ResourceReference(String var1, String var2, String var3, String var4) {
      super((String)null);
      this.refDesc = var1;
      this.refName = var2;
      this.setResourceType(var3);
      this.setResourceAuthMode(var4);
   }

   public void setDescription(String var1) {
      if (debug) {
         System.err.println("setDescription(" + var1 + ")");
      }

      this.refDesc = var1;
   }

   public String getDescription() {
      return this.refDesc;
   }

   public void setName(String var1) {
      if (debug) {
         System.err.println("setName(" + var1 + ")");
      }

      this.refName = var1;
   }

   public String getName() {
      return this.refName;
   }

   public void setResourceType(String var1) {
      if (debug) {
         System.err.println("setResourceType(" + var1 + ")");
      }

      this.resType = var1;
   }

   public String getResourceType() {
      return this.resType;
   }

   public void setResourceAuthMode(String var1) {
      if (debug) {
         System.err.println("setResourceAuthMode(" + var1 + ")");
      }

      this.resAuthMode = var1;
   }

   public String getResourceAuthMode() {
      return this.resAuthMode;
   }

   public void setJNDIName(String var1) {
      if (debug) {
         System.err.println("setJNDIName(" + var1 + ")");
      }

      this.jndiName = var1;
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }
}
