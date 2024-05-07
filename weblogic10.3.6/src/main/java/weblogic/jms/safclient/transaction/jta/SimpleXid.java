package weblogic.jms.safclient.transaction.jta;

import java.nio.ByteBuffer;
import javax.transaction.xa.Xid;

public final class SimpleXid implements Xid {
   private static final int FORMAT_ID = 8675309;
   private static final int DEFAULT_BQUAL = 1;
   private static final IDRoller idRoller = new IDRoller();
   private long timestamp = System.currentTimeMillis();
   private short counter;
   private int branch;

   SimpleXid() {
      this.counter = idRoller.getNextID();
      this.branch = 1;
   }

   public int getFormatId() {
      return 8675309;
   }

   public byte[] getGlobalTransactionId() {
      ByteBuffer var1 = ByteBuffer.allocate(10);
      var1.putLong(this.timestamp);
      var1.putShort(this.counter);
      var1.flip();
      return var1.array();
   }

   public byte[] getBranchQualifier() {
      ByteBuffer var1 = ByteBuffer.allocate(4);
      var1.putInt(this.branch);
      var1.flip();
      return var1.array();
   }

   public int hashCode() {
      return (int)this.timestamp;
   }

   public boolean equals(Object var1) {
      try {
         SimpleXid var2 = (SimpleXid)var1;
         return var2.timestamp == this.timestamp && var2.counter == this.counter && var2.branch == this.branch;
      } catch (ClassCastException var3) {
         return false;
      }
   }

   private static final class IDRoller {
      private short counter;

      private IDRoller() {
      }

      synchronized short getNextID() {
         return ++this.counter;
      }

      // $FF: synthetic method
      IDRoller(Object var1) {
         this();
      }
   }
}
