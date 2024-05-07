package weblogic.xml.crypto.dsig.api.keyinfo;

import java.util.List;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.URIReference;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;

public interface RetrievalMethod extends URIReference, XMLStructure {
   Data dereference(XMLCryptoContext var1) throws URIReferenceException;

   List getTransforms();

   String getURI();
}
