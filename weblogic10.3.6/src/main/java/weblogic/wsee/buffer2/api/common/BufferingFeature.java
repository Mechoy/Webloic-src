package weblogic.wsee.buffer2.api.common;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceFeature;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.buffer2.spi.BufferingProviderManager;
import weblogic.wsee.buffer2.utils.BufferingConstants;
import weblogic.wsee.jaxws.buffer.BufferingConfig;
import weblogic.wsee.reliability2.saf.BufferUtil;

public class BufferingFeature extends WebServiceFeature {
   private static final Logger LOGGER = Logger.getLogger(BufferingFeature.class.getName());
   private static final String ID = "Buffering Feature";
   private static final ConcurrentMap<String, BufferingFeature> _registry = new ConcurrentHashMap();
   private static final Map<String, BufferingFeature> _targetURIMap = new ConcurrentHashMap();
   private static final Map<String, Object> _queueToDynamicMDBMap = new ConcurrentHashMap();
   private static volatile BufferingManager _bufferingManager;
   private String _bufferingFeatureId;
   private final Set<String> _targetURIs;
   private volatile Map<String, ConcurrentHashMap<String, Long>> retryDelays;
   private final BufferingConfig.Queue _requestQueue;
   private final BufferingConfig.Queue _responseQueue;
   private final int _retryCount;
   private final String _retryDelay;
   private BufferingConstants.MsgDirection _direction;
   private volatile BufferingDispatch _bufferingDispatch;
   private final BufferingFeatureUsers _bufferingFeatureUser;

   public static BufferingFeature newBufferingFeature(BufferingFeatureUsers var0, ServerTubeAssemblerContext var1) throws BufferingException {
      BufferingFeature var2 = new BufferingFeature(var0, var1);
      var2.setBufferingFeatureId(registerFeature(var2));
      return var2;
   }

   public static BufferingFeature newBufferingFeature(BufferingFeatureUsers var0, ClientTubeAssemblerContext var1) throws BufferingException {
      BufferingFeature var2 = new BufferingFeature(var0, var1);
      var2.setBufferingFeatureId(registerFeature(var2));
      return var2;
   }

   private BufferingFeature(BufferingFeatureUsers var1, ServerTubeAssemblerContext var2) throws BufferingException {
      this(var1, BufferingConfig.getServiceConfig(var2));
   }

   private BufferingFeature(BufferingFeatureUsers var1, ClientTubeAssemblerContext var2) throws BufferingException {
      this(var1, BufferingConfig.getServiceConfig(var2));
   }

   private BufferingFeature(BufferingFeatureUsers var1, BufferingConfig.Service var2) throws BufferingException {
      try {
         super.enabled = true;
         this._retryCount = var2.getRetryCount();
         this._retryDelay = var2.getRetryDelay();
         this._requestQueue = var2.getRequestQueue();
         this._responseQueue = var2.getResponseQueue();
         this._direction = BufferingConstants.MsgDirection.REQUEST;
         this._bufferingFeatureUser = var1;
         _bufferingManager = BufferingProviderManager.getBufferingProvider().getBufferingManager();
         this._targetURIs = new ConcurrentSkipListSet();
      } catch (Exception var4) {
         throw new BufferingException(var4);
      }
   }

   public String getID() {
      return "Buffering Feature";
   }

   public int getDeployedRetryCount() {
      return this._retryCount;
   }

   public String getDeployedRetryDelay() {
      return this._retryDelay;
   }

   public BufferingConstants.MsgDirection getDirection() {
      return this._direction;
   }

   public void setDirection(BufferingConstants.MsgDirection var1) {
      this._direction = var1;
   }

   public String getBufferingFeatureId() {
      return this._bufferingFeatureId;
   }

   void setBufferingFeatureId(String var1) {
      this._bufferingFeatureId = var1;
   }

   public BufferingDispatch getBufferDispatch() {
      return this._bufferingDispatch;
   }

   public void setBufferDispatch(BufferingDispatch var1) {
      this._bufferingDispatch = var1;
   }

   public BufferingFeatureUsers getBufferingFeatureUser() {
      return this._bufferingFeatureUser;
   }

   private void addTargetURI(String var1) {
      this.getTargetURIs().add(var1);
   }

   public Set<String> getTargetURIs() {
      return this._targetURIs;
   }

   public void putRetryDelay(String var1, String var2, long var3) {
      ConcurrentHashMap var5 = (ConcurrentHashMap)this.getRetryDelays().get(var1);
      if (var5 == null) {
         var5 = new ConcurrentHashMap();
         this.retryDelays.put(var1, var5);
      }

      var5.put(var2, var3);
   }

   public long getRetryDelay(String var1, String var2) {
      ConcurrentHashMap var3 = (ConcurrentHashMap)this.getRetryDelays().get(var1);
      if (var3 == null) {
         return -1L;
      } else {
         Long var4 = (Long)var3.get(var2);
         return var4 == null ? -1L : var4;
      }
   }

   public void removeRetryDelay(String var1) {
      this.getRetryDelays().remove(var1);
   }

   public Map<String, ConcurrentHashMap<String, Long>> getRetryDelays() {
      Object var1 = this.retryDelays;
      if (var1 == null) {
         synchronized(this) {
            var1 = this.retryDelays;
            if (var1 == null) {
               this.retryDelays = (Map)(var1 = new ConcurrentHashMap());
            }
         }
      }

      return (Map)var1;
   }

   public String getQueueJndiName() {
      switch (this.getDirection()) {
         case REQUEST:
         default:
            return this._requestQueue.getJndiName();
         case RESPONSE:
            return this._responseQueue.getJndiName() != null ? this._responseQueue.getJndiName() : this._requestQueue.getJndiName();
      }
   }

   public static boolean isBufferingFeatureRegistered(String var0) {
      Class var1 = BufferingFeature.class;
      synchronized(BufferingFeature.class) {
         return _targetURIMap.containsKey(var0);
      }
   }

   public static BufferingFeature getBufferingFeature(String var0) throws BufferingException {
      BufferingFeature var1 = null;
      Class var2 = BufferingFeature.class;
      synchronized(BufferingFeature.class) {
         var1 = (BufferingFeature)_targetURIMap.get(var0);
      }

      if (var1 == null) {
         throw new BufferingException("No BufferingFeature registered for targetUri='" + var0 + "'");
      } else {
         return var1;
      }
   }

   public static BufferingManager getBufferingManager() {
      return _bufferingManager;
   }

   public static void addTargetURI(String var0, BufferingFeature var1) throws BufferingException {
      Class var2 = BufferingFeature.class;
      synchronized(BufferingFeature.class) {
         BufferingFeature var3 = (BufferingFeature)_targetURIMap.get(var0);
         if (var3 != null && !var3.getBufferingFeatureId().equals(var1.getBufferingFeatureId())) {
            throw new BufferingException("Attempt to register targetURI='" + var0 + "' current associated with BufferingFeature='" + var3.getBufferingFeatureId() + "' with a DIFFERENT BufferingFeature='" + var1.getBufferingFeatureId() + "'.  This is not allowed because a given targetURI MUST be routed to a unique BufferingFeature instance.");
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("BufferingFeature addTargetURI for targetURI='" + (var0 == null ? "NULL" : var0) + "'" + " and BufferingFeature with bufferingFeatureId='" + (var1 == null ? "NULL" : var1.getBufferingFeatureId()));
            }

            _targetURIMap.put(var0, var1);
            var1.addTargetURI(var0);
         }
      }
   }

   public static BufferingDispatch getBufferDispatch(String var0) throws BufferingException {
      BufferingFeature var1 = getBufferingFeature(var0);
      return var1.getBufferDispatch();
   }

   public static BufferingFeatureUsers getBufferingFeatureUser(String var0) throws BufferingException {
      BufferingFeature var1 = getBufferingFeature(var0);
      return var1.getBufferingFeatureUser();
   }

   public static String getQueueJndiName(String var0) throws BufferingException {
      BufferingFeature var1 = getBufferingFeature(var0);
      return var1.getQueueJndiName();
   }

   public static void registerQueue(String var0, Object var1) {
      _queueToDynamicMDBMap.put(var0, var1);
   }

   public static boolean isQueueRegistered(String var0) {
      return _queueToDynamicMDBMap.containsKey(var0);
   }

   private static String registerFeature(BufferingFeature var0) throws BufferingException {
      String var1 = null;
      byte var2 = 3;
      boolean var3 = false;

      for(int var4 = 0; var4 < var2; ++var4) {
         var1 = UUID.randomUUID().toString();
         if (_registry.putIfAbsent(var1, var0) == null) {
            var3 = true;
            break;
         }
      }

      if (!var3) {
         throw new BufferingException("unable to create unique BufferingFeature ID after " + var2 + " tries.");
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("BufferingFeature registerFeature() registering BufferingFeature ID='" + var1 + "' ");
         }

         return var1;
      }
   }

   public static void unRegisterBufferingFeature(ServerTubeAssemblerContext var0) {
      unRegisterBufferingFeature(var0.getEndpoint());
   }

   public static void unRegisterBufferingFeature(WSEndpoint var0) {
      List var1 = BufferUtil.getTargetURIs(var0);
      if (var1.size() > 0) {
         String var2 = (String)var1.get(0);
         BufferingFeature var3 = (BufferingFeature)_targetURIMap.get(var2);
         if (var3 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("unRegisterBufferingFeature for targetURI='" + (var2 == null ? "NULL" : var2) + "'" + " found BufferingFeature with bufferingFeatureID='" + (var3 == null ? "NULL" : var3.getBufferingFeatureId()));
            }

            removeFeature(var3.getBufferingFeatureId());
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("unRegisterBufferingFeature for targetURI='" + (var2 == null ? "NULL" : var2) + "'" + " no BufferingFeature registered.  Skipping unRegister.");
         }

      }
   }

   private static BufferingFeature removeFeature(String var0) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("BufferingFeature removeFeature() unRegistering BufferingFeature ID='" + var0 + "' ");
      }

      BufferingFeature var1 = (BufferingFeature)_registry.get(var0);
      if (var1 != null) {
         Set var2 = var1.getTargetURIs();
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               _targetURIMap.remove(var4);
            }
         }
      }

      return (BufferingFeature)_registry.remove(var0);
   }

   public static void stop() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("BufferingFeature stop()");
      }

      if (_bufferingManager != null) {
         _bufferingManager.cleanupDynamicMDBs(_queueToDynamicMDBMap);
      }

   }

   public static enum BufferingFeatureUsers {
      WSRM;
   }
}
