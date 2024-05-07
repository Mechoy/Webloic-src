package weblogic.xml.crypto.wss;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;

public interface SecurityValidator {
   String SECURITY_TOKEN_INCLUDED_IN_MESSAGE = "weblogic.xml.crypto.wss.SecurityValidator.securityTokenIncludedInMessage";

   XMLSignatureFactory getXMLSignatureFactory();

   XMLEncryptionFactory getXMLEncryptionFactory();

   String getUri(Element var1) throws WSSecurityException;

   Reference getReference(Element var1, DigestMethod var2, List var3) throws WSSecurityException, MarshalException;

   Reference getReference(String var1, String var2, Node var3, DigestMethod var4, List var5) throws WSSecurityException;

   Reference getReference(String var1, String var2, Node var3, DigestMethod var4, List var5, boolean var6) throws WSSecurityException;

   boolean validateTimestamp(short var1) throws WSSecurityException;

   boolean validateSecurityToken(String var1, String var2, Node var3) throws WSSecurityException;

   boolean validateSignature(SignedInfo var1, String var2, String var3, Node var4) throws WSSecurityException;

   boolean validateEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, String var5, Node var6) throws WSSecurityException, XMLEncryptionException;

   boolean hasSecurity();

   Reference getReference(String var1, DigestMethod var2, List var3, boolean var4);
}
