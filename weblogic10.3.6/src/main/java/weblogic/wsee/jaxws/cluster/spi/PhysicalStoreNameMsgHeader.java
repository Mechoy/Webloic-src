package weblogic.wsee.jaxws.cluster.spi;

import com.sun.istack.NotNull;
import com.sun.xml.bind.api.Bridge;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class PhysicalStoreNameMsgHeader extends MsgHeader implements Header {
   private static final long serialVersionUID = 1L;
   public static final QName NAME;
   public static final MsgHeaderType TYPE;
   private PhysicalStoreNameHeader _jaxwsHeader;

   public PhysicalStoreNameMsgHeader(String var1) {
      this._jaxwsHeader = new PhysicalStoreNameHeader(var1);
   }

   public PhysicalStoreNameMsgHeader() {
   }

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         String var2 = DOMUtils.getTextData(var1);
         this._jaxwsHeader = new PhysicalStoreNameHeader(var2);
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not parse the PhysicalStoreName header", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addTextData(var1, this._jaxwsHeader.getStringContent());
   }

   public boolean isIgnorable(@NotNull SOAPVersion var1, @NotNull Set<String> var2) {
      return this._jaxwsHeader.isIgnorable(var1, var2);
   }

   public String getRole(@NotNull SOAPVersion var1) {
      return this._jaxwsHeader.getRole(var1);
   }

   public boolean isRelay() {
      return this._jaxwsHeader.isRelay();
   }

   public String getNamespaceURI() {
      return this._jaxwsHeader.getNamespaceURI();
   }

   public String getLocalPart() {
      return this._jaxwsHeader.getLocalPart();
   }

   public String getAttribute(@NotNull String var1, @NotNull String var2) {
      return this._jaxwsHeader.getAttribute(var1, var2);
   }

   public String getAttribute(@NotNull QName var1) {
      return this._jaxwsHeader.getAttribute(var1);
   }

   public XMLStreamReader readHeader() throws XMLStreamException {
      return this._jaxwsHeader.readHeader();
   }

   public <T> T readAsJAXB(Unmarshaller var1) throws JAXBException {
      return this._jaxwsHeader.readAsJAXB(var1);
   }

   public <T> T readAsJAXB(Bridge<T> var1) throws JAXBException {
      return this._jaxwsHeader.readAsJAXB(var1);
   }

   public WSEndpointReference readAsEPR(AddressingVersion var1) throws XMLStreamException {
      return this._jaxwsHeader.readAsEPR(var1);
   }

   public void writeTo(XMLStreamWriter var1) throws XMLStreamException {
      this._jaxwsHeader.writeTo(var1);
   }

   public void writeTo(SOAPMessage var1) throws SOAPException {
      this._jaxwsHeader.writeTo(var1);
   }

   public void writeTo(ContentHandler var1, ErrorHandler var2) throws SAXException {
      this._jaxwsHeader.writeTo(var1, var2);
   }

   public String getStringContent() {
      return this._jaxwsHeader.getStringContent();
   }

   static {
      NAME = PhysicalStoreNameHeader.QNAME;
      TYPE = new MsgHeaderType();
   }
}
