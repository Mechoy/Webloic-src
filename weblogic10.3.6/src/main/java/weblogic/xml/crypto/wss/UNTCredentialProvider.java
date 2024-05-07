package weblogic.xml.crypto.wss;

import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.wss.provider.CredentialProvider;
import weblogic.xml.crypto.wss.provider.Purpose;

public class UNTCredentialProvider implements CredentialProvider {
   public String[] getValueTypes() {
      return WSSConstants.UNT_VALUETYPES;
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      return null;
   }
}
