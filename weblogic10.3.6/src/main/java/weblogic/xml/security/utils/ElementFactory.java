package weblogic.xml.security.utils;

import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLName;

public final class ElementFactory {
   public static final String XMLNS = "xmlns";

   public static XMLName createXMLName(String var0, String var1, String var2) {
      return new PrefixableName(var0, var1, var2);
   }

   public static XMLName createXMLName(String var0, String var1) {
      return createXMLName(var0, var1, (String)null);
   }

   public static XMLName createXMLName(String var0) {
      return createXMLName((String)null, var0, (String)null);
   }

   public static Attribute createAttribute(XMLName var0, String var1) {
      return new MutableAttribute(var0, var1);
   }

   public static Attribute createAttribute(String var0, String var1) {
      XMLName var2 = createXMLName((String)null, var0, (String)null);
      return new MutableAttribute(var2, var1);
   }

   public static Attribute createAttribute(String var0, String var1, String var2) {
      XMLName var3 = createXMLName(var0, var1, (String)null);
      return new MutableAttribute(var3, var2);
   }

   public static Attribute createAttribute(String var0, String var1, String var2, String var3) {
      XMLName var4 = createXMLName(var0, var1, var2);
      return new MutableAttribute(var4, var3);
   }

   public static StartElement createStartElement(XMLName var0) {
      return new MutableStart(var0);
   }

   public static EndElement createEndElement(XMLName var0) {
      return new MutableEnd(var0);
   }

   public static Attribute createNamespaceAttribute(String var0, String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("namespaceUri cannot be null");
      } else {
         return var0 == null ? createAttribute("xmlns", var1) : createAttribute(createXMLName((String)null, var0, "xmlns"), var1);
      }
   }

   public static XMLEvent createCharacterData(String var0) {
      return weblogic.xml.stream.ElementFactory.createCharacterData(var0);
   }

   public static StartElement createStartElement(String var0, String var1) {
      XMLName var2 = createXMLName(var0, var1);
      return createStartElement(var2);
   }

   public static StartElement createStartElement(String var0, String var1, String var2) {
      XMLName var3 = createXMLName(var0, var1, var2);
      return createStartElement(var3);
   }

   public static StartElement createStartElement(String var0) {
      XMLName var1 = createXMLName(var0);
      return createStartElement(var1);
   }

   public static EndElement createEndElement(String var0, String var1) {
      XMLName var2 = createXMLName(var0, var1);
      return createEndElement(var2);
   }

   public static EndElement createEndElement(String var0, String var1, String var2) {
      XMLName var3 = createXMLName(var0, var1, var2);
      return createEndElement(var3);
   }

   public static EndElement createEndElement(String var0) {
      XMLName var1 = createXMLName(var0);
      return createEndElement(var1);
   }
}
