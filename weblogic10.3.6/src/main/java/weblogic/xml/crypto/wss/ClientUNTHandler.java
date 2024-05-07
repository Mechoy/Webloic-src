package weblogic.xml.crypto.wss;

import javax.security.auth.Subject;
import javax.xml.rpc.handler.MessageContext;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;

/** @deprecated */
public class ClientUNTHandler extends UsernameTokenHandler {
   private static ClientUNTHandler instance = new ClientUNTHandler();

   public Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      return null;
   }

   public SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) throws WSSecurityException {
      return new SecurityTokenValidateResult(true);
   }

   public SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2) {
      return new SecurityTokenValidateResult(true);
   }

   public static SecurityTokenHandler getInstance() {
      return instance;
   }
}
