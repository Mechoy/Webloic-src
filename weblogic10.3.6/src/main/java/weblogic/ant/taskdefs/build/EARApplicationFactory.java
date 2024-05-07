package weblogic.ant.taskdefs.build;

public final class EARApplicationFactory extends ApplicationFactory {
   protected Application claim(BuildCtx var1) {
      return new Application(var1);
   }
}
