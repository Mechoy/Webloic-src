package weblogic.xml.crypto.wss.api;

import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.dom.marshal.WLDOMStructure;

public interface Security extends WLDOMStructure {
   void add(XMLStructure var1, XMLCryptoContext var2, ContextHandler var3) throws WSSecurityException, MarshalException;

   Node add(XMLSignature var1, KeyProvider var2, ContextHandler var3) throws WSSecurityException, MarshalException;
}
