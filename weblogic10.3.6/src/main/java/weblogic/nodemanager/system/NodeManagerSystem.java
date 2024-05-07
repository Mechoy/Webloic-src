package weblogic.nodemanager.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import weblogic.nodemanager.NMException;
import weblogic.nodemanager.plugin.NMPlugin;
import weblogic.nodemanager.plugin.NMServerI;
import weblogic.nodemanager.server.NMHelper;
import weblogic.nodemanager.server.NMServer;
import weblogic.nodemanager.server.WLSProcess;

public final class NodeManagerSystem {
   private static NodeManagerSystem singleton = null;
   private NMPlugin nmPlugin;

   private NodeManagerSystem() {
      this.nmPlugin = null;
   }

   public static NodeManagerSystem getInstance() {
      if (singleton == null) {
         singleton = NodeManagerSystem.SingletonMaker.singleton;
      }

      return singleton;
   }

   public void initialize(NMServer var1) {
      if (var1 == null) {
         throw new NullPointerException("NMServer is null");
      } else {
         this.nmPlugin = new DefaultNMPlugin(var1);
      }
   }

   public void initialize(NMPlugin var1) {
      if (var1 == null) {
         throw new NullPointerException("Plugin is null");
      } else {
         this.nmPlugin = var1;
      }
   }

   public NMServerI getNodeManagerServer() throws NMException {
      this.checkForInitialized();
      return this.nmPlugin.getNodeManagerServer();
   }

   public Thread initializeAndStartMonitor(WLSProcess var1) throws NMException {
      NMServerI var2 = this.getNodeManagerServer();
      return var2.initializeAndStartServerMonitor(var1);
   }

   public WLSProcess createProcess(String[] var1, Map var2, File var3, File var4) throws NMException {
      this.checkForInitialized();
      return this.nmPlugin.createProcess(var1, var2, var3, var4);
   }

   public List getAdditionalProcessArgs() {
      ArrayList var1 = new ArrayList();
      var1.add("-Dweblogic.nodemanager.ServiceEnabled=true");
      return var1;
   }

   public Logger getLogger() {
      return this.nmPlugin == null ? NMServer.nmLog : this.nmPlugin.getLogger();
   }

   public WLSProcess.ExecuteCallbackHook createBindIPHook(String var1, String var2, String var3, String var4, String var5, String var6, boolean var7) throws NMException {
      this.checkForInitialized();
      return this.nmPlugin.createBindIPHook(var1, var2, var3, var4, var5, var6, var7);
   }

   public WLSProcess.ExecuteCallbackHook createUnbindIPHook(String var1, String var2, String var3, String var4, String var5) throws NMException {
      this.checkForInitialized();
      return this.nmPlugin.createUnbindIPHook(var1, var2, var3, var4, var5);
   }

   public synchronized int executeScript(String[] var1, Properties var2, File var3, long var4) throws IOException {
      return NMHelper.executeScript(var1, var2, var3, var4);
   }

   private void checkForInitialized() throws NMException {
      if (this.nmPlugin == null) {
         throw new NMException("NodeManager System not initialized");
      }
   }

   // $FF: synthetic method
   NodeManagerSystem(Object var1) {
      this();
   }

   private static class SingletonMaker {
      public static final NodeManagerSystem singleton = new NodeManagerSystem();
   }
}
