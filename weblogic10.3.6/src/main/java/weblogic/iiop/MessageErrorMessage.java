package weblogic.iiop;

public final class MessageErrorMessage extends Message {
   public MessageErrorMessage(EndPoint var1, MessageHeader var2, IIOPInputStream var3) {
      this.msgHdr = var2;
      this.endPoint = var1;
      this.inputStream = var3;
      this.read(var3);
   }

   public void read(IIOPInputStream var1) {
   }

   public void write(IIOPOutputStream var1) {
      this.msgHdr.write(var1);
   }
}
