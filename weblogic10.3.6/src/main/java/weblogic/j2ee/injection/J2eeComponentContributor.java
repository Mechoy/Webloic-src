package weblogic.j2ee.injection;

import com.oracle.pitchfork.interfaces.MetadataParseException;
import com.oracle.pitchfork.interfaces.PitchforkUtils;
import com.oracle.pitchfork.interfaces.inject.EnricherI;
import com.oracle.pitchfork.interfaces.inject.InjectionI;
import com.oracle.pitchfork.interfaces.inject.Jsr250MetadataI;
import com.oracle.pitchfork.interfaces.inject.LifecycleEvent;
import com.oracle.pitchfork.interfaces.intercept.InterceptorMetadataI;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.InjectionTargetBean;
import weblogic.j2ee.descriptor.J2eeClientEnvironmentBean;
import weblogic.j2ee.descriptor.J2eeEnvironmentBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.j2ee.extensions.ExtensionManager;
import weblogic.j2ee.extensions.InjectionExtension;

public abstract class J2eeComponentContributor extends BaseComponentContributor {
   protected static final DebugLogger debugLogger;

   public J2eeComponentContributor(PitchforkContext var1) {
      super(var1);
   }

   public void contribute(EnricherI var1, String var2, String var3, J2eeEnvironmentBean var4) {
      Jsr250MetadataI var5 = this.buildJsr250MetaData(var1, var2, var3);
      this.buildInjectionMetadata(var5, var4);
      this.contribute(var5, var4);
      this.addLifecycleMethods(var5, var4);
      var1.attach(var5, false);
      this.updateMetadataUsingExtensions(var5, var2, var3);
   }

   private void updateMetadataUsingExtensions(Jsr250MetadataI var1, String var2, String var3) {
      ArrayList var4 = new ArrayList();
      Iterator var5 = var1.getInjections().iterator();

      while(true) {
         InjectionI var6;
         String var7;
         String var8;
         InjectionExtension var9;
         do {
            if (!var5.hasNext()) {
               if (var4.size() > 0) {
                  var1.getInjections().addAll(var4);
               }

               return;
            }

            var6 = (InjectionI)var5.next();
            var7 = var6.getInfo().getName();
            var8 = var6.getInfo().getType().getName();
            var9 = ExtensionManager.instance.getFirstMatchingExtension(var8, var7);
         } while(var9 == null);

         if (var7 != null && var7.length() != 0) {
            var7 = var9.getName(var8, var7);
         } else {
            var7 = var9.getName(var8);
         }

         InjectionI var10 = null;
         PitchforkUtils var11 = this.pitchforkContext.getPitchforkUtils();
         Member var12 = var6.getMember();
         if (var12 instanceof Field) {
            var10 = var11.createFieldInjection((Field)var12, var7, var6.getInfo().getType());
         } else if (var12 instanceof Method) {
            var10 = var11.createMethodInjection((Method)var12, var7, var6.getInfo().getType());
         }

         if (var10 != null) {
            var4.add(var10);
            var5.remove();
         }
      }
   }

   protected abstract void contribute(Jsr250MetadataI var1, J2eeEnvironmentBean var2);

   protected void addLifecycleMethods(Jsr250MetadataI var1, J2eeEnvironmentBean var2) {
      LifecycleCallbackBean[] var3 = var2.getPostConstructs();
      int var4 = var3.length;

      int var5;
      LifecycleCallbackBean var6;
      for(var5 = 0; var5 < var4; ++var5) {
         var6 = var3[var5];
         this.addLifecycleMethods(var1, var6, LifecycleEvent.POST_CONSTRUCT);
      }

      var3 = var2.getPreDestroys();
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         var6 = var3[var5];
         this.addLifecycleMethods(var1, var6, LifecycleEvent.PRE_DESTROY);
      }

   }

   protected void addLifecycleMethods(Jsr250MetadataI var1, LifecycleCallbackBean var2, LifecycleEvent var3) {
      Class var4 = this.loadClass(var2.getLifecycleCallbackClass(), this.classLoader);
      if (var4.isAssignableFrom(var1.getComponentClass())) {
         Method var5;
         if (var1 instanceof InterceptorMetadataI) {
            var5 = this.getDeclaredMethod(var4, var2.getLifecycleCallbackMethod(), InvocationContext.class);
            ((InterceptorMetadataI)var1).registerLifecycleEventListenerMethod(var3, var5);
         } else {
            var5 = this.getDeclaredMethod(var4, var2.getLifecycleCallbackMethod());
            var1.registerLifecycleEventCallbackMethod(var3, var5);
         }

         this.debug("+++++adding lifecycleMethod=" + var5.getName() + " for annotation " + var3);
      }
   }

   protected void buildInjectionMetadata(Jsr250MetadataI var1, J2eeEnvironmentBean var2) {
      super.buildInjectionMetadata(var1, var2);
      PersistenceContextRefBean[] var3 = var2.getPersistenceContextRefs();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         PersistenceContextRefBean var6 = var3[var5];
         this.addPersistenceContextRef(var6, var1);
      }

      EjbLocalRefBean[] var7 = var2.getEjbLocalRefs();
      var4 = var7.length;

      for(var5 = 0; var5 < var4; ++var5) {
         EjbLocalRefBean var8 = var7[var5];
         this.addLocalEjbRef(var8, var1);
      }

   }

   protected Set getInjectableTargetClasses(J2eeEnvironmentBean var1) {
      HashSet var2 = new HashSet();
      var2.addAll(super.getInjectableTargetClasses((J2eeClientEnvironmentBean)var1));
      LifecycleCallbackBean[] var3 = var1.getPostConstructs();
      int var4 = var3.length;

      int var5;
      LifecycleCallbackBean var6;
      for(var5 = 0; var5 < var4; ++var5) {
         var6 = var3[var5];
         var2.add(var6.getLifecycleCallbackClass());
      }

      var3 = var1.getPreDestroys();
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         var6 = var3[var5];
         var2.add(var6.getLifecycleCallbackClass());
      }

      PersistenceContextRefBean[] var7 = var1.getPersistenceContextRefs();
      var4 = var7.length;

      for(var5 = 0; var5 < var4; ++var5) {
         PersistenceContextRefBean var9 = var7[var5];
         var2.addAll(this.getInjectableTargetClasses(var9.getInjectionTargets()));
      }

      EjbLocalRefBean[] var8 = var1.getEjbLocalRefs();
      var4 = var8.length;

      for(var5 = 0; var5 < var4; ++var5) {
         EjbLocalRefBean var10 = var8[var5];
         var2.addAll(this.getInjectableTargetClasses(var10.getInjectionTargets()));
      }

      return var2;
   }

   protected void addPersistenceContextRef(PersistenceContextRefBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getPersistenceContextRefName();
      InjectionTargetBean[] var4 = var1.getInjectionTargets();
      List var5 = this.parseInjectionTarget(var2, var4, EntityManager.class.getName(), var3);
      this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var5);
   }

   protected void addLocalEjbRef(EjbLocalRefBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getEjbRefName();
      String var4 = var1.getEjbRefType();
      String var5 = var1.getLocalHome();
      if (var5 == null) {
         var5 = var1.getLocal();
      }

      InjectionTargetBean[] var6 = var1.getInjectionTargets();
      List var7 = this.parseInjectionTarget(var2, var6, var5, var3);
      this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var7);
   }

   protected Method getDeclaredMethod(Class var1, String var2, Class... var3) {
      Method var4 = null;

      try {
         var4 = var1.getDeclaredMethod(var2, var3);
         return var4;
      } catch (Exception var6) {
         throw new MetadataParseException("Can't get the method " + var2 + " on class " + var1.getName(), var6);
      }
   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
   }
}
