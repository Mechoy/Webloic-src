package weblogic.logging.jms;

import javax.transaction.xa.Xid;

public class JMSMessageAddLogRecord extends JMSMessageLogRecord {
   public JMSMessageAddLogRecord(long var1, long var3, String var5, String var6, String var7, String var8, String var9, String var10, Xid var11) {
      super(var1, var3, var5, var6, var7, var8, var9, var10, var11);
   }

   public String getJMSMessageState() {
      return JMSMessageLogRecord.JMSMessageStates[3];
   }
}
