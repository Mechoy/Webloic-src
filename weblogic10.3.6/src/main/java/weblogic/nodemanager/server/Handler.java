package weblogic.nodemanager.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.Command;
import weblogic.nodemanager.common.ConfigException;
import weblogic.nodemanager.common.DataFormat;

class Handler implements Runnable {
   private NMServer nmServer;
   private Socket sock;
   private BufferedReader in;
   private BufferedWriter out;
   private DomainManager domainMgr;
   private ServerManager serverMgr;
   private CoherenceServerManager coherenceServerMgr;
   private byte[] nmUser;
   private boolean authorized;
   static final String ENCODING = "UTF-8";
   private static final Logger nmLog = Logger.getLogger("weblogic.nodemanager");
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   Handler(NMServer var1, Socket var2) throws IOException {
      this.nmServer = var1;
      this.sock = var2;
      this.in = new BufferedReader(new InputStreamReader(var2.getInputStream()));
      this.out = new BufferedWriter(new OutputStreamWriter(var2.getOutputStream()));
      this.authorized = !var1.getConfig().isAuthenticationEnabled();
   }

   public void run() {
      if (this.isDebugEnabled()) {
         this.debugSay("Handling new connection from " + this.sock.getInetAddress());
      }

      try {
         String var1;
         try {
            while((var1 = this.in.readLine()) != null && this.handleCommand(var1)) {
            }
         } catch (Throwable var11) {
            nmLog.log(Level.WARNING, nmText.getUncaughtHandlerException() + var11, var11);
         }
      } finally {
         try {
            this.sock.close();
            if (this.isDebugEnabled()) {
               this.debugSay("Closed connection from " + this.sock.getInetAddress());
            }
         } catch (IOException var10) {
            nmLog.log(Level.WARNING, nmText.getErrorClosingSocket(), var10);
         }

      }

   }

   private boolean handleCommand(String var1) throws IOException {
      int var2 = var1.indexOf(32);
      String var3 = var2 != -1 ? var1.substring(0, var2) : var1;

      Command var4;
      try {
         var4 = Command.parse(var3);
      } catch (IllegalArgumentException var6) {
         var4 = null;
      }

      if (var4 == null) {
         this.sendERR(nmText.getInvalidCommand(var3));
         return true;
      } else {
         if (var4 == Command.VERSION) {
            this.handleVersion();
         } else if (var4 == Command.DOMAIN) {
            this.handleDomain(var1);
         } else if (var4 == Command.SERVER) {
            this.handleServer(var1);
         } else if (var4 == Command.COHERENCESERVER) {
            this.handleCoherenceServer(var1);
         } else if (var4 == Command.USER) {
            this.handleUser(var1);
         } else if (var4 == Command.PASS) {
            this.handlePass(var1);
         } else if (var4 == Command.START) {
            this.handleStart(false);
         } else if (var4 == Command.STARTP) {
            this.handleStart(true);
         } else if (var4 == Command.KILL) {
            this.handleKill();
         } else if (var4 == Command.STAT) {
            this.handleStat();
         } else if (var4 == Command.GETLOG) {
            this.handleGetLog();
         } else if (var4 == Command.GETNMLOG) {
            this.handleGetNMLog();
         } else if (var4 == Command.GETSTATES) {
            this.handleGetStates();
         } else if (var4 == Command.EXECSCRIPT) {
            this.handleExecScript(var1);
         } else if (var4 == Command.HELLO) {
            this.sendGreeting();
         } else if (var4 == Command.QUIT) {
            this.handleQuit(var4.toString());
         } else if (var4 == Command.CHGCRED) {
            this.handleChgCred(var1);
         } else if (var4 == Command.UPDATEPROPS) {
            this.handleUpdateProps();
         } else {
            this.sendERR(nmText.getInvalidCommand(var4.toString()));
         }

         return true;
      }
   }

   private void handleGetStates() throws IOException {
      if (this.checkDomain() && this.checkAuthorized()) {
         Map var1 = this.domainMgr.getAllStates();
         StringBuffer var2 = new StringBuffer();
         Set var3 = var1.keySet();
         Iterator var4 = var3.iterator();

         String var5;
         while(var4.hasNext()) {
            var5 = (String)var4.next();
            String var6 = (String)var1.get(var5);
            var6 = var6 != null ? var6 : "UNKNOWN";
            var2.append(var5);
            var2.append('=');
            var2.append(var6);
            var2.append(' ');
         }

         var5 = var2.toString();
         this.sendOK(var5);
         if (this.isDebugEnabled()) {
            this.debugSay("Sent statuses: [" + var5 + "]");
         }

      }
   }

   private void handleVersion() throws IOException {
      if (this.checkDomain() && this.checkAuthorized()) {
         this.sendOK("10.3");
      }
   }

   private void handleDomain(String var1) throws IOException {
      String var2 = null;
      String var3 = null;
      int var4 = var1.indexOf(32);
      if (var4++ != -1 && var4 < var1.length()) {
         int var5 = var1.indexOf(32, var4);
         if (var5 == -1) {
            var2 = var1.substring(var4);
         } else if (var5 < var1.length() - 1) {
            var2 = var1.substring(var4, var5);
            var3 = var1.substring(var5 + 1);
         }
      }

      if (var2 == null) {
         this.sendERR(nmText.getInvalidCommandSyntax(Command.DOMAIN.toString()));
      } else {
         String var9 = null;

         try {
            this.domainMgr = this.nmServer.getDomainManager(var2, var3);
         } catch (ConfigException var7) {
            var9 = nmText.getDomainError();
            nmLog.log(Level.WARNING, var9, var7);
         } catch (IOException var8) {
            var9 = nmText.getDomainIOError();
            nmLog.log(Level.WARNING, var9, var8);
         }

         if (var9 != null) {
            this.sendERR(var9);
         } else {
            this.sendOK(nmText.getSetDomainMsg(var2));
         }
      }
   }

   private void handleQuit(String var1) throws IOException {
      if (this.checkDomain() && this.checkAuthorized()) {
         if (this.nmServer.getConfig().getQuitEnabled()) {
            this.sendOK(nmText.getQuitMsg());
            System.exit(0);
         } else {
            this.sendERR(nmText.getDisabledCommand(var1));
         }

      }
   }

   private void handleChgCred(String var1) throws IOException {
      if (this.checkDomain() && this.checkAuthorized()) {
         String var2 = null;
         String var3 = null;
         int var4 = var1.indexOf(32);
         boolean var5 = true;
         if (var4++ != -1 && var4 < var1.length()) {
            int var9 = var1.indexOf(32, var4);
            if (var9 != -1 && var9 < var1.length() - 1) {
               var2 = var1.substring(var4, var9);
               var3 = var1.substring(var9 + 1);
            }
         }

         if (var4++ != -1 && var4 < var1.length()) {
            var2 = var1.substring(var4);
         }

         if (var2 != null && var3 != null) {
            Object var6 = null;

            try {
               this.domainMgr.resetCredentials(var2, var3);
            } catch (IOException var8) {
               nmLog.log(Level.WARNING, nmText.getServerDirIOError(), var8);
            }

            if (var6 != null) {
               this.sendERR((String)var6);
            } else {
               this.sendOK(nmText.getDomainCredChg(this.domainMgr.getDomainName()));
            }
         } else {
            this.sendERR(nmText.getInvalidCommandSyntax(Command.CHGCRED.toString()));
         }
      }
   }

   private void handleUpdateProps() throws IOException {
      if (this.isDebugEnabled()) {
         this.debugSay("Updating server '" + this.serverMgr.getServerName() + "' startup properties");
      }

      if (this.checkServer()) {
         Properties var1 = null;
         var1 = new Properties();
         DataFormat.readProperties(this.in, var1);
         Object var2 = null;

         try {
            this.serverMgr.saveStartupConfig(var1);
         } catch (Throwable var4) {
            nmLog.log(Level.WARNING, nmText.getErrorWritingConfig(this.serverMgr.getServerName()), var4);
         }

         if (var2 != null) {
            this.sendERR((String)var2);
         } else {
            this.sendOK(nmText.getSrvrPropsUpdate(this.serverMgr.getServerName()));
         }

      }
   }

   private void handleServer(String var1) throws IOException {
      this.coherenceServerMgr = null;
      this.serverMgr = null;
      if (this.checkDomain() && this.checkAuthorized()) {
         String var2 = null;
         int var3 = var1.indexOf(32);
         if (var3++ != -1 && var3 < var1.length()) {
            var2 = var1.substring(var3);
         }

         if (var2 == null) {
            this.sendERR(nmText.getInvalidCommandSyntax(Command.SERVER.toString()));
         } else {
            Object var4 = null;

            try {
               this.serverMgr = this.domainMgr.getServerManager(var2);
            } catch (ConfigException var6) {
               nmLog.log(Level.WARNING, nmText.getServerDirError(), var6);
            } catch (IOException var7) {
               nmLog.log(Level.WARNING, nmText.getServerDirIOError(), var7);
            }

            if (var4 != null) {
               this.sendERR((String)var4);
            } else {
               this.sendOK(nmText.getSrvrMsg(var2));
            }
         }
      }
   }

   private void handleCoherenceServer(String var1) throws IOException {
      this.coherenceServerMgr = null;
      this.serverMgr = null;
      if (this.checkDomain() && this.checkAuthorized()) {
         String var2 = null;
         int var3 = var1.indexOf(32);
         if (var3++ != -1 && var3 < var1.length()) {
            var2 = var1.substring(var3);
         }

         if (var2 == null) {
            this.sendERR(nmText.getInvalidCommandSyntax(Command.COHERENCESERVER.toString()));
         } else {
            try {
               this.coherenceServerMgr = this.domainMgr.getCoherenceServerManager(var2);
            } catch (ConfigException var5) {
               nmLog.log(Level.WARNING, nmText.getServerDirError(), var5);
            } catch (IOException var6) {
               nmLog.log(Level.WARNING, nmText.getServerDirIOError(), var6);
            }

            this.sendOK(nmText.getSrvrMsg(var2));
         }
      }
   }

   private void handleUser(String var1) throws IOException {
      if (this.checkDomain()) {
         String var2 = null;
         int var3 = var1.indexOf(32);
         if (var3++ != -1 && var3 < var1.length()) {
            var2 = var1.substring(var3);
         }

         if (var2 == null) {
            this.sendERR(nmText.getInvalidCommandSyntax(Command.USER.toString()));
         } else {
            this.nmUser = var2.getBytes("UTF-8");
            this.sendOK(nmText.getNMUserMsg(var2));
         }
      }
   }

   private void handlePass(String var1) throws IOException {
      if (this.checkDomain()) {
         if (this.nmUser == null) {
            this.sendERR(nmText.getPassError());
         } else {
            String var2 = null;
            int var3 = var1.indexOf(32);
            if (var3++ != -1 && var3 < var1.length()) {
               var2 = var1.substring(var3);
            }

            if (var2 == null) {
               this.sendERR(nmText.getInvalidCommandSyntax(Command.PASS.toString()));
            } else if (!this.domainMgr.isAuthorized(new String(this.nmUser, "UTF-8"), var2)) {
               this.sendERR(nmText.getAuthError(this.domainMgr.getDomainName(), new String(this.nmUser, "UTF-8")));
            } else {
               this.authorized = true;
               this.sendOK(nmText.getPassMsg());
            }
         }
      }
   }

   private void handleGetLog() throws IOException {
      ServerManagerI var1 = this.getServerManager();
      if (var1 != null) {
         File var2 = var1.getServerDir().getOutFile();

         BufferedReader var3;
         try {
            var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var2)));
         } catch (FileNotFoundException var9) {
            DataFormat.writeEOS(this.out);
            this.sendERR(nmText.getOutputLogNotFound(var2.toString()));
            return;
         }

         String var4;
         try {
            while((var4 = var3.readLine()) != null) {
               DataFormat.writeLine(this.out, var4);
            }
         } finally {
            var3.close();
            DataFormat.writeEOS(this.out);
         }

         this.sendOK(nmText.getServerLogFile());
         if (this.isDebugEnabled()) {
            this.debugSay("Sent server '" + var1.getServerName() + "' output log file");
         }

      }
   }

   private void handleGetNMLog() throws IOException {
      if (this.checkDomain() && this.checkAuthorized()) {
         BufferedReader var1;
         String var2;
         try {
            var2 = this.nmServer.getConfig().getLogFile();
            var1 = new BufferedReader(new FileReader(var2));
         } catch (FileNotFoundException var7) {
            DataFormat.writeEOS(this.out);
            this.sendERR("Node manager log file not found");
            return;
         }

         try {
            while((var2 = var1.readLine()) != null) {
               DataFormat.writeLine(this.out, var2);
            }
         } finally {
            var1.close();
            DataFormat.writeEOS(this.out);
         }

         this.sendOK(nmText.getNMLogFile());
         if (this.isDebugEnabled()) {
            this.debugSay("Sent NodeManager log file");
         }

      }
   }

   private void handleStat() throws IOException {
      ServerManagerI var1 = this.getServerManager();
      if (var1 != null) {
         String var2 = var1.getState();
         var2 = var2 != null ? var2 : "UNKNOWN";
         this.sendOK(var2);
         if (this.isDebugEnabled()) {
            this.debugSay("Sent status on server '" + var1.getServerName() + "' : " + var2);
         }

      }
   }

   private void handleExecScript(String var1) throws IOException {
      if (this.checkDomain() && this.checkAuthorized()) {
         String var2 = null;
         String var3 = null;
         int var4 = var1.indexOf(32);
         if (var4++ != -1 && var4 < var1.length()) {
            int var5 = var1.indexOf(32, var4);
            if (var5 == -1) {
               var2 = var1.substring(var4);
            } else if (var5 < var1.length() - 1) {
               var2 = var1.substring(var4, var5);
               var3 = var1.substring(var5 + 1);
            }
         }

         if (var2 == null) {
            this.sendERR(nmText.getInvalidCommandSyntax(Command.EXECSCRIPT.toString()));
         } else {
            long var10 = 0L;
            if (var3 != null) {
               var10 = Long.valueOf(var3);
            }

            try {
               int var7 = this.domainMgr.execScript(var2, var10);
               if (var7 != 0) {
                  this.sendScriptERR(var7);
               } else {
                  this.sendOK(nmText.getScriptMsg(var2));
               }
            } catch (IOException var9) {
               String var8 = nmText.getScriptError();
               nmLog.log(Level.WARNING, var8, var9);
               this.sendERR(var8);
            }

         }
      }
   }

   private void handleStart(boolean var1) throws IOException {
      ServerManagerI var2 = this.getServerManager();
      if (var2 != null) {
         Properties var3 = null;
         if (var1) {
            var3 = new Properties();
            DataFormat.readProperties(this.in, var3);
         }

         String var4 = null;

         try {
            var2.start(var3);
         } catch (IllegalStateException var6) {
            var4 = nmText.getServerStarted(var2.getServerName());
         } catch (Throwable var7) {
            var4 = nmText.getServerStartError(var2.getServerName());
            nmLog.log(Level.WARNING, var4, var7);
         }

         if (var4 != null) {
            this.sendERR(var4);
         } else {
            this.sendOK(nmText.getServerStartedMsg(var2.getServerName()));
         }

      }
   }

   private void handleKill() throws IOException {
      ServerManagerI var1 = this.getServerManager();
      if (var1 != null) {
         String var2 = null;

         try {
            if (this.isDebugEnabled()) {
               this.debugSay("Killing server " + var1.getServerName());
            }

            var1.kill();
         } catch (IllegalStateException var4) {
            var2 = nmText.getServerStopped(var1.getServerName());
         } catch (Throwable var5) {
            var2 = nmText.getServerStopError(var1.getServerName());
            nmLog.log(Level.WARNING, var2, var5);
         }

         if (var2 != null) {
            this.sendERR(var2);
         } else {
            this.sendOK(nmText.getServerKilled(var1.getServerName()));
         }

      }
   }

   private boolean checkDomain() throws IOException {
      if (this.domainMgr == null) {
         this.sendERR(nmText.getDomainNull());
         return false;
      } else {
         return true;
      }
   }

   private ServerManagerI getServerManager() throws IOException {
      Object var1 = this.coherenceServerMgr != null ? this.coherenceServerMgr : this.serverMgr;
      return (ServerManagerI)(this.checkServer((ServerManagerI)var1) ? var1 : null);
   }

   private boolean checkServer() throws IOException {
      return this.checkServer(this.serverMgr);
   }

   private boolean checkServer(ServerManagerI var1) throws IOException {
      if (var1 == null) {
         this.sendERR(nmText.getServerNull());
         return false;
      } else {
         return true;
      }
   }

   private boolean checkAuthorized() throws IOException {
      if (!this.authorized) {
         this.sendERR(nmText.getAuthNull());
         return false;
      } else {
         return true;
      }
   }

   private void sendGreeting() throws IOException {
      String var1 = nmText.getGreeting("Node Manager");
      this.sendOK(var1);
      if (this.isDebugEnabled()) {
         this.debugSay("Sent Greeting : " + var1);
      }

   }

   private void sendOK(String var1) throws IOException {
      if (this.isDebugEnabled()) {
         this.debugSay("Sending OK message : " + var1);
      }

      DataFormat.writeOK(this.out, var1);
      if (this.isDebugEnabled()) {
         this.debugSay("Sent OK message : " + var1);
      }

   }

   private void sendERR(String var1) throws IOException {
      if (this.isDebugEnabled()) {
         this.debugSay("Sending ERROR message : " + var1);
      }

      DataFormat.writeERR(this.out, var1);
      if (this.isDebugEnabled()) {
         this.debugSay("Sent ERROR message : " + var1);
      }

   }

   private void sendScriptERR(int var1) throws IOException {
      if (this.isDebugEnabled()) {
         this.debugSay("Sending ERROR for script exit code : " + var1);
      }

      DataFormat.writeERR(this.out, String.valueOf(var1));
      if (this.isDebugEnabled()) {
         this.debugSay("Sent ERROR for script exit code : " + var1);
      }

   }

   private boolean isDebugEnabled() {
      return this.nmServer.isDebugEnabled();
   }

   private void debugSay(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("<").append(Thread.currentThread()).append("> ");
      var2.append(var1);
      nmLog.log(Level.FINEST, var2.toString());
   }
}
