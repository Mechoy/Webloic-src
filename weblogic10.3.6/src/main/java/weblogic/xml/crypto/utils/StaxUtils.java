package weblogic.xml.crypto.utils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import weblogic.xml.dom.DOMStreamReader;
import weblogic.xml.stax.ReaderToWriter;

public class StaxUtils {
   public static void forwardToEndElement(String var0, String var1, XMLStreamReader var2) throws XMLStreamException {
      while(var2.getEventType() != 2 || !var0.equals(var2.getNamespaceURI()) || !var1.equals(var2.getLocalName())) {
         var2.next();
      }

   }

   public static void writeElement(XMLStreamWriter var0, String var1, String var2, String var3) throws XMLStreamException {
      var0.writeStartElement(var1, var2);
      var0.writeCharacters(var3);
      var0.writeEndElement();
   }

   public static void skipChildren(XMLStreamReader var0) throws XMLStreamException {
      int var1 = 0;
      int var2 = var0.getEventType();

      while(var1 > 0 || var1 == 0 && var2 != 2) {
         var2 = var0.next();
         switch (var2) {
            case 1:
               ++var1;
               break;
            case 2:
               --var1;
         }
      }

   }

   public static String getAttributeValue(String var0, String var1, XMLStreamReader var2) {
      if (var2.getEventType() != 1) {
      }

      String var3 = null;

      for(int var4 = 0; var4 < var2.getAttributeCount(); ++var4) {
         String var5 = var2.getAttributeLocalName(var4);
         if (var5.equals(var1)) {
            String var6 = var2.getAttributeNamespace(var4);
            String var7 = var2.getAttributeValue(var4);
            if (var6 != null && var6.length() != 0) {
               if (var6.equals(var0)) {
                  var3 = var7;
                  break;
               }
            } else {
               var3 = var7;
               if (var0 == null) {
                  break;
               }
            }
         }
      }

      return var3;
   }

   public static boolean findStart(XMLStreamReader var0, String var1, String var2) throws XMLStreamException {
      if (var2 == null) {
         throw new IllegalArgumentException("name parameter cannot be null");
      } else {
         int var3;
         if (!var0.isStartElement() && !var0.isEndElement()) {
            var3 = var0.nextTag();
         } else {
            var3 = var0.getEventType();
         }

         switch (var3) {
            case 1:
               boolean var10000;
               label48: {
                  String var4 = var0.getNamespaceURI();
                  String var5 = var0.getLocalName();
                  if (var2.equals(var5)) {
                     if (var1 == null) {
                        if (var4 == null) {
                           break label48;
                        }
                     } else if (var1.equals(var4)) {
                        break label48;
                     }
                  }

                  var10000 = false;
                  return var10000;
               }

               var10000 = true;
               return var10000;
            default:
               return false;
         }
      }
   }

   public static int skipWS(XMLStreamReader var0) throws XMLStreamException {
      int var1 = var0.getEventType();
      boolean var2 = true;

      do {
         switch (var1) {
            case 3:
            case 5:
            case 6:
               var1 = var0.next();
               break;
            case 4:
               if (var0.isWhiteSpace()) {
                  var1 = var0.next();
               } else {
                  var2 = false;
               }
               break;
            default:
               var2 = false;
         }
      } while(var2);

      return var1;
   }

   public static boolean findStart(XMLStreamReader var0, String var1, String var2, boolean var3) throws XMLStreamException {
      if (findStart(var0, var1, var2)) {
         return true;
      } else if (var3) {
         throw new XMLStreamException("Did not find expected element " + var1 + ":" + var2);
      } else {
         return false;
      }
   }

   public static void findEnd(XMLStreamReader var0, String var1, String var2) throws XMLStreamException {
      skipChildren(var0);
      var0.require(2, var1, var2);
   }

   public static String getElementValue(XMLStreamReader var0, String var1, String var2) throws XMLStreamException {
      String var3;
      if (findStart(var0, var1, var2)) {
         var0.next();
         int var4 = skipWS(var0);
         if (var4 == 4) {
            var3 = var0.getText();
         } else {
            var3 = null;
         }

         findEnd(var0, var1, var2);
         var0.next();
      } else {
         var3 = null;
      }

      return var3;
   }

   public static String readElement(XMLStreamReader var0, String var1, String var2) throws XMLStreamException {
      readStart(var0, var1, var2);
      String var3 = readCharacters(var0);
      readEnd(var0, var1, var2);
      return var3;
   }

   public static int readEnd(XMLStreamReader var0, String var1, String var2) throws XMLStreamException {
      findEnd(var0, var1, var2);
      return var0.hasNext() ? var0.nextTag() : 8;
   }

   public static String readCharacters(XMLStreamReader var0) throws XMLStreamException {
      skipWS(var0);
      var0.require(4, (String)null, (String)null);
      String var1 = var0.getText();
      var0.next();
      return var1;
   }

   public static int readStart(XMLStreamReader var0, String var1, String var2) throws XMLStreamException {
      findStart(var0, var1, var2, true);
      return var0.next();
   }

   public static void writeNode(XMLStreamWriter var0, Node var1) throws XMLStreamException {
      if (((Node)var1).getNodeType() == 9) {
         var1 = ((Document)var1).getDocumentElement();
      }

      DOMStreamReader var2 = new DOMStreamReader((Node)var1);
      ReaderToWriter var3 = new ReaderToWriter(var0);
      var3.writeSubTree(var2);
   }

   public static boolean is(XMLStreamReader var0, QName var1) {
      return var1.getNamespaceURI().equals(var0.getNamespaceURI()) && var1.getLocalPart().equals(var0.getLocalName());
   }
}
