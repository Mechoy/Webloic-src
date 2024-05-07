package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.InvalidKeyPassedException;

public class ServiceKey extends UDDIKey implements Serializable {
   public ServiceKey() throws FatalErrorException {
   }

   public ServiceKey(String var1) throws InvalidKeyPassedException {
      super(var1, false);
   }

   public ServiceKey(ServiceKey var1) {
      super(var1);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ServiceKey)) {
         return false;
      } else {
         ServiceKey var2 = (ServiceKey)var1;
         boolean var3 = super.equals(var2);
         return var3;
      }
   }

   String getElementName() {
      return "serviceKey";
   }
}
