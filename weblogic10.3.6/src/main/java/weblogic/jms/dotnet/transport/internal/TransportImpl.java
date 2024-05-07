package weblogic.jms.dotnet.transport.internal;

import java.util.concurrent.atomic.AtomicLong;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReadableFactory;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.Service;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;
import weblogic.jms.dotnet.transport.TransportPluginSPI;
import weblogic.jms.dotnet.transport.TransportThreadPool;
import weblogic.utils.collections.NumericKeyHashMap;

public class TransportImpl implements Transport {
   private static final long UNUSED = -1L;
   private AtomicLong nextServiceId = new AtomicLong(-10L);
   private final ThreadPoolWrapper defaultThreadPool;
   private final TransportPluginSPI plugin;
   private boolean closed;
   private TransportError closedReason;
   private HeartbeatLock hLock = new HeartbeatLock();
   private HeartbeatService heartbeatService;
   private MarshalLock mLock = new MarshalLock();
   private MarshalReadableFactory[] readableFactories = new MarshalReadableFactory[0];
   private ServiceLock sLock = new ServiceLock();
   private NumericKeyHashMap services = new NumericKeyHashMap();
   private static final byte FLAG_EXT = 1;
   private static final byte FLAG_ISTWOWAY = 2;
   private static final byte FLAG_ISONEWAY = 4;
   private static final byte FLAG_REMOTEORDERED = 8;
   private static final byte FLAG_RESPONSEORDERED = 16;

   public TransportImpl(TransportPluginSPI var1, TransportThreadPool var2) {
      this.plugin = var1;
      this.defaultThreadPool = new ThreadPoolWrapper(var2);
      this.addMarshalReadableFactory(new MarshalFactoryImpl());
      this.registerService(10003L, new BootstrapService());
   }

   public String toString() {
      long var1 = this.nextServiceId.get();
      int var3;
      synchronized(this.mLock) {
         var3 = this.readableFactories.length;
      }

      int var4;
      synchronized(this.sLock) {
         var4 = this.services.size();
      }

      return "Transport<plg=" + this.plugin + ", svcs= " + var4 + ", facts= " + var3 + ", nextId= " + var1 + ">";
   }

   ThreadPoolWrapper getDefaultThreadPool() {
      return this.defaultThreadPool;
   }

   void startHeartbeatService(BootstrapRequest var1) {
      HeartbeatService var2 = null;
      synchronized(this.hLock) {
         if (this.heartbeatService != null) {
            return;
         }

         int var4 = var1.getHeartbeatInterval();
         int var5 = var1.getAllowedMissedBeats();
         if (var4 <= 0 || var5 <= 0) {
            return;
         }

         this.heartbeatService = new HeartbeatService(var4, var5, this);
         var2 = this.heartbeatService;
      }

      this.registerService(10001L, var2);
      var2.startHeartbeat();
   }

   public long allocateServiceID() {
      return this.nextServiceId.getAndDecrement();
   }

   public SendHandlerOneWayImpl createOneWay(long var1) {
      return new SendHandlerOneWayImpl(this, var1, -1L);
   }

   public SendHandlerOneWayImpl createOneWay(long var1, long var3) {
      return new SendHandlerOneWayImpl(this, var1, var3);
   }

   public SendHandlerTwoWayImpl createTwoWay(long var1) {
      return this.createTwoWay(var1, -1L, -1L);
   }

   public SendHandlerTwoWayImpl createTwoWay(long var1, long var3) {
      return this.createTwoWay(var1, var3, -1L);
   }

   public SendHandlerTwoWayImpl createTwoWay(long var1, long var3, long var5) {
      return new SendHandlerTwoWayImpl(this, var1, var3, var5);
   }

   public MarshalReadable bootstrap(String var1) {
      SendHandlerTwoWayImpl var2 = this.createTwoWay(10003L);
      BootstrapRequest var3 = new BootstrapRequest(var1);
      var2.send(var3);
      return var2.getResponse(true);
   }

   public void addMarshalReadableFactory(MarshalReadableFactory var1) {
      if (var1 != null) {
         synchronized(this.mLock) {
            MarshalReadableFactory[] var3 = this.readableFactories;
            this.readableFactories = new MarshalReadableFactory[var3.length + 1];
            System.arraycopy(var3, 0, this.readableFactories, 0, var3.length);
            this.readableFactories[var3.length] = var1;
         }
      }
   }

   public MarshalReadable createMarshalReadable(int var1) {
      synchronized(this.mLock) {
         MarshalReadableFactory[] var3 = this.readableFactories;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MarshalReadableFactory var6 = var3[var5];
            MarshalReadable var7 = var6.createMarshalReadable(var1);
            if (var7 != null) {
               return var7;
            }
         }

         throw new RuntimeException("Unknown marshal type code " + var1);
      }
   }

   public long getScratchId() {
      return this.plugin.getScratchID();
   }

   public void registerService(long var1, Service var3) {
      this.registerService(var1, var3, this.defaultThreadPool);
   }

   void registerService(long var1, Service var3, ThreadPoolWrapper var4) {
      ServiceWrapper var5 = new ServiceWrapper(var1, var3, var4);
      synchronized(this.sLock) {
         if (this.closed) {
            var5.shutdown(this.closedReason);
         } else if (this.services.get(var1) != null) {
            throw new RuntimeException("Duplicate service-id " + var1);
         } else {
            this.services.put(var1, var5);
         }
      }
   }

   public void shutdown(TransportError var1) {
      Object[] var2;
      synchronized(this.sLock) {
         if (this.closed) {
            return;
         }

         this.closedReason = var1;
         this.closed = true;
         var2 = this.services.values().toArray();
         this.services.clear();
      }

      HeartbeatService var3;
      synchronized(this.hLock) {
         var3 = this.heartbeatService;
      }

      if (var3 != null) {
         var3.stopHeartbeat();
      }

      boolean var17 = false;

      Object[] var4;
      int var5;
      int var6;
      Object var7;
      label161: {
         try {
            var17 = true;
            this.plugin.terminateConnection();
            var17 = false;
            break label161;
         } catch (Exception var19) {
            var17 = false;
         } finally {
            if (var17) {
               Object[] var9 = var2;
               int var10 = var2.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  Object var12 = var9[var11];
                  ((ServiceWrapper)var12).shutdown(var1);
               }

            }
         }

         var4 = var2;
         var5 = var2.length;

         for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];
            ((ServiceWrapper)var7).shutdown(var1);
         }

         return;
      }

      var4 = var2;
      var5 = var2.length;

      for(var6 = 0; var6 < var5; ++var6) {
         var7 = var4[var6];
         ((ServiceWrapper)var7).shutdown(var1);
      }

   }

   public void unregisterService(long var1) {
      ServiceWrapper var3;
      synchronized(this.sLock) {
         var3 = (ServiceWrapper)this.services.remove(var1);
      }

      if (var3 != null) {
         var3.unregister();
      }

   }

   void unregisterService(long var1, TransportError var3) {
      ServiceWrapper var4;
      synchronized(this.sLock) {
         var4 = (ServiceWrapper)this.services.remove(var1);
      }

      if (var4 != null) {
         var4.shutdown(var3);
      }

   }

   void unregisterServiceSilent(long var1) {
      synchronized(this.sLock) {
         this.services.remove(var1);
      }
   }

   void sendInternalOneWay(SendHandlerOneWayImpl var1, MarshalWritable var2) {
      MarshalWriter var3 = this.plugin.createMarshalWriter();
      byte var4 = 4;
      if (var1.getRemoteOrderingID() != -1L) {
         var4 = (byte)(var4 | 8);
      }

      var3.writeByte(var4);
      var3.writeLong(var1.getRemoteServiceID());
      if (var1.getRemoteOrderingID() != -1L) {
         var3.writeLong(var1.getRemoteOrderingID());
      }

      var3.writeMarshalable(var2);
      this.plugin.send(var3);
   }

   void sendInternalTwoWay(SendHandlerTwoWayImpl var1, MarshalWritable var2) {
      MarshalWriter var3 = this.plugin.createMarshalWriter();
      byte var4 = 2;
      if (var1.getRemoteOrderingID() != -1L) {
         var4 = (byte)(var4 | 8);
      }

      if (var1.getResponseOrderingID() != -1L) {
         var4 = (byte)(var4 | 16);
      }

      var3.writeByte(var4);
      var3.writeLong(var1.getRemoteServiceID());
      if (var1.getRemoteOrderingID() != -1L) {
         var3.writeLong(var1.getRemoteOrderingID());
      }

      var3.writeLong(var1.getResponseID());
      if (var1.getResponseOrderingID() != -1L) {
         var3.writeLong(var1.getResponseOrderingID());
      }

      var3.writeMarshalable(var2);
      this.plugin.send(var3);
   }

   public void dispatch(MarshalReader var1) {
      int var2 = var1.read();

      for(int var3 = var2; (var3 & 1) != 0; var3 = var1.read()) {
      }

      long var17 = var1.readLong();
      ServiceWrapper var7;
      synchronized(this.sLock) {
         var7 = (ServiceWrapper)this.services.get(var17);
      }

      if (var7 == null) {
         var1.internalClose();
      } else {
         HeartbeatService var8;
         synchronized(this.hLock) {
            var8 = this.heartbeatService;
         }

         if (var8 != null) {
            var8.resetMissCounter();
         }

         long var5;
         if ((var2 & 8) != 0) {
            var5 = var1.readLong();
         } else {
            var5 = -1L;
         }

         if ((var2 & 4) != 0) {
            ReceivedOneWayImpl var18 = new ReceivedOneWayImpl(this, var17, var1);
            var7.invoke(var18, var5);
         } else if ((var2 & 2) != 0) {
            long var9 = var1.readLong();
            long var11;
            if ((var2 & 16) != 0) {
               var11 = var1.readLong();
            } else {
               var11 = -1L;
            }

            SendHandlerOneWayImpl var13 = this.createOneWay(var9, var11);
            ReceivedTwoWayImpl var14 = new ReceivedTwoWayImpl(this, var17, var1, var13);
            var7.invoke(var14, var5);
         } else {
            throw new AssertionError();
         }
      }
   }
}
