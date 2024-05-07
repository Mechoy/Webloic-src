package weblogic.wsee.reliability2.compat;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.bind.api.Bridge;
import com.sun.xml.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.message.StringHeader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.util.WLMessageFactory;

public abstract class CommonHeader extends MsgHeader implements Header {
   private static final long serialVersionUID = 1L;
   private QName _qName;
   private transient Header _jaxwsHeader;
   protected static final AttributesImpl EMPTY_ATTS = new AttributesImpl();

   protected CommonHeader(QName var1) {
      this.setName(var1);
   }

   protected void setupJaxwsHeader() {
      this._jaxwsHeader = new StringHeader(this._qName, "");
   }

   public QName getName() {
      return this._qName;
   }

   protected void setName(QName var1) {
      this._qName = var1;
      this.setupJaxwsHeader();
   }

   public void setNamespaceUri(String var1) {
      this.setName(new QName(var1, this._qName.getLocalPart(), this._qName.getPrefix()));
   }

   public abstract SimpleElement writeToSimpleElement() throws MsgHeaderException;

   protected void commonWriteToSimpleElement(String var1, SOAPVersion var2, SimpleElement var3) {
      QName var4 = new QName(var2.nsUri, "mustUnderstand", var1);
      if (this.isMustUnderstand()) {
         var3.setAttr(var4, SOAPVersion.SOAP_11 == var2 ? "1" : "true");
      }

   }

   public abstract void readFromSimpleElement(SimpleElement var1) throws MsgHeaderException;

   protected void commonReadFromSimpleElement(SimpleElement var1) {
      WsrmConstants.SOAPVersion[] var2 = WsrmConstants.SOAPVersion.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WsrmConstants.SOAPVersion var5 = var2[var4];
         String var6 = var5.getNamespaceUri();
         QName var7 = new QName(var6, "mustUnderstand");
         String var8 = var1.getAttr(var7);
         if (var8 != null) {
            if (var5 == WsrmConstants.SOAPVersion.SOAP_11) {
               int var9 = Integer.parseInt(var8);
               this.setMustUnderstand(var9 == 1);
            } else {
               boolean var10 = Boolean.parseBoolean(var8);
               this.setMustUnderstand(var10);
            }
            break;
         }
      }

   }

   public static SimpleElement getTopParent(SimpleElement var0) {
      SimpleElement var1 = var0;

      while(var1 != null) {
         SimpleElement var2 = var1;
         var1 = var1.getParent();
         if (var1 == null) {
            var1 = var2;
            break;
         }
      }

      return var1;
   }

   public void read(Element var1) {
      try {
         SimpleElement var2 = SimpleElementSerializer.deserialize(var1);
         this.readFromSimpleElement(var2);
         this.commonReadFromSimpleElement(var2);
      } catch (Exception var3) {
         throw new MsgHeaderException(var3.toString(), var3);
      }
   }

   public void read(XMLStreamReader var1) {
      try {
         if (var1.getEventType() != 1) {
            var1.nextTag();
         }

         SimpleElement var2 = SimpleElementSerializer.deserialize(var1);
         this.readFromSimpleElement(var2);
         this.commonReadFromSimpleElement(var2);
      } catch (Exception var3) {
         throw new MsgHeaderException(var3.toString(), var3);
      }
   }

   public void write(Element var1) {
      try {
         SimpleElement var2 = this.writeToSimpleElement();
         String var3 = this.getCurrentSoapEnvelopeNamespace();
         if (var3 == null) {
            var3 = WsrmConstants.SOAPVersion.SOAP_11.getNamespaceUri();
         }

         String var4 = var1.lookupPrefix(var3);
         if (var4 == null) {
            var4 = "soap";
         }

         SOAPVersion var5 = var3 != null ? SOAPVersion.fromNsUri(var3) : SOAPVersion.SOAP_11;
         this.commonWriteToSimpleElement(var4, var5, var2);
         SimpleElementSerializer.serialize(var2, var1);
      } catch (Exception var6) {
         throw new MsgHeaderException(var6.toString(), var6);
      }
   }

   public void write(XMLStreamWriter var1) {
      try {
         String var2 = null;
         SOAPVersion var3 = null;
         SOAPVersion[] var4 = SOAPVersion.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            SOAPVersion var7 = var4[var6];
            var2 = var1.getNamespaceContext().getPrefix(var7.nsUri);
            if (var2 != null) {
               var3 = var7;
               break;
            }
         }

         if (var2 == null) {
            var2 = "soap";
            var3 = SOAPVersion.SOAP_11;
         }

         SimpleElement var9 = this.writeToSimpleElement();
         this.commonWriteToSimpleElement(var2, var3, var9);
         SimpleElementSerializer.serialize(var9, var1);
      } catch (Exception var8) {
         throw new MsgHeaderException(var8.toString(), var8);
      }
   }

   public boolean isIgnorable(@NotNull SOAPVersion var1, @NotNull Set<String> var2) {
      return this._jaxwsHeader.isIgnorable(var1, var2);
   }

   @NotNull
   public String getRole(@NotNull SOAPVersion var1) {
      return this._jaxwsHeader.getRole(var1);
   }

   public boolean isRelay() {
      return this._jaxwsHeader.isRelay();
   }

   @NotNull
   public String getNamespaceURI() {
      return this.getName().getNamespaceURI();
   }

   @NotNull
   public String getLocalPart() {
      return this.getName().getLocalPart();
   }

   @Nullable
   public String getAttribute(@NotNull String var1, @NotNull String var2) {
      SimpleElement var3 = this.writeToSimpleElement();
      return var3.getAttr(var1, var2);
   }

   @Nullable
   public String getAttribute(@NotNull QName var1) {
      SimpleElement var2 = this.writeToSimpleElement();
      return var2.getAttr(var1);
   }

   public XMLStreamReader readHeader() throws XMLStreamException {
      MutableXMLStreamBuffer var1 = new MutableXMLStreamBuffer();
      XMLStreamWriter var2 = var1.createFromXMLStreamWriter();
      this.writeTo(var2);
      return var1.readAsXMLStreamReader();
   }

   public <T> T readAsJAXB(Unmarshaller var1) throws JAXBException {
      return this._jaxwsHeader.readAsJAXB(var1);
   }

   public <T> T readAsJAXB(Bridge<T> var1) throws JAXBException {
      return this._jaxwsHeader.readAsJAXB(var1);
   }

   @NotNull
   public WSEndpointReference readAsEPR(AddressingVersion var1) throws XMLStreamException {
      return this._jaxwsHeader.readAsEPR(var1);
   }

   public void writeTo(XMLStreamWriter var1) throws XMLStreamException {
      this.write(var1);
   }

   public void writeTo(SOAPMessage var1) throws SOAPException {
      this.writeToParent(var1.getSOAPHeader());
   }

   public abstract void writeTo(ContentHandler var1, ErrorHandler var2) throws SAXException;

   @NotNull
   public String getStringContent() {
      throw new IllegalStateException("Not implemented");
   }

   public static WSEndpointReference addHeadersToEPR(WSEndpointReference var0, HeaderList var1) throws WsrmException {
      var0.addReferenceParameters(var1);
      List var2 = getElementListFromHeaderList(var1);
      var0 = new WSEndpointReference(var0.getVersion(), var0.getAddress(), var0.getMetaData().getServiceName(), var0.getMetaData().getPortName(), var0.getMetaData().getPortTypeName(), (List)null, var0.getMetaData().getWsdliLocation(), var2);
      return var0;
   }

   private static List<Element> getElementListFromHeaderList(HeaderList var0) throws WsrmException {
      ArrayList var1 = new ArrayList();

      try {
         MessageFactory var2 = WLMessageFactory.getInstance().getMessageFactory(false);
         SOAPMessage var3 = var2.createMessage();
         Iterator var4 = var0.iterator();

         while(var4.hasNext()) {
            Header var5 = (Header)var4.next();
            var5.writeTo(var3);
         }

         NodeList var7 = var3.getSOAPHeader().getChildNodes();

         for(int var8 = 0; var8 < var7.getLength(); ++var8) {
            var1.add((Element)var7.item(var8));
         }

         return var1;
      } catch (Exception var6) {
         throw new WsrmException(var6.toString(), var6);
      }
   }
}
