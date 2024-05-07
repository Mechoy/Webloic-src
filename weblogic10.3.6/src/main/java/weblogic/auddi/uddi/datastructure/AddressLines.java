package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class AddressLines extends UDDIList {
   public AddressLines() {
   }

   public AddressLines(AddressLines var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(AddressLine var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new AddressLine(var2));
         }

      }
   }

   public void add(AddressLine var1) throws UDDIException {
      super.add(var1);
   }

   public AddressLine getFirst() {
      return (AddressLine)super.getVFirst();
   }

   public AddressLine getNext() {
      return (AddressLine)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
