package weblogic.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class ContentEventListener implements ContentHandler {
   private String namespaceURI;
   private String localName;
   private String qualifiedName;
   private boolean startElementPassed = false;
   private boolean endDocumentPassed = false;

   public void startElement(String var1, String var2, String var3, Attributes var4) {
      this.setNamespaceURI(var1);
      this.setLocalName(var2);
      this.setQualifiedName(var3);
      this.setStartElementPassed(true);
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   private void setNamespaceURI(String var1) {
      this.namespaceURI = var1;
   }

   public String getLocalName() {
      return this.localName;
   }

   private void setLocalName(String var1) {
      this.localName = var1;
   }

   public String getQualifiedName() {
      return this.qualifiedName;
   }

   private void setQualifiedName(String var1) {
      this.qualifiedName = var1;
   }

   protected boolean getStartElementPassed() {
      return this.startElementPassed;
   }

   private void setStartElementPassed(boolean var1) {
      this.startElementPassed = var1;
   }

   protected boolean getEndDocumentPassed() {
      return this.endDocumentPassed;
   }

   private void setEndDocumentPassed(boolean var1) {
      this.endDocumentPassed = var1;
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
   }

   public void endDocument() throws SAXException {
      this.setEndDocumentPassed(true);
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
   }

   public void endPrefixMapping(String var1) throws SAXException {
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
   }

   public void processingInstruction(String var1, String var2) throws SAXException {
   }

   public void setDocumentLocator(Locator var1) {
   }

   public void skippedEntity(String var1) throws SAXException {
   }

   public void startDocument() throws SAXException {
   }

   public void startPrefixMapping(String var1, String var2) throws SAXException {
   }
}
