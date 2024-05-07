package weblogic.xml.crypto.dom;

import weblogic.xml.crypto.dsig.api.XMLSignature;

public interface WLDOMSignContext {
   XMLSignature getXMLSignature();

   void setXMLSignature(XMLSignature var1);
}
