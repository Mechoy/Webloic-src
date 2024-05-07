package weblogic.xml.crypto.dsig;

import java.security.NoSuchAlgorithmException;
import weblogic.xml.crypto.dsig.api.spec.DigestMethodParameterSpec;

public interface DigestMethodFactory {
   String getURI();

   WLDigestMethod newDigestMethod(DigestMethodParameterSpec var1) throws NoSuchAlgorithmException;
}
