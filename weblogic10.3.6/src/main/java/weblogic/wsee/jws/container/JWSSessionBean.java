package weblogic.wsee.jws.container;

import javax.ejb.SessionContext;
import javax.ejb.Timer;
import weblogic.ejbgen.LocalMethod;
import weblogic.ejbgen.Constants.TransactionAttribute;
import weblogic.wsee.jws.conversation.ConversationTimeout;

public class JWSSessionBean {
   public void ejbCreate() {
   }

   public void ejbActivate() {
   }

   public void ejbRemove() {
   }

   public void ejbPassivate() {
   }

   public void setSessionContext(SessionContext var1) {
   }

   @LocalMethod(
      transactionAttribute = TransactionAttribute.REQUIRED
   )
   public void schedule(ConversationTimeout var1) {
      throw new UnsupportedOperationException();
   }

   public void ejbTimeout(Timer var1) {
      throw new UnsupportedOperationException();
   }
}
