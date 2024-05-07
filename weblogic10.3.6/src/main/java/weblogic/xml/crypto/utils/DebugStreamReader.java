package weblogic.xml.crypto.utils;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class DebugStreamReader implements XMLStreamReader {
   int depth = 0;
   final XMLStreamReader delegate;
   private static final String PREFIX = "";
   private static final String INDENT = "  ";

   public DebugStreamReader(XMLStreamReader var1) {
      this.delegate = var1;
   }

   public XMLStreamReader getDelegate() {
      return this.delegate;
   }

   public int next() throws XMLStreamException {
      int var1 = this.delegate.next();
      switch (var1) {
         case 1:
            this.printStart();
            ++this.depth;
            break;
         case 2:
            --this.depth;
            this.printEnd();
         case 3:
         default:
            break;
         case 4:
            this.printCharacters();
      }

      return var1;
   }

   private void printCharacters() {
      String var1 = this.getIndent();
      System.out.println(var1 + "[CHARACTERS ");
      System.out.println(var1 + "]");
      System.out.flush();
   }

   private void printEnd() {
      String var1 = this.getIndent();
      System.out.println(var1 + "[END ELEMENT " + this.getNamespaceURI() + ":" + this.getLocalName() + "]");
      System.out.flush();
   }

   private void printStart() {
      String var1 = this.getIndent();
      System.out.println(var1 + "[START ELEMENT " + this.getNamespaceURI() + ":" + this.getLocalName());

      int var2;
      String var3;
      String var4;
      for(var2 = 0; var2 < this.delegate.getNamespaceCount(); ++var2) {
         var3 = this.delegate.getNamespacePrefix(var2);
         var4 = this.delegate.getNamespaceURI(var2);
         System.out.println(var1 + "  NAMESPACE " + var3 + " = " + var4);
      }

      for(var2 = 0; var2 < this.delegate.getAttributeCount(); ++var2) {
         var3 = this.delegate.getAttributeNamespace(var2);
         var4 = this.delegate.getAttributeLocalName(var2);
         String var5 = this.delegate.getAttributeValue(var2);
         System.out.println(var1 + "  ATTRIBUTE " + var3 + ":" + var4 + " = " + var5);
      }

      System.out.println(var1 + "]");
      System.out.flush();
   }

   private String getIndent() {
      String var1 = "";

      for(int var2 = this.depth; var2 > 0; --var2) {
         var1 = var1 + "  ";
      }

      return var1;
   }

   public Object getProperty(String var1) throws IllegalArgumentException {
      return this.delegate.getProperty(var1);
   }

   public void require(int var1, String var2, String var3) throws XMLStreamException {
      this.delegate.require(var1, var2, var3);
   }

   public String getElementText() throws XMLStreamException {
      return this.delegate.getElementText();
   }

   public int nextTag() throws XMLStreamException {
      int var1;
      do {
         var1 = this.next();
         if (var1 == 8) {
            throw new XMLStreamException("Unexpected end of Document");
         }

         if (this.isCharacters() && !this.isWhiteSpace()) {
            throw new XMLStreamException("Unexpected text");
         }
      } while(1 != var1 && 2 != var1);

      return this.getEventType();
   }

   public boolean hasNext() throws XMLStreamException {
      return this.delegate.hasNext();
   }

   public void close() throws XMLStreamException {
      this.delegate.close();
   }

   public String getNamespaceURI(String var1) {
      return this.delegate.getNamespaceURI(var1);
   }

   public boolean isStartElement() {
      return this.delegate.isStartElement();
   }

   public boolean isEndElement() {
      return this.delegate.isEndElement();
   }

   public boolean isCharacters() {
      return this.delegate.isCharacters();
   }

   public boolean isWhiteSpace() {
      return this.delegate.isWhiteSpace();
   }

   public String getAttributeValue(String var1, String var2) {
      return this.delegate.getAttributeValue(var1, var2);
   }

   public int getAttributeCount() {
      return this.delegate.getAttributeCount();
   }

   public QName getAttributeName(int var1) {
      return this.delegate.getAttributeName(var1);
   }

   public String getAttributeNamespace(int var1) {
      return this.delegate.getAttributeNamespace(var1);
   }

   public String getAttributeLocalName(int var1) {
      return this.delegate.getAttributeLocalName(var1);
   }

   public String getAttributePrefix(int var1) {
      return this.delegate.getAttributePrefix(var1);
   }

   public String getAttributeType(int var1) {
      return this.delegate.getAttributeType(var1);
   }

   public String getAttributeValue(int var1) {
      return this.delegate.getAttributeValue(var1);
   }

   public boolean isAttributeSpecified(int var1) {
      return this.delegate.isAttributeSpecified(var1);
   }

   public int getNamespaceCount() {
      return this.delegate.getNamespaceCount();
   }

   public String getNamespacePrefix(int var1) {
      return this.delegate.getNamespacePrefix(var1);
   }

   public String getNamespaceURI(int var1) {
      return this.delegate.getNamespaceURI(var1);
   }

   public NamespaceContext getNamespaceContext() {
      return this.delegate.getNamespaceContext();
   }

   public int getEventType() {
      return this.delegate.getEventType();
   }

   public String getText() {
      return this.delegate.getText();
   }

   public char[] getTextCharacters() {
      return this.delegate.getTextCharacters();
   }

   public int getTextCharacters(int var1, char[] var2, int var3, int var4) throws XMLStreamException {
      return this.delegate.getTextCharacters(var1, var2, var3, var4);
   }

   public int getTextStart() {
      return this.delegate.getTextStart();
   }

   public int getTextLength() {
      return this.delegate.getTextLength();
   }

   public String getEncoding() {
      return this.delegate.getEncoding();
   }

   public boolean hasText() {
      return this.delegate.hasText();
   }

   public Location getLocation() {
      return this.delegate.getLocation();
   }

   public QName getName() {
      return this.delegate.getName();
   }

   public String getLocalName() {
      return this.delegate.getLocalName();
   }

   public boolean hasName() {
      return this.delegate.hasName();
   }

   public String getNamespaceURI() {
      return this.delegate.getNamespaceURI();
   }

   public String getPrefix() {
      return this.delegate.getPrefix();
   }

   public String getVersion() {
      return this.delegate.getVersion();
   }

   public boolean isStandalone() {
      return this.delegate.isStandalone();
   }

   public boolean standaloneSet() {
      return this.delegate.standaloneSet();
   }

   public String getCharacterEncodingScheme() {
      return this.delegate.getCharacterEncodingScheme();
   }

   public String getPITarget() {
      return this.delegate.getPITarget();
   }

   public String getPIData() {
      return this.delegate.getPIData();
   }
}
