package weblogic.upgrade.jms;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.JMSException;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSUtilities;

public class UpgradeConsumer implements Externalizable {
   static final long serialVersionUID = -1498135422281856478L;
   private static final byte EXTVERSION60 = 1;
   private static final byte EXTVERSIONMIN = 1;
   private static final byte EXTVERSION70 = 2;
   private static final byte EXTVERSION = 2;
   private static final byte EXTVERSIONMAX = 2;
   private static final int VERSION_MASK = 7;
   private JMSID consumerId;
   private JMSMessageId timestampId;
   private Integer durableSlot;
   private byte type;
   static final char SUBSCRIPTION_DELIMITER = '.';
   static final int CLOSE = 301;
   static final int SET_LISTENER = 302;
   static final int INCREMENT_WINDOW_CURRENT = 303;
   static final int IS_ACTIVE = 304;
   static final int RECEIVE = 305;
   static final String SYSTEM_DIST_SUBSCRIBER_CLIENT = "WeblogicJmsDistributedTopic";
   protected String selector;
   protected boolean noLocal;
   private String clientId;
   private String name;
   private String subscriptionName;
   private DestinationImpl storedTopic;
   private transient String queueName;
   static final byte PHYSICAL_SUBSCRIBER = 1;
   static final byte PROXY_SUBSCRIBER = 2;
   static final byte DISTRIBUTED_SUBSCRIBER = 4;
   static final byte SYSTEM_DIST_SUBSCRIBER = 8;

   void parseName() throws JMSException {
      int var1;
      if ((var1 = this.name.indexOf(46)) <= 0) {
         throw new weblogic.jms.common.JMSException("Error parsing durable subscriber: " + this.name);
      } else {
         this.clientId = this.name.substring(0, var1);
         this.subscriptionName = this.name.substring(var1 + 1, this.name.length());
      }
   }

   public void setId(JMSID var1) {
      this.consumerId = var1;
   }

   public JMSMessageId getTimestampId() {
      return this.timestampId;
   }

   public Integer getDurableSlot() {
      return this.durableSlot;
   }

   public String getSubscriptionName() {
      return this.subscriptionName;
   }

   public String getClientId() {
      return this.clientId;
   }

   public String getName() {
      return this.name;
   }

   public String getSelector() {
      return this.selector;
   }

   public DestinationImpl getStoredTopic() {
      return this.storedTopic;
   }

   boolean getNoLocal() {
      return this.noLocal;
   }

   String getQueueName() {
      return this.queueName;
   }

   void setQueueName(String var1) {
      this.queueName = var1;
   }

   public void writeExternal(ObjectOutput var1) {
      throw new AssertionError("writeExternal must not be called on upgrade objects");
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readByte();
      switch (7 & var2) {
         case 1:
         case 2:
            if (var1.readBoolean()) {
               this.name = var1.readUTF();
            } else {
               this.name = null;
            }

            this.subscriptionName = this.name;
            this.storedTopic = new DestinationImpl();
            this.storedTopic.readExternal(var1);
            this.timestampId = new JMSMessageId();
            this.timestampId.readExternal(var1);
            this.durableSlot = new Integer(var1.readInt());
            this.consumerId = new JMSID();
            this.consumerId.readExternal(var1);
            if (var1.readBoolean()) {
               this.selector = var1.readUTF();
            }

            this.noLocal = var1.readBoolean();
            if (var2 != 1) {
               this.type = var1.readByte();
            }

            return;
         default:
            throw JMSUtilities.versionIOException(var2, 1, 2);
      }
   }

   public JMSID getId() {
      return this.consumerId;
   }
}
