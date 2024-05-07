package weblogic.messaging.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.messaging.MessageID;

public abstract class MessageIDImpl implements Externalizable, Comparable, MessageID {
   static final long serialVersionUID = -1173635685896143247L;
   private static final byte MINVERSION = 1;
   private static final byte WL60_VERSION = 1;
   private static final byte WL61_VERSION = 11;
   private static final byte WL81_VERSION = 12;
   private static final byte MAXVERSION = 12;
   private static final int VERSION_MASK = 63;
   private static final int HAS_DIFFERENTIATOR = 64;
   protected int seed;
   protected long timestamp;
   protected int counter;
   protected int differentiator;

   public MessageIDImpl(MessageIDFactory var1) {
      var1.initMessageId(this);
   }

   public MessageIDImpl(int var1, long var2, int var4) {
      this.seed = var1;
      this.timestamp = var2;
      this.counter = var4;
   }

   public MessageIDImpl(MessageIDImpl var1, int var2) {
      this.seed = var1.seed;
      this.timestamp = var1.timestamp;
      this.counter = var1.counter;
      this.differentiator = var2;
   }

   void init(int var1, long var2, int var4) {
      this.seed = var1;
      this.timestamp = var2;
      this.counter = var4;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void setDifferentiator(int var1) {
      this.differentiator = var1;
   }

   public int getDifferentiator() {
      return this.differentiator;
   }

   public MessageIDImpl() {
   }

   private int getStreamVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_81) < 0) {
            return 11;
         }
      }

      return 12;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = this.getStreamVersion(var1);
      if (var2 >= 12 && this.differentiator != 0) {
         var2 |= 64;
      }

      var1.writeByte((byte)var2);
      var1.writeLong(this.timestamp);
      var1.writeInt(this.counter);
      var1.writeInt(this.seed);
      if ((var2 & 64) != 0) {
         var1.writeInt(this.differentiator);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      int var3 = var2 & 63;
      if (var3 != 12 && var3 != 11) {
         if (var3 != 1) {
            throw MessagingUtilities.versionIOException(var3, 1, 12);
         }

         this.timestamp = var1.readLong();
         this.counter = var1.readInt();
         var1.readInt();
         this.seed = var1.readInt();
      } else {
         this.timestamp = var1.readLong();
         this.counter = var1.readInt();
         this.seed = var1.readInt();
         if ((var2 & 64) != 0) {
            this.differentiator = var1.readInt();
         }
      }

   }

   public boolean equals(Object var1) {
      MessageIDImpl var2 = (MessageIDImpl)var1;
      return this.timestamp == var2.timestamp && this.counter == var2.counter && this.seed == var2.seed;
   }

   public boolean differentiatedEquals(Object var1) {
      return this.equals(var1) && this.differentiator == ((MessageIDImpl)var1).differentiator;
   }

   public int compare(MessageIDImpl var1) {
      if (this.timestamp > var1.timestamp) {
         return 1;
      } else if (this.timestamp < var1.timestamp) {
         return -1;
      } else if (this.counter > var1.counter) {
         return 1;
      } else if (this.counter < var1.counter) {
         return -1;
      } else if (this.seed > var1.seed) {
         return 1;
      } else if (this.seed < var1.seed) {
         return -1;
      } else if (this.differentiator > var1.differentiator) {
         return 1;
      } else {
         return this.differentiator < var1.differentiator ? -1 : 0;
      }
   }

   public int compareTime(MessageIDImpl var1) {
      if (this.timestamp < var1.timestamp) {
         return -1;
      } else if (this.timestamp > var1.timestamp) {
         return 1;
      } else if (this.counter < var1.counter) {
         return -1;
      } else {
         return this.counter > var1.counter ? 1 : 0;
      }
   }

   public int compareTo(Object var1) {
      try {
         return this.compare((MessageIDImpl)var1);
      } catch (ClassCastException var3) {
         return -1;
      }
   }

   public String toString() {
      return "<" + this.seed + "." + this.timestamp + "." + this.counter + ">";
   }

   public int hashCode() {
      return (int)((long)this.seed ^ this.timestamp ^ (long)this.counter);
   }

   public int differentiatedHashCode() {
      return this.hashCode() ^ this.differentiator;
   }
}
