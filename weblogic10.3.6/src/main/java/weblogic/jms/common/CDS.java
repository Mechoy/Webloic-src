package weblogic.jms.common;

import java.io.IOException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.deployment.jms.ForeignOpaqueTag;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class CDS {
   private static CDS singleton = new CDS();
   private final List dd2Listeners = Collections.synchronizedList(new LinkedList());
   private final HashMap unsuccessfulDDLookup = new HashMap();
   private final HashMap pendingRegistration = new HashMap();
   private static CDSListProvider localCDSServer;
   private static CDSListProvider localCDSProxy = CDSLocalProxy.getSingleton();
   private TimerManager timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms.common.DistributedDestinationManager", (WorkManager)null);
   private Timer ddPoller;
   private boolean ddLookupIsRunning;
   private TimerManager timerManagerForRegistration = null;
   private Timer listenerRegistrar;
   private static final String DDM_NAME = "weblogic.jms.common.DistributedDestinationManager";
   private static final String DDM_REGISTRATION_MANAGER_NAME = "weblogic.jms.common.DistributedDestinationRegistrationManager";
   private static final String CDS_ASYNC_REGISTRATION_WM_NAME = "CdsAsyncRegistration";
   private WorkManager cdsAsyncRegistrationWorkManager;
   private boolean postDeploymentsStart = false;
   private static final long JMS_DD_JNDI_LOOKUP_INTERVAL = 10000L;
   private static final long JMS_DD_JNDI_LOOKUP_INITIAL_DELAY = 10000L;
   private static final long JMS_DD_LISTENER_REGISTRATION_INTERVAL = 500L;
   private static final long JMS_DD_LISTENER_REGISTRATION_DELAY = 500L;
   private static final int INITIAL_CONTEXT_SUCCEEDED = 0;
   private static final int INITIAL_JNDI_LOOKUP_SUCCEEDED = 1;
   private static final int POLLER_JNDI_LOOKUP_SUCCEEDED = 2;
   private static final int NUMBER_OF_RETRIES_BEFORE_ON_FAILURE_CALLBACK = 10;
   private static final int DEFAULT_CDS_ASYNC_REGISTRATION_THREAD_COUNT;

   public static synchronized CDS getCDS() {
      return singleton;
   }

   private void initializeWorkManager() {
      this.cdsAsyncRegistrationWorkManager = WorkManagerFactory.getInstance().findOrCreate("CdsAsyncRegistration", 100, 1, DEFAULT_CDS_ASYNC_REGISTRATION_THREAD_COUNT);
   }

   private void startPolling(DD2Listener var1) {
      synchronized(this.dd2Listeners) {
         ListIterator var3 = this.dd2Listeners.listIterator();

         while(var3.hasNext()) {
            if (var1 == var3.next()) {
               var3.remove();
               break;
            }
         }
      }

      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         JMSDebug.JMSCDS.debug("The DD " + var1.getDestinationName() + " is not up, starting the poller...");
      }

      synchronized(this) {
         this.unsuccessfulDDLookup.put(var1.getListener(), var1);
         if (this.ddPoller == null) {
            this.ddPoller = this.timerManager.schedule(new DDLookupTimerListener(), 10000L, 10000L);
         }

      }
   }

   public CDSSecurityHandle registerForDDMembershipInformation(DDMembershipChangeListener var1) {
      if (var1 == null) {
         throw new AssertionError("Listener cannot be null");
      } else {
         if (this.cdsAsyncRegistrationWorkManager == null) {
            this.initializeWorkManager();
         }

         this.timerManagerForRegistration = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms.common.DistributedDestinationRegistrationManager", this.cdsAsyncRegistrationWorkManager);
         DD2Listener var2 = new DD2Listener(var1);
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Scheduling initial registration for DD JNDI Name " + var1.getDestinationName() + " providerIRL = " + var2.getProviderURL() + " isLocal =  " + var2.isLocal());
         }

         synchronized(this) {
            this.pendingRegistration.put(var2.getListener(), var2);
            if (this.listenerRegistrar == null) {
               this.listenerRegistrar = this.timerManagerForRegistration.schedule(new DDListenerRegistrationTimerListener(), 500L, 500L);
            }

            return var2;
         }
      }
   }

   public DDMemberInformation[] getDDMembershipInformation(DDMembershipChangeListener var1) {
      if (var1 == null) {
         throw new AssertionError("Listener cannot be null");
      } else {
         DD2Listener var2 = new DD2Listener(var1);
         String var3 = var1.getDestinationName();
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("getSubject: before looking up " + var3 + " providerIRL = " + var2.getProviderURL() + " isLocal =  " + var2.isLocal());
         }

         Context var4 = createInitialContext(var2, false);
         if (var4 == null) {
            this.startPolling(var2);
            return null;
         } else {
            Object var5 = this.lookupDestination(var4, var2, false);
            if (var5 == null) {
               this.startPolling(var2);
               return null;
            } else {
               DDMemberInformation[] var6;
               try {
                  var6 = this.processDD(var4, var2, var5);
               } catch (javax.jms.JMSException var8) {
                  this.startPolling(var2);
                  return null;
               }

               var2.setCurrentMemberList(var6);
               return var6;
            }
         }
      }
   }

   private void moveListenerToPoller(HashMap var1, Object var2) {
      DD2Listener var3 = (DD2Listener)var1.remove(var2);
      if (var1.size() == 0 && this.listenerRegistrar != null) {
         this.listenerRegistrar = null;
      }

      this.startPolling(var3);
   }

   private void lookupDDAndCalloutListener(HashMap var1, boolean var2) {
      synchronized(this) {
         Iterator var4 = ((HashMap)var1.clone()).keySet().iterator();

         while(true) {
            while(true) {
               Object var5;
               DD2Listener var6;
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  var5 = var4.next();
                  var6 = (DD2Listener)var1.get(var5);
                  if (JMSDebug.JMSCDS.isDebugEnabled()) {
                     JMSDebug.JMSCDS.debug("lookupDDAndCalloutListener ddl= " + var6 + " isPoller=" + var2);
                  }
               } while(var6 == null);

               Context var7 = createInitialContext(var6, var2);
               if (var7 == null) {
                  if (!var2) {
                     this.moveListenerToPoller(var1, var5);
                  }
               } else {
                  Object var8 = this.lookupDestination(var7, var6, var2);
                  if (var8 == null) {
                     if (!var2) {
                        this.moveListenerToPoller(var1, var5);
                     }
                  } else {
                     if (JMSDebug.JMSCDS.isDebugEnabled()) {
                        JMSDebug.JMSCDS.debug("lookupDDAndCalloutListener has successfully looked up the destination  with JNDI name " + var6.getDestinationName() + ", going to process the membership information");
                     }

                     DDMemberInformation[] var9;
                     try {
                        var9 = this.processDD(var7, var6, var8);
                     } catch (javax.jms.JMSException var14) {
                        var6.reportException(var14);
                        if (!var2) {
                           this.moveListenerToPoller(var1, var5);
                        }
                        continue;
                     }

                     if (!var6.isDD()) {
                        synchronized(this.dd2Listeners) {
                           this.dd2Listeners.remove(var6);
                        }
                     }

                     var1.remove(var5);
                     if (var1.size() == 0) {
                        if (var2) {
                           if (this.ddPoller != null) {
                              this.ddPoller.cancel();
                              this.ddPoller = null;
                           }
                        } else if (this.listenerRegistrar != null) {
                           this.listenerRegistrar = null;
                        }
                     }

                     var6.listChange(var9);
                  }
               }
            }
         }
      }
   }

   private static Context createInitialContext(DD2Listener var0, boolean var1) {
      Object var2 = null;
      final DD2Listener var3 = var0;
      Context var4 = null;

      try {
         Context var5 = (Context)CrossDomainSecurityManager.runAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getSubjectFromListener(var3), new PrivilegedExceptionAction() {
            public Object run() throws NamingException {
               return var3.getInitialContext();
            }
         });
         var3.setState(0);
         var4 = var5;
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Successfully created the initial context for the JNDIName " + var3.getDestinationName());
         }
      } catch (PrivilegedActionException var6) {
         var2 = var6.getException();
      } catch (NamingException var7) {
         var2 = var7;
      } catch (IOException var8) {
         var2 = var8;
      } catch (SecurityException var9) {
         var2 = var9;
      }

      if (var2 != null) {
         if (var1) {
            var0.incrementPollerRetryCount();
            if (var0.getPollerRetryCount() == 10) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("From ddPoller: The initial context creation for the JNDI name " + var0.getDestinationName() + " has failed, and the poller has reached the retry limit for reporting error, calling out listener's onFailure ...");
               }

               if ((var0.getState() & 0) != 1) {
                  var0.reportException((Exception)var2);
               }
            }
         } else {
            if ((var0.getState() & 0) != 1) {
               var0.reportException((Exception)var2);
            }

            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("The initial context creation for JNDIName " + var0.getDestinationName() + "has failed, going to start polling", (Throwable)var2);
            }
         }
      }

      return var4;
   }

   private Object lookupDestination(final Context var1, final DD2Listener var2, boolean var3) {
      Object var4 = null;
      Object var5 = null;

      try {
         var4 = CrossDomainSecurityManager.runAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getSubjectFromListener(var2), new PrivilegedExceptionAction() {
            public Object run() throws NamingException {
               return var1.lookup(var2.getDestinationName());
            }
         });
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("The destination JNDI lookup succeeded for " + var2.getDestinationName() + " destination = " + var4 + ", going to process the membership information");
         }

         if (!var3) {
            var2.setState(1);
         } else {
            var2.setState(2);
            var2.resetPollerRetryCount();
         }
      } catch (PrivilegedActionException var8) {
         var5 = var8.getException();
      } catch (NamingException var9) {
         var5 = var9;
      } catch (IOException var10) {
         var5 = var10;
      } catch (SecurityException var11) {
         var5 = var11;
      }

      if (var5 != null) {
         if (var3) {
            var2.incrementPollerRetryCount();
            if (var2.getPollerRetryCount() == 10) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("From ddPoller: The destination with JNDI name " + var2.getDestinationName() + " is not up and poller has reached the retry limit for reporting error, calling out listener's onFailure ...");
               }

               if ((var2.getState() & 2) != 1) {
                  var2.reportException((Exception)var5);
               }
            }
         } else {
            if ((var2.getState() & 1) != 1) {
               var2.reportException((Exception)var5);
            }

            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("Initial destination lookup for the JNDI name " + var2.getDestinationName() + "has failed, going to start polling", (Throwable)var5);
            }
         }
      }

      return var4;
   }

   public void unregisterDDMembershipChangeListener(DDMembershipChangeListener var1) {
      if (var1 == null) {
         throw new AssertionError("Listener cannot be null");
      } else {
         String var2 = var1.getDestinationName();
         DD2Listener var3 = null;
         synchronized(this) {
            var3 = (DD2Listener)this.pendingRegistration.remove(var1);
            if (var3 != null) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("unregisterDDMembershipChangeListener: " + var1 + ". Removing the corresponding DD (JNDI name: " + var2 + ") which is currently " + " kept in pendingRegistration map");
               }

               if (this.pendingRegistration.size() == 0) {
                  this.listenerRegistrar = null;
               }

               return;
            }
         }

         synchronized(this) {
            var3 = (DD2Listener)this.unsuccessfulDDLookup.remove(var1);
            if (var3 != null) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("unregisterDDMembershipChangeListener: " + var1 + ". Removing the corresponding DD (JNDI name: " + var2 + ") which is currently " + " kept in unsuccessfulDDLookup map");
               }

               if (this.unsuccessfulDDLookup.size() == 0) {
                  this.ddPoller = null;
               }

               return;
            }
         }

         synchronized(this) {
            synchronized(this.dd2Listeners) {
               ListIterator var6 = this.dd2Listeners.listIterator();

               while(var6.hasNext()) {
                  var3 = (DD2Listener)var6.next();
                  if (var3.getListener() == var1) {
                     if (JMSDebug.JMSCDS.isDebugEnabled()) {
                        JMSDebug.JMSCDS.debug("unregisterDDMembershipChangeListener: " + var1 + ". Removing the correponding DD (JNDI name: " + var2 + ") which is currently " + " kept in ddl map");
                     }

                     var6.remove();
                     if (var3.isLocal()) {
                        if (localCDSServer == null) {
                           throw new AssertionError("MDB says that it is local, but the CDS is not able to find server symbols");
                        }

                        localCDSServer.unregisterListener(var3);
                     } else {
                        localCDSProxy.unregisterListener(var3);
                     }
                  }
               }
            }

         }
      }
   }

   private static DDMemberInformation[] processForeign(DD2Listener var0, Object var1) {
      boolean var3 = false;
      if (var1 instanceof Queue) {
         var3 = true;
         if (var1 instanceof Topic) {
            try {
               var3 = ((Queue)var1).getQueueName() != null;
            } catch (Throwable var6) {
               var3 = false;
            }
         }
      }

      String var2;
      if (var3) {
         var2 = new String("javax.jms.Queue");
      } else if (var1 instanceof Topic) {
         var2 = new String("javax.jms.Topic");
      } else {
         var2 = null;
      }

      DDMemberInformation var4 = new DDMemberInformation((String)null, var2, var0.getDestinationName(), (DestinationImpl)null, (String)null, var0.getDestinationName(), (String)null, (String)null, (String)null);
      DDMemberInformation[] var5 = new DDMemberInformation[]{var4};
      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         JMSDebug.JMSCDS.debug("processDD(): The destination with JNDI name " + var0.getDestinationName() + " is processed as foreign destination");
      }

      return var5;
   }

   private static DDMemberInformation[] processNonDD(DD2Listener var0, Object var1) {
      if (!(var1 instanceof DestinationImpl)) {
         return processForeign(var0, var1);
      } else {
         DestinationImpl var2 = (DestinationImpl)var1;
         var0.setDestinationImpl(var2);
         DDMemberInformation[] var3 = null;
         String var4 = null;
         String var5 = var2.getName();
         Object var6 = null;
         Object var7 = null;
         if (var2.isQueue()) {
            var4 = new String("javax.jms.Queue");
         } else {
            var4 = new String("javax.jms.Topic");
         }

         DDMemberInformation var8 = new DDMemberInformation(var2.getName(), var4, var0.getDestinationName(), var2, (String)null, var0.getDestinationName(), (String)null, (String)null, (String)null);
         var3 = new DDMemberInformation[]{var8};
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("processDD(): The destination with config name " + var5 + " is processed as physical destination");
         }

         return var3;
      }
   }

   private DDMemberInformation[] processDD(final Context var1, final DD2Listener var2, Object var3) throws javax.jms.JMSException {
      boolean var4 = false;
      if (!(var3 instanceof DistributedDestinationImpl) || var3 instanceof DistributedDestinationImpl && ((DestinationImpl)var3).isPre90()) {
         var4 = true;
      }

      Object var5 = null;

      try {
         CrossDomainSecurityManager.runAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getSubjectFromListener(var2), new PrivilegedExceptionAction() {
            public Object run() throws NamingException, IOException {
               CDS.this.checkForeign(var1, var2);
               return null;
            }
         });
         var2.setIsSecurityHandleReady(true);
      } catch (PrivilegedActionException var11) {
         var5 = var11.getException();
      } catch (IOException var12) {
         var5 = var12;
      } catch (NamingException var13) {
         var5 = var13;
      } catch (SecurityException var14) {
         var5 = var14;
      }

      if (var5 != null) {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("The destination JNDI lookup Link failed for " + var2.getJNDIName(), (Throwable)var5);
         }

         throw new JMSException(((Exception)var5).getMessage(), (Throwable)var5);
      } else if (var4) {
         var2.setIsDD(false);
         return processNonDD(var2, var3);
      } else {
         var2.setDestinationImpl((DestinationImpl)var3);
         var2.setIsDD(true);
         DDMemberInformation[] var6 = null;
         String var7 = var2.getConfigName();
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("processDD(): The destination with config name " + var7 + " is being processed as distributed destination");
         }

         if (var2.isLocal()) {
            if (localCDSServer == null) {
               throw new AssertionError("MDB says that it is local, but the CDS is not able to find server symbols");
            }

            var6 = localCDSServer.registerListener(var2);
         } else {
            var6 = localCDSProxy.registerListener(var2);
         }

         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("processDD(): The destination with config name " + var7 + " is a " + (var2.isLocal() ? "local" : "remote") + "distributed destination, " + " getLocalDDMemberInformation(" + var7 + ") returned " + var6.toString());
         }

         synchronized(this.dd2Listeners) {
            this.dd2Listeners.add(var2);
            return var6;
         }
      }
   }

   private void checkForeign(Context var1, CDSListListener var2) throws NamingException, IOException {
      boolean var3 = false;
      Object var4 = null;
      var4 = var1.lookupLink(var2.getJNDIName());
      if (var4 instanceof ForeignOpaqueTag) {
         Hashtable var5 = ((ForeignOpaqueTag)var4).getJNDIEnvironment();
         String var6 = var5 == null ? "unknown" : (String)var5.get("java.naming.provider.url");
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Destination " + var2.getJNDIName() + " is instanceof ForeignOpaqueTag, with foreignJNDIEnv " + var5);
            JMSDebug.JMSCDS.debug("Changing context to foreign provider before registering listener " + (var6 == null ? "unknown" : var6));
         }

         if (var5 != null) {
            var2.setForeign(var5);
         }
      }

   }

   public void postDeploymentsStart() {
      synchronized(this) {
         if (this.ddPoller != null) {
            this.ddPoller.cancel();
            this.ddPoller = null;
            this.ddPoller = this.timerManager.schedule(new DDLookupTimerListener(), 0L, 10000L);
         }

         this.postDeploymentsStart = true;
      }
   }

   private static String getDestinationName(Destination var0) {
      return var0 instanceof DistributedDestinationImpl ? ((DistributedDestinationImpl)var0).getInstanceName() : ((DestinationImpl)var0).getName();
   }

   public static void dumpDDMITable(DDMemberInformation[] var0) {
      if (var0 != null && var0.length != 0) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            if (var0[var1].getDestination() != null) {
               JMSDebug.JMSCDS.debug("Entry[" + var1 + "] = " + getDestinationName(var0[var1].getDestination()) + ": " + " with destination id " + ((DestinationImpl)var0[var1].getDestination()).getDestinationId() + ": " + var0[var1]);
            }
         }

      } else {
         JMSDebug.JMSCDS.debug("Table is empty");
      }
   }

   public static void dumpChangeEvent(DDMembershipChangeEventImpl var0) {
      JMSDebug.JMSCDS.debug("Here is the added table:");
      dumpDDMITable(var0.getAddedDDMemberInformation());
      JMSDebug.JMSCDS.debug("Here is the removed table:");
      dumpDDMITable(var0.getRemovedDDMemberInformation());
   }

   private static boolean unchanged(DDMemberInformation var0, DDMemberInformation var1) {
      if (var0.isProductionPaused() != var1.isProductionPaused()) {
         return false;
      } else if (var0.isInsertionPaused() != var1.isInsertionPaused()) {
         return false;
      } else if (var0.isConsumptionPaused() != var1.isConsumptionPaused()) {
         return false;
      } else {
         DestinationImpl var2 = (DestinationImpl)var0.getDestination();
         DestinationImpl var3 = (DestinationImpl)var1.getDestination();
         if (!var2.getDestinationId().equals(var3.getDestinationId())) {
            return false;
         } else if (!var2.getDispatcherId().equals(var3.getDispatcherId())) {
            return false;
         } else {
            return var2.getServerName().equals(var3.getServerName());
         }
      }
   }

   private static DDMembershipChangeEventImpl makeChangeEvent(boolean var0, String var1, String var2, DDMemberInformation[] var3, DDMemberInformation[] var4) {
      if (var4 == null) {
         return var3 == null ? null : new DDMembershipChangeEventImpl(var0, var1, var2, var3, var4);
      } else if (var3 == null) {
         return new DDMembershipChangeEventImpl(var0, var1, var2, var3, var4);
      } else {
         HashMap var5 = new HashMap();

         for(int var6 = 0; var3 != null && var6 < var3.length; ++var6) {
            var5.put(var3[var6].getMemberName(), var3[var6]);
         }

         HashMap var12 = new HashMap();

         for(int var7 = 0; var4 != null && var7 < var4.length; ++var7) {
            var12.put(var4[var7].getMemberName(), var4[var7]);
         }

         LinkedList var13 = new LinkedList();
         LinkedList var8 = new LinkedList();
         Iterator var9 = var5.values().iterator();

         DDMemberInformation var10;
         DDMemberInformation var11;
         while(var9.hasNext()) {
            var10 = (DDMemberInformation)var9.next();
            var11 = (DDMemberInformation)var12.get(var10.getMemberName());
            if (var11 == null) {
               var13.add(var10);
            } else if (!unchanged(var10, var11)) {
               var13.add(var10);
               var8.add(var11);
            }
         }

         var9 = var12.values().iterator();

         while(var9.hasNext()) {
            var10 = (DDMemberInformation)var9.next();
            var11 = (DDMemberInformation)var5.get(var10.getMemberName());
            if (var11 == null) {
               var8.add(var10);
            }
         }

         if (var13.size() == 0 && var8.size() == 0) {
            return null;
         } else {
            DDMemberInformation[] var14 = null;
            if (var13.size() != 0) {
               var14 = (DDMemberInformation[])((DDMemberInformation[])var13.toArray(new DDMemberInformation[0]));
            }

            DDMemberInformation[] var15 = null;
            if (var8.size() != 0) {
               var15 = (DDMemberInformation[])((DDMemberInformation[])var8.toArray(new DDMemberInformation[0]));
            }

            return new DDMembershipChangeEventImpl(var0, var1, var2, var14, var15);
         }
      }
   }

   private void ddLookup() {
      synchronized(this) {
         if (this.ddLookupIsRunning) {
            return;
         }

         this.ddLookupIsRunning = true;
      }

      try {
         this.lookupDDAndCalloutListener(this.unsuccessfulDDLookup, true);
      } finally {
         synchronized(this) {
            this.ddLookupIsRunning = false;
         }
      }

   }

   static {
      try {
         localCDSServer = (CDSListProvider)Class.forName("weblogic.jms.common.CDSServer").getDeclaredMethod("getSingleton", (Class[])null).invoke((Object)null, (Object[])null);
      } catch (Exception var4) {
      }

      int var0 = 8;
      String var1 = System.getProperty("weblogic.jms.CDS.AsyncRegisterationThreadCount", "8");

      try {
         var0 = Integer.parseInt(var1);
      } catch (NumberFormatException var3) {
         var3.printStackTrace();
      }

      DEFAULT_CDS_ASYNC_REGISTRATION_THREAD_COUNT = var0;
   }

   private final class DDLookupTimerListener implements NakedTimerListener {
      DDLookupTimerListener() {
      }

      public void timerExpired(Timer var1) {
         CDS.this.ddLookup();
      }
   }

   private final class DD2Listener implements CDSListListener, Runnable, CDSSecurityHandle {
      private DDMembershipChangeListener listener;
      private DDMemberInformation[] currentMemberList = null;
      private DDMemberInformation[] pendingMemberList = null;
      private DestinationImpl dImpl;
      private boolean isDD;
      private boolean running;
      private boolean moreToProcess;
      private AbstractSubject foreignSubject;
      private String providerURL;
      private Context foreignContext;
      private AbstractSubject listenerThreadSubject;
      private int pollerRetryCount = 0;
      private int state;
      private final Object stateLock = new Object();
      private Exception lastExceptionReported = null;
      private int privilegedActionExceptionReported = 0;
      private int namingExceptionReported = 0;
      private int ioExceptionReported = 0;
      private int unknownExceptionReported = 0;
      private boolean isSecurityHandleReady;
      private boolean isForeignJMSServer;
      private boolean isRemoteDomain;
      private Object foreignContextLock = new Object();

      public DD2Listener(DDMembershipChangeListener var2) {
         this.listener = var2;
         this.providerURL = var2.getProviderURL();
         this.listenerThreadSubject = CrossDomainSecurityManager.getCurrentSubject();
      }

      public void setState(int var1) {
         synchronized(this.stateLock) {
            this.state |= var1;
         }
      }

      public int getState() {
         return this.state;
      }

      public void incrementPollerRetryCount() {
         ++this.pollerRetryCount;
      }

      public void resetPollerRetryCount() {
         this.pollerRetryCount = 0;
      }

      public int getPollerRetryCount() {
         return this.pollerRetryCount;
      }

      public synchronized void setIsSecurityHandleReady(boolean var1) {
         this.isSecurityHandleReady = var1;
      }

      public void reportException(Exception var1) {
         if (var1 instanceof PrivilegedActionException) {
            ++this.privilegedActionExceptionReported;
            if (this.privilegedActionExceptionReported > 1) {
               return;
            }
         }

         if (var1 instanceof NamingException) {
            ++this.namingExceptionReported;
            if (this.namingExceptionReported > 1) {
               return;
            }
         }

         if (var1 instanceof IOException) {
            ++this.ioExceptionReported;
            if (this.ioExceptionReported > 1) {
               return;
            }
         }

         this.listener.onFailure(this.getJNDIName(), var1);
         this.lastExceptionReported = var1;
      }

      public Exception getLastExceptionReported() {
         return this.lastExceptionReported;
      }

      public int getIOExceptionReported() {
         return this.ioExceptionReported;
      }

      public int getNamingExceptionReported() {
         return this.namingExceptionReported;
      }

      public int getPrivilegedActionExceptionReported() {
         return this.privilegedActionExceptionReported;
      }

      public int getUnknownExceptionReported() {
         return this.unknownExceptionReported;
      }

      public void setIsDD(boolean var1) {
         this.isDD = var1;
      }

      public boolean isDD() {
         return this.isDD;
      }

      public String getDestinationName() {
         return this.listener.getDestinationName();
      }

      public String getProviderURL() {
         return this.providerURL;
      }

      public String getJNDIName() {
         return this.listener.getDestinationName();
      }

      public void setForeign(Hashtable var1) throws NamingException, IOException {
         assert var1 != null;

         Hashtable var2 = new Hashtable(var1);
         if (var1.get("java.naming.factory.initial") == null) {
            var2.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         }

         var2.put("weblogic.jndi.disableLoggingOfWarningMsg", "true");
         synchronized(this.foreignContextLock) {
            this.foreignContext = new InitialContext(var2);
         }

         AbstractSubject var3 = CrossDomainSecurityManager.getCurrentSubject();
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug(" setForeign env = " + var2);
         }

         this.providerURL = (String)var2.get("java.naming.provider.url");
         boolean var4 = false;
         if (this.providerURL != null && var2.get("java.naming.factory.initial") != null && ((String)var2.get("java.naming.factory.initial")).indexOf("weblogic") != -1) {
            var4 = CrossDomainSecurityManager.getCrossDomainSecurityUtil().isRemoteDomain(this.providerURL);
         }

         synchronized(this) {
            this.foreignSubject = var3;
            this.isForeignJMSServer = true;
            this.isRemoteDomain = var4;
            if (var2.get("java.naming.security.principal") == null && this.isRemoteDomain) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug(" setForeign is remote domain use anonynous");
               }

               this.foreignSubject = SubjectManager.getSubjectManager().getAnonymousSubject();
            }

         }
      }

      public DDMembershipChangeListener getListener() {
         return this.listener;
      }

      public Context getInitialContext() throws NamingException {
         return this.getInitialContextFromListener();
      }

      public AbstractSubject getSubject() {
         return this.listener.getSubject();
      }

      public boolean isLocal() {
         return this.providerURL == null || this.providerURL.length() == 0;
      }

      public String getConfigName() {
         return this.dImpl == null ? null : this.dImpl.getName();
      }

      public Context getContext() throws NamingException {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug(" getContext() foreign = " + this.foreignContext);
         }

         synchronized(this.foreignContextLock) {
            if (this.foreignContext != null) {
               return this.foreignContext;
            }
         }

         return this.getInitialContextFromListener();
      }

      private Context getInitialContextFromListener() throws NamingException {
         try {
            return (Context)CrossDomainSecurityManager.runAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(this.providerURL, this.listenerThreadSubject), new PrivilegedExceptionAction() {
               public Object run() throws NamingException {
                  return DD2Listener.this.listener.getInitialContext();
               }
            });
         } catch (PrivilegedActionException var3) {
            Exception var2 = var3.getException();
            if (var2 instanceof NamingException) {
               throw (NamingException)var2;
            } else {
               throw new NamingException(var2.getMessage());
            }
         }
      }

      public synchronized DDMemberInformation[] getCurrentMemberList() {
         return this.currentMemberList;
      }

      public synchronized void setCurrentMemberList(DDMemberInformation[] var1) {
         this.currentMemberList = var1;
      }

      public void listChange(DDMemberInformation[] var1) {
         synchronized(this) {
            if (this.pendingMemberList == var1 || this.currentMemberList == var1) {
               return;
            }

            this.pendingMemberList = var1;
            this.moreToProcess = true;
            if (this.running) {
               return;
            }

            this.running = true;
         }

         WorkManagerFactory.getInstance().getSystem().schedule(this);
      }

      public void distributedDestinationGone(DispatcherId var1) {
         ArrayList var2 = new ArrayList();
         if (this.currentMemberList != null) {
            DDMemberInformation var3 = null;
            synchronized(this) {
               int var5 = 0;

               while(true) {
                  if (var5 >= this.currentMemberList.length) {
                     break;
                  }

                  DDMemberInformation var6 = this.currentMemberList[var5];
                  DispatcherId var7 = ((DestinationImpl)var6.getDestination()).getDispatcherId();
                  if (var7.equals(var1)) {
                     var3 = var6;
                  } else {
                     var2.add(var6);
                  }

                  ++var5;
               }
            }

            if (var3 != null) {
               this.listChange((DDMemberInformation[])((DDMemberInformation[])var2.toArray(new DDMemberInformation[var2.size()])));
            }
         }

         CDS.this.startPolling(this);
      }

      public DestinationImpl getDestinationImpl() {
         return this.dImpl;
      }

      public DistributedDestinationImpl getDistributedDestinationImpl() {
         return (DistributedDestinationImpl)this.dImpl;
      }

      public void setDestinationImpl(DestinationImpl var1) {
         this.dImpl = var1;
      }

      public synchronized boolean isRemoteDomain() {
         return this.isRemoteDomain;
      }

      public void close() {
         synchronized(this.foreignContextLock) {
            try {
               if (this.foreignContext != null) {
                  this.foreignContext.close();
               }
            } catch (NamingException var4) {
            }

         }
      }

      public synchronized boolean isReady() {
         return this.isSecurityHandleReady;
      }

      public synchronized boolean isForeignJMSServer() {
         if (!this.isSecurityHandleReady) {
            throw new java.lang.IllegalStateException("The handle is not ready");
         } else {
            return this.isForeignJMSServer;
         }
      }

      public synchronized AbstractSubject getForeignSubject() {
         if (!this.isSecurityHandleReady) {
            throw new java.lang.IllegalStateException("The handle is not ready");
         } else {
            return this.foreignSubject;
         }
      }

      public void run() {
         DDMembershipChangeEventImpl var1 = null;

         while(true) {
            synchronized(this) {
               var1 = CDS.makeChangeEvent(this.isDD(), this.getConfigName(), this.getJNDIName(), this.pendingMemberList, this.currentMemberList);
               if (var1 == null) {
                  this.running = false;
                  return;
               }

               this.currentMemberList = this.pendingMemberList;
               this.moreToProcess = false;
            }

            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               CDS.dumpChangeEvent(var1);
            }

            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("informDDMembershipChangeLocally(): Invoking the onMembershipChange() of the local  DDMembershipChangeListener " + this.listener + " for DD JNDIName " + this.getJNDIName());
            }

            try {
               this.listener.onDDMembershipChange(var1);
            } catch (Throwable var6) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("Exception when calling user code: ", var6);
               }
            }

            synchronized(this) {
               if (!this.moreToProcess) {
                  this.running = false;
                  return;
               }
            }
         }
      }
   }

   private final class DDListenerRegistrationTimerListener implements NakedTimerListener {
      DDListenerRegistrationTimerListener() {
      }

      public void timerExpired(Timer var1) {
         Timer var2 = null;
         Boolean var3 = false;
         synchronized(CDS.this) {
            if (CDS.this.postDeploymentsStart) {
               var3 = CDS.this.postDeploymentsStart;
               if (CDS.this.listenerRegistrar != null) {
                  var2 = CDS.this.listenerRegistrar;
                  CDS.this.listenerRegistrar = null;
               }
            }
         }

         if (var2 != null) {
            var2.cancel();
         }

         if (var3 || !KernelStatus.isServer()) {
            CDS.this.lookupDDAndCalloutListener(CDS.this.pendingRegistration, false);
            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("ddListenerRegistrar has finished registering all of the listeners pendingRegistration map.");
            }
         }

      }
   }
}
