package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;

public class IdentifierBag extends KRBag {
   public IdentifierBag() {
   }

   public IdentifierBag(IdentifierBag var1) throws UDDIException {
      super(var1);
   }

   public String toXML() {
      return super.toXML("identifierBag");
   }
}
