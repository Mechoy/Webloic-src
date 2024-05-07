package weblogic.jms.dotnet.transport.t3client;

public class T3Header {
   private final byte command;
   private final byte qos;
   private final byte flags;
   private final int responseId;
   private final int invokeId;
   private int len;
   private int offset;
   static final int SIZE = 19;

   T3Header(MarshalReaderImpl var1) throws Exception {
      this.len = var1.readInt();
      this.command = var1.readByte();
      this.qos = var1.readByte();
      this.flags = var1.readByte();
      this.responseId = var1.readInt();
      this.invokeId = var1.readInt();
      this.offset = var1.readInt();
   }

   T3Header(byte var1, byte var2, byte var3, int var4, int var5) {
      this.command = var1;
      this.qos = var2;
      this.flags = var3;
      this.responseId = var4;
      this.invokeId = var5;
   }

   synchronized void setMessageLength(int var1) {
      this.len = var1;
   }

   synchronized void setOffset(int var1) {
      this.offset = var1;
   }

   byte getCommand() {
      return this.command;
   }

   byte getQOS() {
      return this.qos;
   }

   byte getFlags() {
      return this.flags;
   }

   int getResponseId() {
      return this.responseId;
   }

   int getInvokeId() {
      return this.invokeId;
   }

   synchronized int getMessageLength() {
      return this.len;
   }

   synchronized int getOffset() {
      return this.offset;
   }

   public void write(MarshalWriterImpl var1) throws Exception {
      var1.writeInt(this.getMessageLength());
      var1.writeByte(this.command);
      var1.writeByte(this.qos);
      var1.writeByte(this.flags);
      var1.writeInt(this.responseId);
      var1.writeInt(this.invokeId);
      var1.writeInt(this.getOffset());
   }

   public String toString() {
      return "{  *****  header  *****" + "\tlength:     " + this.len + "\tcommand:    " + this.command + "\tqos:        " + this.qos + "\tflags:      " + this.flags + "\tresponseId: " + this.responseId + "\tinvokeId:   " + this.invokeId + "\toffset:     " + this.offset + "}";
   }
}
