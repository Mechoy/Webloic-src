package weblogic.wsee.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.Location;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLStreamException;
import weblogic.xml.stream.events.Name;
import weblogic.xml.stream.util.TypeFilter;
import weblogic.xml.xmlnode.XMLNode;
import weblogic.xml.xmlnode.XMLNodeSet;

public class ParsingHelper {
   private XMLInputStream stream;
   private static final boolean debug = false;
   private static final String DD_NS = "http://www.bea.com/servers/wls70";

   public ParsingHelper(InputStream var1) throws XMLStreamException {
      TypeFilter var2 = new TypeFilter(22);
      this.stream = XMLInputStreamFactory.newInstance().newInputStream(var1, var2);
      this.stream.skip(2);
   }

   public ParsingHelper(XMLInputStream var1) throws XMLStreamException {
      this.stream = var1;
      this.stream.skip(2);
   }

   public XMLEvent peekStartElement(String var1) throws XMLStreamException, DDProcessingException {
      XMLEvent var2 = this.stream.peek();
      return var2.getType() == 2 && var2.getName().getLocalName().equals(var1) ? var2 : null;
   }

   public XMLEvent peekStartElement() throws XMLStreamException, DDProcessingException {
      XMLEvent var1 = this.stream.peek();
      return var1.getType() == 2 ? var1 : null;
   }

   public XMLEvent matchStartElement(String var1) throws XMLStreamException, DDProcessingException {
      XMLEvent var2 = this.stream.peek();
      if (var2.getType() != 2) {
         throw new DDProcessingException("Expected start element <" + var1 + ">, but got " + var2.getTypeAsString() + " instead", var2.getLocation());
      } else {
         String var3 = var2.getName().getLocalName();
         if (!var1.equals(var3)) {
            throw new DDProcessingException("Expected element <" + var1 + ">, but got <" + var3 + "> instead", var2.getLocation());
         } else {
            return this.stream.next();
         }
      }
   }

   public XMLEvent matchStartElement(String[] var1) throws XMLStreamException, DDProcessingException {
      XMLEvent var2 = this.matchOptionalStartElement(var1);
      if (var2 != null) {
         return var2;
      } else {
         StringBuffer var3 = new StringBuffer("Expected a start element matching any of ");

         for(int var4 = 0; var4 < var1.length - 1; ++var4) {
            var3.append("<").append(var1[var4]).append(">,");
         }

         var3.append(" or <").append(var1[var1.length - 1]).append(">");
         var3.append(" but got ");
         var3.append(this.stream.peek().toString());
         var3.append(" instead");
         throw new DDProcessingException(var3.toString(), this.stream.peek().getLocation());
      }
   }

   public XMLEvent matchOptionalStartElement(String[] var1) throws XMLStreamException, DDProcessingException {
      XMLEvent var2 = null;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2 = this.matchOptionalStartElement(var1[var3]);
         if (var2 != null) {
            break;
         }
      }

      return var2;
   }

   public XMLEvent matchOptionalStartElement(String var1) throws XMLStreamException {
      XMLEvent var2 = this.stream.peek();
      return var2.getType() == 2 && var2.getName().getLocalName().equals(var1) ? this.stream.next() : null;
   }

   public XMLEvent matchEndElement(String var1) throws XMLStreamException, DDProcessingException {
      XMLEvent var2 = this.stream.peek();
      if (var2.getType() != 4) {
         StringBuffer var3 = new StringBuffer();
         var3.append("Expected end element </" + var1 + ">, but got ");
         var3.append(var2.getTypeAsString());
         if (var2.getName() != null) {
            var3.append("(" + var2.getName().getQualifiedName() + ")");
         }

         var3.append(" instead.");
         throw new DDProcessingException(var3.toString(), var2.getLocation());
      } else {
         return this.stream.next();
      }
   }

   public XMLEvent matchDocumentEnd() throws XMLStreamException, DDProcessingException {
      XMLEvent var1 = this.stream.peek();
      if (var1.getType() != 512 && var1.getType() != 128) {
         throw new DDProcessingException("Expected end of document, but got " + var1.getTypeAsString() + " instead", var1.getLocation());
      } else {
         return this.stream.next();
      }
   }

   public Location getLocation() throws XMLStreamException {
      XMLEvent var1 = this.stream.peek();
      return var1 == null ? null : var1.getLocation();
   }

   public XMLNode forkSubtree() throws XMLStreamException {
      XMLNode var1 = new XMLNode();
      XMLInputStream var2 = this.stream.getSubStream();
      this.stream.skipElement();

      try {
         var1.read(var2);
         return var1;
      } catch (IOException var4) {
         throw new XMLStreamException(var4);
      }
   }

   public XMLNodeSet forkSubtrees() throws XMLStreamException {
      XMLNodeSet var1 = new XMLNodeSet();
      XMLEvent var2 = this.stream.peek();
      if (var2.getType() != 2) {
         throw new XMLStreamException("Expected a start element");
      } else {
         XMLEvent var3 = var2;

         while(var3.getName().equals(var2.getName())) {
            try {
               XMLNode var4 = new XMLNode();
               var4.read(this.stream);
               var1.addXMLNode(var4);
            } catch (IOException var5) {
               throw new XMLStreamException(var5);
            }

            var3 = this.stream.peek();
            if (var3.getName() == null) {
               break;
            }
         }

         return var1;
      }
   }

   public static NSAttribute getRequiredAttribute(StartElement var0, String var1) throws XMLStreamException, DDProcessingException {
      Attribute var2 = var0.getAttributeByName(new Name(var1));
      if (var2 == null) {
         throw new DDProcessingException("Could not find required attribute \"" + var1 + "\" for element <" + var0.getName().getLocalName() + ">", var0.getLocation());
      } else {
         return new NSAttribute(var0, var2);
      }
   }

   public static NSAttribute getOptionalAttribute(StartElement var0, String var1) throws XMLStreamException, DDProcessingException {
      Attribute var2 = var0.getAttributeByName(new Name(var1));
      return var2 == null ? null : new NSAttribute(var0, var2);
   }

   public static void checkAttributes(StartElement var0, String[] var1) throws DDProcessingException {
      HashSet var2 = new HashSet(Arrays.asList(var1));
      AttributeIterator var3 = var0.getAttributes();

      XMLName var4;
      do {
         do {
            if (!var3.hasNext()) {
               return;
            }

            var4 = var3.next().getName();
         } while(var4.getNamespaceUri() != null && !var4.getNamespaceUri().equals("http://www.bea.com/servers/wls70"));
      } while(var2.contains(var4.getLocalName()));

      throw new DDProcessingException("Unrecognized attribute " + var4.getLocalName(), var0.getLocation());
   }

   private static void dumpEvents(XMLInputStream var0) throws XMLStreamException {
      while(var0.hasNext()) {
         XMLEvent var1 = var0.next();
         System.out.print("EVENT: " + var1.getTypeAsString() + " [");
         System.out.print(var1);
         System.out.println("]");
      }

   }
}
