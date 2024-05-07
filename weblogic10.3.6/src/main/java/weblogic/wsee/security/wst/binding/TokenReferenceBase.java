package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.marshal.MarshalException;

public abstract class TokenReferenceBase extends TrustDOMStructure {
   private SecurityTokenReference str;
   protected SecurityTokenHandler tokenHandler;

   public void setTokenHandler(SecurityTokenHandler var1) {
      this.tokenHandler = var1;
   }

   public SecurityTokenHandler getTokenHandler() {
      return this.tokenHandler;
   }

   public void setSecurityTokenReference(SecurityTokenReference var1) {
      this.str = var1;
   }

   public SecurityTokenReference getSecurityTokenReference() {
      return this.str;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.str == null) {
         throw new MarshalException("SecurityTokenReference can not be null");
      } else {
         createNamespacePrefix(var2, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
         createNamespacePrefix(var2, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
         this.str.marshal(var1, (Node)null, var2);
      }
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var2 = getFirstElement(var1);
      if (var2 == null) {
         throw new MarshalException("SecurityTokenReference must be presented in " + this.getName());
      } else if (this.tokenHandler == null) {
         throw new MarshalException("SecurityTokenHandler must be set in " + this.getName());
      } else {
         this.str = this.tokenHandler.newSecurityTokenReference(var2);
      }
   }
}
