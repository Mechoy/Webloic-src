package weblogic.xml.util;

import java.util.Vector;

public class Attributes extends ReadOnlyAttributes {
   public Attributes() {
   }

   public Attributes(int var1) {
      super(var1);
   }

   public Attributes(ReadOnlyAttributes var1) {
      Vector var2 = var1.attributes;
      if (var2 != null) {
         int var3 = var2.size();
         this.attributes = new Vector(var3);
         this.attributes.setSize(var3);

         for(int var4 = 0; var4 < var3; ++var4) {
            Attribute var5 = (Attribute)var2.elementAt(var4);
            this.attributes.setElementAt(new Attribute(var5.name, var5.getValue()), var4);
         }
      }

   }

   public Attributes(Vector var1) {
      super(var1);
   }

   public void remove(Name var1) {
      Attribute var2 = this.lookup(var1);
      if (var2 != null) {
         this.attributes.removeElement(var2);
      }

   }

   public Object put(Name var1, Object var2) {
      Attribute var3 = this.lookup(var1);
      Object var4 = null;
      if (var3 != null) {
         var4 = var3.getValue();
         var3.setValue(var2);
      } else {
         this.attributes.addElement(new Attribute(var1, var2));
      }

      return var4;
   }

   public Object put(Attribute var1) {
      Object var2 = null;
      Attribute var3 = this.lookup(var1.getName());
      if (var3 != null) {
         var2 = var3.getValue();
         this.attributes.removeElement(var3);
      }

      this.attributes.addElement(var1);
      return var2;
   }

   public void removeAll() {
      this.attributes.removeAllElements();
   }
}
