package weblogic.xml.crypto.dsig.api;

import java.security.spec.AlgorithmParameterSpec;
import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.XMLStructure;

public interface DigestMethod extends XMLStructure, AlgorithmMethod {
   String SHA1_URI = "http://www.w3.org/2000/09/xmldsig#sha1";

   AlgorithmParameterSpec getParameterSpec();
}
