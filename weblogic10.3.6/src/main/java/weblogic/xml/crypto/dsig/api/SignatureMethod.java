package weblogic.xml.crypto.dsig.api;

import java.security.spec.AlgorithmParameterSpec;
import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.XMLStructure;

public interface SignatureMethod extends XMLStructure, AlgorithmMethod {
   String DSA_SHA1_URI = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
   String HMAC_SHA1_URI = "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
   String RSA_SHA1_URI = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";

   AlgorithmParameterSpec getParameterSpec();
}
