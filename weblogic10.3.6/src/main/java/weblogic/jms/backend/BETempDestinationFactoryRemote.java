package weblogic.jms.backend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.jms.Destination;
import javax.jms.JMSException;
import weblogic.jms.common.JMSID;
import weblogic.messaging.dispatcher.DispatcherId;

public interface BETempDestinationFactoryRemote extends Remote {
   Destination createTempDestination(DispatcherId var1, JMSID var2, boolean var3, int var4, long var5, String var7) throws RemoteException, JMSException;
}
