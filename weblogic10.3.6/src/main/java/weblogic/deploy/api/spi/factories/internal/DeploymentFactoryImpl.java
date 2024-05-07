package weblogic.deploy.api.spi.factories.internal;

import java.net.URI;
import java.net.URISyntaxException;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.deploy.WebLogicDeploymentManagerImpl;
import weblogic.deploy.api.spi.factories.WebLogicDeploymentFactory;
import weblogic.utils.net.InetAddressHelper;

public class DeploymentFactoryImpl implements WebLogicDeploymentFactory {
   private static final String JMX_DOMAIN_ACCESS_CLASS = "weblogic.deploy.api.spi.deploy.internal.ServerConnectionImpl";
   private static final String SERVER_CONNECTION_CLASS = "weblogic.deploy.api.spi.deploy.internal.ServerConnectionImpl";
   private static final String DEPLOYMENT_MANAGER_VERSION = "1.0";
   private String host;
   private int port;
   private static final boolean debug = Debug.isDebug("factory");
   private static final String[] uris = new String[]{"deployer:WebLogic", "remote:deployer:WebLogic", "authenticated:deployer:WebLogic"};
   private String deployerUri;
   private URI connectUri;

   public DeploymentManager getDisconnectedDeploymentManager(String var1) throws DeploymentManagerCreationException {
      try {
         ConfigHelper.checkParam("uri", var1);
         if (debug) {
            Debug.say("Getting disconnected DM with uri: " + var1);
         }

         this.validateUri(var1);
         WebLogicDeploymentManagerImpl var2 = new WebLogicDeploymentManagerImpl(this.deployerUri);
         return var2;
      } catch (IllegalArgumentException var3) {
         throw new DeploymentManagerCreationException(var3.getMessage());
      }
   }

   public DeploymentManager getDeploymentManager(String var1, String var2, String var3) throws DeploymentManagerCreationException {
      try {
         ConfigHelper.checkParam("uri", var1);
         if (debug) {
            Debug.say("Getting connected DM with uri: " + var1 + ", user: " + var2);
         }

         this.validateUri(var1);
         if ((this.host == null || this.port == -1) && !var1.toString().startsWith("authenticated:deployer:WebLogic")) {
            throw new DeploymentManagerCreationException(SPIDeployerLogger.getInvalidServerAuth(var1));
         } else {
            return new WebLogicDeploymentManagerImpl("weblogic.deploy.api.spi.deploy.internal.ServerConnectionImpl", this.deployerUri, this.connectUri, var2, var3);
         }
      } catch (IllegalArgumentException var6) {
         var6.printStackTrace();
         DeploymentManagerCreationException var5 = new DeploymentManagerCreationException(var6.getMessage());
         var5.initCause(var6);
         throw var5;
      } catch (DeploymentManagerCreationException var7) {
         var7.printStackTrace();
         throw var7;
      }
   }

   public String getDisplayName() {
      return SPIDeployerLogger.getDisplayName();
   }

   public String getProductVersion() {
      return "1.0";
   }

   public boolean handlesURI(String var1) {
      try {
         this.validateUri(var1);
         return true;
      } catch (IllegalArgumentException var3) {
         return false;
      }
   }

   public String[] getUris() {
      return uris;
   }

   public String createUri(String var1, String var2, String var3) {
      return this.createUri("t3", var1, var2, var3);
   }

   public String createUri(String var1, String var2, String var3, String var4) {
      String var5 = null;

      try {
         var5 = var1 == null ? "t3://" : var1 + "://";
         var5 = var2 == null ? "deployer:WebLogic:" + var5 : var2 + ":" + var5;
         if (debug) {
            Debug.say("createUri host:  " + var3);
         }

         String var6 = null;
         if (var3 == null) {
            var6 = "localhost";
         } else {
            var6 = InetAddressHelper.convertHostIfIPV6(var3);
         }

         if (debug) {
            Debug.say("createUri result host:  " + var6);
         }

         var5 = var5 + var6;
         var5 = var4 == null ? var5 + ":" + "7001" : var5 + ":" + var4;
         this.validateUri(var5);
      } catch (IllegalArgumentException var7) {
         if (debug) {
            Debug.say(var7.getMessage());
         }

         return null;
      }

      if (debug) {
         Debug.say("createUri result: " + var5);
      }

      return var5;
   }

   private void validateUri(String var1) throws IllegalArgumentException {
      if (var1.equals("deployer:WebLogic")) {
         this.deployerUri = "deployer:WebLogic";
      } else if (var1.equals("remote:deployer:WebLogic")) {
         this.deployerUri = "remote:deployer:WebLogic";
      } else if (var1.equals("authenticated:deployer:WebLogic")) {
         this.deployerUri = "authenticated:deployer:WebLogic";
      } else {
         if (var1.startsWith("deployer:WebLogic")) {
            this.deployerUri = "deployer:WebLogic";
         } else if (var1.startsWith("remote:deployer:WebLogic")) {
            this.deployerUri = "remote:deployer:WebLogic";
         } else {
            if (!var1.startsWith("authenticated:deployer:WebLogic")) {
               throw new IllegalArgumentException(SPIDeployerLogger.getInvalidURI(var1));
            }

            this.deployerUri = "authenticated:deployer:WebLogic";
         }

         var1 = var1.substring(this.deployerUri.length());
         if (var1.charAt(0) == ':') {
            try {
               if (var1.indexOf("://") != -1) {
                  this.connectUri = new URI(var1.substring(1));
               } else {
                  this.connectUri = new URI("t3://" + var1.substring(1));
               }

               this.host = this.connectUri.getHost();
               this.port = this.connectUri.getPort();
            } catch (URISyntaxException var3) {
               throw new IllegalArgumentException(SPIDeployerLogger.getInvalidServerAuth(var1));
            }
         } else {
            throw new IllegalArgumentException(SPIDeployerLogger.getInvalidServerAuth(var1));
         }
      }
   }
}
