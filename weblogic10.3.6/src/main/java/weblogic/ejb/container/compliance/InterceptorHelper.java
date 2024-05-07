package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.interceptor.InvocationContext;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.j2ee.descriptor.AroundInvokeBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;

public class InterceptorHelper {
   public static Set getAllBusinessMethods(Collection var0) {
      Iterator var1 = var0.iterator();
      HashSet var2 = new HashSet();

      while(true) {
         HashMap var4;
         Ejb3SessionBeanInfo var5;
         Set var9;
         Iterator var10;
         Class var11;
         Method[] var12;
         int var13;
         String var14;
         List var15;
         do {
            BeanInfo var3;
            do {
               if (!var1.hasNext()) {
                  return var2;
               }

               var3 = (BeanInfo)var1.next();
            } while(!(var3 instanceof Ejb3SessionBeanInfo));

            var4 = new HashMap();
            var5 = (Ejb3SessionBeanInfo)var3;
            Method[] var6 = var5.getBeanClass().getMethods();
            String[] var7 = new String[var6.length];

            for(int var8 = 0; var8 < var6.length; ++var8) {
               var7[var8] = DDUtils.getMethodSignature(var6[var8]);
               var4.put(var7[var8], var6[var8]);
            }

            var15 = Arrays.asList(var7);
            if (var5.hasBusinessRemotes()) {
               var9 = var5.getBusinessRemotes();
               var10 = var9.iterator();

               while(var10.hasNext()) {
                  var11 = (Class)var10.next();
                  var12 = var11.getMethods();

                  for(var13 = 0; var13 < var12.length; ++var13) {
                     var14 = DDUtils.getMethodSignature(var12[var13]);
                     if (var15.contains(var14)) {
                        var2.add(var4.get(var14));
                     }
                  }
               }
            }
         } while(!var5.hasBusinessLocals());

         var9 = var5.getBusinessLocals();
         var10 = var9.iterator();

         while(var10.hasNext()) {
            var11 = (Class)var10.next();
            var12 = var11.getMethods();

            for(var13 = 0; var13 < var12.length; ++var13) {
               var14 = DDUtils.getMethodSignature(var12[var13]);
               if (var15.contains(var14)) {
                  var2.add(var4.get(var14));
               }
            }
         }
      }
   }

   public static Set getAroundInvokeMethodinBean(ClassLoader var0, SessionBeanBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      AroundInvokeBean[] var3 = var1.getAroundInvokes();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getClassName();
            if (var5 == null) {
               var5 = var1.getEjbClass();
            }

            String var6 = var3[var4].getMethodName();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6, InvocationContext.class);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().AROUNDINVOKE_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getAroundInvokeMethodinBean(ClassLoader var0, MessageDrivenBeanBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      AroundInvokeBean[] var3 = var1.getAroundInvokes();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getClassName();
            String var6 = var3[var4].getMethodName();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6, InvocationContext.class);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().AROUNDINVOKE_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getPostConstructCallbackMethodinBean(ClassLoader var0, SessionBeanBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      LifecycleCallbackBean[] var3 = var1.getPostConstructs();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getLifecycleCallbackClass();
            String var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().POSTCONSTRUCT_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getPostConstructCallbackMethodinBean(ClassLoader var0, MessageDrivenBeanBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      LifecycleCallbackBean[] var3 = var1.getPostConstructs();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getLifecycleCallbackClass();
            String var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().POSTCONSTRUCT_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getPreDestroyCallbackMethodinBean(ClassLoader var0, SessionBeanBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      LifecycleCallbackBean[] var3 = var1.getPreDestroys();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getLifecycleCallbackClass();
            String var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().PREDESTROY_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getPreDestroyCallbackMethodinBean(ClassLoader var0, MessageDrivenBeanBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      LifecycleCallbackBean[] var3 = var1.getPreDestroys();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getLifecycleCallbackClass();
            String var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().PREDESTROY_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getPostActivateCallbackMethodinBean(ClassLoader var0, SessionBeanBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      LifecycleCallbackBean[] var3 = var1.getPostActivates();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getLifecycleCallbackClass();
            String var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().POSTACTIVE_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getPrePassivateCallbackMethodinBean(ClassLoader var0, SessionBeanBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      LifecycleCallbackBean[] var3 = var1.getPrePassivates();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getLifecycleCallbackClass();
            String var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().PREPASSIVATE_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getAroundInvokeMethodinInterceptor(ClassLoader var0, InterceptorBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      AroundInvokeBean[] var3 = var1.getAroundInvokes();
      if (var3 != null && var3.length >= 1) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getClassName();
            if (var5 == null) {
               var5 = var1.getInterceptorClass();
            }

            String var6 = var3[var4].getMethodName();

            try {
               Class var7 = var0.loadClass(var5);
               Method var8 = var7.getDeclaredMethod(var6, InvocationContext.class);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().AROUNDINVOKE_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }

   public static Set getLifecycleCallbackMethodinInterceptor(ClassLoader var0, InterceptorBean var1) throws ComplianceException {
      HashSet var2 = new HashSet();
      LifecycleCallbackBean[] var3 = var1.getPostConstructs();
      int var4;
      String var5;
      String var6;
      Class var7;
      Method var8;
      if (var3 != null && var3.length >= 1) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = var3[var4].getLifecycleCallbackClass();
            var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               var7 = var0.loadClass(var5);
               var8 = var7.getDeclaredMethod(var6, InvocationContext.class);
               var2.add(var8);
            } catch (ClassNotFoundException var15) {
               throw new RuntimeException(var15);
            } catch (NoSuchMethodException var16) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().INTERCEPTOR_POSTCONSTRUCT_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      var3 = var1.getPreDestroys();
      if (var3 != null && var3.length >= 1) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = var3[var4].getLifecycleCallbackClass();
            var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               var7 = var0.loadClass(var5);
               var8 = var7.getDeclaredMethod(var6, InvocationContext.class);
               var2.add(var8);
            } catch (ClassNotFoundException var13) {
               throw new RuntimeException(var13);
            } catch (NoSuchMethodException var14) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().INTERCEPTOR_PREDESTROY_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      var3 = var1.getPostActivates();
      if (var3 != null && var3.length >= 1) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = var3[var4].getLifecycleCallbackClass();
            var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               var7 = var0.loadClass(var5);
               var8 = var7.getDeclaredMethod(var6, InvocationContext.class);
               var2.add(var8);
            } catch (ClassNotFoundException var11) {
               throw new RuntimeException(var11);
            } catch (NoSuchMethodException var12) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().INTERCEPTOR_POSTACTIVE_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      var3 = var1.getPrePassivates();
      if (var3 != null && var3.length >= 1) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = var3[var4].getLifecycleCallbackClass();
            var6 = var3[var4].getLifecycleCallbackMethod();

            try {
               var7 = var0.loadClass(var5);
               var8 = var7.getDeclaredMethod(var6, InvocationContext.class);
               var2.add(var8);
            } catch (ClassNotFoundException var9) {
               throw new RuntimeException(var9);
            } catch (NoSuchMethodException var10) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().INTERCEPTOR_PREPASSIVATE_METHOD_WITH_INVALIDE_SIGNATURE(var6, var5));
            }
         }
      }

      return var2;
   }
}
