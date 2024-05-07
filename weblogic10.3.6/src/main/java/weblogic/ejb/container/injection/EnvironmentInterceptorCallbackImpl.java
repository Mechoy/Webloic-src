package weblogic.ejb.container.injection;

import com.oracle.pitchfork.interfaces.EnvironmentInterceptorCallback;
import com.oracle.pitchfork.interfaces.MethodInvocationVisitor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.interfaces.WLSessionBean;
import weblogic.ejb.container.internal.EJBContextManager;
import weblogic.ejb.container.internal.ExtendedPersistenceContextManager;
import weblogic.ejb.container.internal.ExtendedPersistenceContextWrapper;

public class EnvironmentInterceptorCallbackImpl implements EnvironmentInterceptorCallback {
   public Object callback(MethodInvocationVisitor var1) throws Throwable {
      if (var1.getDeclaringClass() == WLEnterpriseBean.class) {
         return var1.visit();
      } else {
         Object var2 = var1.getBean();
         HashSet var3 = null;
         if (var2 instanceof WLSessionBean) {
            Set var4 = ((WLSessionBean)var2).getExtendedPersistenceContexts();
            if (var4 != null) {
               Iterator var5 = var4.iterator();

               while(var5.hasNext()) {
                  ExtendedPersistenceContextWrapper var6 = (ExtendedPersistenceContextWrapper)var5.next();
                  ExtendedPersistenceContextWrapper var7 = ExtendedPersistenceContextManager.getExtendedPersistenceContext(var6.getPersistenceUnitName());
                  if (var7 == null) {
                     ExtendedPersistenceContextManager.setExtendedPersistenceContext(var6.getPersistenceUnitName(), var6);
                     if (var3 == null) {
                        var3 = new HashSet();
                     }

                     var3.add(var6.getPersistenceUnitName());
                  }
               }
            }
         }

         EJBContextManager.pushBean(var2);

         Object var14;
         try {
            var14 = var1.visit();
         } finally {
            if (var3 != null) {
               Iterator var10 = var3.iterator();

               while(var10.hasNext()) {
                  String var11 = (String)var10.next();
                  ExtendedPersistenceContextManager.removeExtendedPersistenceContext(var11);
               }
            }

            EJBContextManager.popBean();
         }

         return var14;
      }
   }
}
