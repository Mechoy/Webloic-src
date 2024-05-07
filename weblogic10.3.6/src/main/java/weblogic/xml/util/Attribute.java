package weblogic.xml.util;

public class Attribute {
   Name name;
   Object value;

   public Attribute() {
   }

   public Attribute(Name var1, Object var2) {
      this.name = var1;
      this.value = var2;
   }

   public Name getName() {
      return this.name;
   }

   public Object getValue() {
      return this.value;
   }

   void setValue(Object var1) {
      this.value = var1;
   }
}
