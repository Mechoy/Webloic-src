package weblogic.xml.security.utils;

import java.util.HashMap;
import java.util.Map;
import weblogic.xml.stream.ChangePrefixMapping;
import weblogic.xml.stream.EndPrefixMapping;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.StartPrefixMapping;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class EventBufferInputStream extends XMLInputStreamBase {
   private static final XMLInputStreamFactory factory = XMLInputStreamFactory.newInstance();
   private final Map namespaceMap = new HashMap();
   private final XMLEventBuffer b;
   private XMLEvent peek = null;

   public EventBufferInputStream(XMLEventBuffer var1, XMLInputStream var2) {
      super(var2);
      this.b = var1;
   }

   public static XMLInputStream reconstitute(XMLEventBuffer var0, XMLInputStream var1) throws XMLStreamException {
      return factory.newBufferedInputStream(new EventBufferInputStream(var0, var1));
   }

   public XMLEvent peek() throws XMLStreamException {
      if (this.peek != null) {
         return this.peek;
      } else if (!this.b.hasNext()) {
         return this.source.peek();
      } else {
         this.peek = this.next();
         return this.peek;
      }
   }

   public XMLEvent next() throws XMLStreamException {
      XMLEvent var1;
      if (this.peek != null) {
         var1 = this.peek;
         this.peek = null;
         return var1;
      } else if (!this.b.hasNext()) {
         return this.source.next();
      } else {
         var1 = this.b.next();
         switch (var1.getType()) {
            case 1024:
               StartPrefixMapping var2 = (StartPrefixMapping)var1;
               this.namespaceMap.put(var2.getPrefix(), var2.getNamespaceUri());
               break;
            case 2048:
               EndPrefixMapping var4 = (EndPrefixMapping)var1;
               this.namespaceMap.remove(var4.getPrefix());
               break;
            case 4096:
               ChangePrefixMapping var3 = (ChangePrefixMapping)var1;
               this.namespaceMap.put(var3.getPrefix(), var3.getNewNamespaceUri());
         }

         return (XMLEvent)(var1.isStartElement() ? new StartEventDelegate((StartElement)var1, this.namespaceMap) : var1);
      }
   }

   public boolean hasNext() throws XMLStreamException {
      return this.peek != null || this.b.hasNext() || this.source.hasNext();
   }

   public XMLInputStream getSubStream() throws XMLStreamException {
      throw new UnsupportedOperationException("substreams not currently supported on this stream type");
   }
}
