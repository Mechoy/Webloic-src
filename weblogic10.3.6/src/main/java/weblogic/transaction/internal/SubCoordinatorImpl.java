package weblogic.transaction.internal;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import javax.transaction.SystemException;
import javax.transaction.xa.Xid;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.HostID;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.TransactionSystemException;
import weblogic.transaction.nonxa.NonXAException;

public class SubCoordinatorImpl implements SubCoordinator3, SubCoordinatorOneway3, Constants, NotificationBroadcaster, NotificationListener, SubCoordinatorRM, SubCoordinatorOneway4, SubCoordinatorOneway5, SubCoordinatorOneway6 {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private NotificationBroadcasterImpl notificationBroadcaster = new NotificationBroadcasterImpl(this);
   private static final boolean INSTR_ENABLED;
   private Map commitUsernameStats;
   private Map rollbackUsernameStats;

   public SubCoordinatorImpl() {
      if (INSTR_ENABLED) {
         this.commitUsernameStats = new HashMap();
         this.rollbackUsernameStats = new HashMap();
      }

   }

   public void startPrePrepareAndChain(PropagationContext var1, int var2) {
      boolean var3 = false;

      ServerTransactionImpl var4;
      try {
         var4 = (ServerTransactionImpl)var1.getTransaction();
      } catch (TransactionSystemException var9) {
         return;
      }

      CoordinatorOneway var5 = null;

      try {
         if (var4 == null) {
            return;
         }

         var5 = this.getCoordinator(var4.getCoordinatorDescriptor(), var4);
         if (var5 == null) {
            return;
         }

         var4.localPrePrepareAndChain();
         if (var4.allSCsPrePrepared()) {
            var3 = true;
         }
      } catch (AbortRequestedException var8) {
         var3 = true;
      }

      if (var3) {
         try {
            if (var4.isCancelled()) {
               var4.globalRollback();
            } else if (var5 != null) {
               if (var4.isCoordinatingTransaction()) {
                  var4.ackPrePrepare();
               } else {
                  try {
                     CoordinatorDescriptor var6 = var4.getCoordinatorDescriptor();
                     String var7 = var6.getCoordinatorURL();
                     SecureAction.runAction(kernelID, new AckPrePrepareAction(var5, var4.getRequestPropagationContext()), CoordinatorDescriptor.getServerURL(var7), "co.ackPrePrepare");
                  } catch (Exception var10) {
                     if (TxDebug.JTA2PC.isDebugEnabled()) {
                        TxDebug.JTA2PC.debug("ackPrePrepare FAILED", var10);
                     }
                  }
               }
            }
         } catch (Exception var11) {
            if (TxDebug.JTA2PC.isDebugEnabled()) {
               TxDebug.JTA2PC.debug("startPrePrepareAndChain FAILED", var11);
            }
         }
      }

   }

   public void startPrepare(Xid var1, String var2, String[] var3, int var4) {
      this.startPrepare(var1, var2, var3, var4, (Map)null);
   }

   public void startPrepare(Xid var1, String var2, String[] var3, int var4, Map var5) {
      byte var6 = 0;
      ServerTransactionImpl var7 = null;
      CoordinatorOneway var8 = null;

      try {
         var7 = this.getTM().getOrCreateTransaction(var1, var4, var4);
         var7.setCoordinatorURL(var2);
         var8 = this.getCoordinator(var7.getCoordinatorDescriptor(), var7);
         if (var8 == null) {
            throw new SystemException("Could not obtain coordinator at " + var2);
         }

         if (var5 != null) {
            if (TxDebug.JTA2PC.isDebugEnabled()) {
               TxDebug.JTA2PC.debug("startPrepare: Add properties: " + var5);
            }

            var7.addProperties(var5);
         }

         if (INSTR_ENABLED) {
            var7.check("SCBeforePrepare", this.getTM().getLocalCoordinatorDescriptor().getServerName());
         }

         var7.localPrepare(var3);
         if (INSTR_ENABLED) {
            var7.check("SCAfterPrepare", this.getTM().getLocalCoordinatorDescriptor().getServerName());
         }
      } catch (SystemException var13) {
         if (TxDebug.JTA2PC.isDebugEnabled()) {
            TxDebug.JTA2PC.debug("startPrepare FAILED", var13);
         }

         return;
      }

      try {
         if (var8 != null) {
            CoordinatorDescriptor var9;
            if (var7.isCancelled()) {
               try {
                  var9 = var7.getCoordinatorDescriptor();
                  SecureAction.runAction(kernelID, new StartRollbackAction(var8, var7.getRequestPropagationContext()), CoordinatorDescriptor.getServerURL(var2), "co.startRollback");
               } catch (Exception var11) {
                  if (TxDebug.JTA2PC.isDebugEnabled()) {
                     TxDebug.JTA2PC.debug("startRollback FAILED", var11);
                  }
               }
            } else if (var7.isPrepared()) {
               try {
                  var9 = var7.getCoordinatorDescriptor();
                  SecureAction.runAction(kernelID, new AckPrepareAction(var8, var1, this.getScUrl(), var6), CoordinatorDescriptor.getServerURL(var2), "co.ackPrepare");
               } catch (Exception var10) {
                  if (TxDebug.JTA2PC.isDebugEnabled()) {
                     TxDebug.JTA2PC.debug("ackPrepare FAILED", var10);
                  }
               }
            }
         }
      } catch (Exception var12) {
         if (TxDebug.JTA2PC.isDebugEnabled()) {
            TxDebug.JTA2PC.debug("startPrepare FAILED", var12);
         }
      }

   }

   public final void startCommit(Xid var1, String var2, String[] var3, boolean var4, boolean var5) {
      this.startCommit(var1, var2, var3, var4, var5, (AuthenticatedUser)null);
   }

   public final void startCommit(Xid var1, String var2, String[] var3, boolean var4, boolean var5, AuthenticatedUser var6) {
      this.startCommit(var1, var2, var3, var4, var5, var6, (Map)null);
   }

   public final void startCommit(Xid var1, String var2, String[] var3, boolean var4, boolean var5, AuthenticatedUser var6, Map var7) {
      if (INSTR_ENABLED && !var5) {
         this.incrementUsernameStats(this.commitUsernameStats);
      }

      AuthenticatedSubject var8 = null;
      if (var6 == null) {
         var8 = SubjectUtils.getAnonymousSubject();
      } else {
         var8 = SecurityServiceManager.getASFromWire((AuthenticatedSubject)var6);
      }

      int var10;
      if (TxDebug.JTA2PC.isDebugEnabled()) {
         StringBuffer var9 = new StringBuffer(50);
         var10 = var3 == null ? 0 : var3.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            var9.append(" " + var3[var11]);
         }

         TxDebug.JTA2PC.debug("startCommit: " + var1 + " for coordinator=" + var2 + " resources=" + var9);
      }

      PeerInfo var23 = null;

      try {
         EndPoint var24 = ServerHelper.getClientEndPoint();
         if (var24 != null && var24 instanceof PeerInfoable) {
            var23 = ((PeerInfoable)var24).getPeerInfo();
         }
      } catch (ServerNotActiveException var22) {
      }

      if (var23 != null && var23.isServer()) {
         if (ReceiveSecureAction.stranger(this.getHostID(), "startCommit")) {
            TXLogger.logUserNotAuthorizedForStartCommit(this.getUserName());
         } else {
            if (TxDebug.JTA2PC.isDebugEnabled()) {
               TxDebug.JTA2PC.debug("startCommit: About to do start.commit");
            }

            var10 = this.getTM().getDefaultTimeoutSeconds();
            ServerTransactionImpl var25 = null;

            try {
               CoordinatorOneway var12 = null;
               CoordinatorOneway2 var13 = null;

               try {
                  var25 = this.getTM().getOrCreateTransaction(var1, var10, var10);
                  var12 = this.getCoordinator(ServerCoordinatorDescriptor.getOrCreate(var2), var25);
                  if (var12 == null) {
                     throw new SystemException("Could not obtain coordinator at " + var2);
                  }

                  if (var12 instanceof CoordinatorOneway2) {
                     var13 = (CoordinatorOneway2)var12;
                  }

                  if (var7 != null) {
                     if (TxDebug.JTA2PC.isDebugEnabled()) {
                        TxDebug.JTA2PC.debug("startCommit: Add properties: " + var7);
                     }

                     var25.addProperties(var7);
                  }

                  if (INSTR_ENABLED && !var5) {
                     var25.check("SCBeforeCommit", this.getTM().getLocalCoordinatorDescriptor().getServerName());
                  }

                  if (!var25.localCommit(var3, var4, var5, var8)) {
                     if (TxDebug.JTA2PC.isDebugEnabled()) {
                        TxDebug.JTA2PC.debug("startCommit: local commit failed, not responding");
                     }

                     if (INSTR_ENABLED && !var5) {
                        var25.check("SCAfterCommit", this.getTM().getLocalCoordinatorDescriptor().getServerName());
                     }

                     return;
                  }

                  if (INSTR_ENABLED && !var5) {
                     var25.check("SCAfterCommit", this.getTM().getLocalCoordinatorDescriptor().getServerName());
                  }

                  short var14 = var25.getHeuristicStatus(4);
                  CoordinatorDescriptor var15;
                  if (var14 == 0) {
                     if (var13 != null) {
                        try {
                           var15 = var25.getCoordinatorDescriptor();
                           SecureAction.runAction(kernelID, new AckCommitAction((CoordinatorOneway)null, var13, var1, this.getScUrl(), var25.getCommittedResources()), CoordinatorDescriptor.getServerURL(var2), "co2.ackCommit");
                        } catch (Exception var19) {
                        }
                     } else if (var12 != null) {
                        try {
                           var15 = var25.getCoordinatorDescriptor();
                           SecureAction.runAction(kernelID, new AckCommitAction(var12, (CoordinatorOneway2)null, var1, this.getScUrl(), (String[])null), CoordinatorDescriptor.getServerURL(var2), "co.ackCommit");
                        } catch (Exception var18) {
                        }
                     }
                  } else {
                     TXLogger.logHeuristicCompletion(var25.toString(), var25.getHeuristicErrorMessage());
                     if (var13 != null) {
                        try {
                           var15 = var25.getCoordinatorDescriptor();
                           SecureAction.runAction(kernelID, new NakCommitAction((CoordinatorOneway)null, var13, var1, this.getScUrl(), var14, var25.getHeuristicErrorMessage(), var25.getCommittedResources(), var25.getRolledbackResources()), CoordinatorDescriptor.getServerURL(var2), "co2.nakCommit");
                        } catch (Exception var17) {
                        }
                     } else if (var12 != null) {
                        try {
                           var15 = var25.getCoordinatorDescriptor();
                           SecureAction.runAction(kernelID, new NakCommitAction(var12, (CoordinatorOneway2)null, var1, this.getScUrl(), var14, var25.getHeuristicErrorMessage(), (String[])null, (String[])null), CoordinatorDescriptor.getServerURL(var2), "co.nakCommit");
                        } catch (Exception var16) {
                        }
                     }
                  }
               } catch (SystemException var20) {
               }
            } catch (Exception var21) {
            }

         }
      } else {
         TXLogger.logUserNotAuthorizedForStartCommit(this.getUserName());
      }
   }

   public void startRollback(Xid var1, String var2, String[] var3) {
      this.startRollback(var1, var2, var3, (AuthenticatedUser)null);
   }

   public void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4) {
      this.startRollback(var1, var2, var3, var4, (String[])null);
   }

   public void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4, String[] var5) {
      this.startRollback(var1, var2, var3, var4, var5, (Map)null);
   }

   public void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4, String[] var5, Map var6) {
      this.startRollback(var1, var2, var3, var4, var5, var6, false);
   }

   public void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4, String[] var5, Map var6, boolean var7) {
      boolean var10000 = this.getTM().getTransaction(var1) == null;
      if (INSTR_ENABLED && !var7) {
         this.incrementUsernameStats(this.rollbackUsernameStats);
      }

      AuthenticatedSubject var9 = null;
      if (var4 == null) {
         var9 = SubjectUtils.getAnonymousSubject();
      } else {
         var9 = SecurityServiceManager.getASFromWire((AuthenticatedSubject)var4);
      }

      int var11;
      if (TxDebug.JTA2PC.isDebugEnabled()) {
         StringBuffer var10 = new StringBuffer(50);
         var11 = var3 == null ? 0 : var3.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            if (var12 > 0) {
               var10.append(" ");
            }

            var10.append(var3[var12]);
         }

         TxDebug.JTA2PC.debug("startRollback: " + var1 + " for coordinator=" + var2 + " resources=" + var10);
      }

      PeerInfo var28 = null;

      try {
         EndPoint var29 = ServerHelper.getClientEndPoint();
         if (var29 != null && var29 instanceof PeerInfoable) {
            var28 = ((PeerInfoable)var29).getPeerInfo();
         }
      } catch (ServerNotActiveException var20) {
      }

      if (var28 != null && var28.isServer()) {
         if (ReceiveSecureAction.stranger(this.getHostID(), "startRollback")) {
            TXLogger.logUserNotAuthorizedForStartRollback(this.getUserName());
         } else {
            try {
               var11 = this.getTM().getDefaultTimeoutSeconds();
               CoordinatorOneway var30 = null;
               CoordinatorOneway2 var13 = null;
               ServerTransactionImpl var14 = this.getTM().getOrCreateTransaction(var1, var11, var11);

               CoordinatorDescriptor var16;
               try {
                  var30 = this.getCoordinator(ServerCoordinatorDescriptor.getOrCreate(var2), var14);
                  if (var30 == null) {
                     throw new SystemException("Could not obtain coordinator at " + var2);
                  }

                  if (var30 instanceof CoordinatorOneway2) {
                     var13 = (CoordinatorOneway2)var30;
                  }

                  if (var5 != null && var5.length > 0) {
                     ArrayList var31 = new ArrayList();
                     TreeSet var33 = new TreeSet();

                     for(int var17 = 0; var17 < var5.length; ++var17) {
                        var33.add(var5[var17]);
                     }

                     ArrayList var35 = var14.getResourceInfoList();
                     if (var35 != null) {
                        var35 = (ArrayList)var35.clone();
                        Iterator var18 = var35.iterator();

                        while(var18.hasNext()) {
                           ResourceInfo var19 = (ResourceInfo)var18.next();
                           if (!var33.contains(var19.getName())) {
                              var31.add(var19.getName());
                           }
                        }
                     }

                     if (var31.size() > 0) {
                        if (var3 != null && var3.length > 0) {
                           for(int var36 = 0; var36 < var3.length; ++var36) {
                              var31.add(var3[var36]);
                           }
                        }

                        var3 = (String[])((String[])var31.toArray(new String[var31.size()]));
                     }
                  }

                  if (var6 != null) {
                     if (TxDebug.JTA2PC.isDebugEnabled()) {
                        TxDebug.JTA2PC.debug("startRollback: Add properties: " + var6);
                     }

                     var14.addProperties(var6);
                  }

                  if (INSTR_ENABLED && !var7) {
                     var14.check("SCBeforeRollback", this.getTM().getLocalCoordinatorDescriptor().getServerName());
                  }

                  if (!var14.localRollback(var3, var9)) {
                     if (TxDebug.JTA2PC.isDebugEnabled()) {
                        TxDebug.JTA2PC.debug("startRollback: local rollback failed, not responding");
                     }

                     if (INSTR_ENABLED && !var7) {
                        var14.check("SCAfterRollback", this.getTM().getLocalCoordinatorDescriptor().getServerName());
                     }

                     return;
                  }

                  if (INSTR_ENABLED && !var7) {
                     var14.check("SCAfterRollback", this.getTM().getLocalCoordinatorDescriptor().getServerName());
                  }

                  short var32 = var14.getHeuristicStatus(8);
                  if (var32 == 0) {
                     if (var13 != null) {
                        try {
                           var16 = var14.getCoordinatorDescriptor();
                           SecureAction.runAction(kernelID, new AckRollbackAction((CoordinatorOneway)null, var13, var1, this.getScUrl(), var14.getRolledbackResources()), CoordinatorDescriptor.getServerURL(var2), "co2.ackRollback");
                        } catch (Exception var25) {
                           if (TxDebug.JTA2PC.isDebugEnabled()) {
                              TxDebug.JTA2PC.debug("ackRollback remote exception: " + var25);
                           }
                        }
                     } else if (var30 != null) {
                        try {
                           var16 = var14.getCoordinatorDescriptor();
                           SecureAction.runAction(kernelID, new AckRollbackAction(var30, (CoordinatorOneway2)null, var1, this.getScUrl(), (String[])null), CoordinatorDescriptor.getServerURL(var2), "co.ackRollback");
                        } catch (Exception var24) {
                           if (TxDebug.JTA2PC.isDebugEnabled()) {
                              TxDebug.JTA2PC.debug("ackRollback remote exception: " + var24);
                           }
                        }
                     }
                  } else {
                     String var34 = var14.getHeuristicErrorMessage();
                     TXLogger.logHeuristicCompletion(var14.toString(), var34);
                     CoordinatorDescriptor var37;
                     if (var13 != null) {
                        try {
                           var37 = var14.getCoordinatorDescriptor();
                           SecureAction.runAction(kernelID, new NakRollbackAction((CoordinatorOneway)null, var13, var1, this.getScUrl(), var32, var14.getHeuristicErrorMessage(), var14.getCommittedResources(), var14.getRolledbackResources()), CoordinatorDescriptor.getServerURL(var2), "co2.nakRollback");
                        } catch (Exception var23) {
                           if (TxDebug.JTA2PC.isDebugEnabled()) {
                              TxDebug.JTA2PC.debug("nakRollback remote exception: " + var23);
                           }
                        }
                     } else if (var30 != null) {
                        try {
                           var37 = var14.getCoordinatorDescriptor();
                           SecureAction.runAction(kernelID, new NakRollbackAction(var30, (CoordinatorOneway2)null, var1, this.getScUrl(), var32, var14.getHeuristicErrorMessage(), (String[])null, (String[])null), CoordinatorDescriptor.getServerURL(var2), "co.nakRollback");
                        } catch (Exception var22) {
                           if (TxDebug.JTA2PC.isDebugEnabled()) {
                              TxDebug.JTA2PC.debug("nakRollback remote exception: " + var22);
                           }
                        }
                     } else if (var30 != null) {
                     }
                  }
               } catch (SystemException var26) {
                  SystemException var15 = var26;
                  if (var30 != null) {
                     try {
                        var16 = var14.getCoordinatorDescriptor();
                        SecureAction.runAction(kernelID, new NakRollbackAction(var30, (CoordinatorOneway2)null, var1, this.getScUrl(), (short)2, var15.toString(), (String[])null, (String[])null), CoordinatorDescriptor.getServerURL(var2), "co.nakRollback");
                     } catch (Exception var21) {
                        if (TxDebug.JTA2PC.isDebugEnabled()) {
                           TxDebug.JTA2PC.debug("nakRollback remote exception: " + var21);
                        }
                     }
                  }
               }
            } catch (Exception var27) {
               if (TxDebug.JTA2PC.isDebugEnabled()) {
                  TxDebug.JTA2PC.debug("startRollback remote exception: " + var27);
               }
            }

         }
      } else {
         TXLogger.logUserNotAuthorizedForStartRollback(this.getUserName());
      }
   }

   public void startRollback(Xid[] var1) throws RemoteException {
      int var2 = var1 == null ? 0 : var1.length;
      if (TxDebug.JTA2PC.isDebugEnabled()) {
         TxDebug.JTA2PC.debug("SC.startRollback: " + var2 + " XIDs");
      }

      if (ReceiveSecureAction.stranger(this.getHostID(), "sc.startRollback")) {
         TXLogger.logUserNotAuthorizedForRollback(this.getUserName());
      } else {
         ServerTransactionImpl var3 = null;

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var3 = (ServerTransactionImpl)this.getTM().getTransaction(var1[var4]);
            if (var3 != null) {
               var3.localRollback();
            }
         }

      }
   }

   public Xid[] recover(String var1, String var2) throws SystemException {
      if (ReceiveSecureAction.stranger(this.getHostID(), "recover")) {
         TXLogger.logUserNotAuthorizedForRecover(this.getUserName());
         return null;
      } else {
         try {
            ServerSCInfo var3 = new ServerSCInfo(this.getScUrl());
            return var3.recover(var1, ServerCoordinatorDescriptor.getOrCreate(var2), (ResourceDescriptor)null);
         } catch (SystemException var4) {
            throw var4;
         } catch (Exception var5) {
            throw new SystemException("Subcoordinator raised exception on recover: " + var5);
         }
      }
   }

   public void rollback(String var1, Xid[] var2) throws SystemException, RemoteException {
      PeerInfo var3 = null;

      try {
         EndPoint var4 = ServerHelper.getClientEndPoint();
         if (var4 != null && var4 instanceof PeerInfoable) {
            var3 = ((PeerInfoable)var4).getPeerInfo();
         }
      } catch (ServerNotActiveException var5) {
      }

      if (var3 != null && var3.isServer() && ReceiveSecureAction.stranger(this.getHostID(), "rollback recovery")) {
         TXLogger.logUserNotAuthorizedForRollback(this.getUserName());
      } else {
         ServerSCInfo var6 = new ServerSCInfo(this.getScUrl());
         var6.rollback(var1, var2);
      }
   }

   public void nonXAResourceCommit(Xid var1, boolean var2, String var3) throws SystemException, RemoteException {
      PeerInfo var4 = null;

      try {
         EndPoint var5 = ServerHelper.getClientEndPoint();
         if (var5 != null && var5 instanceof PeerInfoable) {
            var4 = ((PeerInfoable)var5).getPeerInfo();
         }
      } catch (ServerNotActiveException var9) {
      }

      if (var4 != null && var4.isServer()) {
         if (ReceiveSecureAction.stranger(this.getHostID(), "nonXAResourceCommit")) {
            TXLogger.logUserNotAuthorizedForNonXACommit(this.getUserName());
            throw new SystemException("Failed to commit non XA resource");
         } else {
            int var11 = this.getTM().getDefaultTimeoutSeconds();
            ServerTransactionImpl var6 = null;
            var6 = this.getTM().getOrCreateTransaction(var1, var11, var11);
            ServerResourceInfo var7 = (ServerResourceInfo)var6.getResourceInfo(var3);
            if (var7 instanceof NonXAServerResourceInfo) {
               try {
                  ((NonXAServerResourceInfo)var7).commit(var6, var2, false, true);
               } catch (NonXAException var10) {
                  if (TxDebug.JTANonXA.isDebugEnabled()) {
                     TxDebug.JTANonXA.debug("SC.nonXAResourceCommit: " + var1 + ", failed", var10);
                  }

                  throw new RemoteException("NonXAResource commit failed: " + var10);
               }
            } else {
               throw new SystemException("Attempt to perform a non-XA resource commit on a resource that is not a NonXAResource");
            }
         }
      } else {
         TXLogger.logUserNotAuthorizedForNonXACommit(this.getUserName());
         throw new SystemException("Failed to commit non XA resource");
      }
   }

   public void forceLocalRollback(Xid var1) throws SystemException, RemoteException {
      if (TxDebug.JTA2PC.isDebugEnabled()) {
         TxDebug.JTA2PC.debug("SC.forceLocalRollback: " + var1);
      }

      if (ReceiveSecureAction.stranger(this.getHostID(), "forceLocalRollback")) {
         TXLogger.logUserNotAuthorizedForForceLocalRollback(this.getUserName());
      } else {
         ServerTransactionImpl var2 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var2 == null) {
            TXLogger.logForceLocalRollbackNoTx(var1.toString());
         } else {
            var2.forceLocalRollback();
         }
      }
   }

   public void forceLocalCommit(Xid var1) throws SystemException, RemoteException {
      if (TxDebug.JTA2PC.isDebugEnabled()) {
         TxDebug.JTA2PC.debug("SC.forceLocalCommit: " + var1);
      }

      if (ReceiveSecureAction.stranger(this.getHostID(), "forceLocalCommit")) {
         TXLogger.logUserNotAuthorizedForForceLocalCommit(this.getUserName());
      } else {
         ServerTransactionImpl var2 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var2 == null) {
            TXLogger.logForceLocalCommitNoTx(var1.toString());
         } else {
            var2.forceLocalCommit();
         }
      }
   }

   public Map getProperties(String var1) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "getPoperties")) {
         TXLogger.logUserNotAuthorizedForGetProperties(this.getUserName());
         return null;
      } else {
         ResourceDescriptor var2 = XAResourceDescriptor.get(var1);
         return var2 == null ? null : var2.getProperties();
      }
   }

   protected ServerTransactionManagerImpl getTM() {
      return (ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager();
   }

   protected HostID getHostID() {
      try {
         EndPoint var1 = ServerHelper.getClientEndPoint();
         HostID var2 = var1.getHostID();
         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("RMI call coming from = " + ((ServerIdentity)var2).getDomainName());
         }

         return var2;
      } catch (Exception var3) {
         return null;
      }
   }

   private CoordinatorOneway getCoordinator(CoordinatorDescriptor var1, TransactionImpl var2) {
      return JNDIAdvertiser.getCoordinator(var1, var2);
   }

   private String getScUrl() {
      return this.getTM().getLocalCoordinatorURL();
   }

   public void addNotificationListener(NotificationListener var1, Object var2) throws IllegalArgumentException {
      this.notificationBroadcaster.addNotificationListener(var1, var2);
   }

   public void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
      this.notificationBroadcaster.removeNotificationListener(var1);
   }

   public void handleNotification(Notification var1, Object var2) {
      if (TxDebug.JTANaming.isDebugEnabled()) {
         TxDebug.JTANaming.debug("SubCoordinatorImpl.handleNotification(" + var2 + ")");
      }

      if (ReceiveSecureAction.stranger(this.getHostID(), "handleNotification")) {
         TXLogger.logUserNotAuthorizedForStartCommit(this.getUserName());
      } else {
         if (var1 instanceof PropertyChangeNotification) {
            PropertyChangeNotification var3 = (PropertyChangeNotification)var1;
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("SubCoordinatorImpl.handleNotification(notificaiton=" + var1 + ", handback=" + var2 + ")");
            }

            if ("XAResources".equals(var3.getName())) {
               ((ServerCoordinatorDescriptorManager)PlatformHelper.getPlatformHelper().getCoordinatorDescriptorManager()).updateXAResources((String)var2, (String[])((String[])var3.getNewValue()));
            } else if ("NonXAResources".equals(var3.getName())) {
               ((ServerCoordinatorDescriptorManager)PlatformHelper.getPlatformHelper().getCoordinatorDescriptorManager()).updateNonXAResources((String)var2, (String[])((String[])var3.getNewValue()));
            }
         }

      }
   }

   public Map getSubCoordinatorInfo(String var1) throws RemoteException {
      if (TxDebug.JTANaming.isDebugEnabled()) {
         TxDebug.JTANaming.debug("SubCoordinatorImpl.getSubCoordinatorInfo(" + var1 + ")");
      }

      if (ReceiveSecureAction.stranger(this.getHostID(), "getSubCoordinatorInfo")) {
         TXLogger.logUserNotAuthorizedForGetSubCoordinatorInfo(this.getUserName());
         return null;
      } else {
         CoordinatorDescriptor var2 = ServerCoordinatorDescriptor.getOrCreate(var1);
         ServerCoordinatorDescriptor var3 = ((ServerCoordinatorDescriptorManager)PlatformHelper.getPlatformHelper().getCoordinatorDescriptorManager()).getLocalCoordinatorDescriptor();
         return var3 != null ? SubCoordinatorInfo.createMap(var3) : null;
      }
   }

   public void broadcastXAResourceRegistrationChange(String[] var1, String[] var2) {
      this.notificationBroadcaster.broadcastXAResourceRegistrationChange(var1, var2);
   }

   public void broadcastNonXAResourceRegistrationChange(String[] var1, String[] var2) {
      this.notificationBroadcaster.broadcastNonXAResourceRegistrationChange(var1, var2);
   }

   long getCommitUsernameStat(String var1) {
      if (this.commitUsernameStats == null) {
         return -1L;
      } else {
         synchronized(this.commitUsernameStats) {
            Long var3 = (Long)this.commitUsernameStats.get(var1);
            return var3 == null ? 0L : var3;
         }
      }
   }

   Map getCommitUsernameStats() {
      if (this.commitUsernameStats == null) {
         return null;
      } else {
         synchronized(this.commitUsernameStats) {
            return (Map)((HashMap)this.commitUsernameStats).clone();
         }
      }
   }

   long getRollbackUsernameStat(String var1) {
      if (this.rollbackUsernameStats == null) {
         return -1L;
      } else {
         synchronized(this.rollbackUsernameStats) {
            Long var3 = (Long)this.rollbackUsernameStats.get(var1);
            return var3 == null ? 0L : var3;
         }
      }
   }

   Map getRollbackUsernameStats() {
      if (this.rollbackUsernameStats == null) {
         return null;
      } else {
         synchronized(this.rollbackUsernameStats) {
            return (Map)((HashMap)this.rollbackUsernameStats).clone();
         }
      }
   }

   private String getUserName() {
      return SubjectUtils.getUsername(SecurityServiceManager.getCurrentSubject(kernelID));
   }

   private void incrementUsernameStats(Map var1) {
      synchronized(var1) {
         String var3 = SubjectUtils.getUsername(SecurityServiceManager.getCurrentSubject(kernelID));
         Long var4 = (Long)var1.get(var3);
         if (var4 != null) {
            var4 = new Long(var4 + 1L);
         } else {
            var4 = new Long(1L);
         }

         var1.put(var3, var4);
      }
   }

   static {
      String var0 = System.getProperty("weblogic.transaction.EnableInstrumentedTM");
      INSTR_ENABLED = var0 != null && var0.equals("true");
   }

   private class NakRollbackAction implements PrivilegedExceptionAction {
      private CoordinatorOneway co = null;
      private CoordinatorOneway2 co2 = null;
      private Xid xid = null;
      private String scUrl = null;
      private short heurStatus = 0;
      private String heuristicErrorMessage = null;
      private String[] committedResources = null;
      private String[] rolledbackResources = null;

      NakRollbackAction(CoordinatorOneway var2, CoordinatorOneway2 var3, Xid var4, String var5, short var6, String var7, String[] var8, String[] var9) {
         this.co = var2;
         this.co2 = var3;
         this.xid = var4;
         this.scUrl = var5;
         this.heurStatus = var6;
         this.heuristicErrorMessage = var7;
         this.committedResources = var8;
         this.rolledbackResources = var9;
      }

      public Object run() throws Exception {
         if (this.co2 != null) {
            this.co2.nakRollback(this.xid, SubCoordinatorImpl.this.getScUrl(), this.heurStatus, this.heuristicErrorMessage, this.committedResources, this.rolledbackResources);
         } else if (this.co != null) {
            this.co.nakRollback(this.xid, SubCoordinatorImpl.this.getScUrl(), this.heurStatus, this.heuristicErrorMessage);
         }

         return null;
      }
   }

   private class AckRollbackAction implements PrivilegedExceptionAction {
      private CoordinatorOneway co = null;
      private CoordinatorOneway2 co2 = null;
      private Xid xid;
      private String scUrl = null;
      private String[] rolledbackResources;

      AckRollbackAction(CoordinatorOneway var2, CoordinatorOneway2 var3, Xid var4, String var5, String[] var6) {
         this.co = var2;
         this.co2 = var3;
         this.xid = var4;
         this.scUrl = var5;
         this.rolledbackResources = var6;
      }

      public Object run() throws Exception {
         if (this.co2 != null) {
            this.co2.ackRollback(this.xid, SubCoordinatorImpl.this.getScUrl(), this.rolledbackResources);
         } else if (this.co != null) {
            this.co.ackRollback(this.xid, this.scUrl);
         }

         return null;
      }
   }

   private class NakCommitAction implements PrivilegedExceptionAction {
      private CoordinatorOneway co = null;
      private CoordinatorOneway2 co2 = null;
      private Xid xid = null;
      private String scUrl = null;
      private short heurStatus = 0;
      private String heuristicErrorMessage = null;
      private String[] committedResources = null;
      private String[] rolledbackResources = null;

      NakCommitAction(CoordinatorOneway var2, CoordinatorOneway2 var3, Xid var4, String var5, short var6, String var7, String[] var8, String[] var9) {
         this.co = var2;
         this.co2 = var3;
         this.xid = var4;
         this.scUrl = var5;
         this.heurStatus = var6;
         this.heuristicErrorMessage = var7;
         this.committedResources = var8;
         this.rolledbackResources = var9;
      }

      public Object run() throws Exception {
         if (this.co2 != null) {
            this.co2.nakCommit(this.xid, SubCoordinatorImpl.this.getScUrl(), this.heurStatus, this.heuristicErrorMessage, this.committedResources, this.rolledbackResources);
         } else if (this.co != null) {
            this.co.nakCommit(this.xid, SubCoordinatorImpl.this.getScUrl(), this.heurStatus, this.heuristicErrorMessage);
         }

         return null;
      }
   }

   private class AckCommitAction implements PrivilegedExceptionAction {
      private CoordinatorOneway co = null;
      private CoordinatorOneway2 co2 = null;
      private Xid xid = null;
      private String scUrl = null;
      private String[] committedResources;

      AckCommitAction(CoordinatorOneway var2, CoordinatorOneway2 var3, Xid var4, String var5, String[] var6) {
         this.co = var2;
         this.co2 = var3;
         this.xid = var4;
         this.scUrl = var5;
         this.committedResources = var6;
      }

      public Object run() throws Exception {
         if (this.co2 != null) {
            this.co2.ackCommit(this.xid, SubCoordinatorImpl.this.getScUrl(), this.committedResources);
         } else if (this.co != null) {
            this.co.ackCommit(this.xid, this.scUrl);
         }

         return null;
      }
   }

   private class AckPrepareAction implements PrivilegedExceptionAction {
      private CoordinatorOneway co = null;
      private Xid xid = null;
      private String scUrl = null;
      private int vote = 0;

      AckPrepareAction(CoordinatorOneway var2, Xid var3, String var4, int var5) {
         this.co = var2;
         this.xid = var3;
         this.scUrl = var4;
         this.vote = var5;
      }

      public Object run() throws Exception {
         this.co.ackPrepare(this.xid, SubCoordinatorImpl.this.getScUrl(), this.vote);
         return null;
      }
   }

   private class StartRollbackAction implements PrivilegedExceptionAction {
      private CoordinatorOneway co = null;
      private PropagationContext propagationCtx = null;

      StartRollbackAction(CoordinatorOneway var2, PropagationContext var3) {
         this.co = var2;
         this.propagationCtx = var3;
      }

      public Object run() throws Exception {
         this.co.startRollback(this.propagationCtx);
         return null;
      }
   }

   private class AckPrePrepareAction implements PrivilegedExceptionAction {
      private CoordinatorOneway co = null;
      private PropagationContext propagationCtx = null;

      AckPrePrepareAction(CoordinatorOneway var2, PropagationContext var3) {
         this.co = var2;
         this.propagationCtx = var3;
      }

      public Object run() throws Exception {
         this.co.ackPrePrepare(this.propagationCtx);
         return null;
      }
   }
}
