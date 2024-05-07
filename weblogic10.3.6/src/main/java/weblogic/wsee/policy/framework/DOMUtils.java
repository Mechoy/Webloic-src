package weblogic.wsee.policy.framework;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.utils.StackTraceUtils;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.Base64Encoding;
import weblogic.xml.dom.NamespaceUtils;

public class DOMUtils {
   public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
   public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   public static final String WS_POLICY_SCHEMA = "/weblogic/wsee/policy/schema/ws-policy.xsd";
   private static final boolean verbose = Verbose.isVerbose(DOMUtils.class);
   private static final boolean debug = false;

   public static boolean equalsQName(Node var0, QName var1) {
      return weblogic.xml.dom.DOMUtils.equalsQName(var0, var1);
   }

   public static QName getQNameOf(Node var0) {
      return var0.getNamespaceURI() == null ? new QName(var0.getNodeName()) : new QName(var0.getNamespaceURI(), var0.getLocalName());
   }

   public static String getPathOf(Node var0) {
      if (var0 == null) {
         throw new AssertionError();
      } else {
         StringBuffer var1 = new StringBuffer(getQNameOf(var0).toString());

         for(Node var2 = var0.getParentNode(); var2 != null && var2.getNodeType() == 1; var2 = var2.getParentNode()) {
            var1.insert(0, '/');
            var1.insert(0, getQNameOf(var2).toString());
         }

         return var1.toString();
      }
   }

   public static QName getQNameOf(String var0, Node var1, Map var2) {
      String var3 = var0.trim();
      int var4 = var3.indexOf(58);
      String var5 = null;
      String var6 = var3;
      if (var4 >= 0) {
         if (var4 == 0 || var3.length() - var4 <= 1) {
            throw new IllegalArgumentException("Malformed QName: '" + var0 + "'");
         }

         var5 = var3.substring(0, var4);
         var6 = var3.substring(var4 + 1);
      }

      if (var1.getNodeType() != 1) {
         var1 = var1.getParentNode();
      }

      String var7 = getNamespace(var5, (Element)var1);
      if (var7 == null && var2 != null) {
         var7 = (String)var2.get(var5);
      }

      if (var7 == null) {
         return new QName(var6);
      } else {
         return new QName(var7, var6, var5);
      }
   }

   public static String getAttributeValueAsString(Element var0, QName var1) {
      String var2 = weblogic.xml.dom.DOMUtils.getAttributeValueAsString(var0, var1);
      return var2;
   }

   public static QName getAttributeValueAsQName(Element var0, QName var1) {
      String var2 = getAttributeValueAsString(var0, var1);
      if (var2 != null && var2.length() != 0) {
         int var3 = var2.indexOf(58);
         if (var3 >= 0) {
            String var4 = var2.substring(0, var3);
            String var5 = var2.substring(var3 + 1);
            String var6 = getNamespace(var4, var0);
            return new QName(var6, var5, var4);
         } else {
            return new QName(var2);
         }
      } else {
         return null;
      }
   }

   public static Boolean getAttributeValueAsBoolean(Element var0, QName var1) {
      String var2 = getAttributeValueAsString(var0, var1).trim();
      return var2 != null && var2.length() != 0 ? var2.equalsIgnoreCase("true") || var2.equals("1") : null;
   }

   public static URI getAttributeValueAsURI(Element var0, QName var1) throws URISyntaxException {
      String var2 = getAttributeValueAsString(var0, var1).trim();
      return var2 == null ? null : new URI(var2);
   }

   public static byte[] getAttributeValueAsByteArray(Element var0, QName var1) {
      byte[] var2 = null;
      String var3 = getAttributeValueAsString(var0, var1).trim();
      if (var3 != null && var3.length() > 0) {
         try {
            var2 = (new Base64Encoding()).decode(var3);
         } catch (Throwable var5) {
            throw new NumberFormatException("Could not decode attribute as a base64 encoded byte array: " + StackTraceUtils.throwable2StackTrace(var5));
         }
      }

      return var2;
   }

   public static Map<String, String> getAttributeMap(Node var0) {
      HashMap var1 = new HashMap();
      NamedNodeMap var2 = var0.getAttributes();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            Attr var4 = (Attr)var2.item(var3);
            var1.put(var4.getName(), var4.getValue());
         }
      }

      return var1;
   }

   public static String getTextContent(Element var0, boolean var1) {
      String var2 = weblogic.xml.dom.DOMUtils.getTextContent(var0, var1);
      if (verbose) {
         Verbose.log((Object)("text content of " + var0 + " is " + var2));
      }

      return var2;
   }

   public static QName getQNameContent(Element var0) {
      String var1 = getTextContent(var0, true);
      int var2 = var1.indexOf(58);
      if (var2 >= 0) {
         String var3 = var1.substring(0, var2);
         String var4 = var1.substring(var2 + 1);
         String var5 = getNamespace(var3, var0);
         return new QName(var5, var4, var3);
      } else {
         return new QName(var1);
      }
   }

   public static String getNamespace(String var0, Element var1) {
      String var2 = null;
      String var3 = var0 == null ? "xmlns" : "xmlns:" + var0;

      do {
         var2 = var1.getAttribute(var3);
         if (var2 != null && !var2.equals("")) {
            return var2;
         }

         Node var4 = var1.getParentNode();
         if (var4.getNodeType() != 1) {
            break;
         }

         var1 = (Element)var4;
      } while(var1 != null);

      return null;
   }

   public static Map<String, String> getNamespaceMapping(Node var0) {
      Object var1;
      for(var1 = new HashMap(); var0 != null; var0 = var0.getParentNode()) {
         var1 = getNodeNamespaceMapping(var0, (Map)var1);
      }

      return (Map)var1;
   }

   public static Map<String, String> getNodeNamespaceMapping(Node var0, Map<String, String> var1) {
      if (var1 == null) {
         var1 = new HashMap();
      }

      if (var0 != null) {
         NamedNodeMap var2 = var0.getAttributes();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.getLength(); ++var3) {
               Attr var4 = (Attr)var2.item(var3);
               String var5 = var4.getName();
               String var6 = null;
               if (var5.startsWith("xmlns:")) {
                  var6 = var5.substring(6);
               } else if (var5.equals("xmlns")) {
                  var6 = "";
               }

               if (var6 != null && !((Map)var1).containsKey(var6)) {
                  ((Map)var1).put(var6, var4.getValue());
               }
            }
         }
      }

      return (Map)var1;
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
                  return var5.substring(7);
               }
            }
         }

         var1 = (Element)var1.getParentNode();
      } while(var1 != null);

      return null;
   }

   public static Element createElement(QName var0, Document var1) {
      Element var2 = null;
      String var3 = var0.getNamespaceURI();
      if (var3.length() > 0) {
         var2 = var1.createElementNS(var3, var0.getLocalPart());
      } else {
         var2 = var1.createElement(var0.getLocalPart());
      }

      return var2;
   }

   public static Element createElement(QName var0, Document var1, String var2) {
      Element var3 = null;
      String var4 = var0.getNamespaceURI();
      if (var4.length() > 0) {
         String var5 = var0.getLocalPart();
         if (!StringUtil.isEmpty(var2)) {
            var5 = var2 + ":" + var5;
         }

         var3 = var1.createElementNS(var4, var5);
      } else {
         var3 = var1.createElement(var0.getLocalPart());
      }

      return var3;
   }

   public static void addAttribute(Element var0, QName var1, String var2) {
      String var3 = var1.getNamespaceURI();
      if (var3.length() > 0) {
         var0.setAttributeNS(var3, var1.getLocalPart(), var2);
      } else {
         var0.setAttribute(var1.getLocalPart(), var2);
      }

   }

   public static void addPrefixedAttribute(Element var0, QName var1, String var2, String var3) {
      var0.setAttributeNS(var1.getNamespaceURI(), var2 + ":" + var1.getLocalPart(), var3);
   }

   public static void addAttribute(Element var0, QName var1, QName var2) {
      String var3 = var2.getNamespaceURI();
      if (var3 != null && var3.length() > 0) {
         String var4 = getPrefix(var3, var0);
         if (var4 == null) {
            var4 = var2.getLocalPart() + "-ns";
            NamespaceUtils.defineNamespace(var0, var4, var3);
         }

         addAttribute(var0, var1, var4 + ":" + var2.getLocalPart());
      } else {
         addAttribute(var0, var1, var2.toString());
      }

   }

   public static DocumentBuilder getParser() {
      try {
         DocumentBuilderFactory var0 = DocumentBuilderFactory.newInstance();
         var0.setNamespaceAware(true);
         return var0.newDocumentBuilder();
      } catch (FactoryConfigurationError var1) {
         throw new AssertionError(var1);
      } catch (ParserConfigurationException var2) {
         throw new AssertionError(var2);
      }
   }

   public static void xml2Stream(Node var0, Writer var1) throws IOException {
      Transformer var2 = null;

      try {
         var2 = TransformerFactory.newInstance().newTransformer();
      } catch (TransformerConfigurationException var4) {
         throw new AssertionError(var4);
      } catch (TransformerFactoryConfigurationError var5) {
         throw new AssertionError(var5);
      }

      try {
         var2.transform(new DOMSource(var0), new StreamResult(var1));
      } catch (TransformerException var6) {
         if (verbose) {
            Verbose.log((Object)var6);
         }
      }

      var1.flush();
   }

   public static String toXMLString(Node var0) {
      CharArrayWriter var1 = new CharArrayWriter();

      try {
         xml2Stream(var0, var1);
      } catch (IOException var3) {
         if (verbose) {
            Verbose.log((Object)var3);
         }
      }

      return var1.toString();
   }

   public static List computeContent(List var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         ArrayList var4 = new ArrayList();
         NodeList var5 = var3.getChildNodes();

         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            Node var7 = var5.item(var6);
            var4.add(var7);
         }

         if (var4.size() > 0) {
            var1.add(newNodeListFromArrayOfNodes(var4));
         }
      }

      return var1;
   }

   private static NodeList newNodeListFromArrayOfNodes(final List var0) {
      return new NodeList() {
         public Node item(int var1) {
            return (Node)var0.get(var1);
         }

         public int getLength() {
            return var0.size();
         }
      };
   }

   public static Node toXMLNode(String var0) {
      if (StringUtil.isEmpty(var0)) {
         return null;
      } else {
         Transformer var1 = null;

         try {
            var1 = TransformerFactory.newInstance().newTransformer();
         } catch (TransformerConfigurationException var3) {
            throw new AssertionError(var3);
         } catch (TransformerFactoryConfigurationError var4) {
            throw new AssertionError(var4);
         }

         try {
            DOMResult var2 = new DOMResult();
            var1.transform(new StreamSource(new StringReader(var0)), var2);
            return var2.getNode();
         } catch (TransformerException var5) {
            if (verbose) {
               Verbose.log((Object)var5);
            }

            throw new AssertionError(var5);
         }
      }
   }
}
