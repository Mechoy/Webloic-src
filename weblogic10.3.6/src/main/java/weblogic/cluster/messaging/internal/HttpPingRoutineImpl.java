package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import weblogic.net.http.HttpURLConnection;
import weblogic.utils.collections.ConcurrentHashSet;

public class HttpPingRoutineImpl implements PingRoutine {
   private static final boolean DEBUG;
   private static final int connectTimeout;
   private static final int readTimeout;
   private static final String http = "http";
   private static final String reqURI = "wls_does_not_exist_url";
   private static final HttpPingRoutineImpl THE_ONE;
   private static final ConcurrentHashSet pendingPings;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public static HttpPingRoutineImpl getInstance() {
      return THE_ONE;
   }

   public long ping(ServerConfigurationInformation var1) {
      if (pendingPings.contains(var1)) {
         if (DEBUG) {
            debug("There is already a ping pending for " + var1);
         }

         return 0L;
      } else {
         pendingPings.add(var1);
         long var2 = this.httpPing(var1.getAddress().getHostName(), var1.getPort());
         pendingPings.remove(var1);
         return var2;
      }
   }

   private long httpPing(String var1, int var2) {
      try {
         URL var3 = new URL("http", var1, var2, "wls_does_not_exist_url");
         HttpURLConnection var4 = new HttpURLConnection(var3);
         var4.setConnectTimeout(connectTimeout);
         var4.setReadTimeout(readTimeout);
         var4.connect();
         if (this.isError(var4.getResponseCode())) {
            if (DEBUG) {
               debug("The http response code is an error (" + var4.getResponseCode() + ") and this httpPing will return 0");
            }

            return 0L;
         } else {
            if (DEBUG) {
               debug("The http response code was not an error (" + var4.getResponseCode() + ") and this httpPing will return 1");
            }

            return 1L;
         }
      } catch (MalformedURLException var5) {
         throw new AssertionError("Could not ping server at host: " + var1 + " and port: " + var2);
      } catch (IOException var6) {
         if (DEBUG) {
            debug("HttpPing Caught IOException: " + var6);
         }

         return 0L;
      }
   }

   private boolean isError(int var1) {
      return 300 <= var1 && var1 < 400 || 500 <= var1 && var1 < 600;
   }

   public static void main(String[] var0) throws Exception {
      if (!$assertionsDisabled && var0 == null) {
         throw new AssertionError();
      } else if (!$assertionsDisabled && var0.length != 4) {
         throw new AssertionError();
      } else if (var0[0].equals("-host")) {
         String var1 = var0[1];
         if (var0[2].equals("-port")) {
            String var3 = var0[3];
            int var2 = Integer.parseInt(var3);
            System.out.println("HttpPing returns: " + THE_ONE.httpPing(var1, var2));
         } else {
            System.out.println("ERROR: HttpPingRoutingImpl -host <host> -port <port>");
         }
      } else {
         System.out.println("ERROR: HttpPingRoutingImpl -host <host> -port <port>");
      }
   }

   private static void debug(String var0) {
      Environment.getLogService().debug("[HttpPingRoutine] " + var0);
   }

   static {
      $assertionsDisabled = !HttpPingRoutineImpl.class.desiredAssertionStatus();
      DEBUG = Environment.DEBUG;
      connectTimeout = Integer.valueOf(System.getProperty("httpPing.connectTimeout", "3000"));
      readTimeout = Integer.valueOf(System.getProperty("httpPing.readTimeout", "6000"));
      THE_ONE = new HttpPingRoutineImpl();
      pendingPings = new ConcurrentHashSet();
   }
}
