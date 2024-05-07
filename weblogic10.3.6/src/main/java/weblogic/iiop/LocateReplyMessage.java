package weblogic.iiop;

import org.omg.CORBA.OBJECT_NOT_EXIST;

public final class LocateReplyMessage extends Message implements MessageTypeConstants {
   private int locate_status;
   private IOR ior;

   public LocateReplyMessage(EndPoint var1, MessageHeader var2, IIOPInputStream var3) {
      this.endPoint = var1;
      this.msgHdr = var2;
      this.inputStream = var3;
      this.read(var3);
   }

   public LocateReplyMessage(LocateRequestMessage var1, int var2) {
      this.msgHdr = new MessageHeader(4, var1.getMinorVersion());
      this.endPoint = var1.getEndPoint();
      this.request_id = var1.getRequestID();
      this.locate_status = var2;
   }

   public LocateReplyMessage(LocateRequestMessage var1, IOR var2, int var3) {
      this.msgHdr = new MessageHeader(4, var1.getMinorVersion());
      this.endPoint = var1.getEndPoint();
      this.request_id = var1.getRequestID();
      this.locate_status = var3;
      this.ior = var2;
   }

   public LocateReplyMessage(LocateRequestMessage var1, int var2, Throwable var3) {
      this.msgHdr = new MessageHeader(4, var1.getMinorVersion());
      this.endPoint = var1.getEndPoint();
      this.request_id = var1.getRequestID();
      this.locate_status = var2;
   }

   public IOR needsForwarding() {
      if (this.locate_status != 2 && this.locate_status != 3) {
         if (this.locate_status == 0) {
            throw new OBJECT_NOT_EXIST("Unknown object in LOCATE_REQUEST");
         } else {
            return null;
         }
      } else {
         return this.ior;
      }
   }

   public IOR getIOR() {
      return this.ior;
   }

   public void read(IIOPInputStream var1) {
      this.request_id = var1.read_long();
      this.locate_status = var1.read_long();
      switch (this.locate_status) {
         case 2:
         case 3:
            this.ior = new IOR(var1, true);
            break;
         case 4:
            SystemExceptionReplyBody var2 = new SystemExceptionReplyBody();
            var2.read(var1);
            break;
         case 5:
            short var3 = var1.read_short();
      }

   }

   public void write(IIOPOutputStream var1) {
      this.msgHdr.write(var1);
      var1.write_long(this.request_id);
      var1.write_long(this.locate_status);
      if (this.locate_status == 2 && this.ior != null) {
         this.ior.write(var1);
      }

   }
}
