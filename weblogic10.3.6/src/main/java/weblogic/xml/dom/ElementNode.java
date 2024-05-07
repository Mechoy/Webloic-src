package weblogic.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

public class ElementNode extends NodeImpl implements Element {
   private String tagName;
   private String namespaceURI;
   private String localName;
   private String prefix;
   private AttributeMap attributes;
   public static String XMLNS = "xmlns";
   public static String XMLNS_URI = "http://www.w3.org/2000/xmlns/";

   public ElementNode() {
      this.setNodeType((short)1);
   }

   public ElementNode(int var1) {
      this();
      this.attributes = new AttributeMap(var1);
   }

   public ElementNode(String var1, String var2, String var3) {
      this();
      this.namespaceURI = var1;
      this.localName = var2;
      this.prefix = var3;
   }

   public void setAttributes(AttributeMap var1) {
      this.attributes = var1;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public void setNamespaceURI(String var1) {
      this.namespaceURI = var1;
   }

   public void setLocalName(String var1) {
      this.localName = var1;
   }

   public String getLocalName() {
      return this.localName;
   }

   public void setPrefix(String var1) {
      this.prefix = var1;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getNodeName() {
      return this.prefix != null ? this.prefix + ":" + this.localName : this.localName;
   }

   public AttributeMap getAttributeMap() {
      return this.attributes;
   }

   public NamedNodeMap getAttributes() {
      return (NamedNodeMap)(this.attributes == null ? Util.NAMED_NODE_MAP : this.attributes);
   }

   public String getAttribute(String var1) {
      return this.attributes == null ? null : this.attributes.getValue(var1);
   }

   public Attr getAttributeNode(String var1) {
      if (this.attributes == null) {
         return null;
      } else {
         int var2 = this.attributes.getAttributeIndex(var1);
         return var2 == -1 ? null : this.attributes.getAttribute(var2);
      }
   }

   public String getNamespaceURI(String var1) {
      if (this.attributes != null) {
         int var2 = this.attributes.getAttributeIndex(XMLNS_URI, var1);
         if (var2 != -1) {
            return this.attributes.getValue(var2);
         }
      }

      Node var4 = this.getParentNode();
      if (var4 == null) {
         return null;
      } else if (var4.getNodeType() != 1) {
         return null;
      } else {
         ElementNode var3 = (ElementNode)var4;
         return var3.getNamespaceURI(var1);
      }
   }

   public String getPrefix(String var1) {
      if (this.attributes != null) {
         for(int var2 = 0; var2 < this.attributes.length(); ++var2) {
            if (var1.equals(this.attributes.getValue(var2)) && XMLNS_URI.equals(this.attributes.getNamespaceURI(var2))) {
               return this.attributes.getPrefix(var2);
            }
         }
      }

      Node var4 = this.getParentNode();
      if (var4 == null) {
         return null;
      } else if (var4.getNodeType() != 1) {
         return null;
      } else {
         ElementNode var3 = (ElementNode)var4;
         return var3.getPrefix(var1);
      }
   }

   public String getDefaultNamespaceURI() {
      if (this.attributes != null) {
         int var1 = this.attributes.getAttributeIndex(XMLNS);
         if (var1 != -1) {
            return this.attributes.getValue(var1);
         }
      }

      Node var3 = this.getParentNode();
      if (var3 == null) {
         return null;
      } else if (var3.getNodeType() != 1) {
         return null;
      } else {
         ElementNode var2 = (ElementNode)var3;
         return var2.getDefaultNamespaceURI();
      }
   }

   public int setNamespaceURI(String var1, String var2) {
      int var3 = this.attributes.getAttributeIndex(XMLNS_URI, var1);
      if (var3 == -1) {
         return this.attributes.addAttribute(XMLNS_URI, var1, XMLNS, var2);
      } else {
         this.attributes.setAttribute(var3, XMLNS_URI, var1, XMLNS, var2);
         return var3;
      }
   }

   public Attr setNamespaceURI(Attr var1) {
      int var2 = this.setNamespaceURI(var1.getLocalName(), var1.getValue());
      return this.attributes.getAttribute(var2);
   }

   public Attr getAttributeNodeNS(String var1, String var2) {
      if (this.attributes == null) {
         return null;
      } else {
         int var3 = this.attributes.getAttributeIndex(var1, var2);
         return var3 == -1 ? null : this.attributes.getAttribute(var3);
      }
   }

   public String getAttributeNS(String var1, String var2) {
      return this.attributes == null ? null : this.attributes.getValue(var1, var2);
   }

   public String getTagName() {
      return this.localName;
   }

   public boolean hasAttribute(String var1) {
      if (this.attributes == null) {
         return false;
      } else {
         return this.attributes.getAttributeIndex(var1) != -1;
      }
   }

   public boolean hasAttributeNS(String var1, String var2) {
      if (this.attributes == null) {
         return false;
      } else {
         return this.attributes.getAttributeIndex(var1, var2) != -1;
      }
   }

   public void removeAttribute(String var1) {
      if (this.attributes != null) {
         this.attributes.removeAttribute(this.attributes.getAttributeIndex(var1));
      }
   }

   public Attr removeAttributeNode(Attr var1) {
      if (this.attributes == null) {
         throw new DOMException((short)8, "The attribute provided is not a child of this Element");
      } else {
         Attr var2 = (Attr)var1.cloneNode(false);
         AttributeReference var3 = (AttributeReference)var1;
         if (var3.getOwnerElement() != this) {
            throw new DOMException((short)8, "The attribute provided is not a child of this Element");
         } else {
            int var4 = var3.getIndex();
            this.attributes.removeAttribute(var4);
            return var2;
         }
      }
   }

   public void removeAttributeNS(String var1, String var2) {
      if (this.attributes != null) {
         int var3 = this.attributes.getAttributeIndex(var1, var2);
         if (var3 != -1) {
            this.attributes.removeAttribute(var3);
         }
      }
   }

   public void setAttribute(String var1, String var2) {
      if (this.attributes == null) {
         this.attributes = new AttributeMap();
      }

      int var3 = this.attributes.getAttributeIndex(this.localName);
      if (var3 == -1) {
         this.attributes.addAttribute((String)null, var1, (String)null, var2);
      } else {
         this.attributes.setValue(var3, var2);
      }

   }

   public Attr setAttributeNode(Attr var1) {
      if (this.attributes == null) {
         this.attributes = new AttributeMap();
      }

      if (XMLNS.equals(var1.getPrefix())) {
         this.setNamespaceURI(var1);
      }

      int var2 = this.attributes.getAttributeIndex(var1.getNamespaceURI(), var1.getLocalName());
      if (var2 == -1) {
         int var3 = this.attributes.addAttribute(var1.getNamespaceURI(), var1.getLocalName(), var1.getPrefix(), var1.getValue());
         return this.attributes.getAttribute(var3);
      } else {
         this.attributes.setAttribute(var2, var1.getNamespaceURI(), var1.getLocalName(), var1.getPrefix(), var1.getValue());
         return this.attributes.getAttribute(var2);
      }
   }

   public Attr setAttributeNodeNS(Attr var1) {
      return this.setAttributeNode(var1);
   }

   public boolean hasAttributes() {
      if (this.attributes == null) {
         return false;
      } else {
         return this.attributes.getLength() > 0;
      }
   }

   public void setAttributeNS(String var1, String var2, String var3, String var4) {
      if (this.attributes == null) {
         this.attributes = new AttributeMap();
      }

      if (XMLNS.equals(var2)) {
         this.setNamespaceURI(var3, var4);
      } else {
         int var5 = this.attributes.getAttributeIndex(var1, var3);
         if (var5 == -1) {
            this.attributes.addAttribute(var1, var3, var2, var4);
         } else {
            this.attributes.setPrefix(var5, var2);
            this.attributes.setValue(var5, var4);
         }

      }
   }

   public void setAttributeNS(String var1, String var2, String var3) {
      String var4 = Util.getPrefix(var2);
      String var5 = Util.getLocalName(var2);
      this.setAttributeNS(var1, var4, var5, var3);
   }

   public void printName(StringBuffer var1) {
      String var2 = this.getNamespaceURI();
      String var3 = this.getPrefix();
      if (var2 != null && !"".equals(var2)) {
         var1.append("['" + var2 + "']:");
      }

      if (this.prefix != null) {
         var1.append(var3 + ":");
      }

      var1.append(this.getLocalName());
   }

   public void print(StringBuffer var1, int var2) {
      var1.append("<");
      this.printName(var1);
      if (this.attributes != null) {
         var1.append(this.attributes.toString());
      }

      if (!this.hasChildNodes()) {
         var1.append("/>");
      } else {
         var1.append(">");
         NodeList var3 = this.getChildNodes();

         for(int var4 = 0; var4 < var3.getLength(); ++var4) {
            NodeImpl var5 = (NodeImpl)var3.item(var4);
            var5.print(var1, var2 + 1);
         }

         var1.append("</");
         this.printName(var1);
         var1.append(">");
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      this.print(var1, 0);
      return var1.toString();
   }

   public ElementNode read(XMLStreamReader var1) throws IOException {
      try {
         return Builder.read(this, var1);
      } catch (Exception var3) {
         throw new IOException(var3.getMessage());
      }
   }

   public ElementNode read(InputStream var1) throws IOException {
      try {
         return this.read(XMLInputFactory.newInstance().createXMLStreamReader(var1));
      } catch (Exception var3) {
         throw new IOException(var3.getMessage());
      }
   }

   public static void main(String[] var0) throws Exception {
      ElementNode var1 = new ElementNode();
      var1.setLocalName("test_node");
      var1.setAttribute("a", "apple");
      var1.setAttribute("b", "banana");
      var1.setAttribute("c", "cherry");
      System.out.println("Node at Start=" + var1);
      var1.getAttributeNode("a").setValue("apples");
      var1.getAttributeNode("b").setValue("bananas");
      var1.getAttributeNode("c").setValue("cherrys");
      System.out.println("Node after mod=" + var1);
      var1.removeAttribute("a");
      System.out.println("Node after removal=" + var1);
      System.out.println("isnull=" + var1.getAttributeNode("a"));
      Attr var2 = var1.setAttributeNode(new AttributeImpl(var1, (String)null, (String)null, "d", "donut"));
      System.out.println("Node after mod=" + var1);
      var2.setValue("donuts");
      var2.setPrefix("d");
      System.out.println("Node after mod=" + var1);
      NamedNodeMap var3 = var1.getAttributes();
      AttributeImpl var4 = new AttributeImpl(var1, (String)null, (String)null, "e", "elephant");
      Attr var7 = (Attr)var3.setNamedItem(var4);
      System.out.println("Node after att map mod=" + var1);
      var7.setValue("elephants");
      System.out.println("Node after att map mod=" + var1);
      ElementNode var5 = new ElementNode();
      var5.setLocalName("n1");
      var5.setAttributeNS((String)null, "xmlns:a", "http://a");
      var5.setAttributeNS((String)null, "xmlns:b", "http://b");
      var5.setAttributeNS((String)null, "xmlns", "http://default");
      var5.setAttributeNS("http://b", "b:banana", "food");
      var5.setAttributeNS("http://a", "a:apple", "fruit");
      ElementNode var6 = new ElementNode();
      var6.setLocalName("n2");
      var5.appendChild(var6);
      System.out.println(var5);
      System.out.println("parent:" + var6.getNamespaceURI("b"));
      System.out.println("default:" + var6.getDefaultNamespaceURI());
   }

   public void setIdAttributeNode(Attr var1, boolean var2) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public void setIdAttributeNS(String var1, String var2, boolean var3) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public void setIdAttribute(String var1, boolean var2) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public TypeInfo getSchemaTypeInfo() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }
}
