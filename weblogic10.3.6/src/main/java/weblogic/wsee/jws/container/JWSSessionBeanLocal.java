package weblogic.wsee.jws.container;

import javax.ejb.EJBLocalObject;
import weblogic.wsee.jws.conversation.ConversationTimeout;

public interface JWSSessionBeanLocal extends EJBLocalObject {
   void schedule(ConversationTimeout var1);
}
