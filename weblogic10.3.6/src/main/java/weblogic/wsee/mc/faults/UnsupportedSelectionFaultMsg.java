package weblogic.wsee.mc.faults;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.mc.exception.McFaultException;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.mc.utils.McProtocolUtils;
import weblogic.xml.dom.DOMUtils;

public class UnsupportedSelectionFaultMsg extends McFaultMsg {
   public static final String SUBCODE_LOCAL_NAME = "UnsupportedSelection";
   private List<QName> _unsupportedList;

   public UnsupportedSelectionFaultMsg(McConstants.McVersion var1) {
      super(var1, McConstants.FaultCode.RECEIVER, McConstants.Element.UNSUPPORTED_SELECTION_FAULT.getElementName(), "The extension element used in the message selection is not supported by the MakeConnection receiver");
   }

   public void setUnsupported(List<QName> var1) {
      this._unsupportedList = var1;
   }

   public List<QName> getUnsupported() {
      return this._unsupportedList;
   }

   public void read(SOAPMessage var1) throws McFaultException {
      assert var1 != null;

      try {
         SOAPHeader var2 = var1.getSOAPHeader();
         if (var2 == null) {
            throw new McFaultException("No header in SOAP message");
         } else {
            SOAPBody var3 = var1.getSOAPBody();
            if (var3 == null) {
               throw new McFaultException("No body in SOAP message");
            } else if (!var3.hasFault()) {
               throw new McFaultException("No SOAP fault in SOAP message");
            } else {
               SOAPFault var4 = var3.getFault();
               this.code = McProtocolUtils.getSOAPFaultCodeFromName(var4.getFaultCodeAsName());
               SOAPEnvelope var5 = var1.getSOAPPart().getEnvelope();
               String var6 = var5.getNamespaceURI();
               boolean var7 = "http://www.w3.org/2003/05/soap-envelope".equals(var6);
               this.reason = var4.getFaultString();
               Detail var8 = var4.getDetail();
               this.readDetail(var8);
            }
         }
      } catch (SOAPException var9) {
         throw new McFaultException("SOAPException", var9);
      }
   }

   public void write(SOAPMessage var1) throws McFaultException {
      try {
         SOAPEnvelope var2 = var1.getSOAPPart().getEnvelope();
         String var3 = var2.getNamespaceURI();
         McConstants.SOAPVersion var4 = McProtocolUtils.getSOAPVersionFromNamespaceUri(var3);
         SOAPHeader var5 = var1.getSOAPHeader();
         if (var5 == null) {
            throw new McFaultException("No header in SOAP message");
         } else {
            SOAPBody var6 = var1.getSOAPBody();
            if (var6 == null) {
               throw new McFaultException("No body in SOAP message");
            } else if (this.code == null) {
               throw new McFaultException("Fault code is not set");
            } else {
               SOAPFault var7 = var6.addFault();
               McConstants.McVersion var8 = this.getMcVersion();
               String var9 = DOMUtils.getNamespaceURI(var5, var8.getPrefix());
               boolean var10 = var9 == null || var9.length() == 0;
               if (var10) {
                  DOMUtils.addNamespaceDeclaration(var5, var8.getPrefix(), var8.getNamespaceUri());
               }

               if (var4 == McConstants.SOAPVersion.SOAP_11) {
                  Name var11 = var2.createName(this.getSubCodeLocalName(), this.getMcVersion().getPrefix(), this.getMcVersion().getNamespaceUri());
                  var7.setFaultCode(var11);
               } else {
                  var7.setFaultCode(var2.createName(this.getCodeLocalName(var4), var2.getPrefix(), var2.getNamespaceURI()));
                  var7.appendFaultSubcode(this.getSubCodeQName());
               }

               if (this.reason != null) {
                  var7.setFaultString(this.reason, Locale.US);
               }

               Detail var13 = var7.addDetail();
               this.writeDetail(var13);
            }
         }
      } catch (SOAPException var12) {
         throw new McFaultException("SOAPException", var12);
      }
   }

   public void writeDetail(Element var1) throws McFaultException {
      if (this._unsupportedList == null) {
         throw new McFaultException("Unsupported Selection is not set");
      } else {
         DOMUtils.addNamespaceDeclaration(var1, this.getMcVersion().getPrefix(), this.getMcVersion().getNamespaceUri());
         Iterator var2 = this._unsupportedList.iterator();

         while(var2.hasNext()) {
            QName var3 = (QName)var2.next();
            Element var4 = var1.getOwnerDocument().createElementNS(this.getMcVersion().getNamespaceUri(), McConstants.Element.UNSUPPORTED_SELECTION.getQualifiedName(this.getMcVersion()));
            String var5 = var3.getNamespaceURI();
            if (var5 == null || var5.equals("")) {
               throw new McFaultException("Unsupported Selection must have namespace");
            }

            String var6 = var3.getPrefix();
            if (var6 == null || var6.equals("")) {
               var6 = "ns1";
            }

            DOMUtils.addNamespaceDeclaration(var4, var6, var5);
            String var7 = var6 + ":" + var3.getLocalPart();
            DOMUtils.addTextData(var4, var7);
            var1.appendChild(var4);
         }

      }
   }

   public void readDetail(Element var1) throws McFaultException {
      String var2 = this.getMcVersion().getNamespaceUri();
      String var3 = McConstants.Element.UNSUPPORTED_SELECTION.getElementName();
      Node var4 = var1.getFirstChild();

      while(true) {
         do {
            if (var4 == null) {
               return;
            }
         } while(!(var4 instanceof Element));

         Element var5 = (Element)var4;
         if (!var2.equals(var5.getNamespaceURI()) || !var3.equals(var5.getLocalName())) {
            throw new McFaultException("Illegal element in detail of fault message");
         }

         try {
            String var6 = DOMUtils.getTextContent(var5, true);
            int var8 = var6.indexOf(":");
            QName var7;
            if (var8 <= 0) {
               var7 = new QName(var6);
            } else {
               String var9 = var6.substring(0, var8 - 1);
               String var10 = var6.substring(var8 + 1);
               String var11 = DOMUtils.getNamespaceURI(var5, var9);
               if (var11 == null) {
                  throw new McFaultException("Unsupported selection contains unknown namespace");
               }

               var7 = new QName(var11, var10);
            }

            this._unsupportedList.add(var7);
         } catch (IndexOutOfBoundsException var12) {
            throw new McFaultException("Parsing error during processing of unsupported selection");
         }
      }
   }
}
