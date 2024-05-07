package weblogic.xml.crypto.wss;

import weblogic.xml.crypto.wss.api.Encoding;
import weblogic.xml.security.encryption.Utils;

public class Base64Encoding implements Encoding {
   public String encode(byte[] var1) {
      return Utils.base64(var1);
   }

   public byte[] decode(String var1) {
      return Utils.base64(var1);
   }

   public String getURI() {
      return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
   }
}
