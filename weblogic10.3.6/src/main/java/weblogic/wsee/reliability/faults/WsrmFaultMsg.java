package weblogic.wsee.reliability.faults;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.reliability.WsrmConstants;

public abstract class WsrmFaultMsg {
   private WsrmConstants.RMVersion rmVersion;
   protected WsrmConstants.FaultCode code;
   protected String subCode;
   protected String reason;
   protected SequenceFaultMsgType type;

   protected WsrmFaultMsg(WsrmConstants.RMVersion var1, WsrmConstants.FaultCode var2, String var3, String var4, SequenceFaultMsgType var5) {
      this.rmVersion = var1;
      this.code = var2;
      this.subCode = var3;
      this.reason = var4;
      this.type = var5;
   }

   public WsrmConstants.RMVersion getRmVersion() {
      return this.rmVersion;
   }

   public WsrmConstants.FaultCode getCode() {
      return this.code;
   }

   public String getCodeQualifiedName(WsrmConstants.SOAPVersion var1) {
      return this.code.getCodeQualifiedName(var1);
   }

   public QName getCodeQName(WsrmConstants.SOAPVersion var1) {
      return this.code.getCodeQName(var1);
   }

   public String getCodeLocalName(WsrmConstants.SOAPVersion var1) {
      return this.code.getCodeLocalName(var1);
   }

   public String getSubCodeQualifiedName() {
      return this.rmVersion.getPrefix() + ":" + this.subCode;
   }

   public QName getSubCodeQName() {
      return new QName(this.rmVersion.getNamespaceUri(), this.subCode, this.rmVersion.getPrefix());
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

   public SequenceFaultMsgType getType() {
      return this.type;
   }

   public static QName getSubCodeQName(Class var0, WsrmConstants.RMVersion var1) {
      try {
         Field var2 = var0.getField("SUBCODE_LOCAL_NAME");
         String var3 = (String)var2.get((Object)null);
         return new QName(var1.getNamespaceUri(), var3);
      } catch (Exception var4) {
         throw new RuntimeException(var4.toString(), var4);
      }
   }

   public static List<QName> getSubCodeQNames(Class var0) {
      try {
         ArrayList var1 = new ArrayList();
         WsrmConstants.RMVersion[] var2 = WsrmConstants.RMVersion.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WsrmConstants.RMVersion var5 = var2[var4];
            var1.add(getSubCodeQName(var0, var5));
         }

         return var1;
      } catch (Exception var6) {
         throw new RuntimeException(var6.toString(), var6);
      }
   }

   public abstract void read(SOAPMessage var1) throws SequenceFaultException;

   public abstract void write(SOAPMessage var1) throws SequenceFaultException;

   public abstract void readDetail(Element var1) throws SequenceFaultException;

   public abstract void writeDetail(Element var1) throws SequenceFaultException;

   public String toString() {
      return this.reason;
   }
}
