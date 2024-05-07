package weblogic.xml.crypto.wss;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.wss.provider.Purpose;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public interface SecurityBuilder {
   XMLSignatureFactory getXMLSignatureFactory();

   XMLEncryptionFactory getXMLEncryptionFactory();

   String assignUri(Element var1) throws WSSecurityException;

   Reference createReference(String var1, String var2, DigestMethod var3, List var4, boolean var5, ContextHandler var6) throws WSSecurityException;

   Reference createSTRReference(SecurityToken var1, DigestMethod var2, List var3, boolean var4) throws WSSecurityException;

   SecurityToken createSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException, MarshalException;

   boolean addTimestamp(short var1, ContextHandler var2) throws WSSecurityException, MarshalException;

   SecurityToken addSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException, MarshalException;

   Node addSignature(SignedInfo var1, String var2, String var3, boolean var4, ContextHandler var5) throws WSSecurityException, MarshalException;

   Node addSignature(SignedInfo var1, Reference var2, ContextHandler var3) throws WSSecurityException, MarshalException;

   boolean addEncryption(List var1, EncryptionMethod var2, EncryptionMethod var3, String var4, String var5, boolean var6, ContextHandler var7) throws WSSecurityException, MarshalException, XMLEncryptionException;
}
