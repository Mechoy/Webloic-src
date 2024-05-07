package weblogic.wsee.reliability2.compat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.Holder;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.jaxws.util.WriterUtil;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class SimpleElementSerializer {
   private static final Logger LOGGER = Logger.getLogger(SimpleElementSerializer.class.getName());

   public static void serialize(SimpleElement var0, XMLStreamWriter var1) throws XMLStreamException {
      Holder var2 = new Holder(0);
      serialize(var0, var1, var2);
   }

   public static void serialize(SimpleElement var0, XMLStreamWriter var1, Holder<Integer> var2) throws XMLStreamException {
      QName var3 = var0.getName();

      String var4;
      try {
         if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Writing start element: " + var3);
         }

         var4 = WriterUtil.getBoundNamespace(var1, var3.getNamespaceURI());
         if (var4 == null) {
            var4 = WriterUtil.selectPrefix(var1, var3.getPrefix(), var3.getNamespaceURI(), var2);
            var1.writeStartElement(var4, var3.getLocalPart(), var3.getNamespaceURI());
            var1.writeNamespace(var4, var3.getNamespaceURI());
         } else {
            var1.writeStartElement(var3.getNamespaceURI(), var3.getLocalPart());
         }
      } catch (XMLStreamException var12) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("*** Failed attempt to write element with name: " + var3);
            LOGGER.log(Level.FINE, var12.toString(), var12);
         }
      }

      if (var0.getContent() != null) {
         var4 = var0.getContent();
         if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Writing characters: " + var4);
         }

         var1.writeCharacters(var4);
      } else {
         boolean var13 = false;
         Map var5 = var0.getAttrs();
         if (var5 != null) {
            Iterator var6 = var5.entrySet().iterator();

            while(var6.hasNext()) {
               Map.Entry var7 = (Map.Entry)var6.next();
               QName var8 = (QName)var7.getKey();
               if ("http://www.w3.org/2000/xmlns/".equals(var8.getNamespaceURI())) {
                  WriterUtil.writeNamespace(var1, (String)var7.getValue(), var8.getLocalPart());
               } else {
                  String var9 = var8.getNamespaceURI();
                  String var10 = var8.getLocalPart();
                  if (var9 == null) {
                     var9 = "";
                     var10 = var8.getLocalPart();
                  }

                  String var11;
                  if (var10.startsWith("xmlns:")) {
                     if (!var13) {
                        var13 = true;
                        var11 = var3.getPrefix();
                        if (var11 != null && !"".equals(var11)) {
                           WriterUtil.writeNamespace(var1, var3.getNamespaceURI(), var11);
                        }
                     }

                     WriterUtil.writeNamespace(var1, (String)var7.getValue(), var10.substring(6));
                  } else if (var9.length() == 0) {
                     var1.writeAttribute(var10, (String)var7.getValue());
                  } else {
                     var11 = WriterUtil.getBoundNamespace(var1, var9);
                     if (var11 == null) {
                        var11 = WriterUtil.selectPrefix(var1, var8.getPrefix(), var9, var2);
                        var1.writeNamespace(var11, var9);
                        var1.writeAttribute(var11, var9, var10, (String)var7.getValue());
                     } else {
                        var1.writeAttribute(var9, var10, (String)var7.getValue());
                     }
                  }
               }
            }
         }

         List var14 = var0.getChildren();
         Iterator var15 = var14.iterator();

         while(var15.hasNext()) {
            SimpleElement var16 = (SimpleElement)var15.next();
            serialize(var16, var1, var2);
         }
      }

      var1.writeEndElement();
   }

   public static void serialize(SimpleElement var0, Element var1) {
      if (var0.getContent() != null) {
         DOMUtils.addTextData(var1, var0.getContent());
      } else {
         Map var2 = var0.getAttrs();
         Iterator var3 = var2.keySet().iterator();

         while(true) {
            while(var3.hasNext()) {
               QName var4 = (QName)var3.next();
               String var5 = (String)var2.get(var4);
               String var6 = var4.getNamespaceURI();
               if (var6 != null && var6.length() > 0) {
                  String var7 = var4.getPrefix() != null ? var4.getPrefix() + ":" + var4.getLocalPart() : var4.getLocalPart();
                  var1.setAttributeNS(var6, var7, var5);
               } else {
                  var1.setAttribute(var4.getLocalPart(), var5);
               }
            }

            List var8 = var0.getChildren();
            Iterator var9 = var8.iterator();

            while(var9.hasNext()) {
               SimpleElement var10 = (SimpleElement)var9.next();
               serializeChild(var10, var1);
            }
            break;
         }
      }

   }

   private static void serializeChild(SimpleElement var0, Element var1) {
      QName var2 = var0.getName();
      String var3 = var2.getPrefix() != null ? var2.getPrefix() + ":" + var2.getLocalPart() : var2.getLocalPart();
      Element var4 = DOMUtils.addValueNS(var1, var2.getNamespaceURI(), var3, var0.getContent() != null ? var0.getContent() : "");
      if (var0.getContent() == null) {
         Map var5 = var0.getAttrs();
         Iterator var6 = var5.keySet().iterator();

         while(true) {
            while(var6.hasNext()) {
               QName var7 = (QName)var6.next();
               String var8 = (String)var5.get(var7);
               String var9 = var7.getNamespaceURI();
               if (var9 != null && var9.length() > 0) {
                  var4.setAttributeNS(var9, var7.getLocalPart(), var8);
               } else {
                  var4.setAttribute(var7.getLocalPart(), var8);
               }
            }

            List var10 = var0.getChildren();
            Iterator var11 = var10.iterator();

            while(var11.hasNext()) {
               SimpleElement var12 = (SimpleElement)var11.next();
               serializeChild(var12, var4);
            }
            break;
         }
      }

   }

   public static SimpleElement deserialize(XMLStreamReader var0) throws XMLStreamException {
      SimpleElement var1 = new SimpleElement(var0.getName());
      int var2 = var0.getAttributeCount();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         QName var4 = var0.getAttributeName(var3);
         if (!isNamespaceDecl(var4.getPrefix())) {
            String var5 = var0.getAttributeValue(var3);
            var1.setAttr(var4, var5);
         }
      }

      var3 = 1;

      while(var0.hasNext() && var3 > 0) {
         int var8 = var0.next();
         switch (var8) {
            case 1:
               ++var3;
               SimpleElement var9 = deserialize(var0);
               var1.addChild(var9);
               break;
            case 2:
               --var3;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
               break;
            case 4:
               var1.addContent(var0.getText());
               break;
            case 10:
               QName var6 = var0.getName();
               var0.next();
               String var7 = var0.getText();
               var1.setAttr(var6, var7);
         }
      }

      return var1;
   }

   public static SimpleElement deserialize(Element var0) throws DOMProcessingException {
      QName var1 = getQNameFromDOMNode(var0);
      SimpleElement var2 = new SimpleElement(var1);
      NodeList var3 = var0.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         switch (var5.getNodeType()) {
            case 1:
               Element var7 = (Element)var5;
               SimpleElement var8 = deserialize(var7);
               var2.addChild(var8);
               break;
            case 3:
               String var6 = var5.getNodeValue();
               if (var6 != null && var6.length() > 0) {
                  var2.addContent(var6);
               }
         }
      }

      NamedNodeMap var9 = var0.getAttributes();
      if (var9 != null) {
         for(int var10 = 0; var10 < var9.getLength(); ++var10) {
            Attr var11 = (Attr)var9.item(var10);
            if (!isNamespaceDecl(var11.getPrefix())) {
               var1 = getQNameFromDOMNode(var11);
               var2.setAttr(var1, var11.getValue());
            }
         }
      }

      return var2;
   }

   private static QName getQNameFromDOMNode(Node var0) {
      QName var1;
      if (var0.getPrefix() != null) {
         var1 = new QName(var0.getNamespaceURI(), var0.getLocalName(), var0.getPrefix());
      } else {
         String var2 = var0.getLocalName();
         if (var2 == null) {
            var2 = var0.getNodeName();
         }

         var1 = new QName(var0.getNamespaceURI(), var2);
      }

      return var1;
   }

   private static boolean isNamespaceDecl(String var0) {
      return var0 != null && var0.equals("xmlns");
   }
}
