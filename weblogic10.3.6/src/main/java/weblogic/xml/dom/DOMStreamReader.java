package weblogic.xml.dom;

import java.io.File;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import weblogic.xml.babel.reader.XmlChars;
import weblogic.xml.stax.XMLStreamReaderBase;

public class DOMStreamReader extends XMLStreamReaderBase implements XMLStreamReader {
   private int[] attributes = new int[1];
   private int[] namespaces = new int[1];
   private int numAttr;
   private int numNS;
   private NamedNodeMap attributeNodes;
   private final XMLStreamIterator nodeIterator;
   private Node current = null;

   public DOMStreamReader(Node var1) throws XMLStreamException {
      this.nodeIterator = new XMLStreamIterator(var1);
      this.advance();
   }

   public Node current() {
      return this.current;
   }

   protected boolean atEnd() {
      return this.current() == null;
   }

   protected void advance() throws XMLStreamException {
      if (!this.nodeIterator.hasNext()) {
         this.eventType = 8;
         this.current = null;
      } else {
         this.current = (Node)this.nodeIterator.next();
         this.setTextCache((String)null);
         this.setArrayCache((char[])null);
         this.eventType = this.convert(this.current().getNodeType());
         if (this.eventType == -1) {
            throw new XMLStreamException("Unable to advance the cursor unknown node type" + this.current());
         }

         if (this.isStartElement()) {
            this.initializeAttributesAndNamespaces();
         }

         if (this.isEndElement()) {
            this.initializeAttributesAndNamespaces();
         }
      }

   }

   protected int convert(int var1) {
      switch (var1) {
         case 1:
            if (this.nodeIterator.isOpen()) {
               return 1;
            }

            return 2;
         case 2:
         case 5:
         case 6:
         default:
            return -1;
         case 3:
            return 4;
         case 4:
            return 12;
         case 7:
            return 3;
         case 8:
            return 5;
         case 9:
            return this.nodeIterator.isOpen() ? 7 : 8;
      }
   }

   public String getPrefix() {
      return this.current().getPrefix();
   }

   public String getNamespaceURI() {
      return checkNull(this.current().getNamespaceURI());
   }

   public String getLocalName() {
      return this.current().getLocalName();
   }

   private void addNamespace(int var1) {
      this.namespaces[this.numNS] = var1;
      ++this.numNS;
   }

   private void addAttribute(int var1) {
      this.attributes[this.numAttr] = var1;
      ++this.numAttr;
   }

   private void setAttributeCapacity(int var1) {
      this.numAttr = 0;
      if (var1 >= this.attributes.length) {
         this.attributes = new int[var1];
      }
   }

   private void setNamespaceCapacity(int var1) {
      this.numNS = 0;
      if (var1 >= this.namespaces.length) {
         this.namespaces = new int[var1];
      }
   }

   protected void initializeAttributesAndNamespaces() {
      int var1 = this.updateAttributeNodes();
      this.setAttributeCapacity(var1);
      this.setNamespaceCapacity(var1);

      for(int var2 = 0; var2 < var1; ++var2) {
         Attr var3 = (Attr)this.attributeNodes.item(var2);
         if ("xmlns".equals(var3.getPrefix())) {
            this.addNamespace(var2);
         } else if ("xmlns".equals(var3.getLocalName())) {
            this.addNamespace(var2);
         } else {
            this.addAttribute(var2);
         }
      }

   }

   private int updateAttributeNodes() {
      int var1;
      if (this.current().hasAttributes()) {
         this.attributeNodes = this.current().getAttributes();
         var1 = this.attributeNodes.getLength();
      } else {
         this.attributeNodes = null;
         var1 = 0;
      }

      return var1;
   }

   protected void initializeOutOfScopeNamespaces() {
      int var1 = this.updateAttributeNodes();
      this.setNamespaceCapacity(var1);

      for(int var2 = 0; var2 < var1; ++var2) {
         Attr var3 = (Attr)this.attributeNodes.item(var2);
         if ("xmlns".equals(var3.getPrefix())) {
            this.addNamespace(var2);
         } else if ("xmlns".equals(var3.getLocalName())) {
            this.addNamespace(var2);
         }
      }

   }

   public int getAttributeCount() {
      if (this.isStartElement()) {
         return this.numAttr;
      } else {
         throw new IllegalStateException("Unable to access attributes on a non START_ELEMENT");
      }
   }

   public String getAttributeValue(String var1, String var2) {
      for(int var3 = 0; var3 < this.getAttributeCount(); ++var3) {
         Attr var4 = this.getAttrInternal(var3);
         if (var2.equals(var4.getLocalName())) {
            if (var1 == null) {
               return var4.getValue();
            }

            if (var1.equals(var4.getNamespaceURI())) {
               return var4.getValue();
            }
         }
      }

      return null;
   }

   private Attr getAttrInternal(int var1) {
      return this.attributeNodes == null ? null : (Attr)this.attributeNodes.item(this.attributes[var1]);
   }

   private Attr getNSInternal(int var1) {
      return this.attributeNodes == null ? null : (Attr)this.attributeNodes.item(this.namespaces[var1]);
   }

   public String getAttributeNamespace(int var1) {
      this.checkStartElement();
      Attr var2 = this.getAttrInternal(var1);
      return var2 == null ? null : var2.getNamespaceURI();
   }

   public String getAttributeLocalName(int var1) {
      this.checkStartElement();
      Attr var2 = this.getAttrInternal(var1);
      return var2 == null ? null : var2.getLocalName();
   }

   public String getAttributePrefix(int var1) {
      this.checkStartElement();
      Attr var2 = this.getAttrInternal(var1);
      return var2 == null ? null : var2.getPrefix();
   }

   public String getAttributeValue(int var1) {
      this.checkStartElement();
      Attr var2 = this.getAttrInternal(var1);
      return var2 == null ? null : var2.getValue();
   }

   private Element findElement() {
      if (this.current().getNodeType() == 1) {
         return (Element)this.current();
      } else {
         for(Node var1 = this.current().getParentNode(); var1 != null; var1 = var1.getParentNode()) {
            if (var1.getNodeType() == 1) {
               return (Element)var1;
            }
         }

         return null;
      }
   }

   public NamespaceContext getNamespaceContext() {
      return new NamespaceContextNode(this.findElement());
   }

   public int getNamespaceCount() {
      this.checkStartOrEnd();
      return this.numNS;
   }

   public String getNamespaceURI(String var1) {
      this.checkStartOrEnd();
      Element var2 = (Element)this.current();

      String var3;
      Node var4;
      for(var3 = null; var3 == null && var2 != null && var2.getNodeType() == 1; var2 = (Element)var4) {
         var3 = var2.getAttributeNS(ElementNode.XMLNS_URI, var1);
         if (var3 != null) {
            return var3;
         }

         var4 = var2.getParentNode();
         if (var4.getNodeType() != 1) {
            return null;
         }
      }

      return var3;
   }

   public String getNamespacePrefix(int var1) {
      this.checkStartOrEnd();
      Attr var2 = this.getNSInternal(var1);
      return var2 == null ? null : var2.getLocalName();
   }

   public String getNamespaceURI(int var1) {
      this.checkStartOrEnd();
      Attr var2 = this.getNSInternal(var1);
      return var2 == null ? null : checkNull(var2.getValue());
   }

   public String getText() {
      if (this.getTextCache() == null) {
         if (this.eventType != 12 && this.eventType != 5 && this.eventType != 4) {
            throw new IllegalStateException("Attempt to access text from an illegal state");
         }

         this.setTextCache(this.current().getNodeValue());
      }

      return this.getTextCache();
   }

   public boolean isWhiteSpace() {
      char[] var1 = this.getTextCharacters();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (!XmlChars.isSpace(var1[var2])) {
            return false;
         }
      }

      return true;
   }

   public char[] getTextCharacters() {
      if (this.getArrayCache() == null) {
         if (this.eventType != 12 && this.eventType != 5 && this.eventType != 4) {
            throw new IllegalStateException("Attempt to access text from an illegal state");
         }

         this.setArrayCache(this.current().getNodeValue().toCharArray());
      }

      return this.getArrayCache();
   }

   public int getLineNumber() {
      return -1;
   }

   public int getColumnNumber() {
      return -1;
   }

   public static void main(String[] var0) throws Exception {
      Builder var1 = new Builder();
      NodeImpl var2 = var1.create(new File(var0[0]));
      DOMStreamReader var3 = new DOMStreamReader(var2);

      while(var3.hasNext()) {
         System.out.println(var3.toString());
         var3.next();
      }

   }
}
