package weblogic.io.common.internal;

import java.io.IOException;

class WriteResponse {
   private int bufferWritten = -1;
   private boolean cancelled = false;
   private String error = "";
   private int writeBehind;
   private static final int CLIENT_TIMEOUT_MILLSECS = 120000;

   public WriteResponse(int var1) {
      this.writeBehind = var1;
   }

   public synchronized void waitAroundExactly(int var1) throws IOException {
      this.waitAround(var1 + this.writeBehind + 1);
   }

   public synchronized void waitAround(int var1) throws IOException {
      if (this.cancelled) {
         throw new IOException(this.error);
      } else {
         while(var1 - this.writeBehind - 1 > this.bufferWritten && !this.cancelled) {
            int var2 = this.bufferWritten;

            try {
               this.wait(120000L);
            } catch (InterruptedException var4) {
            }

            if (var2 == this.bufferWritten && !this.cancelled) {
               this.cancel("Timed out or interrupted waiting for write response");
            }
         }

         if (this.cancelled) {
            throw new IOException(this.error);
         }
      }
   }

   public synchronized void signal(int var1) {
      if (var1 > this.bufferWritten) {
         this.bufferWritten = var1;
         this.notify();
      }

   }

   public synchronized void cancel(String var1) {
      this.error = var1;
      this.cancelled = true;
      this.notify();
   }
}
