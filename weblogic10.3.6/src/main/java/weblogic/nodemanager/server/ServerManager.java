package weblogic.nodemanager.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import weblogic.nodemanager.common.ConfigException;
import weblogic.nodemanager.common.ServerType;
import weblogic.nodemanager.common.StartupConfig;
import weblogic.nodemanager.system.NodeManagerSystem;
import weblogic.nodemanager.util.ConcurrentFile;

public class ServerManager extends AbstractServerManager {
   private final NMServer nmServer;
   private static final String RES1 = "bea_wls_management_internal2";
   private static final String RES2 = "bea_wls_management_internal2/wl_management";

   public ServerManager(DomainManager var1, String var2) throws ConfigException, IOException {
      super(var1, var2, ServerType.WebLogic);
      this.nmServer = var1.getNMServer();
   }

   protected ServerMonitorI createServerMonitor(StartupConfig var1) {
      return new ServerMonitor(this, var1);
   }

   protected boolean canPingServer(ServerDir var1) {
      ConcurrentFile var2 = var1.getURLFile();
      if (!var2.exists()) {
         this.log(Level.FINEST, "No URL File exists");
         return false;
      } else {
         String var3 = null;

         try {
            String var4 = var2.readLine();
            if (var4 == null) {
               return false;
            } else {
               if (var4.endsWith("/")) {
                  var3 = var4 + "bea_wls_management_internal2/wl_management";
               } else {
                  var3 = var4 + "/" + "bea_wls_management_internal2/wl_management";
               }

               URL var5 = new URL(var3);
               HttpURLConnection var6 = (HttpURLConnection)var5.openConnection();
               var6.setUseCaches(false);
               var6.connect();
               int var7 = var6.getResponseCode();
               var6.disconnect();
               this.log(Level.FINEST, "The wls URL was available :" + var3 + ", responded with code:" + var7);
               return true;
            }
         } catch (MalformedURLException var8) {
            this.log(Level.FINEST, "The wls URL was unavailable :" + var3 + " with " + var8);
            return false;
         } catch (IOException var9) {
            this.log(Level.FINEST, "The wls URL was unavailable :" + var3 + " with " + var9);
            return false;
         }
      }
   }

   public boolean isAdminServer() throws ConfigException, IOException {
      StartupConfig var1 = this.loadStartupConfig();
      return var1.getAdminURL() == null;
   }

   protected boolean isCrashRecoveryNeeded(StartupConfig var1) throws IOException {
      if (this.domainMgr.getNMServer().getConfig().isDomainsDirRemoteSharingEnabled() && var1.getNMHostName() != null && !this.isStartedByNMConfigured(var1)) {
         this.domainDirShared = true;
         return false;
      } else if (var1.getAdminURL() == null) {
         this.log(Level.INFO, nmText.getRecoveringServerProcess());
         this.startServer();
         synchronized(this.monitor) {
            while(!this.monitor.isStarted() && !this.monitor.isFinished()) {
               try {
                  this.monitor.wait();
               } catch (InterruptedException var5) {
                  throw (IOException)(new IOException(nmText.getServerFailedToStart())).initCause(var5);
               }
            }

            if (this.monitor.isStarted() && !this.monitor.isStartupAborted()) {
               return false;
            } else {
               throw new IOException(nmText.getServerFailedToStart());
            }
         }
      } else {
         return true;
      }
   }

   protected List<WLSProcess.ExecuteCallbackHook> getStartCallbacks(StartupConfig var1) throws IOException {
      ArrayList var2 = new ArrayList();
      if (this.isIPForBinding(var1)) {
         Iterator var3 = var1.getServerIPList().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            NetworkInfo var5 = this.nmServer.getConfig().getNetworkInfoFor(var4);
            var2.add(NodeManagerSystem.getInstance().createBindIPHook(this.serverName, var4, var5.getInterfaceName(), var5.getNetMask(), this.domainMgr.getDomainDir().getServerDir(this.serverName).getPath(), this.getIfConfigScriptDir(), this.nmServer.getConfig().useMACBroadcast()));
         }
      }

      return var2;
   }

   protected List<WLSProcess.ExecuteCallbackHook> getStopCallbacks(StartupConfig var1) throws IOException {
      ArrayList var2 = new ArrayList();
      if (this.isIPForBinding(var1)) {
         Iterator var3 = var1.getServerIPList().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            NetworkInfo var5 = this.nmServer.getConfig().getNetworkInfoFor(var4);
            var2.add(NodeManagerSystem.getInstance().createUnbindIPHook(this.serverName, var4, var5.getInterfaceName(), this.domainMgr.getDomainDir().getServerDir(this.serverName).getPath(), this.getIfConfigScriptDir()));
         }
      }

      return var2;
   }

   protected StartupConfig createStartupConfig(Properties var1) throws ConfigException {
      return new StartupConfig(var1);
   }

   private String getIfConfigScriptDir() {
      String var1 = this.nmServer.getConfig().getIfConfigDir();
      return var1 != null ? var1 : this.domainMgr.getDomainDir().getIfConfigDir();
   }

   private boolean isIPForBinding(StartupConfig var1) {
      if (var1 == null) {
         this.log(Level.WARNING, "The server manager for " + this.serverName + " is not initialized");
         return false;
      } else {
         return var1.getServerIPList() != null && !var1.getServerIPList().isEmpty();
      }
   }

   private boolean isStartedByNMConfigured(StartupConfig var1) {
      return this.getNMHostName().equals(var1.getNMHostName());
   }
}
