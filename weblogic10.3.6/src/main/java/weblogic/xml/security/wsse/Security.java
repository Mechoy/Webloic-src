package weblogic.xml.security.wsse;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Iterator;
import weblogic.xml.security.NamedKey;
import weblogic.xml.security.SecurityProcessingException;
import weblogic.xml.security.UserInfo;
import weblogic.xml.security.encryption.EncryptedKey;
import weblogic.xml.security.encryption.ReferenceList;
import weblogic.xml.security.signature.Signature;
import weblogic.xml.security.specs.EncryptionSpec;
import weblogic.xml.security.specs.SignatureSpec;
import weblogic.xml.security.wsu.Timestamp;

/** @deprecated */
public interface Security {
   String WSSE_VERBOSE_PROP = "weblogic.webservice.security.verbose";
   boolean WSSE_VERBOSE = Boolean.getBoolean("weblogic.webservice.security.verbose");

   Signature addSignature(Token var1) throws SecurityProcessingException;

   Signature addSignature(Token var1, SignatureSpec var2) throws SecurityProcessingException;

   EncryptedKey addEncryption(Token var1, EncryptionSpec var2) throws SecurityProcessingException;

   EncryptedKey addEncryption(X509Certificate var1, EncryptionSpec var2) throws SecurityProcessingException;

   EncryptedKey addEncryption(Token var1) throws SecurityProcessingException;

   EncryptedKey addEncryption(Token var1, NamedKey var2, EncryptionSpec var3) throws SecurityProcessingException;

   ReferenceList addEncryption(NamedKey var1, EncryptionSpec var2) throws SecurityProcessingException;

   /** @deprecated */
   void addBinarySecurityToken(BinarySecurityToken var1);

   /** @deprecated */
   void addUsernameToken(UsernameToken var1);

   Token addToken(Token var1);

   Token addToken(X509Certificate var1, PrivateKey var2);

   Token addToken(UserInfo var1);

   Iterator getUsernameTokens();

   Iterator getBinarySecurityTokens();

   Token getTokenById(String var1);

   Iterator getSignatures();

   Iterator getEncryptedKeys();

   String getRole();

   Iterator getChildren();

   Iterator getTimestamps();

   Timestamp addTimestamp();

   Timestamp addTimestamp(long var1);

   Timestamp addTimestamp(Calendar var1);

   Timestamp addTimestamp(Calendar var1, Calendar var2);

   boolean expired();

   boolean expired(long var1);
}
