package weblogic.ant.taskdefs.iiop;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class Ipaddress extends Task {
   private String property;
   private String host;

   public void setHost(String var1) {
      this.host = var1;
   }

   public void setProperty(String var1) {
      this.property = var1;
   }

   public void execute() throws BuildException {
      if (this.property == null) {
         throw new BuildException("property attribute required", this.location);
      } else if (this.host == null) {
         throw new BuildException("host attribute required", this.location);
      } else {
         try {
            String var1 = InetAddress.getByName(this.host).getHostAddress();
            this.getProject().setNewProperty(this.property, var1);
         } catch (UnknownHostException var2) {
            throw new BuildException(var2.getMessage());
         }
      }
   }
}
