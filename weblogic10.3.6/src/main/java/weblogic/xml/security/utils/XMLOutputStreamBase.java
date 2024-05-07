package weblogic.xml.security.utils;

import java.io.StringReader;
import java.util.Map;
import weblogic.xml.babel.stream.XMLInputStreamFactoryImpl;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.stream.util.TypeFilter;

public abstract class XMLOutputStreamBase implements NSOutputStream {
   protected NSOutputStream dest;
   private StartElement lastStart;
   private MutableStart mutableElement;
   private static final String XMLNS = "xmlns";

   protected XMLOutputStreamBase() {
      this.dest = null;
   }

   protected XMLOutputStreamBase(XMLOutputStream var1) {
      this.setDestination(var1);
   }

   protected void setDestination(XMLOutputStream var1) {
      if (var1 instanceof NSOutputStream) {
         this.dest = (NSOutputStream)var1;
      } else {
         this.dest = new NamespaceAwareXOS(var1);
      }

   }

   protected abstract void addXMLEvent(XMLEvent var1) throws XMLStreamException;

   public final void add(XMLEvent var1) throws XMLStreamException {
      if (this.lastStart != null) {
         this.addXMLEvent(this.lastStart);
         this.clearLastStart();
      }

      if (var1.isStartElement()) {
         this.setLastStart((StartElement)var1);
      } else {
         this.addXMLEvent(var1);
      }

   }

   protected void setLastStart(StartElement var1) {
      this.lastStart = var1;
      this.mutableElement = null;
   }

   protected void clearLastStart() {
      this.lastStart = null;
      this.mutableElement = null;
   }

   public void add(XMLInputStream var1) throws XMLStreamException {
      while(var1.hasNext()) {
         this.add(var1.next());
      }

   }

   public void add(String var1) throws XMLStreamException {
      new XMLInputStreamFactoryImpl();
      XMLInputStreamFactory var2 = XMLInputStreamFactoryImpl.newInstance();
      XMLInputStream var3 = var2.newInputStream(new StringReader(var1), new TypeFilter(8318));
      this.add(var3);
   }

   public final void add(Attribute var1) throws XMLStreamException {
      if (this.lastStart == null) {
         throw new XMLStreamException("Cannot add attribute: Last event was not a StartEvent");
      } else {
         if (this.mutableElement == null) {
            this.lastStart = this.mutableElement = new MutableStart(this.lastStart);
         }

         XMLName var2 = var1.getName();
         String var3 = var2.getPrefix();
         String var4 = var2.getLocalName();
         if (!"xmlns".equals(var3) && (var3 != null || !"xmlns".equals(var4))) {
            this.mutableElement.addAttribute(var1);
         } else {
            this.mutableElement.addNamespace(var1);
         }

      }
   }

   public void close() throws XMLStreamException {
      this.close(true);
   }

   public void close(boolean var1) throws XMLStreamException {
      if (this.lastStart != null) {
         this.dest.add(this.lastStart);
      }

      this.clearLastStart();
      this.dest.close(var1);
   }

   public void flush() throws XMLStreamException {
      if (this.lastStart != null) {
         this.dest.add(this.lastStart);
      }

      this.clearLastStart();
      this.dest.flush();
   }

   public void addPrefix(String var1, String var2) {
      this.dest.addPrefix(var1, var2);
   }

   public Map getNamespaces() {
      return this.dest.getNamespaces();
   }
}
