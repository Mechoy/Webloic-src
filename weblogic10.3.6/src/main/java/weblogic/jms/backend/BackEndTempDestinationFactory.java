package weblogic.jms.backend;

import javax.jms.Destination;
import javax.jms.JMSException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.messaging.dispatcher.DispatcherId;

public final class BackEndTempDestinationFactory {
   private final BackEnd backEnd;

   BackEndTempDestinationFactory(BackEnd var1) {
      this.backEnd = var1;
   }

   public Destination createTempDestination(DispatcherId var1, JMSID var2, boolean var3, int var4, long var5, String var7) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("creating back-end Temp Destination with type" + var4);
      }

      BEDestinationImpl var8;
      if (var4 == 4) {
         var8 = this.backEnd.createTemporaryDestination(var1, "Queue", var2, var3, var5, var7);
      } else {
         var8 = this.backEnd.createTemporaryDestination(var1, "Topic", var2, var3, var5, var7);
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("done calling createTemporaryDestinaition");
      }

      return var8.getDestinationImpl();
   }
}
