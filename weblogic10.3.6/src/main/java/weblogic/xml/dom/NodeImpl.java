package weblogic.xml.dom;

import java.util.Iterator;
import javax.xml.soap.Name;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import weblogic.xml.stax.ReaderToWriter;

public class NodeImpl implements Node, NodeList, Cloneable {
   private Document ownerDocument;
   private NodeImpl parent;
   private NodeImpl firstChild;
   private NodeImpl lastChild;
   private NodeImpl previousSibling;
   private NodeImpl nextSibling;
   private int numChildren = 0;
   private short nodeType;
   private String nodeName;
   private String nodeValue;
   private String localName;
   private transient NodeImpl currentNode;
   private transient int currentNodeCount;
   private static final boolean debug = false;

   public NodeImpl() {
      this.currentNode = this.firstChild;
      this.currentNodeCount = 0;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public void setNodeName(String var1) {
      this.nodeName = var1;
   }

   public String getNodeValue() throws DOMException {
      return this.nodeValue;
   }

   public void setNodeValue(String var1) throws DOMException {
      this.nodeValue = var1;
   }

   public void setNodeType(short var1) {
      this.nodeType = var1;
   }

   public short getNodeType() {
      return this.nodeType;
   }

   public Node getParentNode() {
      return this.parent;
   }

   public NodeList getChildNodes() {
      return (NodeList)(this.firstChild == null ? Util.EMPTY_NODELIST : this);
   }

   public Node getFirstChild() {
      return this.firstChild;
   }

   public Node getLastChild() {
      return this.lastChild;
   }

   public Node getPreviousSibling() {
      return this.previousSibling;
   }

   public Node getNextSibling() {
      return this.nextSibling;
   }

   public NamedNodeMap getAttributes() {
      return null;
   }

   public void setOwnerDocument(Document var1) {
      this.ownerDocument = var1;
   }

   public Document getOwnerDocument() {
      if (this.ownerDocument == null && this.parent != null) {
         this.ownerDocument = this.parent.getOwnerDocument();
      }

      return this.ownerDocument;
   }

   public void setParentNode(Node var1) {
      this.parent = (NodeImpl)var1;
   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      if (var1 == null) {
         throw new NullPointerException("newChild may not be null");
      } else if (var2 == null) {
         return null;
      } else if (var1 == var2) {
         return var1;
      } else if (var1.getNodeType() != 11) {
         NodeImpl var3 = (NodeImpl)var2;
         if (var3.parent != this) {
            throw new DOMException((short)8, "Unable to insert before the refChild because it is not a child of this node");
         } else {
            NodeImpl var4 = (NodeImpl)var1;
            if (var4.parent != null) {
               var4.parent.removeChild(var4);
            }

            ++this.numChildren;
            var4.parent = this;
            if (this.firstChild == var2) {
               this.firstChild = var4;
            } else {
               var3.previousSibling.nextSibling = var4;
            }

            var4.nextSibling = var3;
            var3.previousSibling = var4;
            this.resetCache();
            return var4;
         }
      } else {
         while(var1.hasChildNodes()) {
            this.insertBefore(var1.getFirstChild(), var2);
         }

         return var1;
      }
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      if (var1 == null) {
         throw new NullPointerException("newChild may not be null");
      } else {
         this.insertBefore(var1, var2);
         this.removeChild(var2);
         this.resetCache();
         return var2;
      }
   }

   public Node removeChild(Node var1) throws DOMException {
      if (var1 == null) {
         return null;
      } else {
         NodeImpl var2 = (NodeImpl)var1;
         if (var2.parent != this) {
            throw new DOMException((short)8, "Attempt to removeChild that is not a child of this Node");
         } else {
            var2.parent = null;
            --this.numChildren;
            if (this.firstChild == var2) {
               this.firstChild = this.firstChild.nextSibling;
               if (this.lastChild == var2) {
                  this.lastChild = this.firstChild;
               }

               this.resetCache();
               return var2;
            } else if (this.lastChild == var2) {
               this.lastChild = this.lastChild.previousSibling;
               this.resetCache();
               return var2;
            } else {
               var2.previousSibling.nextSibling = var2.nextSibling;
               this.resetCache();
               return var2;
            }
         }
      }
   }

   public Node appendChild(Node var1) throws DOMException {
      if (var1 == this) {
         throw new DOMException((short)3, "A Node may not be its own child");
      } else if (var1 == null) {
         throw new NullPointerException("newChild may not be null");
      } else if (var1.getNodeType() != 11) {
         NodeImpl var2 = (NodeImpl)var1;
         if (var2.parent != null) {
            var2.parent.removeChild(var2);
         }

         var2.parent = this;
         if (this.firstChild == null) {
            this.numChildren = 1;
            this.firstChild = var2;
            this.lastChild = var2;
            this.resetCache();
            return var2;
         } else {
            ++this.numChildren;
            this.lastChild.nextSibling = var2;
            var2.previousSibling = this.lastChild;
            this.lastChild = var2;
            this.resetCache();
            return var2;
         }
      } else {
         while(var1.hasChildNodes()) {
            this.appendChild(var1.getFirstChild());
         }

         return var1;
      }
   }

   public boolean hasChildNodes() {
      return this.numChildren != 0;
   }

   public void removeChildren() {
      this.firstChild = null;
      this.lastChild = null;
      this.numChildren = 0;
      this.resetCache();
   }

   public Node cloneNode(boolean var1) {
      NodeImpl var2;
      try {
         var2 = (NodeImpl)this.clone();
      } catch (CloneNotSupportedException var4) {
         throw new RuntimeException(var4.toString());
      }

      var2.removeChildren();
      var2.parent = null;
      if (var1) {
         for(NodeImpl var3 = this.firstChild; var3 != null; var3 = var3.nextSibling) {
            var2.appendChild(var3.cloneNode(true));
         }
      }

      return var2;
   }

   public void normalize() {
   }

   public boolean isSupported(String var1, String var2) {
      return false;
   }

   public String getNamespaceURI() {
      return null;
   }

   public String getPrefix() {
      return null;
   }

   public void setPrefix(String var1) throws DOMException {
   }

   public String getLocalName() {
      return null;
   }

   public boolean hasAttributes() {
      return false;
   }

   public Iterator getChildren() {
      return new ChildIterator(this);
   }

   private void resetCache() {
      this.currentNode = this.firstChild;
      this.currentNodeCount = 0;
   }

   public int getLength() {
      return this.numChildren;
   }

   public Node item(int var1) {
      if (var1 >= this.numChildren) {
         throw new IndexOutOfBoundsException(var1 + " greater than " + " the number of chilren " + this.numChildren);
      } else if (this.currentNodeCount == var1) {
         if (this.currentNode == null) {
            throw new NullPointerException("Internal Error: null child");
         } else {
            return this.currentNode;
         }
      } else {
         switch (var1) {
            case 0:
               this.currentNode = this.firstChild;
               this.currentNodeCount = 0;
               if (this.currentNode == null) {
                  throw new NullPointerException("Internal Error: null child");
               }

               return this.currentNode;
            case 1:
               this.currentNode = this.firstChild.nextSibling;
               this.currentNodeCount = 1;
               if (this.currentNode == null) {
                  throw new NullPointerException("Internal Error: null child");
               }

               return this.currentNode;
            case 2:
               this.currentNode = this.firstChild.nextSibling.nextSibling;
               this.currentNodeCount = 2;
               if (this.currentNode == null) {
                  throw new NullPointerException("Internal Error: null child");
               }

               return this.currentNode;
            default:
               if (this.currentNodeCount + 1 == var1) {
                  this.currentNode = this.currentNode.nextSibling;
                  ++this.currentNodeCount;
                  if (this.currentNode == null) {
                     throw new NullPointerException("Internal Error: null child");
                  } else {
                     return this.currentNode;
                  }
               } else if (this.currentNodeCount - 1 == var1) {
                  this.currentNode = this.currentNode.previousSibling;
                  --this.currentNodeCount;
                  if (this.currentNode == null) {
                     throw new NullPointerException("Internal Error: null child");
                  } else {
                     return this.currentNode;
                  }
               } else if (this.numChildren - 1 == var1) {
                  this.currentNode = this.lastChild;
                  if (this.currentNode == null) {
                     throw new NullPointerException("Internal Error: null child");
                  } else {
                     this.currentNodeCount = this.numChildren - 1;
                     return this.currentNode;
                  }
               } else {
                  this.currentNode = this.firstChild.nextSibling.nextSibling.nextSibling;
                  this.currentNodeCount = 3;

                  for(int var2 = 3; var2 < this.numChildren; ++var2) {
                     if (var2 == var1) {
                        if (this.currentNode == null) {
                           throw new NullPointerException("Internal Error: null child");
                        }

                        return this.currentNode;
                     }

                     this.currentNode = this.currentNode.nextSibling;
                     ++this.currentNodeCount;
                  }

                  throw new DOMException((short)8, "child " + var1 + " not found");
               }
         }
      }
   }

   public NodeIterator iterator() {
      return new NodeIterator(this);
   }

   public void print(StringBuffer var1, int var2) {
      var1.append("NoData");
   }

   public void write(XMLStreamWriter var1) throws XMLStreamException {
      ReaderToWriter var2 = new ReaderToWriter(var1);
      XMLStreamReader var3 = this.reader();

      while(var3.hasNext()) {
         var2.write(var3);
         var3.next();
      }

   }

   public NodeList getElementsByTagName(String var1) {
      NodeListImpl var2 = new NodeListImpl();
      NodeIterator var3 = this.iterator();
      var3.next();

      while(var3.hasNext()) {
         Node var4 = var3.nextNode();
         if (var4.getNodeType() == 1 && var1.equals(((Element)var4).getTagName())) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public NodeList getElementsByTagNameNS(String var1, String var2) {
      NodeListImpl var3 = new NodeListImpl();
      NodeIterator var4 = this.iterator();
      var4.next();

      while(true) {
         Node var5;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     return var3;
                  }

                  var5 = var4.nextNode();
               } while(var5.getNodeType() != 1);
            } while(!var2.equals(var5.getLocalName()));
         } while(var1 != null && !"*".equals(var1) && !var1.equals(var5.getNamespaceURI()));

         var3.add(var5);
      }
   }

   public XMLStreamReader reader() throws XMLStreamException {
      return new DOMStreamReader(this);
   }

   public Node createChild(Name var1) {
      throw new IllegalStateException("createChild is abstract");
   }

   public Node renameNode(Node var1, String var2, String var3) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public void normalizeDocument() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public DOMConfiguration getDomConfig() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public void setDocumentURI(String var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String getDocumentURI() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public Node adoptNode(Node var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public void setStrictErrorChecking(boolean var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public boolean getStrictErrorChecking() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public void setXmlVersion(String var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String getXmlVersion() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public void setXmlStandalone(boolean var1) throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public boolean getXmlStandalone() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String getXmlEncoding() throws DOMException {
      throw new UnsupportedOperationException("This class does not support JDK1.5");
   }

   public String getInputEncoding() throws DOMException {
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
