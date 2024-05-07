package weblogic.ejb.container.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import weblogic.ejb.container.interfaces.Ejb3LocalHome;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionHome;
import weblogic.ejb20.interfaces.LocalHomeHandle;
import weblogic.utils.AssertionError;

public class StatelessEJBLocalHomeImpl extends StatelessEJBLocalHome implements Ejb3SessionHome, Ejb3LocalHome {
   private final Map ifaceToImpl = new HashMap();

   public StatelessEJBLocalHomeImpl() {
      super((Class)null);
   }

   public StatelessEJBLocalHomeImpl(Class var1) {
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
         StatelessLocalObject var5 = null;

         try {
            var5 = this.allocateBI(var4);
            this.ifaceToImpl.put(var3, var5);
         } catch (Exception var7) {
            throw new AssertionError(var7);
         }
      }

   }

   public Object getBindableImpl(Class var1) {
      return this.ifaceToImpl.get(var1);
   }

   public Object getBusinessImpl(Object var1, Class var2) {
      return this.ifaceToImpl.get(var2);
   }

   private StatelessLocalObject allocateBI(Class var1) {
      StatelessLocalObject var2 = null;

      try {
         var2 = (StatelessLocalObject)var1.newInstance();
         var2.setEJBLocalHome(this);
         var2.setBeanManager(this.getBeanManager());
         var2.setBeanInfo(this.getBeanInfo());
         return var2;
      } catch (Exception var4) {
         throw new AssertionError(var4);
      }
   }
}
