package weblogic.iiop;

public final class LocateRequestMessage extends SequencedRequestMessage implements MessageTypeConstants {
   private ObjectKey object_key = null;
   private TargetAddress target;

   public LocateRequestMessage(EndPoint var1, IOR var2) {
      this.msgHdr = new MessageHeader(3, var1.getMinorVersion());
      this.endPoint = var1;
      this.request_id = var1.getNextRequestID();
      this.object_key = var2.getProfile().getObjectKey();
      this.target = new TargetAddress(this.object_key);
      this.write(this.getOutputStream());
   }

   public LocateRequestMessage(EndPoint var1, MessageHeader var2, IIOPInputStream var3) {
      this.msgHdr = var2;
      this.endPoint = var1;
      this.inputStream = var3;
      this.read(var3);
   }

   public ObjectKey getObjectKey() {
      return this.object_key;
   }

   public void read(IIOPInputStream var1) {
      switch (this.getMinorVersion()) {
         case 0:
         case 1:
            this.request_id = var1.read_long();
            this.object_key = new ObjectKey(var1);
            break;
         case 2:
            this.request_id = var1.read_long();
            this.target = new TargetAddress(var1);
            this.object_key = this.target.object_key;
      }

   }

   public void write(IIOPOutputStream var1) {
      this.msgHdr.write(var1);
      switch (this.getMinorVersion()) {
         case 0:
         case 1:
            var1.write_long(this.request_id);
            this.object_key.write(var1);
            break;
         case 2:
            var1.write_long(this.request_id);
            this.target.write(var1);
      }

   }
}
