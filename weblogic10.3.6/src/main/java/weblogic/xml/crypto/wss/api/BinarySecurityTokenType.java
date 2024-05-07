package weblogic.xml.crypto.wss.api;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import javax.xml.rpc.handler.MessageContext;
import weblogic.xml.crypto.wss.BSTEncodingException;

public interface BinarySecurityTokenType {
   String getValueType();

   String getKeyIdentifierValueType();

   byte[] getUnencodedValue(Object var1) throws BSTEncodingException;

   byte[] getKeyIdRefValue(Object var1) throws BSTEncodingException;

   PrivateKey getPrivateKey(Object var1);

   X509Certificate getCertificate(Object var1);

   PublicKey getPublicKey(Object var1);

   SecretKey getSecretKey(Object var1);

   Object getCredentials(byte[] var1);

   boolean validate(BinarySecurityToken var1, MessageContext var2);
}
