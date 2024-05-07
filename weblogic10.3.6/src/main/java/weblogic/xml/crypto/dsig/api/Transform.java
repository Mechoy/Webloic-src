package weblogic.xml.crypto.dsig.api;

import java.security.spec.AlgorithmParameterSpec;
import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.XMLStructure;

public interface Transform extends XMLStructure, AlgorithmMethod {
   String BASE64_URI = "http://www.w3.org/2000/09/xmldsig#base64";
   String ENVELOPED_URI = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
   String XPATH_URI = "http://www.w3.org/TR/1999/REC-xpath-19991116";
   String XPATH2_URI = "http://www.w3.org/2002/06/xmldsig-filter2";
   String XSLT_URI = "http://www.w3.org/TR/1999/REC-xslt-19991116";

   AlgorithmParameterSpec getParameterSpec();
}
