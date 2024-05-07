package weblogic.wsee.jaxrpc.soapfault;

import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;

public final class WLSOAPFaultException extends SOAPFaultException {
   private boolean isSoap12 = false;
   private SOAPFault fault = null;

   public WLSOAPFaultException(SOAPFault var1) {
      super(var1.getFaultCodeAsQName(), var1.getFaultString(), var1.getFaultActor(), var1.getDetail());
      this.fault = var1;

      try {
         this.isSoap12 = SOAPFaultUtil.getSOAPVersion(var1);
      } catch (SOAPException var3) {
      }

   }

   public WLSOAPFaultException(boolean var1, SOAPFault var2) throws SOAPException {
      super(var2.getFaultCodeAsQName(), var2.getFaultString(), var2.getFaultActor(), var2.getDetail());
      this.fault = var2;
      this.isSoap12 = var1;
      SOAPFaultUtil.validateFaultCode(var1, var2.getFaultCodeAsQName());
   }

   public SOAPFault getFault() {
      return this.fault;
   }

   public boolean isSOAP12() {
      return this.isSoap12;
   }

   public Detail getDetail() {
      return super.getDetail();
   }

   public String getFaultActor() {
      return super.getFaultActor();
   }

   public QName getFaultCode() {
      return super.getFaultCode();
   }

   public String getFaultString() {
      return super.getFaultString();
   }
}
