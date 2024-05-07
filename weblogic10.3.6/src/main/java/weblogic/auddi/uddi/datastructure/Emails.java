package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;

public class Emails extends UDDIList {
   public Emails() {
   }

   public Emails(Emails var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         for(Email var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new Email(var2));
         }

      }
   }

   public void add(Email var1) throws UDDIException {
      super.add(var1);
   }

   public Email getFirst() {
      return (Email)super.getVFirst();
   }

   public Email getNext() {
      return (Email)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
