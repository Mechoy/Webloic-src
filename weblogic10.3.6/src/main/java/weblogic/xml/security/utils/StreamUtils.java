package weblogic.xml.security.utils;

import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.CharacterData;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class StreamUtils {
   private static final String SPACES = "                                                                                                                                            ";
   public static final String PRETTY_PRINT_PROPERTY = "weblogic.xml.security.prettyprint";
   private static boolean PRETTY_PRINT = Boolean.getBoolean(System.getProperty("weblogic.xml.security.prettyprint"));

   public static String space(int var0) {
      var0 %= 80;
      return "                                                                                                                                            ".substring(0, var0);
   }

   public static String getValue(XMLInputStream var0, String var1, String var2) throws XMLStreamException {
      XMLEvent var3 = getElement(var0, var1, var2);
      if (var3 == null) {
         return null;
      } else {
         var3 = skipWS(var0, false);
         if (!var3.isCharacterData()) {
            throw new XMLStreamException(var2 + " does not contain text: " + var3);
         } else {
            String var4 = ((CharacterData)var3).getContent();
            var3 = skipWS(var0, false);
            if (!var3.isEndElement()) {
               throw new XMLStreamException("Malformed " + var2);
            } else {
               return var4.trim();
            }
         }
      }
   }

   public static String getData(XMLInputStream var0, String var1) throws XMLStreamException {
      XMLEvent var2 = skipWS(var0, false);
      if (!var2.isCharacterData()) {
         throw new XMLStreamException(var1 + " does not contain data: " + var2);
      } else {
         return ((CharacterData)var2).getContent().trim();
      }
   }

   public static XMLEvent getElement(XMLInputStream var0, String var1, String var2) throws XMLStreamException {
      return element(var0, var1, var2, false);
   }

   public static boolean peekElement(XMLInputStream var0, String var1, String var2) throws XMLStreamException {
      return element(var0, var1, var2, true) != null;
   }

   public static void closeScope(XMLInputStream var0, String var1, String var2) throws XMLStreamException {
      boolean var3 = var0.skip(4);
      if (!var3) {
         throw new XMLStreamException("Found: " + var0.peek() + " expected: " + var2);
      } else {
         XMLEvent var4 = var0.next();
         if (!var4.getName().getLocalName().equals(var2) || !var4.getName().getNamespaceUri().equals(var1)) {
            throw new XMLStreamException("<" + var2 + "> terminated with </" + var4.getName().getLocalName() + ">");
         }
      }
   }

   public static void closeScope(XMLInputStream var0, String var1) throws XMLStreamException {
      boolean var2 = var0.skip(4);
      if (!var2) {
         throw new XMLStreamException("Found: " + var0.peek() + " expected: " + var1);
      } else {
         XMLEvent var3 = var0.next();
         if (!var3.getName().getLocalName().equals(var1)) {
            throw new XMLStreamException("<" + var1 + "> terminated with </" + var3.getName().getLocalName() + ">");
         }
      }
   }

   public static Attribute createAttribute(String var0, String var1, String var2) {
      if (var2 == null) {
         return null;
      } else {
         XMLName var3 = ElementFactory.createXMLName(var0, var1);
         return ElementFactory.createAttribute(var3, var2);
      }
   }

   public static Attribute createAttribute(String var0, String var1) {
      if (var1 == null) {
         return null;
      } else {
         XMLName var2 = ElementFactory.createXMLName(var0);
         return ElementFactory.createAttribute(var2, var1);
      }
   }

   public static XMLEvent peekElement(XMLInputStream var0) throws XMLStreamException {
      return skipWS(var0, true);
   }

   public static final boolean matches(XMLEvent var0, String var1, String var2) {
      XMLName var3 = var0.getName();
      return var3.getLocalName().equals(var1) && var3.getNamespaceUri().equals(var2);
   }

   public static final boolean matches(XMLEvent var0, XMLName var1) {
      return var1.equals(var0.getName());
   }

   public static final boolean matches(XMLEvent var0, String var1) {
      return var0.getName().getLocalName().equals(var1);
   }

   public static final boolean matchesNamespace(XMLEvent var0, String var1) {
      return var0.getName().getNamespaceUri().equals(var1);
   }

   private static XMLEvent element(XMLInputStream var0, String var1, String var2, boolean var3) throws XMLStreamException {
      XMLEvent var4 = skipWS(var0, true);
      if (!var4.isStartElement()) {
         return null;
      } else if (var4.getName().getLocalName().equals(var2) && var4.getName().getNamespaceUri().equals(var1)) {
         return var3 ? var4 : var0.next();
      } else {
         return null;
      }
   }

   public static void discard(XMLInputStream var0) throws XMLStreamException {
      int var1 = 0;
      boolean var2 = true;

      while(var1 > 0 || var2) {
         var2 = false;
         XMLEvent var3 = var0.next();
         if (var3.isStartElement()) {
            ++var1;
         } else if (var3.isEndElement()) {
            --var1;
         }
      }

   }

   public static XMLEvent skipWS(XMLInputStream var0, boolean var1) throws XMLStreamException {
      for(XMLEvent var2 = var0.peek(); var2.isSpace() || var2.isStartPrefixMapping() || var2.isEndPrefixMapping() || var2.isChangePrefixMapping(); var2 = var0.peek()) {
         var0.next();
      }

      if (var1) {
         return var0.peek();
      } else {
         return var0.next();
      }
   }

   public static XMLEvent indent(int var0) {
      return ElementFactory.createCharacterData("\n" + space(var0));
   }

   public static void add(XMLOutputStream var0, XMLEvent var1, int var2) throws XMLStreamException {
      if (PRETTY_PRINT) {
         var0.add(indent(var2));
      }

      var0.add(var1);
   }

   public static void required(Object var0, String var1, String var2) throws XMLStreamException {
      if (var0 == null) {
         throw new XMLStreamException("<" + var1 + "> missing <" + var2 + ">");
      }
   }

   public static void requiredAttr(String var0, String var1, String var2) throws XMLStreamException {
      if (var0 == null) {
         throw new XMLStreamException("<" + var1 + "> missing attribute: " + var2);
      }
   }

   public static String getAttribute(StartElement var0, String var1) {
      XMLName var2 = ElementFactory.createXMLName(var1);
      Attribute var3 = var0.getAttributeByName(var2);
      return var3 == null ? null : var3.getValue();
   }

   public static void addStart(XMLOutputStream var0, String var1, String var2, int var3) throws XMLStreamException {
      if (PRETTY_PRINT) {
         var0.add(indent(var3));
      }

      addStart(var0, var1, var2);
   }

   public static void addStart(XMLOutputStream var0, String var1, int var2) throws XMLStreamException {
      if (PRETTY_PRINT) {
         var0.add(indent(var2));
      }

      addStart(var0, var1);
   }

   public static void addStart(XMLOutputStream var0, String var1, String var2) throws XMLStreamException {
      var0.add(ElementFactory.createStartElement(var1, var2));
   }

   public static void addStart(XMLOutputStream var0, String var1) throws XMLStreamException {
      var0.add(ElementFactory.createStartElement(var1));
   }

   public static void addStart(XMLOutputStream var0, String var1, String var2, Attribute[] var3, int var4) throws XMLStreamException {
      if (PRETTY_PRINT) {
         var0.add(indent(var4));
      }

      var0.add(createStartElement(var1, var2, var3));
   }

   public static void addStart(XMLOutputStream var0, String var1, String var2, Attribute[] var3, Attribute[] var4, int var5) throws XMLStreamException {
      MutableStart var6 = null;
      var6 = createStartElement(var1, var2, var3);

      for(int var7 = 0; var7 < var4.length; ++var7) {
         Attribute var8 = var4[var7];
         var6.addNamespace(var8);
      }

      if (PRETTY_PRINT) {
         var0.add(indent(var5));
      }

      var0.add(var6);
   }

   private static MutableStart createStartElement(String var0, String var1, Attribute[] var2) {
      MutableStart var3 = (MutableStart)ElementFactory.createStartElement(var0, var1);

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4] != null) {
            var3.addAttribute(var2[var4]);
         }
      }

      return var3;
   }

   public static void addStart(XMLOutputStream var0, String var1, String var2, Attribute[] var3) throws XMLStreamException {
      MutableStart var4 = (MutableStart)ElementFactory.createStartElement(var1, var2);

      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5] != null) {
            var4.addAttribute(var3[var5]);
         }
      }

      var0.add(var4);
   }

   public static void addStart(XMLOutputStream var0, String var1, Attribute[] var2) throws XMLStreamException {
      MutableStart var3 = (MutableStart)ElementFactory.createStartElement(var1);

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4] != null) {
            var3.addAttribute(var2[var4]);
         }
      }

      var0.add(var3);
   }

   public static void addStart(XMLOutputStream var0, String var1, Attribute[] var2, int var3) throws XMLStreamException {
      if (PRETTY_PRINT) {
         var0.add(indent(var3));
      }

      addStart(var0, var1, var2);
   }

   public static void addEnd(XMLOutputStream var0, String var1, String var2, int var3) throws XMLStreamException {
      if (PRETTY_PRINT) {
         var0.add(indent(var3));
      }

      addEnd(var0, var1, var2);
   }

   public static void addEnd(XMLOutputStream var0, String var1, String var2) throws XMLStreamException {
      var0.add(ElementFactory.createEndElement(var1, var2));
   }

   public static void addEnd(XMLOutputStream var0, String var1, int var2) throws XMLStreamException {
      if (PRETTY_PRINT) {
         var0.add(indent(var2));
      }

      var0.add(ElementFactory.createEndElement(var1));
   }

   public static void addEnd(XMLOutputStream var0, String var1) throws XMLStreamException {
      var0.add(ElementFactory.createEndElement(var1));
   }

   public static void addEmptyElement(XMLOutputStream var0, String var1, Attribute[] var2, int var3) throws XMLStreamException {
      addStart(var0, var1, var2, var3);
      var0.add(ElementFactory.createEndElement(var1));
   }

   public static void addElement(XMLOutputStream var0, String var1, String var2, String var3, int var4, int var5) throws XMLStreamException {
      addStart(var0, var1, var2, var4);
      if (var3 != null) {
         addText(var0, var3, var4 + var5);
      }

      addEnd(var0, var1, var2, var4);
   }

   public static void addElement(XMLOutputStream var0, String var1, String var2, String var3, Attribute[] var4, int var5, int var6) throws XMLStreamException {
      addStart(var0, var1, var2, var4, var5);
      if (var3 != null) {
         addText(var0, var3, var5 + var6);
      }

      addEnd(var0, var1, var2, var5);
   }

   public static void addElement(XMLOutputStream var0, String var1, String var2, Attribute[] var3, int var4, int var5) throws XMLStreamException {
      addStart(var0, var1, var3, var4);
      if (var2 != null) {
         addText(var0, var2, var4 + var5);
      }

      addEnd(var0, var1, var4);
   }

   public static void addElement(XMLOutputStream var0, String var1, String var2, String var3, int var4) throws XMLStreamException {
      addStart(var0, var1, var2, var4);
      addText(var0, var3, var4 + 2);
      addEnd(var0, var1, var2, var4);
   }

   public static void addElement(XMLOutputStream var0, String var1, String var2, int var3) throws XMLStreamException {
      addStart(var0, var1, var3);
      var0.add(ElementFactory.createCharacterData(var2));
      var0.add(ElementFactory.createEndElement(var1));
   }

   public static void addElement(XMLOutputStream var0, String var1, String var2, int var3, int var4) throws XMLStreamException {
      addStart(var0, var1, var3);
      addText(var0, var2, var3 + var4);
      addEnd(var0, var1, var3);
   }

   public static void addText(XMLOutputStream var0, String var1, int var2) throws XMLStreamException {
      add(var0, ElementFactory.createCharacterData(var1), var2);
   }

   public static String getAttributeByName(String var0, String var1, StartElement var2) {
      AttributeIterator var3 = var2.getAttributes();

      while(var3.hasNext()) {
         Attribute var4 = var3.next();
         XMLName var5 = var4.getName();
         if (var0.equals(var5.getLocalName())) {
            String var6 = var5.getNamespaceUri();
            if (var6 != null) {
               if (var6.equals(var1)) {
                  return var4.getValue();
               }
            } else if (var1 == null) {
               return var4.getValue();
            }
         }
      }

      return null;
   }
}
