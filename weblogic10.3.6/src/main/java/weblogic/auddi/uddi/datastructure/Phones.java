package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class Phones extends UDDIList {
   public Phones() {
   }

   public Phones(Phones var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(Phone var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new Phone(var2));
         }

      }
   }

   public void add(Phone var1) throws UDDIException {
      if (this.size() > 0) {
         if (var1.getUseType() == null) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.useType", "phone"));
         }

         for(Phone var2 = this.getFirst(); var2 != null; var2 = this.getNext()) {
            if (var2.getUseType() == null) {
               throw new FatalErrorException(UDDIMessages.get("error.fatalError.useType", "phone"));
            }
         }
      }

      super.add(var1);
   }

   public Phone getFirst() {
      return (Phone)super.getVFirst();
   }

   public Phone getNext() {
      return (Phone)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
