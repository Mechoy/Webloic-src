package weblogic.wsee.mc.messages;

import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.mc.exception.McMsgException;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.util.ToStringWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class McMsg {
   private final McConstants.McVersion _mcVersion;
   private String _address;
   private List<QName> _otherList = new ArrayList();

   public McMsg(McConstants.McVersion var1) {
      this._mcVersion = var1;
   }

   public McMsg() {
      this._mcVersion = McConstants.McVersion.latest();
   }

   public McConstants.McVersion getMcVersion() {
      return this._mcVersion;
   }

   public void setAddress(String var1) {
      this._address = var1;
   }

   public String getAddress() {
      return this._address;
   }

   public List<QName> getOther() {
      return this._otherList;
   }

   public Element readFromSOAPMsg(@NotNull SOAPMessage var1) throws McMsgException {
      SOAPBody var2;
      try {
         var2 = var1.getSOAPBody();
      } catch (SOAPException var7) {
         throw new McMsgException(WseeMCLogger.logSOAPBodyExceptionLoggable().getMessage(), var7);
      }

      if (var2 == null) {
         throw new McMsgException(WseeMCLogger.logNoSOAPBodyLoggable().getMessage());
      } else {
         QName var3 = new QName(this._mcVersion.getNamespaceUri(), McConstants.Element.MC.getElementName(), this._mcVersion.getPrefix());

         Element var4;
         try {
            var4 = DOMUtils.getElementByTagNameNS(var2, var3.getNamespaceURI(), var3.getLocalPart());
         } catch (DOMProcessingException var6) {
            throw new McMsgException(WseeMCLogger.logMCMsgExceptionLoggable().getMessage(), var6);
         }

         this.read(var4);
         return var4;
      }
   }

   public Element writeIntoSOAPMsg(@NotNull SOAPMessage var1) throws McMsgException {
      SOAPBody var2;
      try {
         var2 = var1.getSOAPBody();
      } catch (SOAPException var5) {
         throw new McMsgException(WseeMCLogger.logSOAPBodyExceptionLoggable().getMessage(), var5);
      }

      if (var2 == null) {
         throw new McMsgException(WseeMCLogger.logNoSOAPBodyLoggable().getMessage());
      } else {
         QName var3 = new QName(this._mcVersion.getNamespaceUri(), McConstants.Element.MC.getElementName(), this._mcVersion.getPrefix());
         Element var4 = var2.getOwnerDocument().createElementNS(var3.getNamespaceURI(), var3.getPrefix() + ":" + var3.getLocalPart());
         DOMUtils.addNamespaceDeclaration(var2, var3.getPrefix(), var3.getNamespaceURI());
         this.write(var4);
         var2.appendChild(var4);
         return var4;
      }
   }

   public void read(Element var1) throws McMsgException {
      String var2 = this.getMcVersion().getNamespaceUri();
      String var3 = McConstants.Element.ADDRESS.getElementName();

      for(Node var4 = var1.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var2.equals(var5.getNamespaceURI()) && var3.equals(var5.getLocalName())) {
               try {
                  this._address = DOMUtils.getTextData(var5);
               } catch (DOMProcessingException var7) {
               }
            } else {
               this._otherList.add(new QName(var5.getNamespaceURI(), var5.getLocalName()));
            }
         }
      }

   }

   public void write(Element var1) throws McMsgException {
      if (this._address == null) {
         throw new McMsgException(WseeMCLogger.logNoAddressLoggable().getMessage());
      } else {
         DOMUtils.addValueNS(var1, this.getMcVersion().getNamespaceUri(), McConstants.Element.ADDRESS.getQualifiedName(this.getMcVersion()), this._address);
      }
   }

   public void toString(ToStringWriter var1) {
      var1.writeField(McConstants.Element.ADDRESS.getElementName(), this._address);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }
}
