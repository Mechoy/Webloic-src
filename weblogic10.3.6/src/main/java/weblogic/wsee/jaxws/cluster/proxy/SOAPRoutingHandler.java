package weblogic.wsee.jaxws.cluster.proxy;

import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamReader;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.proxy.HttpClusterServlet;
import weblogic.servlet.proxy.RoutingHandler;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedInputStream;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;
import weblogic.wsee.jaxws.security.SCTIDRoutingInfoFinder;
import weblogic.wsee.mc.cluster.McAnonIDRoutingInfoFinder;
import weblogic.wsee.monitoring.WseeClusterFrontEndRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WseeClusterRoutingRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WseeRuntimeMBeanDelegate;
import weblogic.wsee.reliability2.tube.SequenceIDRoutingInfoFinder;

public class SOAPRoutingHandler implements RoutingHandler, HttpClusterServlet.ServerListListener {
   private static RuntimeAccess _runtimeAccess;
   private static final Logger LOGGER;
   public static final String EOL = "\r\n";
   public static final String X_WEBLOGIC_WSEE_STORETOSERVER_LIST_ACCEPTED = "X-weblogic-wsee-storetoserver-accepted";
   public static final String X_WEBLOGIC_WSEE_REQUEST_STORETOSERVER_LIST = "X-weblogic-wsee-request-storetoserver-list";
   public static final String X_WEBLOGIC_WSEE_IGNORE_THIS_RESPONSE = "X-weblogic-wsee-ignore-this-response";
   public static final String X_WEBLOGIC_WSEE_STORETOSERVER_HASH = "X-weblogic-wsee-storetoserver-hash";
   public static final String X_WEBLOGIC_WSEE_STORETOSERVER_LIST = "X-weblogic-wsee-storetoserver-list";
   private HttpClusterServlet _servlet;
   private WseeClusterFrontEndRuntimeMBeanImpl _clusterFrontEndMBean;
   private WseeClusterRoutingRuntimeMBeanImpl _clusterRoutingMBean;
   private FrontEndSOAPRouter _router;
   private ReentrantReadWriteLock _mapLock = new ReentrantReadWriteLock();
   private Map<String, HttpClusterServlet.Server> _storeNameToServerMap;
   private Map<String, HttpClusterServlet.Server> _serverNameToServerMap;
   private Map<HttpClusterServlet.Server, String> _serverToServerNameMap;
   private ReentrantReadWriteLock _hashLock = new ReentrantReadWriteLock();
   private String _currentStoreToServerMapHash;
   private String _pendingStoreToServerMapHash;
   private String _pendingStoreToServerMapString;
   private boolean _dummyServerCreated;

   public SOAPRoutingHandler() throws ManagementException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("SOAP routing handler created");
      }

      this._currentStoreToServerMapHash = null;
      this._storeNameToServerMap = new HashMap();
      this._serverNameToServerMap = new HashMap();
      this._serverToServerNameMap = new HashMap();
      this._router = new FrontEndSOAPRouter(this);
   }

   public void init(HttpClusterServlet var1) {
      this._servlet = var1;
      this._servlet.addServerListChangeListener(this);
      String var2 = this._servlet.getServletContext().getContextPath() + "/" + this._servlet.getServletName();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("SOAP routing handler init called for: " + var2);
      }

      try {
         this._clusterFrontEndMBean = new WseeClusterFrontEndRuntimeMBeanImpl(var2, _runtimeAccess.getServerRuntime());
         this._clusterRoutingMBean = (WseeClusterRoutingRuntimeMBeanImpl)this._clusterFrontEndMBean.getClusterRouting();
         _runtimeAccess.getServerRuntime().setWseeClusterFrontEndRuntime(this._clusterFrontEndMBean);
         this._router.init(this._clusterRoutingMBean);
      } catch (Exception var4) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, var4.toString(), var4);
         }

         WseeCoreLogger.logUnexpectedException(var4.toString(), var4);
      }

   }

   public void destroy() {
      try {
         this._clusterRoutingMBean.unregister();
      } catch (Exception var3) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, var3.toString(), var3);
         }

         WseeCoreLogger.logUnexpectedException(var3.toString(), var3);
      }

      try {
         this._clusterFrontEndMBean.unregister();
      } catch (Exception var2) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, var2.toString(), var2);
         }

         WseeCoreLogger.logUnexpectedException(var2.toString(), var2);
      }

      this._servlet.removeServerListChangeListener(this);
   }

   public HttpClusterServlet.RequestInfo route(HttpClusterServlet.RequestInfo var1, HttpServletRequest var2) throws Exception {
      String var3 = var2.getContentType();
      if (var3 == null) {
         return null;
      } else if (!var3.startsWith("text/xml") && !var3.startsWith("application/xml")) {
         return null;
      } else {
         String var4 = var2.getHeader("SOAPAction");
         if (var4 == null) {
            return null;
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("SOAPRoutingHandler handling incoming SOAP message with action: " + var4);
            }

            InputStream var5 = var1.getInputStream();
            if (var5 == null) {
               String var29 = "SOAPRoutingHandler supports only HTTP POST with chunked data streaming.";
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine(var29);
               }

               this._clusterRoutingMBean.setLastRoutingFailure(var29);
               this._clusterRoutingMBean.setLastRoutingFailureTime(System.currentTimeMillis());
               this._clusterRoutingMBean.incrementRoutingFailureCount();
               return null;
            } else {
               int var6 = 1000000;
               XMLStreamReader var7 = null;
               String var8 = null;
               long var9 = 0L;

               HttpClusterServlet.RequestInfo var15;
               try {
                  var5.mark(var6);
                  var7 = XMLStreamReaderFactory.create((String)null, var5, false);
                  Message var12 = Messages.create(var7);
                  Packet var11 = new Packet();
                  var11.setMessage(var12);
                  FrontEndRoutables var13 = new FrontEndRoutables(var1, var11);
                  FrontEndRoutables var14 = (FrontEndRoutables)this._router.route(var13);
                  if (var14 == null) {
                     var15 = null;
                     return var15;
                  }

                  if (var14.abstainers != null) {
                     Map var30 = var1.getNotificationData();
                     var30.put(this, var14.abstainers);
                     Object var16 = null;
                     return (HttpClusterServlet.RequestInfo)var16;
                  }

                  var15 = var14.ri;
               } catch (Exception var27) {
                  var8 = var27.toString();
                  var9 = System.currentTimeMillis();
                  throw var27;
               } finally {
                  if (var8 != null) {
                     this._clusterRoutingMBean.setLastRoutingFailure(var8);
                     this._clusterRoutingMBean.incrementRoutingFailureCount();
                     this._clusterRoutingMBean.setLastRoutingFailureTime(var9);
                  }

                  if (var7 != null) {
                     XMLStreamReaderFactory.recycle(var7);
                  }

                  try {
                     var5.reset();
                  } catch (Exception var26) {
                     if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, var26.toString(), var26);
                     }

                     WseeCoreLogger.logUnexpectedException(var26.toString(), var26);
                  }

               }

               return var15;
            }
         }
      }
   }

   public void addRequestHeaders(HttpServletRequest var1, PrintStream var2, HttpClusterServlet.RequestInfo var3, HttpClusterServlet.Server var4) {
      if (this.needNewStoreToServerList()) {
         var2.print("X-weblogic-wsee-request-storetoserver-list: " + this.getHash());
         var2.print("\r\n");
      }

      var2.print("X-weblogic-wsee-storetoserver-accepted: true");
      var2.print("\r\n");
   }

   public boolean handleResponseHeader(HttpServletResponse var1, String var2, String var3, HttpClusterServlet.RequestInfo var4) {
      if (var2.equals("X-weblogic-wsee-storetoserver-list")) {
         this.setPendingStoreToServerMap(var3);
         return true;
      } else if (var2.equals("X-weblogic-wsee-storetoserver-hash")) {
         this.setPendingStoreToServerMapHash(var3);
         return true;
      } else if (!var2.equals("X-weblogic-wsee-ignore-this-response")) {
         return false;
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Ignoring router-induced response from primary server: " + var4.getPrimaryServer());
         }

         var1.setStatus(202);

         try {
            var1.getOutputStream().close();
         } catch (Exception var6) {
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.warning("While trying to ignore router-induced response: " + var6.toString());
            }

            WseeCoreLogger.logUnexpectedException(var6.toString(), var6);
         }

         return true;
      }
   }

   private boolean needNewStoreToServerList() {
      boolean var1;
      try {
         this._hashLock.readLock().lock();
         var1 = this._currentStoreToServerMapHash == null || this._dummyServerCreated;
      } finally {
         this._hashLock.readLock().unlock();
      }

      return var1;
   }

   private String getHash() {
      String var1;
      try {
         this._hashLock.readLock().lock();
         var1 = this._currentStoreToServerMapHash;
      } finally {
         this._hashLock.readLock().unlock();
      }

      return var1;
   }

   private void setPendingStoreToServerMap(String var1) {
      try {
         this._hashLock.writeLock().lock();
         this._pendingStoreToServerMapString = var1;
         if (this._pendingStoreToServerMapHash != null) {
            this.parseMapIfNeeded();
         }
      } finally {
         this._hashLock.writeLock().unlock();
      }

   }

   private void setPendingStoreToServerMapHash(String var1) {
      try {
         this._hashLock.writeLock().lock();
         this._pendingStoreToServerMapHash = var1;
         if (this._pendingStoreToServerMapString != null) {
            this.parseMapIfNeeded();
         }
      } finally {
         this._hashLock.writeLock().unlock();
      }

   }

   void requestUpdatedStoreToServerMap() {
      try {
         this._hashLock.writeLock().lock();
         this._pendingStoreToServerMapHash = null;
      } finally {
         this._hashLock.writeLock().unlock();
      }

   }

   private void parseMapIfNeeded() {
      ParsedMaps var1 = null;
      if (this._currentStoreToServerMapHash == null || this._pendingStoreToServerMapHash != null && !this._currentStoreToServerMapHash.equals(this._pendingStoreToServerMapHash)) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Replacing store/server map with new contents: " + this._pendingStoreToServerMapString);
         }

         var1 = this.parseMap(this._pendingStoreToServerMapString);
         this._currentStoreToServerMapHash = this._pendingStoreToServerMapHash;
         this._pendingStoreToServerMapHash = null;
         this._pendingStoreToServerMapString = null;
      }

      if (var1 != null) {
         try {
            this._mapLock.writeLock().lock();
            this._dummyServerCreated = var1.dummyServerCreated;
            this._storeNameToServerMap = var1.storeNameToServerMap;
            this._serverNameToServerMap = var1.serverNameToServerMap;
            this._serverToServerNameMap = var1.serverToServerNameMap;
         } finally {
            this._mapLock.writeLock().unlock();
         }
      }

   }

   private ParsedMaps parseMap(String var1) {
      ParsedMaps var2 = new ParsedMaps();
      StringTokenizer var3 = new StringTokenizer(var1, "|");

      while(var3.hasMoreTokens()) {
         String var4 = var3.nextToken().trim();
         this.parseStoreServerTupple(var4, var2);
      }

      return var2;
   }

   private void parseStoreServerTupple(String var1, ParsedMaps var2) {
      int var3 = var1.indexOf("/");
      if (var3 > 0) {
         String var4 = var1.substring(0, var3);
         String var5 = var1.substring(var3 + 1);
         StringTokenizer var6 = new StringTokenizer(var5, ":");
         int var7 = 0;
         String var8 = "";
         String var9 = "";
         int var10 = -1;

         int var11;
         for(var11 = -1; var6.hasMoreTokens(); ++var7) {
            String var12 = var6.nextToken().trim();
            switch (var7) {
               case 0:
                  var8 = var12;
                  break;
               case 1:
                  var9 = var12;
                  break;
               case 2:
                  var10 = Integer.parseInt(var12);
                  break;
               case 3:
                  var11 = Integer.parseInt(var12);
            }
         }

         HttpClusterServlet.Server var13 = this._servlet.getServerList().getByHostAndPort(var9, var10);
         if (var13 == null) {
            LOGGER.warning("WARNING: Didn't find servlet Server definition matching " + var8 + ":" + var9 + ":" + var10 + ". We'll create a dummy Server item and then wait to see the servlet's Server list get refreshed.");
            var13 = this._servlet.createServer((String)null, var9, var10, var11);
            var2.dummyServerCreated = true;
         }

         var2.storeNameToServerMap.put(var4, var13);
         var2.serverNameToServerMap.put(var8, var13);
         var2.serverToServerNameMap.put(var13, var8);
      }

   }

   public void serverListChanged() {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Refreshing server info from HttpClusterServlet server list...");
         }

         this._mapLock.writeLock().lock();
         this._dummyServerCreated = false;
         Iterator var1 = this._storeNameToServerMap.keySet().iterator();

         String var2;
         HttpClusterServlet.Server var3;
         HttpClusterServlet.Server var4;
         while(var1.hasNext()) {
            var2 = (String)var1.next();
            var3 = (HttpClusterServlet.Server)this._storeNameToServerMap.get(var2);
            var4 = this._servlet.getServerList().getByHostAndPort(var3.getHost(), var3.getPort());
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("For storeName '" + var2 + "' oldServer: " + var3 + " newServer: " + var4);
            }

            if (var4 != null) {
               this._storeNameToServerMap.put(var2, var4);
            }
         }

         var1 = this._serverNameToServerMap.keySet().iterator();

         while(var1.hasNext()) {
            var2 = (String)var1.next();
            var3 = (HttpClusterServlet.Server)this._serverNameToServerMap.get(var2);
            var4 = this._servlet.getServerList().getByHostAndPort(var3.getHost(), var3.getPort());
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("For serverName '" + var2 + "' oldServer: " + var3 + " newServer: " + var4);
            }

            if (var4 != null) {
               this._serverNameToServerMap.put(var2, var4);
               this._serverToServerNameMap.put(var4, var2);
            }
         }
      } finally {
         this._mapLock.writeLock().unlock();
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("DONE refreshing server info from HttpClusterServlet server list...");
      }

   }

   public void notifyRoutingDecision(HttpClusterServlet.Server var1, Object var2) {
      this._router.notifyRoutingDecision(var1, (Map)var2);
   }

   HttpClusterServlet.Server getTargetServerForRouting(RoutingInfo var1) {
      try {
         this._mapLock.readLock().lock();
         String var4;
         HttpClusterServlet.Server var11;
         switch (var1.getType()) {
            case SERVER_NAME:
               var11 = (HttpClusterServlet.Server)this._serverNameToServerMap.get(var1.getName());
               return var11;
            case PHYSICAL_STORE_NAME:
               var11 = (HttpClusterServlet.Server)this._storeNameToServerMap.get(var1.getName());
               return var11;
            case HOST_AND_PORT:
               String var2 = var1.getName();
               int var3 = var2.indexOf(":");
               if (var3 > 0) {
                  var4 = var2.substring(0, var3);
                  int var5 = Integer.parseInt(var2.substring(var3 + 1));
                  HttpClusterServlet.Server var6 = this._servlet.getServerList().getByHostAndPort(var4, var5);
                  return var6;
               }

               var4 = null;
               return var4;
            default:
               var4 = null;
               return var4;
         }
      } finally {
         this._mapLock.readLock().unlock();
      }
   }

   RoutingInfo getRoutingForTargetServer(HttpClusterServlet.Server var1) {
      RoutingInfo var3;
      try {
         this._mapLock.readLock().lock();
         String var2 = (String)this._serverToServerNameMap.get(var1);
         if (var2 == null) {
            StringBuilder var9 = new StringBuilder();
            var9.append(var1.getHostIp());
            var9.append(':');
            var9.append(Integer.toString(var1.getPort()));
            RoutingInfo var4 = new RoutingInfo(var9.toString(), RoutingInfo.Type.HOST_AND_PORT);
            return var4;
         }

         var3 = new RoutingInfo(var2, RoutingInfo.Type.SERVER_NAME);
      } finally {
         this._mapLock.readLock().unlock();
      }

      return var3;
   }

   static {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      _runtimeAccess = ManagementService.getRuntimeAccess(var0);
      LOGGER = Logger.getLogger(SOAPRoutingHandler.class.getName());
      if (WseeRuntimeMBeanDelegate.isReliableSecureProfileEnabled()) {
         SequenceIDRoutingInfoFinder.registerIfNeeded();
         McAnonIDRoutingInfoFinder.registerIfNeeded();
         SCTIDRoutingInfoFinder.registerIfNeeded();
      }

   }

   private static class ParsedMaps {
      boolean dummyServerCreated;
      Map<String, HttpClusterServlet.Server> storeNameToServerMap = new HashMap();
      Map<String, HttpClusterServlet.Server> serverNameToServerMap = new HashMap();
      Map<HttpClusterServlet.Server, String> serverToServerNameMap = new HashMap();

      public ParsedMaps() {
      }
   }

   private static class SOAPHeaderChunkedInputStream extends ChunkedInputStream {
      int _maxHeaderBlockSize;

      public SOAPHeaderChunkedInputStream(Chunk var1, int var2) {
         super(var1, 0);
         this._maxHeaderBlockSize = var2;
      }

      public int read() throws IOException {
         int var1 = super.read();
         if (this.pos() > this._maxHeaderBlockSize) {
            throw new IOException("Max header block size exceeded: " + this._maxHeaderBlockSize);
         } else {
            return var1;
         }
      }
   }
}
