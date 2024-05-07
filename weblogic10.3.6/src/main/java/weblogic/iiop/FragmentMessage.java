package weblogic.iiop;

public final class FragmentMessage extends Message {
   public FragmentMessage(EndPoint var1, MessageHeader var2, IIOPInputStream var3) {
      this.endPoint = var1;
      this.msgHdr = var2;
      this.inputStream = var3;
      this.read(var3);
   }

   public void read(IIOPInputStream var1) {
      this.request_id = var1.read_long();
   }

   public void write(IIOPOutputStream var1) {
      this.msgHdr.write(var1);
      var1.write_long(this.request_id);
   }
}
