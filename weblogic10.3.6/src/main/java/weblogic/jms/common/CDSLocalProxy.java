package weblogic.jms.common;

import java.io.IOException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jms.dispatcher.DispatcherAdapter;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.CrossDomainManager;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.work.WorkManagerFactory;

public final class CDSLocalProxy implements Invocable, JMSPeerGoneListener, CDSListProvider {
   private static final CDSLocalProxy singleton = new CDSLocalProxy();
   private final HashMap listenerDispatcherMap = new HashMap();
   private transient int refCount;

   public static CDSLocalProxy getSingleton() {
      return singleton;
   }

   public void unregisterListener(CDSListListener var1) {
      WorkManagerFactory.getInstance().getSystem().schedule(new UnregisterListenerThread(var1));
   }

   private void removeFromLDList(CDSListListener var1, List var2) {
      ListenerDispatcher var3 = null;
      synchronized(var2) {
         ListIterator var5 = var2.listIterator();

         while(var5.hasNext()) {
            var3 = (ListenerDispatcher)var5.next();
            if (var3.getListener() == var1) {
               var5.remove();
               break;
            }
         }
      }

      if (var3 != null) {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("unregisterListener: " + var1 + ". Removing the correponding DD with JNDI name: " + var1.getJNDIName());
         }

         final JMSDispatcher var4 = var3.getDispatcher();
         JMSDispatcherManager.exportLocalDispatcher();
         DispatcherWrapper var13 = JMSDispatcherManager.getLocalDispatcherWrapper();
         final DDMembershipCancelRequest var6 = new DDMembershipCancelRequest(var1.getJNDIName(), var13);
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("unregisterListener: " + var1 + ". sending cancel request to remote side for " + var1.getJNDIName());
         }

         Object var7 = null;

         try {
            CrossDomainSecurityManager.doAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getSubjectFromListener(var1), new PrivilegedExceptionAction() {
               public Object run() throws javax.jms.JMSException {
                  var4.dispatchNoReply(var6);
                  return null;
               }
            });
         } catch (javax.jms.JMSException var9) {
            var7 = var9;
         } catch (NamingException var10) {
            var7 = var10;
         } catch (IOException var11) {
            var7 = var11;
         }

         if (var7 != null && JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Exception in dispatching DDMembershipCancelRequest for " + var1.getJNDIName(), (Throwable)var7);
         }

      }
   }

   public DDMemberInformation[] registerListener(final CDSListListener var1) throws javax.jms.JMSException {
      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         JMSDebug.JMSCDS.debug("registerListener(): Creating remote dispatcher for " + var1.getJNDIName());
      }

      Object var2 = null;
      JMSDispatcher var3 = null;
      javax.jms.JMSException var4 = null;

      try {
         final Context var5 = var1.getContext();
         var3 = (JMSDispatcher)CrossDomainSecurityManager.runAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getSubjectFromListener(var1), new PrivilegedExceptionAction() {
            public Object run() throws javax.jms.JMSException {
               return CDSLocalProxy.this.getRemoteDispatcher(var1, var5);
            }
         });
      } catch (PrivilegedActionException var14) {
         var2 = var14.getException();
         if (var2 instanceof javax.jms.JMSException) {
            var4 = (javax.jms.JMSException)var2;
         }
      } catch (NamingException var15) {
         var2 = var15;
      } catch (IOException var16) {
         var2 = var16;
      }

      if (var4 != null) {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Exception in getting remote dispatcher for registeration of DDMembership change for " + var1.getJNDIName(), var4);
         }

         throw var4;
      } else if (var2 != null) {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Failed to get the remote dispatcher for " + var1.getJNDIName(), (Throwable)var2);
         }

         throw new JMSException(((Exception)var2).getMessage(), (Throwable)var2);
      } else {
         ListenerDispatcher var17 = new ListenerDispatcher(var1, var3);
         JMSDispatcherManager.exportLocalDispatcher();
         DispatcherWrapper var6 = JMSDispatcherManager.getLocalDispatcherWrapper();
         final DDMembershipRequest var7 = new DDMembershipRequest(var1.getDistributedDestinationImpl().getName(), var1.getJNDIName(), var6);
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("registerListener(): Creating remote dispatcher for " + var1.getJNDIName());
         }

         DDMembershipResponse var8 = null;
         final JMSDispatcher var9 = var3;

         try {
            var8 = (DDMembershipResponse)CrossDomainSecurityManager.runAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getSubjectFromListener(var1), new PrivilegedExceptionAction() {
               public Object run() throws javax.jms.JMSException {
                  return var9.dispatchSyncNoTran(var7);
               }
            });
         } catch (PrivilegedActionException var11) {
            var2 = var11.getException();
         } catch (NamingException var12) {
            var2 = var12;
         } catch (IOException var13) {
            var2 = var13;
         }

         if (var2 != null) {
            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("Exception in registering listener for " + var1.getJNDIName(), (Throwable)var2);
            }

            throw new JMSException((Throwable)var2);
         } else {
            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("processDD(): Got back the DDMembershipResponse with " + var8.getDDMemberInformation());
            }

            this.addToLDMapList(var17);
            return var8.getDDMemberInformation();
         }
      }
   }

   private synchronized List getLDList(String var1) {
      return (List)this.listenerDispatcherMap.get(var1);
   }

   private void addToLDMapList(ListenerDispatcher var1) {
      String var2 = var1.getListener().getJNDIName();
      synchronized(this) {
         Object var4 = (List)this.listenerDispatcherMap.get(var2);
         if (var4 == null) {
            var4 = new LinkedList();
            this.listenerDispatcherMap.put(var2, var4);
         }

         synchronized(var4) {
            ((List)var4).add(var1);
         }

      }
   }

   private JMSDispatcher getRemoteDispatcher(CDSListListener var1, Context var2) throws javax.jms.JMSException {
      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         JMSDebug.JMSCDS.debug("CDSLocalProxy.getRemoteDispatcher is called. id = " + var1.getDistributedDestinationImpl().getDispatcherId().getDetail());
      }

      try {
         JMSDispatcher var3 = JMSDispatcherManager.dispatcherFindOrCreate(var2, var1.getDistributedDestinationImpl().getDispatcherId());
         var3.addDispatcherPeerGoneListener(this);
         return var3;
      } catch (DispatcherException var4) {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Exception in register Listener for " + var1.getJNDIName(), var4);
         }

         throw new JMSException(var4.getMessage(), var4);
      }
   }

   private void listChange(String var1, DispatcherWrapper var2, DDMemberInformation[] var3) {
      List var4 = this.getLDList(var1);
      if (var4 != null) {
         synchronized(var4) {
            ListIterator var6 = var4.listIterator();

            while(var6.hasNext()) {
               ListenerDispatcher var7 = (ListenerDispatcher)var6.next();
               CDSListListener var8 = var7.getListener();
               Dispatcher var9 = ((DispatcherAdapter)var7.getDispatcher()).getDelegate();
               if (var9.getId().equals(var2.getId()) && CrossDomainManager.getCrossDomainUtil().isSameDomain(var9, (weblogic.messaging.dispatcher.DispatcherWrapper)var2)) {
                  var8.listChange(var3);
               }
            }

         }
      }
   }

   private int processDDMembershipPushRequest(DDMembershipPushRequest var1) throws javax.jms.JMSException {
      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         JMSDebug.JMSCDS.debug("processDDMembershipPushRequest(): Informing the membership change locally for DD JNDIName " + var1.getDDJndiName());
      }

      this.listChange(var1.getDDJndiName(), var1.getDispatcherWrapper(), var1.getMemberList());
      return Integer.MAX_VALUE;
   }

   private int processDDMembershipCancelRequest(DDMembershipCancelRequest var1) {
      List var2 = this.getLDList(var1.getDDJndiName());
      if (var2 == null) {
         return Integer.MAX_VALUE;
      } else {
         synchronized(var2) {
            ListIterator var4 = var2.listIterator();

            while(var4.hasNext()) {
               ListenerDispatcher var5 = (ListenerDispatcher)var4.next();
               Dispatcher var6 = ((DispatcherAdapter)var5.getDispatcher()).getDelegate();
               DispatcherWrapper var7 = var1.getDispatcherWrapper();
               if (var5.getListener().getJNDIName().equals(var1.getDDJndiName()) && var6.getId().equals(var7.getId()) && CrossDomainManager.getCrossDomainUtil().isSameDomain(var6, (weblogic.messaging.dispatcher.DispatcherWrapper)var7)) {
                  var5.getListener().distributedDestinationGone(var1.getDispatcherWrapper().getId());
                  var4.remove();
               }
            }

            return Integer.MAX_VALUE;
         }
      }
   }

   public int invoke(Request var1) throws Throwable {
      DispatcherWrapper var2 = null;
      switch (var1.getMethodId()) {
         case 18711:
            DDMembershipPushRequest var3 = (DDMembershipPushRequest)var1;
            var2 = var3.getDispatcherWrapper();
            CrossDomainSecurityManager.getCrossDomainSecurityUtil().checkRole(var2.getRemoteDispatcher(), var1);
            return this.processDDMembershipPushRequest(var3);
         case 18967:
            DDMembershipCancelRequest var4 = (DDMembershipCancelRequest)var1;
            var2 = var4.getDispatcherWrapper();
            CrossDomainSecurityManager.getCrossDomainSecurityUtil().checkRole(var2.getRemoteDispatcher(), var1);
            return this.processDDMembershipCancelRequest(var4);
         default:
            throw new JMSException("No such method " + var1.getMethodId());
      }
   }

   public JMSID getJMSID() {
      return null;
   }

   public ID getId() {
      return null;
   }

   public InvocableMonitor getInvocableMonitor() {
      return null;
   }

   public int incrementRefCount() {
      return ++this.refCount;
   }

   public int decrementRefCount() {
      return --this.refCount;
   }

   public void dispatcherPeerGone(Exception var1, Dispatcher var2) {
      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         JMSDebug.JMSCDS.debug("CDSLocalProxy.jmsPeerGone()");
      }

      this.peerGone(var2);
   }

   private void peerGone(Dispatcher var1) {
      LinkedList var2 = new LinkedList();
      Iterator var3 = null;
      synchronized(this) {
         var3 = ((HashMap)this.listenerDispatcherMap.clone()).values().iterator();
      }

      while(var3.hasNext()) {
         List var4 = (List)var3.next();
         synchronized(var4) {
            ListIterator var6 = var4.listIterator();

            while(var6.hasNext()) {
               ListenerDispatcher var7 = (ListenerDispatcher)var6.next();
               Dispatcher var8 = ((DispatcherAdapter)var7.getDispatcher()).getDelegate();
               if (var8.getId().equals(var1.getId()) && CrossDomainManager.getCrossDomainUtil().isSameDomain(var8, var1)) {
                  if (JMSDebug.JMSCDS.isDebugEnabled()) {
                     JMSDebug.JMSCDS.debug("peerGone(): Listener " + var7.getListener() + " for DD with JNDI Name of " + var7.getListener().getJNDIName());
                  }

                  var6.remove();
                  var2.add(var7);
               }
            }
         }
      }

      Iterator var12 = var2.iterator();

      while(var12.hasNext()) {
         ListenerDispatcher var5 = (ListenerDispatcher)var12.next();
         var5.getListener().distributedDestinationGone(var1.getId());
         var12.remove();
      }

   }

   static {
      try {
         if (!KernelStatus.isServer()) {
            InvocableManagerDelegate.delegate.addManager(23, CDSRouter.getSingleton());
         }
      } catch (Exception var1) {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Failed to register CDSRouter with dispatcher manager. Exception = " + var1);
         }
      }

   }

   private class UnregisterListenerThread implements Runnable {
      CDSListListener listener;

      private UnregisterListenerThread(CDSListListener var2) {
         this.listener = var2;
      }

      public void run() {
         List var1 = CDSLocalProxy.this.getLDList(this.listener.getJNDIName());
         if (var1 != null) {
            CDSLocalProxy.this.removeFromLDList(this.listener, var1);
            synchronized(CDSLocalProxy.this) {
               synchronized(var1) {
                  if (var1.size() == 0) {
                     CDSLocalProxy.this.listenerDispatcherMap.remove(this.listener.getJNDIName());
                  }
               }
            }
         }

      }

      // $FF: synthetic method
      UnregisterListenerThread(CDSListListener var2, Object var3) {
         this(var2);
      }
   }

   private static final class ListenerDispatcher {
      private CDSListListener listener;
      private JMSDispatcher dispatcher;

      public ListenerDispatcher(CDSListListener var1, JMSDispatcher var2) {
         this.listener = var1;
         this.dispatcher = var2;
      }

      public CDSListListener getListener() {
         return this.listener;
      }

      public JMSDispatcher getDispatcher() {
         return this.dispatcher;
      }
   }
}
