package weblogic.deploy.api.tools.deployer.remote;

import java.io.Serializable;

public class DeployerResponse implements Serializable {
   private static final long serialVersionUID = 1L;
   private int failures;
   private String output;

   DeployerResponse(int var1, String var2) {
      this.failures = var1;
      this.output = var2;
   }

   public int getFailures() {
      return this.failures;
   }

   public String getOutput() {
      return this.output;
   }
}
