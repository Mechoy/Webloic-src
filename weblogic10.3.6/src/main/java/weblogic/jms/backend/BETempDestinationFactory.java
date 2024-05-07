package weblogic.jms.backend;

import javax.jms.Destination;
import javax.jms.JMSException;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSID;
import weblogic.jms.frontend.FETempDestinationFactory;
import weblogic.messaging.dispatcher.DispatcherId;

public final class BETempDestinationFactory implements BETempDestinationFactoryRemote {
   private final FETempDestinationFactory factoryWrapper = new FETempDestinationFactory(this);

   public Destination createTempDestination(DispatcherId var1, JMSID var2, boolean var3, int var4, long var5, String var7) throws JMSException {
      JMSService.getJMSService().checkShutdown();
      BackEndTempDestinationFactory var8 = JMSService.getJMSService().getBEDeployer().nextFactory();
      if (var8 == null) {
         throw new weblogic.jms.common.JMSException("No server configured for temporary destinations");
      } else {
         return var8.createTempDestination(var1, var2, var3, var4, var5, var7);
      }
   }

   public FETempDestinationFactory getFactoryWrapper() {
      return this.factoryWrapper;
   }
}
