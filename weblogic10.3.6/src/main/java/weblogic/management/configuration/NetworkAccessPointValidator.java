package weblogic.management.configuration;

public class NetworkAccessPointValidator {
   public static void validateListenPort(int var0) {
      if (var0 != -1 && (var0 < 1 || var0 > 65535)) {
         throw new IllegalArgumentException("Illegal value for ListenPort: " + var0);
      }
   }

   public static void validatePublicPort(int var0) {
      if (var0 != -1 && (var0 < 1 || var0 > 65535)) {
         throw new IllegalArgumentException("Illegal value for PublicPort: " + var0);
      }
   }

   public static void validateMaxMessageSize(int var0) {
      if (var0 != -1 && (var0 < 1 || var0 > 65534)) {
         throw new IllegalArgumentException("Illegal value for MaxMessageSize: " + var0);
      }
   }

   public static void validateHttpEnabledForThisProtocol(NetworkAccessPointMBean var0, boolean var1) {
      if (var0.isTunnelingEnabled() && var0.isHttpEnabledForThisProtocol() && !var1) {
         throw new IllegalArgumentException("Can't disable HTTP as tunneling is enabled for channel: " + var0.getName());
      }
   }

   public static void validateTunnelingEnabled(NetworkAccessPointMBean var0, boolean var1) {
      if (!var0.isHttpEnabledForThisProtocol() && !var0.isTunnelingEnabled() && var1) {
         throw new IllegalArgumentException("Can't enable tunneling as HTTP is disabled for channel: " + var0.getName());
      }
   }
}
