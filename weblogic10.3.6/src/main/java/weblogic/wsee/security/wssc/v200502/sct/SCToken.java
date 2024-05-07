package weblogic.wsee.security.wssc.v200502.sct;

import java.io.Serializable;
import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.wsee.security.wssc.v200502.WSCConstants;
import weblogic.wsee.security.wst.framework.TrustToken;

public class SCToken extends SCTokenBase implements TrustToken, Serializable {
   private static final long serialVersionUID = -2498262085668898417L;

   public SCToken() {
   }

   public SCToken(weblogic.wsee.security.wssc.sct.SCCredential var1) {
      super(var1);
   }

   protected QName getSCT_IDENTIFIER_QNAME() {
      return WSCConstants.SCT_IDENTIFIER_QNAME;
   }

   protected QName getSCT_QNAME() {
      return WSCConstants.SCT_QNAME;
   }

   protected String getSCT_VALUE_TYPE() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc/sct";
   }

   protected String getXMLNS_WSC() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc";
   }
}
