package weblogic.wsee.security.wssc.sct;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.xml.crypto.wss.KeyIdentifierImpl;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;

public class SCTokenReference extends SecurityTokenReferenceImpl {
   public SCTokenReference() {
   }

   public SCTokenReference(SCTokenBase var1) {
      super((QName)null, (String)null, var1);
   }

   public SCTokenReference(QName var1, SCTokenBase var2) throws WSSecurityException {
      super(var1, var2.getValueType(), var2);
      if (WSSConstants.REFERENCE_QNAME.equals(var1)) {
         String var3 = var2.getCredential().getIdentifier();
         if (var3 != null) {
            this.setReferenceURI(var3);
         } else if (var2.getId() != null) {
            this.setReferenceURI("#" + var2.getId());
         }
      } else if (WSSConstants.KEY_IDENTIFIER_QNAME.equals(var1)) {
         KeyIdentifierImpl var4 = new KeyIdentifierImpl(var2.getCredential().getIdentifier().getBytes());
         this.setKeyIdentifier(var4);
      }

   }
}
