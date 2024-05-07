package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class OnlySignEntireHeadersAndBody extends QNameAssertion {
   public static final String ONLY_SIGN_ENTIRE_HEADERS_AND_BODY = "OnlySignEntireHeadersAndBody";

   public QName getName() {
      return new QName(this.getNamespace(), "OnlySignEntireHeadersAndBody", "sp");
   }
}
