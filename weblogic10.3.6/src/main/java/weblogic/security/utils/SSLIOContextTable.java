package weblogic.security.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import javax.net.ssl.SSLSocket;
import weblogic.security.SecurityEnvironment;

public final class SSLIOContextTable {
   private static HashMap registeredContextsStream = new HashMap();
   private static HashMap registeredContextsSocket = new HashMap();
   private static HashSet throttledSockets = new HashSet();

   public static synchronized void addContext(SSLIOContext var0) {
      SSLSetupLogging.debug(3, "SSLIOContextTable.addContext(ctx): " + var0.hashCode());
      registeredContextsStream.put(var0.getRawInputStream(), var0);
      registeredContextsSocket.put(var0.getSSLSocket(), var0);
   }

   public static synchronized void removeContext(SSLIOContext var0) {
      SSLSetupLogging.debug(3, "SSLIOContextTable.removeContext(ctx): " + var0.hashCode());
      registeredContextsStream.remove(var0.getRawInputStream());
      registeredContextsSocket.remove(var0.getSSLSocket());
      unregisterThrottled(var0.getSSLSocket());
   }

   public static synchronized void removeContext(InputStream var0) {
      SSLSetupLogging.debug(3, "SSLIOContextTable.removeContext(is): " + var0.hashCode());
      SSLIOContext var1 = (SSLIOContext)registeredContextsStream.remove(var0);
      if (var1 != null) {
         registeredContextsSocket.remove(var1.getSSLSocket());
         unregisterThrottled(var1.getSSLSocket());
      }

   }

   public static synchronized void removeContext(SSLSocket var0) {
      SSLSetupLogging.debug(3, "SSLIOContextTable.removeContext(sock): " + var0.hashCode());
      SSLIOContext var1 = (SSLIOContext)registeredContextsSocket.remove(var0);
      if (var1 != null) {
         registeredContextsStream.remove(var1.getRawInputStream());
         unregisterThrottled(var0);
      }

   }

   public static synchronized SSLIOContext findContext(InputStream var0) {
      SSLSetupLogging.debug(3, "SSLIOContextTable.findContext(is): " + var0.hashCode());
      return (SSLIOContext)registeredContextsStream.get(var0);
   }

   public static synchronized SSLIOContext findContext(SSLSocket var0) {
      SSLSetupLogging.debug(3, "SSLIOContextTable.findContext(sock): " + var0.hashCode());
      return (SSLIOContext)registeredContextsSocket.get(var0);
   }

   public static synchronized void registerForThrottling(SSLSocket var0) {
      if (!throttledSockets.contains(var0) && registeredContextsSocket.containsKey(var0)) {
         throttledSockets.add(var0);
      }

   }

   private static void unregisterThrottled(SSLSocket var0) {
      if (throttledSockets.remove(var0)) {
         SecurityEnvironment.getSecurityEnvironment().decrementOpenSocketCount(var0);
      }

   }
}
