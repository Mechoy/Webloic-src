package weblogic.jms.dotnet.transport;

class TransportAnnouncement implements ReceivedOneWay {
   private Transport transport;

   TransportAnnouncement(Transport var1) {
      this.transport = var1;
   }

   public Transport getTransport() {
      return this.transport;
   }

   public long getServiceId() {
      return 10000L;
   }

   public MarshalReadable getRequest() {
      return null;
   }
}
