package weblogic.jms.frontend;

import javax.jms.JMSException;
import javax.security.auth.login.LoginException;
import weblogic.jms.client.JMSConnection;
import weblogic.jms.common.JMSSecurityException;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.security.SimpleCallbackHandler;

public final class FEConnectionFactoryImpl implements FEConnectionFactoryRemote {
   private final transient FEConnectionFactory feConnectionFactory;

   public FEConnectionFactoryImpl(FEConnectionFactory var1) {
      this.feConnectionFactory = var1;
   }

   public JMSConnection connectionCreate(DispatcherWrapper var1, String var2, String var3) throws JMSException {
      if (!JMSSecurityHelper.authenticate(var2, var3)) {
         throw new JMSSecurityException("Authentication failure");
      } else {
         return this.connectionCreateInternal(var1, true);
      }
   }

   public JMSConnection connectionCreate(DispatcherWrapper var1) throws JMSException {
      return this.connectionCreateInternal(var1, true);
   }

   public JMSConnection connectionCreateRequest(FEConnectionCreateRequest var1) throws JMSException {
      try {
         if (var1.getUserName() == null || JMSSecurityHelper.getJMSSecurityHelper().getPrincipalAuthenticator().authenticate(new SimpleCallbackHandler(var1.getUserName(), var1.getPassword())) != null) {
            return this.connectionCreateInternal(var1.getDispatcherWrapper(), var1.getCreateXAConnection());
         }
      } catch (LoginException var3) {
         throw new JMSSecurityException("Authentication failure due to LoginException", var3);
      }

      throw new JMSSecurityException("Authentication failure");
   }

   private JMSConnection connectionCreateInternal(DispatcherWrapper var1, boolean var2) throws JMSException {
      return this.feConnectionFactory.connectionCreateInternal(var1, var2);
   }
}
