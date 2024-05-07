package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.flightrecorder.event.JMSBEConsumerLogEventInfo;

public class JMSBEConsumerLogEventInfoImpl implements JMSBEConsumerLogEventInfo {
   private String consumerLifecycle = null;
   private String consumer = null;
   private String subscription = null;
   private String destination = null;

   public JMSBEConsumerLogEventInfoImpl(String var1, String var2, String var3) {
      this.consumer = var1;
      this.subscription = var2;
      this.destination = var3;
   }

   public String getConsumerLifecycle() {
      return this.consumerLifecycle;
   }

   public void setConsumerLifecycle(String var1) {
      this.consumerLifecycle = var1;
   }

   public String getConsumer() {
      return this.consumer;
   }

   public void setConsumer(String var1) {
      this.consumer = var1;
   }

   public String getSubscription() {
      return this.subscription;
   }

   public void setSubscription(String var1) {
      this.subscription = var1;
   }

   public String getDestination() {
      return this.destination;
   }

   public void setDestination(String var1) {
      this.destination = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.consumerLifecycle != null) {
         var1.append("consumerLifecycle=");
         var1.append(this.consumerLifecycle);
         var1.append(",");
      }

      var1.append("consumer=");
      var1.append(this.consumer);
      var1.append("subscription=");
      var1.append(this.subscription);
      var1.append("destination=");
      var1.append(this.destination);
      return var1.toString();
   }
}
