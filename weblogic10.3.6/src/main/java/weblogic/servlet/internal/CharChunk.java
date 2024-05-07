package weblogic.servlet.internal;

import java.io.IOException;
import java.io.Reader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import weblogic.utils.collections.PartitionedStackPool;
import weblogic.utils.collections.Pool;

public class CharChunk {
   private static final int CHUNK_SIZE = getIntegerProperty("weblogic.servlet.internal.CharChunk.ChunkSize", 8192);
   private static final int TOTAL_CHUNKS = getIntegerProperty("weblogic.servlet.internal.CharChunk.TotalChunks", 256);
   private static final int CHUNK_PARTITIONS = getIntegerProperty("weblogic.servlet.internal.CharChunk.ChunkPartitions", 4);
   private static final Pool chunkPool;
   public final char[] buf;
   public int end;
   public CharChunk next;

   public static CharChunk getChunk() {
      CharChunk var0 = null;
      var0 = (CharChunk)chunkPool.remove();
      return var0 == null ? new CharChunk() : var0;
   }

   public static void releaseChunk(CharChunk var0) {
      var0.end = 0;
      var0.next = null;
      chunkPool.add(var0);
   }

   public static void releaseChunks(CharChunk var0) {
      while(var0 != null) {
         CharChunk var1 = var0.next;
         releaseChunk(var0);
         var0 = var1;
      }

   }

   public static CharChunk tail(CharChunk var0) {
      CharChunk var1;
      for(var1 = var0; var1.next != null; var1 = var1.next) {
      }

      return var1;
   }

   public static CharChunk ensureCapacity(CharChunk var0) {
      if (CHUNK_SIZE == var0.end) {
         var0.next = getChunk();
         return var0.next;
      } else {
         return var0;
      }
   }

   public static int chunkFully(CharChunk var0, Reader var1) throws IOException {
      CharChunk var2 = tail(var0);
      int var3 = 0;

      while(true) {
         var2 = ensureCapacity(var2);
         int var4 = var1.read(var2.buf, var2.end, CHUNK_SIZE - var2.end);
         if (var4 == -1) {
            return var3;
         }

         var2.end += var4;
         var3 += var4;
      }
   }

   public static int chunk(CharChunk var0, Reader var1, int var2) throws IOException {
      CharChunk var3 = tail(var0);

      int var4;
      int var6;
      for(var4 = var2; var2 > 0; var2 -= var6) {
         var3 = ensureCapacity(var3);
         int var5 = Math.min(var2, CHUNK_SIZE - var3.end);
         var6 = var1.read(var3.buf, var3.end, var5);
         if (var6 == -1) {
            return var4 - var2;
         }

         var3.end += var6;
      }

      return var4;
   }

   private static int getIntegerProperty(final String var0, int var1) {
      Integer var2 = (Integer)AccessController.doPrivileged(new PrivilegedAction<Integer>() {
         public Integer run() {
            return Integer.getInteger(var0);
         }
      });
      return var2 == null ? var1 : var2;
   }

   private CharChunk() {
      this.buf = new char[CHUNK_SIZE];
      this.end = 0;
      this.next = null;
   }

   static {
      chunkPool = new PartitionedStackPool(TOTAL_CHUNKS, CHUNK_PARTITIONS);
   }
}
