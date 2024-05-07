package weblogic.nodemanager.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Properties;
import weblogic.nodemanager.NMConnectException;
import weblogic.nodemanager.NMException;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.ScriptExecutionFailureException;
import weblogic.nodemanager.common.Command;
import weblogic.nodemanager.common.DataFormat;
import weblogic.nodemanager.common.ServerType;

abstract class NMServerClient extends NMClient {
   private Socket sock;
   private BufferedReader in;
   private BufferedWriter out;
   private boolean connected;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public synchronized String getState(int var1) throws IOException {
      this.checkConnected(var1);
      this.sendServer();
      this.sendCmd(Command.STAT);
      return this.checkResponse();
   }

   public synchronized String getStates(int var1) throws IOException {
      this.checkConnected(var1);
      this.sendCmd(Command.GETSTATES);
      return this.checkResponse();
   }

   public synchronized String getVersion() throws IOException {
      this.checkConnected();
      this.sendCmd(Command.VERSION);
      return this.checkResponse();
   }

   public synchronized void getNMLog(Writer var1) throws IOException {
      this.checkConnected();
      this.sendCmd(Command.GETNMLOG);
      DataFormat.copy(this.in, var1);
      this.checkResponse();
   }

   public synchronized void getLog(Writer var1) throws IOException {
      this.checkConnected();
      this.sendServer();
      this.sendCmd(Command.GETLOG);
      DataFormat.copy(this.in, var1);
      this.checkResponse();
   }

   public synchronized void start() throws IOException {
      this.start((Properties)null);
   }

   public synchronized void start(Properties var1) throws IOException {
      this.checkConnected();
      this.sendServer();
      if (var1 != null) {
         this.sendCmd(Command.STARTP);
         DataFormat.writeProperties(this.out, var1);
      } else {
         this.sendCmd(Command.START);
      }

      this.checkResponse();
   }

   public synchronized void kill() throws IOException {
      this.checkConnected();
      this.sendServer();
      this.sendCmd(Command.KILL);
      this.checkResponse();
   }

   public synchronized void chgCred(String var1, String var2, String var3, String var4) throws IOException {
      this.setNMUser(var1);
      this.setNMPass(var2);
      this.checkConnected();
      this.sendCmd(Command.CHGCRED, new String[]{var3, var4});
      this.checkResponse();
   }

   private void sendHello() throws IOException {
      this.sendCmd(Command.HELLO);
   }

   public synchronized void done() throws IOException {
      try {
         this.disconnect();
      } catch (IOException var2) {
      }

   }

   public synchronized void quit() throws IOException {
      this.checkConnected();
      this.sendCmd(Command.QUIT);
      this.checkResponse();
   }

   protected abstract Socket createSocket(String var1, int var2, int var3) throws IOException;

   protected void checkNotConnected() throws IllegalStateException {
      if (this.connected) {
         throw new IllegalStateException(nmText.getAlreadyConnected());
      }
   }

   public void executeScript(String var1, long var2) throws IOException, ScriptExecutionFailureException {
      if (var1 != null && !var1.equals("")) {
         this.checkConnected();
         this.sendCmd(Command.EXECSCRIPT, new String[]{var1, String.valueOf(var2)});
         this.checkResponse(var1);
      } else {
         throw new IOException(nmText.getInvalidPath(var1));
      }
   }

   public void updateServerProps(Properties var1) throws IOException {
      this.checkConnected();
      this.sendServer();
      this.sendCmd(Command.UPDATEPROPS);
      DataFormat.writeProperties(this.out, var1);
      this.checkResponse();
   }

   private void checkConnected(int var1) throws IOException {
      if (!this.connected) {
         this.connect(var1);
         this.connected = true;
      }

   }

   private void checkConnected() throws IOException {
      this.checkConnected(0);
   }

   private void connect(int var1) throws IOException {
      if (this.domainName == null) {
         throw new IllegalStateException(nmText.getDomainNotSet());
      } else if (this.nmUser != null && this.nmPass == null) {
         throw new IllegalStateException(nmText.getNoPassword());
      } else if (this.nmPass != null && this.nmUser == null) {
         throw new IllegalStateException(nmText.getNoUser());
      } else {
         try {
            this.sock = this.createSocket(this.host, this.port, var1);
         } catch (IOException var4) {
            NMConnectException var3 = new NMConnectException(var4.getMessage() + ". " + nmText.getNoConnect(this.host, Integer.toString(this.port)), this.host, this.port);
            var3.setStackTrace(var4.getStackTrace());
            throw var3;
         }

         this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
         this.out = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
         this.sendHello();
         this.checkResponse();
         if (this.domainDir != null) {
            this.sendCmd(Command.DOMAIN, new String[]{this.domainName, this.domainDir});
         } else {
            this.sendCmd(Command.DOMAIN, new String[]{this.domainName});
         }

         this.checkResponse();
         if (this.nmUser != null && this.nmPass != null) {
            this.sendCmd(Command.USER, new String[]{new String(this.nmUser, "UTF-8")});
            this.checkResponse();
            this.sendCmd(Command.PASS, new String[]{new String(this.nmPass, "UTF-8")});
            this.checkResponse();
         }

      }
   }

   private void sendServer() throws IOException {
      if (this.serverName == null) {
         throw new IllegalStateException(nmText.getServerNotSet());
      } else {
         Command var1 = this.serverType == ServerType.Coherence ? Command.COHERENCESERVER : Command.SERVER;
         this.sendCmd(var1, new String[]{this.serverName});
         this.checkResponse();
      }
   }

   private synchronized void disconnect() throws IOException {
      if (this.connected) {
         this.sock.close();
         this.connected = false;
      }

   }

   private String checkResponse(String var1) throws IOException {
      String var2 = this.in.readLine();
      if (var2 == null) {
         throw new IOException(nmText.getEndOfStream());
      } else {
         String var3;
         if ((var3 = DataFormat.parseERR(var2)) != null) {
            if (var1 != null) {
               boolean var4 = false;
               int var5;
               if ((var5 = DataFormat.parseScriptERR(var2)) != 0) {
                  throw new ScriptExecutionFailureException(var1, var5);
               }
            }

            throw new NMException(var3);
         } else if ((var3 = DataFormat.parseOK(var2)) != null) {
            return var3;
         } else {
            throw new IOException(nmText.getUnexpectedResponse(var2));
         }
      }
   }

   private String checkResponse() throws IOException {
      return this.checkResponse((String)null);
   }

   private void sendCmd(Command var1) throws IOException {
      DataFormat.writeCommand(this.out, var1, (String[])null);
   }

   private void sendCmd(Command var1, String[] var2) throws IOException {
      DataFormat.writeCommand(this.out, var1, var2);
   }
}
