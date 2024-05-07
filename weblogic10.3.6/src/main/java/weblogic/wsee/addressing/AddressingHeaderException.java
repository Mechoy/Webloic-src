package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;

public class AddressingHeaderException extends SOAPFaultException {
   private String faultHeader;
   private QName[] headerFaultCode;
   private static QName faultcode = SOAPFaultUtil.createFaultCodeQName("Sender");

   public AddressingHeaderException(String var1, String var2, QName... var3) {
      super(faultcode, var1, (String)null, (Detail)null);
      this.faultHeader = var2;
      this.headerFaultCode = var3;
   }

   public String getFaultHeader() {
      return this.faultHeader;
   }

   public void setFaultHeader(String var1) {
      this.faultHeader = var1;
   }

   public QName[] getHeaderFaultCode() {
      return this.headerFaultCode;
   }

   public void setHeaderFaultCode(QName... var1) {
      this.headerFaultCode = var1;
   }
}
