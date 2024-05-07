package weblogic.jms.deployer;

import java.rmi.NoSuchObjectException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.naming.NamingException;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.backend.BETempDestinationFactory;
import weblogic.jms.backend.BackEnd;
import weblogic.jms.backend.BackEndTempDestinationFactory;
import weblogic.jms.common.CDS;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class BEDeployer {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private BETempDestinationFactory beTempDestinationFactory;
   private static final String JMS_TEMP_DESTINATION_FTY_JNDI = "weblogic.jms.TempDestinationFactory";
   private Object shutdownLock;
   private final HashMap backEnds = new HashMap();
   private final HashMap backEndByIds = new HashMap();
   private long backEndsHighCount;
   private long backEndsTotalCount;
   private ArrayList tempDestFactories = new ArrayList();
   private int currentFactoryIndex = -1;
   private JMSService jmsService = null;

   public BEDeployer(JMSService var1) {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Constructing JMS BEDeployer");
      }

      this.jmsService = var1;
      this.beTempDestinationFactory = new BETempDestinationFactory();
      this.shutdownLock = this.backEnds;
   }

   public Object getShutdownLock() {
      synchronized(this.shutdownLock) {
         return this.shutdownLock;
      }
   }

   public long getBackEndsHighCount() {
      synchronized(this.shutdownLock) {
         return this.backEndsHighCount;
      }
   }

   public long getBackEndsTotalCount() {
      synchronized(this.shutdownLock) {
         return this.backEndsTotalCount;
      }
   }

   public void addBackEnd(BackEnd var1) throws JMSException {
      synchronized(this.shutdownLock) {
         this.jmsService.checkShutdown();
         if (this.backEnds.put(var1.getName(), var1) == null) {
            this.backEndsHighCount = Math.max((long)this.backEnds.size(), this.backEndsHighCount);
            ++this.backEndsTotalCount;
            this.backEndByIds.put(var1.getJMSServerId(), var1);
            InvocableManagerDelegate.delegate.invocableAdd(14, var1);
         }

      }
   }

   public void removeBackEnd(BackEnd var1) {
      if (var1 != null) {
         synchronized(this.shutdownLock) {
            this.backEnds.remove(var1.getName());
            this.backEndByIds.remove(var1.getJMSServerId());
            InvocableManagerDelegate.delegate.invocableRemove(14, var1.getJMSID());
         }
      }

   }

   public BackEnd[] getBackEnds() {
      synchronized(this.shutdownLock) {
         BackEnd[] var2 = new BackEnd[this.backEnds.size()];
         return (BackEnd[])((BackEnd[])this.backEnds.values().toArray(var2));
      }
   }

   public HashMap getBackEndsMap() {
      synchronized(this.shutdownLock) {
         return this.backEnds;
      }
   }

   public BackEnd findBackEnd(String var1) {
      synchronized(this.shutdownLock) {
         return (BackEnd)this.backEnds.get(var1);
      }
   }

   public BackEnd findBackEnd(JMSServerId var1) {
      synchronized(this.shutdownLock) {
         return (BackEnd)this.backEndByIds.get(var1);
      }
   }

   public BEDestinationImpl findBEDestination(String var1) {
      BEDestinationImpl var2 = null;
      BackEnd[] var3 = this.getBackEnds();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2 = var3[var4].findDestination(var1);
         if (var2 != null) {
            return var2;
         }
      }

      return var2;
   }

   public void postDeploymentsStart() {
      synchronized(this.shutdownLock) {
         CDS.getCDS().postDeploymentsStart();
         BackEnd[] var2 = this.getBackEnds();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               var2[var3].postDeploymentsStart();
            }

         }
      }
   }

   public void postDeploymentsStop() {
      synchronized(this.shutdownLock) {
         BackEnd[] var2 = this.getBackEnds();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               var2[var3].postDeploymentsStop();
            }

         }
      }
   }

   public BackEndTempDestinationFactory nextFactory() {
      synchronized(this.shutdownLock) {
         int var2;
         if ((var2 = this.tempDestFactories.size()) <= 0) {
            this.currentFactoryIndex = -1;
            return null;
         } else {
            if (this.currentFactoryIndex < 0 || this.currentFactoryIndex >= var2) {
               this.currentFactoryIndex = 0;
            }

            BackEndTempDestinationFactory var3 = (BackEndTempDestinationFactory)this.tempDestFactories.get(this.currentFactoryIndex++);
            if (this.currentFactoryIndex >= var2) {
               this.currentFactoryIndex = 0;
            }

            return var3;
         }
      }
   }

   public void addTempDestinationFactory(BackEndTempDestinationFactory var1) throws NamingException {
      synchronized(this.shutdownLock) {
         this.tempDestFactories.add(var1);
         if (this.tempDestFactories.size() == 1) {
            PrivilegedActionUtilities.bindAsSU(JMSService.getContext(true), "weblogic.jms.TempDestinationFactory", this.beTempDestinationFactory.getFactoryWrapper(), kernelId);
         }

      }
   }

   public void removeTempDestinationFactory(BackEndTempDestinationFactory var1) throws NamingException {
      synchronized(this.shutdownLock) {
         Iterator var3 = this.tempDestFactories.iterator();
         boolean var4 = false;

         while(var3.hasNext()) {
            BackEndTempDestinationFactory var5 = (BackEndTempDestinationFactory)var3.next();
            if (var5 == var1) {
               var4 = true;
               var3.remove();
            }
         }

         if (var4) {
            int var10 = this.tempDestFactories.size();
            if (var10 <= 0) {
               this.currentFactoryIndex = -1;
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(true), "weblogic.jms.TempDestinationFactory", kernelId);

               try {
                  ServerHelper.unexportObject(this.beTempDestinationFactory, true);
               } catch (NoSuchObjectException var8) {
               }
            } else if (this.currentFactoryIndex >= var10) {
               this.currentFactoryIndex = 0;
            }
         }

      }
   }

   public BETempDestinationFactory getBETempDestinationFactory() {
      return this.beTempDestinationFactory;
   }
}
