package weblogic.wsee.reliability2.compat;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.message.StringHeader;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class SOAPActionHeader extends StringHeader {
   private boolean _mustUnderstand;
   private SOAPVersion _soapVersion;

   public SOAPActionHeader(QName var1, String var2, SOAPVersion var3) {
      super(var1, var2);
      this._soapVersion = var3;
   }

   public void setMustUnderstand(boolean var1) {
      this._mustUnderstand = var1;
   }

   @Nullable
   public String getAttribute(@NotNull String var1, @NotNull String var2) {
      String var3 = null;
      if (var1 != null && "mustUnderstand".equals(var2)) {
         if (var1.equals(SOAPVersion.SOAP_11.nsUri)) {
            var3 = this._mustUnderstand ? "1" : "0";
         } else {
            var3 = Boolean.toString(this._mustUnderstand);
         }
      }

      if (var3 == null) {
         var3 = super.getAttribute(var1, var2);
      }

      return var3;
   }

   protected void writeAttributes(XMLStreamWriter var1) throws XMLStreamException {
      String var2 = var1.getNamespaceContext().getPrefix(this._soapVersion.nsUri);
      if (var2 == null) {
         var2 = "soap";
         var1.writeNamespace(var2, this._soapVersion.nsUri);
      }

      var1.writeAttribute(var2, this._soapVersion.nsUri, "mustUnderstand", this.getAttribute(this._soapVersion.nsUri, "mustUnderstand"));
   }

   public void writeTo(SOAPMessage var1) throws SOAPException {
      SOAPHeader var2 = var1.getSOAPHeader();
      SOAPHeaderElement var3 = var2.addHeaderElement(this.name);
      if (this._mustUnderstand) {
         var3.addNamespaceDeclaration("soap", this._soapVersion.nsUri);
         var3.addAttribute(new QName(this._soapVersion.nsUri, "mustUnderstand", "soap"), this.getAttribute(this._soapVersion.nsUri, "mustUnderstand"));
      }

      var3.addTextNode(this.value);
   }

   public void writeTo(ContentHandler var1, ErrorHandler var2) throws SAXException {
      String var3 = this.name.getNamespaceURI();
      String var4 = this.name.getLocalPart();
      var1.startPrefixMapping("", var3);
      AttributesImpl var5 = new AttributesImpl();
      if (this._mustUnderstand) {
         var1.startPrefixMapping("soap", this._soapVersion.nsUri);
         var5.addAttribute(this._soapVersion.nsUri, "mustUnderstand", "soap:mustUnderstand", "string", this.getAttribute(this._soapVersion.nsUri, "mustUnderstand"));
         var1.endPrefixMapping("soap");
      }

      var1.startElement(var3, var4, var4, var5);
      var1.characters(this.value.toCharArray(), 0, this.value.length());
      var1.endElement(var3, var4, var4);
   }
}
