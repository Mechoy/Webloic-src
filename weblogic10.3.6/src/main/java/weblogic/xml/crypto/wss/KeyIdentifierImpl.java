package weblogic.xml.crypto.wss;

import weblogic.xml.crypto.wss.api.KeyIdentifier;

public class KeyIdentifierImpl implements KeyIdentifier {
   private byte[] identifier;
   private String encoding;
   private static final String DEFAULT_ENCODING = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";

   public KeyIdentifierImpl(byte[] var1) {
      this(var1, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
   }

   public KeyIdentifierImpl(byte[] var1, String var2) {
      this.identifier = var1;
      if (var2 != null && !var2.equals("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary")) {
         throw new IllegalArgumentException("Unsupported " + WSSConstants.ENCODING_TYPE_QNAME + ": " + var2);
      } else {
         this.encoding = var2;
      }
   }

   public byte[] getIdentifier() {
      return this.identifier;
   }

   public String getEncodingType() {
      return this.encoding;
   }
}
