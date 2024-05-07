package weblogic.xml.crypto.dsig.api;

import java.util.List;
import org.w3c.dom.Element;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;

public interface XMLSignature extends XMLStructure {
   String XMLNS = "http://www.w3.org/2000/09/xmldsig#";

   String getId();

   KeyInfo getKeyInfo();

   List getObjects();

   Element getSignatureNode();

   byte[] getSignatureValue();

   String getSignatureValueId();

   SignedInfo getSignedInfo();

   void sign(XMLSignContext var1) throws MarshalException, XMLSignatureException;

   boolean validate(XMLValidateContext var1) throws XMLSignatureException;
}
