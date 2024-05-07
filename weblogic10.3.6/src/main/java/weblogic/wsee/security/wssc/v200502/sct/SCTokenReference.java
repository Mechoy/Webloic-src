package weblogic.wsee.security.wssc.v200502.sct;

import javax.xml.namespace.QName;
import weblogic.xml.crypto.wss.KeyIdentifierImpl;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;

public class SCTokenReference extends SecurityTokenReferenceImpl {
   public SCTokenReference() {
   }

   public SCTokenReference(QName var1, SCToken var2) throws WSSecurityException {
      super(var1, var2.getValueType(), var2);
      if (WSSConstants.REFERENCE_QNAME.equals(var1)) {
         if (var2.getId() != null) {
            this.setReferenceURI("#" + var2.getId());
         }
      } else if (WSSConstants.KEY_IDENTIFIER_QNAME.equals(var1)) {
         KeyIdentifierImpl var3 = new KeyIdentifierImpl(((SCCredential)var2.getCredential()).getIdentifier().getBytes());
         this.setKeyIdentifier(var3);
      }

   }
}
