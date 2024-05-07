package weblogic.nodemanager.system;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import weblogic.nodemanager.NMException;
import weblogic.nodemanager.plugin.NMPlugin;
import weblogic.nodemanager.plugin.NMServerI;
import weblogic.nodemanager.server.NMHelper;
import weblogic.nodemanager.server.NMServer;
import weblogic.nodemanager.server.WLSProcess;
import weblogic.nodemanager.server.WLSProcessImpl;

public class DefaultNMPlugin implements NMPlugin {
   private NMServer server = null;
   private Logger logger = null;
   private static final long IFCONFIG_TIMEOUT = 0L;

   public DefaultNMPlugin(NMPlugin var1) {
      NodeManagerSystem.getInstance().initialize(var1);
   }

   protected DefaultNMPlugin(NMServer var1) {
      this.server = var1;
   }

   public WLSProcess createProcess(String[] var1, Map var2, File var3, File var4) throws NMException {
      if (var1 == null) {
         throw new NMException("Command Line provided is null");
      } else {
         WLSProcessImpl var5 = new WLSProcessImpl(var1, var2, var3, var4);
         return var5;
      }
   }

   public NMServerI getNodeManagerServer() throws NMException {
      if (this.server == null) {
         throw new NMException("NodeManager Server is not initiated");
      } else {
         return this.server;
      }
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public Logger getLogger() {
      if (this.logger == null) {
         this.setLogger(NMServer.nmLog);
      }

      return this.logger;
   }

   public WLSProcess.ExecuteCallbackHook createBindIPHook(String var1, String var2, String var3, String var4, String var5, String var6, boolean var7) throws NMException {
      final File var8 = new File(var6);
      if (!(new File(var8, NMHelper.getIFControlScriptName())).exists()) {
         throw new NMException("wlsifconfig script does not exist at " + var8.getPath());
      } else {
         final String[] var9 = NMHelper.buildAddMigrationCommand(var2, var3, var4, var6);
         final Properties var10 = NMHelper.buildMigrationEnv(var1, var5, var7);
         WLSProcess.ExecuteCallbackHook var11 = new WLSProcess.ExecuteCallbackHook() {
            public void execute() throws IOException {
               int var1 = NodeManagerSystem.getInstance().executeScript(var9, var10, var8, 0L);
               if (var1 != 0) {
                  StringBuffer var2 = new StringBuffer();

                  for(int var3 = 0; var3 < var9.length; ++var3) {
                     var2.append(var9[var3]);
                     var2.append(" ");
                  }

                  throw new IOException("Command '" + var2 + "' returned an unsuccessful exit code '" + var1 + "'. Check NM logs for script output.");
               }
            }
         };
         return var11;
      }
   }

   public WLSProcess.ExecuteCallbackHook createUnbindIPHook(String var1, String var2, String var3, String var4, String var5) throws NMException {
      final File var6 = new File(var5);
      if (!(new File(var6, NMHelper.getIFControlScriptName())).exists()) {
         throw new NMException("wlsifconfig script does not exist at " + var6.getPath());
      } else {
         final String[] var7 = NMHelper.buildRemoveMigrationCommand(var2, var3, var5);
         final Properties var8 = NMHelper.buildMigrationEnv(var1, var4);
         WLSProcess.ExecuteCallbackHook var9 = new WLSProcess.ExecuteCallbackHook() {
            public void execute() throws IOException {
               int var1 = NodeManagerSystem.getInstance().executeScript(var7, var8, var6, 0L);
               if (var1 != 0) {
                  StringBuffer var2 = new StringBuffer();

                  for(int var3 = 0; var3 < var7.length; ++var3) {
                     var2.append(var7[var3]);
                     var2.append(" ");
                  }

                  throw new IOException("Command '" + var2 + "' returned an unsuccessful exit code '" + var1 + "'. Check NM logs for script output.");
               }
            }
         };
         return var9;
      }
   }
}
