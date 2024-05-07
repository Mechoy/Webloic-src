package weblogic.jms.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.JMSIDFactories;
import weblogic.messaging.common.MessageIDFactory;
import weblogic.messaging.common.MessageIDImpl;

public final class JMSMessageId extends MessageIDImpl {
   static final long serialVersionUID = 3784286757441851850L;
   private static MessageIDFactory messageIDFactory;

   public static JMSMessageId create() {
      return new JMSMessageId(true);
   }

   private JMSMessageId(boolean var1) {
      super(messageIDFactory);
   }

   public JMSMessageId(int var1, long var2, int var4) {
      super(var1, var2, var4);
   }

   public JMSMessageId(JMSMessageId var1, int var2) {
      super(var1, var2);
   }

   public int getSeed() {
      return this.seed;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public int getCounter() {
      return this.counter;
   }

   public boolean equals(Object var1) {
      return !(var1 instanceof JMSMessageId) ? false : super.equals(var1);
   }

   public JMSMessageId() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
   }

   static {
      messageIDFactory = JMSIDFactories.messageIDFactory;
   }
}
