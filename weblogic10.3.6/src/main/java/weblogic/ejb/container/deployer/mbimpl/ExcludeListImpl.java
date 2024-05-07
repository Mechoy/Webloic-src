package weblogic.ejb.container.deployer.mbimpl;

import java.util.ArrayList;
import java.util.Collection;
import weblogic.ejb.container.interfaces.ExcludeList;
import weblogic.j2ee.descriptor.ExcludeListBean;
import weblogic.j2ee.descriptor.MethodBean;

public final class ExcludeListImpl implements ExcludeList {
   ExcludeListBean m_bean;

   public ExcludeListImpl(ExcludeListBean var1) {
      this.m_bean = var1;
   }

   public Collection getAllMethodDescriptors() {
      ArrayList var1 = new ArrayList();
      MethodBean[] var2 = this.m_bean.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.add(new MethodDescriptorImpl(var2[var3]));
      }

      return var1;
   }
}
