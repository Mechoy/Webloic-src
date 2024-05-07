package weblogic.xml.crypto.wss11.internal;

import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public interface SecurityValidator extends weblogic.xml.crypto.wss.SecurityValidator {
   Reference getReference(SecurityToken var1, DigestMethod var2, List var3, boolean var4);

   boolean validateSignature(SignedInfo var1, String var2, List var3, String var4, Node var5) throws WSSecurityException;

   boolean validateEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, List var5, String var6, Node var7) throws WSSecurityException, XMLEncryptionException;

   boolean validateSignatureAndEncryptionRequest(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, ContextHandler var9) throws WSSecurityException, XMLEncryptionException;

   boolean validateSignatureAndEncryptionResponse(SignedInfo var1, List var2, EncryptionMethod var3, ContextHandler var4) throws WSSecurityException, XMLEncryptionException;

   boolean validateSignatureConfirmation();

   boolean validateEncryptionAndSignatureRequest(SignedInfo var1, List var2, EncryptionMethod var3, EncryptionMethod var4, String var5, List var6, String var7, boolean var8, ContextHandler var9) throws WSSecurityException, XMLEncryptionException;

   boolean validateEncryptionforEncryptFirst(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, List var5, String var6, Node var7, Map var8) throws WSSecurityException, XMLEncryptionException;
}
