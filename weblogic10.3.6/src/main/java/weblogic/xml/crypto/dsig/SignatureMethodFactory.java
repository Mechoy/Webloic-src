package weblogic.xml.crypto.dsig;

import weblogic.xml.crypto.dsig.api.SignatureMethod;

public interface SignatureMethodFactory {
   String getURI();

   SignatureMethod newSignatureMethod();
}
