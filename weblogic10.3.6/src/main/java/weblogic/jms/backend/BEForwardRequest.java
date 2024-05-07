package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.SecureRandom;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import javax.jms.JMSException;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSProducerSendResponse;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.utils.Hex;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedInputStream;
import weblogic.utils.io.ChunkedObjectInputStream;
import weblogic.utils.io.ChunkedObjectOutputStream;
import weblogic.utils.io.ChunkedOutputStream;

public final class BEForwardRequest extends Request implements Externalizable {
   static final long serialVersionUID = -3794283731017311093L;
   private static final int EXTVERSION = 1;
   private static final int EXTVERSION2 = 2;
   private static final int VERSION_MASK = 255;
   private BEProducerSendRequest[] requests;
   private int size;
   private transient int position;
   private byte[] signature;
   private transient byte[] signedData;
   private int securityMode;
   private byte[] signatureSecret;
   private long signedChecksum;
   private long calculatedChecksum;
   private static long requestNum = (new SecureRandom()).nextLong();
   String debugStr = null;

   public BEForwardRequest(JMSID var1, BEProducerSendRequest[] var2, byte[] var3) {
      super(var1, 17684);
      this.requests = var2;
      this.size = var2.length;
      this.signatureSecret = var3;
   }

   BEProducerSendRequest[] getRequests() {
      return this.requests;
   }

   BEProducerSendRequest getCurrentRequest() {
      return this.requests[this.position];
   }

   int getSize() {
      return this.size;
   }

   int getPosition() {
      return this.position;
   }

   void incrementPosition() {
      ++this.position;
   }

   public int remoteSignature() {
      return 35;
   }

   public Response createResponse() {
      return new JMSProducerSendResponse();
   }

   void setSecurityMode(int var1) {
      this.securityMode = var1;
   }

   int getSecurityMode() {
      return this.securityMode;
   }

   boolean verify(byte[] var1) {
      if (var1 != null && this.signature != null && this.signedData != null) {
         boolean var2 = JMSServerUtilities.verifySignature(this.signature, this.signedData, var1);
         boolean var3 = this.signedChecksum == this.calculatedChecksum;
         if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
            System.out.println("verify sig=" + var2);
            System.out.println("verify cksum=" + var3);
            this.dump(this.signedData, this.signature, this.signedChecksum, this.calculatedChecksum);
         }

         return var2 && var3;
      } else {
         return false;
      }
   }

   public BEForwardRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      this.debugStr = null;
      boolean var2 = false;
      byte var10;
      switch (this.securityMode) {
         case 11:
         case 12:
         case 13:
            var10 = 2;
            break;
         case 14:
            var10 = 1;
            break;
         default:
            throw new AssertionError();
      }

      var1.writeInt(var10);
      ChunkedObjectOutputStream var3 = (ChunkedObjectOutputStream)var1;
      int var4 = var3.getPosition();
      if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
         this.debugStr = this.debugStr + "\n BEForwardingConsumer.DD_FORWARDING_DEBUG writeExternal: coosStart = " + var4;
      }

      ChunkListReader var5 = new ChunkListReader(var3);
      super.writeExternal(var3);
      var3.writeInt(this.size);
      if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
         this.debugStr = this.debugStr + "\n writeExternal: before requests position = " + var3.getPosition();
      }

      int var6;
      for(var6 = 0; var6 < this.size; ++var6) {
         this.requests[var6].writeExternal(var3);
         if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
            this.debugStr = this.debugStr + "\n writeExternal: position(" + var6 + ") = " + var3.getPosition();
         }
      }

      if (var10 == 2) {
         var3.writeInt(this.securityMode);
         if (this.securityMode != 12) {
            synchronized(this.getClass()) {
               var3.writeLong((long)(requestNum++));
            }

            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               this.debugStr = this.debugStr + "\n writeExternal: security mode = " + this.securityMode + " requestNum =" + requestNum + " position = " + var3.getPosition();
            }

            var6 = var3.getPosition() - var4;
            this.calculatedChecksum = var5.getChecksum((long)var6);
            this.signedChecksum = this.calculatedChecksum;
            var3.writeLong(this.calculatedChecksum);
            var6 += 8;
            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               this.debugStr = this.debugStr + "\n writeExternal: len = " + var6 + " checksum =" + this.calculatedChecksum + " position = " + var3.getPosition();
            }

            byte[] var7 = var5.getSignedData(var6, this.securityMode);
            byte[] var8 = JMSServerUtilities.digest(var7, this.signatureSecret);
            var3.writeInt(var8.length);
            var3.write(var8);
            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               this.debugStr = this.debugStr + "\n writeExternal: after signature position = " + var3.getPosition();
               System.out.println(this.debugStr + "\n" + "writeExternal() len = " + var6 + " securityMode = " + this.securityMode + " version = " + var10);
               this.dump(var7, var8, this.signedChecksum, this.calculatedChecksum);
            }

         }
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.debugStr = null;
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1 && var3 != 2) {
         throw JMSUtilities.versionIOException(var3, 1, 2);
      } else {
         ChunkedObjectInputStream var4 = (ChunkedObjectInputStream)var1;
         int var5 = var4.pos();
         ChunkListReader var6 = new ChunkListReader(var4);
         if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
            this.debugStr = this.debugStr + "\n BEForwardingConsumer.DD_FORWARDING_DEBUG readExternal: coisStart = " + var5;
         }

         boolean var7 = false;
         if (!var4.isMarked() && var3 != 1) {
            var4.mark(Integer.MAX_VALUE);
            var7 = true;
         }

         super.readExternal(var4);
         this.size = var4.readInt();
         this.requests = new BEProducerSendRequest[this.size];
         if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
            this.debugStr = this.debugStr + "\n readExternal: before requests position = " + var4.pos();
         }

         for(int var8 = 0; var8 < this.size; ++var8) {
            this.requests[var8] = new BEProducerSendRequest();
            this.requests[var8].readExternal(var4);
            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               this.debugStr = this.debugStr + "\n readExternal: position(" + var8 + ") = " + var4.pos();
            }
         }

         this.securityMode = 14;
         if (var3 == 2) {
            this.securityMode = var4.readInt();
            if (this.securityMode == 15) {
               this.securityMode = 14;
            }

            int var10;
            if (this.securityMode != 12) {
               long var15 = var4.readLong();
               if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
                  this.debugStr = this.debugStr + "\n readExternal: security mode = " + this.securityMode + " requestNum =" + var15 + " position = " + var4.pos();
               }

               var10 = var4.pos() - var5;
               if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
                  try {
                     String var11 = (String)this.requests[0].getMessage().getObjectProperty("CORRUPT");
                     if (var11 != null) {
                        System.out.println("  **** INSTR CORRUPT " + var11);
                        if (var11.equals("first")) {
                           var6.corrupt(0);
                        } else if (var11.equals("middle")) {
                           var6.corrupt(var10 / 2);
                        } else if (var11.equals("last")) {
                           var6.corrupt(var10 + 4);
                        }
                     }
                  } catch (JMSException var14) {
                  }
               }

               this.calculatedChecksum = var6.getChecksum((long)var10);
               this.signedChecksum = var4.readLong();
               var10 += 8;
               if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
                  this.debugStr = this.debugStr + "\n readExternal: len = " + var10 + " calculatedChecksum =" + this.calculatedChecksum + " position = " + var4.pos();
               }

               this.signedData = var6.getSignedData(var10, this.securityMode);
               int var16 = var4.readInt();
               this.signature = new byte[var16];
               var4.readFully(this.signature);
               if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
                  this.debugStr = this.debugStr + "\n readExternal: after signature position = " + var4.pos();
                  System.out.println(this.debugStr);

                  try {
                     String var12 = (String)this.requests[0].getMessage().getObjectProperty("CORRUPT");
                     if (var12 != null && var12.equals("sig")) {
                        System.out.println("explicit test increments sig[0] on request=" + var15);
                        ++this.signature[0];
                     }
                  } catch (JMSException var13) {
                  }
               }
            }

            if (var7) {
               var10 = var4.pos() - var5;
               var4.reset();
               var4.skip((long)var10);
            }

         }
      }
   }

   private void dump(byte[] var1, byte[] var2, long var3, long var5) {
      System.out.println("\nsd=\n" + Hex.asHex(var1) + "\nsg=\n" + Hex.asHex(var2) + "\nck_signed=" + var3 + "\nck_calc=" + var5);
   }

   private class ChunkListReader {
      private Chunk head;
      private Chunk headMark;
      private int chunkPos;
      private int chunkPosMark;
      private static final int READ = 1;
      private static final int SKIP = 2;
      private static final int CHECKSUM = 3;
      private static final int CORRUPT = 4;

      ChunkListReader(ChunkedInputStream var2) {
         this.head = this.headMark = var2.getChunks();
         this.chunkPos = this.chunkPosMark = var2.getChunkPos();
      }

      ChunkListReader(ChunkedOutputStream var2) {
         this.head = this.headMark = var2.getCurrentChunk();
         this.chunkPos = this.chunkPosMark = var2.getChunkPos();
      }

      long getChecksum(long var1) {
         Adler32 var3 = new Adler32();
         long var4 = this.checksum(var3, var1);
         this.reset();
         if (var4 != var1) {
            throw new AssertionError(var4 + "," + var1);
         } else {
            return var3.getValue();
         }
      }

      byte[] getSignedData(int var1, int var2) {
         if (var2 == 13) {
            BEForwardRequest.this.signedData = new byte[var1];
            this.read(BEForwardRequest.this.signedData, 0, var1);
            this.reset();
            return BEForwardRequest.this.signedData;
         } else {
            StringBuilder var10000;
            BEForwardRequest var10002;
            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               var10000 = new StringBuilder();
               var10002 = BEForwardRequest.this;
               var10002.debugStr = var10000.append(var10002.debugStr).append("\n getSignedData: len = ").append(var1).toString();
            }

            short var3 = 128;
            BEForwardRequest.this.signedData = new byte[var3 * 2];
            this.read(BEForwardRequest.this.signedData, 0, Math.min(var3, var1));
            this.reset();
            if (var1 > var3) {
               this.skip((long)(var1 - var3));
            }

            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               var10000 = new StringBuilder();
               var10002 = BEForwardRequest.this;
               var10002.debugStr = var10000.append(var10002.debugStr).append("\n getSignedData: len = ").append(var1).toString();
            }

            this.read(BEForwardRequest.this.signedData, var3, Math.min(var3, var1));
            this.reset();
            return BEForwardRequest.this.signedData;
         }
      }

      void corrupt(int var1) {
         this.corruptInternal((long)var1);
         this.reset();
      }

      public void reset() {
         this.head = this.headMark;
         this.chunkPos = this.chunkPosMark;
      }

      private int read(byte[] var1, int var2, int var3) {
         return (int)this.scanOp(var1, var2, (long)var3, (Checksum)null, 1);
      }

      private long checksum(Checksum var1, long var2) {
         return this.scanOp((byte[])null, -1, var2, var1, 3);
      }

      private long skip(long var1) {
         return (long)((int)this.scanOp((byte[])null, -1, var1, (Checksum)null, 2));
      }

      private long corruptInternal(long var1) {
         return this.scanOp((byte[])null, -1, var1 + 1L, (Checksum)null, 4);
      }

      private long scanOp(byte[] var1, int var2, long var3, Checksum var5, int var6) {
         StringBuilder var10000;
         BEForwardRequest var10002;
         if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
            var10000 = new StringBuilder();
            var10002 = BEForwardRequest.this;
            var10002.debugStr = var10000.append(var10002.debugStr).append("\n scanOp: mode = ").append(var6).append(" len = ").append(var3).toString();
         }

         long var7;
         int var9;
         for(var7 = var3; var3 > 0L; var3 -= (long)var9) {
            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               var10000 = new StringBuilder();
               var10002 = BEForwardRequest.this;
               var10002.debugStr = var10000.append(var10002.debugStr).append("\n scanOp: chunkPos = ").append(this.chunkPos).append(" head.end = ").append(this.head.end).append(" head.next is null? ").append(this.head.next == null).append(" len = ").append(var3).toString();
            }

            if (this.chunkPos == this.head.end && this.head.next != null) {
               this.chunkPos = 0;
               this.head = this.head.next;
            }

            var9 = (int)Math.min((long)(this.head.end - this.chunkPos), var3);
            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               var10000 = new StringBuilder();
               var10002 = BEForwardRequest.this;
               var10002.debugStr = var10000.append(var10002.debugStr).append("\n scanOp: copyLen = ").append(var9).append(" head.end = ").append(this.head.end).append(" head.next is null? ").append(this.head.next == null).append(" len = ").append(var3).toString();
            }

            if (this.head.next == null && (long)var9 < var3) {
               var9 = (int)var3;
            }

            if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
               var10000 = new StringBuilder();
               var10002 = BEForwardRequest.this;
               var10002.debugStr = var10000.append(var10002.debugStr).append("\n scanOp: real copyLen = ").append(var9).toString();
            }

            switch (var6) {
               case 1:
                  System.arraycopy(this.head.buf, this.chunkPos, var1, var2, var9);
               case 2:
               default:
                  break;
               case 3:
                  var5.update(this.head.buf, this.chunkPos, var9);
                  break;
               case 4:
                  ++this.head.buf[this.chunkPos + var9 - 1];
            }

            this.chunkPos += var9;
            var2 += var9;
         }

         if (BEForwardingConsumer.DD_FORWARDING_DEBUG) {
            var10000 = new StringBuilder();
            var10002 = BEForwardRequest.this;
            var10002.debugStr = var10000.append(var10002.debugStr).append("\n scanOp: chunkPos = ").append(this.chunkPos).toString();
         }

         return var7;
      }
   }
}
