package weblogic.wsee.jaxws.util;

import com.sun.xml.txw2.TypedXmlWriter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WriterUtil {
   public static final String DEFAULT_NAMESPACE = "http://www.w3.org/2000/xmlns/";

   public static void writeElement(Element var0, XMLStreamWriter var1) {
      writeElement(var0, (String)null, var1);
   }

   public static void writeElement(Element var0, Object var1) {
      writeElement(var0, (String)null, var1);
   }

   public static void writeElement(Element var0, String var1, Object var2) {
      Holder var3 = new Holder(0);
      String var4 = var0.getNamespaceURI();
      String var5 = var0.getLocalName();
      if (var4 == null) {
         var4 = "";
         var5 = var0.getNodeName();
      }

      Object var6;
      if (var2 instanceof TypedXmlWriter) {
         var6 = ((TypedXmlWriter)var2)._element(var4, var5, TypedXmlWriter.class);
      } else {
         var6 = var2;

         try {
            XMLStreamWriter var7 = (XMLStreamWriter)var6;
            if (!isNamespaceBound(var7, var4)) {
               String var8 = selectPrefix(var7, var4, var3);
               var7.writeStartElement(var8, var5, var4);
               var7.writeNamespace(var8, var4);
            } else {
               var7.writeStartElement(var4, var5);
            }
         } catch (XMLStreamException var15) {
            throw new WebServiceException(var15);
         }
      }

      if (var1 != null) {
         writeAttribute(var6, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id", var1, var3);
      }

      boolean var16 = false;
      NamedNodeMap var17 = var0.getAttributes();
      if (var17 != null) {
         for(int var9 = 0; var9 < var17.getLength(); ++var9) {
            Node var10 = var17.item(var9);
            if (var1 == null || !"Id".equals(var10.getLocalName()) || !"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd".equals(var10.getNamespaceURI())) {
               if ("http://www.w3.org/2000/xmlns/".equals(var10.getNamespaceURI())) {
                  writeNamespace(var6, var10.getNodeValue(), var10.getLocalName());
               } else {
                  String var11 = var10.getNamespaceURI();
                  String var12 = var10.getLocalName();
                  if (var11 == null) {
                     var11 = "";
                     var12 = var10.getNodeName();
                  }

                  if (var12.startsWith("xmlns:")) {
                     if (!var16) {
                        var16 = true;
                        String var13 = var0.getPrefix();
                        if (var13 != null && !"".equals(var13)) {
                           writeNamespace(var6, var4, var13);
                        }
                     }

                     writeNamespace(var6, var10.getNodeValue(), var12.substring(6));
                  } else {
                     writeAttribute(var6, var11, var12, var10.getNodeValue(), var3);
                  }
               }
            }
         }
      }

      NodeList var18 = var0.getChildNodes();

      for(int var19 = 0; var19 < var18.getLength(); ++var19) {
         Node var20 = var18.item(var19);
         if (var20.getNodeType() == 1) {
            writeElement((Element)var20, (String)null, var6);
         } else if (var20.getNodeType() == 3) {
            writeCharacters(var6, var20.getNodeValue());
         }
      }

      if (var6 instanceof XMLStreamWriter) {
         try {
            ((XMLStreamWriter)var6).writeEndElement();
         } catch (XMLStreamException var14) {
            throw new WebServiceException(var14);
         }
      }

   }

   private static void ensureNamespace(XMLStreamWriter var0, String var1, Holder<Integer> var2) {
      if (!isNamespaceBound(var0, var1)) {
         String var3 = selectPrefix(var0, var1, var2);

         try {
            var0.writeNamespace(var3, var1);
         } catch (XMLStreamException var5) {
            throw new WebServiceException(var5);
         }
      }

   }

   public static String getBoundNamespace(XMLStreamWriter var0, String var1) {
      NamespaceContext var2 = var0.getNamespaceContext();
      String var3 = var2.getPrefix(var1);
      return var3 != null && var3.length() == 0 ? null : var3;
   }

   public static boolean isNamespaceBound(XMLStreamWriter var0, String var1) {
      NamespaceContext var2 = var0.getNamespaceContext();
      String var3 = var2.getPrefix(var1);
      return var3 != null && var3.length() > 0;
   }

   public static String selectPrefix(XMLStreamWriter var0, String var1, Holder<Integer> var2) {
      return selectPrefix(var0, (String)null, var1, var2);
   }

   public static String selectPrefix(XMLStreamWriter var0, String var1, String var2, Holder<Integer> var3) {
      NamespaceContext var4 = var0.getNamespaceContext();
      if (var1 != null) {
         String var5 = var4.getNamespaceURI(var1);
         if (var5 == null || var5.length() == 0) {
            return var1;
         }
      }

      String var6;
      do {
         int var7 = (Integer)var3.value;
         var1 = "ns" + var7;
         var3.value = var7 + 1;
         var6 = var4.getNamespaceURI(var1);
      } while(var6 != null && var6.length() != 0);

      return var1;
   }

   public static void writeAttribute(Object var0, String var1, String var2, String var3, Holder<Integer> var4) {
      if (var0 instanceof TypedXmlWriter) {
         ((TypedXmlWriter)var0)._attribute(var1, var2, var3);
      } else {
         try {
            XMLStreamWriter var5 = (XMLStreamWriter)var0;
            if (var1 != null && var1.length() != 0) {
               ensureNamespace(var5, var1, var4);
               var5.writeAttribute(var1, var2, var3);
            } else {
               var5.writeAttribute(var2, var3);
            }
         } catch (XMLStreamException var6) {
            throw new WebServiceException(var6);
         }
      }

   }

   public static void writeNamespace(Object var0, String var1, String var2) {
      if (var0 instanceof TypedXmlWriter) {
         ((TypedXmlWriter)var0)._namespace(var1, var2);
      } else {
         try {
            ((XMLStreamWriter)var0).writeNamespace(var2, var1);
         } catch (XMLStreamException var4) {
            throw new WebServiceException(var4);
         }
      }

   }

   private static void writeCharacters(Object var0, String var1) {
      if (var0 instanceof TypedXmlWriter) {
         ((TypedXmlWriter)var0)._pcdata(var1);
      } else {
         try {
            ((XMLStreamWriter)var0).writeCharacters(var1);
         } catch (XMLStreamException var3) {
            throw new WebServiceException(var3);
         }
      }

   }
}
