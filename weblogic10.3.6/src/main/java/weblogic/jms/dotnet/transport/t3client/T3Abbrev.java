package weblogic.jms.dotnet.transport.t3client;

class T3Abbrev {
   private final int id;
   private final byte[] content;
   static final T3Abbrev[] NULL_ABBREVS;

   T3Abbrev(int var1, T3JVMID var2) {
      this.id = var1;
      if (var2 != null) {
         this.content = var2.getBuf();
      } else {
         this.content = null;
      }

   }

   int getId() {
      return this.id;
   }

   byte[] getContent() {
      return this.content;
   }

   void write(MarshalWriterImpl var1) {
      T3.writeLength(var1, this.id);
      if (this.id > T3.PROTOCOL_ABBV_SIZE) {
         var1.write(this.content, 0, this.content.length);
      }

   }

   int size() {
      int var1 = T3.getLengthNumBytes(this.id);
      if (this.id > T3.PROTOCOL_ABBV_SIZE) {
         var1 += this.content.length;
      }

      return var1;
   }

   static {
      T3Abbrev[] var0 = new T3Abbrev[]{new T3Abbrev(255, (T3JVMID)null)};
      NULL_ABBREVS = var0;
   }
}
