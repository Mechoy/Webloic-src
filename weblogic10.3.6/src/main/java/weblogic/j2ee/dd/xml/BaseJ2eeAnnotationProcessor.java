package weblogic.j2ee.dd.xml;

import com.sun.java.xml.ns.javaee.HandlerChainType;
import com.sun.java.xml.ns.javaee.PortComponentHandlerType;
import commonj.timers.TimerManager;
import commonj.work.WorkManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.annotation.Resource.AuthenticationType;
import javax.ejb.EJB;
import javax.ejb.EJBHome;
import javax.ejb.EJBs;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.jws.HandlerChain;
import javax.mail.Session;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;
import javax.sql.DataSource;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.WebServiceRefs;
import org.omg.CORBA_2_3.ORB;
import org.w3c.dom.Node;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.j2ee.dd.xml.validator.AnnotationValidatorVisitor;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.InjectionTargetBean;
import weblogic.j2ee.descriptor.J2eeClientEnvironmentBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDestinationRefBean;
import weblogic.j2ee.descriptor.PersistenceUnitRefBean;
import weblogic.j2ee.descriptor.ResourceEnvRefBean;
import weblogic.j2ee.descriptor.ResourceRefBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerChainBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerChainsBean;
import weblogic.j2ee.extensions.ExtensionManager;
import weblogic.j2ee.extensions.InjectionExtension;
import weblogic.javaee.EJBReference;
import weblogic.kernel.KernelStatus;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.ErrorCollectionException;

public class BaseJ2eeAnnotationProcessor implements AnnotationProcessor {
   private static boolean productionMode = false;
   private Map<Class<?>, List<Method>> methodsCache = new HashMap();
   private ErrorCollectionException errors;

   public void processJ2eeAnnotations(Class var1, J2eeClientEnvironmentBean var2, boolean var3) throws ErrorCollectionException {
      this.processJ2eeAnnotations(var1, var2);
      if (var3) {
         this.throwProcessingErrors();
      }

   }

   protected void processJ2eeAnnotations(Class var1, J2eeClientEnvironmentBean var2) {
      this.addDescriptorDefaults(var1, var2);
      List var3 = this.getFields(var1);
      List var4 = this.getMethods(var1);
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Field var6 = (Field)var5.next();
         this.processField(var6, var2);
      }

      this.processMethods(var4, var2);
      Collection var11 = this.getClassResources(var1).values();
      Iterator var12 = var11.iterator();

      while(var12.hasNext()) {
         Resource var7 = (Resource)var12.next();
         this.addEnvironmentEntry(var7, var2);
      }

      Collection var13 = this.getClassEJBRefs(var1).values();
      Iterator var14 = var13.iterator();

      while(var14.hasNext()) {
         EJB var8 = (EJB)var14.next();
         this.addEjbRef(var8, var2);
      }

      Collection var15 = this.getClassPersistenceUnitRefs(var1).values();
      Iterator var16 = var15.iterator();

      while(var16.hasNext()) {
         PersistenceUnit var9 = (PersistenceUnit)var16.next();
         this.addPersistenceUnitRef(var9, var2);
      }

      Collection var17 = this.getClassWebServiceRefs(var1).values();
      Iterator var18 = var17.iterator();

      while(var18.hasNext()) {
         WebServiceRef var10 = (WebServiceRef)var18.next();
         this.addWebServiceRef(var10, var1, var2);
      }

   }

   public void validate(ClassLoader var1, DescriptorBean var2, boolean var3) throws ErrorCollectionException {
      this.validate(var1, var2);
      if (var3) {
         this.throwProcessingErrors();
      }

   }

   private void validate(ClassLoader var1, DescriptorBean var2) {
      if (!productionMode) {
         AnnotationValidatorVisitor var3 = new AnnotationValidatorVisitor(var1);
         ((AbstractDescriptorBean)var2).accept(var3);
         ErrorCollectionException var4 = var3.getErrors();
         if (var4 != null && var4.size() != 0) {
            if (this.errors == null) {
               this.errors = new ErrorCollectionException();
            }

            this.errors.add(var4);
         }

      }
   }

   protected void processField(Field var1, J2eeClientEnvironmentBean var2) {
      if (var1.isAnnotationPresent(Resource.class)) {
         this.addEnvironmentEntry(var1, var2);
      } else if (var1.isAnnotationPresent(EJB.class)) {
         this.addEjbRef(var1, var2);
      } else if (var1.isAnnotationPresent(EJBReference.class)) {
         this.addEjbRef(var1, var2);
      } else if (var1.isAnnotationPresent(PersistenceUnit.class)) {
         this.addPersistenceUnitRef(var1, var2);
      } else if (var1.isAnnotationPresent(WebServiceRef.class)) {
         this.addWebServiceRef(var1, var2);
      }

   }

   private void processMethods(List<Method> var1, J2eeClientEnvironmentBean var2) {
      this.processLifcycleMethods(var1, var2);
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Method var4 = (Method)var3.next();
         this.processMethod(var4, var2);
      }

   }

   protected void processMethod(Method var1, J2eeClientEnvironmentBean var2) {
      if (var1.isAnnotationPresent(Resource.class)) {
         this.addEnvironmentEntry(var1, var2);
      } else if (var1.isAnnotationPresent(EJB.class)) {
         this.addEjbRef(var1, var2);
      } else if (var1.isAnnotationPresent(PersistenceUnit.class)) {
         this.addPersistenceUnitRef(var1, var2);
      } else if (var1.isAnnotationPresent(WebServiceRef.class)) {
         this.addWebServiceRef(var1, var2);
      } else if (var1.isAnnotationPresent(EJBReference.class)) {
         this.addEjbRef(var1, var2);
      }

   }

   private void processLifcycleMethods(List<Method> var1, J2eeClientEnvironmentBean var2) {
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      LifecycleCallbackBean[] var5 = var2.getPostConstructs();
      int var6 = var5.length;

      int var7;
      LifecycleCallbackBean var8;
      for(var7 = 0; var7 < var6; ++var7) {
         var8 = var5[var7];
         var3.add(var8.getLifecycleCallbackClass());
      }

      var5 = var2.getPreDestroys();
      var6 = var5.length;

      for(var7 = 0; var7 < var6; ++var7) {
         var8 = var5[var7];
         var4.add(var8.getLifecycleCallbackClass());
      }

      Iterator var9 = var1.iterator();

      while(var9.hasNext()) {
         Method var10 = (Method)var9.next();
         String var11 = var10.getDeclaringClass().getName();
         if (var10.isAnnotationPresent(PostConstruct.class) && !var3.contains(var11)) {
            this.addPostConstructCallback(var10, var2);
         }

         if (var10.isAnnotationPresent(PreDestroy.class) && !var4.contains(var11)) {
            this.addPreDestroyCallback(var10, var2);
         }
      }

   }

   protected void addPostConstructCallback(Method var1, J2eeClientEnvironmentBean var2) {
      LifecycleCallbackBean var3 = var2.createPostConstruct();
      var3.setBeanSource(1);
      var3.setLifecycleCallbackClass(var1.getDeclaringClass().getName());
      var3.setLifecycleCallbackMethod(var1.getName());
   }

   protected void addPreDestroyCallback(Method var1, J2eeClientEnvironmentBean var2) {
      LifecycleCallbackBean var3 = var2.createPreDestroy();
      var3.setBeanSource(1);
      var3.setLifecycleCallbackClass(var1.getDeclaringClass().getName());
      var3.setLifecycleCallbackMethod(var1.getName());
   }

   protected void addPersistenceUnitRef(PersistenceUnit var1, J2eeClientEnvironmentBean var2) {
      this.addPersistenceUnitRef(var1.name(), var1, var2, false);
   }

   protected void addPersistenceUnitRef(Field var1, J2eeClientEnvironmentBean var2) {
      PersistenceUnit var3 = (PersistenceUnit)var1.getAnnotation(PersistenceUnit.class);
      String var4 = this.getCompEnvJndiName(var3.name(), var1);
      if (this.findInjectionTargetFromPersistenceUnitRef(var1, var4, var2) == null) {
         this.addInjectionTarget(var1, this.addPersistenceUnitRef(var4, var3, var2, true));
      }

   }

   protected void addPersistenceUnitRef(Method var1, J2eeClientEnvironmentBean var2) {
      PersistenceUnit var3 = (PersistenceUnit)var1.getAnnotation(PersistenceUnit.class);
      String var4 = this.getCompEnvJndiName(var3.name(), var1);
      if (this.findInjectionTargetFromPersistenceUnitRef(var1, var4, var2) == null) {
         this.addInjectionTarget(var1, this.addPersistenceUnitRef(var4, var3, var2, true));
      }

   }

   protected InjectionTargetBean addPersistenceUnitRef(String var1, PersistenceUnit var2, J2eeClientEnvironmentBean var3, boolean var4) {
      PersistenceUnitRefBean var5 = null;
      PersistenceUnitRefBean[] var6 = var3.getPersistenceUnitRefs();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         PersistenceUnitRefBean var9 = var6[var8];
         if (var9.getPersistenceUnitRefName().equals(var1)) {
            var5 = var9;
            break;
         }
      }

      if (var5 == null) {
         var5 = var3.createPersistenceUnitRef();
         var5.setPersistenceUnitRefName(var1);
      }

      if (!this.isSet("PersistenceUnitName", var5) && var2.unitName().length() > 0) {
         var5.setPersistenceUnitName(var2.unitName());
      }

      return var4 ? var5.createInjectionTarget() : null;
   }

   protected InjectionTargetBean findInjectionTargetFromPersistenceUnitRef(Method var1, String var2, J2eeClientEnvironmentBean var3) {
      return this.findInjectionTargetFromPersistenceUnitRef(var1.getDeclaringClass().getName(), this.getPropertyName(var1), var2, var3);
   }

   protected InjectionTargetBean findInjectionTargetFromPersistenceUnitRef(Field var1, String var2, J2eeClientEnvironmentBean var3) {
      return this.findInjectionTargetFromPersistenceUnitRef(var1.getDeclaringClass().getName(), var1.getName(), var2, var3);
   }

   protected InjectionTargetBean findInjectionTargetFromPersistenceUnitRef(String var1, String var2, String var3, J2eeClientEnvironmentBean var4) {
      PersistenceUnitRefBean var5 = null;
      PersistenceUnitRefBean[] var6 = var4.getPersistenceUnitRefs();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         PersistenceUnitRefBean var9 = var6[var8];
         if (var9.getPersistenceUnitRefName().equals(var3)) {
            var5 = var9;
            break;
         }
      }

      return var5 != null ? this.findInjectionTargetInArray(var1, var2, var5.getInjectionTargets()) : null;
   }

   private void addWebServiceRef(WebServiceRef var1, Class var2, J2eeClientEnvironmentBean var3) {
      HandlerChain var4 = (HandlerChain)var2.getAnnotation(HandlerChain.class);
      this.addWebServiceRef(var1.name(), var1.type(), var1, var4, var2, var3, false);
   }

   private void addWebServiceRef(Field var1, J2eeClientEnvironmentBean var2) {
      WebServiceRef var3 = (WebServiceRef)var1.getAnnotation(WebServiceRef.class);
      HandlerChain var4 = (HandlerChain)var1.getAnnotation(HandlerChain.class);
      String var5 = this.getCompEnvJndiName(var3.name(), var1);
      Class var6 = this.getEnvironmentType(var3.type(), var1);
      if (this.findInjectionTargetFromServiceRef(var1, var5, var2) == null) {
         this.addInjectionTarget(var1, this.addWebServiceRef(var5, var6, var3, var4, var1.getDeclaringClass(), var2, true));
      }

   }

   private void addWebServiceRef(Method var1, J2eeClientEnvironmentBean var2) {
      WebServiceRef var3 = (WebServiceRef)var1.getAnnotation(WebServiceRef.class);
      HandlerChain var4 = (HandlerChain)var1.getAnnotation(HandlerChain.class);
      String var5 = this.getCompEnvJndiName(var3.name(), var1);
      Class var6 = this.getEnvironmentType(var3.type(), var1);
      if (this.findInjectionTargetFromServiceRef(var1, var5, var2) == null) {
         this.addInjectionTarget(var1, this.addWebServiceRef(var5, var6, var3, var4, var1.getDeclaringClass(), var2, true));
      }

   }

   private InjectionTargetBean addWebServiceRef(String var1, Class var2, WebServiceRef var3, HandlerChain var4, Class var5, J2eeClientEnvironmentBean var6, boolean var7) {
      ServiceRefBean var8 = this.findOrCreateServiceRef(var1, var6);
      if (!this.isSet("ServiceRefType", var8)) {
         var8.setServiceRefType(var2.getName());
      }

      if (!this.isSet("ServiceInterface", var8)) {
         Class var9 = var3.value();
         if ((var9 == Object.class || var9 == Service.class) && Service.class.isAssignableFrom(var2)) {
            var9 = var2;
         }

         if (var9 != Object.class) {
            var8.setServiceInterface(var9.getName());
         }
      }

      if (!this.isSet("WsdlFile", var8)) {
         var8.setWsdlFile(var3.wsdlLocation());
      }

      if (var8.getHandlerChains() == null && var4 != null) {
         this.addHandlerChain(var8, var5, var4);
      }

      return var7 ? var8.createInjectionTarget() : null;
   }

   private void addHandlerChain(ServiceRefBean var1, Class var2, HandlerChain var3) {
      try {
         HandlerChainLoader var4 = new HandlerChainLoader(var3, var2);
         if (var4.getHandlerChains() == null) {
            return;
         }

         ServiceRefHandlerChainsBean var5 = var1.createHandlerChains();
         HandlerChainType[] var6 = var4.getHandlerChains();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            HandlerChainType var9 = var6[var8];
            ServiceRefHandlerChainBean var10 = var5.createHandlerChain();
            Node var11;
            if (var9.xgetPortNamePattern() == null) {
               var10.setPortNamePattern(var9.getPortNamePattern());
            } else {
               var11 = var9.xgetPortNamePattern().getDomNode();
               var10.setPortNamePattern(this.getQualifiedNamePattern(var11));
            }

            if (var9.getProtocolBindings() != null) {
               var10.setProtocolBindings(var9.getProtocolBindings().toString());
            }

            if (var9.xgetServiceNamePattern() == null) {
               var10.setServiceNamePattern(var9.getServiceNamePattern());
            } else {
               var11 = var9.xgetServiceNamePattern().getDomNode();
               var10.setServiceNamePattern(this.getQualifiedNamePattern(var11));
            }

            PortComponentHandlerType[] var19 = var9.getHandlerArray();
            int var12 = var19.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               PortComponentHandlerType var14 = var19[var13];
               ServiceRefHandlerBean var15 = var10.createHandler();
               if (var14.getHandlerName() != null) {
                  var15.setHandlerName(var14.getHandlerName().getStringValue());
               }

               var15.setHandlerClass(var14.getHandlerClass().getStringValue());

               for(int var16 = 0; var16 < var14.sizeOfSoapRoleArray(); ++var16) {
                  var15.addSoapRole(var14.getSoapRoleArray(var16).getStringValue());
               }

               Class var20 = Class.forName(var14.getHandlerClass().getStringValue(), true, Thread.currentThread().getContextClassLoader());
               J2eeAnnotationProcessor var17 = new J2eeAnnotationProcessor();
               var17.processJ2eeAnnotations(var20, var15);
            }
         }
      } catch (Exception var18) {
         this.addProcessingError("Error adding handler chain: " + var18.getMessage());
      }

   }

   private String getQualifiedNamePattern(Node var1) {
      if (var1 != null && var1.getFirstChild() != null) {
         String var2 = var1.getFirstChild().getNodeValue();
         String var3 = var2;
         if (var2 != null) {
            int var4 = var2.indexOf(":");
            if (var4 > 0) {
               String var5 = "xmlns:" + var2.substring(0, var4);
               String var6 = null;

               for(Node var7 = var1; var7 != null; var7 = var7.getParentNode()) {
                  if (var7.getAttributes() != null && var7.getAttributes().getNamedItem(var5) != null) {
                     var6 = var7.getAttributes().getNamedItem(var5).getNodeValue();
                     if (var6 != null) {
                        break;
                     }
                  }
               }

               if (var6 != null) {
                  var3 = "{" + var6 + "}" + var2.substring(var4 + 1);
               }
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   protected InjectionTargetBean findInjectionTargetFromServiceRef(Method var1, String var2, J2eeClientEnvironmentBean var3) {
      return this.findInjectionTargetFromServiceRef(var1.getDeclaringClass().getName(), this.getPropertyName(var1), var2, var3);
   }

   protected InjectionTargetBean findInjectionTargetFromServiceRef(Field var1, String var2, J2eeClientEnvironmentBean var3) {
      return this.findInjectionTargetFromServiceRef(var1.getDeclaringClass().getName(), var1.getName(), var2, var3);
   }

   protected InjectionTargetBean findInjectionTargetFromServiceRef(String var1, String var2, String var3, J2eeClientEnvironmentBean var4) {
      ServiceRefBean var5 = this.findOrCreateServiceRef(var3, var4);
      return var5 != null ? this.findInjectionTargetInArray(var1, var2, var5.getInjectionTargets()) : null;
   }

   protected void addEjbRef(EJB var1, J2eeClientEnvironmentBean var2) {
      this.addEjbRef(var1.name(), var1.beanInterface(), var1, var2, false);
   }

   protected void addEjbRef(Field var1, J2eeClientEnvironmentBean var2) {
      EJB var3 = (EJB)var1.getAnnotation(EJB.class);
      if (var3 == null) {
         EJBReference var4 = (EJBReference)var1.getAnnotation(EJBReference.class);
         String var5 = this.getCompEnvJndiName(var4.name(), var1);
         Class var6 = var1.getType();
         if (this.findInjectionTargetFromEjbRef(var1, var5, var6, var2) == null) {
            this.addInjectionTarget(var1, this.addEjbRef(var5, var6, var4, var2, true));
         }
      } else {
         String var8 = this.getCompEnvJndiName(var3.name(), var1);
         Class var7 = this.getEnvironmentType(var3.beanInterface(), var1);
         if (this.findInjectionTargetFromEjbRef(var1, var8, var7, var2) == null) {
            this.addInjectionTarget(var1, this.addEjbRef(var8, var7, var3, var2, true));
         }
      }

   }

   protected void addEjbRef(Method var1, J2eeClientEnvironmentBean var2) {
      EJB var3 = (EJB)var1.getAnnotation(EJB.class);
      if (var3 == null) {
         EJBReference var4 = (EJBReference)var1.getAnnotation(EJBReference.class);
         String var5 = this.getCompEnvJndiName(var4.name(), var1);
         Class var6 = var1.getParameterTypes()[0];
         if (this.findInjectionTargetFromEjbRef(var1, var5, var6, var2) == null) {
            this.addInjectionTarget(var1, this.addEjbRef(var5, var6, var4, var2, true));
         }
      } else {
         String var8 = this.getCompEnvJndiName(var3.name(), var1);
         Class var7 = this.getEnvironmentType(var3.beanInterface(), var1);
         if (this.findInjectionTargetFromEjbRef(var1, var8, var7, var2) == null) {
            this.addInjectionTarget(var1, this.addEjbRef(var8, var7, var3, var2, true));
         }
      }

   }

   protected InjectionTargetBean addEjbref(String var1, Class var2, EJBReference var3, J2eeClientEnvironmentBean var4, boolean var5) {
      EjbRefBean var6 = this.findEjbRef(var1, var4);
      if (var6 != null) {
         return this.addEJBRemoteRef(var1, var2, var3, var4, var6, var5);
      } else if (var2 == Object.class) {
         this.addBeanInterfaceNotSetError(var4);
         return null;
      } else {
         return this.addEJBRemoteRef(var1, var2, (EJBReference)var3, var4, (EjbRefBean)null, var5);
      }
   }

   protected InjectionTargetBean addEJBRemoteRef(String var1, Class var2, EJBReference var3, J2eeClientEnvironmentBean var4, EjbRefBean var5, boolean var6) {
      if (var5 == null) {
         var5 = var4.createEjbRef();
         var5.setEjbRefName(var1);
      }

      if (var2 != Object.class) {
         if (EJBHome.class.isAssignableFrom(var2)) {
            if (!this.isSet("Home", var5)) {
               var5.setHome(var2.getName());
            }
         } else if (!this.isSet("Remote", var5)) {
            var5.setRemote(var2.getName());
         }
      }

      if (!this.isSet("MappedName", var5) && var3.jndiName().length() > 0) {
         var5.setMappedName("weblogic-jndi:" + var3.jndiName());
      }

      return var6 ? var5.createInjectionTarget() : null;
   }

   protected InjectionTargetBean addEjbRef(String var1, Class var2, EJBReference var3, J2eeClientEnvironmentBean var4, boolean var5) {
      EjbRefBean var6 = this.findEjbRef(var1, var4);
      if (var6 != null) {
         return this.addEJBRemoteRef(var1, var2, var3, var4, var6, var5);
      } else if (var2 == Object.class) {
         this.addBeanInterfaceNotSetError(var4);
         return null;
      } else {
         return this.addEJBRemoteRef(var1, var2, (EJBReference)var3, var4, (EjbRefBean)null, var5);
      }
   }

   protected InjectionTargetBean addEjbRef(String var1, Class var2, EJB var3, J2eeClientEnvironmentBean var4, boolean var5) {
      EjbRefBean var6 = this.findEjbRef(var1, var4);
      if (var6 != null) {
         return this.addEJBRemoteRef(var1, var2, var3, var4, var6, var5);
      } else {
         return EJBHome.class.isAssignableFrom(var2) ? this.addEJBRemoteRef(var1, var2, (EJB)var3, var4, (EjbRefBean)null, var5) : this.addEJBRemoteRef(var1, var2, (EJB)var3, var4, (EjbRefBean)null, var5);
      }
   }

   protected InjectionTargetBean findInjectionTargetFromEjbRef(Method var1, String var2, Class var3, J2eeClientEnvironmentBean var4) {
      return this.findInjectionTargetFromEjbRef(var1.getDeclaringClass().getName(), this.getPropertyName(var1), var2, var3, var4);
   }

   protected InjectionTargetBean findInjectionTargetFromEjbRef(Field var1, String var2, Class var3, J2eeClientEnvironmentBean var4) {
      return this.findInjectionTargetFromEjbRef(var1.getDeclaringClass().getName(), var1.getName(), var2, var3, var4);
   }

   protected InjectionTargetBean findInjectionTargetFromEjbRef(String var1, String var2, String var3, Class var4, J2eeClientEnvironmentBean var5) {
      EjbRefBean var6 = this.findEjbRef(var3, var5);
      return var6 != null ? this.findInjectionTargetInArray(var1, var2, var6.getInjectionTargets()) : null;
   }

   protected InjectionTargetBean addEJBRemoteRef(String var1, Class var2, EJB var3, J2eeClientEnvironmentBean var4, EjbRefBean var5, boolean var6) {
      if (var5 == null) {
         var5 = var4.createEjbRef();
         var5.setEjbRefName(var1);
      }

      if (var2 != Object.class) {
         if (EJBHome.class.isAssignableFrom(var2)) {
            if (!this.isSet("Home", var5)) {
               var5.setHome(var2.getName());
            }
         } else if (!this.isSet("Remote", var5)) {
            var5.setRemote(var2.getName());
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

   protected EjbRefBean findEjbRef(String var1, J2eeClientEnvironmentBean var2) {
      EjbRefBean[] var3 = var2.getEjbRefs();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EjbRefBean var6 = var3[var5];
         if (var1.equals(var6.getEjbRefName())) {
            return var6;
         }
      }

      return null;
   }

   protected void addEnvironmentEntry(Resource var1, J2eeClientEnvironmentBean var2) {
      this.addEnvironmentEntry(var1.name(), var1.type(), var1, var2, false);
   }

   protected void addEnvironmentEntry(Field var1, J2eeClientEnvironmentBean var2) {
      Resource var3 = (Resource)var1.getAnnotation(Resource.class);
      String var4 = this.getCompEnvJndiName(var3.name(), var1);
      Class var5 = this.getEnvironmentType(var3.type(), var1);
      if (this.findInjectionTargetFromEnvironmentEntry(var1, var4, var5, var3, var2) == null) {
         this.addInjectionTarget(var1, this.addEnvironmentEntry(var4, var5, var3, var2, true));
      }

   }

   protected void addEnvironmentEntry(Method var1, J2eeClientEnvironmentBean var2) {
      Resource var3 = (Resource)var1.getAnnotation(Resource.class);
      String var4 = this.getCompEnvJndiName(var3.name(), var1);
      Class var5 = this.getEnvironmentType(var3.type(), var1);
      if (this.findInjectionTargetFromEnvironmentEntry(var1, var4, var5, var3, var2) == null) {
         this.addInjectionTarget(var1, this.addEnvironmentEntry(var4, var5, var3, var2, true));
      }

   }

   protected String getPropertyName(Method var1) {
      String var2 = var1.getName();
      char var3 = var2.charAt(3);
      return ("" + var3).toLowerCase() + var2.substring(4);
   }

   protected EnvEntryBean findOrCreateEnvEntry(String var1, J2eeClientEnvironmentBean var2) {
      EnvEntryBean[] var3 = var2.getEnvEntries();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnvEntryBean var6 = var3[var5];
         if (var1.equals(var6.getEnvEntryName())) {
            return var6;
         }
      }

      EnvEntryBean var7 = var2.createEnvEntry();
      var7.setEnvEntryName(var1);
      return var7;
   }

   protected ServiceRefBean findOrCreateServiceRef(String var1, J2eeClientEnvironmentBean var2) {
      ServiceRefBean[] var3 = var2.getServiceRefs();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ServiceRefBean var6 = var3[var5];
         if (var1.equals(var6.getServiceRefName())) {
            return var6;
         }
      }

      ServiceRefBean var7 = var2.createServiceRef();
      var7.setServiceRefName(var1);
      return var7;
   }

   protected ResourceRefBean findOrCreateResourceRef(String var1, J2eeClientEnvironmentBean var2) {
      ResourceRefBean[] var3 = var2.getResourceRefs();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ResourceRefBean var6 = var3[var5];
         if (var1.equals(var6.getResRefName())) {
            return var6;
         }
      }

      ResourceRefBean var7 = var2.createResourceRef();
      var7.setResRefName(var1);
      return var7;
   }

   protected MessageDestinationRefBean findOrCreateMessageDestinationRef(String var1, J2eeClientEnvironmentBean var2) {
      MessageDestinationRefBean[] var3 = var2.getMessageDestinationRefs();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         MessageDestinationRefBean var6 = var3[var5];
         if (var1.equals(var6.getMessageDestinationRefName())) {
            return var6;
         }
      }

      MessageDestinationRefBean var7 = var2.createMessageDestinationRef();
      var7.setMessageDestinationRefName(var1);
      return var7;
   }

   protected ResourceEnvRefBean findOrCreateResourceEnvRef(String var1, J2eeClientEnvironmentBean var2) {
      ResourceEnvRefBean[] var3 = var2.getResourceEnvRefs();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ResourceEnvRefBean var6 = var3[var5];
         if (var1.equals(var6.getResourceEnvRefName())) {
            return var6;
         }
      }

      ResourceEnvRefBean var7 = var2.createResourceEnvRef();
      var7.setResourceEnvRefName(var1);
      return var7;
   }

   private String transformPrimitiveType(Class var1) {
      if (var1 == Boolean.TYPE) {
         return Boolean.class.getName();
      } else if (var1 == Integer.TYPE) {
         return Integer.class.getName();
      } else if (var1 == Float.TYPE) {
         return Float.class.getName();
      } else if (var1 == Short.TYPE) {
         return Short.class.getName();
      } else if (var1 == Character.TYPE) {
         return Character.class.getName();
      } else if (var1 == Byte.TYPE) {
         return Byte.class.getName();
      } else {
         return var1 == Long.TYPE ? Long.class.getName() : var1.getName();
      }
   }

   protected InjectionTargetBean addEnvironmentEntry(String var1, Class var2, Resource var3, J2eeClientEnvironmentBean var4, boolean var5) {
      if (!var2.isPrimitive() && var2 != String.class && var2 != Character.class && var2 != Integer.class && var2 != Boolean.class && var2 != Double.class && var2 != Byte.class && var2 != Short.class && var2 != Long.class && var2 != Float.class) {
         if (var2 == javax.xml.rpc.Service.class) {
            ServiceRefBean var11 = this.findOrCreateServiceRef(var1, var4);
            if (!this.isSet("ServiceRefType", var11)) {
               var11.setServiceRefType(var2.getName());
            }

            if (var3.mappedName().length() > 0 && !this.isSet("MappedName", var11)) {
               var11.setMappedName(var3.mappedName());
            }

            return var5 ? var11.createInjectionTarget() : null;
         } else if (var2 != DataSource.class && var2 != ConnectionFactory.class && var2 != QueueConnectionFactory.class && var2 != TopicConnectionFactory.class && var2 != Session.class && var2 != URL.class && var2 != javax.resource.cci.ConnectionFactory.class && var2 != ORB.class && var2 != org.omg.CORBA.ORB.class && var2 != WorkManager.class && var2 != TimerManager.class && !var2.getName().equals("com.tangosol.net.NamedCache") && !var2.getName().equals("com.tangosol.net.Service") && !JCAConnectionFactoryProvider.isAdapterConnectionFactoryClass(var2.getName())) {
            if (var2 != Queue.class && var2 != Topic.class) {
               InjectionExtension var10 = ExtensionManager.instance.getFirstMatchingExtension(var2.getName(), var3.name());
               ResourceEnvRefBean var7;
               if (var10 != null) {
                  if (var3.name().length() == 0) {
                     var1 = var10.getName(var2.getName());
                  } else {
                     var1 = var10.getName(var2.getName(), var1);
                  }

                  var7 = this.findOrCreateResourceEnvRef(var1, var4);
                  if (!this.isSet("ResourceEnvRefType", var7)) {
                     var7.setResourceEnvRefType(var2.getName());
                  }

                  if (!this.isSet("MappedName", var7)) {
                     var7.setMappedName(var1);
                  }

                  return var5 ? var7.createInjectionTarget() : null;
               } else {
                  var7 = this.findOrCreateResourceEnvRef(var1, var4);
                  if (!this.isSet("ResourceEnvRefType", var7)) {
                     var7.setResourceEnvRefType(var2.getName());
                  }

                  if (var3.mappedName().length() > 0 && !this.isSet("MappedName", var7)) {
                     var7.setMappedName(var3.mappedName());
                  }

                  return var5 ? var7.createInjectionTarget() : null;
               }
            } else {
               MessageDestinationRefBean var9 = this.findOrCreateMessageDestinationRef(var1, var4);
               if (!this.isSet("MessageDestinationType", var9)) {
                  var9.setMessageDestinationType(var2.getName());
               }

               if (var3.mappedName().length() > 0 && !this.isSet("MappedName", var9)) {
                  var9.setMappedName(var3.mappedName());
               }

               return var5 ? var9.createInjectionTarget() : null;
            }
         } else {
            ResourceRefBean var8 = this.findOrCreateResourceRef(var1, var4);
            if (!this.isSet("ResType", var8)) {
               var8.setResType(var2.getName());
            }

            if (!this.isSet("ResAuth", var8)) {
               if (var3.authenticationType() != AuthenticationType.CONTAINER) {
                  var8.setResAuth("Application");
               } else {
                  var8.setResAuth("Container");
               }
            }

            if (!this.isSet("ResSharingScope", var8) && !var3.shareable()) {
               var8.setResSharingScope("Unshareable");
            }

            if (var3.mappedName().length() > 0 && !this.isSet("MappedName", var8)) {
               var8.setMappedName(var3.mappedName());
            }

            return var5 ? var8.createInjectionTarget() : null;
         }
      } else {
         EnvEntryBean var6 = this.findOrCreateEnvEntry(var1, var4);
         if (!this.isSet("EnvEntryType", var6)) {
            if (var2.isPrimitive()) {
               var6.setEnvEntryType(this.transformPrimitiveType(var2));
            } else {
               var6.setEnvEntryType(var2.getName());
            }
         }

         if (var3.mappedName().length() > 0 && !this.isSet("MappedName", var6)) {
            var6.setMappedName(var3.mappedName());
         }

         return var5 ? var6.createInjectionTarget() : null;
      }
   }

   protected InjectionTargetBean findInjectionTargetFromEnvironmentEntry(Method var1, String var2, Class var3, Resource var4, J2eeClientEnvironmentBean var5) {
      return this.findInjectionTargetFromEnvironmentEntry(var1.getDeclaringClass().getName(), this.getPropertyName(var1), var2, var3, var4, var5);
   }

   protected InjectionTargetBean findInjectionTargetFromEnvironmentEntry(Field var1, String var2, Class var3, Resource var4, J2eeClientEnvironmentBean var5) {
      return this.findInjectionTargetFromEnvironmentEntry(var1.getDeclaringClass().getName(), var1.getName(), var2, var3, var4, var5);
   }

   protected InjectionTargetBean findInjectionTargetFromEnvironmentEntry(String var1, String var2, String var3, Class var4, Resource var5, J2eeClientEnvironmentBean var6) {
      if (!var4.isPrimitive() && var4 != String.class && var4 != Character.class && var4 != Integer.class && var4 != Boolean.class && var4 != Double.class && var4 != Byte.class && var4 != Short.class && var4 != Long.class && var4 != Float.class) {
         if (var4 == javax.xml.rpc.Service.class) {
            this.findOrCreateServiceRef(var3, var6);
         }

         if (var4 != DataSource.class && var4 != ConnectionFactory.class && var4 != QueueConnectionFactory.class && var4 != TopicConnectionFactory.class && var4 != Session.class && var4 != URL.class && var4 != javax.resource.cci.ConnectionFactory.class && var4 != ORB.class && var4 != org.omg.CORBA.ORB.class && var4 != WorkManager.class && var4 != TimerManager.class && !var4.getName().equals("com.tangosol.net.NamedCache") && !var4.getName().equals("com.tangosol.net.Service") && !JCAConnectionFactoryProvider.isAdapterConnectionFactoryClass(var4.getName())) {
            if (var4 != Queue.class && var4 != Topic.class) {
               InjectionExtension var11 = ExtensionManager.instance.getFirstMatchingExtension(var4.getName(), var5.name());
               ResourceEnvRefBean var8;
               if (var11 != null) {
                  if (var5.name().length() == 0) {
                     var3 = var11.getName(var4.getName());
                  } else {
                     var3 = var11.getName(var4.getName(), var3);
                  }

                  var8 = this.findOrCreateResourceEnvRef(var3, var6);
                  return var8 != null ? this.findInjectionTargetInArray(var1, var2, var8.getInjectionTargets()) : null;
               } else {
                  var8 = this.findOrCreateResourceEnvRef(var3, var6);
                  return var8 != null ? this.findInjectionTargetInArray(var1, var2, var8.getInjectionTargets()) : null;
               }
            } else {
               MessageDestinationRefBean var10 = this.findOrCreateMessageDestinationRef(var3, var6);
               return var10 != null ? this.findInjectionTargetInArray(var1, var2, var10.getInjectionTargets()) : null;
            }
         } else {
            ResourceRefBean var9 = this.findOrCreateResourceRef(var3, var6);
            return var9 != null ? this.findInjectionTargetInArray(var1, var2, var9.getInjectionTargets()) : null;
         }
      } else {
         EnvEntryBean var7 = this.findOrCreateEnvEntry(var3, var6);
         return var7 != null ? this.findInjectionTargetInArray(var1, var2, var7.getInjectionTargets()) : null;
      }
   }

   protected List<Field> getFields(Class var1) {
      return this.getFields(var1, new ArrayList());
   }

   protected List<Field> getFields(Class var1, Collection<String> var2) {
      ArrayList var3 = new ArrayList();
      Field[] var4 = var1.getDeclaredFields();
      Field[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Field var8 = var5[var7];
         var3.add(var8);
      }

      Class var9 = var1.getSuperclass();
      if (!var9.equals(Object.class)) {
         var3.addAll(this.getFields(var9, var2));
      }

      return var3;
   }

   protected List<Method> getMethods(Class var1) {
      List var2 = (List)this.methodsCache.get(var1);
      if (var2 == null) {
         var2 = this.getMethods(var1, new HashSet());
         Collections.reverse(var2);
         this.methodsCache.put(var1, var2);
      }

      return var2;
   }

   protected List<Method> getMethods(Class var1, Set<String> var2) {
      ArrayList var3 = new ArrayList();
      Method[] var4 = var1.getDeclaredMethods();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];
         int var8 = var7.getModifiers();
         if (Modifier.isPrivate(var8)) {
            var3.add(var7);
         } else {
            StringBuffer var9 = (new StringBuffer(var7.getName())).append("(");
            Class[] var10 = var7.getParameterTypes();
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               Class var13 = var10[var12];
               var9.append(var13.getName()).append(", ");
            }

            if (!var2.contains(var9.toString())) {
               var3.add(var7);
               var2.add(var9.toString());
            }
         }
      }

      Class var14 = var1.getSuperclass();
      if (!var14.equals(Object.class)) {
         var3.addAll(this.getMethods(var14, var2));
      }

      return var3;
   }

   protected Map<String, Resource> getClassResources(Class var1) {
      Object var2 = null;
      Class var3 = var1.getSuperclass();
      if (var3.equals(Object.class)) {
         var2 = new HashMap();
      } else {
         var2 = this.getClassResources(var3);
      }

      if (var1.isAnnotationPresent(Resources.class)) {
         Resources var4 = (Resources)var1.getAnnotation(Resources.class);
         Resource[] var5 = var4.value();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Resource var8 = var5[var7];
            this.validateClassResource(var8, var1);
            ((Map)var2).put(var8.name(), var8);
         }
      }

      if (var1.isAnnotationPresent(Resource.class)) {
         Resource var9 = (Resource)var1.getAnnotation(Resource.class);
         this.validateClassResource(var9, var1);
         ((Map)var2).put(var9.name(), var9);
      }

      return (Map)var2;
   }

   protected Map<String, EJB> getClassEJBRefs(Class var1) {
      Object var2 = null;
      Class var3 = var1.getSuperclass();
      if (var3.equals(Object.class)) {
         var2 = new HashMap();
      } else {
         var2 = this.getClassEJBRefs(var3);
      }

      if (var1.isAnnotationPresent(EJBs.class)) {
         EJBs var4 = (EJBs)var1.getAnnotation(EJBs.class);
         EJB[] var5 = var4.value();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            EJB var8 = var5[var7];
            this.validateClassResource(var8, var1);
            ((Map)var2).put(var8.name(), var8);
         }
      }

      if (var1.isAnnotationPresent(EJB.class)) {
         EJB var9 = (EJB)var1.getAnnotation(EJB.class);
         this.validateClassResource(var9, var1);
         ((Map)var2).put(var9.name(), var9);
      }

      return (Map)var2;
   }

   protected Map<String, PersistenceUnit> getClassPersistenceUnitRefs(Class var1) {
      Object var2 = null;
      Class var3 = var1.getSuperclass();
      if (var3.equals(Object.class)) {
         var2 = new HashMap();
      } else {
         var2 = this.getClassPersistenceUnitRefs(var3);
      }

      if (var1.isAnnotationPresent(PersistenceUnits.class)) {
         PersistenceUnits var4 = (PersistenceUnits)var1.getAnnotation(PersistenceUnits.class);
         PersistenceUnit[] var5 = var4.value();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PersistenceUnit var8 = var5[var7];
            this.validateClassResource(var8, var1);
            ((Map)var2).put(var8.name(), var8);
         }
      }

      if (var1.isAnnotationPresent(PersistenceUnit.class)) {
         PersistenceUnit var9 = (PersistenceUnit)var1.getAnnotation(PersistenceUnit.class);
         this.validateClassResource(var9, var1);
         ((Map)var2).put(var9.name(), var9);
      }

      return (Map)var2;
   }

   private Map<String, WebServiceRef> getClassWebServiceRefs(Class var1) {
      Object var2 = null;
      Class var3 = var1.getSuperclass();
      if (var3.equals(Object.class)) {
         var2 = new HashMap();
      } else {
         var2 = this.getClassWebServiceRefs(var3);
      }

      if (var1.isAnnotationPresent(WebServiceRefs.class)) {
         WebServiceRefs var4 = (WebServiceRefs)var1.getAnnotation(WebServiceRefs.class);
         WebServiceRef[] var5 = var4.value();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            WebServiceRef var8 = var5[var7];
            this.validateClassResource(var8, var1);
            ((Map)var2).put(var8.name(), var8);
         }
      }

      if (var1.isAnnotationPresent(WebServiceRef.class)) {
         WebServiceRef var9 = (WebServiceRef)var1.getAnnotation(WebServiceRef.class);
         this.validateClassResource(var9, var1);
         ((Map)var2).put(var9.name(), var9);
      }

      return (Map)var2;
   }

   protected Set<PersistenceContext> getClassPersistenceContextRefs(Class var1) {
      Object var2 = null;
      Class var3 = var1.getSuperclass();
      if (var3.equals(Object.class)) {
         var2 = new HashSet();
      } else {
         var2 = this.getClassPersistenceContextRefs(var3);
      }

      if (var1.isAnnotationPresent(PersistenceContexts.class)) {
         PersistenceContexts var4 = (PersistenceContexts)var1.getAnnotation(PersistenceContexts.class);
         PersistenceContext[] var5 = var4.value();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PersistenceContext var8 = var5[var7];
            this.validateClassResource(var8, var1);
            ((Set)var2).add(var8);
         }
      }

      if (var1.isAnnotationPresent(PersistenceContext.class)) {
         PersistenceContext var9 = (PersistenceContext)var1.getAnnotation(PersistenceContext.class);
         this.validateClassResource(var9, var1);
         ((Set)var2).add(var9);
      }

      return (Set)var2;
   }

   private void validateClassResource(WebServiceRef var1, Class var2) {
      if (var1.name().length() == 0) {
         this.addProcessingError("A class level WebServiceRef annotation on class " + var2.getName() + " does not have the name attribute set.");
      }

   }

   protected void validateClassResource(PersistenceContext var1, Class var2) {
      if (var1.name().length() == 0) {
         this.addProcessingError("A class level PersistenceContext annotation on class " + var2.getName() + " does not have the name attribute set.");
      }

   }

   protected void validateClassResource(PersistenceUnit var1, Class var2) {
      if (var1.name().length() == 0) {
         this.addProcessingError("A class level PersistenceUnit annotation on class " + var2.getName() + " does not have the name attribute set.");
      }

   }

   protected void validateClassResource(Resource var1, Class var2) {
      if (var1.name().length() == 0) {
         this.addProcessingError("A class level Resource annotation on class " + var2.getName() + " does not have the name attribute set.");
      }

      if (var1.type() == Object.class) {
         this.addProcessingError("A class level Resource annotation on class " + var2.getName() + " does not have the type attribute set");
      }

   }

   protected void validateClassResource(EJB var1, Class var2) {
      if (var1.name().length() == 0) {
         this.addProcessingError("A class level EJB annotation on class " + var2.getName() + " does not have the name attribute set.");
      }

   }

   protected String getCompEnvJndiName(String var1, Field var2) {
      if (var1.length() == 0) {
         var1 = var2.getDeclaringClass().getName() + "/" + var2.getName();
      }

      return var1;
   }

   protected String getCompEnvJndiName(String var1, Method var2) {
      if (var1.length() == 0) {
         var1 = var2.getDeclaringClass().getName() + "/" + this.getPropertyName(var2);
      }

      return var1;
   }

   protected Class getEnvironmentType(Class var1, Field var2) {
      return var1 != Object.class ? var1 : var2.getType();
   }

   protected Class getEnvironmentType(Class var1, Method var2) {
      return var1 != Object.class ? var1 : var2.getParameterTypes()[0];
   }

   protected void addInjectionTarget(Field var1, InjectionTargetBean var2) {
      assert var1 != null;

      assert var2 != null;

      var2.setInjectionTargetClass(var1.getDeclaringClass().getName());
      var2.setInjectionTargetName(var1.getName());
   }

   protected void addInjectionTarget(Method var1, InjectionTargetBean var2) {
      assert var1 != null;

      assert var2 != null;

      var2.setInjectionTargetClass(var1.getDeclaringClass().getName());
      var2.setInjectionTargetName(this.getPropertyName(var1));
   }

   protected InjectionTargetBean findInjectionTargetInArray(String var1, String var2, InjectionTargetBean[] var3) {
      if (var3 == null) {
         return null;
      } else {
         InjectionTargetBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            InjectionTargetBean var7 = var4[var6];
            if (var1.equals(var7.getInjectionTargetClass()) && var2.equals(var7.getInjectionTargetName())) {
               return var7;
            }
         }

         return null;
      }
   }

   protected void log(String var1) {
      System.out.println(var1);
   }

   protected boolean isSet(String var1, Object var2) {
      return ((DescriptorBean)var2).isSet(var1);
   }

   protected void addProcessingError(String var1) {
      if (this.errors == null) {
         this.errors = new ErrorCollectionException();
      }

      AnnotationProcessException var2 = new AnnotationProcessException(var1);
      this.errors.add(var2);
   }

   protected void addFatalProcessingError(String var1) throws ErrorCollectionException {
      this.addProcessingError(var1);
      this.throwProcessingErrors();
   }

   protected void throwProcessingErrors() throws ErrorCollectionException {
      if (this.errors != null && !this.errors.isEmpty()) {
         throw this.errors;
      }
   }

   protected void addBeanInterfaceNotSetError(J2eeClientEnvironmentBean var1) {
      this.addProcessingError("@EJB annotation doesn't have beanInterface set");
   }

   protected void addDescriptorDefaults(Class var1, J2eeClientEnvironmentBean var2) {
      this.addLifecycleCallbackDefaults(var2.getPostConstructs(), var1.getName());
      this.addLifecycleCallbackDefaults(var2.getPreDestroys(), var1.getName());
   }

   private void addLifecycleCallbackDefaults(LifecycleCallbackBean[] var1, String var2) {
      LifecycleCallbackBean[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         LifecycleCallbackBean var6 = var3[var5];
         if (var6.getLifecycleCallbackClass() == null) {
            var6.setLifecycleCallbackClass(var2);
         }
      }

   }

   static {
      if (KernelStatus.isServer()) {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         productionMode = ManagementService.getRuntimeAccess(var0).getDomain().isProductionModeEnabled();
      }

   }
}
