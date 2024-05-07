package weblogic.logging.jms;

import java.util.logging.LogRecord;
import javax.transaction.xa.Xid;

public abstract class JMSMessageLogRecord extends LogRecord {
   protected static final int PRODUCED = 0;
   protected static final int CONSUMED = 1;
   protected static final int EXPIRED = 2;
   protected static final int ADDED = 3;
   protected static final int RETRYEXCEEDED = 4;
   protected static final int ADMINPRODUCED = 5;
   protected static final int ADMINDELETED = 6;
   protected static final int REMOVED = 7;
   protected static final int CONSUMERCREATE = 8;
   protected static final int CONSUMERDESTROY = 9;
   protected static final int STORED = 10;
   protected static final int FORWARDED = 11;
   protected static final String[] JMSMessageStates = new String[]{"Produced", "Consumed", "Expired", "Added", "Retry exceeded", "Admin-produced", "Admin-deleted", "Removed", "ConsumerCreate", "ConsumerDestroy", "Stored", "Forwarded"};
   private Xid transactionId;
   private String diagCtxId;
   private String jmsDestinationName;
   private String jmsMessageId;
   private String jmsCorrelationId;
   private String user;
   private long eventTimeMillisStamp;
   private long eventTimeNanoStamp;
   private String subscriptionName;

   public JMSMessageLogRecord(long var1, long var3, String var5, String var6, String var7, String var8, String var9, String var10, Xid var11) {
      super(JMSMessageLevel.PERSISTENT_LEVEL, var5);
      this.eventTimeMillisStamp = var1;
      this.eventTimeNanoStamp = var3;
      this.jmsDestinationName = var6;
      this.jmsMessageId = var7;
      this.jmsCorrelationId = var8;
      this.user = var9;
      this.subscriptionName = var10;
      this.transactionId = var11;
   }

   public String getTransactionId() {
      return this.transactionId != null ? this.transactionId.toString() : null;
   }

   public long getEventTimeMillisStamp() {
      return this.eventTimeMillisStamp;
   }

   public long getEventTimeNanoStamp() {
      return this.eventTimeNanoStamp;
   }

   public String getDiagnosticContextId() {
      return this.diagCtxId;
   }

   public String getJMSDestinationName() {
      return this.jmsDestinationName;
   }

   public String getJMSMessageId() {
      return this.jmsMessageId;
   }

   public String getJMSCorrelationId() {
      return this.jmsCorrelationId;
   }

   public abstract String getJMSMessageState();

   public String getUser() {
      return this.user;
   }

   public String getDurableSubscriber() {
      return this.subscriptionName;
   }
}
