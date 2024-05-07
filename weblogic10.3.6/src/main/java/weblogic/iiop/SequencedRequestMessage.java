package weblogic.iiop;

import weblogic.rmi.extensions.RequestTimeoutException;

public abstract class SequencedRequestMessage extends Message {
   private Message reply;
   private Throwable t;
   protected int flags = 0;
   private static final int DEFAULT_TIMEOUT = Integer.getInteger("weblogic.iiop.requestTimeout", 0);
   private long timeout;

   public SequencedRequestMessage() {
      this.timeout = (long)DEFAULT_TIMEOUT;
   }

   public final void setTimeout(long var1) {
      if (var1 > 0L) {
         this.timeout = var1;
      }

   }

   public final Message getReply() {
      return this.reply;
   }

   public final int getFlags() {
      return this.flags;
   }

   public final synchronized void notify(Message var1) {
      this.reply = var1;
      this.notify();
   }

   public final synchronized void notify(Throwable var1) {
      this.t = var1;
      this.notify();
   }

   public final synchronized void waitForData() throws Throwable {
      while(this.noReply()) {
         try {
            this.wait(this.timeout);
            if (this.noReply()) {
               this.t = new RequestTimeoutException("Response timed out after: '" + this.timeout + "' milliseconds.");
               this.endPoint.removePendingResponse(this.request_id);
            }
         } catch (InterruptedException var2) {
         }
      }

      if (this.t != null) {
         throw this.t;
      }
   }

   public final synchronized boolean pollResponse() {
      return !this.noReply();
   }

   private final boolean noReply() {
      return this.reply == null && this.t == null;
   }

   public void close() {
      super.close();
      this.reply = null;
      this.t = null;
   }
}
