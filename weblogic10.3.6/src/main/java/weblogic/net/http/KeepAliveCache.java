package weblogic.net.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import weblogic.utils.AssertionError;

public final class KeepAliveCache {
   private static int LIFETIME = 15000;
   private static int PROXY_LIFETIME = 20000;
   private static int INTERVAL_RATE = 2;
   private static int HEALTH_CHECK_TIMOUT = 0;
   private Timer timer = new Timer(true);
   private final ConcurrentMap<KeepAliveKey, List<HttpClient>> cache = new ConcurrentHashMap();
   private Random random = new Random();

   public void put(HttpClient var1) {
      if (this.isClientTimeout(var1)) {
         var1.closeServer();
      } else {
         final KeepAliveKey var2 = new KeepAliveKey(var1.getURL(), var1.getClientInfo(), var1.instProxy);
         List var3 = (List)this.cache.get(var2);
         if (HttpURLConnection.debug) {
            HttpURLConnection.p("cache connection: " + var1);
         }

         if (var3 == null) {
            Vector var9 = new Vector();
            List var4 = (List)this.cache.putIfAbsent(var2, var9);
            if (var4 != null) {
               var4.add(var1);
               return;
            }

            var9.add(var1);
            int var5 = var1.usingProxy ? PROXY_LIFETIME : LIFETIME;
            TimerTask var6 = new TimerTask() {
               public void run() {
                  List var1 = (List)KeepAliveCache.this.cache.get(var2);
                  if (var1 == null) {
                     this.cancel();
                  } else {
                     synchronized(var1) {
                        if (var1.isEmpty()) {
                           if (HttpURLConnection.debug) {
                              HttpURLConnection.p("cancel cleanup timer task: " + this);
                           }

                           KeepAliveCache.this.cache.remove(var2);
                           this.cancel();
                        } else {
                           int var3;
                           for(var3 = 0; var3 < var1.size(); ++var3) {
                              HttpClient var4 = (HttpClient)var1.get(var3);
                              if (!KeepAliveCache.this.isClientTimeout(var4)) {
                                 break;
                              }

                              if (HttpURLConnection.debug) {
                                 HttpURLConnection.p("cleanup invalid client: " + var4);
                              }

                              var4.closeServer();
                           }

                           var1.subList(0, var3).clear();
                        }
                     }
                  }
               }
            };

            try {
               if (HttpURLConnection.debug) {
                  HttpURLConnection.p("init timer task: " + var6);
               }

               this.timer.schedule(var6, (long)var5, (long)(var5 / INTERVAL_RATE));
            } catch (IllegalStateException var8) {
               throw new AssertionError("Keep-Alive timer task cancelled: " + var6);
            }
         } else {
            var3.add(var1);
         }

      }
   }

   public HttpClient get(URL var1, Object var2) {
      return this.get(var1, var2, (Proxy)null);
   }

   public HttpClient get(URL var1, Object var2, Proxy var3) {
      KeepAliveKey var4 = new KeepAliveKey(var1, var2, var3);
      List var5 = (List)this.cache.get(var4);
      if (var5 != null && !var5.isEmpty()) {
         HttpClient var6 = null;
         synchronized(var5) {
            if (var5.size() == 0) {
               return null;
            }

            var6 = (HttpClient)var5.remove(this.random.nextInt(var5.size()));
         }

         if (var6 != null && !this.isClientTimeout(var6)) {
            if (HEALTH_CHECK_TIMOUT > 0 && !this.isSocketHealthy(var6)) {
               var6.closeServer();
               return null;
            } else {
               if (HttpURLConnection.debug) {
                  HttpURLConnection.p("reuse connection from cache: " + var6);
               }

               return var6;
            }
         } else {
            if (var6 != null) {
               var6.closeServer();
            }

            return null;
         }
      } else {
         if (HttpURLConnection.debug) {
            HttpURLConnection.p("doesn't hit for: " + var4);
         }

         return null;
      }
   }

   private boolean isClientTimeout(HttpClient var1) {
      long var2 = System.currentTimeMillis() - var1.lastUsed;
      return var1.usingProxy ? var2 > (long)PROXY_LIFETIME : var2 > (long)LIFETIME;
   }

   private boolean isSocketHealthy(HttpClient var1) {
      try {
         var1.serverSocket.setSoTimeout(HEALTH_CHECK_TIMOUT);
         var1.serverSocket.getInputStream().read();
      } catch (SocketTimeoutException var3) {
         return true;
      } catch (SocketException var4) {
      } catch (IOException var5) {
      }

      return false;
   }

   static {
      try {
         LIFETIME = Integer.getInteger("http.keepAliveCache.lifeTime", LIFETIME);
         PROXY_LIFETIME = Integer.getInteger("http.keepAliveCache.proxyLifeTime", PROXY_LIFETIME);
         HEALTH_CHECK_TIMOUT = Integer.getInteger("http.keepAliveCache.socketHealthCheckTimeout", HEALTH_CHECK_TIMOUT);
      } catch (SecurityException var1) {
      }

   }
}
