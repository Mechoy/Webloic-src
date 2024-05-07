package weblogic.xml.crypto.wss11.internal.enckey;

import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.utils.EncryptedKeyUtils;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;

public class EncryptedKeyCredentialProvider implements CredentialProvider {
   public String[] getValueTypes() {
      return WSS11Constants.ENCRYPTED_KEY_VALUE_TYPES;
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      EncryptionMethod var5 = (EncryptionMethod)var3.getValue("weblogic.wsee.ek.encrypt_method");
      return var5 != null ? EncryptedKeyUtils.generateKey(var5) : null;
   }
}
