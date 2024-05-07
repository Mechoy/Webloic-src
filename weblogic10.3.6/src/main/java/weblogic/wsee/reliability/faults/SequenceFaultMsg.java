package weblogic.wsee.reliability.faults;

import java.util.Locale;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmProtocolUtils;
import weblogic.wsee.util.Verbose;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public abstract class SequenceFaultMsg extends WsrmFaultMsg {
   private static final boolean verbose = Verbose.isVerbose(SequenceFaultMsg.class);
   private String seqId;

   public SequenceFaultMsg(WsrmConstants.RMVersion var1, WsrmConstants.FaultCode var2, String var3, String var4, SequenceFaultMsgType var5) {
      super(var1, var2, var3, var4, var5);
   }

   public void setSequenceId(String var1) {
      this.seqId = var1;
   }

   public String getSequenceId() {
      return this.seqId;
   }

   public void read(SOAPMessage var1) throws SequenceFaultException {
      assert var1 != null;

      try {
         SOAPHeader var2 = var1.getSOAPHeader();
         if (var2 == null) {
            throw new SequenceFaultException("No header in SOAP message");
         } else {
            SOAPBody var3 = var1.getSOAPBody();
            if (var3 == null) {
               throw new SequenceFaultException("No body in SOAP message");
            } else if (!var3.hasFault()) {
               throw new SequenceFaultException("No SOAP fault in SOAP message");
            } else {
               SOAPFault var4 = var3.getFault();
               this.code = WsrmProtocolUtils.getSOAPFaultCodeFromName(var4.getFaultCodeAsName());
               SOAPEnvelope var5 = var1.getSOAPPart().getEnvelope();
               String var6 = var5.getNamespaceURI();
               boolean var7 = "http://www.w3.org/2003/05/soap-envelope".equals(var6);
               if (var7) {
                  Detail var8 = var4.getDetail();
                  if (var8 != null) {
                     this.readDetail(var8);
                  }
               } else {
                  Element var11 = DOMUtils.getElementByTagNameNS(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.SEQUENCE_FAULT.getElementName());
                  this.readDetail(var11);
               }

               this.reason = var4.getFaultString();
            }
         }
      } catch (SOAPException var9) {
         if (verbose) {
            Verbose.logException(var9);
         }

         throw new SequenceFaultException("SOAPException", var9);
      } catch (DOMProcessingException var10) {
         if (verbose) {
            Verbose.logException(var10);
         }

         throw new SequenceFaultException("DOMProcessingException", var10);
      }
   }

   public void write(SOAPMessage var1) throws SequenceFaultException {
      try {
         SOAPEnvelope var2 = var1.getSOAPPart().getEnvelope();
         String var3 = var2.getNamespaceURI();
         WsrmConstants.SOAPVersion var4 = WsrmProtocolUtils.getSOAPVersionFromNamespaceUri(var3);
         SOAPHeader var5 = var1.getSOAPHeader();
         if (var5 == null) {
            throw new SequenceFaultException("No header in SOAP message");
         } else {
            SOAPBody var6 = var1.getSOAPBody();
            if (var6 == null) {
               throw new SequenceFaultException("No body in SOAP message");
            } else if (this.code == null) {
               throw new SequenceFaultException("Fault code is not set");
            } else {
               SOAPFault var7 = var6.addFault();
               var7.setFaultCode(var2.createName(this.getCodeLocalName(var4), var2.getPrefix(), var3));
               if (this.reason != null) {
                  var7.setFaultString(this.reason, Locale.US);
               }

               WsrmConstants.RMVersion var8 = this.getRmVersion();
               String var9 = DOMUtils.getNamespaceURI(var5, var8.getPrefix());
               boolean var10 = var9 == null || var9.length() == 0;
               if (var10) {
                  DOMUtils.addNamespaceDeclaration(var5, var8.getPrefix(), var8.getNamespaceUri());
               }

               if (var4 == WsrmConstants.SOAPVersion.SOAP_11) {
                  Element var11 = var5.getOwnerDocument().createElementNS(var8.getNamespaceUri(), WsrmConstants.Element.SEQUENCE_FAULT.getQualifiedName(var8));
                  DOMUtils.addValueNS(var11, var8.getNamespaceUri(), WsrmConstants.Element.FAULT_CODE.getQualifiedName(var8), this.getSubCodeQualifiedName());
                  this.writeDetail(var11);
                  var5.appendChild(var11);
               } else {
                  var9 = DOMUtils.getNamespaceURI(var7, var8.getPrefix());
                  var10 = var9 == null || var9.length() == 0;
                  if (var10) {
                     DOMUtils.addNamespaceDeclaration(var7, var8.getPrefix(), var8.getNamespaceUri());
                  }

                  var7.appendFaultSubcode(this.getSubCodeQName());
                  Detail var13 = var7.addDetail();
                  this.writeDetail(var13);
               }

            }
         }
      } catch (SOAPException var12) {
         if (verbose) {
            Verbose.logException(var12);
         }

         throw new SequenceFaultException("SOAPException", var12);
      }
   }

   public void writeDetail(Element var1) throws SequenceFaultException {
      if (this.seqId == null) {
         throw new SequenceFaultException("Sequence ID is not set");
      } else {
         DOMUtils.addNamespaceDeclaration(var1, this.getRmVersion().getPrefix(), this.getRmVersion().getNamespaceUri());
         WsrmConstants.Element var2 = WsrmConstants.Element.IDENTIFIER;
         DOMUtils.addValueNS(var1, this.getRmVersion().getNamespaceUri(), var2.getQualifiedName(this.getRmVersion()), this.seqId);
      }
   }

   public void readDetail(Element var1) throws SequenceFaultException {
      try {
         WsrmConstants.Element var2 = WsrmConstants.Element.IDENTIFIER;
         this.seqId = DOMUtils.getValueByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), var2.getElementName());
      } catch (DOMProcessingException var3) {
         throw new SequenceFaultException("DOMProcessingException", var3);
      }
   }

   public String toString() {
      return this.getReason() + ": " + this.seqId;
   }
}
