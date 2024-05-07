package weblogic.xml.crypto.wss11.internal.bst;

import javax.security.auth.Subject;
import javax.xml.rpc.handler.MessageContext;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.utils.ClientBSTUtils;
import weblogic.xml.crypto.wss.SecurityTokenValidateResult;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;

public class ClientBSTHandler extends BSTHandler {
   private static ClientBSTHandler instance = new ClientBSTHandler();

   public static final SecurityTokenHandler getInstance() {
      return instance;
   }

   public Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      return null;
   }

   public SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      WSSecurityContext var3 = WSSecurityContext.getSecurityContext(var2);
      ContextHandler var4 = this.getContextHandler(var3, "com.bea.contextelement.xml.SecurityToken", var1);
      return !ClientBSTUtils.isTrusted(var1, var2, var3, var4) ? new SecurityTokenValidateResult(false, "Untrusted token.") : new SecurityTokenValidateResult(true);
   }

   public SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2) {
      return new SecurityTokenValidateResult(true);
   }
}
