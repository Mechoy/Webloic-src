package weblogic.xml.util;

import java.util.Enumeration;
import java.util.Vector;

public class ReadOnlyAttributes {
   Vector attributes;

   public ReadOnlyAttributes() {
      this.attributes = new Vector();
   }

   public ReadOnlyAttributes(Vector var1) {
      this.attributes = var1;
   }

   public ReadOnlyAttributes(int var1) {
      this.attributes = new Vector(var1);
   }

   public int size() {
      return this.attributes.size();
   }

   public Object get(Name var1) {
      Attribute var2 = this.lookup(var1);
      return var2 == null ? null : var2.getValue();
   }

   public Enumeration attributes() {
      return this.attributes.elements();
   }

   public Attribute lookup(Name var1) {
      Enumeration var2 = this.attributes.elements();

      Attribute var3;
      do {
         if (!var2.hasMoreElements()) {
            return null;
         }

         var3 = (Attribute)var2.nextElement();
      } while(var3.name != var1);

      return var3;
   }

   public String toString() {
      return this.getClass().getName();
   }
}
