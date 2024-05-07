package weblogic.xml.jaxp;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class ReParsingContentHandler implements ContentHandler, ReParsingEventQueue.EventHandler {
   private ReParsingEventQueue queue = null;
   private ContentHandler contentHandler = null;
   private static final int CHARACTERS = 1;
   private static final int END_DOCUMENT = 2;
   private static final int END_ELEMENT = 3;
   private static final int END_PREFIX_MAPPING = 4;
   private static final int IGNORABLE_WHITESPACE = 5;
   private static final int PROCESSING_INSTRUCTION = 6;
   private static final int SET_DOCUMENT_LOCATOR = 7;
   private static final int SKIPPED_ENTITY = 8;
   private static final int START_DOCUMENT = 9;
   private static final int START_ELEMENT = 10;
   private static final int START_PREFIX_MAPPING = 11;

   public void setContentHandler(ContentHandler var1) {
      this.contentHandler = var1;
   }

   public ContentHandler getContentHandler() {
      return this.contentHandler;
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.ch = var1;
         var4.start = var2;
         var4.length = var3;
         this.queue.addEvent(var4, this, 1);
      }

   }

   public void endDocument() throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var1 = new EventInfo();
         this.queue.addEvent(var1, this, 2);
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.uri = var1;
         var4.localName = var2;
         var4.qName = var3;
         this.queue.addEvent(var4, this, 3);
      }

   }

   public void endPrefixMapping(String var1) throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.prefix = var1;
         this.queue.addEvent(var2, this, 4);
      }

   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.ch = var1;
         var4.start = var2;
         var4.length = var3;
         this.queue.addEvent(var4, this, 5);
      }

   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var3 = new EventInfo();
         var3.target = var1;
         var3.data = var2;
         this.queue.addEvent(var3, this, 6);
      }

   }

   public void setDocumentLocator(Locator var1) {
      if (this.contentHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.locator = var1;
         this.queue.addEvent(var2, this, 7);
      }

   }

   public void skippedEntity(String var1) throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.name = var1;
         this.queue.addEvent(var2, this, 8);
      }

   }

   public void startDocument() throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var1 = new EventInfo();
         this.queue.addEvent(var1, this, 9);
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var5 = new EventInfo();
         var5.uri = var1;
         var5.localName = var2;
         var5.qName = var3;
         var5.atts = var4;
         this.queue.addEvent(var5, this, 10);
      }

   }

   public void startPrefixMapping(String var1, String var2) throws SAXException {
      if (this.contentHandler != null) {
         EventInfo var3 = new EventInfo();
         var3.prefix = var1;
         var3.uri = var2;
         this.queue.addEvent(var3, this, 11);
      }

   }

   public void registerQueue(ReParsingEventQueue var1) {
      this.queue = var1;
   }

   public void sendEvent(ReParsingEventQueue.EventInfo var1) throws SAXException {
      EventInfo var2 = (EventInfo)var1;
      int var3 = var2.type;
      switch (var3) {
         case 1:
            this.contentHandler.characters(var2.ch, var2.start, var2.length);
            break;
         case 2:
            this.contentHandler.endDocument();
            break;
         case 3:
            this.contentHandler.endElement(var2.uri, var2.localName, var2.qName);
            break;
         case 4:
            this.contentHandler.endPrefixMapping(var2.prefix);
            break;
         case 5:
            this.contentHandler.ignorableWhitespace(var2.ch, var2.start, var2.length);
            break;
         case 6:
            this.contentHandler.processingInstruction(var2.target, var2.data);
            break;
         case 7:
            this.contentHandler.setDocumentLocator(var2.locator);
            break;
         case 8:
            this.contentHandler.skippedEntity(var2.name);
            break;
         case 9:
            this.contentHandler.startDocument();
            break;
         case 10:
            this.contentHandler.startElement(var2.uri, var2.localName, var2.qName, var2.atts);
            break;
         case 11:
            this.contentHandler.startPrefixMapping(var2.prefix, var2.uri);
      }

   }

   private class EventInfo extends ReParsingEventQueue.EventInfo {
      public char[] ch;
      public int start;
      public int length;
      public String uri;
      public String localName;
      public String qName;
      public String prefix;
      public String target;
      public String data;
      public Locator locator;
      public String name;
      public Attributes atts;

      private EventInfo() {
      }

      // $FF: synthetic method
      EventInfo(Object var2) {
         this();
      }
   }
}
