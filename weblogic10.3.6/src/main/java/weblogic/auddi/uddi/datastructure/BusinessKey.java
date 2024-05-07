package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.InvalidKeyPassedException;

public class BusinessKey extends UDDIKey implements Serializable {
   public BusinessKey() throws FatalErrorException {
   }

   public BusinessKey(String var1) throws InvalidKeyPassedException {
      super(var1, false);
   }

   public BusinessKey(BusinessKey var1) {
      super(var1);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BusinessKey)) {
         return false;
      } else {
         BusinessKey var2 = (BusinessKey)var1;
         boolean var3 = super.equals(var2);
         return var3;
      }
   }

   String getElementName() {
      return "businessKey";
   }
}
