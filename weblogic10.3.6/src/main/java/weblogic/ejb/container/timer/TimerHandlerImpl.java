package weblogic.ejb.container.timer;

import java.lang.reflect.InvocationTargetException;
import javax.ejb.EJBException;
import javax.ejb.EnterpriseBean;
import javax.ejb.NoSuchEntityException;
import javax.ejb.TimedObject;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.TimerHandler;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.EJBContextHandler;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.internal.TimerDrivenLocalObject;
import weblogic.transaction.TxHelper;

public final class TimerHandlerImpl implements TimerHandler {
   private BeanManager beanManager;
   private BeanInfo beanInfo;
   private MethodDescriptor md;

   public TimerHandlerImpl(BeanManager var1, BeanInfo var2, MethodDescriptor var3) {
      this.beanManager = var1;
      this.beanInfo = var2;
      this.md = var3;
   }

   public void executeTimer(ClusteredTimerImpl var1) {
      TimerObject var2 = new TimerObject(this.beanManager, this.beanInfo, this.md);
      var2.execute(var1);
   }

   private class TimerObject extends TimerDrivenLocalObject {
      private boolean isMessageDrivenBean = false;
      private MethodDescriptor md;

      public TimerObject(BeanManager var2, BeanInfo var3, MethodDescriptor var4) {
         super.setBeanManager(var2);
         super.setBeanInfo(var3);
         this.md = var4;
         if (var3 instanceof MessageDrivenBeanInfo) {
            this.isMessageDrivenBean = true;
         }

      }

      public void execute(ClusteredTimerImpl var1) {
         InvocationWrapper var2;
         try {
            var2 = this.preInvoke(var1.getPrimaryKey(), this.md, new EJBContextHandler(this.md, new Object[]{var1}));
         } catch (Throwable var16) {
            if (var16 instanceof EJBException) {
               Exception var4 = ((EJBException)var16).getCausedByException();
               if (var4 instanceof NoSuchEntityException) {
                  var1.cancel();
                  return;
               }
            }

            EJBLogger.logExceptionBeforeInvokingEJBTimeout(this.beanInfo.getDisplayName(), var16);
            if (var1.isTransactional()) {
               try {
                  TxHelper.getTransaction().setRollbackOnly();
               } catch (Exception var15) {
                  EJBLogger.logErrorMarkingRollback(var15);
               }
            }

            return;
         }

         try {
            EnterpriseBean var3 = var2.getBean();
            WLEnterpriseBean var20 = null;
            if (!this.isMessageDrivenBean) {
               var20 = (WLEnterpriseBean)var3;
            }

            int var5 = 0;
            if (var20 != null) {
               var5 = var20.__WL_getMethodState();
            }

            Throwable var6 = null;

            try {
               if (var20 != null) {
                  var20.__WL_setMethodState(65536);
               }

               ((TimedObject)var3).ejbTimeout(new TimerWrapper(var1));
            } catch (Throwable var17) {
               Throwable var7 = var17;
               if (var17 instanceof InvocationTargetException) {
                  var7 = var17.getCause();
               }

               var6 = var7;
            } finally {
               if (var20 != null) {
                  var20.__WL_setMethodState(var5);
               }

            }

            this.postInvoke(var2, var6);
         } catch (Throwable var19) {
            EJBLogger.logExceptionInvokingEJBTimeout(this.beanInfo.getDisplayName(), var19);
         }

      }
   }
}
