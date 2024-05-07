package weblogic.deploy.api.tools.deployer;

import javax.management.MBeanServerConnection;
import weblogic.deploy.api.internal.utils.DeployerHelperException;
import weblogic.deploy.api.internal.utils.JMXDeployerHelper;
import weblogic.deploy.utils.MBeanHomeTool;
import weblogic.deploy.utils.MBeanHomeToolException;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;

abstract class TaskOperation extends Operation {
   protected DeploymentTaskRuntimeMBean task = null;
   protected int failures;
   protected MBeanServerConnection mbs;
   protected JMXDeployerHelper helper;

   TaskOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void validate() throws IllegalArgumentException, DeployerException {
      super.validate();
   }

   public void prepare() throws DeployerException {
      try {
         this.helper = new JMXDeployerHelper(this.mbs);
      } catch (DeployerHelperException var2) {
         throw new DeployerException(var2.toString());
      }
   }

   public void connect() throws DeployerException {
      try {
         this.mbs = this.tool.getMBeanServer();
      } catch (MBeanHomeToolException var3) {
         DeployerException var2 = new DeployerException(cat.errorUnableToAccessDeployer(this.tool.getOpts().getOption("adminurl", "t3://localhost:7001"), var3.getMessage()));
         if (this.options.debug || this.options.verbose) {
            var2.initCause(var3);
         }

         throw var2;
      }
   }

   public abstract void execute() throws Exception;

   public abstract int report() throws DeployerException;
}
