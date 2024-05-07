package weblogic.xml.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public final class DocumentImpl extends NodeImpl implements Document {
   private String namespaceURI;
   private String localName;
   private ElementNode documentElement;

   public DocumentImpl() {
      this.setNodeType((short)9);
   }

   public DocumentImpl(String var1, String var2) {
      this.namespaceURI = var1;
      this.localName = Util.getLocalName(var2);
   }

   public String getNodeName() {
      return "#document";
   }

   public Document getOwnerDocument() {
      return this;
   }

   private void check(Node var1) {
      if (var1.getNodeType() == 1) {
         this.documentElement = (ElementNode)var1;
      }

   }

   public Node appendChild(Node var1) throws DOMException {
      this.check(var1);
      return super.appendChild(var1);
   }

   public Node removeChild(Node var1) throws DOMException {
      if (var1 == this.documentElement) {
         this.documentElement = null;
      }

      return super.removeChild(var1);
   }

   public Node insertBefore(Node var1, Node var2) {
      this.check(var1);
      return super.insertBefore(var1, var2);
   }

   public Node getPreviousSibling() {
      return null;
   }

   public Node getNextSibling() {
      return null;
   }

   public String getNamespaceURI(String var1) {
      return var1;
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

   public DOMImplementation getImplementation() {
      return ImplementationFactory.newImplementation();
   }

   public DocumentType getDoctype() {
      return null;
   }

   public Element getDocumentElement() {
      return this.documentElement;
   }

   public Element createElement(String var1) throws DOMException {
      ElementNode var2 = new ElementNode();
      var2.setLocalName(var1);
      var2.setOwnerDocument(this);
      return var2;
   }

   public DocumentFragment createDocumentFragment() {
      return new DocumentFragmentNode();
   }

   public Text createTextNode(String var1) {
      TextNode var2 = new TextNode(var1);
      this.setOwnerDocument(this);
      return var2;
   }

   public Comment createComment(String var1) {
      TextNode var2 = new TextNode(var1);
      var2.setOwnerDocument(this);
      return var2.asComment();
   }

   public CDATASection createCDATASection(String var1) throws DOMException {
      TextNode var2 = new TextNode(var1);
      var2.setOwnerDocument(this);
      return var2.asCDATA();
   }

   public ProcessingInstruction createProcessingInstruction(String var1, String var2) throws DOMException {
      PINode var3 = new PINode(var1, var2);
      var3.setOwnerDocument(this);
      return var3;
   }

   public Attr createAttribute(String var1) throws DOMException {
      AttributeImpl var2 = new AttributeImpl();
      var2.setLocalName(var1);
      return var2;
   }

   public EntityReference createEntityReference(String var1) throws DOMException {
      throw new UnsupportedOperationException("NYI");
   }

   public Node importNode(Node var1, boolean var2) throws DOMException {
      return var1;
   }

   public Element createElementNS(String var1, String var2) throws DOMException {
      ElementNode var3 = new ElementNode(var1, Util.getLocalName(var2), Util.getPrefix(var2));
      var3.setOwnerDocument(this);
      return var3;
   }

   public Attr createAttributeNS(String var1, String var2) throws DOMException {
      AttributeImpl var3 = new AttributeImpl();
      var3.setLocalName(Util.getLocalName(var2));
      var3.setNamespaceURI(var1);
      var3.setPrefix(Util.getPrefix(var2));
      return var3;
   }

   public Element getElementById(String var1) {
      return null;
   }

   public void print(StringBuffer var1, int var2) {
      var1.append("DOCUMENT[" + this.namespaceURI + "][" + this.localName + "][\n");
      NodeList var3 = this.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         NodeImpl var5 = (NodeImpl)var3.item(var4);
         if (var5 == null) {
            System.out.println(var4 + " is null");
         }

         if (var5 == this.documentElement) {
            var1.append("ROOT(" + var4 + ").->[");
         } else {
            var1.append("CHILD(" + var4 + ").->[");
         }

         var5.print(var1, var2 + 1);
         var1.append("]\n");
      }

      var1.append("]\n");
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      this.print(var1, 0);
      return var1.toString();
   }

   public static void main(String[] var0) throws Exception {
      DOMImplementation var1 = ImplementationFactory.newImplementation();
      Document var2 = var1.createDocument("http://myuri", "prefix:document", (DocumentType)null);
      Element var3 = var2.createElement("doc");
      var2.appendChild(var3);
      var2.insertBefore(var2.createProcessingInstruction("pi1", "data1"), var3);
      var2.insertBefore(var2.createTextNode("\n    \n"), var3);
      var2.appendChild(var2.createProcessingInstruction("pi2", "data2"));
      var2.appendChild(var2.createTextNode("\n"));
      Element var4 = var2.createElementNS("http://fruit", "a:apple");
      var3.appendChild(var4);
      Element var5 = var2.createElementNS("http://animal", "b:bear");
      var5.appendChild(var2.createTextNode("some text about bears"));
      var3.appendChild(var5);
      var3.setAttribute("root", "value");
      System.out.println(var2);
      var2.removeChild(var3);
      System.out.println(var2);
      var2.appendChild(var3);
      System.out.println(var2);
   }
}
