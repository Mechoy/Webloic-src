package weblogic.common.internal;

public class VersionInfoFactory {
   private static boolean isServer = false;

   public static synchronized void initialize(boolean var0) {
      isServer = var0;
      getVersionInfo();
   }

   public static final VersionInfo getVersionInfo() {
      return VersionInfoFactory.VERSION_INFO_SINGLETON.instance;
   }

   public static final PeerInfo getPeerInfo() {
      return VersionInfoFactory.PEER_INFO_SINGLETON.instance;
   }

   public static final PeerInfo getPeerInfo(String var0) {
      PeerInfo var1 = VersionInfoFactory.PEER_INFO_SINGLETON.instance;
      return PeerInfo.getPeerInfo(var0);
   }

   public static final PeerInfo getPeerInfoForWire() {
      return VersionInfoFactory.PEER_INFO_FOR_WIRE_SINGLETON.instance;
   }

   private static class PEER_INFO_FOR_WIRE_SINGLETON {
      static PeerInfo instance;

      static {
         VersionInfo var0 = VersionInfoFactory.VERSION_INFO_SINGLETON.instance;
         instance = new PeerInfo(var0.getMajor(), var0.getMinor(), var0.getServicePack(), var0.getRollingPatch(), var0.hasTemporaryPatch(), (PackageInfo[])null);
      }
   }

   private static class PEER_INFO_SINGLETON {
      static PeerInfo instance;

      static {
         VersionInfo var0 = VersionInfoFactory.VERSION_INFO_SINGLETON.instance;
         instance = new PeerInfo(var0.getMajor(), var0.getMinor(), var0.getServicePack(), var0.getRollingPatch(), var0.hasTemporaryPatch(), var0.getPackages());
      }
   }

   private static class VERSION_INFO_SINGLETON {
      static VersionInfo instance;

      static {
         VersionInfo.initialize(VersionInfoFactory.isServer, "BEA Systems", "WebLogic Server 10.3.6.0  Tue Nov 15 08:52:36 PST 2011 1441050 ", "10.3.6.0", "10.3.6", 10, 3, 6, 0);
         instance = VersionInfo.theOne();
      }
   }
}
