package weblogic.xml.jaxp;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class ReParsingLexicalHandler implements LexicalHandler, ReParsingEventQueue.EventHandler {
   private static final int COMMENT = 1;
   private static final int END_CDATA = 2;
   private static final int END_DTD = 3;
   private static final int END_ENTITY = 4;
   private static final int START_CDATA = 5;
   private static final int START_DTD = 6;
   private static final int START_ENTITY = 7;
   private ReParsingEventQueue queue = null;
   private LexicalHandler lexicalHandler = null;

   public void setLexicalHandler(LexicalHandler var1) {
      this.lexicalHandler = var1;
   }

   public LexicalHandler getLexicalHandler() {
      return this.lexicalHandler;
   }

   public void comment(char[] var1, int var2, int var3) throws SAXException {
      if (this.lexicalHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.ch = var1;
         var4.start = var2;
         var4.length = var3;
         this.queue.addEvent(var4, this, 1);
      }

   }

   public void endCDATA() throws SAXException {
      if (this.lexicalHandler != null) {
         EventInfo var1 = new EventInfo();
         this.queue.addEvent(var1, this, 2);
      }

   }

   public void endDTD() throws SAXException {
      if (this.lexicalHandler != null) {
         EventInfo var1 = new EventInfo();
         this.queue.addEvent(var1, this, 3);
      }

   }

   public void endEntity(String var1) throws SAXException {
      if (this.lexicalHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.name = var1;
         this.queue.addEvent(var2, this, 4);
      }

   }

   public void startCDATA() throws SAXException {
      if (this.lexicalHandler != null) {
         EventInfo var1 = new EventInfo();
         this.queue.addEvent(var1, this, 5);
      }

   }

   public void startDTD(String var1, String var2, String var3) throws SAXException {
      if (this.lexicalHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.name = var1;
         var4.publicId = var2;
         var4.systemId = var3;
         this.queue.addEvent(var4, this, 6);
      }

   }

   public void startEntity(String var1) throws SAXException {
      if (this.lexicalHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.name = var1;
         this.queue.addEvent(var2, this, 7);
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
            this.lexicalHandler.comment(var2.ch, var2.start, var2.length);
            break;
         case 2:
            this.lexicalHandler.endCDATA();
            break;
         case 3:
            this.lexicalHandler.endDTD();
            break;
         case 4:
            this.lexicalHandler.endEntity(var2.name);
            break;
         case 5:
            this.lexicalHandler.startCDATA();
            break;
         case 6:
            this.lexicalHandler.startDTD(var2.name, var2.publicId, var2.systemId);
            break;
         case 7:
            this.lexicalHandler.startEntity(var2.name);
      }

   }

   private class EventInfo extends ReParsingEventQueue.EventInfo {
      public String systemId;
      public String publicId;
      public String name;
      public int length;
      public int start;
      public char[] ch;

      private EventInfo() {
      }

      // $FF: synthetic method
      EventInfo(Object var2) {
         this();
      }
   }
}
