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

public final class AttributeReference implements Name, Attr, Cloneable {
   private AttributeMap atts;
   private int index;

   public AttributeReference(AttributeMap var1, int var2) {
      this.atts = var1;
      this.index = var2;
   }

   public int getIndex() {
      return this.index;
   }

   public String getName() {
      return this.getNodeName();
   }

   public Element getOwnerElement() {
      return this.atts.getOwner();
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
      String var1 = this.atts.getPrefix(this.index);
      String var2 = this.atts.getLocalName(this.index);
      return var1 != null && !"".equals(var1) ? var1 + ":" + var2 : var2;
   }

   public String getNodeValue() throws DOMException {
      return this.atts.getValue(this.index);
   }

   public void setNodeValue(String var1) throws DOMException {
      this.atts.setValue(this.index, var1);
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
      return new AttributeImpl(this.getOwnerElement(), this.getNamespaceURI(), this.getPrefix(), this.getLocalName(), this.getValue());
   }

   public void normalize() {
   }

   public boolean isSupported(String var1, String var2) {
      return false;
   }

   public String getNamespaceURI() {
      return this.atts.getNamespaceURI(this.index);
   }

   public String getPrefix() {
      return this.atts.getPrefix(this.index);
   }

   public void setPrefix(String var1) throws DOMException {
      this.atts.setPrefix(this.index, var1);
   }

   public String getLocalName() {
      return this.atts.getLocalName(this.index);
   }

   public boolean hasAttributes() {
      return false;
   }

   public String toString() {
      String var1 = this.getPrefix();
      String var2 = this.getLocalName();
      String var3 = this.getNamespaceURI();
      String var4 = this.getValue();
      if (var4 == null) {
         var4 = "";
      }

      StringBuffer var5 = new StringBuffer();
      if (var3 != null) {
         var5.append("['" + var3 + "']");
      }

      if (var1 != null) {
         var5.append(var1 + ":");
      }

      var5.append(var2 + "='" + var4 + "' ");
      return var5.toString();
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
