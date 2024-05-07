package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public abstract class KRBag extends UDDIBag implements Serializable {
   public KRBag() {
   }

   public KRBag(KRBag var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(KeyedReference var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new KeyedReference(var2));
         }

      }
   }

   public void add(KeyedReference var1) throws UDDIException {
      String var2 = var1.getKey();
      super.add(var1, var2);
   }

   public KeyedReference getFirst() {
      return (KeyedReference)super.getMFirst();
   }

   public KeyedReference getNext() {
      return (KeyedReference)super.getMNext();
   }
}
