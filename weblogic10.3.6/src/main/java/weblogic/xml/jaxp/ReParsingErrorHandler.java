package weblogic.xml.jaxp;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReParsingErrorHandler implements ErrorHandler, ReParsingEventQueue.EventHandler {
   private ReParsingEventQueue queue = null;
   private ErrorHandler errorHandler = null;
   private ReParsingStatus status = null;
   private static final int ERROR = 1;
   private static final int FATAL_ERROR = 2;
   private static final int WARNING = 3;

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandler = var1;
   }

   public ErrorHandler getErrorHandler() {
      return this.errorHandler;
   }

   public void hookStatus(ReParsingStatus var1) {
      this.status = var1;
   }

   public void error(SAXParseException var1) throws SAXException {
      if (this.status != null) {
         this.status.error = var1;
      }

      if (this.errorHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.exception = var1;
         this.queue.addEvent(var2, this, 1);
      }

   }

   public void fatalError(SAXParseException var1) throws SAXException {
      if (this.status != null) {
         this.status.error = var1;
      }

      if (this.errorHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.exception = var1;
         this.queue.addEvent(var2, this, 2);
      } else {
         throw var1;
      }
   }

   public void warning(SAXParseException var1) throws SAXException {
      if (this.errorHandler != null) {
         EventInfo var2 = new EventInfo();
         var2.exception = var1;
         this.queue.addEvent(var2, this, 3);
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
            this.errorHandler.error(var2.exception);
            break;
         case 2:
            this.errorHandler.fatalError(var2.exception);
            break;
         case 3:
            this.errorHandler.warning(var2.exception);
      }

   }

   private class EventInfo extends ReParsingEventQueue.EventInfo {
      public SAXParseException exception;

      private EventInfo() {
      }

      // $FF: synthetic method
      EventInfo(Object var2) {
         this();
      }
   }
}
