package weblogic.logging.jms;

import javax.transaction.xa.Xid;

public class JMSMessageConsumerCreationLogRecord extends JMSMessageLogRecord {
   private String selector = null;

   public JMSMessageConsumerCreationLogRecord(long var1, long var3, String var5, String var6, String var7, String var8) {
      super(var1, var3, (String)null, var5, (String)null, (String)null, var6, var7, (Xid)null);
      this.selector = var8;
   }

   public String getJMSMessageState() {
      return JMSMessageLogRecord.JMSMessageStates[8];
   }

   public String getSelector() {
      return this.selector;
   }
}
