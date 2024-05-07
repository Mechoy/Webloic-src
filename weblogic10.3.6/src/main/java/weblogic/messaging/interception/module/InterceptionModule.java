package weblogic.messaging.interception.module;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.AssociationBean;
import weblogic.j2ee.descriptor.wl.InterceptionBean;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.messaging.interception.MessageInterceptionService;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.AssociationHandle;
import weblogic.messaging.interception.interfaces.InterceptionPointNameDescriptionListener;
import weblogic.messaging.interception.interfaces.InterceptionService;
import weblogic.messaging.interception.interfaces.ProcessorHandle;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.classloaders.GenericClassLoader;

public class InterceptionModule implements Module, UpdateListener {
   private InterceptionBean wholeModule = null;
   private HashMap doLaterList = new HashMap();
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ApplicationContextInternal appCtx;
   private InterceptionParser parser;
   private final String uri;
   private InterceptionComponent interceptionComponent;

   public InterceptionModule(String var1) {
      this.uri = var1;
      this.parser = new InterceptionParser();
   }

   public String getId() {
      return this.uri;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_INTERCEPT;
   }

   public DescriptorBean[] getDescriptors() {
      return new DescriptorBean[0];
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[]{this.interceptionComponent};
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.init(var1, var2, var3);
   }

   public static String getCanonicalPath(ApplicationContextInternal var0, String var1) {
      File[] var2 = var0.getApplicationPaths();
      var1 = var2[0] + "/" + var1;
      return (new File(var1)).getAbsolutePath().replace(File.separatorChar, '/');
   }

   static void callAddAssociation(AssociationBean var0) throws InterceptionServiceException {
      InterceptionService var1 = MessageInterceptionService.getSingleton();
      var1.addAssociation(var0.getInterceptionPoint().getType(), var0.getInterceptionPoint().getNameSegment(), var0.getProcessor().getType(), var0.getProcessor().getName(), true);
   }

   private static void callRemoveAssociation(AssociationBean var0) throws InterceptionServiceException {
      InterceptionService var1 = MessageInterceptionService.getSingleton();
      AssociationHandle var2 = var1.getAssociationHandle(var0.getInterceptionPoint().getType(), var0.getInterceptionPoint().getNameSegment());
      if (var2 != null) {
         var1.removeAssociation(var2);
      }

   }

   public boolean loadConfiguration() throws InterceptionServiceException {
      InterceptionService var1 = MessageInterceptionService.getSingleton();

      int var2;
      for(var2 = 0; var2 < this.wholeModule.getProcessorTypes().length; ++var2) {
         try {
            var1.registerProcessorType(this.wholeModule.getProcessorTypes()[var2].getType(), this.wholeModule.getProcessorTypes()[var2].getFactory());
         } catch (InterceptionServiceException var6) {
         }
      }

      for(var2 = 0; var2 < this.wholeModule.getProcessors().length; ++var2) {
         var1.addProcessor(this.wholeModule.getProcessors()[var2].getType(), this.wholeModule.getProcessors()[var2].getName(), this.wholeModule.getProcessors()[var2].getMetadata());
      }

      for(var2 = 0; var2 < this.wholeModule.getAssociations().length; ++var2) {
         AssociationBean var3 = this.wholeModule.getAssociations()[var2];
         String var4 = var3.getInterceptionPoint().getType();
         if (var1.getInterceptionPointNameDescription(var4) == null) {
            DoLaterClass var5 = (DoLaterClass)this.doLaterList.get(var4);
            if (var5 == null) {
               var5 = new DoLaterClass(var4);
               this.doLaterList.put(var4, var5);
            }

            var5.addLater(var3);
         } else {
            callAddAssociation(var3);
         }
      }

      return true;
   }

   public boolean unloadConfiguration() throws InterceptionServiceException {
      InterceptionService var1 = MessageInterceptionService.getSingleton();

      int var2;
      for(var2 = 0; var2 < this.wholeModule.getAssociations().length; ++var2) {
         AssociationBean var3 = this.wholeModule.getAssociations()[var2];
         String var4 = var3.getInterceptionPoint().getType();
         if (var1.getInterceptionPointNameDescription(var4) == null) {
            DoLaterClass var5 = (DoLaterClass)this.doLaterList.get(var4);
            if (var5 != null) {
               var5.dontAddLater(var3);
            }
         } else {
            callRemoveAssociation(var3);
         }
      }

      for(var2 = 0; var2 < this.wholeModule.getProcessors().length; ++var2) {
         ProcessorHandle var6 = var1.getProcessorHandle(this.wholeModule.getProcessors()[var2].getType(), this.wholeModule.getProcessors()[var2].getName());
         if (var6 != null) {
            var1.removeProcessor(var6);
         }
      }

      return true;
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      boolean var4 = false;
      new LinkedList();
      this.appCtx = (ApplicationContextInternal)var1;
      var3.addUpdateListener(this);
      this.wholeModule = this.parser.createInterceptionDescriptor(this.appCtx, this.uri);

      try {
         this.interceptionComponent = new InterceptionComponent(this.appCtx.getApplicationId(), this.appCtx);
         this.interceptionComponent.begin();
         return var2;
      } catch (ManagementException var7) {
         throw new ModuleException("ERROR: Could not create JMSComponent", var7);
      }
   }

   public void prepare() {
   }

   public void activate() throws ModuleException {
      try {
         this.loadConfiguration();
      } catch (InterceptionServiceException var2) {
         throw new ModuleException("Cannot load interception configuration" + var2, var2);
      }
   }

   public void start() {
   }

   public void deactivate() throws ModuleException {
      try {
         this.unloadConfiguration();
      } catch (InterceptionServiceException var2) {
         throw new ModuleException("Cannot unload interception configuration");
      }
   }

   public void unprepare() {
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      var1.removeUpdateListener(this);

      try {
         this.interceptionComponent.end();
      } catch (ManagementException var3) {
         throw new ModuleException("unregister of InterceptionComponent failed", var3);
      }
   }

   public void remove() {
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
   }

   public void forceProductionToAdmin() {
   }

   public boolean acceptURI(String var1) {
      return ".".equals(var1) ? true : this.uri.equals(var1);
   }

   public void prepareUpdate(String var1) {
   }

   public void activateUpdate(String var1) throws ModuleException {
      this.deactivate();
      this.activate();
   }

   public void rollbackUpdate(String var1) {
   }

   private final class DoLaterClass implements InterceptionPointNameDescriptionListener {
      private String type = null;
      private LinkedList doLater = new LinkedList();
      private boolean registered = false;
      private InterceptionService service = MessageInterceptionService.getSingleton();

      public DoLaterClass(String var2) {
         this.type = var2;

         try {
            this.service.registerInterceptionPointNameDescriptionListener(this);
         } catch (InterceptionServiceException var4) {
            throw new AssertionError("Programmer error - exception when registering for notification with Interception Service" + var4);
         }
      }

      public void addLater(AssociationBean var1) throws InterceptionServiceException {
         if (this.registered) {
            InterceptionModule.callAddAssociation(var1);
         } else {
            this.doLater.add(var1);
         }

      }

      private boolean sameNamedAssociations(AssociationBean var1, AssociationBean var2) {
         String[] var3 = var1.getInterceptionPoint().getNameSegment();
         String[] var4 = var2.getInterceptionPoint().getNameSegment();
         if (var3.length != var4.length) {
            return false;
         } else {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               if (!var3[var5].equals(var4[var5])) {
                  return false;
               }
            }

            return true;
         }
      }

      public void dontAddLater(AssociationBean var1) {
         ListIterator var2 = this.doLater.listIterator();

         while(var2.hasNext()) {
            AssociationBean var3 = (AssociationBean)var2.next();
            if (this.sameNamedAssociations(var1, var3)) {
               this.doLater.remove(var3);
               break;
            }
         }

      }

      public final void addAssociationsFromList() {
         ListIterator var1 = this.doLater.listIterator(0);

         while(var1.hasNext()) {
            AssociationBean var2 = (AssociationBean)var1.next();

            try {
               InterceptionModule.callAddAssociation(var2);
            } catch (InterceptionServiceException var4) {
            }
         }

         this.doLater = null;
      }

      public void onRegister() {
         this.registered = true;

         try {
            SecurityServiceManager.runAs(InterceptionModule.KERNEL_ID, InterceptionModule.KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() {
                  DoLaterClass.this.addAssociationsFromList();
                  return null;
               }
            });
         } catch (PrivilegedActionException var2) {
            throw new AssertionError("addAssociations problem" + var2);
         }
      }

      public String getType() {
         return this.type;
      }
   }
}
