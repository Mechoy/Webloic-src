package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class Addresses extends UDDIList {
   public Addresses() {
   }

   public Addresses(Addresses var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(Address var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new Address(var2));
         }

      }
   }

   public void add(Address var1) throws UDDIException {
      super.add(var1);
   }

   public Address getFirst() {
      return (Address)super.getVFirst();
   }

   public Address getNext() {
      return (Address)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
