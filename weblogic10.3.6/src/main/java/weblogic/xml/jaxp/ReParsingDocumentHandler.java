package weblogic.xml.jaxp;

import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class ReParsingDocumentHandler implements DocumentHandler, ReParsingEventQueue.EventHandler {
   private ReParsingEventQueue queue = null;
   private DocumentHandler documentHandler = null;
   private static final int CHARACTERS = 1;
   private static final int END_DOCUMENT = 2;
   private static final int END_ELEMENT = 3;
   private static final int IGNORABLE_WHITESPACE = 5;
   private static final int PROCESSING_INSTRUCTION = 6;
   private static final int SET_DOCUMENT_LOCATOR = 7;
   private static final int START_DOCUMENT = 9;
   private static final int START_ELEMENT = 10;

   public void setDocumentHandler(DocumentHandler var1) {
      this.documentHandler = var1;
   }

   public DocumentHandler getDocumentHandler() {
      return this.documentHandler;
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.documentHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.ch = var1;
         var4.start = var2;
         var4.length = var3;
         this.queue.addEvent(var4, this, 1);
      }

   }

   public void endDocument() throws SAXException {
      if (this.documentHandler != null) {
         EventInfo var1 = new EventInfo();
         this.queue.addEvent(var1, this, 2);
      }

   }

   public void endElement(String var1) throws SAXException {
      if (this.documentHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.name = var1;
         this.queue.addEvent(var2, this, 3);
      }

   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      if (this.documentHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.ch = var1;
         var4.start = var2;
         var4.length = var3;
         this.queue.addEvent(var4, this, 5);
      }

   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      if (this.documentHandler != null) {
         EventInfo var3 = new EventInfo();
         var3.target = var1;
         var3.data = var2;
         this.queue.addEvent(var3, this, 6);
      }

   }

   public void setDocumentLocator(Locator var1) {
      if (this.documentHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.locator = var1;
         this.queue.addEvent(var2, this, 7);
      }

   }

   public void startDocument() throws SAXException {
      if (this.documentHandler != null) {
         EventInfo var1 = new EventInfo();
         this.queue.addEvent(var1, this, 9);
      }

   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
      if (this.documentHandler != null) {
         EventInfo var3 = new EventInfo();
         var3.name = var1;
         var3.atts = var2;
         this.queue.addEvent(var3, this, 10);
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
            this.documentHandler.characters(var2.ch, var2.start, var2.length);
            break;
         case 2:
            this.documentHandler.endDocument();
            break;
         case 3:
            this.documentHandler.endElement(var2.name);
         case 4:
         case 8:
         default:
            break;
         case 5:
            this.documentHandler.ignorableWhitespace(var2.ch, var2.start, var2.length);
            break;
         case 6:
            this.documentHandler.processingInstruction(var2.target, var2.data);
            break;
         case 7:
            this.documentHandler.setDocumentLocator(var2.locator);
            break;
         case 9:
            this.documentHandler.startDocument();
            break;
         case 10:
            this.documentHandler.startElement(var2.name, var2.atts);
      }

   }

   private class EventInfo extends ReParsingEventQueue.EventInfo {
      public char[] ch;
      public int start;
      public int length;
      public String target;
      public String data;
      public Locator locator;
      public String name;
      public AttributeList atts;

      private EventInfo() {
      }

      // $FF: synthetic method
      EventInfo(Object var2) {
         this();
      }
   }
}
