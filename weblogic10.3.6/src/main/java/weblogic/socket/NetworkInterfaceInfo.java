package weblogic.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.kernel.Kernel;
import weblogic.utils.io.Chunk;

public class NetworkInterfaceInfo {
   private static Map<InetAddress, NetworkInterfaceInfo> map = new ConcurrentHashMap(10);
   private boolean asyncMuxer;
   private InetAddress localAddress;
   private int mtuSize;
   private boolean supportsGatheredWrites;
   private boolean supportsScatteredReads;
   private int numBuffers;

   public static NetworkInterfaceInfo getNetworkInterfaceInfo(InetAddress var0) {
      NetworkInterfaceInfo var1 = (NetworkInterfaceInfo)map.get(var0);
      if (var1 == null) {
         synchronized(map) {
            var1 = (NetworkInterfaceInfo)map.get(var0);
            if (var1 == null) {
               var1 = create(var0);
               map.put(var0, var1);
            }
         }
      }

      return var1;
   }

   private static NetworkInterfaceInfo create(InetAddress var0) {
      int var1 = 1500;

      try {
         NetworkInterface var2 = NetworkInterface.getByInetAddress(var0);
         if (var2 != null) {
            var1 = Jdk6.getMTU(var2);
         }
      } catch (IOException var3) {
      }

      NetworkInterfaceInfo var4 = new NetworkInterfaceInfo(var0, SocketMuxer.getMuxer().isAsyncMuxer(), var1);
      return var4;
   }

   private NetworkInterfaceInfo(InetAddress var1, boolean var2, int var3) {
      this.localAddress = var1;
      this.asyncMuxer = var2;
      this.mtuSize = var3;
      this.supportsGatheredWrites = this.isGatheredWritesEnabled() && var2;
      this.supportsScatteredReads = this.isScatteredReadsEnabled() && var2;
      this.numBuffers = this.mtuSize / Chunk.CHUNK_SIZE;
      if (this.numBuffers == 0) {
         this.numBuffers = 16;
      }

   }

   public InetAddress getLocalInetAddress() {
      return this.localAddress;
   }

   public int getMTU() {
      return this.mtuSize;
   }

   public boolean supportsGatheredWrites() {
      return this.supportsGatheredWrites;
   }

   public boolean supportsScatteredReads() {
      return this.supportsScatteredReads;
   }

   public int getOptimalNumberOfBuffers() {
      return this.numBuffers;
   }

   private boolean isGatheredWritesEnabled() {
      return Kernel.getConfig().isGatheredWritesEnabled();
   }

   private boolean isScatteredReadsEnabled() {
      return Kernel.getConfig().isScatteredReadsEnabled();
   }
}
