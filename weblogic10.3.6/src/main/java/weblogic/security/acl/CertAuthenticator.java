package weblogic.security.acl;

import java.security.cert.Certificate;

/** @deprecated */
public interface CertAuthenticator {
   User authenticate(String var1, Certificate[] var2, boolean var3);
}
