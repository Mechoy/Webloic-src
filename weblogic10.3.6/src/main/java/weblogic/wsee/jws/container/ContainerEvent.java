package weblogic.wsee.jws.container;

public interface ContainerEvent {
   String getURI();

   String getConversationID();

   TYPE getEventType();

   public static enum TYPE {
      EVENT_AGE_TIMEOUT,
      EVENT_IDLE_TIMEOUT;
   }
}
