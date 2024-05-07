package weblogic.ejb.container.dd;

import java.util.Collections;
import java.util.Iterator;

public final class FieldDescriptor extends BaseDescriptor {
   private static boolean debug = System.getProperty("weblogic.ejb.deployment.debug") != null;
   private String fieldName;
   private String fieldDescription;

   public FieldDescriptor() {
      super((String)null);
   }

   public FieldDescriptor(String var1) {
      super((String)null);
      this.fieldName = var1;
   }

   public FieldDescriptor(String var1, String var2) {
      super((String)null);
      this.fieldName = var1;
      this.fieldDescription = var2;
   }

   public void setDescription(String var1) {
      if (debug) {
         System.err.println("setDescription(" + var1 + ")");
      }

      this.fieldDescription = var1;
   }

   public String getDescription() {
      return this.fieldDescription;
   }

   public void setName(String var1) {
      if (debug) {
         System.err.println("setName(" + var1 + ")");
      }

      this.fieldName = var1;
   }

   public String getName() {
      return this.fieldName;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof FieldDescriptor)) {
         return false;
      } else {
         FieldDescriptor var2 = (FieldDescriptor)var1;
         return var2.getName().equals(this.getName());
      }
   }

   public int hashCode() {
      return this.getName().hashCode();
   }
}
