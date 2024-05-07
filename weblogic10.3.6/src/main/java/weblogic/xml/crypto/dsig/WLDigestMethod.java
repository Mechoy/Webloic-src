package weblogic.xml.crypto.dsig;

import java.security.MessageDigest;
import weblogic.xml.crypto.dsig.api.DigestMethod;

public interface WLDigestMethod extends WLXMLStructure, DigestMethod {
   String SHA256_URI = "http://www.w3.org/2001/04/xmlenc#sha256";
   String SHA512_URI = "http://www.w3.org/2001/04/xmlenc#sha512";

   byte[] digest(byte[] var1);

   MessageDigest getMessageDigest();
}
