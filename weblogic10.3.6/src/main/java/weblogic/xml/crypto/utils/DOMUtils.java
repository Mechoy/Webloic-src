package weblogic.xml.crypto.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import weblogic.rjvm.LocalRJVM;
import weblogic.xml.dom.DOMStreamReader;
import weblogic.xml.dom.DOMStreamWriter;
import weblogic.xml.dom.Util;
import weblogic.xml.domimpl.Saver;
import weblogic.xml.stax.XMLStreamInputFactory;
import weblogic.xml.stax.XMLWriterBase;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;

public class DOMUtils {
   public static final String QNAME_SEPARATOR = ":";
   private static final String ID_DELIM = "_";
   public static final XMLStreamInputFactory STREAM_FAC = new XMLStreamInputFactory();
   public static final String EMPTY_STRING = "";
   private static final byte[] ID_CHARS = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 50, 49};

   public static Map getNamespaceMap(Node var0) {
      HashMap var1 = new HashMap();
      ArrayList var2 = new ArrayList();
      if (var0 instanceof Document) {
         var0 = ((Document)var0).getDocumentElement();
      }

      while(var0 != null) {
         var2.add(var0);
         var0 = ((Node)var0).getParentNode();
      }

      for(int var3 = var2.size() - 1; var3 >= 0; --var3) {
         Node var4 = (Node)var2.get(var3);
         NamedNodeMap var5 = var4.getAttributes();
         if (var5 != null) {
            for(int var6 = 0; var6 < var5.getLength(); ++var6) {
               Attr var7 = (Attr)var5.item(var6);
               String var8 = var7.getNamespaceURI();
               if (var8 != null && var8.equals("http://www.w3.org/2000/xmlns/")) {
                  String var9 = var7.getLocalName();
                  if ("xmlns".equals(var9)) {
                     var9 = "";
                  }

                  String var10 = var7.getValue();
                  var1.put(var9, var10);
               }
            }
         }
      }

      return var1;
   }

   public static boolean isIdQName(Node var0, Set var1) {
      String var2 = var0.getLocalName();
      if (var2 == null) {
         var2 = var0.getNodeName();
      }

      String var3 = var0.getNamespaceURI();
      QName var4 = new QName(var3, var2);
      return var1.contains(var4);
   }

   public static List getNodeListAndIdAttrNodeMap(Node var0, HashMap var1, Set var2, boolean var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("The second parameter must not NULL");
      } else {
         ArrayList var4 = new ArrayList();
         traverseNodeAndIdAttrNodeMap(var0, var4, var1, var2, var3);
         return var4;
      }
   }

   private static void traverseNodeAndIdAttrNodeMap(Node var0, List var1, HashMap var2, Set var3, boolean var4) {
      if (var4 || var0.getNodeType() != 8) {
         int[] var5 = new int[]{var1.size(), 0};
         var1.add(var0);
         int var7;
         if (var0.getNodeType() == 1) {
            NamedNodeMap var6 = var0.getAttributes();

            for(var7 = 0; var7 < var6.getLength(); ++var7) {
               var1.add(var6.item(var7));
               if (isIdQName(var6.item(var7), var3)) {
                  String var8 = var6.item(var7).getNodeValue();
                  var2.put(var8, var5);
               }
            }
         }

         if (var0.hasChildNodes()) {
            NodeList var9 = var0.getChildNodes();

            for(var7 = 0; var7 < var9.getLength(); ++var7) {
               traverseNodeAndIdAttrNodeMap(var9.item(var7), var1, var2, var3, var4);
            }
         }

         var5[1] = var1.size();
      }
   }

   public static Set getNodeSet(Node var0, boolean var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      traverseNode(var0, var2, var1);
      return var2;
   }

   private static void traverseNode(Node var0, Set var1, boolean var2) {
      if (var2 || var0.getNodeType() != 8) {
         var1.add(var0);
         int var4;
         if (var0.getNodeType() == 1) {
            NamedNodeMap var3 = var0.getAttributes();

            for(var4 = 0; var4 < var3.getLength(); ++var4) {
               var1.add(var3.item(var4));
            }
         }

         if (var0.hasChildNodes()) {
            NodeList var5 = var0.getChildNodes();

            for(var4 = 0; var4 < var5.getLength(); ++var4) {
               traverseNode(var5.item(var4), var1, var2);
            }
         }

      }
   }

   public static QName getQName(Node var0) {
      String var1 = var0.getLocalName();
      if (var1 == null) {
         return new QName(var0.getNodeName());
      } else {
         String var2 = var0.getPrefix();
         return var2 == null ? new QName(var0.getNamespaceURI(), var1) : new QName(var0.getNamespaceURI(), var1, var2);
      }
   }

   public static Element createElement(Element var0, QName var1, String var2) {
      var2 = getPrefix(var2);
      Element var3 = var0.getOwnerDocument().createElementNS(var1.getNamespaceURI(), var2 + var1.getLocalPart());
      return var3;
   }

   public static Element createAndAddElement(Element var0, QName var1, String var2) {
      Element var3 = createElement(var0, var1, var2);
      var0.appendChild(var3);
      return var3;
   }

   public static void addNoNSAttribute(Element var0, QName var1, String var2) {
      var0.setAttribute(var1.getLocalPart(), var2);
   }

   public static void addAttribute(Element var0, QName var1, Map var2, String var3) {
      String var4 = (String)var2.get(var1.getNamespaceURI());
      var4 = getPrefix(var4);
      var0.setAttributeNS(var1.getNamespaceURI(), var1.getLocalPart(), var3);
   }

   public static void addAttribute(Element var0, QName var1, String var2) {
      var0.setAttributeNS(var1.getNamespaceURI(), var1.getLocalPart(), var2);
   }

   public static void addPrefixedAttribute(Element var0, QName var1, String var2, String var3) {
      var2 = getPrefix(var2);
      var0.setAttributeNS(var1.getNamespaceURI(), var2 + var1.getLocalPart(), var3);
   }

   private static String getPrefix(String var0) {
      return var0 != null && var0.length() > 0 ? var0 + ":" : "";
   }

   public static void addText(Element var0, String var1) {
      Text var2 = var0.getOwnerDocument().createTextNode(var1);
      var0.appendChild(var2);
   }

   public static String getAttributeValue(Element var0, QName var1, String var2) {
      String var3 = getAttributeValue(var0, var1);
      return var3 != null && var3.length() != 0 ? var3 : var2;
   }

   public static String getAttributeValue(Element var0, QName var1) {
      String var2 = var1.getLocalPart();
      Attr var3 = var0.getAttributeNodeNS(var1.getNamespaceURI(), var2);
      if (var3 == null) {
         var3 = var0.getAttributeNode(var2);
      }

      return var3 != null ? var3.getValue() : null;
   }

   public static void require(Node var0, QName var1) throws DOMException {
      QName var2 = getQName(var0);
      if (!var2.equals(var1)) {
         throw new DOMException((short)11, "Node " + var1 + " required, found " + var2);
      }
   }

   public static boolean is(Node var0, String var1, String var2) {
      if (var0 != null && var2 != null) {
         if (!var2.equals(var0.getLocalName())) {
            return false;
         } else if (var1 != null) {
            return var1.equals(var0.getNamespaceURI());
         } else {
            return var0.getNamespaceURI() == null;
         }
      } else {
         return false;
      }
   }

   public static boolean is(Node var0, QName var1) {
      if (var0 != null && var1 != null) {
         QName var2 = getQName(var0);
         return var1.equals(var2);
      } else {
         return false;
      }
   }

   public static String getText(Element var0) {
      return var0.getFirstChild().getNodeValue();
   }

   public static Element getFirstElement(Node var0) {
      Node var1;
      for(var1 = var0.getFirstChild(); var1 != null && var1.getNodeType() != 1; var1 = var1.getNextSibling()) {
      }

      return (Element)var1;
   }

   public static Element getLastElement(Node var0) {
      Node var1;
      for(var1 = var0.getLastChild(); var1 != null && var1.getNodeType() != 1; var1 = var1.getPreviousSibling()) {
      }

      return (Element)var1;
   }

   public static Element getNextElement(Node var0) {
      Node var1;
      for(var1 = var0.getNextSibling(); var1 != null && var1.getNodeType() != 1; var1 = var1.getNextSibling()) {
      }

      return (Element)var1;
   }

   public static Map getNSMap(Node var0) {
      HashMap var1 = new HashMap();

      ArrayList var2;
      for(var2 = new ArrayList(); var0 != null; var0 = var0.getParentNode()) {
         var2.add(var0);
      }

      for(int var3 = var2.size() - 1; var3 >= 0; --var3) {
         Node var4 = (Node)var2.get(var3);
         NamedNodeMap var5 = var4.getAttributes();
         if (var5 != null) {
            for(int var6 = 0; var6 < var5.getLength(); ++var6) {
               Attr var7 = (Attr)var5.item(var6);
               String var8 = var7.getNamespaceURI();
               if (var8 != null && var8.equals("http://www.w3.org/2000/xmlns/")) {
                  String var9 = var7.getLocalName();
                  if ("xmlns".equals(var9)) {
                     var9 = "";
                  }

                  String var10 = var7.getValue();
                  var1.put(var9, var10);
                  var1.put(var10, var9);
               }
            }
         }
      }

      return var1;
   }

   public static String assignId(Element var0, QName var1, String var2, Set var3) {
      String var4 = getExistingId(var0, var3);
      if (var4 == null || var4.length() == 0) {
         var4 = generateId(var0.getLocalName());
         addPrefixedAttribute(var0, var1, var2, var4);
      }

      return var4;
   }

   public static String getExistingId(Element var0, Set var1) {
      String var2 = null;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         QName var4 = (QName)var3.next();
         String var5 = var4.getNamespaceURI();
         if ("".equals(var5)) {
            var5 = null;
         }

         var2 = var0.getAttributeNS(var5, var4.getLocalPart());
         if (var2 != null && var2.length() > 0) {
            break;
         }
      }

      return var2;
   }

   public static String generateId(String var0) {
      return var0 + "_" + generateId();
   }

   public static String generateId() {
      byte var0 = 16;
      byte[] var1 = new byte[var0];

      for(int var2 = 0; var2 < var0; ++var2) {
         byte[] var3 = new byte[1];
         LocalRJVM.getLocalRJVM().getSecureRandom().nextBytes(var3);
         var1[var2] = ID_CHARS[(var3[0] + 128) % 64];
      }

      return new String(var1);
   }

   public static void declareNamespace(Element var0, String var1, String var2) {
      var0.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var2, var1);
   }

   public static void declareNamespace(Element var0, String var1, String var2, Map var3) {
      String var4 = (String)var3.get(var2);
      if (var4 == null || !var4.equals(var1)) {
         declareNamespace(var0, var1, var2);
      }

   }

   public static void main(String[] var0) throws Exception {
      DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
      var1.setNamespaceAware(true);
      Document var2 = var1.newDocumentBuilder().parse(new InputSource(new StringReader("<foo1 xmlns:foo='foo1'><foo2 xmlns:foo='foo2'><bar id='bar'/></foo2></foo1>")));
      System.out.println("document: " + Util.printNode(var2));
      Node var3 = var2.getElementsByTagName("bar").item(0);
      if (var3 != null) {
         System.out.println("node: " + Util.printNode(var3));
      }

      Map var4 = getNamespaceMap(var3);
      System.out.println(var4);
      var4.clear();
      var2 = var1.newDocumentBuilder().parse(new InputSource(new StringReader("<foo1 xmlns:foo1='foo'><foo2 xmlns:foo2='foo'><bar id='bar'/></foo2></foo1>")));
      System.out.println("document: " + Util.printNode(var2));
      var3 = var2.getElementsByTagName("bar").item(0);
      if (var3 != null) {
         System.out.println(Util.printNode(var3));
      }

      var4 = getNamespaceMap(var3);
      System.out.println(var4);
   }

   public static void writeNode(Node var0, OutputStream var1, String var2) throws UnsupportedEncodingException, XMLStreamException {
      XMLWriterBase var3 = createStreamWriter(var1, var2);
      Saver.save(var3, var0);
      var3.flush();
   }

   private static XMLWriterBase createStreamWriter(OutputStream var0, String var1) throws UnsupportedEncodingException {
      OutputStreamWriter var2 = new OutputStreamWriter(var0, var1);
      XMLWriterBase var3 = new XMLWriterBase(var2);
      return var3;
   }

   private static void writeNode(Node var0, XMLWriterBase var1) throws XMLStreamException {
      DOMStreamReader var2 = new DOMStreamReader(var0);
      writeAll(var2, var1);
   }

   private static void writeAll(XMLStreamReader var0, XMLWriterBase var1) throws XMLStreamException {
      var1.write(var0);

      while(var0.hasNext()) {
         var0.next();
         var1.write(var0);
      }

   }

   public static void writeNodeList(NodeList var0, OutputStream var1, String var2) throws XMLStreamException, UnsupportedEncodingException {
      int var3 = var0.getLength();
      XMLWriterBase var4 = createStreamWriter(var1, var2);
      var4.setEscapingCR(true);

      for(int var5 = 0; var5 < var3; ++var5) {
         Node var6 = var0.item(var5);
         writeNode(var6, var4);
      }

      var4.flush();
   }

   public static byte[] getBytes(Node var0, String var1) throws XMLStreamException, UnsupportedEncodingException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      writeNode(var0, var2, var1);
      return var2.toByteArray();
   }

   public static byte[] getBytes(NodeList var0, String var1) throws XMLStreamException, UnsupportedEncodingException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      writeNodeList(var0, var2, var1);
      return var2.toByteArray();
   }

   public static void replace(Node var0, InputStream var1, String var2) throws XMLStreamException {
      Node var3 = var0.getParentNode();
      Node var4 = var0.getNextSibling();
      var3.removeChild(var0);
      insertChild(var3, var4, var1, var2);
   }

   public static void insertChild(Node var0, Node var1, InputStream var2, String var3) throws XMLStreamException {
      Map var4 = getNSMap(var0);
      XMLStreamReader var5 = STREAM_FAC.createXMLStreamFragmentReader(var2, var3, var4);
      insertChild(var5, var0, var1);
   }

   public static void insertChild(XMLStreamReader var0, Node var1, Node var2) throws XMLStreamException {
      Document var3 = var1.getOwnerDocument();
      DOMStreamWriter var4;
      if (var2 != null) {
         var4 = new DOMStreamWriter(var3, var1, var2);
      } else {
         var4 = new DOMStreamWriter(var3, var1);
      }

      var4.writeAll(var0);
   }

   public static String getPrefix(String var0, Element var1) {
      assert var0 != null;

      assert var1 != null;

      do {
         NamedNodeMap var2 = var1.getAttributes();

         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            Node var4 = var2.item(var3);
            String var5 = var4.getNodeName();
            if (var5.startsWith("xmlns:")) {
               String var6 = var4.getNodeValue();
               if (var0.equals(var6)) {
                  return var5.substring(6);
               }
            }
         }

         var1 = (Element)var1.getParentNode();
      } while(var1 != null);

      return null;
   }

   public static Node findNode(Node var0, String var1) {
      NodeList var2 = var0.getChildNodes();

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         Node var4 = var2.item(var3);
         String var5 = var4.getLocalName();
         if (var5 != null && var5.equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   public static XMLInputStream getXMLInputStream(Node var0) throws weblogic.xml.stream.XMLStreamException {
      return XMLInputStreamFactory.newInstance().newInputStream(var0);
   }
}
