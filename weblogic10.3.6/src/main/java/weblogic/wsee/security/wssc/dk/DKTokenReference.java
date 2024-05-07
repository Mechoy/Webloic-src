package weblogic.wsee.security.wssc.dk;

import weblogic.wsee.security.wssc.base.dk.DKTokenBase;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;

public class DKTokenReference extends SecurityTokenReferenceImpl {
   public DKTokenReference() {
   }

   public DKTokenReference(DKTokenBase var1) throws WSSecurityException {
      super(WSSConstants.REFERENCE_QNAME, var1.getValueType(), var1);
      this.setReferenceURI("#" + var1.getId());
   }
}
