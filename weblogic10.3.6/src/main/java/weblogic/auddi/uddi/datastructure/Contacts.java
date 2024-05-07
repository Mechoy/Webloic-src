package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;

public class Contacts extends UDDIList implements Serializable {
   public Contacts() {
   }

   public Contacts(Contacts var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException("parameter to copy constructor was null");
      } else {
         for(Contact var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new Contact(var2));
         }

      }
   }

   public void add(Contact var1) throws UDDIException {
      if (var1 != null) {
         super.add(var1);
      }
   }

   public Contact getFirst() {
      return (Contact)super.getVFirst();
   }

   public Contact getNext() {
      return (Contact)super.getVNext();
   }

   public String toXML() {
      return super.toXML("contacts");
   }
}
