package weblogic.application;

public abstract class ApplicationVersionLifecycleListener {
   public void preDeploy(ApplicationVersionLifecycleEvent var1) throws ApplicationException {
   }

   public void postDeploy(ApplicationVersionLifecycleEvent var1) {
   }

   public void preUndeploy(ApplicationVersionLifecycleEvent var1) throws ApplicationException {
   }

   public void postDelete(ApplicationVersionLifecycleEvent var1) {
   }
}
