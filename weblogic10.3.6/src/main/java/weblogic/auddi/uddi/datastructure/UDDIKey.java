package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.InvalidKeyPassedException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.util.UUIDFormatValidator;
import weblogic.auddi.util.uuid.UUIDException;
import weblogic.auddi.util.uuid.UUIDGeneratorFactory;

public abstract class UDDIKey extends UDDIListObject implements Serializable {
   private String m_key = null;

   public UDDIKey() throws FatalErrorException {
      try {
         this.m_key = UUIDGeneratorFactory.getGenerator().uuidGen();
      } catch (UUIDException var2) {
         throw new FatalErrorException("Failure while attempting to generate a UUID key", var2);
      }
   }

   public UDDIKey(String var1, boolean var2) throws InvalidKeyPassedException {
      if (!UUIDFormatValidator.validate(var1, var2)) {
         throw new InvalidKeyPassedException(UDDIMessages.get("error.invalidKeyPassed." + this.getElementName(), var1));
      } else {
         if (var2) {
            this.m_key = var1.substring(5);
         } else {
            this.m_key = var1;
         }

      }
   }

   public UDDIKey(UDDIKey var1) {
      this.m_key = var1.m_key;
   }

   public String getKey() {
      return this.m_key;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof UDDIKey)) {
         return false;
      } else {
         UDDIKey var2 = (UDDIKey)var1;
         boolean var3 = true;
         var3 &= this.m_key.equalsIgnoreCase(var2.m_key);
         return var3;
      }
   }

   public int hashCode() {
      return this.getKey().toLowerCase().hashCode();
   }

   public String toString() {
      return this.getKey();
   }

   public String toXML() {
      return "<" + this.getElementName() + ">" + this.getKey() + "</" + this.getElementName() + ">";
   }

   abstract String getElementName();
}
