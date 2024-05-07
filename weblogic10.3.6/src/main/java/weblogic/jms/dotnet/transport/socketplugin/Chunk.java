package weblogic.jms.dotnet.transport.socketplugin;

public class Chunk {
   private static final int CHUNK_SIZE = 4096;
   private static Chunk head;
   private boolean isFree;
   private Chunk next;
   private byte[] buf;
   private static int allocated;
   private static int freed;

   private Chunk(int var1) {
      this.buf = new byte[var1];
   }

   public byte[] getBuffer() {
      return this.buf;
   }

   public static synchronized Chunk alloc() {
      Chunk var0 = head;
      if (head != null) {
         head = head.next;
         var0.isFree = false;
         return var0;
      } else {
         ++allocated;
         System.out.println("Allocating new Chunk, alloc count=" + allocated + "free count=" + freed);
         return new Chunk(4096);
      }
   }

   private static synchronized void free(Chunk var0) {
      ++freed;
      if (var0.isFree) {
         throw new AssertionError();
      } else {
         var0.isFree = true;
         var0.next = head;
         head = var0;
      }
   }

   public void free() {
      free(this);
   }

   static {
      for(int var0 = 0; var0 < 10000; ++var0) {
         Chunk var1 = new Chunk(4096);
         free(var1);
      }

   }
}
