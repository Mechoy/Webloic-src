package weblogic.ejb.container.internal;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.EntityContext;
import weblogic.ejb.container.interfaces.BeanManager;

public final class EntityEJBContextImpl extends BaseEJBContext implements EntityContext {
   private Object primaryKey = null;

   public EntityEJBContextImpl(EnterpriseBean var1, BeanManager var2, BaseEJBHome var3, BaseEJBLocalHome var4, EJBObject var5, EJBLocalObject var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public Object getPrimaryKey() throws IllegalStateException {
      this.checkAllowedMethod(114936);
      return this.__WL_getPrimaryKey();
   }

   public void __WL_setPrimaryKey(Object var1) {
      this.primaryKey = var1;
   }

   public Object __WL_getPrimaryKey() {
      return this.primaryKey;
   }

   public EJBObject __WL_getEJBObject() {
      return this.ejbObject;
   }

   public EJBLocalObject __WL_getEJBLocalObject() {
      return this.ejbLocalObject;
   }
}
