package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.wss.SecurityTokenContextHandler;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.dom.marshal.MarshalException;

public class RequestedSecurityToken extends TrustDOMStructure {
   private static final long serialVersionUID = 1274568700804020443L;
   public static final String NAME = "RequestedSecurityToken";
   private static final SecurityTokenContextHandler EMPTY_CONTEXT = new SecurityTokenContextHandler();
   private transient SecurityToken securityToken;
   private transient SecurityTokenHandler tokenHandler;
   private String tokenType;

   public RequestedSecurityToken() {
   }

   public RequestedSecurityToken(String var1) {
      this.namespaceUri = var1;
   }

   public void setTokenHandler(SecurityTokenHandler var1) {
      this.tokenHandler = var1;
   }

   public SecurityTokenHandler getTokenHandler() {
      return this.tokenHandler;
   }

   public void setSecurityToken(SecurityToken var1) {
      this.securityToken = var1;
   }

   public SecurityToken getSecurityToken() {
      return this.securityToken;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.securityToken == null) {
         throw new MarshalException("Security token can not be null");
      } else {
         this.securityToken.marshal(var1, (Node)null, var2);
      }
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var2 = getFirstElement(var1);
      if (var2 == null) {
         throw new MarshalException("RequestedSecurityToken can not be empty.");
      } else if (this.tokenHandler == null) {
         throw new MarshalException("Can not find valid security token in RequestedSecurityToken: " + var2);
      } else {
         try {
            this.securityToken = this.tokenHandler.newSecurityToken(var2);
            Object var3 = this.securityToken.getCredential();
            SecurityToken var4 = this.tokenHandler.getSecurityToken(this.tokenType, var3, EMPTY_CONTEXT);
            if (var4 != null) {
               this.securityToken = var4;
            }

         } catch (weblogic.xml.crypto.api.MarshalException var5) {
            throw new MarshalException(var5);
         } catch (WSSecurityException var6) {
            throw new MarshalException(var6);
         }
      }
   }

   public String getName() {
      return "RequestedSecurityToken";
   }

   public String getTokenType() {
      return this.tokenType;
   }

   public void setTokenType(String var1) {
      this.tokenType = var1;
   }
}
