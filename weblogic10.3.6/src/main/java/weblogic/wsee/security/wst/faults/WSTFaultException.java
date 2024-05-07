package weblogic.wsee.security.wst.faults;

import javax.xml.namespace.QName;

public class WSTFaultException extends Exception {
   private static final String XMLNS_TRUST = "http://schemas.xmlsoap.org/ws/2005/02/trust";
   private static final String PREFIX_WST = "wst";
   protected String faultString = "";
   protected String faultCode = "";

   public WSTFaultException(String var1) {
      super(var1);
   }

   public String getFaultString() {
      return this.faultString;
   }

   public String getFaultCode() {
      return this.faultCode;
   }

   public QName getFault() {
      return new QName("http://schemas.xmlsoap.org/ws/2005/02/trust", this.faultCode, "wst");
   }
}
