package weblogic.xml.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class Builder {
   public static void nextTag(XMLStreamReader var0) throws XMLStreamException {
      while(true) {
         if (var0.hasNext()) {
            if (!var0.isStartElement() && !var0.isEndElement()) {
               var0.next();
               continue;
            }

            return;
         }

         return;
      }
   }

   public NodeImpl create(File var1) throws XMLStreamException, DOMException, IOException {
      XMLInputFactory var2 = XMLInputFactory.newInstance();
      XMLStreamReader var3 = var2.createXMLStreamReader(new FileInputStream(var1));
      nextTag(var3);
      return read(new ElementNode(), var3);
   }

   public void readText(ElementNode var1, XMLStreamReader var2) throws XMLStreamException, DOMException {
      for(; var2.getEventType() == 4 || var2.getEventType() == 5; var2.next()) {
         switch (var2.getEventType()) {
            case 4:
               var1.appendChild(new TextNode(var2.getText()));
               break;
            case 5:
               var1.appendChild((new TextNode(var2.getText())).asComment());
         }
      }

   }

   public static ElementNode read(ElementNode var0, XMLStreamReader var1) throws XMLStreamException, DOMException {
      if (var1.getEventType() != 1) {
         throw new XMLStreamException("Expected a Start Element!");
      } else {
         nextTag(var1);
         var0.setPrefix(var1.getPrefix());
         var0.setNamespaceURI(var1.getNamespaceURI());
         var0.setLocalName(var1.getLocalName());
         int var2 = var1.getAttributeCount();
         int var3 = var1.getNamespaceCount();
         int var4 = var2 + var3;
         if (var4 > 0) {
            AttributeMap var5 = new AttributeMap(var4);
            var5.setOwner(var0);
            var0.setAttributes(var5);
            int var6;
            if (var3 > 0) {
               for(var6 = 0; var6 < var3; ++var6) {
                  String var7 = var1.getNamespacePrefix(var6);
                  if (var7 == null) {
                     var5.setAttribute(var6, ElementNode.XMLNS_URI, ElementNode.XMLNS, (String)null, var1.getNamespaceURI(var6));
                  } else {
                     var5.setAttribute(var6, ElementNode.XMLNS_URI, var7, ElementNode.XMLNS, var1.getNamespaceURI(var6));
                  }
               }
            }

            if (var2 > 0) {
               for(var6 = 0; var6 < var2; ++var6) {
                  var5.setAttribute(var6 + var3, var1.getAttributeNamespace(var6), var1.getAttributeLocalName(var6), var1.getAttributePrefix(var6), var1.getAttributeValue(var6));
               }
            }
         }

         var1.next();

         while(var1.getEventType() == 1 || var1.getEventType() == 4 || var1.getEventType() == 5 || var1.getEventType() == 3) {
            switch (var1.getEventType()) {
               case 1:
                  var0.appendChild(read(new ElementNode(), var1));
               case 2:
               default:
                  break;
               case 3:
                  var0.appendChild(new PINode(var1.getPITarget(), var1.getPIData()));
                  var1.next();
                  break;
               case 4:
                  var0.appendChild(new TextNode(var1.getText()));
                  var1.next();
                  break;
               case 5:
                  var0.appendChild((new TextNode(var1.getText())).asComment());
                  var1.next();
            }
         }

         if (var1.getEventType() != 2) {
            throw new XMLStreamException("Expected an End Element!");
         } else {
            var1.next();
            return var0;
         }
      }
   }

   public static void main(String[] var0) throws Exception {
      Builder var1 = new Builder();
      NodeImpl var2 = var1.create(new File(var0[0]));
      System.out.println(var2);
      NodeIterator var3 = var2.iterator();

      while(true) {
         while(var3.hasNext()) {
            Node var4 = var3.nextNode();
            if (var4.getNodeType() != 3 && var4.getNodeType() != 8 && var4.getNodeType() != 4) {
               System.out.println("[" + var4.getLocalName() + "]");
            } else {
               System.out.println("[" + var4 + "]");
            }
         }

         XMLStreamIterator var6 = new XMLStreamIterator(var2);

         while(true) {
            while(var6.hasNext()) {
               Node var5 = var6.nextNode();
               if (var5.getNodeType() != 3 && var5.getNodeType() != 8 && var5.getNodeType() != 4) {
                  if (var6.isOpen()) {
                     System.out.println("<" + var5.getLocalName() + ">");
                  } else {
                     System.out.println("</" + var5.getLocalName() + ">");
                  }
               } else {
                  System.out.println(var5);
               }
            }

            return;
         }
      }
   }
}
