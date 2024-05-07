package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.wss.UsernameTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;
import weblogic.xml.dom.marshal.MarshalException;

public class OnBehalfOf extends TrustDOMStructure {
   public static final String NAME = "OnBehalfOf";
   private SecurityToken token;
   private SecurityTokenReference str;
   protected SecurityTokenHandler tokenHandler;

   public OnBehalfOf() {
   }

   public OnBehalfOf(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public String getName() {
      return "OnBehalfOf";
   }

   public void setTokenHandler(SecurityTokenHandler var1) {
      this.tokenHandler = var1;
   }

   public SecurityTokenHandler getTokenHandler() {
      return this.tokenHandler;
   }

   public void setSecurityToken(SecurityToken var1) {
      this.token = var1;
   }

   public SecurityToken getSecurityToken() {
      return this.token;
   }

   public void setSecurityTokenReference(SecurityTokenReference var1) {
      this.str = var1;
   }

   public SecurityTokenReference getSecurityTokenReference() {
      return this.str;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.str == null && this.token == null) {
         throw new MarshalException("SecurityToken and SecurityTokenReference can not both be null.");
      } else {
         createNamespacePrefix(var2, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
         createNamespacePrefix(var2, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
         if (this.str != null) {
            this.str.marshal(var1, (Node)null, var2);
         }

         if (this.token != null) {
            this.token.marshal(var1, (Node)null, var2);
         }

      }
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var2 = getFirstElement(var1);
      if (var2 == null) {
         throw new MarshalException("SecurityToken or SecurityTokenReference must be presented in " + this.getName());
      } else {
         SecurityTokenHandler var3 = this.getTokenHandler(var2);
         if (var3 == null) {
            throw new MarshalException("No SecurityTokenHandler for " + var2.getLocalName() + " in " + this.getName());
         } else {
            if (var2.getLocalName().equals("SecurityTokenReference")) {
               this.str = var3.newSecurityTokenReference(var2);
            } else {
               try {
                  this.token = var3.newSecurityToken(var2);
               } catch (weblogic.xml.crypto.api.MarshalException var5) {
                  throw new MarshalException(var5);
               }
            }

         }
      }
   }

   private SecurityTokenHandler getTokenHandler(Element var1) {
      return (SecurityTokenHandler)(this.tokenHandler != null ? this.tokenHandler : new UsernameTokenHandler());
   }
}
