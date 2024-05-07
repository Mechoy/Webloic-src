package weblogic.jms.client;

import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

public class JMSExceptionContext extends JMSContext {
   private ExceptionListener listener = null;

   public JMSExceptionContext() {
   }

   public JMSExceptionContext(ExceptionListener var1) {
      if (var1 != null) {
         this.setClassLoader(var1.getClass().getClassLoader());
      }

      this.listener = var1;
   }

   public JMSExceptionContext(ExceptionListener var1, ClassLoader var2) {
      this.setClassLoader(var2);
      this.listener = var1;
   }

   public void setExceptionListener(ExceptionListener var1) {
      this.listener = var1;
   }

   public ExceptionListener getExceptionListener() {
      return this.listener;
   }

   public void invokeListener(JMSException var1) throws Exception {
      if (this.listener != null) {
         synchronized(this.listener) {
            final ExceptionListener var3 = this.listener;
            final JMSException var4 = var1;

            try {
               this.subject.doAs(this.getKernelId(), new PrivilegedExceptionAction() {
                  public Object run() throws JMSException {
                     var3.onException(var4);
                     return null;
                  }
               });
            } catch (PrivilegedActionException var7) {
               throw var7.getException();
            }
         }
      }

   }
}
