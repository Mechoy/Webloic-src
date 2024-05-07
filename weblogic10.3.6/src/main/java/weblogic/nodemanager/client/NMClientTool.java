package weblogic.nodemanager.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;
import weblogic.nodemanager.common.Command;
import weblogic.nodemanager.common.DataFormat;
import weblogic.nodemanager.common.ServerType;

public class NMClientTool {
   private String host;
   private int port = -1;
   private String domainName = "mydomain";
   private String domainDir;
   private String serverName = "myserver";
   private ServerType serverType;
   private String nmUser;
   private String nmPass;
   private String nmDir;
   private String clientType;
   private String scriptPath;
   private String timeout;
   private Command cmd;
   private boolean verbose;
   private static final String SERVER_NAME = "myserver";
   private static final String DOMAIN_NAME = "mydomain";
   private static final String CLIENT_TYPE = "ssl";
   private static final ServerType SERVER_TYPE;
   private static final String[] USAGE;

   NMClientTool(String[] var1) {
      this.serverType = SERVER_TYPE;
      this.clientType = "ssl";
      int var2 = this.parseOptions(var1);
      if (var2 >= var1.length) {
         this.printUsage();
         System.exit(1);
      }

      try {
         this.cmd = Command.parse(var1[var2]);
      } catch (IllegalArgumentException var4) {
         System.err.println("Unrecognized command: " + var1[var2]);
         System.exit(1);
      }

   }

   public int run() throws Throwable {
      if (this.clientType.equalsIgnoreCase("ssl") || this.clientType.equalsIgnoreCase("vmms-")) {
         Properties var1 = System.getProperties();
         if (var1.getProperty("weblogic.security.TrustKeyStore") == null) {
            var1.setProperty("weblogic.security.TrustKeyStore", "DemoTrust");
         }
      }

      NMClient var6 = NMClient.getInstance(this.clientType);

      int var2;
      try {
         var2 = this.doCommand(var6);
      } finally {
         var6.done();
      }

      return var2;
   }

   private int doCommand(NMClient var1) throws IOException {
      if (this.host != null) {
         var1.setHost(this.host);
      }

      if (this.port > 0) {
         var1.setPort(this.port);
      }

      if (this.nmDir != null) {
         var1.setNMDir(this.nmDir);
      }

      if (this.domainName != null) {
         var1.setDomainName(this.domainName);
      }

      if (this.domainDir != null) {
         var1.setDomainDir(this.domainDir);
      }

      if (this.serverName != null) {
         var1.setServerName(this.serverName);
      }

      if (this.serverType != null) {
         var1.setServerType(this.serverType);
      }

      if (this.nmUser != null) {
         var1.setNMUser(this.nmUser);
      }

      if (this.nmPass != null) {
         var1.setNMPass(this.nmPass);
      }

      if (this.verbose) {
         var1.setVerbose(true);
      }

      if (this.cmd == Command.STAT) {
         System.out.println(var1.getState(0));
      } else if (this.cmd == Command.VERSION) {
         System.out.println(var1.getVersion());
      } else if (this.cmd == Command.START) {
         var1.start();
      } else if (this.cmd == Command.STARTP) {
         Properties var2 = new Properties();
         DataFormat.readProperties(new BufferedReader(new InputStreamReader(System.in)), var2);
         var1.start(var2);
      } else if (this.cmd == Command.KILL) {
         var1.kill();
      } else if (this.cmd == Command.GETSTATES) {
         System.out.println(var1.getStates(0));
      } else {
         OutputStreamWriter var3;
         if (this.cmd == Command.GETLOG) {
            var3 = new OutputStreamWriter(System.out);
            var1.getLog(var3);
            var3.flush();
         } else if (this.cmd == Command.GETNMLOG) {
            var3 = new OutputStreamWriter(System.out);
            var1.getNMLog(var3);
            var3.flush();
         } else if (this.cmd == Command.EXECSCRIPT) {
            var1.execScript(this.scriptPath, Long.valueOf(this.timeout));
         } else {
            if (this.cmd != Command.QUIT) {
               System.err.println("Unrecognized command: " + this.cmd);
               return 1;
            }

            var1.quit();
         }
      }

      return 0;
   }

   private static String getServerTypeAsString() {
      StringBuilder var0 = new StringBuilder();
      ServerType[] var1 = ServerType.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ServerType var4 = var1[var3];
         var0.append(var4.toString()).append(" ");
      }

      return var0.toString();
   }

   private void printUsage() {
      String[] var1 = USAGE;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         System.out.println(var4);
      }

   }

   private int parseOptions(String[] var1) {
      int var2 = 0;

      try {
         while(var2 < var1.length && var1[var2].startsWith("-")) {
            String var3 = var1[var2++];
            if (var3.equals("-host")) {
               this.host = var1[var2++];
            } else if (var3.equals("-port")) {
               String var4 = var1[var2++];

               try {
                  this.port = Integer.parseInt(var4);
               } catch (NumberFormatException var6) {
                  System.err.println("Invalid port number: " + var4);
                  System.exit(1);
               }
            } else if (!var3.equals("-nmdir") && !var3.equals("-n")) {
               if (!var3.equals("-server") && !var3.equals("-s")) {
                  if (var3.equals("-serverType")) {
                     this.serverType = ServerType.valueOf(var1[var2++]);
                  } else if (!var3.equals("-domain") && !var3.equals("-d")) {
                     if (!var3.equals("-root") && !var3.equals("-r")) {
                        if (var3.equals("-user")) {
                           this.nmUser = var1[var2++];
                        } else if (var3.equals("-pass")) {
                           this.nmPass = var1[var2++];
                        } else if (!var3.equals("-type") && !var3.equals("-t")) {
                           if (!var3.equals("-verbose") && !var3.equals("-v")) {
                              if (!var3.equals("-script_path") && !var3.equals("-sp")) {
                                 if (!var3.equals("-script_timeout") && !var3.equals("-st")) {
                                    if (!var3.equals("-help") && !var3.equals("-?")) {
                                       System.err.println("Invalid option: " + var3);
                                       System.exit(1);
                                    } else {
                                       this.printUsage();
                                       System.exit(0);
                                    }
                                 } else {
                                    this.timeout = var1[var2++];
                                 }
                              } else {
                                 this.scriptPath = var1[var2++];
                              }
                           } else {
                              this.verbose = true;
                           }
                        } else {
                           this.clientType = var1[var2++];
                        }
                     } else {
                        this.domainDir = var1[var2++];
                     }
                  } else {
                     this.domainName = var1[var2++];
                  }
               } else {
                  this.serverName = var1[var2++];
               }
            } else {
               this.nmDir = var1[var2++];
            }
         }
      } catch (IndexOutOfBoundsException var7) {
         System.err.println("Invalid argument syntax");
         System.exit(1);
      }

      return var2;
   }

   public static void main(String[] var0) throws Throwable {
      NMClientTool var1 = new NMClientTool(var0);
      System.exit(var1.run());
   }

   static {
      SERVER_TYPE = ServerType.WebLogic;
      USAGE = new String[]{"Usage: java weblogic.nodemanager.client.NMClientTool [OPTIONS] CMD", "", "Where OPTIONS include:", "  -type <type>      Node manager client type (ssl, plain, rsh, ssh, or vmm[s][-adapter_type][_adapter_version])", "                    (default is 'ssl')", "  -host <host>      Node manager host name (default is 'localhost')", "  -port <port>      Node manager port (default based on client type)", "  -nmdir <nmdir>    Node manager home directory", "  -server <server>  Server name (default is 'myserver')", "  -domain <domain>  Domain name (default is 'mydomain')", "  -serverType <st>  Server type (default is '" + SERVER_TYPE + "') - one of " + getServerTypeAsString(), "  -root <dir>       Domain root directory", "  -user <username>  Node manager username", "  -pass <password>  Node manager password", "  -verbose          Enable verbose output", "  -help             Print this help message", "", "And CMD is one of the following:", "  START       Start server", "  KILL        Kill server", "  STAT        Get server status", "  GETLOG      Retrieve WLS server log", "  GETNMLOG    Retrieve node manager server log", "  VERSION     Return node manager server version", "  QUIT        Asks the nodemanager to quit", ""};
   }
}
