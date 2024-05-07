package weblogic.xml.crypto.dsig.api;

import java.security.spec.AlgorithmParameterSpec;

public interface CanonicalizationMethod extends Transform {
   String WITH_COMMENTS_EXC_URI = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
   String WITH_COMMENTS_URI = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
   String WITHOUT_COMMENTS_EXC_URI = "http://www.w3.org/2001/10/xml-exc-c14n#";
   String WITHOUT_COMMENTS_URI = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";

   AlgorithmParameterSpec getParameterSpec();
}
