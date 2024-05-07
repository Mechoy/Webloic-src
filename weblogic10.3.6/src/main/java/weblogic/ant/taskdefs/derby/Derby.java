package weblogic.ant.taskdefs.derby;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;

public class Derby extends Java {
   private static final String DERBY_MAIN_CLASS = "org.apache.derby.drda.NetworkServerControl";
   private static final int DEFAULT_PORT = 1527;
   private String host = null;
   private Integer port = null;
   private String systemHome = null;
   private String operation = "ping";
   private boolean execFailed = false;

   public void setHost(String var1) {
      this.host = var1;
   }

   public void setPort(int var1) {
      this.port = var1;
   }

   public void setOperation(String var1) {
      this.operation = var1;
   }

   public void setSystemHome(String var1) {
      this.systemHome = var1;
   }

   public void execute() throws BuildException {
      this.executeDerby();
   }

   private void executeDerby() {
      this.setFork(true);
      this.setSpawn(true);
      this.setClassname("org.apache.derby.drda.NetworkServerControl");
      this.setProperty("derby.system.home", this.systemHome);
      if (this.port != null) {
         this.setProperty("derby.drda.portNumber", this.port);
      }

      if (this.host != null) {
         this.setProperty("derby.drda.host", this.host);
      }

      if (this.operation != null) {
         this.createArg().setValue(this.operation);
      }

      System.out.println("invoking: " + this.getCommandLine());
      if (this.executeJava() != 0) {
         System.out.println("failed: ");
         this.execFailed = true;
      }

      System.out.println("finished");
   }

   private void setProperty(String var1, Object var2) {
      if (var2 != null) {
         this.createJvmarg().setValue("-D" + var1 + "=" + var2.toString());
      }

   }

   private void setProperty(String var1, boolean var2) {
      if (var2) {
         this.createJvmarg().setValue("-D" + var1);
      }

   }

   public void run() {
      this.executeDerby();
   }
}
