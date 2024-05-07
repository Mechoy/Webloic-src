package weblogic.jms.extensions;

import javax.jms.Destination;
import weblogic.jms.common.JMSException;

public final class DataOverrunException extends JMSException {
   static final long serialVersionUID = 6488522262679030149L;
   private String messageId;
   private String correlationId;
   private Destination destination;

   public DataOverrunException(String var1, String var2, String var3, String var4, Destination var5) {
      super(var1, var2);
      this.messageId = var3;
      this.correlationId = var4;
      this.destination = var5;
   }

   public DataOverrunException(String var1, String var2, String var3, Destination var4) {
      super(var1);
      this.messageId = var2;
      this.correlationId = var3;
      this.destination = var4;
   }

   public String getJMSMessageId() {
      return this.messageId;
   }

   public String getJMSCorrelationId() {
      return this.correlationId;
   }

   public Destination getJMSDestination() {
      return this.destination;
   }

   public boolean isInformational() {
      return true;
   }
}
