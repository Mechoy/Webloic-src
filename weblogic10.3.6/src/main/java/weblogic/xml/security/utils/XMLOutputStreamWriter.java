package weblogic.xml.security.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.utils.collections.Stack;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.stream.events.AttributeImpl;
import weblogic.xml.stream.events.CharacterDataEvent;
import weblogic.xml.stream.events.EndElementEvent;
import weblogic.xml.stream.events.Name;
import weblogic.xml.stream.events.StartElementEvent;

public class XMLOutputStreamWriter extends AbstractXMLWriter {
   private String ATTR_TYPE;
   private final XMLOutputStream destination;
   private final Stack nameStack;
   private StartElementEvent start;

   public XMLOutputStreamWriter(XMLOutputStream var1) {
      this.ATTR_TYPE = "CDATA";
      this.nameStack = new Stack();
      this.start = null;
      this.destination = var1;
   }

   public XMLOutputStreamWriter(NSOutputStream var1) {
      this((XMLOutputStream)var1);
      Map var2 = var1.getNamespaces();
      this.openScope();
      Set var3 = var2.entrySet();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         this.addNamespacePrefix((String)var5.getKey(), (String)var5.getValue());
      }

   }

   public void writeStartElement(String var1, String var2) {
      if (this.start != null) {
         this.writeStart();
      }

      this.openScope();
      Name var3;
      if (var1 == null) {
         var3 = new Name((String)null, var2, (String)null);
         this.start = new StartElementEvent(var3);
      } else {
         String var4 = this.findPrefix(var1);
         if (var4 == null) {
            var4 = this.generatePrefix(var1);
            var3 = new Name(var1, var2, var4);
            this.start = new StartElementEvent(var3);
            this.bindNamespace(var4, var1);
         } else {
            var3 = new Name(var1, var2, var4);
            this.start = new StartElementEvent(var3);
         }
      }

      this.nameStack.push(var3);
   }

   public void writeAttribute(String var1, String var2, String var3) throws IllegalStateException {
      if (this.start == null) {
         throw new IllegalStateException("Attributes can only be written immediately after start");
      } else {
         Name var4;
         if (var1 != null) {
            String var5 = this.findOrBindNamespace(var1);
            var4 = new Name(var1, var2, var5);
         } else {
            var4 = new Name(var1, var2);
         }

         this.start.addAttribute(new AttributeImpl(var4, var3, this.ATTR_TYPE));
      }
   }

   public void writeCharacters(String var1) throws XMLWriterRuntimeException {
      try {
         if (this.start != null) {
            this.writeStart();
         }

         CharacterDataEvent var2 = new CharacterDataEvent(var1);
         this.destination.add(var2);
      } catch (XMLStreamException var3) {
         throw new XMLWriterRuntimeException(var3);
      }
   }

   private void writeStart() {
      if (this.start != null) {
         try {
            this.destination.add(this.start);
         } catch (XMLStreamException var2) {
            throw new XMLWriterRuntimeException(var2);
         }

         this.start = null;
      }

   }

   public void writeEndElement() throws XMLWriterRuntimeException {
      try {
         if (this.start != null) {
            this.writeStart();
         }

         if (this.nameStack.isEmpty()) {
            throw new XMLWriterRuntimeException("Unmatched End element");
         } else {
            Name var1 = (Name)this.nameStack.pop();
            EndElementEvent var2 = new EndElementEvent(var1);
            this.destination.add(var2);
            this.closeScope();
         }
      } catch (XMLStreamException var3) {
         throw new XMLWriterRuntimeException(var3);
      }
   }

   public void flush() {
      this.writeStart();
   }

   public void close() {
      this.flush();

      try {
         this.destination.close();
      } catch (XMLStreamException var2) {
         throw new XMLWriterRuntimeException(var2);
      }
   }

   protected void bindNamespace(String var1, String var2) {
      if (this.start == null) {
         throw new IllegalStateException("namespace declaration required but start tag has already been written");
      } else {
         this.start.addNamespace(ElementFactory.createNamespaceAttribute(var1, var2));
         this.addNamespacePrefix(var1, var2);
      }
   }

   protected void bindDefaultNamespace(String var1) {
      if (this.start == null) {
         throw new IllegalStateException("namespace declaration required but start tag has already been written");
      } else {
         this.start.addNamespace(ElementFactory.createNamespaceAttribute("", var1));
         this.addDefaultNamespace(var1);
      }
   }
}
