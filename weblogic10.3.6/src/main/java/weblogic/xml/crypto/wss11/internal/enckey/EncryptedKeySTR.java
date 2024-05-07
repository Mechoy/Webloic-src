package weblogic.xml.crypto.wss11.internal.enckey;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.KeyIdentifierImpl;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;

public class EncryptedKeySTR extends SecurityTokenReferenceImpl {
   public EncryptedKeySTR() {
   }

   public static void init() {
      register(new EncryptedKeyTokenHandler());
   }

   public EncryptedKeySTR(QName var1, EncryptedKeyToken var2) {
      super(var1, (String)null, var2);
      if (WSSConstants.KEY_IDENTIFIER_QNAME.equals(var1)) {
         EncryptedKeyProvider var3 = (EncryptedKeyProvider)var2.getCredential();
         if (var3 == null) {
            throw new IllegalArgumentException("EncryptedKeySRT of type " + var1 + " requires CipherValue in token.");
         }

         this.setValueType("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1");
         this.setKeyIdentifier(new KeyIdentifierImpl(var3.getIdentifier()));
      } else {
         if (!WSSConstants.REFERENCE_QNAME.equals(var1)) {
            throw new IllegalArgumentException("Unsupported SecurityTokenReference type for EncryptedKey: " + var1);
         }

         this.setValueType("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
         this.setReferenceURI("#" + var2.getId());
      }

   }

   protected void marshalAttributes(Element var1, Map var2) {
      String var3 = getPrefix(var2, "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd", "wsse11");
      DOMUtils.addPrefixedAttribute(var1, WSS11Constants.TOKEN_TYPE_QNAME, var3, "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey");
      DOMUtils.declareNamespace(var1, "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd", var3);
   }
}
