package weblogic.socket.utils;

import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public final class SDPSocketUtils {
   private static volatile Method sdpServerSocketMethod;
   private static volatile Method sdpSocketMethod;

   private static Method getSDPServerSocketChannelMethod() throws Exception {
      if (sdpServerSocketMethod != null) {
         return sdpServerSocketMethod;
      } else {
         Class var0 = Class.forName("com.oracle.net.Sdp");
         synchronized(var0) {
            if (sdpServerSocketMethod == null) {
               ensureEnvironment();
               sdpServerSocketMethod = var0.getDeclaredMethod("openServerSocketChannel", (Class[])null);
            }
         }

         return sdpServerSocketMethod;
      }
   }

   private static Method getSDPSocketChannelMethod() throws Exception {
      if (sdpSocketMethod != null) {
         return sdpSocketMethod;
      } else {
         Class var0 = Class.forName("com.oracle.net.Sdp");
         synchronized(var0) {
            if (sdpSocketMethod == null) {
               ensureEnvironment();
               sdpSocketMethod = var0.getDeclaredMethod("openSocketChannel", (Class[])null);
            }
         }

         return sdpSocketMethod;
      }
   }

   public static Socket createSDPSocket() {
      try {
         Method var0 = getSDPSocketChannelMethod();
         SocketChannel var3 = (SocketChannel)var0.invoke((Object)null, (Object[])null);
         return var3.socket();
      } catch (Exception var2) {
         AssertionError var1 = new AssertionError("Failed to create SDP Server Socket");
         var1.initCause(var2);
         throw var1;
      }
   }

   public static ServerSocket createSDPServerSocket() {
      try {
         Method var0 = getSDPServerSocketChannelMethod();
         ServerSocketChannel var3 = (ServerSocketChannel)var0.invoke((Object)null, (Object[])null);
         return var3.socket();
      } catch (Exception var2) {
         AssertionError var1 = new AssertionError("Failed to create SDP Server Socket");
         var1.initCause(var2);
         throw var1;
      }
   }

   public static void ensureEnvironment() {
      if (!Boolean.getBoolean("java.net.preferIPv4Stack")) {
         throw new AssertionError("SDP protocol requires system property java.net.preferIPv4Stack to be set to true");
      }
   }
}
