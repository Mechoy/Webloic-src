package weblogic.ejb.container.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import javax.ejb.EntityBean;
import javax.transaction.Transaction;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.persistence.spi.PersistenceManager;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.transaction.TxHelper;
import weblogic.utils.AssertionError;

public final class BeanManagedPersistenceManager implements PersistenceManager {
   BaseEntityManager beanManager = null;

   public void setup(BeanManager var1) throws InternalException {
      this.beanManager = (BaseEntityManager)var1;
   }

   public Object findByPrimaryKey(EntityBean var1, Method var2, Object var3) throws InternalException {
      try {
         return var2.invoke(var1, var3);
      } catch (IllegalAccessException var6) {
         EJBRuntimeUtils.throwInternalException("Exception in ejbFindByPrimaryKey", var6);
         throw new AssertionError("cannot reach");
      } catch (InvocationTargetException var7) {
         Throwable var5 = var7.getTargetException();
         EJBRuntimeUtils.throwInternalException("Exception in ejbFindByPrimaryKey", var5);
         throw new AssertionError("cannot reach");
      }
   }

   public EntityBean findByPrimaryKeyLoadBean(EntityBean var1, Method var2, Object var3) throws InternalException {
      throw new InternalException("NYI");
   }

   public Object scalarFinder(EntityBean var1, Method var2, Object[] var3) throws InternalException {
      try {
         Transaction var4 = TxHelper.getTransactionManager().getTransaction();
         this.beanManager.flushModifiedBeans(var4);
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new InternalException(var9.getMessage());
      }

      try {
         return var2.invoke(var1, var3);
      } catch (IllegalAccessException var6) {
         EJBRuntimeUtils.throwInternalException("Exception in " + var2.getName(), var6);
         throw new AssertionError("cannot reach");
      } catch (InvocationTargetException var7) {
         Throwable var5 = var7.getTargetException();
         EJBRuntimeUtils.throwInternalException("Exception in " + var2.getName(), var5);
         throw new AssertionError("cannot reach");
      }
   }

   public Map scalarFinderLoadBean(EntityBean var1, Method var2, Object[] var3) throws InternalException {
      throw new InternalException("NYI");
   }

   public Enumeration enumFinder(EntityBean var1, Method var2, Object[] var3) throws InternalException {
      try {
         Transaction var4 = TxHelper.getTransactionManager().getTransaction();
         this.beanManager.flushModifiedBeans(var4);
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new InternalException(var9.getMessage());
      }

      try {
         return (Enumeration)var2.invoke(var1, var3);
      } catch (IllegalAccessException var6) {
         EJBRuntimeUtils.throwInternalException("Exception in " + var2.getName(), var6);
         throw new AssertionError("cannot reach");
      } catch (InvocationTargetException var7) {
         Throwable var5 = var7.getTargetException();
         EJBRuntimeUtils.throwInternalException("Exception in " + var2.getName(), var5);
         throw new AssertionError("cannot reach");
      }
   }

   public Collection collectionFinder(EntityBean var1, Method var2, Object[] var3) throws InternalException {
      try {
         Transaction var4 = TxHelper.getTransactionManager().getTransaction();
         this.beanManager.flushModifiedBeans(var4);
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new InternalException(var9.getMessage());
      }

      try {
         return (Collection)var2.invoke(var1, var3);
      } catch (IllegalAccessException var6) {
         EJBRuntimeUtils.throwInternalException("Exception in " + var2.getName(), var6);
         throw new AssertionError("cannot reach");
      } catch (InvocationTargetException var7) {
         Throwable var5 = var7.getTargetException();
         EJBRuntimeUtils.throwInternalException("Exception in " + var2.getName(), var5);
         throw new AssertionError("cannot reach");
      }
   }

   public Map collectionFinderLoadBean(EntityBean var1, Method var2, Object[] var3) throws InternalException {
      throw new InternalException("NYI");
   }

   public void loadBeanFromRS(EntityBean var1, RSInfo var2) throws InternalException {
      throw new InternalException("NYI");
   }

   public void updateClassLoader(ClassLoader var1) {
   }
}
