package weblogic.ejb20.internal;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb20.interfaces.LocalHandle;
import weblogic.utils.AssertionError;

public final class LocalHandleImpl implements LocalHandle, Serializable {
   private static final long serialVersionUID = 3817127104258844908L;
   private transient EJBLocalObject ejbObject = null;
   private EJBLocalHome home = null;
   private Object primaryKey = null;
   private static final boolean debug = false;

   public LocalHandleImpl() {
   }

   public LocalHandleImpl(EJBLocalObject var1) {
      this.ejbObject = var1;
      this.home = var1.getEJBLocalHome();
      this.primaryKey = null;
   }

   public LocalHandleImpl(EJBLocalObject var1, Object var2) {
      this.ejbObject = var1;
      this.home = var1.getEJBLocalHome();
      this.primaryKey = var2;
   }

   private EJBLocalObject allocateELO(Class[] var1, Object[] var2) {
      Class var3 = this.home.getClass();

      try {
         Method var4 = var3.getMethod("allocateELO", var1);
         return (EJBLocalObject)var4.invoke(this.home, var2);
      } catch (NoSuchMethodException var6) {
         EJBRuntimeUtils.throwEJBException("Exception re-establishing handle: " + var3 + " doesn't define allocatELO() method", var6);
         throw new AssertionError("cannot reach here");
      } catch (InvocationTargetException var7) {
         Throwable var5 = var7.getTargetException();
         EJBRuntimeUtils.throwEJBException("Exception re-establishing handle", var5);
         throw new AssertionError("cannot reach here");
      } catch (IllegalAccessException var8) {
         EJBRuntimeUtils.throwEJBException("Exception re-establishing handle", var8);
         throw new AssertionError("cannot reach here");
      }
   }

   public EJBLocalObject getEJBLocalObject() {
      if (this.ejbObject != null) {
         return this.ejbObject;
      } else {
         return this.primaryKey == null ? this.allocateELO((Class[])null, (Object[])null) : this.allocateELO(new Class[]{Object.class}, new Object[]{this.primaryKey});
      }
   }
}
