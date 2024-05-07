package weblogic.wsee.security.wst.framework;

import weblogic.xml.crypto.wss.provider.SecurityToken;

public interface TrustToken extends SecurityToken {
   TrustCredential getTrustCredential();
}
