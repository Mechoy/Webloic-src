package weblogic.xml.crypto.dsig;

import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;

public interface WLTransform extends Transform, WLXMLStructure {
   Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException;
}
