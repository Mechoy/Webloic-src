package weblogic.jms.backend;

import javax.jms.JMSException;
import weblogic.jms.dd.DDHandler;

public final class BEUOOQueueState extends BEUOOState {
   public BEUOOQueueState(BEDestinationImpl var1, DDHandler var2) {
      super(var1, var2);

      try {
         BEDestinationImpl.addPropertyFlags(var1.getKernelDestination(), "Logging", 16);
      } catch (JMSException var4) {
         throw new AssertionError(var4);
      }
   }
}
