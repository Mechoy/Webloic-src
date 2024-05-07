package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssGssKerberosV5ApReqToken11 extends QNameAssertion {
   public static final String WSS_GSS_KERBEROS_V5_AP_REQ_TOKEN11 = "WssGssKerberosV5ApReqToken11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssGssKerberosV5ApReqToken11", "sp");
   }
}
