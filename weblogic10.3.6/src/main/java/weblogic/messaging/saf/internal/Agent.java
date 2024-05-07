package weblogic.messaging.saf.internal;

import javax.naming.NamingException;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.messaging.saf.SAFException;

public interface Agent {
   String getName();

   void init(SAFAgentMBean var1) throws SAFException, NamingException;

   void suspend(boolean var1);

   void resume() throws SAFException;
}
