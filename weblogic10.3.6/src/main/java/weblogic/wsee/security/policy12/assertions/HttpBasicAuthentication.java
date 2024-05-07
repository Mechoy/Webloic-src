package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class HttpBasicAuthentication extends QNameAssertion {
   public static final String HTTP_BASIC_AUTHENTICATION = "HttpBasicAuthentication";

   public QName getName() {
      return new QName(this.getNamespace(), "HttpBasicAuthentication", "sp");
   }
}
