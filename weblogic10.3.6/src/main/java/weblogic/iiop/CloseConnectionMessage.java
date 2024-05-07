package weblogic.iiop;

public final class CloseConnectionMessage extends Message implements MessageTypeConstants {
   public CloseConnectionMessage(EndPoint var1, MessageHeader var2, IIOPInputStream var3) {
      this.endPoint = var1;
      this.msgHdr = var2;
      this.inputStream = var3;
      this.read(var3);
   }

   public CloseConnectionMessage(EndPoint var1) {
      this.endPoint = var1;
      this.msgHdr = new MessageHeader(5, var1.getMinorVersion());
   }

   public void write(IIOPOutputStream var1) {
      this.msgHdr.write(var1);
   }

   public void read(IIOPInputStream var1) {
   }
}
