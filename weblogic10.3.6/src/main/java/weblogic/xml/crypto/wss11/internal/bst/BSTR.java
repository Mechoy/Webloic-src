package weblogic.xml.crypto.wss11.internal.bst;

import java.security.cert.X509Certificate;
import javax.xml.namespace.QName;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.wss.BinarySecurityTokenReference;
import weblogic.xml.crypto.wss.KeyIdentifierImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class BSTR extends BinarySecurityTokenReference {
   public BSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      super(var1, var2, var3);
   }

   protected void init(QName var1, String var2, SecurityToken var3) throws WSSecurityException {
      if (WSSConstants.KEY_IDENTIFIER_QNAME.equals(var1) && "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1".equals(var2)) {
         this.initThumbprint(var3);
      } else {
         super.init(var1, var2, var3);
      }

   }

   private void initThumbprint(SecurityToken var1) throws WSSecurityException {
      this.setValueType("http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1");
      X509Certificate var2 = ((BinarySecurityToken)var1).getCertificate();
      this.setKeyIdentifier(new KeyIdentifierImpl(CertUtils.getThumbprint(var2)));
   }
}
