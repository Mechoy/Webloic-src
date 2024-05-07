package weblogic.xml.crypto.encrypt;

import java.util.Collections;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.encrypt.api.ReferenceType;
import weblogic.xml.crypto.utils.StaxUtils;

public abstract class WLReferenceType extends WLURIReference implements WLXMLStructure, ReferenceType {
   public static final String ATTR_URI = "URI";
   private List content;

   protected WLReferenceType() {
      this((String)null, (String)null, (List)null);
   }

   protected WLReferenceType(String var1) {
      this(var1, (String)null, (List)null);
   }

   protected WLReferenceType(String var1, List var2) {
      this(var1, (String)null, var2);
   }

   protected WLReferenceType(String var1, String var2, List var3) {
      super(var1, var2);
      if (var3 != null) {
         this.content = Collections.unmodifiableList(var3);
      } else {
         this.content = null;
      }

   }

   protected abstract String getLocalName();

   protected abstract String getNamespace();

   public String toString() {
      return this.getLocalName() + ":  URI=" + this.getURI();
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement(this.getNamespace(), this.getLocalName());
         var1.writeAttribute("URI", this.getURI());
         this.writeChildren(var1);
         var1.writeEndElement();
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   protected void writeChildren(XMLStreamWriter var1) {
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         StaxUtils.findStart(var1, this.getNamespace(), this.getLocalName(), true);
         String var2 = StaxUtils.getAttributeValue("http://www.w3.org/2001/04/xmlenc#", "URI", var1);
         this.setURI(var2);
         this.readChildren(var1);
         StaxUtils.skipChildren(var1);
         StaxUtils.readEnd(var1, this.getNamespace(), this.getLocalName());
      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }

      if (this.getURI() == null) {
         throw new MarshalException(this.getLocalName() + " did not have required URI attribute");
      }
   }

   protected void readChildren(XMLStreamReader var1) {
   }

   static ReferenceType newInstance(XMLStreamReader var0) throws MarshalException {
      try {
         Object var1;
         if (StaxUtils.findStart(var0, "http://www.w3.org/2001/04/xmlenc#", "DataReference")) {
            var1 = new WLDataReference();
         } else {
            if (!StaxUtils.findStart(var0, "http://www.w3.org/2001/04/xmlenc#", "KeyReference")) {
               throw new MarshalException("found unknown Reference type");
            }

            var1 = new WLKeyReference();
         }

         ((WLReferenceType)var1).read(var0);
         return (ReferenceType)var1;
      } catch (XMLStreamException var3) {
         throw new MarshalException("failed to read reference", var3);
      }
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public List getContent() {
      return this.content;
   }
}
