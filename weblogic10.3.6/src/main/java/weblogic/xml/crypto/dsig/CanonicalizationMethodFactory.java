package weblogic.xml.crypto.dsig;

import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;

public interface CanonicalizationMethodFactory {
   String getURI();

   CanonicalizationMethod newCanonicalizationMethod();
}
