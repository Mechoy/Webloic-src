package weblogic.wsee.mc.faults;

import java.util.Iterator;
import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import weblogic.wsee.mc.exception.McFaultException;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.mc.utils.McProtocolUtils;

public class MissingSelectionFaultMsg extends McFaultMsg {
   public MissingSelectionFaultMsg(McConstants.McVersion var1) {
      super(var1, McConstants.FaultCode.RECEIVER, McConstants.Element.MISSING_SELECTION_FAULT.getElementName(), "The MakeConnection element did not contain any selection criteria");
   }

   public void read(SOAPMessage var1) throws McFaultException {
      try {
         SOAPBody var2 = var1.getSOAPBody();
         if (var2 == null) {
            throw new McFaultException("No body in SOAP message");
         } else if (!var2.hasFault()) {
            throw new McFaultException("No SOAP fault in SOAP message");
         } else {
            SOAPFault var3 = var2.getFault();
            this.code = McProtocolUtils.getSOAPFaultCodeFromName(var3.getFaultCodeAsName());
            SOAPEnvelope var4 = var1.getSOAPPart().getEnvelope();
            String var5 = var4.getNamespaceURI();
            McConstants.SOAPVersion var6 = McProtocolUtils.getSOAPVersionFromNamespaceUri(var5);
            if (var6 == McConstants.SOAPVersion.SOAP_11) {
               String var7 = var3.getFaultCodeAsName().getLocalName();
               if (!this.getSubCodeLocalName().equals(var7)) {
                  throw new McFaultException("Wrong subcode for UnspportedSelectionFault");
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
                  throw new McFaultException("Wrong subcode for UnsupportedSelectionFault");
               }
            }

            this.reason = var3.getFaultString();
         }
      } catch (SOAPException var10) {
         throw new McFaultException("SOAPException", var10);
      }
   }

   public void write(SOAPMessage var1) throws McFaultException {
      try {
         SOAPEnvelope var2 = var1.getSOAPPart().getEnvelope();
         String var3 = var2.getNamespaceURI();
         SOAPBody var4 = var1.getSOAPBody();
         if (var4 == null) {
            throw new McFaultException("No body in SOAP message");
         } else if (this.code == null) {
            throw new McFaultException("Fault code is not set");
         } else {
            SOAPFault var5 = var4.addFault();
            McConstants.SOAPVersion var6 = McProtocolUtils.getSOAPVersionFromNamespaceUri(var3);
            if (var6 == McConstants.SOAPVersion.SOAP_11) {
               Name var7 = var2.createName(this.getSubCodeLocalName(), this.getMcVersion().getPrefix(), this.getMcVersion().getNamespaceUri());
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
         throw new McFaultException("SOAPException", var8);
      }
   }

   public void writeDetail(Element var1) throws McFaultException {
   }

   public void readDetail(Element var1) throws McFaultException {
   }
}
