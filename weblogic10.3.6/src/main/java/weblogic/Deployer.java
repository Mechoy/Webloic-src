package weblogic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import weblogic.deploy.internal.DeployerTextFormatter;
import weblogic.utils.Debug;
import weblogic.utils.compiler.Tool;

public class Deployer {
   private static final String oldDeployerClass = "weblogic.deploy.api.tools.deployer.OldDeployer";
   private static final String newDeployerClass = "weblogic.deploy.api.tools.deployer.Deployer";
   private static final String OLD_DEPLOYER_PROPERTY = "weblogic.use.old.deployer";
   private static final String NEW_DEPLOYER_PROPERTY = "weblogic.use.new.deployer";
   private static final boolean forceOld = Debug.getCategory("weblogic.use.old.deployer").isEnabled();
   private static final boolean forceNew = Debug.getCategory("weblogic.use.new.deployer").isEnabled();
   private static final boolean defaultOld = false;
   private static final boolean useOld;
   private Tool delegate;
   private static final DeployerTextFormatter messageFormatter;
   public static final long MAX_NOTIFICATION_WAIT = 1000L;

   public Deployer(String[] var1) throws Exception {
      this.delegate = this.getDelegate(var1);
      this.delegate.setUsageName(this.getClass().getName());
   }

   private Tool getDelegate(String[] var1) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      String var2 = useOld ? "weblogic.deploy.api.tools.deployer.OldDeployer" : "weblogic.deploy.api.tools.deployer.Deployer";
      Class var3 = Class.forName(var2);
      Constructor var4 = var3.getDeclaredConstructor(var1.getClass());
      return (Tool)var4.newInstance(var1);
   }

   public static void main(String[] var0) {
      try {
         (new Deployer(var0)).run();
      } catch (Exception var2) {
         if (var2.getMessage() == null) {
            var2.printStackTrace();
         }

         System.err.println(messageFormatter.errorInitDeployer(var2.toString()));
      }

   }

   public static void mainWithExceptions(String[] var0) throws Exception {
      (new Deployer(var0)).run();
   }

   public void run() throws Exception {
      if (this.delegate == null) {
         throw new AssertionError("Deployer not initialized");
      } else {
         try {
            this.delegate.run();
         } catch (weblogic.deploy.api.tools.deployer.DeployerException var2) {
            throw new DeployerException(var2);
         }
      }
   }

   public void run(String[] var1) throws Exception {
      if (this.delegate == null) {
         throw new AssertionError("Deployer not initialized");
      } else {
         try {
            this.delegate.run(var1);
         } catch (weblogic.deploy.api.tools.deployer.DeployerException var3) {
            throw new DeployerException(var3);
         }
      }
   }

   public void prepare() {
      if (this.delegate != null) {
         try {
            this.delegate.prepare();
         } catch (Exception var2) {
            System.err.println(messageFormatter.errorInitDeployer(var2.toString()));
         }
      }

   }

   public void runBody() {
      if (this.delegate != null) {
         try {
            this.delegate.runBody();
         } catch (Exception var2) {
            System.err.println(var2.toString());
         }
      }

   }

   static {
      useOld = forceOld && !forceNew;
      messageFormatter = new DeployerTextFormatter();
   }

   public static class DeployerException extends Exception {
      private static final long serialVersionUID = -182829700656700682L;

      DeployerException(String var1) {
         super(var1);
      }

      DeployerException(Exception var1) {
         super(var1);
      }
   }
}
