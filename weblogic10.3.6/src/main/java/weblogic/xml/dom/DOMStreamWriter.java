package weblogic.xml.dom;

import java.io.FileReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.stax.XMLWriterBase;

public class DOMStreamWriter extends XMLWriterBase {
   private Document document;
   private Node current;
   private Node sibling;

   public DOMStreamWriter(Document var1) throws XMLStreamException {
      this.setStreamWriter(this);
      this.document = var1;
      this.current = var1;
   }

   public DOMStreamWriter(Document var1, Node var2) {
      this.setStreamWriter(this);
      this.document = var1;
      this.current = var2;
   }

   public DOMStreamWriter(Document var1, Node var2, Node var3) {
      this.setStreamWriter(this);
      this.document = var1;
      this.current = var2;
      this.sibling = var3;
   }

   public Node getCurrentNode() {
      return this.current;
   }

   public Document getDocument() {
      return this.document;
   }

   protected void openStartTag() throws XMLStreamException {
   }

   protected void closeStartTag() throws XMLStreamException {
      this.flushNamespace();
   }

   protected void openEndTag() throws XMLStreamException {
   }

   protected void closeEndTag() throws XMLStreamException {
   }

   protected String writeName(String var1, String var2, String var3) throws XMLStreamException {
      return var1;
   }

   private String getQualifiedName(String var1, String var2, String var3) {
      if (!"".equals(var2)) {
         var1 = this.getPrefixInternal(var2);
      }

      String var4;
      if (ElementNode.XMLNS_URI.equals(var2)) {
         var4 = "xmlns:" + var3;
      } else if (var1 != null && !"".equals(var1)) {
         var4 = var1 + ":" + var3;
      } else {
         var4 = var3;
      }

      return var4;
   }

   private void report() {
      System.out.println("appended current:" + this.current);
      System.out.println(Util.printNode(this.document));
   }

   protected void writeStartElementInternal(String var1, String var2) throws XMLStreamException {
      super.writeStartElementInternal(var1, var2);
      String var3 = this.getQualifiedName("", var1, var2);
      this.current = this.appendChild(this.document.createElementNS(var1, var3));
   }

   protected void writeStartElementInternal(String var1, String var2, String var3) throws XMLStreamException {
      super.writeStartElementInternal(var2, var3);
      String var4 = this.getQualifiedName(var1, var2, var3);
      this.current = this.appendChild(this.document.createElementNS(var2, var4));
   }

   protected void write(String var1) throws XMLStreamException {
      this.appendChild(this.document.createTextNode(var1));
   }

   protected void write(char var1) throws XMLStreamException {
      this.write(String.valueOf(var1));
   }

   protected void write(char[] var1) throws XMLStreamException {
      this.write(String.valueOf(var1));
   }

   protected void write(char[] var1, int var2, int var3) throws XMLStreamException {
      this.write(String.valueOf(var1, var2, var3));
   }

   public void writeCharacters(String var1) throws XMLStreamException {
      this.closeStartElement();
      this.write(var1);
   }

   public void writeCharacters(char[] var1, int var2, int var3) throws XMLStreamException {
      this.closeStartElement();
      this.write(var1, var2, var3);
   }

   public void writeAttribute(String var1, String var2) throws XMLStreamException {
      if (!this.isOpen()) {
         throw new XMLStreamException("A start element must be written before an attribute");
      } else if (var1 == null) {
         throw new IllegalArgumentException("The local name of an attribute may not be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("An attribute value may not be null");
      } else {
         this.writeAttribute("", var1, var2);
      }
   }

   public void writeAttribute(String var1, String var2, String var3) throws XMLStreamException {
      if (!this.isOpen()) {
         throw new XMLStreamException("A start element must be written before an attribute");
      } else if (var1 == null) {
         throw new IllegalArgumentException("The namespace URI of an attribute may not be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("The local name of an attribute may not be null");
      } else if (var3 == null) {
         throw new IllegalArgumentException("An attribute value may not be null");
      } else {
         this.prepareNamespace(var1);
         Element var4 = (Element)this.current;
         var4.setAttributeNS(var1, this.getQualifiedName("", var1, var2), var3);
      }
   }

   public void writeAttribute(String var1, String var2, String var3, String var4) throws XMLStreamException {
      if (!this.isOpen()) {
         throw new XMLStreamException("A start element must be written before an attribute");
      } else if (var2 == null) {
         throw new IllegalArgumentException("The namespace URI of an attribute may not be null");
      } else if (var3 == null) {
         throw new IllegalArgumentException("The local name of an attribute may not be null");
      } else if (var4 == null) {
         throw new IllegalArgumentException("An attribute value may not be null");
      } else {
         this.prepareNamespace(var2);
         this.context.bindNamespace(var1, var2);
         Element var5 = (Element)this.current;
         var5.setAttributeNS(var2, this.getQualifiedName(var1, var2, var3), var4);
      }
   }

   public void writeDefaultNamespace(String var1) throws XMLStreamException {
      if (!this.isOpen()) {
         throw new XMLStreamException("A start element must be written before the default namespace");
      } else {
         Element var2 = (Element)this.current;
         var2.setAttributeNS(ElementNode.XMLNS_URI, "xmlns", var1);
         this.setPrefix("", var1);
      }
   }

   public void writeNamespace(String var1, String var2) throws XMLStreamException {
      if (!this.isOpen()) {
         throw new XMLStreamException("A start element must be written before a namespace");
      } else if (var1 != null && !"".equals(var1) && !"xmlns".equals(var1)) {
         Element var3 = (Element)this.current;
         var3.setAttributeNS(ElementNode.XMLNS_URI, "xmlns:" + var1, var2);
         this.setPrefix(var1, var2);
      } else {
         this.writeDefaultNamespace(var2);
      }
   }

   public void writeEmptyElement(String var1, String var2) throws XMLStreamException {
      this.writeStartElement(var1, var2);
      this.writeEndElement();
   }

   public void writeEmptyElement(String var1, String var2, String var3) throws XMLStreamException {
      this.writeStartElement(var1, var2, var3);
      this.writeEndElement();
   }

   public void writeEndElement() throws XMLStreamException {
      super.writeEndElement();
      this.current = this.current.getParentNode();
   }

   public void writeComment(String var1) throws XMLStreamException {
      this.closeStartElement();
      this.appendChild(this.document.createComment(var1));
   }

   public void writeProcessingInstruction(String var1, String var2) throws XMLStreamException {
      this.closeStartElement();
      this.appendChild(this.document.createProcessingInstruction(var1, var2));
   }

   public void writeCData(String var1) throws XMLStreamException {
      this.closeStartElement();
      this.appendChild(this.document.createCDATASection(var1));
   }

   public void writeStartDocument() throws XMLStreamException {
   }

   public void writeStartDocument(String var1) throws XMLStreamException {
   }

   public void writeStartDocument(String var1, String var2) throws XMLStreamException {
   }

   public void flush() throws XMLStreamException {
   }

   public void writeEndDocument() throws XMLStreamException {
   }

   private final Node appendChild(Node var1) {
      if (this.sibling != null) {
         Node var2 = this.current.insertBefore(var1, this.sibling);
         this.sibling = null;
         return var2;
      } else {
         return this.current.appendChild(var1);
      }
   }

   public static void main(String[] var0) throws Exception {
      DocumentImpl var1 = new DocumentImpl();
      DOMStreamWriter var2 = new DOMStreamWriter(var1);
      XMLInputFactory var3 = XMLInputFactory.newInstance();
      XMLStreamReader var4 = var3.createXMLStreamReader(new FileReader(var0[0]));

      while(var4.hasNext()) {
         var2.write(var4);
         var4.next();
      }

      System.out.println(Util.printNode(var1));
   }
}
