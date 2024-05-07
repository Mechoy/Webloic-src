package weblogic.ejb.container.deployer.mbimpl;

import java.util.ArrayList;
import java.util.Collection;
import weblogic.ejb.container.interfaces.ContainerTransaction;
import weblogic.j2ee.descriptor.ContainerTransactionBean;
import weblogic.j2ee.descriptor.MethodBean;

public final class ContainerTransactionImpl implements ContainerTransaction {
   private MethodBean[] m_methods;
   private String m_transactionAttribute;

   public ContainerTransactionImpl(ContainerTransactionBean var1) {
      this.m_methods = var1.getMethods();
      this.m_transactionAttribute = var1.getTransAttribute();
   }

   public Collection getAllMethodDescriptors() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.m_methods.length; ++var2) {
         MethodDescriptorImpl var3 = new MethodDescriptorImpl(this.m_methods[var2]);
         var1.add(var3);
      }

      return var1;
   }

   public String getTransactionAttribute() {
      return this.m_transactionAttribute;
   }
}
