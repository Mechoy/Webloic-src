package weblogic.xml.crypto.wss;

import javax.xml.namespace.QName;
import weblogic.xml.crypto.wss.nonce.NonceValidatorFactory;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class UsernameTokenReference extends SecurityTokenReferenceImpl {
   public UsernameTokenReference() {
   }

   public UsernameTokenReference(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      super(var1, var2, var3);
   }

   static void init() {
      register(new UsernameTokenHandler());
      NonceValidatorFactory.getInstance();
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }
}
