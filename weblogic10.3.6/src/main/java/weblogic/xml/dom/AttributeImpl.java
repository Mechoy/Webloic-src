package weblogic.xml.dom;

import javax.xml.soap.Name;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public final class AttributeImpl implements Name, Attr, Cloneable {
   private String uri;
   private String prefix;
   private String value;
   private String localName;
   private Element owner;

   public AttributeImpl() {
   }

   public AttributeImpl(Element var1, String var2, String var3, String var4, String var5) {
      this.owner = var1;
      this.uri = var2;
      this.prefix = var3;
      this.localName = var4;
      this.value = var5;
   }

   public String getName() {
      return this.getNodeName();
   }

   public Element getOwnerElement() {
      return this.owner;
   }

   public boolean getSpecified() {
      return true;
   }

   public String getValue() {
      return this.getNodeValue();
   }

   public void setValue(String var1) {
      this.setNodeValue(var1);
   }

   public String getURI() {
      return this.getNamespaceURI();
   }

   public String getQualifiedName() {
      return this.getPrefix() == null ? this.getLocalName() : this.getPrefix() + ":" + this.getLocalName();
   }

   public String getNodeName() {
      return this.prefix != null && !"".equals(this.prefix) ? this.prefix + ":" + this.localName : this.localName;
   }

   public String getNodeValue() throws DOMException {
      return this.value;
   }

   public void setNodeValue(String var1) throws DOMException {
      this.value = var1;
   }

   public short getNodeType() {
      return 2;
   }

   public Node getParentNode() {
      return null;
   }

   public NodeList getChildNodes() {
      return Util.EMPTY_NODELIST;
   }

   public Node getFirstChild() {
      return null;
   }

   public Node getLastChild() {
      return null;
   }

   public Node getPreviousSibling() {
      return null;
   }

   public Node getNextSibling() {
      return null;
   }

   public NamedNodeMap getAttributes() {
      return null;
   }

   public Document getOwnerDocument() {
      return null;
   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      return null;
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      return null;
   }

   public Node removeChild(Node var1) throws DOMException {
      return null;
   }

   public Node appendChild(Node var1) throws DOMException {
      return null;
   }

   public boolean hasChildNodes() {
      return false;
   }

   public void removeChildren() {
   }

   public Node cloneNode(boolean var1) {
      try {
         return (Node)this.clone();
      } catch (CloneNotSupportedException var3) {
         return null;
      }
   }

   public void normalize() {
   }

   public boolean isSupported(String var1, String var2) {
      return false;
   }

   public String getNamespaceURI() {
      return this.uri;
   }

   public void setNamespaceURI(String var1) {
      this.uri = var1;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String var1) throws DOMException {
      this.prefix = var1;
   }

   public String getLocalName() {
      return this.localName;
   }

   public void setLocalName(String var1) {
      this.localName = var1;
   }

   public boolean hasAttributes() {
      return false;
   }

   public String toString() {
      if (this.value == null) {
         this.value = "";
      }

      StringBuffer var1 = new StringBuffer();
      if (this.uri != null) {
         var1.append("['" + this.uri + "']");
      }

      if (this.prefix != null) {
         var1.append(this.prefix + ":");
      }

      var1.append(this.localName + "='" + this.value + "' ");
      return var1.toString();
   }

   public boolean isId() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public TypeInfo getSchemaTypeInfo() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public Object getUserData(String var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public Object setUserData(String var1, Object var2, UserDataHandler var3) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public Object getFeature(String var1, String var2) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public boolean isEqualNode(Node var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String lookupNamespaceURI(String var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public boolean isDefaultNamespace(String var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String lookupPrefix(String var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public boolean isSameNode(Node var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public void setTextContent(String var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String getTextContent() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public short compareDocumentPosition(Node var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String getBaseURI() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }
}
