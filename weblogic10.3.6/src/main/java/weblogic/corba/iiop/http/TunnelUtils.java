package weblogic.corba.iiop.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

public final class TunnelUtils {
   private static final boolean DEBUG = getDebug();
   public static final String TUNNEL_SEND = "/bea_wls_internal/iiop/ClientSend";
   public static final String TUNNEL_RECV = "/bea_wls_internal/iiop/ClientRecv";
   public static final String TUNNEL_LOGIN = "/bea_wls_internal/iiop/ClientLogin";
   public static final String TUNNEL_CLOSE = "/bea_wls_internal/iiop/ClientClose";
   public static final int MSG_HEADER_LENGTH = 12;
   public static final String TUNNEL_OK = "OK";
   public static final String TUNNEL_DEAD = "DEAD";
   public static final String TUNNEL_RETRY = "RETRY";
   public static final String TUNNEL_UNAVAIL = "UNAVAIL";
   public static final String RESULT_HEADER = "WL-Result";
   public static final String TYPE_HEADER = "WL-Type";
   public static final String VERSION_HEADER = "WL-Version";
   public static final String ID_HEADER = "Conn-Id";
   public static final String CONNECT_PARAM_HEADER_LEN = "HL";
   public static final String DEST_HEADER = "WL-Dest";
   public static final String CLUSTER_LIST_HEADER = "WL-List";
   public static final String SCHEME_HEADER = "WL-Scheme";
   public static final String PARAM_CONNECTION_ID = "connectionID";
   public static final String PARAM_RAND = "rand";
   public static final String TUNNEL_COOKIE_NAME = "JSESSIONID";
   public static final String CLIENT_ADDRESS = "WL-Client-Address";
   private static final SecureRandom rand = new SecureRandom();

   private static final boolean getDebug() {
      try {
         return Boolean.getBoolean("weblogic.debug.client.http.tunnelUtils");
      } catch (Exception var1) {
         return false;
      }
   }

   public static long getNextRandom() {
      return rand.nextLong() & Long.MAX_VALUE;
   }

   public static void drainStream(InputStream var0) throws IOException {
      if (var0 != null) {
         boolean var1 = false;

         int var2;
         do {
            var2 = var0.read();
         } while(var2 != -1);

         var0.close();
      }

   }

   public static int toInt(int var0) {
      return var0 & 255;
   }

   public static void readConnectionParams(DataInputStream var0) throws IOException {
      boolean var1 = true;
      String var2 = "";

      while((var2 = var0.readLine()) != null && var2.length() != 0) {
         if (var2.charAt(0) == "HL".charAt(0) && var2.charAt(1) == "HL".charAt(1)) {
            try {
               Integer.parseInt(var2.substring(var2.indexOf(58) + 1, var2.length()));
            } catch (Exception var4) {
               throw new ProtocolException("Invalid parameter: " + var2);
            }
         }
      }

   }

   public static Properties getProperties(String var0) {
      if (var0 == null) {
         return null;
      } else if (var0.indexOf(61) == -1) {
         return null;
      } else {
         Properties var1 = new Properties();
         if (var0.indexOf(44) == -1) {
            setProperty(var1, var0);
            return var1;
         } else {
            String[] var2 = splitCompletely(var0, ",");

            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var2[var3].indexOf(61) != -1) {
                  setProperty(var1, var2[var3]);
               }
            }

            return var1;
         }
      }
   }

   public static void setCustomRequestProperties(Properties var0, URLConnection var1) {
      String var3;
      String var4;
      if (var0 != null) {
         for(Enumeration var2 = var0.propertyNames(); var2.hasMoreElements(); var1.setRequestProperty(var3, var4)) {
            var3 = (String)var2.nextElement();
            var4 = var0.getProperty(var3);
            if (DEBUG) {
               p("setRequestProperty(" + var3 + "," + var4 + ")");
            }
         }
      }

   }

   private static String[] splitCompletely(String var0, String var1) {
      StringTokenizer var2 = new StringTokenizer(var0, var1);
      int var3 = var2.countTokens();
      String[] var4 = new String[var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         var4[var5] = var2.nextToken();
      }

      return var4;
   }

   private static final void p(String var0) {
      System.out.println("<TunnelUtils>: " + var0);
   }

   private static final void setProperty(Properties var0, String var1) {
      String var2 = var1.substring(0, var1.indexOf(61));
      String var3 = var1.substring(var1.indexOf(61) + 1);
      if (DEBUG) {
         p("CustomHeader(" + var2 + "," + var3 + ")");
      }

      var0.setProperty(var2, var3);
   }

   public static final String getRequestArgs(String var0) {
      return "?connectionID=" + var0 + "&rand=" + getNextRandom();
   }
}
