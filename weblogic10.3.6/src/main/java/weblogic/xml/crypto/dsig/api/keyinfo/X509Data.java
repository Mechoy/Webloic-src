package weblogic.xml.crypto.dsig.api.keyinfo;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface X509Data extends XMLStructure {
   String RAW_X509_CERTIFICATE_TYPE = "http://www.w3.org/2000/09/xmldsig#rawX509Certificate";
   String TYPE = "http://www.w3.org/2000/09/xmldsig#X509Data";

   List getContent();
}
