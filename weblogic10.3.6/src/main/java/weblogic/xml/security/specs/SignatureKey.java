package weblogic.xml.security.specs;

import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class SignatureKey extends EntityDescriptor {
   public SignatureKey(String var1, String var2) {
      super(var1, var2);
   }

   public SignatureKey(XMLInputStream var1) throws XMLStreamException {
      super(var1);
   }

   public String elementTagName() {
      return "signatureKey";
   }
}
