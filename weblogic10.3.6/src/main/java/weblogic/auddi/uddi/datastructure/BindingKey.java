package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.InvalidKeyPassedException;

public class BindingKey extends UDDIKey implements Serializable {
   public BindingKey() throws FatalErrorException {
   }

   public BindingKey(String var1) throws InvalidKeyPassedException {
      super(var1, false);
   }

   public BindingKey(BindingKey var1) {
      super(var1);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BindingKey)) {
         return false;
      } else {
         BindingKey var2 = (BindingKey)var1;
         boolean var3 = super.equals(var2);
         return var3;
      }
   }

   String getElementName() {
      return "bindingKey";
   }
}
