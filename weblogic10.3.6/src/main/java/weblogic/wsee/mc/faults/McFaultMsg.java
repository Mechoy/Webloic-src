package weblogic.wsee.mc.faults;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.mc.exception.McFaultException;
import weblogic.wsee.mc.utils.McConstants;

public abstract class McFaultMsg {
   private final McConstants.McVersion version;
   protected McConstants.FaultCode code;
   protected String subCode;
   protected String reason;

   protected McFaultMsg(McConstants.McVersion var1, McConstants.FaultCode var2, String var3, String var4) {
      this.version = var1;
      this.code = var2;
      this.subCode = var3;
      this.reason = var4;
   }

   public McConstants.McVersion getMcVersion() {
      return this.version;
   }

   public McConstants.FaultCode getCode() {
      return this.code;
   }

   public String getCodeQualifiedName(McConstants.SOAPVersion var1) {
      return this.code.getCodeQualifiedName(var1);
   }

   public QName getCodeQName(McConstants.SOAPVersion var1) {
      return this.code.getCodeQName(var1);
   }

   public String getCodeLocalName(McConstants.SOAPVersion var1) {
      return this.code.getCodeLocalName(var1);
   }

   public String getSubCodeQualifiedName() {
      return this.version.getPrefix() + ":" + this.subCode;
   }

   public QName getSubCodeQName() {
      return new QName(this.version.getNamespaceUri(), this.subCode, this.version.getPrefix());
   }

   public String getSubCodeLocalName() {
      return this.subCode;
   }

   public String getReason() {
      return this.reason;
   }

   public void setReason(String var1) {
      this.reason = var1;
   }

   public abstract void read(SOAPMessage var1) throws McFaultException;

   public abstract void write(SOAPMessage var1) throws McFaultException;

   public abstract void readDetail(Element var1) throws McFaultException;

   public abstract void writeDetail(Element var1) throws McFaultException;
}
