package weblogic.logging.jms;

import javax.transaction.xa.Xid;

public class JMSMessageConsumerDestroyLogRecord extends JMSMessageLogRecord {
   public JMSMessageConsumerDestroyLogRecord(long var1, long var3, String var5, String var6, String var7) {
      super(var1, var3, (String)null, var5, (String)null, (String)null, var6, var7, (Xid)null);
   }

   public String getJMSMessageState() {
      return JMSMessageLogRecord.JMSMessageStates[9];
   }
}
