package weblogic.xml.crypto.wss11.internal;

import java.util.List;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityException;

public interface SecurityBuilder extends weblogic.xml.crypto.wss.SecurityBuilder {
   String STRICT = "Strict";
   String LAX_TS_FIRST = "LaxTimestampFirst";
   String LAX_TS_LAST = "LaxTimestampLast";
   String LAX = "Lax";
   int ACTION_ENCRYPT = 8;
   int ACTION_SIGN = 16;
   int ACTION_DERIVED_KEY = 512;
   int ACTION_ENDORSE = 1024;
   int ACTION_SIGN_ENDOSE = 3072;
   int ACTION_ENCRYPT_BEFORE_SIGN = 4096;
   int ACTION_ENCRYPT_SIGNATURE = 8192;
   int ACTION_SIGN_ENDORSE_ENCRYPT_ENCRYPTSIGNATURE = 9240;

   void setLayout(String var1);

   void setWSSVersion(String var1);

   SignatureConfirmation[] addSignatureConfirmation(String[] var1, ContextHandler var2) throws MarshalException, WSSecurityException;

   void addSignatureAndEncryption(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, String var6, boolean var7, ContextHandler var8) throws WSSecurityException, MarshalException, XMLEncryptionException;

   void addSignatureAndEncryption(SignedInfo var1, List var2, EncryptionMethod var3, ContextHandler var4) throws WSSecurityException, MarshalException, XMLEncryptionException;

   void addSignature(SignedInfo var1, EncryptionMethod var2, String var3, String var4, boolean var5, ContextHandler var6) throws WSSecurityException, MarshalException, XMLEncryptionException;

   void addSignature(SignedInfo var1, ContextHandler var2) throws MarshalException, WSSecurityException;

   void addEncryption(List var1, EncryptionMethod var2, ContextHandler var3) throws MarshalException, WSSecurityException;

   void addSignatureAndEncryption(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, ContextHandler var9) throws WSSecurityException, MarshalException, XMLEncryptionException;

   void addSignature(SignedInfo var1, EncryptionMethod var2, String var3, List var4, String var5, boolean var6, ContextHandler var7) throws WSSecurityException, MarshalException, XMLEncryptionException;

   Reference createReference(String var1, List var2, String var3, DigestMethod var4, List var5, boolean var6, ContextHandler var7) throws WSSecurityException;

   Node addSignature(SignedInfo var1, String var2, List var3, String var4, boolean var5, ContextHandler var6) throws WSSecurityException, MarshalException;

   boolean addEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, List var5, String var6, boolean var7, ContextHandler var8) throws WSSecurityException, MarshalException, XMLEncryptionException;

   Node addSignature(SignedInfo var1, Reference var2, List var3, ContextHandler var4) throws WSSecurityException, MarshalException;

   boolean isCredentialAvailable(String var1);

   void addSignatureAndEncryptionAndEndorsing(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, int var9, SignedInfo var10, String var11, List var12, String var13, boolean var14, ContextHandler var15) throws WSSecurityException, MarshalException, XMLEncryptionException;

   void addSignatureAndEncryptionAndEndorsing(SignedInfo var1, List var2, EncryptionMethod var3, int var4, SignedInfo var5, String var6, List var7, String var8, boolean var9, ContextHandler var10) throws WSSecurityException, MarshalException, XMLEncryptionException;
}
