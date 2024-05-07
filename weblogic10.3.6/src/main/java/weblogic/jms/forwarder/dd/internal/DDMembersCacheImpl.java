package weblogic.jms.forwarder.dd.internal;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.jms.cache.CacheContextInfo;
import weblogic.jms.cache.CacheEntry;
import weblogic.jms.common.CDS;
import weblogic.jms.common.DDMemberInformation;
import weblogic.jms.common.DDMembershipChangeEventImpl;
import weblogic.jms.common.DDMembershipChangeListener;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.forwarder.DestinationName;
import weblogic.jms.forwarder.SessionRuntimeContext;
import weblogic.jms.forwarder.dd.DDMembersCache;
import weblogic.jms.forwarder.dd.DDMembersCacheChangeListener;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public class DDMembersCacheImpl implements DDMembersCache {
   private static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   private static final AbstractSubject kernelID = getKernelIdentity();
   private String cacheName;
   private boolean isForLocalCluster = true;
   private DestinationName destinationName;
   private CacheContextInfo cacheContextInfo;
   private Context providerContext;
   private AbstractSubject subject;
   private SessionRuntimeContext sessionRuntimeContext;
   private static DDMembersCacheManagerImpl ddMembersCacheManager;
   private DDMembershipHandler ddMembershipHandler;
   private HashMap ddEventListenersMap = new HashMap();
   private DDMemberInformation[] ddMemberConfigInformation;
   private Map ddMemberRuntimeInformationMap = new HashMap();
   private String loginUrl;

   private static final AbstractSubject getKernelIdentity() {
      try {
         return (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
      } catch (AccessControlException var1) {
         return null;
      }
   }

   public DDMembersCacheImpl(DestinationName var1) {
      this.init(var1);
      if (this.ddMembershipHandler == null) {
         this.ddMembershipHandler = new DDMembershipHandler();
      }

   }

   public DDMembersCacheImpl(CacheContextInfo var1, DestinationName var2) {
      this.isForLocalCluster = false;
      this.setCacheContextInfo(var1);
      this.init(var2);
      if (this.ddMembershipHandler == null) {
         this.ddMembershipHandler = new DDMembershipHandler();
      }

   }

   public DDMembersCacheImpl(SessionRuntimeContext var1, DestinationName var2, boolean var3) {
      this.sessionRuntimeContext = var1;
      this.isForLocalCluster = var3;
      this.loginUrl = var1.getLoginUrl();
      this.setProviderContext(var1.getProviderContext());
      this.setSubject(var1.getSubject());
      this.init(var2);
      if (this.ddMembershipHandler == null) {
         this.ddMembershipHandler = new DDMembershipHandler();
      }

   }

   private void init(DestinationName var1) {
      this.setName(var1);
      this.setDestinationName(var1);
   }

   private void setName(DestinationName var1) {
      this.setName("[ProviderUrl = " + (this.isForLocalCluster ? " Local Cluster" : (this.cacheContextInfo == null ? this.loginUrl : this.cacheContextInfo.getProviderUrl())) + "]" + var1);
   }

   public String getName() {
      return this.cacheName;
   }

   public void setName(String var1) {
      this.cacheName = var1;
   }

   public synchronized CacheEntry[] getCacheEntries() {
      if (this.ddMembershipHandler == null) {
         this.ddMembershipHandler = new DDMembershipHandler();
      }

      return new CacheEntry[0];
   }

   public void setCacheContextInfo(CacheContextInfo var1) {
      this.cacheContextInfo = var1;
   }

   public CacheContextInfo getCacheContextInfo() {
      return this.cacheContextInfo;
   }

   public void setProviderContext(Context var1) {
      this.providerContext = var1;
   }

   public Context getProviderContext() {
      return this.sessionRuntimeContext != null ? this.sessionRuntimeContext.getProviderContext() : this.providerContext;
   }

   public void setSubject(AbstractSubject var1) {
      this.subject = var1;
   }

   public boolean isForLocalCluster() {
      return this.isForLocalCluster;
   }

   public DDMemberInformation[] getDDMemberConfigInformation() {
      CacheEntry[] var1 = this.getCacheEntries();
      return (DDMemberInformation[])((DDMemberInformation[])var1);
   }

   public Map getDDMemberRuntimeInformation() {
      return this.ddMemberRuntimeInformationMap;
   }

   public void removeDDMembersCacheChangeListener(DDMembersCacheChangeListener var1) {
      synchronized(this) {
         if (this.ddMembershipHandler != null) {
            this.ddMembershipHandler.close();
         }

         this.ddEventListenersMap.remove(var1.getId());
      }
   }

   public void addDDMembersCacheChangeListener(DDMembersCacheChangeListener var1) {
      DDMembersCacheChangeListener var2 = null;
      boolean var3 = false;
      DDMemberInformation[] var4 = null;
      int var10;
      synchronized(this) {
         var2 = (DDMembersCacheChangeListener)this.ddEventListenersMap.get(var1.getId());
         if (this.ddMemberConfigInformation == null) {
            if (var2 == null) {
               this.ddEventListenersMap.put(var1.getId(), var1);
            }

            return;
         }

         var10 = this.ddMemberConfigInformation.length;
         var4 = (DDMemberInformation[])((DDMemberInformation[])this.ddMemberConfigInformation.clone());
      }

      for(int var5 = 0; var5 < var10; ++var5) {
         var1.onCacheEntryAdd(var4[var5]);
      }

      synchronized(this) {
         if (var2 == null) {
            this.ddEventListenersMap.put(var1.getId(), var1);
         }

      }
   }

   public void setDestinationName(DestinationName var1) {
      this.destinationName = var1;
   }

   public DestinationName getDestinationName() {
      return this.destinationName;
   }

   static {
      ddMembersCacheManager = DDMembersCacheManagerImpl.ddMembersCacheManager;
   }

   private class DDMembershipHandler implements DDMembershipChangeListener {
      public DDMembershipHandler() {
         synchronized(DDMembersCacheImpl.this) {
            DDMembersCacheImpl.this.ddMemberConfigInformation = CDS.getCDS().getDDMembershipInformation(this);
         }
      }

      void close() {
         CDS.getCDS().unregisterDDMembershipChangeListener(this);
      }

      public void onDDMembershipChange(DDMembershipChangeEventImpl var1) {
         Iterator var2 = null;
         synchronized(DDMembersCacheImpl.this) {
            var2 = ((HashMap)DDMembersCacheImpl.this.ddEventListenersMap.clone()).values().iterator();
         }

         DDMemberInformation[] var3 = var1.getRemovedDDMemberInformation();
         DDMemberInformation[] var4 = var1.getAddedDDMemberInformation();

         while(var2.hasNext()) {
            try {
               DDMembersCacheChangeListener var5 = (DDMembersCacheChangeListener)var2.next();
               if (var3 != null) {
                  var5.onCacheEntryRemove(var3);
               }

               if (var4 != null) {
                  var5.onCacheEntryAdd(var4);
               }
            } catch (Throwable var6) {
               var6.printStackTrace();
            }
         }

      }

      public String getDestinationName() {
         return DDMembersCacheImpl.this.destinationName.getJNDIName();
      }

      public String getProviderURL() {
         return DDMembersCacheImpl.this.loginUrl;
      }

      public Context getInitialContext() throws NamingException {
         JMSDebug.JMSSAF.debug(this + ": getInitialContext() " + "sessionRuntimeContext " + DDMembersCacheImpl.this.sessionRuntimeContext + " loginURL " + this.getProviderURL());
         if (DDMembersCacheImpl.this.sessionRuntimeContext != null) {
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug(this + ": CACHED getInitialContext returning sessionRuntimeContext.getProviderContext() " + DDMembersCacheImpl.this.sessionRuntimeContext.getProviderContext() + " with subject " + DDMembersCacheImpl.this.sessionRuntimeContext.getSubject() + " forceResolveDNS " + DDMembersCacheImpl.this.sessionRuntimeContext.getForceResolveDNS());
            }

            if (!DDMembersCacheImpl.this.sessionRuntimeContext.getForceResolveDNS()) {
               DDMembersCacheImpl.this.providerContext = DDMembersCacheImpl.this.sessionRuntimeContext.getProviderContext();
            }
         }

         String var1 = this.getProviderURL();
         String var2 = null;
         String var3 = null;
         boolean var4 = false;
         if (DDMembersCacheImpl.this.sessionRuntimeContext != null) {
            var3 = DDMembersCacheImpl.this.sessionRuntimeContext.getPassword();
            var2 = DDMembersCacheImpl.this.sessionRuntimeContext.getUsername();
            var4 = DDMembersCacheImpl.this.sessionRuntimeContext.getForceResolveDNS();
         }

         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + ": NEW PATH getInitialContext url " + var1 + " username " + var2 + " pass " + "XXXX" + " forceResolveDNS " + var4);
         }

         Hashtable var5 = new Hashtable();
         var5.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         if (var1 != null) {
            var5.put("java.naming.provider.url", var1);
         }

         if (var2 != null) {
            var5.put("java.naming.security.principal", var2);
         }

         if (var3 != null) {
            var5.put("java.naming.security.credentials", var3);
         }

         if (var4) {
            System.setProperty("weblogic.jndi.forceResolveDNSName", "true");
         }

         DDMembersCacheImpl.this.providerContext = new InitialContext(var5);
         DDMembersCacheImpl.this.subject = SubjectManager.getSubjectManager().getCurrentSubject(DDMembersCacheImpl.kernelID);
         if (DDMembersCacheImpl.this.sessionRuntimeContext != null) {
            DDMembersCacheImpl.this.sessionRuntimeContext.setSubject(DDMembersCacheImpl.this.subject);
            DDMembersCacheImpl.this.sessionRuntimeContext.setProviderContext(DDMembersCacheImpl.this.providerContext);
         }

         JMSDebug.JMSSAF.debug(this + ": getInitialcontext , return providerContext " + DDMembersCacheImpl.this.providerContext + " subject " + DDMembersCacheImpl.this.subject);
         return DDMembersCacheImpl.this.providerContext;
      }

      public AbstractSubject getSubject() {
         return DDMembersCacheImpl.this.sessionRuntimeContext != null ? DDMembersCacheImpl.this.sessionRuntimeContext.getSubject() : DDMembersCacheImpl.this.subject;
      }

      public void onFailure(String var1, Exception var2) {
      }
   }
}
