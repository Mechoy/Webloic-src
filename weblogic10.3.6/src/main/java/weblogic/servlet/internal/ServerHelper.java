package weblogic.servlet.internal;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.servlet.utils.ServletObjectInputStream;
import weblogic.servlet.utils.ServletObjectOutputStream;

public final class ServerHelper {
   private static final String CLUSTER_LIST_IPV6_PREFIX = "ipv6_";
   private static final boolean useExtendedSessionFormat = "true".equalsIgnoreCase(System.getProperty("weblogic.servlet.useExtendedSessionFormat"));
   private static final Pattern ipv6regex = Pattern.compile("((:?([0-9a-fA-F]{0,4})){0,8})((\\.?[0-9]{1,3}){0,4})");

   public static final String createServerEntry(String var0, ServerIdentity var1, String var2) {
      return createServerEntry(var0, var1, var2, true);
   }

   public static final String createServerEntry(String var0, ServerIdentity var1, String var2, boolean var3) {
      ServerChannel var4 = ServerChannelManager.findServerChannel(var1, var0);
      if (var4 == null) {
         return null;
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append(var1.hashCode()).append(var2);
         if (var3) {
            var5.append(getRawAddress(var4.getPublicAddress()));
         } else {
            var5.append(var4.getPublicAddress());
         }

         var5.append(var2);
         ServerChannel var6;
         if (var4.supportsTLS()) {
            var6 = ServerChannelManager.findRelatedServerChannel(var1, ProtocolHandlerHTTP.PROTOCOL_HTTP, var4.getPublicAddress());
            var5.append(var6 == null ? -1 : var6.getPublicPort()).append(var2);
            var5.append(var4.getPublicPort());
         } else {
            var6 = ServerChannelManager.findRelatedServerChannel(var1, ProtocolHandlerHTTPS.PROTOCOL_HTTPS, var4.getPublicAddress());
            var5.append(var4.getPublicPort()).append(var2);
            var5.append(var6 == null ? -1 : var6.getPublicPort());
         }

         return var5.toString();
      }
   }

   public static final String getRawAddress(String var0) {
      try {
         InetAddress var1 = InetAddress.getByName(var0);
         boolean var2 = var1 instanceof Inet6Address;
         if (var2) {
            if (!ipv6regex.matcher(var0).matches()) {
               return var0;
            }
         } else if (!var1.getHostAddress().equals(var0)) {
            return var0;
         }

         byte[] var3 = var1.getAddress();
         if (var2) {
            BigInteger var8 = new BigInteger(var3);
            return "ipv6_" + var8.toString();
         } else {
            long var4 = 0L;

            for(int var6 = 0; var6 < var3.length; ++var6) {
               var4 += (long)((var3[var3.length - (var6 + 1)] & 255) << var6 * 8);
            }

            return Long.toString(var4);
         }
      } catch (UnknownHostException var7) {
         return var0;
      }
   }

   public static final boolean useExtendedSessionFormat() {
      return useExtendedSessionFormat;
   }

   public static final String getNetworkChannelName() {
      ServerChannel var0 = weblogic.rmi.extensions.server.ServerHelper.getServerChannel();
      return var0 != null ? var0.getChannelName() : "Default";
   }

   public static byte[] passivate(Object var0) throws IOException {
      ServletObjectOutputStream var1 = ServletObjectOutputStream.getOutputStream();
      var1.writeObject(var0);
      return var1.toByteArray();
   }

   public static Object activate(byte[] var0) throws IOException, ClassNotFoundException {
      ServletObjectInputStream var1 = ServletObjectInputStream.getInputStream(var0);
      return var1.readObject();
   }

   public static byte[] zipBytes(byte[] var0) {
      Deflater var1 = new Deflater(9);
      var1.setInput(var0);
      var1.finish();
      int var2 = var1.deflate(var0);
      byte[] var3 = new byte[var2];
      System.arraycopy(var0, 0, var3, 0, var2);
      return var3;
   }

   public static void unzipBytes(byte[] var0, byte[] var1) throws DataFormatException {
      Inflater var2 = new Inflater();
      var2.setInput(var0, 0, var0.length);
      var2.end();
      var2.inflate(var1);
   }
}
