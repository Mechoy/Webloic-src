package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.jms.backend.BEConsumerImpl;
import weblogic.jms.backend.BEDestinationImpl;

public class JMSConsumerImplRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 != null && var1 instanceof BEConsumerImpl) {
         BEConsumerImpl var2 = (BEConsumerImpl)var1;
         BEDestinationImpl var3 = var2.getDestination();
         return new JMSBEConsumerLogEventInfoImpl(var2.getName(), var2.getSubscriptionName(), var3 == null ? null : var3.getName());
      } else {
         return null;
      }
   }
}
