package weblogic.jms.common;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedDataInputStream;

public class PayloadFactoryImpl {
   static boolean REPLACE_STOLEN_CHUNKS = true;
   static int SHIFT_REPLACEMENT_STOLEN_CHUNK_COUNT = 0;
   static int CHUNK_LINK_THRESHOLD;

   static BufferOutputStream createOutputStream() {
      return new BufferOutputStreamChunked((ObjectIOBypass)null);
   }

   public static Payload createPayload(InputStream var0) throws IOException {
      int var1 = ((DataInput)var0).readInt();
      if (var1 > CHUNK_LINK_THRESHOLD) {
         ChunkedDataInputStream var2;
         if (var0 instanceof ChunkedDataInputStream) {
            var2 = (ChunkedDataInputStream)var0;
            return new PayloadChunkBase(PayloadChunkBase.linkAndCopyChunksWithoutWastedMemory(var2, var1));
         }

         if (var0 instanceof BufferInputStreamChunked) {
            var2 = ((BufferInputStreamChunked)var0).getInternalCDIS();
            return new PayloadChunkBase(PayloadChunkBase.linkAndCopyChunksWithoutWastedMemory(var2, var1));
         }
      }

      return copyPayloadFromStream(var0, var1);
   }

   static final Payload copyPayloadFromStream(InputStream var0, int var1) throws IOException {
      int var2 = Math.min(var1, Chunk.CHUNK_SIZE * 2);
      var1 -= var2;
      Chunk var3 = Chunk.createOneSharedChunk(var0, var2);

      for(Chunk var4 = var3; var1 > 0; var4 = var4.next) {
         var2 = Math.min(var1, Chunk.CHUNK_SIZE);
         var1 -= var2;
         var4.next = Chunk.createOneSharedChunk(var0, var2);
      }

      return new PayloadChunkBase(var3);
   }

   static final Payload convertObjectToPayload(Object var0) throws IOException {
      BufferOutputStream var1 = createOutputStream();
      ObjectOutputStream var2 = new ObjectOutputStream(var1);
      var2.writeObject(var0);
      var1.flush();
      return var1.moveToPayload();
   }

   static {
      CHUNK_LINK_THRESHOLD = Chunk.CHUNK_SIZE * 9;

      Integer var1;
      int var2;
      String var3;
      try {
         String var0 = "weblogic.jms.CHUNK_LINK_THRESHOLD";
         var1 = Integer.getInteger(var0);
         if (var1 != null && CHUNK_LINK_THRESHOLD != (var2 = var1)) {
            CHUNK_LINK_THRESHOLD = var2;
            var3 = var0 + "=" + CHUNK_LINK_THRESHOLD;
            System.out.println(var3);
         }
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

      try {
         String var7 = System.getProperty("weblogic.jms.addChunkPool");
         if (var7 != null && "noPool".equals(var7)) {
            var3 = "PayloadFactoryImpl weblogic.jms.addChunkPool";
            REPLACE_STOLEN_CHUNKS = false;
            var3 = var3 + " do NOT proactively refill chunk pool!!";
            System.out.println(var3);
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      try {
         var1 = Integer.getInteger("weblogic.jms.ReplaceChunkPoolShift");
         if (var1 != null && SHIFT_REPLACEMENT_STOLEN_CHUNK_COUNT != (var2 = var1)) {
            SHIFT_REPLACEMENT_STOLEN_CHUNK_COUNT = var2;
            var3 = "PayloadFactoryImpl weblogic.jms.ReplaceChunkPoolShift";
            var3 = var3 + "= " + SHIFT_REPLACEMENT_STOLEN_CHUNK_COUNT + " value";
            System.out.println(var3);
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }
}
