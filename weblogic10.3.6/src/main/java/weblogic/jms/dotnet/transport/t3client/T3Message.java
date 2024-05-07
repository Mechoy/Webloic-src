package weblogic.jms.dotnet.transport.t3client;

import java.io.IOException;
import java.io.InputStream;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedDataInputStream;

public class T3Message {
   private T3Header hdr;
   private T3Abbrev[] abbrevs;
   private MarshalReaderImpl payload;
   private byte[] abbrevAsByte;
   private static final boolean DEBUG_READ_ABBREVS = false;

   private T3Message() {
   }

   T3Message(T3Header var1, T3Abbrev[] var2) {
      this.hdr = var1;
      this.abbrevs = var2;
   }

   void cleanup() {
      if (this.payload != null) {
         this.payload.internalClose();
      }

   }

   T3Header getMessageHeader() {
      return this.hdr;
   }

   MarshalReaderImpl getPayload() {
      return this.payload;
   }

   T3Abbrev[] getAbbrevs() {
      return this.abbrevs;
   }

   int getBodyLength() {
      return this.hdr.getOffset() - T3.PROTOCOL_HDR_SIZE;
   }

   void write(MarshalWriterImpl var1) throws Exception {
      int var2 = this.abbrevs != null ? this.abbrevs.length : 0;
      int var3 = 0;
      int var5;
      if (this.abbrevs != null) {
         var3 = T3.getLengthNumBytes(this.abbrevs.length);
         T3Abbrev[] var4 = this.abbrevs;
         var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            T3Abbrev var7 = var4[var6];
            var3 += var7.size();
         }
      }

      this.hdr.setMessageLength(var1.getPosition() + var3);
      this.hdr.setOffset(var1.getPosition());
      int var8 = var1.getPosition();
      var1.setPosition(0);
      this.hdr.write(var1);
      var1.setPosition(var8);
      T3.writeLength(var1, var2);
      if (this.abbrevs != null) {
         for(var5 = 0; var5 < this.abbrevs.length; ++var5) {
            this.abbrevs[var5].write(var1);
         }
      }

   }

   static T3Message readT3Message(InputStream var0, Transport var1) throws Exception {
      T3Message var2 = new T3Message();
      Chunk var3 = Chunk.getChunk();
      var3.end = T3.PROTOCOL_HDR_SIZE;

      for(int var4 = 0; var4 < T3.PROTOCOL_HDR_SIZE; var4 += var0.read(var3.buf, var4, T3.PROTOCOL_HDR_SIZE - var4)) {
      }

      ChunkedDataInputStream var5 = new ChunkedDataInputStream(var3, 0);
      var5.mark(0);
      MarshalReaderImpl var6 = new MarshalReaderImpl(var5, var1);
      var2.hdr = new T3Header(var6);
      int var7 = var2.hdr.getMessageLength();
      var7 -= T3.PROTOCOL_HDR_SIZE;
      if (var7 != Chunk.chunk(var3, var0, var7)) {
         throw new IOException("EOF reached");
      } else {
         var5.reset();
         var5.skipBytes(T3.PROTOCOL_HDR_SIZE);
         var2.payload = var6;
         return var2;
      }
   }

   void print() {
      System.out.println(this.hdr);
      System.out.println("{  ***** abbrev *****");
      int var1;
      if (this.abbrevAsByte != null) {
         for(var1 = 1; var1 <= this.abbrevAsByte.length; ++var1) {
            System.out.print("" + this.abbrevAsByte[var1 - 1] + " ");
            if (var1 % 10 == 0) {
               System.out.print("\n");
            }
         }

         System.out.println("");
      } else if (this.abbrevs != null) {
         for(var1 = 0; var1 < this.abbrevs.length; ++var1) {
            System.out.println("id: " + this.abbrevs[var1].getId());
            byte[] var2 = this.abbrevs[var1].getContent();
            if (var2 != null && var2.length != 0) {
               for(int var3 = 1; var3 <= var2.length; ++var3) {
                  System.out.print("" + var2[var3 - 1] + " ");
                  if (var3 % 10 == 0) {
                     System.out.print("\n");
                  }
               }

               System.out.println("");
            }
         }

         System.out.println("");
      }

      System.out.println("}\n");
   }

   void printAbbrevs() throws Exception {
   }

   private static byte readByte(InputStream var0) throws IOException {
      int var1 = var0.read();
      if (var1 < 0) {
         throw new IOException("EOF");
      } else {
         return (byte)var1;
      }
   }

   private static int readLength(InputStream var0) throws Exception {
      byte var1 = readByte(var0);
      int var2 = var1 & 255;
      if (var2 < 254) {
         return var2;
      } else {
         int var3;
         int var4;
         if (var2 == 254) {
            var3 = readByte(var0) & 255;
            var4 = readByte(var0) & 255;
            return (var3 << 8) + var4;
         } else {
            var3 = readByte(var0) & 255;
            var4 = readByte(var0) & 255;
            int var5 = readByte(var0) & 255;
            int var6 = readByte(var0) & 255;
            return (var3 << 24) + (var4 << 16) + (var5 << 8) + (var6 << 0);
         }
      }
   }
}
