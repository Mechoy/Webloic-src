package weblogic.xml.crypto.dsig;

import java.security.Key;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;

public interface WLSignatureMethod {
   boolean verify(Key var1, byte[] var2, String var3) throws XMLSignatureException;

   String sign(Key var1, byte[] var2) throws XMLSignatureException;
}
