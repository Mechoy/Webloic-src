package weblogic.jms.common;

import java.io.IOException;
import java.rmi.Remote;
import javax.naming.NamingException;
import weblogic.jms.dispatcher.DispatcherAdapter;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherProxy;
import weblogic.messaging.dispatcher.DispatcherRemote;
import weblogic.messaging.dispatcher.DispatcherWrapperState;
import weblogic.messaging.dispatcher.Request;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.spi.EndPoint;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public final class ServerCrossDomainSecurityUtil implements CrossDomainSecurityUtil {
   private final AbstractSubject anonymous = SubjectManager.getSubjectManager().getAnonymousSubject();

   public AbstractSubject getSubjectFromListener(CDSListListener var1) throws NamingException, IOException {
      AbstractSubject var2 = null;

      try {
         var2 = var1.getForeignSubject();
      } catch (java.lang.IllegalStateException var4) {
      }

      if (var2 == null) {
         var2 = var1.getSubject();
      }

      String var3 = var1.getProviderURL();
      if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
         JMSDebug.JMSCrossDomainSecurity.debug("getSubjectFromListener() listener's url = " + var3 + " listener's subject = " + var2 + " isLocal  = " + var1.isLocal());
      }

      if (!var1.isLocal() && var3 != null && var3.trim().length() != 0) {
         if (var2 != null && this.isKernelIdentity((AuthenticatedSubject)var2) && this.isRemoteDomain(var1.getProviderURL())) {
            var2 = this.getRemoteSubjectFromCM(var1.getProviderURL());
         }

         if (var2 == null || this.isKernelIdentity((AuthenticatedSubject)var2)) {
            var2 = this.anonymous;
         }

         if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
            JMSDebug.JMSCrossDomainSecurity.debug("Final subject for URL " + var3 + " is " + var2);
         }

         return var2;
      } else {
         if (var2 == null) {
            var2 = CrossDomainSecurityManager.getCurrentSubject();
         }

         if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
            JMSDebug.JMSCrossDomainSecurity.debug("getSubjectFromListener() final subject = " + var2);
         }

         return var2;
      }
   }

   public AbstractSubject getRemoteSubject(String var1, AbstractSubject var2) {
      AbstractSubject var3 = null;

      try {
         var3 = this.getRemoteSubjectFromCM(var1);
         if (var3 != null) {
            return var3;
         }

         if (CrossDomainSecurityManager.getCrossDomainSecurityUtil().isRemoteDomain(var1)) {
            return this.anonymous;
         }
      } catch (IOException var5) {
         return this.anonymous;
      } catch (NamingException var6) {
         return this.anonymous;
      }

      if (var2 != null) {
         var3 = var2;
      } else {
         var3 = CrossDomainSecurityManager.getCurrentSubject();
      }

      if (var3 == null || this.isKernelIdentity((AuthenticatedSubject)var3)) {
         var3 = this.anonymous;
      }

      return var3;
   }

   private AbstractSubject getRemoteSubjectFromCM(String var1) throws IOException, NamingException {
      return var1 != null && var1.trim().length() != 0 ? RemoteDomainSecurityHelper.getSubject(var1) : null;
   }

   public AbstractSubject getRemoteSubject(JMSDispatcher var1) throws JMSException {
      return this.getRemoteSubject(var1, CrossDomainSecurityManager.getCurrentSubject(), false);
   }

   public AbstractSubject getRemoteSubject(JMSDispatcher var1, AbstractSubject var2, boolean var3) throws JMSException {
      AbstractSubject var4 = CrossDomainSecurityManager.getCurrentSubject();
      if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
         JMSDebug.JMSCrossDomainSecurity.debug("getRemoteSubject from dispatcher: isLocal = " + var1.isLocal() + " currentSubject = " + var4 + " suggestedSubject = " + var2 + " dispatcher " + var1 + " suggestedSubjectGoodForRemoteDomain = " + var3);
      }

      if (var1.isLocal()) {
         return var2 != null ? var2 : var4;
      } else {
         Dispatcher var5 = ((DispatcherAdapter)var1).getDelegate();
         Remote var6 = RemoteHelper.getRemote(var5);
         if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
            JMSDebug.JMSCrossDomainSecurity.debug("Remote = " + var6);
         }

         if (var6 != null && (var6 instanceof DispatcherProxy || var5 instanceof DispatcherWrapperState)) {
            Object var7 = null;
            if (var6 instanceof DispatcherProxy) {
               if (var2 != null && (var3 || !RemoteDomainSecurityHelper.isRemoteDomain((EndPoint)((DispatcherProxy)var6).getRJVM()))) {
                  var7 = var2;
               }

               if (var7 == null || this.isKernelIdentity((AuthenticatedSubject)var7)) {
                  try {
                     var7 = RemoteDomainSecurityHelper.getSubject((EndPoint)((DispatcherProxy)var6).getRJVM());
                  } catch (IOException var9) {
                     throw new JMSException(var9);
                  }
               }
            } else {
               var7 = var2;
            }

            if (var7 == null || this.isKernelIdentity((AuthenticatedSubject)var7)) {
               var7 = this.anonymous;
            }

            if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
               JMSDebug.JMSCrossDomainSecurity.debug("final subject = " + var7);
            }

            return (AbstractSubject)var7;
         } else {
            return var2 != null ? var2 : var4;
         }
      }
   }

   private void checkRole(DispatcherProxy var1, Request var2) throws JMSException {
      if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
         int var3 = var2.getMethodId();
         if (var3 == 18455 || var3 == 18711 || var3 == 18967 || var3 == 4 || var3 == 15616) {
            String var4 = null;
            switch (var2.getMethodId()) {
               case 4:
                  var4 = "JMSSessionRequest:";
                  break;
               case 15616:
                  var4 = "JMSPushMessageRequest:";
                  break;
               case 18455:
                  var4 = "DDMembershipRequest:";
                  break;
               case 18711:
                  var4 = "DDMembershipPushRequest:";
                  break;
               case 18967:
                  var4 = "DDMembershipCancalRequest:";
            }

            if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
               JMSDebug.JMSCrossDomainSecurity.debug("Processing " + var4 + " dispatcherProxy = " + var1 + " isCollocatd = " + var2.isCollocated());
            }
         }
      }

      AbstractSubject var5 = CrossDomainSecurityManager.getCurrentSubject();
      int var6 = RemoteDomainSecurityHelper.acceptRemoteDomainCall(var1.getRJVM().getHostID(), (AuthenticatedSubject)var5);
      if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled() && JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
         JMSDebug.JMSCrossDomainSecurity.debug("Verifying subject = " + var5 + " acceptRemoteDomainCall()= " + var6);
      }

      if (var6 == 1) {
         throw new JMSException("User <" + var5 + "> does not have " + "permission for cross-domain communication");
      }
   }

   public void checkRole(JMSDispatcher var1, Request var2) throws JMSException {
      DispatcherProxy var3 = this.getDispatcherProxy(var1);
      if (var3 != null) {
         this.checkRole(var3, var2);
      }
   }

   public void checkRole(DispatcherRemote var1, Request var2) throws JMSException {
      if (!var2.isCollocated() && var1 instanceof DispatcherProxy) {
         this.checkRole((DispatcherProxy)var1, var2);
      }
   }

   public boolean isRemoteDomain(String var1) throws IOException {
      return RemoteDomainSecurityHelper.isRemoteDomain(var1);
   }

   public boolean isRemoteDomain(JMSDispatcher var1) throws IOException {
      DispatcherProxy var2 = this.getDispatcherProxy(var1);
      return var2 == null ? false : RemoteDomainSecurityHelper.isRemoteDomain((EndPoint)var2.getRJVM());
   }

   private DispatcherProxy getDispatcherProxy(JMSDispatcher var1) {
      if (var1.isLocal()) {
         return null;
      } else {
         Dispatcher var2 = ((DispatcherAdapter)var1).getDelegate();
         Remote var3 = RemoteHelper.getRemote(var2);
         return !(var3 instanceof DispatcherProxy) ? null : (DispatcherProxy)var3;
      }
   }

   public boolean isKernelIdentity(AbstractSubject var1) {
      if (!(var1 instanceof AuthenticatedSubject)) {
         return false;
      } else {
         return SecurityServiceManager.isKernelIdentity((AuthenticatedSubject)var1) || SecurityServiceManager.isServerIdentity((AuthenticatedSubject)var1);
      }
   }

   public boolean ifRemoteSubjectExists(String var1) {
      AuthenticatedSubject var2 = null;

      try {
         if (var1 != null && var1.trim().length() > 0) {
            var2 = RemoteDomainSecurityHelper.getSubject(var1);
         }
      } catch (Exception var4) {
      }

      return var2 != null;
   }
}
