package weblogic.servlet.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.CharArrayWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.version;
import weblogic.servlet.internal.ChunkInput;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.utils.Hex;
import weblogic.utils.StringUtils;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedInputStream;
import weblogic.utils.io.NullInputStream;

public final class HttpClusterServlet extends GenericProxyServlet {
   private static final int DEFAULT_PORT = 80;
   private static final int DEFAULT_SSL_PORT = 443;
   public static final int MAX_POST_IN_MEMORY;
   protected int connectTimeoutSecs;
   protected int connectRetrySecs;
   /** @deprecated */
   protected int numOfRetries;
   protected boolean idempotent;
   protected boolean ignoreCookie;
   protected boolean useDynamicList;
   protected boolean crossOverProxyEnabled;
   protected int maxSkips = 0;
   protected long maxSkipTime = 0L;
   protected boolean proxyForConnectionResets;
   protected ServerList srvrList;
   protected Map allKnownServers;
   protected List<RoutingHandler> routingHandlers = new ArrayList();
   private final List<ServerListListener> _serverListListeners = new ArrayList();

   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      if (this.verbose) {
         this.trace("HttpClusterServlet:init()");
      }

      String var2 = this.getInitParameter("WebLogicCluster");
      if (var2 == null) {
         var2 = this.getInitParameter("defaultServers");
      }

      if (var2 == null) {
         throw new ServletException("WebLogicCluster is not defined, cannot continue");
      } else {
         if (this.srvrList == null) {
            ServerList var3 = new ServerList(var2, true);
            this.srvrList = var3;
         }

         var2 = this.getInitParameter("ConnectTimeoutSecs");
         if (var2 == null) {
            this.connectTimeoutSecs = 10;
         } else {
            this.connectTimeoutSecs = Integer.parseInt(var2);
         }

         var2 = this.getInitParameter("ConnectRetrySecs");
         if (var2 == null) {
            this.connectRetrySecs = 2;
         } else {
            this.connectRetrySecs = Integer.parseInt(var2);
         }

         var2 = this.getInitParameter("numOfRetries");
         if (var2 == null) {
            this.numOfRetries = this.connectTimeoutSecs / this.connectRetrySecs;
         } else {
            this.numOfRetries = Integer.parseInt(var2);
         }

         var2 = this.getInitParameter("Idempotent");
         this.idempotent = isTrue(var2, true);
         var2 = this.getInitParameter("DisableCookie2Server");
         this.ignoreCookie = isTrue(var2, false);
         var2 = this.getInitParameter("RoutingHandlerClassName");
         ClassLoader var9 = Thread.currentThread().getContextClassLoader();
         if (var9 == null) {
            var9 = this.getClass().getClassLoader();
         }

         if (var2 != null) {
            StringTokenizer var4 = new StringTokenizer(var2, ",");

            while(var4.hasMoreTokens()) {
               String var5 = var4.nextToken().trim();

               try {
                  Class var6 = var9.loadClass(var5);
                  RoutingHandler var7 = (RoutingHandler)var6.newInstance();
                  var7.init(this);
                  this.routingHandlers.add(var7);
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            }
         }

         if (this.routingHandlers.isEmpty()) {
            var2 = this.getInitParameter("DynamicServerList");
            this.useDynamicList = isTrue(var2, true);
         } else {
            this.useDynamicList = true;
         }

         var2 = this.getInitParameter("WLCrossOverProxyEnabled");
         this.crossOverProxyEnabled = isTrue(var2, false);
         var2 = this.getInitParameter("MaxSkips");
         if (var2 != null) {
            this.maxSkips = Integer.parseInt(var2);
            if (this.verbose) {
               this.trace("MaxSkips is deprecated and replaced by MaxSkipTime");
            }
         }

         var2 = this.getInitParameter("MaxSkipTime");
         if (var2 != null) {
            this.maxSkipTime = (long)(Integer.parseInt(var2) * 1000);
         } else {
            this.maxSkipTime = 10000L;
         }

         var2 = this.getInitParameter("ProxyForConnectionResets");
         this.proxyForConnectionResets = isTrue(var2, false);
         this.allKnownServers = createNewMap();
         this.srvrList.addToKnownServersList(this.allKnownServers);
         this.notifyServerListChange();
      }
   }

   public void destroy() {
      Iterator var1 = this.routingHandlers.iterator();

      while(var1.hasNext()) {
         RoutingHandler var2 = (RoutingHandler)var1.next();

         try {
            var2.destroy();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      super.destroy();
   }

   public ServerList getServerList() {
      return this.srvrList;
   }

   public void addServerListChangeListener(ServerListListener var1) {
      synchronized(this._serverListListeners) {
         if (!this._serverListListeners.contains(var1)) {
            this._serverListListeners.add(var1);
         }

      }
   }

   public void removeServerListChangeListener(ServerListListener var1) {
      synchronized(this._serverListListeners) {
         this._serverListListeners.remove(var1);
      }
   }

   private void notifyServerListChange() {
      ServerListListener[] var1;
      synchronized(this._serverListListeners) {
         var1 = (ServerListListener[])this._serverListListeners.toArray(new ServerListListener[this._serverListListeners.size()]);
      }

      ServerListListener[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ServerListListener var5 = var2[var4];

         try {
            var5.serverListChanged();
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

   }

   private void notifyRoutingDecision(RequestInfo var1, Server var2) {
      Map var3 = var1.getNotificationData();
      Iterator var4 = var3.keySet().iterator();

      while(var4.hasNext()) {
         RoutingHandler var5 = (RoutingHandler)var4.next();
         Object var6 = var3.get(var5);
         var5.notifyRoutingDecision(var2, var6);
      }

   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      GenericProxyServlet.ProxyConnection var3 = null;
      Server var4 = null;
      RequestInfo var5 = new RequestInfo();

      try {
         var5.setVerbose(this.verbose);
         var5.setServerList(this.srvrList);
         var5.setIsSecureNeeded(this.isSecureProxy);
         var5.setCanFailover(this.idempotent);
         var5.setRequest(this.resolveRequest(var1));
         var5.setPost(!var1.getMethod().equalsIgnoreCase("GET") && !var1.getMethod().equalsIgnoreCase("HEAD") && (var1.getHeader("Content-Length") != null || var1.getHeader("Transfer-Encoding") != null));
         if (!this.readPostData(var5, var1)) {
            if (this.verbose) {
               this.trace("Failed to read the post data.");
            }

            var2.sendError(400, "Invalid Post");
         } else {
            this.getPreferred(var5, var1);
            String var6 = var1.getQueryString();
            if (var6 != null && this.debugConfigInfo && var6.equals("__WebLogicBridgeConfig")) {
               this.printConfigInfo(var5, var2.getWriter());
            } else {
               int var7 = 0;
               boolean var8 = true;
               boolean var9 = false;
               var4 = var5.getPrimaryServer();
               if (var4 != null && var4.isBad()) {
                  var4 = null;
               }

               if (this.verbose && var4 != null) {
                  this.trace("#### Trying to connect to the primary server:" + var4);
               }

               while(var3 == null) {
                  if (this.verbose) {
                     this.trace("attempt #" + var7 + " out of a max of " + this.numOfRetries);
                  }

                  if (var4 == null) {
                     var4 = var5.getServerInList();
                     if (var4 == null) {
                        if (this.verbose) {
                           this.trace("=== whole list is bad, reverting to the all servers list ===");
                        }

                        var5.revertToAllKnownServersList();
                        var4 = var5.getServerInList();
                        if (var4 == null) {
                           break;
                        }

                        var8 = false;
                        if (this.verbose) {
                           this.trace("#### Trying to connect with server " + var4);
                        }
                     } else if (this.verbose) {
                        this.trace("#### Trying to connect with server " + var4);
                     }
                  }

                  var3 = this.attemptConnect(var5, var4, var1);
                  if (var3 == null) {
                     if (!var4.isBad()) {
                        if (this.verbose) {
                           this.trace("attemptConnect failed to read post data from client");
                        }

                        var2.sendError(500, "Error reading post data");
                        return;
                     }

                     var4 = null;
                     var8 = false;
                  } else {
                     this.notifyRoutingDecision(var5, var4);
                     var3 = this.sendResponse(var5, var1, var2, var3, var4);
                     if (var3 != null) {
                        if (this.verbose) {
                           this.trace("Request successfully processed");
                        }

                        this.connPool.requeue(var3);
                        return;
                     }

                     if (!var4.isBad()) {
                        if (this.verbose) {
                           this.trace("sendResponse failed to write to client");
                        }

                        var2.sendError(500, "Error writing response data");
                        return;
                     }

                     if (!var5.canFailover()) {
                        if (this.verbose) {
                           this.trace("sendResponse failed to read response from server and cannot failover");
                        }

                        var2.sendError(500, "Error reading response data");
                        return;
                     }

                     if (this.verbose) {
                        this.trace("Error reading response code, or got 503. Trying next server");
                     }

                     var4 = null;
                     var8 = false;
                  }

                  if (var7 >= this.numOfRetries) {
                     if (this.verbose) {
                        this.trace("Tried all servers but didn't succeed");
                     }
                     break;
                  }

                  if (!var8) {
                     var8 = true;
                     if (this.verbose) {
                        this.trace("Sleeping for " + this.connectRetrySecs + " secs .....");
                     }

                     try {
                        ++var7;
                        Thread.sleep((long)(this.connectRetrySecs * 1000));
                     } catch (InterruptedException var38) {
                     }
                  }
               }

               if (var3 == null) {
                  var2.sendError(503, "No backend servers available");
               }

            }
         }
      } finally {
         boolean var13 = false;
         var5.deletePostDataFile();
         var5.releasePostData();
         String var14 = var5.getDynamicList();
         if (var14 != null) {
            ServerList var15 = new ServerList(var14, false);
            if (!this.useDynamicList) {
               if (this.srvrList.getHash() == null || !this.srvrList.getHash().equals(var5.getDynamicHash())) {
                  synchronized(this.srvrList) {
                     if (this.srvrList.getHash() == null || !this.srvrList.getHash().equals(var5.getDynamicHash())) {
                        this.srvrList.setHash(var5.getDynamicHash());
                        this.populateJVMID(this.srvrList, var15);
                        var13 = true;
                     }
                  }
               }
            } else {
               var15.setHash(var5.getDynamicHash());
               if (this.verbose) {
                  this.trace("Updating dynamic server list: " + var14);
               }

               this.srvrList = var15;
               var15.addToKnownServersList(this.allKnownServers);
               var13 = true;
            }
         }

         String var41 = var5.getServerJVMID();
         if (var41 != null && var4 != null && !var41.equals(var4.getJVMID())) {
            synchronized(this.srvrList) {
               if (!var41.equals(var4.getJVMID())) {
                  if (this.verbose) {
                     this.trace("updating JVMID " + var41 + " for " + var4);
                  }

                  this.srvrList.remove(var4);
                  var4.setJVMID(var41);
                  this.srvrList.add(var4);
                  var13 = true;
               }
            }
         }

         if (var13) {
            this.notifyServerListChange();
         }

      }
   }

   private void populateJVMID(ServerList var1, ServerList var2) {
      TreeMap var3 = new TreeMap(new Comparator() {
         public int compare(Object var1, Object var2) {
            Server var3 = (Server)var1;
            Server var4 = (Server)var2;
            String var5 = var3.getHostIp();
            String var6 = var4.getHostIp();
            int var7 = var3.getPort();
            int var8 = var4.getPort();
            int var9 = var3.getSecurePort();
            int var10 = var4.getSecurePort();
            if (var5 != null && var6 != null) {
               int var11 = var5.compareTo(var6);
               if (var11 == 0) {
                  var11 = var7 - var8;
                  if (var11 == 0 && var7 == -1) {
                     return var9 - var10;
                  }

                  return var11;
               }
            }

            return -1;
         }
      });
      Server[] var4 = var1.toArray();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var3.put(var4[var5], var4[var5]);
      }

      Iterator var8 = var2.iterator();

      while(var8.hasNext()) {
         Server var6 = (Server)var8.next();
         if (var3.containsKey(var6)) {
            Server var7 = (Server)var3.get(var6);
            var1.remove(var7);
            var1.add(var6);
         }
      }

   }

   public void printConfigInfo(RequestInfo var1, PrintWriter var2) {
      var2.write("<HTML><TITLE>WEBLOGIC PROXY DEBUG INFO</TITLE>");
      var2.write("<FONT FACE=\"Tahoma\">");
      var2.write("<BODY>Query String: __WebLogicBridgeConfig");
      if (var1.getPrimaryServer() != null) {
         Server var3 = var1.getPrimaryServer();
         StringBuilder var4 = new StringBuilder(256);
         var4.append("<BR><BR><B>Primary Server:</B> <FONT COLOR=\"#0000ff\">");
         var4.append(var3.getHost());
         var4.append(":");
         var4.append(var3.getPort());
         var4.append(":");
         var4.append(var3.getSecurePort());
         var4.append("</FONT>");
         var2.write(var4.toString());
         if (var1.getSecondaryServer() != null) {
            var3 = var1.getSecondaryServer();
            var4 = new StringBuilder(516);
            var4.append("<BR><BR><B>Secondary Server:</B> <FONT COLOR=\"#0000ff\">");
            var4.append(var3.getHost());
            var4.append(":");
            var4.append(var3.getPort());
            var4.append(":");
            var4.append(var3.getSecurePort());
            var4.append("</FONT>");
            var2.write(var4.toString());
         }
      }

      ServerList var7 = var1.getServerList();
      var2.write("<BR><BR><B>General Server List:</B><OL>");

      for(int var8 = 0; var8 < var7.size(); ++var8) {
         StringBuilder var5 = new StringBuilder(256);
         Server var6 = var7.next();
         var5.append("<LI> <FONT COLOR=\"#0000ff\">");
         var5.append(var6.getHost());
         var5.append(":");
         var5.append(var6.getPort());
         var5.append(":");
         var5.append(var6.getSecurePort());
         var5.append("</FONT></LI>");
         var2.write(var5.toString());
      }

      var2.write("</OL><BR><B>ConnectRetrySecs: </B>" + this.connectRetrySecs);
      var2.write("<BR><B>ConnectTimeoutSecs: </B>" + this.connectTimeoutSecs);
      if (this.cookieName != null) {
         var2.write("<BR><B>CookieName: </B><font color=#0000ff> deprecated</font>");
      }

      var2.write("<BR><B>WLCookieName: </B>" + this.wlCookieName);
      var2.write("<BR><B>Debug: </B>" + this.verbose);
      var2.write("<BR><B>DebugConfigInfo: </B>" + this.debugConfigInfo);
      var2.write("<BR><B>DefaultFileName: </B>" + this.defaultFileName);
      var2.write("<BR><B>DisableCookie2Server: </B>" + this.ignoreCookie);
      var2.write("<BR><B>DynamicServerList: </B>" + this.useDynamicList);
      var2.write("<BR><B>FileCaching: </B>" + this.fileCaching);
      var2.write("<BR><B>WLIOTimeoutSecs: </B>" + this.socketTimeout);
      var2.write("<BR><B>Idempotent: </B>" + this.idempotent);
      var2.write("<BR><B>KeepAliveEnabled: </B>" + this.keepAliveEnabled);
      var2.write("<BR><B>KeepAliveSecs: </B>" + this.keepAliveSecs);
      var2.write("<BR><B>MaxPostSize: </B>" + this.maxPostSize);
      if (this.maxSkips != 0) {
         var2.write("<BR><B>MaxSkips: </B>deprecated");
      }

      var2.write("<BR><B>MaxSkipTime: </B>" + this.maxSkipTime / 1000L);
      var2.write("<BR><B>PathPrepend: </B>" + this.pathPrepend);
      var2.write("<BR><B>PathTrim: </B>" + this.pathTrim);
      var2.write("<BR><B>TrimExt: </B>" + this.trimExt);
      var2.write("<BR><B>SecureProxy: </B>" + this.isSecureProxy);
      var2.write("<BR><B>WLLogFile: </B>" + this.logFileName);
      var2.write("<BR><B>WLProxySSL: </B>" + this.wlProxySSL);
      var2.write("<BR><B>ProxyForConnectionResets: </B>" + this.proxyForConnectionResets);
      var2.write("<BR>_____________________________________________________");
      var2.write("<BR><BR>Last Modified: " + version.getBuildVersion());
      var2.write("</BODY></HTML>");
      var2.close();
   }

   protected void addRequestHeaders(HttpServletRequest var1, PrintStream var2, Object var3, Object var4) {
      super.addRequestHeaders(var1, var2, var3, var4);
      Iterator var5 = this.routingHandlers.iterator();

      while(var5.hasNext()) {
         RoutingHandler var6 = (RoutingHandler)var5.next();

         try {
            var6.addRequestHeaders(var1, var2, (RequestInfo)var3, (Server)var4);
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }

      if (this.srvrList.getHash() != null) {
         var2.print("X-WebLogic-Cluster-Hash: " + this.srvrList.getHash());
         var2.print("\r\n");
      }

      int var9 = ((RequestInfo)var3).getContentLen();
      if (var9 != -1) {
         var2.print("Content-Length: " + var9);
         var2.print("\r\n");
      }

      if (this.srvrList.getHash() == null) {
         if (((Server)var4).getJVMID() == null) {
            var2.print("X-WebLogic-Force-JVMID: unset");
         } else {
            var2.print("X-WebLogic-Force-JVMID: " + ((Server)var4).getJVMID());
         }

         var2.print("\r\n");
      }

      var2.print("X-WebLogic-Request-ClusterInfo: true");
      var2.print("\r\n");
   }

   public void addResponseHeaders(HttpServletResponse var1, String var2, String var3, Object var4) {
      RequestInfo var5 = (RequestInfo)var4;
      if (var5.needToUpdateDynamicList() && var2.equals("X-WebLogic-Cluster-List")) {
         var5.setDynamicList(var3);
      } else if (var5.needToUpdateDynamicList() && var2.equals("X-WebLogic-Cluster-Hash")) {
         var5.setDynamicHash(var3);
      } else if (var2.equals("X-WebLogic-JVMID")) {
         var5.setServerJVMID(var3);
      } else {
         boolean var6 = false;
         Iterator var7 = this.routingHandlers.iterator();

         while(var7.hasNext()) {
            RoutingHandler var8 = (RoutingHandler)var7.next();

            try {
               var6 |= var8.handleResponseHeader(var1, var2, var3, var5);
            } catch (Exception var10) {
               var10.printStackTrace();
            }
         }

         if (!var6) {
            super.addResponseHeaders(var1, var2, var3, var4);
         }
      }
   }

   public boolean getPreferred(RequestInfo var1, HttpServletRequest var2) {
      var1.setNotificationData(new HashMap());
      Iterator var3 = this.routingHandlers.iterator();

      while(var3.hasNext()) {
         RoutingHandler var4 = (RoutingHandler)var3.next();

         try {
            RequestInfo var5 = var4.route(var1, var2);
            if (var5 != null) {
               return true;
            }
         } catch (Exception var19) {
            var19.printStackTrace();
         }
      }

      String var20 = null;
      int var7;
      int var24;
      if (!this.ignoreCookie) {
         Enumeration var21 = var2.getHeaders("Cookie");

         label326:
         while(true) {
            do {
               if (!var21.hasMoreElements() || var20 != null) {
                  break label326;
               }

               var20 = (String)var21.nextElement();
            } while(var20 == null);

            var24 = -1;

            for(int var6 = 0; var6 < var20.length() - this.wlCookieName.length(); var24 = -1) {
               var24 = var20.indexOf(this.wlCookieName, var6);
               if (var24 < 1) {
                  break;
               }

               for(var7 = var24 - 1; var7 > -1 && var20.charAt(var7) == ' '; --var7) {
               }

               if (var7 < 0 || var20.charAt(var7) == ';') {
                  break;
               }

               var6 = var24 + this.wlCookieName.length();
            }

            if (var24 >= 0) {
               try {
                  var20 = var20.substring(var24 + this.wlCookieName.length() + 1);
                  var24 = var20.indexOf(";");
                  if (var24 != -1) {
                     var20 = var20.substring(0, var24);
                  }

                  var24 = var20.indexOf("!");
                  var20 = var20.substring(var24 + 1);
                  if (this.verbose) {
                     this.trace("Found cookie: " + var20);
                  }
               } catch (ArrayIndexOutOfBoundsException var18) {
                  var20 = null;
               }
            } else {
               var20 = null;
            }
         }
      }

      if (var20 == null) {
         var20 = ServletRequestImpl.getOriginalRequest(var2).getSessionHelper().getEncodedSessionID();
         if (var20 != null) {
            int var22 = var20.indexOf("!");
            if (var20.endsWith("|")) {
               var20 = var20.substring(var22 + 1, var20.length() - 1);
            } else {
               var20 = var20.substring(var22 + 1);
            }
         }

         if (this.verbose) {
            this.trace("Found session from url: " + var20);
         }
      }

      if (var20 == null && var1.isPost()) {
         String var23 = var2.getContentType();
         var24 = this.wlCookieName.length();
         if (var23 != null && var23.startsWith("application/x-www-form-urlencoded")) {
            Chunk var26 = var1.getPostData();
            if (var26 != null) {
               var7 = Chunk.size(var26);
               byte[] var8 = new byte[Chunk.size(var26)];
               Chunk var9 = var26;

               for(int var10 = 0; var9 != null; var9 = var9.next) {
                  System.arraycopy(var9.buf, 0, var8, var10, var9.end);
                  var10 += var9.end;
               }

               int var11 = 0;

               for(int var12 = 0; var12 < var7; ++var12) {
                  if (var8[var12] == 37 && var12 + 2 < var7 && Hex.isHexChar(var8[var12 + 1]) && Hex.isHexChar(var8[var12 + 2])) {
                     var8[var11++] = (byte)((Hex.hexValueOf(var8[var12 + 1]) << 4) + (Hex.hexValueOf(var8[var12 + 2]) << 0));
                     var12 += 2;
                  } else if (var8[var12] == 43) {
                     var8[var11++] = 32;
                  } else {
                     var8[var11++] = var8[var12];
                  }
               }

               char var30 = this.wlCookieName.charAt(0);

               for(int var13 = 0; var13 < var11; ++var13) {
                  if (var8[var13] != var30) {
                     if ((char)var8[var13] != ' ') {
                        while(var13 < var11 && (char)var8[var13] != '&') {
                           ++var13;
                        }
                     }
                  } else {
                     int var15 = 0;

                     int var14;
                     for(var14 = 1; var14 < var24 && var8[var13 + var14] == this.wlCookieName.charAt(var14); ++var14) {
                     }

                     if (var14 >= var24 && (char)var8[var13 + var14] == '=') {
                        var13 += var14;
                        boolean var16 = false;
                        if (var14 >= var24) {
                           while(var13 < var11 && var8[var13] == 32) {
                              ++var13;
                           }

                           while(var13 < var11 && var8[var13] == 61) {
                              ++var13;
                           }

                           while(var13 < var11 && var8[var13] == 32) {
                              ++var13;
                           }

                           while(var13 < var11 && var8[var13] == 34) {
                              ++var13;
                           }

                           while(var13 < var11 && var8[var13] != "!".charAt(0)) {
                              ++var13;
                           }

                           var14 = 0;

                           for(var15 = var13 + var14; var15 < var11; ++var15) {
                              byte var17 = var8[var15];
                              if (var17 == 59 || var17 == 34 || var17 == 38) {
                                 var16 = true;
                                 break;
                              }

                              ++var14;
                           }
                        }

                        if (var8[var13] == "!".charAt(0)) {
                           ++var13;
                        }

                        if (var16) {
                           var20 = new String(var8, var13, var15 - var13);
                        } else {
                           var20 = new String(var8, var13, var11 - var13);
                        }

                        if (this.verbose) {
                           this.trace("Found session in the post data: " + var20);
                        }
                     }
                  }
               }
            }
         }
      }

      if (var20 != null) {
         String[] var25 = StringUtils.splitCompletely(new String(var20), "!");
         Server var28;
         switch (var25.length) {
            case 1:
               if (this.useDynamicList) {
                  ServerList var29 = this.initDynamicServerList(this.srvrList);
                  if (var29 != null) {
                     this.srvrList = var29;
                     var1.setServerList(this.srvrList);
                  }
               }

               var28 = new Server(var25[0], (String)null, 0, 0);
               var1.setPrimaryServer(var28);
               return true;
            case 2:
            case 3:
               if (this.srvrList.isStatic()) {
                  ServerList var27 = this.initDynamicServerList(this.srvrList);
                  if (var27 != null) {
                     this.srvrList = var27;
                     var1.setServerList(this.srvrList);
                  }
               }

               var28 = new Server(var25[0], (String)null, 0, 0);
               var1.setPrimaryServer(var28);
               if (var1.getPrimaryServer() != null && !var25[1].regionMatches(true, 0, "NONE", 0, 4) && var25[1].length() <= 11) {
                  var28 = new Server(var25[1], (String)null, 0, 0);
                  var1.setSecondaryServer(var28);
               }

               return true;
            case 4:
            case 5:
            case 6:
               if (var25.length != 4 && !var25[4].regionMatches(true, 0, "NONE", 0, 4)) {
                  if (var25[4].length() > 6) {
                     var28 = new Server(var25[0], var25[1], Integer.parseInt(var25[2]), Integer.parseInt(var25[3]));
                     var1.setPrimaryServer(var28);
                     if (var1.getPrimaryServer() != null) {
                        var28 = new Server(var25[4], (String)null, 0, 0);
                        var1.setSecondaryServer(var28);
                     }
                  } else {
                     var28 = new Server(var25[0], (String)null, 0, 0);
                     var1.setPrimaryServer(var28);
                     if (var1.getPrimaryServer() != null) {
                        var28 = new Server(var25[1], var25[2], Integer.parseInt(var25[3]), Integer.parseInt(var25[4]));
                        var1.setSecondaryServer(var28);
                     }
                  }
               } else {
                  var28 = new Server(var25[0], var25[1], Integer.parseInt(var25[2]), Integer.parseInt(var25[3]));
                  var1.setPrimaryServer(var28);
               }

               return true;
            case 7:
            default:
               if (this.verbose) {
                  this.trace("malformed cookie: " + var20);
               }

               return false;
            case 8:
            case 9:
               var28 = new Server(var25[0], var25[1], Integer.parseInt(var25[2]), Integer.parseInt(var25[3]));
               var1.setPrimaryServer(var28);
               var28 = new Server(var25[4], var25[5], Integer.parseInt(var25[6]), Integer.parseInt(var25[7]));
               var1.setSecondaryServer(var28);
               return true;
         }
      } else {
         return false;
      }
   }

   private ServerList initDynamicServerList(ServerList var1) {
      ServerList var2 = null;
      String var3 = null;
      GenericProxyServlet.ProxyConnection var4 = null;
      String var5 = "HEAD /bea_wls_internal/WLDummyInitJVMIDs HTTP/1.0\r\n";
      Server[] var6 = var1.toArray();
      if (var6 == null) {
         return null;
      } else {
         int var7 = var6.length;
         if (var7 < 1) {
            return null;
         } else {
            for(int var8 = 0; var8 < var7; ++var8) {
               if (var6[var8].getJVMID() == null) {
                  long var9 = var6[var8].getStartMarkBad();
                  if (var9 > 0L) {
                     long var11 = System.currentTimeMillis();
                     long var13 = var11 - var9;
                     if (var13 < this.maxSkipTime) {
                        if (this.verbose) {
                           this.trace("Skip retrieving JVMID for " + var6[var8] + ", skipTime=" + var13 / 1000L + ", max=" + this.maxSkipTime / 1000L);
                        }
                        continue;
                     }

                     if (this.verbose) {
                        this.trace("Reset retrieving JVMID for " + var6[var8] + ", mark good after " + this.maxSkipTime / 1000L);
                     }

                     var6[var8].markGood();
                  }

                  if (this.verbose) {
                     this.trace("Trying to get JVMID from server: " + var6[var8]);
                  }

                  try {
                     if (this.isSecureProxy) {
                        var4 = new GenericProxyServlet.ProxyConnection(var6[var8].getHost(), var6[var8].getSecurePort(), true, 10000);
                     } else {
                        var4 = new GenericProxyServlet.ProxyConnection(var6[var8].getHost(), var6[var8].getPort(), false, 10000);
                     }

                     BufferedOutputStream var28 = new BufferedOutputStream(var4.getSocket().getOutputStream());
                     PrintStream var12 = new PrintStream(var28);
                     var12.print(var5);
                     var12.print("X-WebLogic-Request-ClusterInfo: true");
                     var12.print("\r\n");
                     var12.print("X-WebLogic-Force-JVMID: unset");
                     var12.print("\r\n");
                     var12.print("\r\n");
                     var12.flush();
                     DataInputStream var29 = new DataInputStream(new BufferedInputStream(var4.getSocket().getInputStream(), 100));
                     String var14 = var29.readLine();

                     while((var14 = var29.readLine()) != null && var14.length() > 0) {
                        String[] var15 = StringUtils.split(var14, ':');
                        String var16 = var15[0].trim();
                        String var17 = var15[1].trim();
                        if (var16.equals("X-WebLogic-Cluster-List")) {
                           if (this.verbose) {
                              this.trace("Update dynmaic list: " + var17);
                           }

                           var2 = new ServerList(var17, false);
                           if (var3 != null) {
                              if (this.verbose) {
                                 this.trace("Update dynmaic hash: " + var3);
                              }

                              var2.setHash(var3);
                              break;
                           }
                        } else if (var16.equals("X-WebLogic-Cluster-Hash")) {
                           if (var2 != null) {
                              if (this.verbose) {
                                 this.trace("Update dynmaic hash: " + var17);
                              }

                              var2.setHash(var17);
                              break;
                           }

                           var3 = var17;
                        } else if (var16.equals("X-WebLogic-JVMID") && var17 != null && !var17.equals(var6[var8].getJVMID())) {
                           synchronized(var1) {
                              if (!var17.equals(var6[var8].getJVMID())) {
                                 if (this.verbose) {
                                    this.trace("Update jvmid " + var17 + " for " + var6[var8]);
                                 }

                                 var1.remove(var6[var8]);
                                 var6[var8].setJVMID(var17);
                                 var1.add(var6[var8]);
                              }
                           }
                        }
                     }

                     if (var2 != null) {
                        break;
                     }
                  } catch (IOException var26) {
                     if (this.verbose) {
                        this.trace("Failed to update jvmid for " + var6[var8]);
                     }
                  } finally {
                     if (var4 != null) {
                        var4.close();
                     }

                  }
               }
            }

            return var2;
         }
      }
   }

   protected boolean readPostData(RequestInfo var1, HttpServletRequest var2) {
      if (!var1.isPost()) {
         return true;
      } else {
         int var3 = var2.getContentLength();
         if (this.maxPostSize > 0 && var3 > this.maxPostSize) {
            if (this.verbose) {
               this.trace("Content Length exceeded the MaxPostSize: " + this.maxPostSize);
            }

            return false;
         } else if (var3 > MAX_POST_IN_MEMORY) {
            return this.readPostDataToFile(var1, var2, var3);
         } else if (var3 >= 0) {
            return this.readPostDataToMemory(var1, var2, var3);
         } else {
            String var4 = var2.getHeader("Transfer-Encoding");
            if (var4 == null) {
               return true;
            } else if (var4.equalsIgnoreCase("Chunked")) {
               var1.setIsChunked(true);
               return this.readChunkedPostData(var1, var2);
            } else {
               if (this.verbose) {
                  this.trace("Transfer-Encoding not set or encountered an unsupported value " + var4);
               }

               return false;
            }
         }
      }
   }

   protected boolean readChunkedPostData(RequestInfo var1, HttpServletRequest var2) {
      if (this.fileCaching) {
         File var3 = null;
         FileOutputStream var4 = null;
         Chunk var5 = null;
         boolean var6 = true;

         boolean var7;
         try {
            var3 = File.createTempFile("proxy", (String)null, (File)null);
            if (var3 != null) {
               var4 = new FileOutputStream(var3);
               ServletInputStream var26 = var2.getInputStream();
               var5 = Chunk.getChunk();
               int var28 = 0;
               int var9 = 0;
               boolean var10 = true;

               boolean var11;
               int var29;
               while((var29 = var26.read()) != -1) {
                  var5.buf[var5.end++] = (byte)var29;
                  if (var9 == MAX_POST_IN_MEMORY - 1) {
                     var4.write(var5.buf, 0, MAX_POST_IN_MEMORY);
                     var9 = -1;
                     var5.end = 0;
                  }

                  ++var9;
                  ++var28;
                  if (this.maxPostSize > 0 && var28 > this.maxPostSize) {
                     if (this.verbose) {
                        this.trace("Chunked post data exceeded MaxPostSize: " + this.maxPostSize);
                     }

                     try {
                        if (var4 != null) {
                           var4.close();
                        }

                        if (var3 != null) {
                           var3.delete();
                        }
                     } catch (Exception var21) {
                     }

                     var11 = false;
                     return var11;
                  }
               }

               if (var28 > MAX_POST_IN_MEMORY) {
                  if (var9 > 0) {
                     var4.write(var5.buf, 0, var5.end);
                  }

                  var4.flush();
                  var4.close();
                  var1.setPostDataFile(var3);
               } else {
                  if (var28 <= 0) {
                     if (this.verbose) {
                        this.trace("Failed to read chunked post data");
                     }

                     try {
                        if (var4 != null) {
                           var4.close();
                        }

                        if (var3 != null) {
                           var3.delete();
                        }
                     } catch (Exception var23) {
                     }

                     var11 = false;
                     return var11;
                  }

                  var6 = false;
                  var1.setPostData(var5);

                  try {
                     if (var4 != null) {
                        var4.close();
                     }

                     if (var3 != null) {
                        var3.delete();
                     }
                  } catch (Exception var22) {
                  }
               }

               var1.setContentLen(var28);
               return true;
            }

            var7 = false;
         } catch (IOException var24) {
            if (this.verbose) {
               CharArrayWriter var8 = new CharArrayWriter();
               var24.printStackTrace(new PrintWriter(var8));
               this.trace("Failed to read chunked post data: " + var8.toString());
            }

            try {
               if (var4 != null) {
                  var4.close();
               }

               if (var3 != null) {
                  var3.delete();
               }
            } catch (Exception var20) {
            }

            boolean var27;
            if (this.proxyForConnectionResets) {
               if (this.verbose) {
                  this.trace("ProxyForConnectionResets enabled");
               }

               var27 = true;
               return var27;
            }

            var27 = false;
            return var27;
         } finally {
            if (var6 && var5 != null) {
               Chunk.releaseChunk(var5);
            }

         }

         return var7;
      } else {
         return true;
      }
   }

   protected boolean readPostDataToMemory(RequestInfo var1, HttpServletRequest var2, int var3) {
      if (var3 == 0) {
         return true;
      } else {
         try {
            Chunk var4 = this.readPostDataToMemory(var2, var3);
            if (var4 == null) {
               return false;
            } else {
               var1.setPostData(var4);
               return true;
            }
         } catch (IOException var6) {
            if (this.verbose) {
               CharArrayWriter var5 = new CharArrayWriter();
               var6.printStackTrace(new PrintWriter(var5));
               this.trace("Failed to read post data in memory: " + var5.toString());
            }

            if (this.proxyForConnectionResets) {
               if (this.verbose) {
                  this.trace("ProxyForConnectionResets enabled");
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   protected boolean readPostDataToFile(RequestInfo var1, HttpServletRequest var2, int var3) {
      if (this.fileCaching) {
         try {
            File var4 = this.readPostDataToFile(var2, var3);
            if (var4 == null) {
               return false;
            }

            var1.setPostDataFile(var4);
         } catch (IOException var6) {
            if (this.verbose) {
               CharArrayWriter var5 = new CharArrayWriter();
               var6.printStackTrace(new PrintWriter(var5));
               this.trace("Failed to read post data into file: " + var5.toString());
            }

            if (this.proxyForConnectionResets) {
               if (this.verbose) {
                  this.trace("ProxyForConnectionResets enabled");
               }

               return true;
            }

            return false;
         }
      }

      return true;
   }

   protected boolean sendPostData(HttpServletRequest var1, RequestInfo var2, PrintStream var3) {
      Chunk var4 = var2.getPostData();
      if (var4 != null) {
         while(true) {
            if (var4 == null) {
               var3.flush();
               break;
            }

            var3.write(var4.buf, 0, var4.end);
            var4 = var4.next;
         }
      } else {
         File var5;
         boolean var7;
         int var27;
         if (!this.fileCaching) {
            if (this.verbose) {
               this.trace("FileCaching is OFF, no failover");
            }

            var2.setCanFailover(false);
            var5 = null;

            try {
               var4 = Chunk.getChunk();
               ServletInputStream var24 = var1.getInputStream();
               int var26;
               if (!var2.isChunked()) {
                  for(var26 = var1.getContentLength(); var26 > 0; var26 -= var27) {
                     var27 = var24.read(var4.buf, 0, var4.buf.length);
                     var3.write(var4.buf, 0, var27);
                     var3.flush();
                  }

                  return true;
               }

               boolean var25 = true;

               while((var26 = var24.read()) != -1) {
                  var4.buf[var4.end++] = (byte)var26;
                  if (var4.end == Chunk.CHUNK_SIZE) {
                     var3.write(var4.buf, 0, var4.end);
                     var3.flush();
                     var4.end = 0;
                  }
               }

               if (var4.end > 0) {
                  var3.write(var4.buf, 0, var4.end);
                  var3.flush();
               }

               return true;
            } catch (IOException var20) {
               if (this.verbose) {
                  this.trace("Error reading Post data from client");
               }

               var7 = false;
            } finally {
               Chunk.releaseChunk(var4);
            }

            return var7;
         } else {
            var5 = var2.getPostDataFile();
            if (var5 != null) {
               try {
                  FileInputStream var6 = new FileInputStream(var5);
                  var4 = Chunk.getChunk();
                  var7 = false;

                  while((var27 = var6.read(var4.buf, 0, var4.buf.length)) != -1) {
                     var3.write(var4.buf, 0, var27);
                     var3.flush();
                  }

                  var6.close();
                  return true;
               } catch (IOException var22) {
                  var2.setCanFailover(false);
                  if (this.verbose) {
                     this.trace("Error reading Post data from tmp file");
                  }

                  var7 = false;
               } finally {
                  Chunk.releaseChunk(var4);
               }

               return var7;
            }
         }
      }

      return true;
   }

   protected GenericProxyServlet.ProxyConnection attemptConnect(RequestInfo var1, Server var2, HttpServletRequest var3) {
      GenericProxyServlet.ProxyConnection var4 = null;
      boolean var5 = var1.isSecureNeeded();

      try {
         var4 = this.connPool.getProxyConnection(var2.getHost(), var5 ? var2.getSecurePort() : var2.getPort(), var5, this.connectTimeoutSecs);
         var4.setTimeout(this.socketTimeout);
      } catch (IOException var10) {
         if (this.verbose) {
            CharArrayWriter var7 = new CharArrayWriter();
            var10.printStackTrace(new PrintWriter(var7));
            this.trace("Caught exception while trying to connect to server " + var2 + ": " + var7.toString());
         }

         var1.markServerBad(var2);
         if (var4 != null) {
            var4.close();
         }

         return null;
      }

      BufferedOutputStream var6 = null;

      CharArrayWriter var9;
      try {
         var6 = new BufferedOutputStream(var4.getSocket().getOutputStream());
      } catch (IOException var13) {
         if (this.verbose) {
            CharArrayWriter var8 = new CharArrayWriter();
            var13.printStackTrace(new PrintWriter(var8));
            this.trace("Caught exception while trying to get an output stream from the connection object: " + var8.toString());
         }

         if (var4.getLastUsed() == 0L) {
            if (this.verbose) {
               this.trace("New connection threw IOException, Mark Server as BAD");
            }

            var1.markServerBad(var2);
            if (var4 != null) {
               var4.close();
            }

            return null;
         }

         if (var4 != null) {
            var4.close();
         }

         if (this.verbose) {
            this.trace("Recycled connection could be BAD, so try creating a new connection to ensure server is down");
         }

         try {
            var4 = this.connPool.getNewProxyConnection(var2.getHost(), var5 ? var2.getSecurePort() : var2.getPort(), var5, this.connectTimeoutSecs);
            var4.setTimeout(this.socketTimeout);
            var6 = new BufferedOutputStream(var4.getSocket().getOutputStream());
         } catch (IOException var12) {
            if (this.verbose) {
               var9 = new CharArrayWriter();
               var12.printStackTrace(new PrintWriter(var9));
               this.trace("Caught exception while trying to connect to server " + var2 + ": " + var9.toString());
            }

            var1.markServerBad(var2);
            if (var4 != null) {
               var4.close();
            }

            return null;
         }
      }

      PrintStream var14 = new PrintStream(var6);
      var14.print(var1.getRequest());
      this.sendRequestHeaders(var3, var14, var1, var2);
      if (!var14.checkError()) {
         if (this.verbose) {
            this.trace("Successfully send request headers to server: " + var2);
         }
      } else {
         if (var4.getLastUsed() == 0L) {
            if (this.verbose) {
               this.trace("New Connection reported error while forwarding request headers to server: " + var2 + "; Mark Server as BAD");
            }

            var1.markServerBad(var2);
            if (var4 != null) {
               var4.close();
            }

            return null;
         }

         if (var4 != null) {
            var4.close();
         }

         if (this.verbose) {
            this.trace("Error while forwarding request headers. Recycled connection could be BAD, so try creating a new connection");
         }

         try {
            var4 = this.connPool.getNewProxyConnection(var2.getHost(), var5 ? var2.getSecurePort() : var2.getPort(), var5, this.connectTimeoutSecs);
            var4.setTimeout(this.socketTimeout);
            var6 = new BufferedOutputStream(var4.getSocket().getOutputStream());
         } catch (IOException var11) {
            if (this.verbose) {
               var9 = new CharArrayWriter();
               var11.printStackTrace(new PrintWriter(var9));
               this.trace("Caught exception while trying to connect to server " + var2 + ": " + var9.toString());
            }

            var1.markServerBad(var2);
            if (var4 != null) {
               var4.close();
            }

            return null;
         }

         var14 = new PrintStream(var6);
         var14.print(var1.getRequest());
         this.sendRequestHeaders(var3, var14, var1, var2);
      }

      if (var1.isPost()) {
         boolean var15 = this.sendPostData(var3, var1, var14);
         if (!var15) {
            if (this.verbose) {
               this.trace("Error while reading post data from client");
            }

            if (var4 != null) {
               var4.close();
            }

            return null;
         }

         if (var14.checkError()) {
            if (this.verbose) {
               this.trace("Error while forwarding post data to server: " + var2);
            }

            var1.markServerBad(var2);
            if (var4 != null) {
               var4.close();
            }

            return null;
         }
      }

      return var4;
   }

   public GenericProxyServlet.ProxyConnection sendResponse(RequestInfo var1, HttpServletRequest var2, HttpServletResponse var3, GenericProxyServlet.ProxyConnection var4, Server var5) {
      try {
         DataInputStream var6 = new DataInputStream(new BufferedInputStream(var4.getSocket().getInputStream(), 100));
         int var14 = this.readStatus(var2, var3, var6, var4.canRecycle());
         if (var14 == 503) {
            if (this.verbose) {
               this.trace("Got 503 from server: " + var5);
            }

            var1.markServerBad(var5);
            if (var4 != null) {
               var4.close();
            }

            return null;
         } else {
            var1.setCanFailover(false);
            int var8 = this.sendResponseHeaders(var3, var6, var4, var1);
            if (var14 == 100) {
               var14 = this.readStatus(var2, var3, var6);
               var8 = this.sendResponseHeaders(var3, var6, var4, var1);
            }

            if (var14 != 204 && var14 != 304 && !"HEAD".equalsIgnoreCase(var2.getMethod())) {
               ServletOutputStream var9 = null;

               try {
                  var9 = var3.getOutputStream();
               } catch (IOException var11) {
                  if (var4 != null) {
                     var4.close();
                  }

                  return null;
               }

               if (var8 == -9999) {
                  ChunkInput.readCTE((OutputStream)var9, var6);
               } else if (var8 != 0) {
                  this.readAndWriteResponseData(var6, var9, var8);
               }

               return var4;
            } else {
               return var4;
            }
         }
      } catch (WriteClientIOException var12) {
         if (var4 != null) {
            var4.close();
         }

         return null;
      } catch (IOException var13) {
         if (this.verbose) {
            CharArrayWriter var7 = new CharArrayWriter();
            var13.printStackTrace(new PrintWriter(var7));
            this.trace("Caught exception while reading response status : " + var7.toString());
         }

         var1.markServerBad(var5);
         if (var4 != null) {
            var4.close();
         }

         return null;
      }
   }

   protected static Map createNewMap() {
      return Collections.synchronizedMap(new TreeMap(new Comparator() {
         public int compare(Object var1, Object var2) {
            Server var3 = (Server)var1;
            Server var4 = (Server)var2;
            return var3.compareTo(var4);
         }
      }));
   }

   public Server createServer(String var1, boolean var2) {
      return new Server(var1, var2);
   }

   public Server createServer(String var1, String var2, int var3, int var4) {
      return new Server(var1, var2, var3, var4);
   }

   static {
      MAX_POST_IN_MEMORY = Chunk.CHUNK_SIZE;
   }

   public class ServerList {
      private String hash;
      private int index;
      private boolean isStaticList;
      private TreeMap list = new TreeMap(new Comparator() {
         public int compare(Object var1, Object var2) {
            Server var3 = (Server)var1;
            Server var4 = (Server)var2;
            return var3.compareTo(var4);
         }
      });

      public ServerList(boolean var2) {
         this.isStaticList = var2;
         this.index = -1;
      }

      public ServerList(String var2, boolean var3) {
         this.isStaticList = var3;
         this.index = -1;
         StringTokenizer var4 = new StringTokenizer(var2, "|");

         while(var4.hasMoreTokens()) {
            String var5 = var4.nextToken();
            Server var6 = HttpClusterServlet.this.new Server(var5, this.isStaticList);
            this.list.put(var6, var6);
         }

      }

      public Server getServer(Server var1) {
         return (Server)this.list.get(var1);
      }

      public Server[] toArray() {
         Set var1 = this.list.keySet();
         if (var1 != null && !var1.isEmpty()) {
            int var2 = this.size();
            if (var2 <= 0) {
               return null;
            } else {
               Iterator var3 = var1.iterator();
               Server[] var4 = new Server[var2];

               for(int var5 = 0; var3.hasNext(); ++var5) {
                  var4[var5] = (Server)var3.next();
               }

               return var4;
            }
         } else {
            return null;
         }
      }

      public Iterator iterator() {
         return this.list.values().iterator();
      }

      public boolean isStatic() {
         return this.isStaticList;
      }

      public String getHash() {
         return this.hash;
      }

      public void setHash(String var1) {
         this.hash = var1;
      }

      public int size() {
         return this.list.size();
      }

      public void add(Server var1) {
         this.list.put(var1, var1);
      }

      public void remove(Server var1) {
         this.list.remove(var1);
      }

      public synchronized Server next() {
         if (this.list.size() == 0) {
            return null;
         } else {
            if (this.index == -1) {
               this.index = (int)(Math.random() * (double)this.list.size());
            } else {
               this.index = ++this.index % this.list.size();
            }

            Object[] var1 = this.list.values().toArray();
            return (Server)var1[this.index];
         }
      }

      public void addToKnownServersList(Map var1) {
         Object[] var2 = this.list.values().toArray();

         for(int var3 = 0; var3 < this.list.size(); ++var3) {
            Object var4 = var2[var3];
            if (!var1.containsKey(var4)) {
               var1.put(var4, var4);
            }
         }

      }

      public Server getByHostAndPort(String var1, int var2) {
         Iterator var3 = this.list.values().iterator();

         Server var5;
         do {
            do {
               if (!var3.hasNext()) {
                  return null;
               }

               Object var4 = var3.next();
               var5 = (Server)var4;
            } while(!var5.getHost().equals(var1) && !var5.getHostIp().equals(var1));
         } while(var5.getPort() != var2);

         return var5;
      }
   }

   public class Server {
      private String jvmid;
      private String host;
      private String hostip;
      private int port;
      private int sslPort;
      private boolean isBad = false;
      private long startMarkBad = 0L;

      public Server(String var2, boolean var3) {
         if (var3) {
            int var4;
            if (var2.indexOf("[") != -1) {
               var4 = var2.indexOf("[");
               int var5 = var2.indexOf("]");
               this.setHost(var2.substring(var4 + 1, var5));
               String var6 = var2.substring(var5 + 1);
               this.parsePorts(var6);
            } else {
               var4 = var2.indexOf(58);
               if (var4 == -1) {
                  this.setHost(var2);
                  this.port = 80;
                  this.sslPort = 443;
               } else {
                  this.setHost(var2.substring(0, var4));
                  this.parsePorts(var2.substring(var4));
               }
            }
         } else {
            String[] var7 = StringUtils.splitCompletely(var2, "!");
            if (var7.length == 4) {
               this.jvmid = var7[0];
               this.setHost(var7[1]);
               this.port = Integer.parseInt(var7[2]);
               this.sslPort = Integer.parseInt(var7[3]);
            }
         }

      }

      public Server(String var2, String var3, int var4, int var5) {
         this.jvmid = var2;
         this.setHost(var3);
         this.port = var4;
         this.sslPort = var5;
      }

      public void markGood() {
         this.startMarkBad = 0L;
         this.isBad = false;
      }

      public void markBad() {
         this.startMarkBad = System.currentTimeMillis();
         this.isBad = true;
      }

      public boolean isBad() {
         if (!this.isBad) {
            return false;
         } else {
            if (HttpClusterServlet.this.maxSkipTime > 0L) {
               long var1 = System.currentTimeMillis();
               if (var1 - this.startMarkBad >= HttpClusterServlet.this.maxSkipTime) {
                  this.startMarkBad = 0L;
                  this.isBad = false;
               }
            }

            return this.isBad;
         }
      }

      public long getStartMarkBad() {
         return this.startMarkBad;
      }

      public String getJVMID() {
         return this.jvmid;
      }

      public void setJVMID(String var1) {
         this.jvmid = var1;
      }

      public void setHost(String var1) {
         if (var1 != null) {
            try {
               int var2 = Integer.valueOf(var1);
               this.hostip = (var2 >> 24 & 255) + "." + (var2 >> 16 & 255) + "." + (var2 >> 8 & 255) + "." + (var2 >> 0 & 255);
               this.host = this.hostip;
            } catch (NumberFormatException var5) {
               this.host = var1;

               try {
                  this.hostip = InetAddress.getByName(var1).getHostAddress();
               } catch (UnknownHostException var4) {
               }
            }

         }
      }

      public String getHost() {
         return this.host;
      }

      public String getHostIp() {
         return this.hostip;
      }

      public int getPort() {
         return this.port;
      }

      public int getSecurePort() {
         return this.sslPort;
      }

      public int compareTo(Server var1) {
         String var2 = var1.getHostIp();
         String var3 = var1.getJVMID();
         if (this.jvmid != null && var3 != null) {
            return this.jvmid.compareToIgnoreCase(var3);
         } else if (var2 != null && this.hostip != null) {
            int var4 = this.hostip.compareTo(var2);
            if (var4 == 0) {
               var4 = this.port - var1.getPort();
               return var4 == 0 ? this.sslPort - var1.getSecurePort() : var4;
            } else {
               return var4;
            }
         } else {
            return -1;
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(100);
         if (this.jvmid != null) {
            var1.append(this.jvmid);
         }

         if (this.host != null) {
            if (this.jvmid != null) {
               var1.append("!");
            }

            var1.append(this.host);
            var1.append("!");
            var1.append(this.port);
            var1.append("!");
            var1.append(this.sslPort);
         }

         return var1.toString();
      }

      private void parsePorts(String var1) {
         int var2 = var1.indexOf(":");
         int var3 = var1.lastIndexOf(":");
         if (var2 == -1) {
            this.port = 80;
            this.sslPort = 443;
         } else if (var3 != -1 && var2 != var3) {
            this.port = Integer.parseInt(var1.substring(var2 + 1, var3));
            this.sslPort = Integer.parseInt(var1.substring(var3 + 1));
         } else if (HttpClusterServlet.this.isSecureProxy) {
            this.port = 80;
            this.sslPort = Integer.parseInt(var1.substring(var2 + 1));
         } else {
            this.port = Integer.parseInt(var1.substring(var2 + 1));
            this.sslPort = 443;
         }

      }
   }

   public class RequestInfo {
      private ServerList list;
      private boolean isSecure = false;
      private File postDataFile = null;
      private Server primary;
      private Server secondary;
      private String request = null;
      private Chunk postData = null;
      private boolean post = false;
      private boolean canFailover = true;
      private boolean verbose;
      private boolean needToUpdate = true;
      private String dynamicHash = null;
      private String dynamicList = null;
      private boolean chunked = false;
      private int contentLen = -1;
      private String serverJVMID = null;
      private Map<RoutingHandler, Object> notificationData = null;

      public void setServerList(ServerList var1) {
         this.list = var1;
      }

      public ServerList getServerList() {
         return this.list;
      }

      public Server getServerInList() {
         for(int var1 = 0; var1 < this.list.size(); ++var1) {
            Server var2 = this.list.next();
            if (!var2.isBad()) {
               return var2;
            }
         }

         return null;
      }

      public void revertToAllKnownServersList() {
         this.list = HttpClusterServlet.this.new ServerList(true);
         Iterator var1 = HttpClusterServlet.this.allKnownServers.values().iterator();

         while(var1.hasNext()) {
            Server var2 = (Server)var1.next();
            var2.markGood();
            this.list.add(var2);
         }

      }

      public void markServerBad(Server var1) {
         if (this.primary != null && var1.equals(this.primary)) {
            this.primary.markBad();
         }

         if (this.secondary != null && var1.equals(this.secondary)) {
            this.secondary.markBad();
         }

         var1.markBad();
         if (this.verbose) {
            HttpClusterServlet.this.trace("marked bad: " + var1);
         }

      }

      public boolean isSecureNeeded() {
         return this.isSecure;
      }

      public void setIsSecureNeeded(boolean var1) {
         this.isSecure = var1;
      }

      public File getPostDataFile() {
         return this.postDataFile;
      }

      public void setPostDataFile(File var1) {
         this.postDataFile = var1;
      }

      public void deletePostDataFile() {
         if (this.postDataFile != null) {
            if (this.verbose) {
               HttpClusterServlet.this.trace("Remove temp file: " + this.postDataFile.getAbsolutePath());
            }

            this.postDataFile.delete();
         }

      }

      public Server getPrimaryServer() {
         return this.primary;
      }

      public void setPrimaryServer(Server var1) {
         this.primary = this.list.getServer(var1);
         if (this.primary == null && HttpClusterServlet.this.crossOverProxyEnabled) {
            this.primary = var1;
            this.needToUpdate = false;
         }

      }

      public Server getSecondaryServer() {
         return this.secondary;
      }

      public void setSecondaryServer(Server var1) {
         this.secondary = this.list.getServer(var1);
         if (this.secondary == null && HttpClusterServlet.this.crossOverProxyEnabled) {
            this.secondary = var1;
            this.needToUpdate = false;
         }

      }

      public String getRequest() {
         return this.request;
      }

      public void setRequest(String var1) {
         this.request = var1;
      }

      public Chunk getPostData() {
         return this.postData;
      }

      public void setPostData(Chunk var1) {
         this.postData = var1;
      }

      public void releasePostData() {
         Chunk.releaseChunks(this.postData);
      }

      public boolean isPost() {
         return this.post;
      }

      public void setPost(boolean var1) {
         this.post = var1;
      }

      public void setCanFailover(boolean var1) {
         this.canFailover = var1;
      }

      public boolean canFailover() {
         return this.canFailover;
      }

      public void setVerbose(boolean var1) {
         this.verbose = var1;
      }

      public void setNeedToUpdateDynamicList(boolean var1) {
         this.needToUpdate = var1;
      }

      public boolean needToUpdateDynamicList() {
         return this.needToUpdate;
      }

      public void setDynamicHash(String var1) {
         this.dynamicHash = var1;
      }

      public String getDynamicHash() {
         return this.dynamicHash;
      }

      public void setDynamicList(String var1) {
         this.dynamicList = var1;
      }

      public String getDynamicList() {
         return this.dynamicList;
      }

      public void setIsChunked(boolean var1) {
         this.chunked = var1;
      }

      public boolean isChunked() {
         return this.chunked;
      }

      public void setContentLen(int var1) {
         this.contentLen = var1;
      }

      public int getContentLen() {
         return this.contentLen;
      }

      public String getServerJVMID() {
         return this.serverJVMID;
      }

      public void setServerJVMID(String var1) {
         this.serverJVMID = var1;
      }

      public Map<RoutingHandler, Object> getNotificationData() {
         return this.notificationData;
      }

      public void setNotificationData(Map<RoutingHandler, Object> var1) {
         this.notificationData = var1;
      }

      public InputStream getInputStream() throws IOException {
         if (this.postData != null) {
            return new ChunkedInputStream(this.postData, 0);
         } else {
            return (InputStream)(this.postDataFile != null ? new BufferedInputStream(new FileInputStream(this.postDataFile)) : new NullInputStream());
         }
      }
   }

   public interface ServerListListener {
      void serverListChanged();
   }
}
