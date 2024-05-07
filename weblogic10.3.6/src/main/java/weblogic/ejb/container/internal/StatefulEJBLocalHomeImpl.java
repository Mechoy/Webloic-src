package weblogic.ejb.container.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.Ejb3LocalHome;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3StatefulHome;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.ejb20.interfaces.LocalHomeHandle;
import weblogic.rmi.extensions.activation.Activator;

public class StatefulEJBLocalHomeImpl extends StatefulEJBLocalHome implements Ejb3StatefulHome, Ejb3LocalHome {
   private final Map opaqueReferenceMap = new HashMap();

   public StatefulEJBLocalHomeImpl() {
      super((Class)null);
   }

   public StatefulEJBLocalHomeImpl(Class var1) {
      super(var1);
   }

   public LocalHomeHandle getLocalHomeHandle() {
      throw new IllegalStateException();
   }

   public void remove(Object var1) throws EJBException, RemoveException {
      throw new IllegalStateException();
   }

   public void prepare() {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.beanInfo;
      Iterator var2 = var1.getBusinessLocals().iterator();

      while(var2.hasNext()) {
         Class var3 = (Class)var2.next();
         Class var4 = var1.getGeneratedLocalBusinessImplClass(var3);
         OpaqueReferenceImpl var5 = new OpaqueReferenceImpl(this, var4, (Activator)null, var3, (Class)null);
         this.opaqueReferenceMap.put(var3.getName(), var5);
      }

   }

   public Object getBindableImpl(Class var1) {
      return this.opaqueReferenceMap.get(var1.getName());
   }

   public Object getBusinessImpl(Object var1, Class var2) {
      Ejb3SessionBeanInfo var3 = (Ejb3SessionBeanInfo)this.beanInfo;
      Class var4 = var3.getGeneratedLocalBusinessImplClass(var2);
      return var4 == null ? null : this.getBusinessImpl((Object)var1, (Class)var4, (Activator)null, (Class)null);
   }

   public Object getBusinessImpl(String var1) {
      throw new AssertionError("This method not implemented for Local homes");
   }

   public Object getBusinessImpl(Class var1, Activator var2, Class var3, Class var4) {
      if (var4 != null) {
         throw new AssertionError();
      } else {
         try {
            Object var5 = ((StatefulSessionManager)this.beanManager).createBean();
            return this.getBusinessImpl(var5, var1, var2, var3);
         } catch (InternalException var7) {
            EJBException var6 = new EJBException();
            var6.initCause(var7.detail);
            throw var6;
         }
      }
   }

   private Object getBusinessImpl(Object var1, Class var2, Activator var3, Class var4) {
      if (var3 != null) {
         throw new AssertionError();
      } else {
         StatefulLocalObject var5 = null;

         try {
            var5 = (StatefulLocalObject)var2.newInstance();
         } catch (InstantiationException var7) {
            throw new AssertionError(var7);
         } catch (IllegalAccessException var8) {
            throw new AssertionError(var8);
         }

         var5.setEJBLocalHome(this);
         var5.setBeanManager(this.getBeanManager());
         var5.setBeanInfo(this.getBeanInfo());
         var5.setPrimaryKey(var1);
         return var5;
      }
   }
}
