package weblogic.xml.crypto.wss.provider;

import weblogic.security.service.ContextHandler;

public interface CredentialProvider {
   String[] getValueTypes();

   Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4);
}
