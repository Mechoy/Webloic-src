package weblogic.ant.taskdefs.build;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public abstract class ApplicationFactory {
   private static ApplicationFactory[] factories = new ApplicationFactory[]{new EARApplicationFactory()};

   abstract Application claim(BuildCtx var1) throws BuildException;

   private static void logVerbose(Project var0, String var1) {
      var0.log(var1, 3);
   }

   public static Application newApplication(BuildCtx var0) throws BuildException {
      Application var1 = null;
      Project var2 = var0.getProject();

      for(int var3 = 0; var3 < factories.length; ++var3) {
         logVerbose(var2, "Trying factory: " + factories[var3].getClass().getName());
         var1 = factories[var3].claim(var0);
         if (var1 != null) {
            logVerbose(var2, "Factory: " + factories[var3].getClass().getName() + " claimed application");
            return var1;
         }

         logVerbose(var2, "Factory: " + factories[var3].getClass().getName() + " did not claim application.");
      }

      throw new BuildException("Unable to recognize directory: " + var0.getSrcDir().getAbsolutePath() + " as a valid application.  An application should have a " + "META-INF/application.xml, META-INF/ejb-jar.xml, " + "WEB-INF/web.xml, or META-INF/ra.xml file to be recognized.");
   }
}
