package weblogic.jms.multicast;

import java.io.IOException;
import java.util.HashMap;
import javax.jms.Destination;
import weblogic.jms.client.JMSSession;
import weblogic.jms.common.BufferDataInputStream;
import weblogic.jms.extensions.SequenceGapException;
import weblogic.utils.io.Chunk;

public final class JMSFragmentStash {
   private static final int INVALID_FRAGMENT_NUMBER = -1;
   private final JMSSession session;
   private long currentSeqNo;
   private int lastFragNum;
   private HashMap<Integer, Chunk> chunkMap = null;
   private int numFragmentsReceived;
   private final Destination destination;

   JMSFragmentStash(JMSSession var1, long var2, Destination var4) {
      this.session = var1;
      this.currentSeqNo = var2 - 1L;
      this.destination = var4;
   }

   Chunk processFragment(long var1, int var3, int var4, int var5, BufferDataInputStream var6, int var7) throws SequenceGapException, IOException {
      if (var1 < this.currentSeqNo) {
         return null;
      } else {
         if (var1 > this.currentSeqNo) {
            int var8 = (int)(var1 - this.currentSeqNo);
            if (var8 > 1) {
               this.session.onException(new SequenceGapException("Missing message(s)", this.destination, var8 - 1));
            }

            this.currentSeqNo = var1;
            this.lastFragNum = -1;
            this.chunkMap = null;
            this.numFragmentsReceived = 0;
         }

         Chunk var11;
         if (this.chunkMap == null || !this.chunkMap.containsKey(var5)) {
            var11 = Chunk.createOneSharedChunk(var6, var7);
            if (var5 + var7 >= var3) {
               this.lastFragNum = var4;
               if (this.lastFragNum == 0) {
                  return var11;
               }
            }

            if (this.chunkMap == null) {
               this.chunkMap = new HashMap();
            }

            this.chunkMap.put(var5, var11);
            ++this.numFragmentsReceived;
         }

         if (this.numFragmentsReceived != this.lastFragNum + 1) {
            return null;
         } else {
            Chunk var9;
            var11 = var9 = (Chunk)this.chunkMap.remove(0);

            for(int var10 = var11.buf.length; this.numFragmentsReceived > 1; --this.numFragmentsReceived) {
               var9.next = (Chunk)this.chunkMap.remove(var10);
               var9 = var9.next;
            }

            return var11;
         }
      }
   }
}
