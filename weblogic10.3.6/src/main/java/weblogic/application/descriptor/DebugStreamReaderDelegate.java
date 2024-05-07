package weblogic.application.descriptor;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import weblogic.utils.Debug;

public abstract class DebugStreamReaderDelegate extends StreamReaderDelegate implements XMLStreamReader {
   private boolean debug = Debug.getCategory("weblogic.descriptor.reader").isEnabled();

   public DebugStreamReaderDelegate(XMLStreamReader var1) throws XMLStreamException {
      super(var1);
   }

   public char[] getTextCharacters() {
      char[] var1 = super.getTextCharacters();
      if (this.debug) {
         System.out.println("->getTextCharacters: " + (var1 == null ? "null" : new String(var1)));
      }

      return var1;
   }

   public int next() throws XMLStreamException {
      int var1 = super.next();
      if (this.debug) {
         System.out.println("->next = " + Utils.type2Str(var1, this));
      }

      return var1;
   }

   public void setParent(XMLStreamReader var1) {
      if (this.debug) {
         System.out.println("->setParent");
      }

      super.setParent(var1);
   }

   public XMLStreamReader getParent() {
      if (this.debug) {
         System.out.println("->getParent");
      }

      return super.getParent();
   }

   public int nextTag() throws XMLStreamException {
      int var1 = super.nextTag();
      if (this.debug) {
         System.out.println("->nextTag: " + var1);
      }

      return var1;
   }

   public String getElementText() throws XMLStreamException {
      String var1 = super.getElementText();
      if (this.debug) {
         System.out.println("->getElementText: " + var1);
      }

      return var1;
   }

   public void require(int var1, String var2, String var3) throws XMLStreamException {
      if (this.debug) {
         System.out.println("->require");
      }

      super.require(var1, var2, var3);
   }

   public boolean hasNext() throws XMLStreamException {
      boolean var1 = super.hasNext();
      if (this.debug) {
         System.out.println("->hasNext: = " + var1);
      }

      return var1;
   }

   public void close() throws XMLStreamException {
      if (this.debug) {
         System.out.println("->close");
      }

      super.close();
   }

   public String getNamespaceURI(String var1) {
      String var2 = super.getNamespaceURI(var1);
      if (this.debug) {
         System.out.println("->getNamespaceURI(String): (" + var1 + "): " + var2);
      }

      return var2;
   }

   public NamespaceContext getNamespaceContext() {
      NamespaceContext var1 = super.getNamespaceContext();
      if (this.debug) {
         System.out.println("->getNamespaceContext: " + var1);
      }

      return var1;
   }

   public boolean isStartElement() {
      boolean var1 = super.isStartElement();
      if (this.debug) {
         System.out.println("->isStartElement: " + var1);
         if (!var1) {
            System.out.println("... eventType = " + Utils.type2Str(super.getEventType(), this));
         }
      }

      return var1;
   }

   public boolean isEndElement() {
      if (this.debug) {
         System.out.println("->isEndElement");
      }

      return super.isEndElement();
   }

   public boolean isCharacters() {
      boolean var1 = super.isCharacters();
      if (this.debug) {
         System.out.println("->isCharacters: " + var1);
      }

      return var1;
   }

   public boolean isWhiteSpace() {
      if (this.debug) {
         System.out.println("->isWhiteSpace");
      }

      return super.isWhiteSpace();
   }

   public String getAttributeValue(String var1, String var2) {
      String var3 = super.getAttributeValue(var1, var2);
      if (this.debug) {
         System.out.println("->getAttributeValue(" + var1 + ", " + var2 + ") returns: " + var3);
      }

      return var3;
   }

   public int getAttributeCount() {
      int var1 = super.getAttributeCount();
      if (this.debug) {
         System.out.println("->getAttributeCount() returns " + var1);
      }

      return var1;
   }

   public QName getAttributeName(int var1) {
      QName var2 = super.getAttributeName(var1);
      if (this.debug) {
         System.out.println("->getAttributeName(" + var1 + ") returns: " + var2);
      }

      return var2;
   }

   public String getAttributePrefix(int var1) {
      String var2 = super.getAttributePrefix(var1);
      if (this.debug) {
         System.out.println("->getAttributePrefix(" + var1 + ") return " + var2);
      }

      return var2;
   }

   public String getAttributeNamespace(int var1) {
      String var2 = super.getAttributeNamespace(var1);
      if (this.debug) {
         System.out.println("->getAttributeNamespace(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public String getAttributeLocalName(int var1) {
      String var2 = super.getAttributeLocalName(var1);
      if (this.debug) {
         System.out.println("->getAttributeLocalName(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public String getAttributeType(int var1) {
      String var2 = super.getAttributeType(var1);
      if (this.debug) {
         System.out.println("->getAttributeType(" + var1 + " returns " + var2);
      }

      return var2;
   }

   public String getAttributeValue(int var1) {
      String var2 = super.getAttributeValue(var1);
      if (this.debug) {
         System.out.println("->getAttributeValue(" + var1 + ") returns: " + var2);
      }

      return var2;
   }

   public boolean isAttributeSpecified(int var1) {
      boolean var2 = super.isAttributeSpecified(var1);
      if (this.debug) {
         System.out.println("->isAttributeSpecified(" + var1 + ") returns " + var2);
      }

      return var2;
   }

   public int getNamespaceCount() {
      int var1 = super.getNamespaceCount();
      if (this.debug) {
         System.out.println("->getNamespaceCount return " + var1);
      }

      return var1;
   }

   public String getNamespacePrefix(int var1) {
      String var2 = super.getNamespacePrefix(var1);
      if (this.debug) {
         System.out.println("->getNamespacePrefix(" + var1 + ") return " + var2);
      }

      return var2;
   }

   public String getNamespaceURI(int var1) {
      String var2 = super.getNamespaceURI(var1);
      if (this.debug) {
         System.out.println("->getNamespaceURI(" + var1 + "): " + var2);
      }

      return var2;
   }

   public int getEventType() {
      int var1 = super.getEventType();
      if (this.debug) {
         System.out.println("->getEventType: " + Utils.type2Str(var1, this));
      }

      return var1;
   }

   public String getText() {
      String var1 = super.getText();
      if (this.debug) {
         System.out.println("->getText: " + var1);
      }

      return var1;
   }

   public int getTextCharacters(int var1, char[] var2, int var3, int var4) throws XMLStreamException {
      if (this.debug) {
         System.out.println("->getTextCharacters");
      }

      return super.getTextCharacters(var1, var2, var3, var4);
   }

   public int getTextStart() {
      int var1 = super.getTextStart();
      if (this.debug) {
         System.out.println("->getTextStart");
      }

      return var1;
   }

   public int getTextLength() {
      if (this.debug) {
         System.out.println("->getTextLength");
      }

      return super.getTextLength();
   }

   public String getEncoding() {
      if (this.debug) {
         System.out.println("->getEncoding");
      }

      return super.getEncoding();
   }

   public boolean hasText() {
      if (this.debug) {
         System.out.println("->hasText");
      }

      return super.hasText();
   }

   public Location getLocation() {
      Location var1 = super.getLocation();
      if (this.debug) {
         System.out.println("->getLocation: " + var1);
      }

      return var1;
   }

   public QName getName() {
      if (this.debug) {
         System.out.println("->getName");
      }

      return super.getName();
   }

   public boolean hasName() {
      if (this.debug) {
         System.out.println("->hasName");
      }

      return super.hasName();
   }

   public String getPrefix() {
      if (this.debug) {
         System.out.println("->getPrefix");
      }

      return super.getPrefix();
   }

   public String getVersion() {
      if (this.debug) {
         System.out.println("->getVersion");
      }

      return super.getVersion();
   }

   public boolean isStandalone() {
      if (this.debug) {
         System.out.println("->isStandalone");
      }

      return super.isStandalone();
   }

   public boolean standaloneSet() {
      if (this.debug) {
         System.out.println("->standaloneSet");
      }

      return super.standaloneSet();
   }

   public String getCharacterEncodingScheme() {
      if (this.debug) {
         System.out.println("->getCharacterEncodingScheme");
      }

      return super.getCharacterEncodingScheme();
   }

   public String getPITarget() {
      if (this.debug) {
         System.out.println("->getPITarget");
      }

      return super.getPITarget();
   }

   public String getPIData() {
      if (this.debug) {
         System.out.println("->getPIData");
      }

      return super.getPIData();
   }

   public Object getProperty(String var1) {
      if (this.debug) {
         System.out.println("->getProperty");
      }

      return super.getProperty(var1);
   }
}
