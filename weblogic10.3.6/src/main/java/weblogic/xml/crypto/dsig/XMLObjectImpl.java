package weblogic.xml.crypto.dsig;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.dom.DOMStructure;
import weblogic.xml.crypto.dsig.api.XMLObject;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.dom.Util;

public class XMLObjectImpl implements XMLObject, WLXMLStructure {
   private static final String OBJECT_ELEMENT = "Object";
   private static final String MIMETYPE_ATTRIBUTE = "MimeType";
   private static final String ENCODING_ATTRIBUTE = "Encoding";
   private List content;
   private String id;
   private String mimeType;
   private String encoding;

   public XMLObjectImpl(List var1, String var2, String var3, String var4) {
      this.content = var1;
      this.id = var2;
      this.mimeType = var3;
      this.encoding = var4;
   }

   public List getContent() {
      return Collections.unmodifiableList(this.content);
   }

   public String getId() {
      return this.id;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "Object");
         if (this.id != null) {
            var1.writeAttribute("Id", this.id);
         }

         if (this.mimeType != null) {
            var1.writeAttribute("MimeType", this.mimeType);
         }

         if (this.encoding != null) {
            var1.writeAttribute("Encoding", this.encoding);
         }

         if (this.content != null && !this.content.isEmpty()) {
            this.writeContent(this.content, var1);
         }

         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to write element Object", var3);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
   }

   private void writeContent(List var1, XMLStreamWriter var2) throws XMLStreamException {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         DOMStructure var4 = (DOMStructure)var3.next();
         final Node var5 = var4.getNode();
         LogUtils.logDsig(new LogUtils.LogMethod() {
            public String log() {
               return "writing Object content node: " + Util.printNode(var5);
            }
         });
         StaxUtils.writeNode(var2, var5);
      }

   }
}
