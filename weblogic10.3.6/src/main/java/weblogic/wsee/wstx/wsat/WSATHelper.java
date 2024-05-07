package weblogic.wsee.wstx.wsat;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import javax.xml.soap.SOAPException;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.WLXid;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.wstx.TransactionIdHelper;
import weblogic.wsee.wstx.TransactionServices;
import weblogic.wsee.wstx.internal.BranchXidImpl;
import weblogic.wsee.wstx.internal.WLSTransactionServicesImpl;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsat.common.CoordinatorIF;
import weblogic.wsee.wstx.wsat.common.ParticipantIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;
import weblogic.wsee.wstx.wsat.common.client.CoordinatorProxyBuilder;
import weblogic.wsee.wstx.wsat.common.client.ParticipantProxyBuilder;
import weblogic.wsee.wstx.wsat.tube.WSATTubeHelper;

public class WSATHelper<T> {
   public static final WSATHelper V10;
   public static final WSATHelper V11;
   private Map<WSATXAResource, ParticipantIF<T>> m_durableParticipantPortMap = new HashMap();
   private final Object m_durableParticipantPortMapLock = new Object();
   private Map<Xid, WSATXAResource> m_durableParticipantXAResourceMap = new HashMap();
   private final Object m_durableParticipantXAResourceMapLock = new Object();
   private Map<Xid, ParticipantIF<T>> m_volatileParticipantPortMap = new HashMap();
   private final Object m_volatileParticipantPortMapLock = new Object();
   private Map<Xid, WSATSynchronization> m_volatileParticipantSynchronizationMap = new HashMap();
   private final Object m_volatileParticipantSynchronizationMapLock = new Object();
   private final int m_waitForReplyTimeout = new Integer(System.getProperty("weblogic.wsee.wstx.wsat.reply.timeout", "120"));
   private final boolean m_isUseLocalServerAddress = Boolean.valueOf(System.getProperty("weblogic.wsee.wstx.wsat.use.local.server.address", "false"));
   private static final AuthenticatedSubject _kernelId;
   private static final RuntimeAccess _runtimeAccess;
   protected WSATVersion<T> builderFactory;
   private static final DebugLogger debugWSAT;

   WSATHelper WSATVersion(WSATVersion var1) {
      this.builderFactory = var1;
      return this;
   }

   protected WSATHelper() {
   }

   public static WSATHelper getInstance() {
      return V10;
   }

   public static WSATHelper getInstance(Transactional.Version var0) {
      if (var0 != Version.WSAT10 && var0 != Version.DEFAULT) {
         if (var0 != Version.WSAT12 && var0 != Version.WSAT11) {
            throw new WebServiceException("not supported WSAT version");
         } else {
            return V11;
         }
      } else {
         return V10;
      }
   }

   public static TransactionServices getTransactionServices() {
      return WLSTransactionServicesImpl.getInstance();
   }

   public int getWaitForReplyTimeout() {
      return this.m_waitForReplyTimeout * 1000;
   }

   public boolean setDurableParticipantStatus(Xid var1, String var2) {
      WSATXAResource var3;
      synchronized(this.m_durableParticipantXAResourceMapLock) {
         var3 = (WSATXAResource)this.getDurableParticipantXAResourceMap().get(new BranchXidImpl(var1));
      }

      if (var3 == null) {
         WseeWsatLogger.logXidNotInDurableResourceMap(var1, var2);
         return false;
      } else {
         synchronized(var3) {
            var3.setStatus(var2);
            var3.notifyAll();
            return true;
         }
      }
   }

   boolean setVolatileParticipantStatus(Xid var1, String var2) {
      WSATSynchronization var3;
      synchronized(this.m_volatileParticipantSynchronizationMapLock) {
         var3 = (WSATSynchronization)this.m_volatileParticipantSynchronizationMap.get(var1);
      }

      if (var3 == null) {
         if (isDebugEnabled()) {
            WseeWsatLogger.logXidNotInVolatileResourceMap(var1, var2);
         }

         return false;
      } else {
         synchronized(var3) {
            var3.setStatus(var2);
            var3.notifyAll();
            return true;
         }
      }
   }

   void removeDurableParticipant(WSATXAResource var1) {
      synchronized(this.m_durableParticipantPortMapLock) {
         if (this.getDurableParticipantPortMap().containsKey(var1)) {
            this.m_durableParticipantPortMap.remove(var1);
            if (isDebugEnabled()) {
               WseeWsatLogger.logDurablePortRemoved(var1);
            }
         }
      }

      synchronized(this.m_durableParticipantXAResourceMapLock) {
         if (this.getDurableParticipantXAResourceMap().containsKey(var1.getXid())) {
            this.getDurableParticipantXAResourceMap().remove(var1.getXid());
            if (isDebugEnabled()) {
               WseeWsatLogger.logDurableXAResourceRemoved(var1);
            }
         }

      }
   }

   void removeVolatileParticipant(Xid var1) {
      synchronized(this.m_volatileParticipantPortMapLock) {
         if (this.m_volatileParticipantPortMap.containsKey(new BranchXidImpl(var1))) {
            this.m_volatileParticipantPortMap.remove(new BranchXidImpl(var1));
            if (isDebugEnabled()) {
               WseeWsatLogger.logVolatilePortRemoved(new BranchXidImpl(var1));
            }
         }
      }

      synchronized(this.m_volatileParticipantSynchronizationMapLock) {
         if (this.m_volatileParticipantSynchronizationMap.containsKey(new BranchXidImpl(var1))) {
            this.m_volatileParticipantSynchronizationMap.remove(new BranchXidImpl(var1));
            if (isDebugEnabled()) {
               WseeWsatLogger.logVolatileSynchronizationRemoved(var1);
            }
         }

      }
   }

   public void prepare(EndpointReference var1, Xid var2, WSATXAResource var3) throws XAException {
      if (isDebugEnabled()) {
         WseeWsatLogger.logAboutToSendPrepare(var2, Thread.currentThread());
      }

      synchronized(this.m_durableParticipantXAResourceMapLock) {
         BranchXidImpl var5 = new BranchXidImpl(var2);
         this.getDurableParticipantXAResourceMap().put(var5, var3);
      }

      if (isDebugEnabled()) {
         WseeWsatLogger.logDurableParticipantXAResourcePlacedInCacheFromPrepare(var2);
      }

      ParticipantIF var4 = this.getDurableParticipantPort(var1, var2, var3);
      Object var8 = this.builderFactory.newNotificationBuilder().build();
      var4.prepare(var8);
      if (isDebugEnabled()) {
         WseeWsatLogger.logPrepareSent(var2, Thread.currentThread());
      }

   }

   public void commit(EndpointReference var1, Xid var2, WSATXAResource var3) throws XAException {
      if (isDebugEnabled()) {
         WseeWsatLogger.logAboutToSendCommit(var2, Thread.currentThread());
      }

      Object var4 = this.builderFactory.newNotificationBuilder().build();
      this.getDurableParticipantPort(var1, var2, var3).commit(var4);
      if (isDebugEnabled()) {
         WseeWsatLogger.logCommitSent(var2, Thread.currentThread());
      }

   }

   public void rollback(EndpointReference var1, Xid var2, WSATXAResource var3) throws XAException {
      if (isDebugEnabled()) {
         WseeWsatLogger.logAboutToSendRollback(var2, Thread.currentThread());
      }

      synchronized(this.m_durableParticipantXAResourceMapLock) {
         BranchXidImpl var5 = new BranchXidImpl(var2);
         this.getDurableParticipantXAResourceMap().put(var5, var3);
      }

      if (isDebugEnabled()) {
         WseeWsatLogger.logRollbackParticipantXAResourcePlacedInCache(var2);
      }

      Object var4 = this.builderFactory.newNotificationBuilder().build();
      this.getDurableParticipantPort(var1, var2, var3).rollback(var4);
      if (isDebugEnabled()) {
         WseeWsatLogger.logRollbackSent(var2, Thread.currentThread());
      }

   }

   public void beforeCompletion(EndpointReference var1, Xid var2, WSATSynchronization var3) throws SOAPException {
      if (isDebugEnabled()) {
         WseeWsatLogger.logAboutToSendPrepareVolatile(var2, Thread.currentThread());
      }

      Object var4 = this.builderFactory.newNotificationBuilder().build();
      this.getVolatileParticipantPort(var1, var2).prepare(var4);
      if (isDebugEnabled()) {
         WseeWsatLogger.logPrepareVolatileSent(var2, Thread.currentThread());
      }

      synchronized(this.m_volatileParticipantSynchronizationMapLock) {
         this.m_volatileParticipantSynchronizationMap.put(new BranchXidImpl(var2), var3);
      }

      if (isDebugEnabled()) {
         WseeWsatLogger.logPrepareParticipantSynchronizationPlacedInCache(var2);
      }

   }

   private ParticipantIF<T> getVolatileParticipantPort(EndpointReference var1, Xid var2) throws SOAPException {
      ParticipantIF var3;
      synchronized(this.m_volatileParticipantPortMapLock) {
         var3 = (ParticipantIF)this.m_volatileParticipantPortMap.get(new BranchXidImpl(var2));
      }

      if (var3 != null) {
         if (isDebugEnabled()) {
            WseeWsatLogger.logVolatileParticipantRetrievedFromCache(var2);
         }

         return var3;
      } else {
         var3 = this.getParticipantPort(var1, var2, (String)null);
         synchronized(this.m_volatileParticipantPortMapLock) {
            this.m_volatileParticipantPortMap.put(new BranchXidImpl(var2), var3);
         }

         if (isDebugEnabled()) {
            WseeWsatLogger.logVolatileParticipantPortPlacedInCache(var2);
         }

         return var3;
      }
   }

   private ParticipantIF<T> getDurableParticipantPort(EndpointReference var1, Xid var2, WSATXAResource var3) throws XAException {
      ParticipantIF var4;
      synchronized(this.m_durableParticipantPortMapLock) {
         var4 = (ParticipantIF)this.getDurableParticipantPortMap().get(var3);
      }

      if (var4 != null) {
         if (isDebugEnabled()) {
            WseeWsatLogger.logDurableParticipantPortRetreivedFromCache(var2);
         }

         return var4;
      } else {
         try {
            var4 = this.getParticipantPort(var1, var2, new String(var3.getXid().getBranchQualifier()));
         } catch (SOAPException var12) {
            if (isDebugEnabled()) {
               WseeWsatLogger.logCannotCreateDurableParticipantPort(var2);
            }

            var12.printStackTrace();
            XAException var6 = new XAException("Unable to create durable participant port:" + var12);
            var6.initCause(var12);
            var6.errorCode = -7;
            throw var6;
         }

         synchronized(this.m_durableParticipantXAResourceMapLock) {
            BranchXidImpl var13 = new BranchXidImpl(var2);
            this.getDurableParticipantXAResourceMap().put(var13, var3);
         }

         synchronized(this.m_durableParticipantPortMapLock) {
            this.getDurableParticipantPortMap().put(var3, var4);
         }

         if (isDebugEnabled()) {
            WseeWsatLogger.logDurableParticipantPortPlacedInCache(var2);
         }

         return var4;
      }
   }

   public ParticipantIF<T> getParticipantPort(EndpointReference var1, Xid var2, String var3) throws SOAPException {
      String var4 = TransactionIdHelper.getInstance().xid2wsatid((WLXid)var2);
      ParticipantProxyBuilder var5 = this.builderFactory.newParticipantProxyBuilder();
      ParticipantIF var6 = ((ParticipantProxyBuilder)((ParticipantProxyBuilder)var5.to(var1)).txIdForReference(var4, var3)).build();
      if (isDebugEnabled()) {
         WseeWsatLogger.logSuccessfullyCreatedParticipantPort(var6, var2);
      }

      return var6;
   }

   public CoordinatorIF<T> getCoordinatorPort(EndpointReference var1, Xid var2) {
      if (isDebugEnabled()) {
         this.debug("WSATHelper.getCoordinatorPort xid:" + var2 + " epr:" + var1);
      }

      String var3 = TransactionIdHelper.getInstance().xid2wsatid((WLXid)var2);
      CoordinatorProxyBuilder var4 = this.builderFactory.newCoordinatorProxyBuilder();
      CoordinatorIF var5 = ((CoordinatorProxyBuilder)((CoordinatorProxyBuilder)var4.to(var1)).txIdForReference(var3, "")).build();
      if (isDebugEnabled()) {
         this.debug("WSATHelper.getCoordinatorPort xid:" + var2 + " epr:" + var1 + " coordinatorProxy:" + var5);
      }

      return var5;
   }

   public String getRoutingAddress() {
      return _runtimeAccess == null ? null : _runtimeAccess.getServerName();
   }

   String getHostAndPort() {
      boolean var1 = WSATTubeHelper.isSSLRequired();
      return this.m_isUseLocalServerAddress ? ServerUtil.getLocalServerPublicURL(var1 ? "https" : "http") : ServerUtil.getHTTPServerURL(var1);
   }

   public String getRegistrationCoordinatorAddress() {
      return this.getHostAndPort() + "/wls-wsat/RegistrationPortTypeRPC";
   }

   public String getCoordinatorAddress() {
      return this.getHostAndPort() + "/wls-wsat/CoordinatorPortType";
   }

   public String getParticipantAddress() {
      return this.getHostAndPort() + "/wls-wsat/ParticipantPortType";
   }

   public String getRegistrationRequesterAddress() {
      return this.getHostAndPort() + "/wls-wsat/RegistrationRequesterPortType";
   }

   public WLXid getXidFromWebServiceContextHeaderList(WebServiceContext var1) {
      String var2 = this.getWSATTidFromWebServiceContextHeaderList(var1);
      return TransactionIdHelper.getInstance().wsatid2xid(var2);
   }

   public String getWSATTidFromWebServiceContextHeaderList(WebServiceContext var1) {
      MessageContext var2 = var1.getMessageContext();
      HeaderList var3 = (HeaderList)var2.get("com.sun.xml.ws.api.message.HeaderList");
      Iterator var4 = var3.getHeaders(WSATConstants.TXID_QNAME, false);
      if (!var4.hasNext()) {
         WseeWsatLogger.logWSATNoContextHeaderList(var1);
         throw new WebServiceException("txid does not exist in header");
      } else {
         String var5 = ((Header)var4.next()).getStringContent();
         if (isDebugEnabled()) {
            WseeWsatLogger.logWLSWSATTxIdInHeader(var5, Thread.currentThread());
         }

         return var5;
      }
   }

   public String getBQualFromWebServiceContextHeaderList(WebServiceContext var1) {
      MessageContext var2 = var1.getMessageContext();
      HeaderList var3 = (HeaderList)var2.get("com.sun.xml.ws.api.message.HeaderList");
      Iterator var4 = var3.getHeaders(WSATConstants.BRANCHQUAL_QNAME, false);
      if (!var4.hasNext()) {
         throw new WebServiceException("branchqual does not exist in header");
      } else {
         String var5 = ((Header)var4.next()).getStringContent();
         if (isDebugEnabled()) {
            WseeWsatLogger.logWLSWSATTxIdInHeader(var5, Thread.currentThread());
         }

         return var5;
      }
   }

   public static boolean isDebugEnabled() {
      return DebugLogger.getDebugLogger("DebugWSAT").isDebugEnabled();
   }

   public Map<WSATXAResource, ParticipantIF<T>> getDurableParticipantPortMap() {
      return this.m_durableParticipantPortMap;
   }

   Map<Xid, WSATXAResource> getDurableParticipantXAResourceMap() {
      return this.m_durableParticipantXAResourceMap;
   }

   public Map<Xid, WSATSynchronization> getVolatileParticipantSynchronizationMap() {
      return this.m_volatileParticipantSynchronizationMap;
   }

   public Map<Xid, ParticipantIF<T>> getVolatileParticipantPortMap() {
      return this.m_volatileParticipantPortMap;
   }

   public void debug(String var1) {
      debugWSAT.debug(var1);
   }

   public static String assignUUID() {
      return UUID.randomUUID().toString();
   }

   static {
      V10 = (new WSATHelper()).WSATVersion(WSATVersion.v10);
      V11 = (new WSATHelper() {
         public String getRegistrationCoordinatorAddress() {
            return this.getHostAndPort() + "/wls-wsat/RegistrationPortTypeRPC11";
         }

         public String getCoordinatorAddress() {
            return this.getHostAndPort() + "/wls-wsat/CoordinatorPortType11";
         }

         public String getParticipantAddress() {
            return this.getHostAndPort() + "/wls-wsat/ParticipantPortType11";
         }

         public String getRegistrationRequesterAddress() {
            return this.getHostAndPort() + "/wls-wsat/RegistrationRequesterPortType11";
         }
      }).WSATVersion(WSATVersion.v11);
      _kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      _runtimeAccess = ManagementService.getRuntimeAccess(_kernelId);
      debugWSAT = DebugLogger.getDebugLogger("DebugWSAT");
   }
}
