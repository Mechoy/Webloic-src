package weblogic.deploy.api.tools.deployer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.deploy.internal.DeployerTextFormatter;
import weblogic.deploy.utils.MBeanHomeTool;
import weblogic.jndi.Environment;
import weblogic.rmi.extensions.RemoteRuntimeException;

public class Deployer extends MBeanHomeTool {
   private static DeployerTextFormatter cat = new DeployerTextFormatter();
   private Options options;
   private String[] myargs;
   private static final String JNDI_NAME = "weblogic.remote.Deployer";

   public Deployer(String[] var1) {
      super(var1);
      this.myargs = var1;
      cat = new DeployerTextFormatter();
   }

   public void prepare() {
      super.prepare();
      this.options = new Options(this.opts);
      this.setRequireExtraArgs(false);
   }

   public void runBody() throws Exception {
      int var1 = -1;
      this.options.extractOptions();
      System.out.println(cat.infoOptions(this.removePassword(this.myargs)));
      this.checkForMultipleOps();
      if (this.options.examples) {
         this.showDetailedMessage();
      } else {
         this.setShowStackTrace(this.options.debug);
         Operation var2 = null;

         try {
            var2 = this.newOperation();
            var2.validate();
            var1 = this.perform(var2, var1);
         } catch (Throwable var8) {
            if (var8 instanceof RemoteRuntimeException) {
               throw new DeployerException(cat.errorLostConnection());
            }

            if ((!(var8 instanceof RuntimeException) || var8 instanceof IllegalArgumentException) && var8 instanceof Exception) {
               var1 = this.handleExpectedException((Exception)var8);
               throw (Exception)var8;
            }

            Exception var4 = this.handleUnexpectedException(var8);
            var1 = 1;
            throw var4;
         } finally {
            if (var2 != null) {
               var2.cleanUp();
            }

            this.reset();
            if (!this.options.noexit && var1 != -1) {
               System.exit(var1);
            }

         }

      }
   }

   private Exception handleUnexpectedException(Throwable var1) throws Exception {
      var1.printStackTrace();
      Object var2;
      if (var1 instanceof Exception) {
         var2 = (Exception)var1;
      } else {
         var2 = new DeployerException(var1.getMessage());
      }

      if (!this.options.noexit) {
         System.out.println(((Exception)var2).getMessage());
      }

      return (Exception)var2;
   }

   private int handleExpectedException(Exception var1) {
      if (this.options.debug) {
         var1.printStackTrace();
      } else if (!this.options.noexit) {
         if (var1.getMessage() != null) {
            System.out.println(var1.getMessage());
         } else {
            System.out.println(var1.toString());
         }
      }

      return 1;
   }

   private int perform(Operation var1, int var2) throws Exception {
      var1.connect();
      var1.prepare();
      var1.execute();
      var2 = var1.report();
      return var2;
   }

   private String getUriAsString(URI var1) throws URISyntaxException {
      String var2 = var1.getScheme();
      String var3 = var1.getHost();
      int var4 = var1.getPort();
      return (new URI(var2, (String)null, var3, var4, (String)null, (String)null, (String)null)).toString();
   }

   private Context getIIOPContext(String var1, String var2, String var3) throws NamingException {
      Hashtable var4 = new Hashtable();
      var4.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      var4.put("java.naming.provider.url", var1);
      var4.put("java.naming.security.principal", var2);
      var4.put("java.naming.security.credentials", var3);
      return new InitialContext(var4);
   }

   private Context getContext(String var1, String var2, String var3) throws NamingException {
      Environment var4 = new Environment();
      var4.setProviderUrl(var1);
      var4.setSecurityPrincipal(var2);
      var4.setSecurityCredentials(var3);
      return var4.getInitialContext();
   }

   private String removePassword(String[] var1) {
      if (var1 == null) {
         return "";
      } else {
         boolean var2 = false;
         String var3 = "";

         for(int var4 = 0; var4 < var1.length; ++var4) {
            String var5 = var1[var4];
            if (var5.equals("-password")) {
               var2 = true;
            } else if (!var2) {
               var3 = var3 + " " + var5;
            } else {
               var2 = false;
            }
         }

         return var3;
      }
   }

   private void checkForMultipleOps() throws IllegalArgumentException {
      int var1 = 0;
      StringBuffer var2 = new StringBuffer();
      if (this.options.distributeOp) {
         ++var1;
         var2.append("distribute ");
      }

      if (this.options.startOp) {
         ++var1;
         var2.append("start ");
      }

      if (this.options.stopOp) {
         ++var1;
         var2.append("stop ");
      }

      if (this.options.redeployOp) {
         ++var1;
         var2.append("redeploy ");
      }

      if (this.options.undeployOp) {
         ++var1;
         var2.append("undeploy ");
      }

      if (this.options.deployOp) {
         ++var1;
         var2.append("deploy ");
      }

      if (this.options.updateOp) {
         ++var1;
         var2.append("update ");
      }

      if (this.options.cancelOp) {
         ++var1;
         var2.append("cancel ");
      }

      if (this.options.listOp) {
         ++var1;
         var2.append("list ");
      }

      if (this.options.purgetasksOp) {
         ++var1;
         var2.append("purgetasks ");
      }

      if (this.options.listtaskOp) {
         ++var1;
         var2.append("listtask ");
      }

      if (this.options.listappOp) {
         ++var1;
         var2.append("listapps ");
      }

      if (this.options.activateOp) {
         ++var1;
         var2.append("activate ");
      }

      if (this.options.deactivateOp) {
         ++var1;
         var2.append("deactivate ");
      }

      if (this.options.unprepareOp) {
         ++var1;
         var2.append("unprepare ");
      }

      if (this.options.removeOp) {
         ++var1;
         var2.append("remove ");
      }

      if (var1 > 1) {
         throw new IllegalArgumentException(cat.errorMultipleActions(var2.toString()));
      }
   }

   private void showDetailedMessage() {
      if (this.options.distributeOp) {
         System.out.println(cat.usageAdDistribute());
      } else if (this.options.startOp) {
         System.out.println(cat.usageAdStart());
      } else if (this.options.stopOp) {
         System.out.println(cat.usageAdStop());
      } else if (this.options.deployOp) {
         System.out.println(cat.usageAdDeploy());
      } else if (this.options.redeployOp) {
         System.out.println(cat.usageAdRedeploy());
      } else if (this.options.undeployOp) {
         System.out.println(cat.usageAdUndeploy());
      } else if (this.options.updateOp) {
         System.out.println(cat.usageAdUpdate());
      } else if (this.options.cancelOp) {
         System.out.println(cat.usageAdCancel());
      } else if (this.options.listOp) {
         System.out.println(cat.usageAdList());
      } else if (this.options.purgetasksOp) {
         System.out.println(cat.usageAdPurgeTasks());
      } else if (this.options.listtaskOp) {
         System.out.println(cat.usageAdListtask());
      } else if (this.options.listappOp) {
         System.out.println(cat.usageAdListapps());
      } else if (this.options.activateOp) {
         System.out.println(cat.usageAdDeploy());
      } else if (this.options.deactivateOp) {
         System.out.println(cat.usageAdStop());
      } else if (this.options.unprepareOp) {
         System.out.println(cat.usageAdStop());
      } else if (this.options.removeOp) {
         System.out.println(cat.usageAdUndeploy());
      } else {
         System.out.println(cat.showExamples());
      }

   }

   private Operation newOperation() throws IllegalArgumentException {
      if (this.options.distributeOp) {
         return new DistributeOperation(this, this.options);
      } else if (this.options.startOp) {
         return new StartOperation(this, this.options);
      } else if (this.options.stopOp) {
         return new StopOperation(this, this.options);
      } else if (this.options.redeployOp) {
         return new RedeployOperation(this, this.options);
      } else if (this.options.undeployOp) {
         return new UndeployOperation(this, this.options);
      } else if (this.options.deployOp) {
         return new DeployOperation(this, this.options);
      } else if (this.options.updateOp) {
         return new UpdateOperation(this, this.options);
      } else if (this.options.cancelOp) {
         return new CancelOperation(this, this.options);
      } else if (this.options.purgetasksOp) {
         return new PurgeTasksOperation(this, this.options);
      } else if (this.options.listOp) {
         return new ListTaskOperation(this, this.options);
      } else if (this.options.listtaskOp) {
         return new ListTaskOperation(this, this.options);
      } else if (this.options.listappOp) {
         return new ListappsOperation(this, this.options);
      } else if (this.options.activateOp) {
         return new ActivateOperation(this, this.options);
      } else if (this.options.deactivateOp) {
         return new DeactivateOperation(this, this.options);
      } else if (this.options.unprepareOp) {
         return new UnprepareOperation(this, this.options);
      } else if (this.options.removeOp) {
         return new RemoveOperation(this, this.options);
      } else {
         throw new IllegalArgumentException(cat.errorMissingAction());
      }
   }

   private void debug(String var1) {
      if (this.options.debug) {
         System.out.println(var1);
      }

   }

   private void inform(String var1) {
      if (this.options.verbose) {
         System.out.println(var1);
      }

   }
}
