package weblogic.xml.crypto.wss.api;

import java.util.Calendar;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public interface UsernameToken extends SecurityToken {
   String getUsername();

   byte[] getPassword();

   String getPasswordDigest();

   String getPasswordType();

   String getEncodedNonce();

   String getNonceEncodingType();

   Calendar getCreated();
}
