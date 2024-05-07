package weblogic.scheduler.ejb.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.TargetUtils;
import weblogic.j2ee.ApplicationManager;
import weblogic.scheduler.ApplicationNotFoundException;
import weblogic.scheduler.TimerCreationCallback;
import weblogic.scheduler.TimerListenerExtension;
import weblogic.scheduler.ejb.EJBTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.utils.classloaders.Annotation;

public class EJBListenerWrapper implements TimerListener, TimerCreationCallback, TimerListenerExtension, Externalizable {
   private EJBTimerListener ejbListener;
   private String annotation;
   private String dispatchPolicy;

   EJBListenerWrapper(String var1, EJBTimerListener var2, String var3) {
      this.ejbListener = var2;
      this.annotation = var1;
      if (var3 != null) {
         this.dispatchPolicy = var3;
      } else {
         this.dispatchPolicy = "default";
      }

   }

   public EJBListenerWrapper() {
   }

   public void timerExpired(Timer var1) {
      this.executeTimer(var1);
   }

   private void executeTimer(Timer var1) {
      ClassLoader var2 = ApplicationManager.getApplicationClassLoader(new Annotation(this.annotation));
      ClassLoader var3 = Thread.currentThread().getContextClassLoader();

      try {
         Thread.currentThread().setContextClassLoader(var2);
         this.ejbListener.timerExpired(var1);
      } finally {
         Thread.currentThread().setContextClassLoader(var3);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.annotation);
      var1.writeObject(this.ejbListener);
      var1.writeUTF(this.dispatchPolicy);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.annotation = var1.readUTF();
      Annotation var2 = new Annotation(this.annotation);
      ClassLoader var3 = ApplicationManager.getApplicationClassLoader(var2);
      ApplicationContextInternal var4 = ApplicationAccess.getApplicationAccess().getApplicationContext(var2.getApplicationName());
      boolean var5 = var4 != null && TargetUtils.isDeployedLocally(var4.getBasicDeploymentMBean().getTargets());
      if (!var5) {
         throw new ApplicationNotFoundException(var2.getApplicationName());
      } else {
         ClassLoader var6 = Thread.currentThread().getContextClassLoader();

         try {
            Thread.currentThread().setContextClassLoader(var3);
            this.ejbListener = (EJBTimerListener)var1.readObject();
         } finally {
            Thread.currentThread().setContextClassLoader(var6);
         }

         this.dispatchPolicy = var1.readUTF();
      }
   }

   public String getTimerId(String var1) {
      String var2 = var1;
      if (this.ejbListener.getGroupName() != null) {
         var2 = this.ejbListener.getGroupName() + "@@" + var1;
      }

      return var2;
   }

   public EJBTimerListener getEJBTimerListener() {
      return this.ejbListener;
   }

   public String getDispatchPolicy() {
      return this.dispatchPolicy;
   }

   public boolean isTransactional() {
      return false;
   }

   public String getApplicationName() {
      return (new Annotation(this.annotation)).getApplicationName();
   }

   public String getModuleName() {
      return (new Annotation(this.annotation)).getModuleName();
   }
}
