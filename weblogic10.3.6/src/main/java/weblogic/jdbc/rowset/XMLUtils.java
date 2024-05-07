package weblogic.jdbc.rowset;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.AttributeIterator;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.Location;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLName;

final class XMLUtils implements XMLSchemaConstants {
   private static String loc(StartElement var0) {
      Location var1 = var0.getLocation();
      return "line: " + var1.getLineNumber() + " column: " + var1.getColumnNumber();
   }

   public static boolean getOptionalBooleanAttribute(StartElement var0, XMLName var1) throws IOException {
      Attribute var2 = var0.getAttributeByName(var1);
      if (var2 == null) {
         return false;
      } else {
         try {
            return new Boolean(var2.getValue());
         } catch (NumberFormatException var4) {
            throw new IOException("Expected true or false, but found " + var2.getValue() + " for attribute: " + var1 + " on element: " + var0 + " at " + loc(var0));
         }
      }
   }

   public static String getOptionalStringAttribute(StartElement var0, XMLName var1) throws IOException {
      Attribute var2 = var0.getAttributeByName(var1);
      return var2 == null ? null : var2.getValue();
   }

   public static Attribute getRequiredAttribute(StartElement var0, String var1) throws IOException {
      return getRequiredAttribute(var0, ElementFactory.createXMLName(var1));
   }

   public static Attribute getRequiredAttribute(StartElement var0, XMLName var1) throws IOException {
      Attribute var2 = var0.getAttributeByName(var1);
      if (var2 == null) {
         throw new IOException("Element " + var0.getName() + " at " + loc(var0) + " does not contain required attribute: " + var1.getLocalName());
      } else {
         return var2;
      }
   }

   public static void addAttributesFromPropertyMap(List var0, Map var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         XMLName var3 = (XMLName)var2.next();
         String var4 = var3.getNamespaceUri();
         String var5 = var3.getPrefix();
         if (var4 != null && var5 != null) {
            var0.add(ElementFactory.createNamespaceAttribute(var5, var4));
         }

         Properties var6 = (Properties)var1.get(var3);
         Enumeration var7 = var6.propertyNames();

         while(var7.hasMoreElements()) {
            String var8 = (String)var7.nextElement();
            String var9 = var6.getProperty(var8);
            XMLName var10 = ElementFactory.createXMLName(var4, var8, var5);
            var0.add(ElementFactory.createAttribute(var10, var9));
         }
      }

   }

   public static Map readPropertyMapFromAttributes(StartElement var0) {
      HashMap var1 = new HashMap();
      AttributeIterator var2 = var0.getAttributes();

      while(var2.hasNext()) {
         Attribute var3 = var2.next();
         XMLName var4 = var3.getName();
         String var5 = var4.getNamespaceUri();
         if (!"http://www.w3.org/2001/XMLSchema".equals(var5) && !"http://www.bea.com/2002/10/weblogicdata".equals(var5) && var5 != null) {
            XMLName var6 = ElementFactory.createXMLName(var4.getNamespaceUri(), "", var4.getPrefix());
            Properties var7 = (Properties)var1.get(var6);
            if (var7 == null) {
               var7 = new Properties();
               var1.put(var6, var7);
            }

            var7.put(var4.getLocalName(), var3.getValue());
         }
      }

      return var1;
   }
}
