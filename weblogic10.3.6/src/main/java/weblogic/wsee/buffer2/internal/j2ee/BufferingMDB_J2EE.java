package weblogic.wsee.buffer2.internal.j2ee;

import javax.jms.Message;
import javax.jms.Session;
import weblogic.wsee.buffer2.internal.common.BufferingMDB;

public final class BufferingMDB_J2EE extends BufferingMDB {
   protected boolean retrySupported() {
      return false;
   }

   public void setSession(Session var1) {
      throw new RuntimeException("WebLogic Server Extension method, not implemented for generic J2EE use.");
   }

   protected void setRetryDelay(Message var1, String var2, long var3) {
      throw new RuntimeException("WebLogic Server Extension method, not implemented for generic J2EE use.");
   }
}
