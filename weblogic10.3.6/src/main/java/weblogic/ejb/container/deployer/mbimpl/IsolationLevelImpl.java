package weblogic.ejb.container.deployer.mbimpl;

import java.util.ArrayList;
import java.util.Collection;
import weblogic.ejb.container.interfaces.IsolationLevel;
import weblogic.j2ee.descriptor.wl.MethodBean;
import weblogic.j2ee.descriptor.wl.TransactionIsolationBean;

public final class IsolationLevelImpl implements IsolationLevel {
   private String m_isolationLevel;
   private Collection m_methodDescriptors = new ArrayList();

   public IsolationLevelImpl(TransactionIsolationBean var1) {
      this.m_isolationLevel = var1.getIsolationLevel();
      MethodBean[] var2 = var1.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.m_methodDescriptors.add(new MethodDescriptorImpl(var2[var3]));
      }

   }

   public String getIsolationLevel() {
      return this.m_isolationLevel;
   }

   public Collection getAllMethodDescriptors() {
      return this.m_methodDescriptors;
   }
}
