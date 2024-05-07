package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.InvalidKeyPassedException;

public class TModelKey extends UDDIKey implements Serializable {
   private String m_prefix = "uuid:";

   public TModelKey() throws FatalErrorException {
   }

   public TModelKey(String var1) throws InvalidKeyPassedException, FatalErrorException {
      super(var1, true);
   }

   public TModelKey(TModelKey var1) throws FatalErrorException {
      super(var1);
   }

   public String getKey() {
      return this.m_prefix + super.getKey();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TModelKey)) {
         return false;
      } else {
         TModelKey var2 = (TModelKey)var1;
         boolean var3 = super.equals(var2);
         return var3;
      }
   }

   String getElementName() {
      return "tModelKey";
   }
}
