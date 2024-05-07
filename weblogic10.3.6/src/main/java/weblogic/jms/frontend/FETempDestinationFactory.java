package weblogic.jms.frontend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.jms.Destination;
import javax.jms.JMSException;
import weblogic.jms.backend.BETempDestinationFactoryRemote;
import weblogic.jms.common.JMSID;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.rmi.extensions.server.RemoteWrapper;

public final class FETempDestinationFactory implements RemoteWrapper {
   static final long serialVersionUID = 599643165308006354L;
   private final BETempDestinationFactoryRemote factoryRemote;

   public FETempDestinationFactory(BETempDestinationFactoryRemote var1) {
      this.factoryRemote = var1;
   }

   public Destination createTempDestination(DispatcherId var1, JMSID var2, boolean var3, int var4, long var5, String var7) throws JMSException {
      try {
         return this.factoryRemote.createTempDestination(var1, var2, var3, var4, var5, var7);
      } catch (RemoteException var9) {
         throw new weblogic.jms.common.JMSException("Failed to create temporary destination", var9);
      }
   }

   public Remote getRemoteDelegate() {
      return this.factoryRemote;
   }
}
