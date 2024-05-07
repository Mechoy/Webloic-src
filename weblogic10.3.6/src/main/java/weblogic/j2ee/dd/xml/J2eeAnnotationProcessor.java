package weblogic.j2ee.dd.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceProperty;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.InjectionTargetBean;
import weblogic.j2ee.descriptor.J2eeClientEnvironmentBean;
import weblogic.j2ee.descriptor.J2eeEnvironmentBean;
import weblogic.j2ee.descriptor.JavaEEPropertyBean;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.javaee.EJBReference;

public class J2eeAnnotationProcessor extends BaseJ2eeAnnotationProcessor {
   public void processJ2eeAnnotations(Class var1, J2eeClientEnvironmentBean var2) {
      Iterator var3 = this.getClassPersistenceContextRefs(var1).iterator();

      while(var3.hasNext()) {
         PersistenceContext var4 = (PersistenceContext)var3.next();
         this.addPersistenceContextRef(var4.name(), var4, (J2eeEnvironmentBean)var2, false);
      }

      super.processJ2eeAnnotations(var1, var2);
   }

   protected void processField(Field var1, J2eeClientEnvironmentBean var2) {
      if (var1.isAnnotationPresent(PersistenceContext.class)) {
         this.addPersistenceContextRef(var1, (J2eeEnvironmentBean)var2);
      } else {
         super.processField(var1, var2);
      }

   }

   protected void processMethod(Method var1, J2eeClientEnvironmentBean var2) {
      if (var1.isAnnotationPresent(PersistenceContext.class)) {
         this.addPersistenceContextRef(var1, (J2eeEnvironmentBean)var2);
      } else {
         super.processMethod(var1, var2);
      }

   }

   private void addPersistenceContextRef(Field var1, J2eeEnvironmentBean var2) {
      PersistenceContext var3 = (PersistenceContext)var1.getAnnotation(PersistenceContext.class);
      String var4 = this.getCompEnvJndiName(var3.name(), var1);
      if (this.findInjectionTargetFromPersistenceContextRef(var1, var4, var2) == null) {
         this.addInjectionTarget(var1, this.addPersistenceContextRef(var4, var3, var2, true));
      }

   }

   private void addPersistenceContextRef(Method var1, J2eeEnvironmentBean var2) {
      PersistenceContext var3 = (PersistenceContext)var1.getAnnotation(PersistenceContext.class);
      String var4 = this.getCompEnvJndiName(var3.name(), var1);
      if (this.findInjectionTargetFromPersistenceContextRef(var1, var4, var2) == null) {
         this.addInjectionTarget(var1, this.addPersistenceContextRef(var4, var3, var2, true));
      }

   }

   private InjectionTargetBean addPersistenceContextRef(String var1, PersistenceContext var2, J2eeEnvironmentBean var3, boolean var4) {
      PersistenceContextRefBean var5 = null;
      PersistenceContextRefBean[] var6 = var3.getPersistenceContextRefs();
      int var7 = var6.length;

      int var8;
      for(var8 = 0; var8 < var7; ++var8) {
         PersistenceContextRefBean var9 = var6[var8];
         if (var9.getPersistenceContextRefName().equals(var1)) {
            var5 = var9;
            break;
         }
      }

      if (var5 == null) {
         var5 = var3.createPersistenceContextRef();
         var5.setPersistenceContextRefName(var1);
      }

      if (!this.isSet("PersistenceUnitName", var5) && var2.unitName().length() > 0) {
         var5.setPersistenceUnitName(var2.unitName());
      }

      if (!this.isSet("PersistenceContextType", var5)) {
         if (var2.type() == PersistenceContextType.TRANSACTION) {
            var5.setPersistenceContextType("Transaction");
         } else {
            var5.setPersistenceContextType("Extended");
         }
      }

      if (!this.isSet("PersistenceProperties", var5)) {
         PersistenceProperty[] var11 = var2.properties();
         var7 = var11.length;

         for(var8 = 0; var8 < var7; ++var8) {
            PersistenceProperty var12 = var11[var8];
            JavaEEPropertyBean var10 = var5.createPersistenceProperty();
            var10.setName(var12.name());
            var10.setValue(var10.getValue());
         }
      }

      return var4 ? var5.createInjectionTarget() : null;
   }

   protected InjectionTargetBean findInjectionTargetFromEjbRef(String var1, String var2, String var3, Class var4, J2eeClientEnvironmentBean var5) {
      EjbRefBean var6 = this.findEjbRef(var3, var5);
      if (var6 != null) {
         InjectionTargetBean var7 = this.findInjectionTargetInArray(var1, var2, var6.getInjectionTargets());
         if (var7 != null) {
            return var7;
         }
      }

      EjbLocalRefBean var9 = this.findEjbLocalRef(var3, (J2eeEnvironmentBean)var5);
      if (var9 != null) {
         InjectionTargetBean var8 = this.findInjectionTargetInArray(var1, var2, var9.getInjectionTargets());
         if (var8 != null) {
            return var8;
         }
      }

      return null;
   }

   protected InjectionTargetBean findInjectionTargetFromPersistenceContextRef(Method var1, String var2, J2eeEnvironmentBean var3) {
      return this.findInjectionTargetFromPersistenceContextRef(var1.getDeclaringClass().getName(), this.getPropertyName(var1), var2, var3);
   }

   protected InjectionTargetBean findInjectionTargetFromPersistenceContextRef(Field var1, String var2, J2eeEnvironmentBean var3) {
      return this.findInjectionTargetFromPersistenceContextRef(var1.getDeclaringClass().getName(), var1.getName(), var2, var3);
   }

   protected InjectionTargetBean findInjectionTargetFromPersistenceContextRef(String var1, String var2, String var3, J2eeEnvironmentBean var4) {
      PersistenceContextRefBean var5 = null;
      PersistenceContextRefBean[] var6 = var4.getPersistenceContextRefs();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         PersistenceContextRefBean var9 = var6[var8];
         if (var9.getPersistenceContextRefName().equals(var3)) {
            var5 = var9;
            break;
         }
      }

      return var5 != null ? this.findInjectionTargetInArray(var1, var2, var5.getInjectionTargets()) : null;
   }

   protected InjectionTargetBean addEjbRef(String var1, Class var2, EJBReference var3, J2eeClientEnvironmentBean var4, boolean var5) {
      J2eeEnvironmentBean var6 = (J2eeEnvironmentBean)var4;
      EjbRefBean var7 = this.findEjbRef(var1, var6);
      if (var7 != null) {
         return this.addEJBRemoteRef(var1, var2, var3, var6, var7, var5);
      } else {
         EjbLocalRefBean var8 = this.findEjbLocalRef(var1, var6);
         if (var8 != null) {
            return this.addEJBLocalRef(var1, var2, var3, var6, var8, var5);
         } else if (var2 == Object.class) {
            this.addBeanInterfaceNotSetError(var4);
            return null;
         } else {
            return this.addEJBLocalRef(var1, var2, (EJBReference)var3, var6, (EjbLocalRefBean)null, var5);
         }
      }
   }

   protected InjectionTargetBean addEjbRef(String var1, Class var2, EJB var3, J2eeClientEnvironmentBean var4, boolean var5) {
      J2eeEnvironmentBean var6 = (J2eeEnvironmentBean)var4;
      EjbRefBean var7 = this.findEjbRef(var1, var6);
      if (var7 != null) {
         return this.addEJBRemoteRef(var1, var2, var3, var6, var7, var5);
      } else {
         EjbLocalRefBean var8 = this.findEjbLocalRef(var1, var6);
         if (var8 != null) {
            return this.addEJBLocalRef(var1, var2, var3, var6, var8, var5);
         } else if (EJBHome.class.isAssignableFrom(var2)) {
            return this.addEJBRemoteRef(var1, var2, var3, var6, (EjbRefBean)null, var5);
         } else {
            return EJBLocalHome.class.isAssignableFrom(var2) ? this.addEJBLocalRef(var1, var2, (EJB)var3, var6, (EjbLocalRefBean)null, var5) : this.addEJBLocalRef(var1, var2, (EJB)var3, var6, (EjbLocalRefBean)null, var5);
         }
      }
   }

   private InjectionTargetBean addEJBLocalRef(String var1, Class var2, EJBReference var3, J2eeEnvironmentBean var4, EjbLocalRefBean var5, boolean var6) {
      if (var5 == null) {
         var5 = var4.createEjbLocalRef();
         var5.setEjbRefName(var1);
      }

      if (var2 != Object.class) {
         if (EJBLocalHome.class.isAssignableFrom(var2)) {
            if (!this.isSet("LocalHome", var5)) {
               var5.setLocalHome(var2.getName());
            }
         } else if (!this.isSet("Local", var5)) {
            var5.setLocal(var2.getName());
         }
      }

      if (!this.isSet("MappedName", var5) && var3.jndiName().length() > 0) {
         var5.setMappedName("weblogic-jndi:" + var3.jndiName());
      }

      return var6 ? var5.createInjectionTarget() : null;
   }

   private InjectionTargetBean addEJBLocalRef(String var1, Class var2, EJB var3, J2eeEnvironmentBean var4, EjbLocalRefBean var5, boolean var6) {
      if (var5 == null) {
         var5 = var4.createEjbLocalRef();
         var5.setEjbRefName(var1);
      }

      if (var2 != Object.class) {
         if (EJBLocalHome.class.isAssignableFrom(var2)) {
            if (!this.isSet("LocalHome", var5)) {
               var5.setLocalHome(var2.getName());
            }
         } else if (!this.isSet("Local", var5)) {
            var5.setLocal(var2.getName());
         }
      }

      if (!this.isSet("EjbLink", var5) && var3.beanName().length() > 0) {
         var5.setEjbLink(var3.beanName());
      }

      if (!this.isSet("MappedName", var5) && var3.mappedName().length() > 0) {
         var5.setMappedName(var3.mappedName());
      }

      return var6 ? var5.createInjectionTarget() : null;
   }

   private EjbLocalRefBean findEjbLocalRef(String var1, J2eeEnvironmentBean var2) {
      EjbLocalRefBean[] var3 = var2.getEjbLocalRefs();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EjbLocalRefBean var6 = var3[var5];
         if (var1.equals(var6.getEjbRefName())) {
            return var6;
         }
      }

      return null;
   }

   protected void processRunAs(Class var1, DescriptorBean var2) {
      if (var1.isAnnotationPresent(RunAs.class)) {
         RunAs var3 = (RunAs)var1.getAnnotation(RunAs.class);
         this.perhapsDeclareRunAs(var2, var3.value());
      }

   }

   protected void perhapsDeclareRunAs(DescriptorBean var1, String var2) {
   }

   protected void processDeclareRoles(Class var1, DescriptorBean var2) {
      if (var1.isAnnotationPresent(DeclareRoles.class)) {
         DeclareRoles var3 = (DeclareRoles)var1.getAnnotation(DeclareRoles.class);
         this.perhapsDeclareRoles(var2, var3.value());
      }

   }

   protected void perhapsDeclareRoles(DescriptorBean var1, String[] var2) {
   }
}
