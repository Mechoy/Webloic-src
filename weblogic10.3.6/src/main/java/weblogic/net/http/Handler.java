package weblogic.net.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Properties;
import weblogic.net.NetLogger;

public class Handler extends URLStreamHandler {
   private boolean isHTTP = true;
   private static final int HTTP_DEFAULT_PORT = 80;
   private static final int HTTPS_DEFAULT_PORT = 443;
   private static final String[] NEEDED_CLASSES = new String[]{"weblogic.net.http.Handler", "weblogic.net.http.Handler$1", "weblogic.net.http.HttpClient", "weblogic.net.http.HttpOutputStream", "weblogic.net.http.HttpURLConnection", "weblogic.net.http.HttpUnauthorizedException", "weblogic.net.http.KeepAliveCache", "weblogic.net.http.KeepAliveStream", "weblogic.net.http.MessageHeader", "weblogic.net.http.KeepAliveKey", "weblogic.net.http.HttpsURLConnection", "weblogic.net.http.HttpsClient"};
   private static boolean inited = false;

   public Handler() {
   }

   public Handler(boolean var1) {
      this.isHTTP = var1;
   }

   protected URLConnection openConnection(URL var1) throws IOException {
      return this.openConnection(var1, (Proxy)null);
   }

   protected URLConnection openConnection(URL var1, Proxy var2) throws IOException {
      String var3 = var1.getProtocol().toLowerCase();
      if (var3.equals("http")) {
         return new SOAPHttpURLConnection(var1, var2);
      } else {
         return var3.equals("https") ? new SOAPHttpsURLConnection(var1, var2) : null;
      }
   }

   protected int getDefaultPort() {
      return this.isHTTP ? 80 : 443;
   }

   public static void init() {
      if (!NETEnvironment.getNETEnvironment().useSunHttpHandler()) {
         if (!inited) {
            inited = true;

            try {
               if (Boolean.getBoolean("UseSunHttpHandler")) {
                  return;
               }
            } catch (SecurityException var13) {
            }

            try {
               String var0 = System.getProperty("javaplugin.enabled");

               for(int var1 = 0; var1 < NEEDED_CLASSES.length; ++var1) {
                  try {
                     Class.forName(NEEDED_CLASSES[var1]);
                  } catch (ThreadDeath var11) {
                     throw var11;
                  } catch (Throwable var12) {
                  }
               }

               if (var0 == null || var0.equals("false")) {
                  Properties var16 = System.getProperties();
                  String var2 = var16.getProperty("java.protocol.handler.pkgs");
                  if (var2 == null) {
                     var2 = "weblogic.net";
                  } else {
                     var2 = var2 + "|weblogic.net";
                  }

                  var16.put("java.protocol.handler.pkgs", var2);
                  System.setProperties(var16);
               }

               String var17 = System.getProperty("weblogic.net.http.URLStreamHandlerFactory");
               if (var17 != null) {
                  boolean var18 = false;

                  try {
                     Class var3 = Class.forName(var17);
                     Class[] var4 = new Class[]{String[].class};
                     Object[] var5 = new Object[]{new String[0]};
                     var3.getMethod("main", var4).invoke((Object)null, var5);
                  } catch (IllegalAccessException var6) {
                     var18 = true;
                     NetLogger.logHandlerInitFailure("IllegalAccessException", var17, var6);
                  } catch (InvocationTargetException var7) {
                     var18 = true;
                     NetLogger.logHandlerInitFailure("InvocationTargetException", var17, var7);
                  } catch (NoSuchMethodException var8) {
                     var18 = true;
                     NetLogger.logHandlerInitFailure("NoSuchMethodException", var17, var8);
                  } catch (ClassNotFoundException var9) {
                     var18 = true;
                     NetLogger.logHandlerInitFailure("ClassNotFoundException", var17, var9);
                  } catch (Error var10) {
                     var18 = true;
                     NetLogger.logHandlerInitFailure("Error", var17, var10);
                  }

                  if (!var18) {
                     return;
                  }
               }

               URLStreamHandlerFactory var19 = new URLStreamHandlerFactory() {
                  public URLStreamHandler createURLStreamHandler(String var1) {
                     if ("http".equals(var1)) {
                        return new Handler(true);
                     } else {
                        return "https".equals(var1) ? new Handler(false) : null;
                     }
                  }
               };
               if (var0 == null || var0.equals("false")) {
                  URL.setURLStreamHandlerFactory(var19);
               }
            } catch (SecurityException var14) {
               System.out.println("caught a SecurityException.  That's OK.");
               return;
            } catch (Error var15) {
            }

         }
      }
   }
}
