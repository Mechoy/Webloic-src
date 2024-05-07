package weblogic.wsee.reliability.faults;

import java.util.Iterator;
import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmProtocolUtils;

public class CreateSequenceRefusedFaultMsg extends WsrmFaultMsg {
   public static final String SUBCODE_LOCAL_NAME = "CreateSequenceRefused";
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();
   private final Exception exception;

   public CreateSequenceRefusedFaultMsg(Exception var1, WsrmConstants.RMVersion var2) {
      super(var2, WsrmConstants.FaultCode.SENDER, "CreateSequenceRefused", "The create sequence request has been refused by the RM Destination.", TYPE);
      this.exception = var1;
   }

   public void read(SOAPMessage var1) throws SequenceFaultException {
      try {
         SOAPBody var2 = var1.getSOAPBody();
         if (var2 == null) {
            throw new SequenceFaultException("No body in SOAP message");
         } else if (!var2.hasFault()) {
            throw new SequenceFaultException("No SOAP fault in SOAP message");
         } else {
            SOAPFault var3 = var2.getFault();
            SOAPEnvelope var4 = var1.getSOAPPart().getEnvelope();
            String var5 = var4.getNamespaceURI();
            WsrmConstants.SOAPVersion var6 = WsrmProtocolUtils.getSOAPVersionFromNamespaceUri(var5);
            if (var6 == WsrmConstants.SOAPVersion.SOAP_11) {
               String var7 = var3.getFaultCodeAsName().getLocalName();
               if (!this.getSubCodeLocalName().equals(var7)) {
                  throw new SequenceFaultException("Wrong subcode for CreateSequenceRefusedFault");
               }
            } else {
               Iterator var11 = var3.getFaultSubcodes();
               boolean var8 = false;

               while(var11.hasNext() && !var8) {
                  QName var9 = (QName)var11.next();
                  if (this.getSubCodeQName().equals(var9)) {
                     var8 = true;
                  }
               }

               if (!var8) {
                  throw new SequenceFaultException("Wrong subcode for CreateSequenceRefusedFault");
               }
            }

            this.reason = var3.getFaultString();
         }
      } catch (SOAPException var10) {
         throw new SequenceFaultException("SOAPException", var10);
      }
   }

   public void write(SOAPMessage var1) throws SequenceFaultException {
      try {
         SOAPEnvelope var2 = var1.getSOAPPart().getEnvelope();
         String var3 = var2.getNamespaceURI();
         SOAPBody var4 = var1.getSOAPBody();
         if (var4 == null) {
            throw new SequenceFaultException("No body in SOAP message");
         } else if (this.code == null) {
            throw new SequenceFaultException("Fault code is not set");
         } else {
            SOAPFault var5 = var4.addFault();
            WsrmConstants.SOAPVersion var6 = WsrmProtocolUtils.getSOAPVersionFromNamespaceUri(var3);
            if (var6 == WsrmConstants.SOAPVersion.SOAP_11) {
               Name var7 = var2.createName(this.getSubCodeLocalName(), this.getRmVersion().getPrefix(), this.getRmVersion().getNamespaceUri());
               var5.setFaultCode(var7);
            } else {
               var5.setFaultCode(var2.createName(this.getCodeLocalName(var6), var2.getPrefix(), var2.getNamespaceURI()));
               var5.appendFaultSubcode(this.getSubCodeQName());
            }

            if (this.reason != null) {
               var5.setFaultString(this.reason, Locale.US);
            }

            this.writeDetail(var5);
         }
      } catch (SOAPException var8) {
         throw new SequenceFaultException("SOAPException", var8);
      }
   }

   public SequenceFaultMsgType getType() {
      return TYPE;
   }

   public void writeDetail(SOAPFault var1) throws SOAPException {
      if (this.exception != null) {
         Detail var2 = var1.addDetail();
         SOAPFaultUtil.fillDetail(this.exception, var2, false);
      }

   }

   public void writeDetail(Element var1) throws SequenceFaultException {
   }

   public void readDetail(Element var1) throws SequenceFaultException {
   }
}
