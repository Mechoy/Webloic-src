package weblogic.xml.jaxp;

import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;

public class ReParsingDTDHandler implements DTDHandler, ReParsingEventQueue.EventHandler {
   private ReParsingEventQueue queue = null;
   private DTDHandler dtdHandler = null;
   private static final int NOTATION_DECL = 1;
   private static final int UNPARSED_ENTITY_DECL = 2;

   public void setDTDHandler(DTDHandler var1) {
      this.dtdHandler = var1;
   }

   public DTDHandler getDTDHandler() {
      return this.dtdHandler;
   }

   public void notationDecl(String var1, String var2, String var3) throws SAXException {
      if (this.dtdHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.name = var1;
         var4.publicId = var2;
         var4.systemId = var3;
         this.queue.addEvent(var4, this, 1);
      }

   }

   public void unparsedEntityDecl(String var1, String var2, String var3, String var4) throws SAXException {
      if (this.dtdHandler != null) {
         EventInfo var5 = new EventInfo();
         var5.name = var1;
         var5.publicId = var2;
         var5.systemId = var3;
         var5.notationName = var4;
         this.queue.addEvent(var5, this, 2);
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
            this.dtdHandler.notationDecl(var2.name, var2.publicId, var2.systemId);
            break;
         case 2:
            this.dtdHandler.unparsedEntityDecl(var2.name, var2.publicId, var2.systemId, var2.notationName);
      }

   }

   private class EventInfo extends ReParsingEventQueue.EventInfo {
      public String name;
      public String publicId;
      public String systemId;
      public String notationName;

      private EventInfo() {
      }

      // $FF: synthetic method
      EventInfo(Object var2) {
         this();
      }
   }
}
