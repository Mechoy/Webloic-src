package weblogic.jms.dotnet.transport.t3client;

final class T3JVMID {
   private static final long serialVersionUID = -2573312136796037590L;
   static final int INVALID_PORT = -1;
   private static final long INVALID_DIFFERENTIATOR = 0L;
   static final byte HAS_HOST_ADDRESS = 1;
   private static final byte HAS_ROUTER = 2;
   private static final byte HAS_CLUSTER_ADDRESS = 4;
   static final byte HAS_DOMAIN_NAME = 8;
   static final byte HAS_SERVER_NAME = 16;
   static final byte HAS_DNS_NAME = 32;
   private static final byte HAS_CHANNEL = 64;
   private static final byte SC_BLOCK_DATA = 8;
   private static final byte SC_EXTERNALIZABLE = 4;
   private static final byte TC_NULL = 112;
   private static final byte TC_CLASSDESC = 114;
   private static final byte TC_OBJECT = 115;
   private static final byte TC_BLOCKDATA = 119;
   private static final byte TC_ENDBLOCKDATA = 120;
   private static final short STREAM_MAGIC = -21267;
   private static final short STREAM_VERSION = 5;
   private static final String className = "weblogic.rjvm.JVMID";
   private byte flags;
   private String hostAddress;
   private String clusterAddress;
   private byte arrayLength;
   private long differentiator;
   private int rawAddress;
   private int[] ports;
   private String domainName;
   private String serverName;
   private String dnsName;
   private byte[] buf;

   T3JVMID(byte var1, long var2, String var4, int var5, int[] var6) {
      this.flags = var1;
      this.differentiator = var2;
      this.hostAddress = var4;
      this.rawAddress = var5;
      if (var6 != null) {
         this.ports = new int[var6.length];

         for(int var7 = 0; var7 < var6.length; ++var7) {
            this.ports[var7] = var6[var7];
         }
      }

      MarshalWriterImpl var8 = new MarshalWriterImpl();
      var8.write(0);
      this.write(var8);
      this.buf = var8.toByteArray();
   }

   byte getFlags() {
      return this.flags;
   }

   byte[] getBuf() {
      return this.buf;
   }

   long getDifferentiator() {
      return this.differentiator;
   }

   String getHostAddress() {
      return this.hostAddress;
   }

   int getRawAddress() {
      return this.rawAddress;
   }

   private void write2(MarshalWriterImpl var1) {
      var1.write(this.buf, 0, this.buf.length);
   }

   private void write(MarshalWriterImpl var1) {
      var1.writeShort((short)-21267);
      var1.writeShort((short)5);
      var1.write(115);
      var1.write(114);
      var1.writeUTF("weblogic.rjvm.JVMID");
      var1.writeLong(-2573312136796037590L);
      var1.write(12);
      var1.writeShort((int)0);
      var1.write(120);
      var1.write(112);
      var1.write(119);
      int var2 = var1.getPosition();
      var1.skip(1);
      var1.write(this.flags);
      var1.writeLong(this.differentiator);
      var1.writeUTF(this.hostAddress);
      if (this.dnsName != null) {
         var1.writeUTF(this.dnsName);
      }

      var1.writeInt(this.rawAddress);
      int var3;
      if (this.ports != null && this.ports.length != 0) {
         var1.writeInt(this.ports.length);

         for(var3 = 0; var3 < this.ports.length; ++var3) {
            var1.writeInt(this.ports[var3]);
         }
      } else {
         var1.writeInt(0);
      }

      var3 = var1.getPosition();
      int var4 = var3 - var2 - 1;
      var1.setPosition(var2);
      var1.write(var4);
      var1.setPosition(var3);
      var1.write(120);
   }
}
