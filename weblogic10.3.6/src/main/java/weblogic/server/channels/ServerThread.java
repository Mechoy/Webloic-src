package weblogic.server.channels;

import weblogic.kernel.AuditableThread;

public class ServerThread extends AuditableThread {
   private static int nexttid = 0;
   private final int tid = getNextTid();

   private static synchronized int getNextTid() {
      return ++nexttid;
   }

   public ServerThread(ThreadGroup var1, Runnable var2, String var3) {
      super(var1, var2, var3);
   }

   ServerThread(Runnable var1, String var2) {
      super(var1, var2);
   }

   public String getThreadID() {
      return String.valueOf(this.tid);
   }

   public int hashCode() {
      return this.tid;
   }
}
