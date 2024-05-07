package weblogic.xml.crypto.wss11.internal;

import org.w3c.dom.Node;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.dom.marshal.WLDOMStructure;

public interface SignatureConfirmation extends XMLStructure, WLDOMStructure {
   String getId();

   String getSignatureValue();

   Node getSignatureConfirmationNode();
}
