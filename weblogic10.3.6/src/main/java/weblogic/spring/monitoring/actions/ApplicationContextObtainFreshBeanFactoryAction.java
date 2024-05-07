package weblogic.spring.monitoring.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPointImpl;
import weblogic.diagnostics.instrumentation.JoinPoint;

public class ApplicationContextObtainFreshBeanFactoryAction extends BaseElapsedTimeAction {
   private static final long serialVersionUID = 1L;
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugSpringStatistics");

   public ApplicationContextObtainFreshBeanFactoryAction() {
      super("SpringApplicationContextObtainFreshBeanFactoryAction");
   }

   public void postProcess(JoinPoint var1, DiagnosticActionState var2) {
      super.postProcess(var1, var2);
      if (var1 instanceof DynamicJoinPointImpl) {
         DynamicJoinPointImpl var3 = (DynamicJoinPointImpl)var1;

         try {
            Class var4 = Thread.currentThread().getContextClassLoader().loadClass("org.springframework.context.ApplicationContext");
            Class var5 = Thread.currentThread().getContextClassLoader().loadClass("org.springframework.beans.factory.BeanFactory");
            Class var6 = Thread.currentThread().getContextClassLoader().loadClass("weblogic.spring.beans.SpringServerApplicationContextUtils");
            Method var7 = var6.getMethod("setParentBeanFactoryIfNecessary", var4, var5);
            var7.invoke((Object)null, ((ElapsedTimeActionState)var2).getSpringBean(), var3.getReturnValue());
         } catch (Exception var8) {
         }

      }
   }

   protected void updateRuntimeMBean(DiagnosticActionState var1) {
      ElapsedTimeActionState var2 = (ElapsedTimeActionState)var1;
      this.updateBeanFactory(var2.getSpringBean());
   }

   private void updateBeanFactory(Object var1) {
      if (var1 != null) {
         Method var2;
         try {
            Class var3 = Class.forName("weblogic.spring.monitoring.SpringRuntimeStatisticsMBeanManager", true, var1.getClass().getClassLoader());
            var2 = var3.getDeclaredMethod("updateBeanFactory", Object.class);
         } catch (ClassNotFoundException var4) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("ApplicationContextObtainFreshBeanFactoryAction.updateBeanFactory failed to find class", var4);
            }

            return;
         } catch (SecurityException var5) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("ApplicationContextObtainFreshBeanFactoryAction.updateBeanFactory failed to find method", var5);
            }

            return;
         } catch (NoSuchMethodException var6) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("ApplicationContextObtainFreshBeanFactoryAction.updateBeanFactory failed to find method", var6);
            }

            return;
         }

         try {
            var2.invoke((Object)null, var1);
         } catch (IllegalArgumentException var7) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("ApplicationContextObtainFreshBeanFactoryAction.updateBeanFactory failed to invoke", var7);
            }
         } catch (IllegalAccessException var8) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("ApplicationContextObtainFreshBeanFactoryAction.updateBeanFactory failed to invoke", var8);
            }
         } catch (InvocationTargetException var9) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("ApplicationContextObtainFreshBeanFactoryAction.updateBeanFactory failed to invoke", var9);
            }
         }

      }
   }
}
