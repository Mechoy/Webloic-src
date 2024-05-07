package weblogic.ejb.container.deployer.mbimpl;

import java.util.ArrayList;
import java.util.Collection;
import weblogic.ejb.container.interfaces.RetryMethodsOnRollback;
import weblogic.j2ee.descriptor.wl.MethodBean;
import weblogic.j2ee.descriptor.wl.RetryMethodsOnRollbackBean;

public final class RetryMethodsOnRollbackImpl implements RetryMethodsOnRollback {
   private int m_retryCount;
   private Collection m_methodDescriptors = new ArrayList();

   public RetryMethodsOnRollbackImpl(RetryMethodsOnRollbackBean var1) {
      this.m_retryCount = var1.getRetryCount();
      MethodBean[] var2 = var1.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.m_methodDescriptors.add(new MethodDescriptorImpl(var2[var3]));
      }

   }

   public int getRetryCount() {
      return this.m_retryCount;
   }

   public Collection getAllMethodDescriptors() {
      return this.m_methodDescriptors;
   }
}
