package weblogic.iiop;

import org.omg.CORBA.MARSHAL;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.utils.Debug;
import weblogic.utils.collections.Pool;
import weblogic.utils.collections.StackPool;

public final class MessageHeader implements MessageHeaderConstants {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private byte[] magic = new byte[4];
   private byte[] GIOP_version = new byte[2];
   private byte flags;
   private byte message_type;
   private long message_size;
   private static final int MSG_POOL_SIZE = 1024;
   private static final Pool msgPool = new StackPool(1024);

   public String toString() {
      return "MessageHeader: magic = " + (char)this.magic[0] + (char)this.magic[1] + (char)this.magic[2] + (char)this.magic[3] + " GIOP_version = " + this.GIOP_version[0] + "." + this.GIOP_version[1] + "\n" + ((this.flags & 1) == 0 ? " big-endian byte order" : "little-endian byte order ") + " fragments follow = " + this.isFragmented() + " message_type = " + this.message_type + " message_size = " + this.message_size;
   }

   public MessageHeader(IIOPInputStream var1) {
      this.read(var1);
   }

   public void read(IIOPInputStream var1) {
      var1.read_octet_array((byte[])this.magic, 0, 4);
      var1.read_octet_array((byte[])this.GIOP_version, 0, 2);
      if (this.GIOP_version[0] != 1) {
         throw new MARSHAL("Unsupported GIOP version.");
      } else {
         switch (this.getMinorVersion()) {
            case 0:
            case 1:
            case 2:
            default:
               this.flags = var1.read_octet();
               var1.setEndian(this.isLittleEndian());
               var1.setMinorVersion(this.getMinorVersion());
               this.message_type = var1.read_octet();
               this.message_size = (long)var1.read_long();
               if (this.isFragmented() && (this.message_size + 12L) % 8L != 0L) {
                  throw new MARSHAL("Fragments must be a multiple of 8 bytes ");
               } else {
                  if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                     Debug.say("MessageHeader: " + this);
                  }

               }
         }
      }
   }

   public MessageHeader(int var1, int var2) {
      this.magic = MAGIC_VALUE;
      switch (var2) {
         case 0:
            this.GIOP_version = GIOP_VERSION_1_0;
            break;
         case 1:
            this.GIOP_version = GIOP_VERSION_1_1;
            break;
         case 2:
            this.GIOP_version = GIOP_VERSION_1_2;
            break;
         default:
            throw new MARSHAL("Unsupported GIOP version.");
      }

      this.flags = 0;
      this.message_type = (byte)var1;
      this.message_size = 0L;
   }

   public static int getMsgType(byte[] var0) {
      switch (var0[7]) {
         case 0:
            return 0;
         case 1:
            return 1;
         case 2:
            return 2;
         case 3:
            return 3;
         case 4:
            return 4;
         case 5:
            return 5;
         case 6:
            return 6;
         case 7:
            return 7;
         default:
            return -1;
      }
   }

   public int getMsgType() {
      return this.message_type;
   }

   public int getMsgSize() {
      return (int)this.message_size;
   }

   public boolean isLittleEndian() {
      return (this.flags & 1) == 1;
   }

   public boolean isFragmented() {
      return (this.flags & 2) == 2;
   }

   public void setFragmented(boolean var1) {
      if (var1) {
         this.flags = (byte)(this.flags | 2);
      } else {
         this.flags &= -3;
      }

   }

   public int getMinorVersion() {
      return this.GIOP_version[1];
   }

   public String getMsgTypeAsString() {
      switch (this.message_type) {
         case 0:
            return "REQUEST";
         case 1:
            return "REPLY";
         case 2:
            return "CANCEL_REQUEST";
         case 3:
            return "LOCATE_REQUEST";
         case 4:
            return "LOCATE_REPLY";
         case 5:
            return "CLOSE_CONNECTION";
         case 6:
            return "MESSAGE_ERROR";
         case 7:
            return "FRAGMENT";
         default:
            return "UNKNOWN";
      }
   }

   public void write(IIOPOutputStream var1) {
      var1.write_octet(MAGIC_VALUE[0]);
      var1.write_octet(MAGIC_VALUE[1]);
      var1.write_octet(MAGIC_VALUE[2]);
      var1.write_octet(MAGIC_VALUE[3]);
      var1.write_octet(this.GIOP_version[0]);
      var1.write_octet(this.GIOP_version[1]);
      var1.write_octet(this.flags);
      var1.write_octet(this.message_type);
      var1.write_long(51966);
   }

   public static final MessageHeader getMessageHeader(IIOPInputStream var0) {
      MessageHeader var1 = (MessageHeader)msgPool.remove();
      if (var1 == null) {
         var1 = new MessageHeader(var0);
      } else {
         var1.read(var0);
      }

      return var1;
   }

   public final void close() {
      this.flags = 0;
      this.message_type = 0;
      this.message_size = 0L;
      msgPool.add(this);
   }
}
