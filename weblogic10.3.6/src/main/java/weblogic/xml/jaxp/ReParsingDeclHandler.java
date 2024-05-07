package weblogic.xml.jaxp;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

public class ReParsingDeclHandler implements DeclHandler, ReParsingEventQueue.EventHandler {
   private static final int ATTRIBUTE_DECL = 1;
   private static final int ELEMENT_DECL = 2;
   private static final int EXTERNAL_ENTITY_DECL = 3;
   private static final int INTERNAL_ENTITY_DECL = 4;
   private ReParsingEventQueue queue = null;
   private DeclHandler declHandler = null;

   public void setDeclHandler(DeclHandler var1) {
      this.declHandler = var1;
   }

   public DeclHandler getDeclHandler() {
      return this.declHandler;
   }

   public void attributeDecl(String var1, String var2, String var3, String var4, String var5) throws SAXException {
      if (this.declHandler != null) {
         EventInfo var6 = new EventInfo();
         var6.name = var1;
         var6.name2 = var2;
         var6._type = var3;
         var6.mode = var4;
         var6.value = var5;
         this.queue.addEvent(var6, this, 1);
      }

   }

   public void elementDecl(String var1, String var2) throws SAXException {
      if (this.declHandler != null) {
         EventInfo var3 = new EventInfo();
         var3.name = var1;
         var3.model = var2;
         this.queue.addEvent(var3, this, 2);
      }

   }

   public void externalEntityDecl(String var1, String var2, String var3) throws SAXException {
      if (this.declHandler != null) {
         EventInfo var4 = new EventInfo();
         var4.name = var1;
         var4.publicId = var2;
         var4.systemId = var3;
         this.queue.addEvent(var4, this, 3);
      }

   }

   public void internalEntityDecl(String var1, String var2) throws SAXException {
      if (this.declHandler != null) {
         EventInfo var3 = new EventInfo();
         var3.name = var1;
         var3.value = var2;
         this.queue.addEvent(var3, this, 4);
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
            this.declHandler.attributeDecl(var2.name, var2.name2, var2._type, var2.mode, var2.value);
            break;
         case 2:
            this.declHandler.elementDecl(var2.name, var2.model);
            break;
         case 3:
            this.declHandler.externalEntityDecl(var2.name, var2.publicId, var2.systemId);
            break;
         case 4:
            this.declHandler.internalEntityDecl(var2.name, var2.value);
      }

   }

   private class EventInfo extends ReParsingEventQueue.EventInfo {
      public String _type;
      public String systemId;
      public String publicId;
      public String model;
      public String value;
      public String mode;
      public String name2;
      public String name;

      private EventInfo() {
      }

      // $FF: synthetic method
      EventInfo(Object var2) {
         this();
      }
   }
}
