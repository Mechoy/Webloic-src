package weblogic.ant.taskdefs.iiop;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class CppIdlSetup extends Task {
   private PrintWriter pw;
   private String idlFile;
   private String rootDir;
   private final String CL_COMMAND = "cl -EP -I. -Iidl -nologo ";
   private final String CLIENT_IDL = "idl/client.idl";

   public void setDir(String var1) {
      this.rootDir = var1;
   }

   public void setFile(String var1) {
      this.idlFile = var1;
   }

   public void execute() throws BuildException {
      this.createClientIdlFile();
   }

   private void createClientIdlFile() {
      Process var2 = null;

      try {
         var2 = Runtime.getRuntime().exec("cl -EP -I. -Iidl -nologo " + this.idlFile);
      } catch (Exception var4) {
         throw new BuildException("Unable to find command: cl.  This example was developed for Windows only and requires Microsoft Visual C++ and Visibroker 4.1.");
      }

      try {
         BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.getInputStream()));
         this.pw = new PrintWriter(new FileWriter(this.rootDir + "/" + "idl/client.idl"));

         String var1;
         while((var1 = var3.readLine()) != null) {
            this.pw.println(var1);
         }

         this.pw.close();
      } catch (IOException var5) {
         throw new BuildException("Unable to write client.idl.  Check file and directory permissions.");
      }
   }
}
