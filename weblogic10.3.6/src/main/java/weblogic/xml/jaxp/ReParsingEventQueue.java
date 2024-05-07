package weblogic.xml.jaxp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;

public class ReParsingEventQueue {
   private List<EventInfo> events = new ArrayList();
   private List<EventInfo> lastEvents = new ArrayList();

   public void addEvent(EventInfo var1, EventHandler var2, int var3) {
      var1.owner = var2;
      var1.type = var3;
      this.events.add(var1);
   }

   public void clearAllEvents() {
      this.lastEvents.clear();
      this.events.clear();
   }

   public void shiftLastEvents() {
      this.lastEvents.clear();
      this.lastEvents.addAll(this.events);
      this.events.clear();
   }

   public void reSendLastEvents() throws SAXException {
      Iterator var1 = this.lastEvents.iterator();

      while(var1.hasNext()) {
         EventInfo var2 = (EventInfo)var1.next();
         EventHandler var3 = var2.owner;
         var3.sendEvent(var2);
      }

   }

   protected interface EventHandler {
      void sendEvent(EventInfo var1) throws SAXException;

      void registerQueue(ReParsingEventQueue var1);
   }

   protected static class EventInfo {
      public EventHandler owner = null;
      public int type = 0;
   }
}
