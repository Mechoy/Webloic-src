package weblogic.j2ee.injection;

import com.oracle.pitchfork.interfaces.MetadataParseException;
import com.oracle.pitchfork.interfaces.inject.ComponentContributor;
import com.oracle.pitchfork.interfaces.inject.EnricherI;
import com.oracle.pitchfork.interfaces.inject.InjectionI;
import com.oracle.pitchfork.interfaces.inject.Jsr250MetadataI;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import weblogic.deployment.descriptors.EnvironmentEntry;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.InjectionTargetBean;
import weblogic.j2ee.descriptor.J2eeClientEnvironmentBean;
import weblogic.j2ee.descriptor.MessageDestinationRefBean;
import weblogic.j2ee.descriptor.PersistenceUnitRefBean;
import weblogic.j2ee.descriptor.ResourceEnvRefBean;
import weblogic.j2ee.descriptor.ResourceRefBean;
import weblogic.j2ee.descriptor.ServiceRefBean;

public abstract class BaseComponentContributor implements ComponentContributor {
   protected static final DebugLogger debugLogger;
   protected ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
   protected PitchforkContext pitchforkContext;

   public BaseComponentContributor(PitchforkContext var1) {
      this.pitchforkContext = var1;
   }

   public Jsr250MetadataI buildJsr250MetaData(EnricherI var1, String var2, String var3) {
      Class var4 = null;
      if (var3 != null) {
         var4 = this.loadClass(var3, this.classLoader);
      }

      return this.newJsr250Metadata(var2, var4, var1.getDeploymentUnitMetadata());
   }

   protected void buildInjectionMetadata(Jsr250MetadataI var1, J2eeClientEnvironmentBean var2) {
      this.debug("Convert environmentGroupBean to jsr250Metadata for : " + var2);
      ResourceEnvRefBean[] var3 = var2.getResourceEnvRefs();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         ResourceEnvRefBean var6 = var3[var5];
         this.addResourceEnvRef(var6, var1);
      }

      EnvEntryBean[] var7 = var2.getEnvEntries();
      var4 = var7.length;

      for(var5 = 0; var5 < var4; ++var5) {
         EnvEntryBean var13 = var7[var5];
         this.addEnvEntry(var13, var1);
      }

      EjbRefBean[] var8 = var2.getEjbRefs();
      var4 = var8.length;

      for(var5 = 0; var5 < var4; ++var5) {
         EjbRefBean var14 = var8[var5];
         this.addEjbRef(var14, var1);
      }

      ServiceRefBean[] var9 = var2.getServiceRefs();
      var4 = var9.length;

      for(var5 = 0; var5 < var4; ++var5) {
         ServiceRefBean var15 = var9[var5];
         this.addServiceRef(var15, var1);
      }

      ResourceRefBean[] var10 = var2.getResourceRefs();
      var4 = var10.length;

      for(var5 = 0; var5 < var4; ++var5) {
         ResourceRefBean var16 = var10[var5];
         this.addResourceRef(var16, var1);
      }

      MessageDestinationRefBean[] var11 = var2.getMessageDestinationRefs();
      var4 = var11.length;

      for(var5 = 0; var5 < var4; ++var5) {
         MessageDestinationRefBean var17 = var11[var5];
         this.addMessageDestinationRef(var17, var1);
      }

      PersistenceUnitRefBean[] var12 = var2.getPersistenceUnitRefs();
      var4 = var12.length;

      for(var5 = 0; var5 < var4; ++var5) {
         PersistenceUnitRefBean var18 = var12[var5];
         this.addPersistenceUnitRef(var18, var1);
      }

   }

   protected Set getInjectableTargetClasses(J2eeClientEnvironmentBean var1) {
      HashSet var2 = new HashSet();
      ResourceEnvRefBean[] var3 = var1.getResourceEnvRefs();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         ResourceEnvRefBean var6 = var3[var5];
         var2.addAll(this.getInjectableTargetClasses(var6.getInjectionTargets()));
      }

      EnvEntryBean[] var7 = var1.getEnvEntries();
      var4 = var7.length;

      for(var5 = 0; var5 < var4; ++var5) {
         EnvEntryBean var13 = var7[var5];
         var2.addAll(this.getInjectableTargetClasses(var13.getInjectionTargets()));
      }

      EjbRefBean[] var8 = var1.getEjbRefs();
      var4 = var8.length;

      for(var5 = 0; var5 < var4; ++var5) {
         EjbRefBean var14 = var8[var5];
         var2.addAll(this.getInjectableTargetClasses(var14.getInjectionTargets()));
      }

      ServiceRefBean[] var9 = var1.getServiceRefs();
      var4 = var9.length;

      for(var5 = 0; var5 < var4; ++var5) {
         ServiceRefBean var15 = var9[var5];
         var2.addAll(this.getInjectableTargetClasses(var15.getInjectionTargets()));
      }

      ResourceRefBean[] var10 = var1.getResourceRefs();
      var4 = var10.length;

      for(var5 = 0; var5 < var4; ++var5) {
         ResourceRefBean var16 = var10[var5];
         var2.addAll(this.getInjectableTargetClasses(var16.getInjectionTargets()));
      }

      MessageDestinationRefBean[] var11 = var1.getMessageDestinationRefs();
      var4 = var11.length;

      for(var5 = 0; var5 < var4; ++var5) {
         MessageDestinationRefBean var17 = var11[var5];
         var2.addAll(this.getInjectableTargetClasses(var17.getInjectionTargets()));
      }

      PersistenceUnitRefBean[] var12 = var1.getPersistenceUnitRefs();
      var4 = var12.length;

      for(var5 = 0; var5 < var4; ++var5) {
         PersistenceUnitRefBean var18 = var12[var5];
         var2.addAll(this.getInjectableTargetClasses(var18.getInjectionTargets()));
      }

      return var2;
   }

   protected Set getInjectableTargetClasses(InjectionTargetBean[] var1) {
      HashSet var2 = new HashSet();
      InjectionTargetBean[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         InjectionTargetBean var6 = var3[var5];
         var2.add(var6.getInjectionTargetClass());
      }

      return var2;
   }

   protected void addEnvEntry(EnvEntryBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getEnvEntryName();
      String var4 = var1.getEnvEntryValue();
      String var5 = var1.getEnvEntryType();
      if (var4 != null) {
         InjectionTargetBean[] var6 = var1.getInjectionTargets();
         Object var7 = null;
         EnvironmentEntry var8 = new EnvironmentEntry();
         var8.setValue(var5, var4);

         try {
            var7 = var8.getValue();
         } catch (Exception var12) {
         }

         List var9 = this.parseInjectionTarget(var2, var6, var5, var3);
         Iterator var10 = var9.iterator();

         while(var10.hasNext()) {
            InjectionI var11 = (InjectionI)var10.next();
            var11.setValue(var7);
         }

         this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var9);
      }
   }

   protected void addEjbRef(EjbRefBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getEjbRefName();
      String var4 = var1.getEjbRefType();
      String var5 = var1.getHome();
      if (var5 == null) {
         var5 = var1.getRemote();
      }

      InjectionTargetBean[] var6 = var1.getInjectionTargets();
      List var7 = this.parseInjectionTarget(var2, var6, var5, var3);
      this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var7);
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

   protected void addServiceRef(ServiceRefBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getServiceRefName();
      String var4 = var1.getServiceInterface();
      String var5 = var1.getServiceRefType();
      InjectionTargetBean[] var6 = var1.getInjectionTargets();
      List var7 = this.parseInjectionTarget(var2, var6, var5, var3);
      this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var7);
   }

   protected void addResourceRef(ResourceRefBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getResRefName();
      String var4 = var1.getResType();
      InjectionTargetBean[] var5 = var1.getInjectionTargets();
      List var6 = this.parseInjectionTarget(var2, var5, var4, var3);
      this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var6);
   }

   protected void addResourceEnvRef(ResourceEnvRefBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getResourceEnvRefName();
      String var4 = var1.getResourceEnvRefType();
      InjectionTargetBean[] var5 = var1.getInjectionTargets();
      List var6 = this.parseInjectionTarget(var2, var5, var4, var3);
      this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var6);
   }

   protected void addMessageDestinationRef(MessageDestinationRefBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getMessageDestinationRefName();
      String var4 = var1.getMessageDestinationType();
      InjectionTargetBean[] var5 = var1.getInjectionTargets();
      List var6 = this.parseInjectionTarget(var2, var5, var4, var3);
      this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var6);
   }

   protected void addPersistenceUnitRef(PersistenceUnitRefBean var1, Jsr250MetadataI var2) {
      String var3 = var1.getPersistenceUnitRefName();
      InjectionTargetBean[] var4 = var1.getInjectionTargets();
      List var5 = this.parseInjectionTarget(var2, var4, EntityManagerFactory.class.getName(), var3);
      this.insertOrOverwriteInjectionStrategy(var2.getInjections(), var5);
   }

   protected List<InjectionI> parseInjectionTarget(Jsr250MetadataI var1, InjectionTargetBean[] var2, String var3, String var4) {
      ArrayList var5 = new ArrayList();
      if (var2.length == 0) {
         return var5;
      } else {
         Class var6 = this.loadClass(var3, this.classLoader);
         InjectionTargetBean[] var7 = var2;
         int var8 = var2.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            InjectionTargetBean var10 = var7[var9];
            String var11 = var10.getInjectionTargetClass();
            String var12 = var10.getInjectionTargetName();
            Class var13 = this.loadClass(var11, this.classLoader);
            Class var14 = var1.getComponentClass();
            if (!var13.isAssignableFrom(var14)) {
               this.debug("Skipping injection for: targetClassName " + var11 + "; componentName " + var1.getComponentName());
            } else {
               Class var15 = ClassUtils.getPrimitiveClass(var6);
               if (null == var15) {
                  if (!this.findTargetMethodOrField(var5, var13, var12, var6, var4)) {
                     throw new IllegalArgumentException("No setter method or field named \"" + var12 + "\" found in inject target class " + var13.getName());
                  }
               } else if (!this.findTargetMethodOrField(var5, var13, var12, var6, var4) && !this.findTargetMethodOrField(var5, var13, var12, var15, var4)) {
                  throw new IllegalArgumentException("No setter method or field named \"" + var12 + "\" found in inject target class " + var13.getName());
               }
            }
         }

         return var5;
      }
   }

   private boolean findTargetMethodOrField(List<InjectionI> var1, Class var2, String var3, Class var4, String var5) {
      if (var1 == null) {
         return false;
      } else {
         try {
            Method var6 = var2.getMethod(MethodUtils.setMethodName(var3), var4);
            InjectionI var11 = this.pitchforkContext.getPitchforkUtils().createMethodInjection(var6, var5, var4);
            this.debug("+++++adding method injection " + var11 + " for " + var3);
            var1.add(var11);
         } catch (NoSuchMethodException var10) {
            try {
               Field var7 = var2.getDeclaredField(var3);
               InjectionI var8 = this.pitchforkContext.getPitchforkUtils().createFieldInjection(var7, var5, var4);
               this.debug("+++++adding field injection " + var8 + " for " + var3);
               var1.add(var8);
            } catch (NoSuchFieldException var9) {
               return false;
            }
         }

         return true;
      }
   }

   protected void insertOrOverwriteInjectionStrategy(List<InjectionI<?>> var1, List<InjectionI> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         InjectionI var4 = (InjectionI)var3.next();
         Member var5 = var4.getMember();
         Class var6 = var4.getType();
         boolean var7 = false;
         Iterator var8 = var1.iterator();

         while(var8.hasNext()) {
            InjectionI var9 = (InjectionI)var8.next();
            Member var10 = var9.getMember();
            Class var11 = var9.getType();
            if (var9.getName().equals(var4.getName()) && var10.equals(var5)) {
               var1.remove(var9);
               break;
            }

            if (var11.getName().equals(var6.getName()) && var10.getName().equals(var5.getName())) {
               if (!Modifier.isPrivate(var10.getModifiers()) && var10.getDeclaringClass().isAssignableFrom(var5.getDeclaringClass())) {
                  var1.remove(var9);
                  break;
               }

               if (!Modifier.isPrivate(var5.getModifiers()) && var5.getDeclaringClass().isAssignableFrom(var10.getDeclaringClass())) {
                  var7 = true;
                  break;
               }
            }
         }

         if (!var7) {
            var1.add(var4);
         }
      }

   }

   protected Class loadClass(String var1, ClassLoader var2) {
      try {
         return var2.loadClass(var1);
      } catch (ClassNotFoundException var4) {
         throw new MetadataParseException("Can't find class:" + var1, var4);
      }
   }

   protected Class forName(String var1, ClassLoader var2) {
      return this.pitchforkContext.getPitchforkUtils().forName(var1, var2);
   }

   protected void debug(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("[BaseComponentContributor] " + var1);
      }

   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
   }
}
