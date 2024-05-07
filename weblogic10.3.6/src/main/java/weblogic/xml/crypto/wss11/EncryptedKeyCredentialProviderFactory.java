package weblogic.xml.crypto.wss11;

import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss11.internal.enckey.EncryptedKeyCredentialProvider;

public class EncryptedKeyCredentialProviderFactory {
   private static EncryptedKeyCredentialProvider instance = new EncryptedKeyCredentialProvider();

   public static final CredentialProvider getEncryptedKeyCredentialProvider() {
      return instance;
   }
}
