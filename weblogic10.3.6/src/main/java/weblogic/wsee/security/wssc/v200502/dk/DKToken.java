package weblogic.wsee.security.wssc.v200502.dk;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.dk.DKTokenBase;
import weblogic.wsee.security.wssc.dk.DKCredential;
import weblogic.wsee.security.wssc.v200502.WSCConstants;
import weblogic.wsee.security.wssc.v200502.faults.BadContextTokenException;
import weblogic.wsee.security.wssc.v200502.faults.UnknownDerivationSourceException;
import weblogic.wsee.security.wssc.v200502.faults.UnsupportedContextTokenException;

public class DKToken extends DKTokenBase {
   public DKToken() {
   }

   public DKToken(DKCredential var1) {
      super(var1);
   }

   protected QName getDK_QNAME() {
      return WSCConstants.DK_QNAME;
   }

   protected QName getDK_OFFSET_QNAME() {
      return WSCConstants.DK_OFFSET_QNAME;
   }

   protected QName getDK_LENGTH_QNAME() {
      return WSCConstants.DK_LENGTH_QNAME;
   }

   protected QName getDK_LABEL_QNAME() {
      return WSCConstants.DK_LABEL_QNAME;
   }

   protected QName getDK_NONCE_QNAME() {
      return WSCConstants.DK_NONCE_QNAME;
   }

   protected QName getDK_GENERATION_QNAME() {
      return WSCConstants.DK_GENERATION_QNAME;
   }

   protected QName getDK_ALGORITHM_QNAME() {
      return WSCConstants.DK_ALGORITHM_QNAME;
   }

   protected String getURI_P_SHA1() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc/dk/p_sha1";
   }

   protected String getXMLNS_WSS() {
      return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
   }

   protected String getXMLNS_WSC() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc";
   }

   protected String getDK_VALUE_TYPE() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
   }

   protected BadContextTokenException newBadContextTokenException(String var1) {
      return new BadContextTokenException(var1);
   }

   protected UnknownDerivationSourceException newUnknownDerivationSourceException(String var1) {
      return new UnknownDerivationSourceException(var1);
   }

   protected UnsupportedContextTokenException newUnsupportedContextTokenException(String var1) {
      return new UnsupportedContextTokenException(var1);
   }
}
