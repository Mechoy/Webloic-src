package weblogic.jms.common;

import java.io.IOException;
import javax.naming.NamingException;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.messaging.dispatcher.DispatcherRemote;
import weblogic.messaging.dispatcher.Request;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public final class ClientCrossDomainSecurityUtil implements CrossDomainSecurityUtil {
   public AbstractSubject getSubjectFromListener(CDSListListener var1) throws NamingException, IOException {
      AbstractSubject var2 = var1.getSubject();
      if (var2 == null) {
         var2 = SubjectManager.getSubjectManager().getAnonymousSubject();
      }

      return var2;
   }

   public AbstractSubject getRemoteSubject(String var1, AbstractSubject var2) {
      return var2 != null ? var2 : SubjectManager.getSubjectManager().getAnonymousSubject();
   }

   public void checkRole(DispatcherRemote var1, Request var2) throws javax.jms.JMSException {
   }

   public void checkRole(JMSDispatcher var1, Request var2) throws javax.jms.JMSException {
   }

   public AbstractSubject getRemoteSubject(JMSDispatcher var1) throws javax.jms.JMSException {
      return SubjectManager.getSubjectManager().getAnonymousSubject();
   }

   public AbstractSubject getRemoteSubject(JMSDispatcher var1, AbstractSubject var2, boolean var3) throws javax.jms.JMSException {
      return var2 != null ? var2 : SubjectManager.getSubjectManager().getAnonymousSubject();
   }

   public boolean isRemoteDomain(String var1) throws IOException {
      return false;
   }

   public boolean isRemoteDomain(JMSDispatcher var1) throws IOException {
      return false;
   }

   public boolean ifRemoteSubjectExists(String var1) {
      return false;
   }

   public boolean isKernelIdentity(AbstractSubject var1) {
      return false;
   }
}
