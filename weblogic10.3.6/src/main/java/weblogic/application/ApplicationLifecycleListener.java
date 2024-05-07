package weblogic.application;

public abstract class ApplicationLifecycleListener {
   public void initializingApplication(ApplicationLifecycleEvent var1) {
   }

   public void applicationInitialized(ApplicationLifecycleEvent var1) {
   }

   public void applicationDestroyed(ApplicationLifecycleEvent var1) {
   }

   public void preStart(ApplicationLifecycleEvent var1) throws ApplicationException {
   }

   public void postStart(ApplicationLifecycleEvent var1) throws ApplicationException {
   }

   public void preStop(ApplicationLifecycleEvent var1) throws ApplicationException {
   }

   public void postStop(ApplicationLifecycleEvent var1) throws ApplicationException {
   }

   public void preparing(ApplicationLifecycleEvent var1) {
   }

   public void prepared(ApplicationLifecycleEvent var1) {
   }

   public void unpreparing(ApplicationLifecycleEvent var1) {
   }

   public void unprepared(ApplicationLifecycleEvent var1) {
   }
}
