package weblogic.iiop;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.Identity;
import weblogic.rmi.spi.EndPointFinder;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.utils.Utilities;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.SyncKeyTable;

public final class EndPointManager implements EndPointFinder {
   private static final HashMap outboundEndPointTable = new HashMap();
   private static final IdentityHashMap inboundEndPointTable = new IdentityHashMap();
   private static final HashMap bidirEndPointTable = new HashMap();
   private static final ConcurrentHashMap routingTable = new ConcurrentHashMap();
   private static final boolean DEBUG = false;
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   private static final boolean DEBUG_TUNNELING = false;
   private static final WeakHashMap outboundConnectionLockTable = new WeakHashMap();

   static void dumpTables() {
      p("\nDump tables: \n Outbound EndPointTable: " + outboundEndPointTable.toString() + "\n Inbound EndPointTable: " + inboundEndPointTable.toString() + "\n Bidir EndPointTable: " + bidirEndPointTable.toString() + "\n RoutingTable: " + routingTable.toString() + "\nDumpTables complete.");
   }

   static void p(String var0) {
      System.err.println("<EndPointManager>: " + var0);
   }

   public static EndPoint findEndPoint(Connection var0) {
      return (EndPoint)inboundEndPointTable.get(var0);
   }

   public static EndPoint findEndPoint(ConnectionKey var0) {
      return (EndPoint)bidirEndPointTable.get(var0);
   }

   public static EndPoint findOrCreateEndPoint(Connection var0) throws IOException {
      EndPoint var1 = (EndPoint)inboundEndPointTable.get(var0.getConnectionKey());
      if (var1 == null) {
         var1 = createEndPoint(var0);
      }

      return var1;
   }

   private static synchronized EndPoint createEndPoint(Connection var0) throws IOException {
      EndPoint var1 = (EndPoint)inboundEndPointTable.get(var0);
      if (var1 == null) {
         var1 = var0.getEndPoint();
         var0.setFlag(8);
         if (var0.isStateful()) {
            inboundEndPointTable.put(var0, var1);
            Debug.assertion(bidirEndPointTable.get(var0.getConnectionKey()) == null);
            bidirEndPointTable.put(var0.getConnectionKey(), var1);
         }
      }

      return var1;
   }

   public static void updateConnection(Connection var0, ConnectionKey var1) {
      ConnectionKey var2 = var0.getConnectionKey();
      if (!var2.equals(var1) && inboundEndPointTable.get(var0) != null) {
         if (bidirEndPointTable.get(var1) == null) {
            Class var3 = EndPointManager.class;
            synchronized(EndPointManager.class) {
               if (bidirEndPointTable.get(var1) == null) {
                  EndPoint var4 = (EndPoint)bidirEndPointTable.get(var2);
                  if (var4 != null) {
                     bidirEndPointTable.remove(var2);
                     bidirEndPointTable.put(var1, var4);
                  }
               }
            }

            var0.setConnectionKey(var1);
         }

      }
   }

   public static synchronized void updateRoutingTable(Identity var0, ConnectionKey var1) {
      ConnectionKey var2 = (ConnectionKey)routingTable.get(var0);
      if (var2 == null) {
         routingTable.put(var0, var1);
      }

   }

   public static synchronized void purgeRoutingTable(Identity var0) {
      routingTable.remove(var0);
   }

   public static synchronized EndPoint getRoute(Identity var0) {
      if (var0 == null) {
         return null;
      } else {
         Object var1 = routingTable.get(var0);
         EndPoint var2 = null;
         if (var1 != null) {
            var2 = (EndPoint)bidirEndPointTable.get(var1);
         }

         return var2;
      }
   }

   public static synchronized EndPoint getForwardingDestination(ConnectionKey var0) {
      return (EndPoint)bidirEndPointTable.get(var0);
   }

   public static EndPoint findOrCreateEndPoint(IOR var0, String var1, boolean var2) throws IOException {
      EndPoint var3 = (EndPoint)outboundEndPointTable.get(var0.getConnectionKey());
      if (var3 != null && !var2 && !var3.getConnection().isDead()) {
         if (!var3.getFlag(16) && var0.isRemote()) {
            negotiateConnection(var3, var0);
         }
      } else {
         var3 = createEndPoint(var0, var1, var2);
      }

      return var3;
   }

   public static EndPoint findOrCreateEndPoint(IOR var0) throws IOException {
      return findOrCreateEndPoint(var0, (String)null, false);
   }

   public static EndPoint findOrCreateEndPoint(IOR var0, String var1) throws IOException {
      return findOrCreateEndPoint(var0, var1, false);
   }

   public static EndPoint findEndPoint(IOR var0) {
      return (EndPoint)outboundEndPointTable.get(var0.getConnectionKey());
   }

   private static EndPoint createEndPoint(IOR var0, String var1, boolean var2) throws IOException {
      EndPoint var3 = null;
      IOPProfile var4 = var0.getProfile();
      Class var5 = EndPointManager.class;
      synchronized(EndPointManager.class) {
         var3 = (EndPoint)outboundEndPointTable.get(var0.getConnectionKey());
         if (var3 == null || !var2 && !var3.getConnection().isDead()) {
            if (var3 == null) {
               var3 = (EndPoint)bidirEndPointTable.get(var0.getConnectionKey());
               if (var3 != null) {
                  SyncKeyTable var6 = (SyncKeyTable)var3.getConnection().getProperty("weblogic.iiop.BiDirKeys");
                  if (var6 != null && var6.get(var0.getConnectionKey().hashCode()) != null) {
                     outboundEndPointTable.put(var0.getConnectionKey(), var3);
                  } else {
                     var3 = null;
                  }
               }
            }
         } else {
            ConnectionManager.getConnectionManager().forceConnectionShutdown(var3.getConnection(), new EOFException("Forceful shutdown"));
            var3 = null;
         }

         if (var3 != null) {
            negotiateConnection(var3, var0);
            return var3;
         }
      }

      Object var22 = getOutboundConnectionLock(var0.getConnectionKey());
      synchronized(var22) {
         Class var7 = EndPointManager.class;
         synchronized(EndPointManager.class) {
            var3 = (EndPoint)outboundEndPointTable.get(var0.getConnectionKey());
            if (var3 != null && (var2 || var3.getConnection().isDead())) {
               ConnectionManager.getConnectionManager().forceConnectionShutdown(var3.getConnection(), new EOFException("Forceful shutdown"));
               var3 = null;
            }

            ConnectionKey var8 = var0.getConnectionKey();
            if (var8.isBidirSet()) {
               throw new IOException("client requested BiDir, but EndPoint is closed for address: " + var8.getAddress() + " port: " + var8.getPort());
            }
         }

         if (var3 == null) {
            ObjectKey var23 = var4.getObjectKey();
            Connection var24 = null;
            Class var9;
            if (KernelStatus.isServer() && IIOPService.isTGIOPEnabled() && (var23.isWLEKey() || var23.isWLSKey() && !var23.getWLEDomainId().equals(ObjectKey.getLocalDomainID()))) {
               try {
                  var9 = Utilities.classForName("weblogic.tgiop.TGIOPConnection", var0.getClass());
                  Constructor var26 = var9.getConstructor(String.class, String.class, Integer.TYPE);
                  var24 = (Connection)var26.newInstance(var23.getWLEDomainId().toString(), var4.getHost(), new Integer(var4.getPort()));
               } catch (ClassNotFoundException var14) {
                  throw new ExportException("TGIOPConnection constructor not found" + var14);
               } catch (NoSuchMethodException var15) {
                  throw new ExportException("Missing constructor TGIOPConnection: " + var15);
               } catch (IllegalAccessException var16) {
                  throw new ExportException("TGIOPConnection constructor not public: " + var16);
               } catch (InstantiationException var17) {
                  throw new ExportException("Failed to instantiate TGIOPConnection: " + var17);
               } catch (InvocationTargetException var18) {
                  Object var10 = var18.getTargetException();
                  if (!(var10 instanceof IOException)) {
                     if (!(var10 instanceof Exception)) {
                        var10 = var18;
                     }

                     throw new ExportException("Failed to invoke constructor for TGIOPConnection: " + (Exception)var10);
                  }
               }
            }

            if (var24 == null) {
               if (var0.isSecure()) {
                  String var25 = var4.getSecureHost();
                  if (var25 == null) {
                     var25 = var4.getHost();
                  }

                  var24 = MuxableSocketIIOPS.createConnection(InetAddress.getByName(var25), var4.getSecurePort(), var1).getConnection();
               } else {
                  var24 = MuxableSocketIIOP.createConnection(var4.getHostAddress(), var4.getPort(), var1).getConnection();
               }
            }

            var3 = var24.getEndPoint();
            if (var24.isStateful()) {
               var9 = EndPointManager.class;
               synchronized(EndPointManager.class) {
                  outboundEndPointTable.put(var24.getConnectionKey(), var3);
                  inboundEndPointTable.put(var24, var3);
               }
            }
         }
      }

      negotiateConnection(var3, var0);
      return var3;
   }

   private static synchronized void negotiateConnection(EndPoint var0, IOR var1) throws IOException {
      if (!var0.getFlag(16)) {
         IOPProfile var2 = var1.getProfile();
         Connection var3 = var0.getConnection();
         var3.setMinorVersion(var2.getMinorVersion());
         if (var1.isRemote()) {
            var3.setFlag(16);
            CodeSetsComponent var4 = (CodeSetsComponent)var2.getComponent(1);
            if (var4 != null) {
               if (!var3.getFlag(32) || !var3.getFlag(64) || !var4.supportedCharCodeSet(var3.getCharCodeSet()) || !var4.supportedWcharCodeSet(var3.getWcharCodeSet())) {
                  var3.setCodeSets(var4.negotiatedCharCodeSet(), var4.negotiatedWcharCodeSet());
               }

               if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
                  IIOPLogger.logDebugTransport("negotiated char codeset = " + Integer.toHexString(var3.getCharCodeSet()) + ", wchar codeset = " + Integer.toHexString(var3.getWcharCodeSet()));
               }
            }
         }
      }

   }

   public static synchronized EndPoint removeConnection(Connection var0) {
      ConnectionKey var1 = var0.getConnectionKey();
      Object var2 = inboundEndPointTable.remove(var0);
      if (var2 != null) {
         Object var3 = outboundEndPointTable.get(var1);
         if (var3 == var2) {
            outboundEndPointTable.remove(var1);
         }

         var3 = bidirEndPointTable.get(var1);
         if (var3 == var2) {
            bidirEndPointTable.remove(var1);
         }
      }

      if (routingTable.containsValue(var1)) {
         Iterator var5 = routingTable.values().iterator();

         while(var5.hasNext()) {
            ConnectionKey var4 = (ConnectionKey)var5.next();
            if (var1.equals(var4)) {
               var5.remove();
            }
         }
      }

      return (EndPoint)var2;
   }

   private static synchronized Object getOutboundConnectionLock(ConnectionKey var0) {
      Object var1 = outboundConnectionLockTable.get(var0);
      if (var1 == null) {
         var1 = new Object();
      }

      outboundConnectionLockTable.put(var0, var1);
      return var1;
   }

   public boolean claimHostID(HostID var1) {
      return var1 instanceof HostIDImpl;
   }

   public boolean claimServerURL(String var1) {
      return false;
   }

   public weblogic.rmi.spi.EndPoint findOrCreateEndPoint(HostID var1) {
      return var1 instanceof HostIDImpl ? (EndPoint)outboundEndPointTable.get(((HostIDImpl)var1).getConnectionKey()) : null;
   }

   public weblogic.rmi.spi.EndPoint findEndPoint(HostID var1) {
      return var1 instanceof HostIDImpl ? (EndPoint)outboundEndPointTable.get(((HostIDImpl)var1).getConnectionKey()) : null;
   }

   public weblogic.rmi.spi.EndPoint findOrCreateEndPoint(String var1) {
      return null;
   }
}
