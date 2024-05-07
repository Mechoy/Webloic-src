package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarFile;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.stream.XMLStreamException;
import kodo.jdbc.conf.descriptor.PersistenceConfigurationBean;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.SplitDirectoryInfo;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.config.DefaultModule;
import weblogic.application.internal.AppClientModule;
import weblogic.application.internal.flow.ModuleListenerInvoker;
import weblogic.application.internal.flow.ScopedModuleDriver;
import weblogic.application.io.ExplodedJar;
import weblogic.application.io.JarCopyFilter;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryReference;
import weblogic.application.utils.CompositeWebAppFinder;
import weblogic.application.utils.PathUtils;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.EditableJ2eeApplicationObject;
import weblogic.deploy.api.model.WebLogicDeployableObjectFactory;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.ejb.container.deployer.EJBModule;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.j2ee.descriptor.ApplicationClientBean;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.PersistenceBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationClientBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.servlet.internal.War;
import weblogic.servlet.internal.WebAppModule;
import weblogic.servlet.utils.WebAppLibraryUtils;
import weblogic.utils.FileUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.application.WarDetector;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.FilteringClassLoader;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class LightWeightDeploymentViewerFlow extends CompilerFlow {
   private ApplicationContextInternal appCtx = null;
   private VirtualJarFile vjf = null;
   private EditableJ2eeApplicationObject deployableApplication = null;
   private GenericClassLoader appClassLoader = null;
   private File baseDir = null;

   public LightWeightDeploymentViewerFlow(CompilerCtx var1) {
      super(var1);
      String var2 = ".appmergegen_" + System.currentTimeMillis();
      this.baseDir = new File(J2EEApplicationService.getTempDir(), var2);
      if (this.baseDir.exists() && !this.baseDir.isDirectory()) {
         this.baseDir.delete();
      }

      this.baseDir.mkdirs();
   }

   private GenericClassLoader createAppClassLoader(String var1) {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      FilteringClassLoader var3 = new FilteringClassLoader(var2);
      GenericClassLoader var4 = new GenericClassLoader(new MultiClassFinder(), var3);
      var4.setAnnotation(new Annotation(var1));
      return var4;
   }

   private void initWebAppLibraryManager(LibraryManager var1, WeblogicWebAppBean var2, String var3) throws ToolFailureException {
      if (var2 != null) {
         if (var2.getLibraryRefs() != null) {
            LibraryReference[] var4 = WebAppLibraryUtils.getWebLibRefs(var2, var3);
            var1.lookup(var4);
         }
      }
   }

   public void compile() throws ToolFailureException {
      WebLogicDeployableObjectFactory var1 = this.ctx.getObjectFactory();
      if (var1 != null) {
         this.appCtx = ApplicationAccess.getApplicationAccess().getApplicationContext(this.ctx.getLightWeightAppName());
         this.appClassLoader = this.createAppClassLoader(this.ctx.getLightWeightAppName());
         DescriptorBean var3;
         if (this.appCtx.isEar()) {
            try {
               this.deployableApplication = var1.createApplicationObject();
               this.vjf = this.appCtx.getApplicationFileManager().getVirtualJarFile();
               DescriptorBean var2 = (DescriptorBean)this.appCtx.getApplicationDescriptor().getApplicationDescriptor();
               if (var2 != null) {
                  var3 = ((Descriptor)var2.getDescriptor().clone()).getRootBean();
                  this.deployableApplication.addRootBean("META-INF/application.xml", var3, (ModuleType)null);
                  this.deployableApplication.setRootBean(var3);
               }

               var2 = (DescriptorBean)this.appCtx.getApplicationDescriptor().getWeblogicApplicationDescriptor();
               if (var2 != null) {
                  var3 = ((Descriptor)var2.getDescriptor().clone()).getRootBean();
                  this.deployableApplication.addRootBean("META-INF/weblogic-application.xml", var3, (ModuleType)null);
               }

               var2 = (DescriptorBean)this.appCtx.getApplicationDescriptor().getWeblogicExtensionDescriptor();
               if (var2 != null) {
                  var3 = ((Descriptor)var2.getDescriptor().clone()).getRootBean();
                  this.deployableApplication.addRootBean("META-INF/weblogic-extension.xml", var3, (ModuleType)null);
               }

               Module[] var13 = this.appCtx.getApplicationModules();

               for(int var4 = 0; var4 < var13.length; ++var4) {
                  ModuleType var5 = WebLogicModuleType.getTypeFromString(var13[var4].getType());
                  if (var5.equals(WebLogicModuleType.WLDF)) {
                     DescriptorBean[] var6 = var13[var4].getDescriptors();
                     DescriptorBean var7 = ((Descriptor)var6[0].getDescriptor().clone()).getRootBean();
                     this.deployableApplication.addRootBean("META-INF/weblogic-diagnostics.xml", var7, var5);
                  } else {
                     EditableDeployableObject var18 = this.processModules(var1, var13[var4]);
                     if (var18 != null) {
                        this.deployableApplication.addDeployableObject(var18);
                     }
                  }
               }

               this.deployableApplication.setClassLoader(this.appClassLoader);
               ApplicationViewerFlow.ApplicationResourceFinder var15 = new ApplicationViewerFlow.ApplicationResourceFinder(this.appCtx.getEar().getURI(), this.appClassLoader.getClassFinder());
               this.deployableApplication.setResourceFinder(var15);
               this.deployableApplication.setVirtualJarFile(this.vjf);
            } catch (XMLStreamException var9) {
               this.deployableApplication = null;
            } catch (IOException var10) {
               this.deployableApplication = null;
            } catch (ToolFailureException var11) {
               this.deployableApplication = null;
            }

            this.ctx.setDeployableApplication(this.deployableApplication);
         } else {
            try {
               this.vjf = this.appCtx.getApplicationFileManager().getVirtualJarFile();
            } catch (IOException var8) {
               return;
            }

            EditableDeployableObject var12 = null;
            var3 = null;
            DescriptorBean[] var16 = null;
            Module[] var17 = this.appCtx.getApplicationModules();

            for(int var19 = 0; var19 < var17.length; ++var19) {
               ModuleType var20 = WebLogicModuleType.getTypeFromString(var17[var19].getType());
               if (var20.equals(WebLogicModuleType.WLDF)) {
                  var16 = (DescriptorBean[])var17[var19].getDescriptors().clone();
               } else {
                  var12 = this.processModules(var1, var17[var19]);
                  if (var12 != null) {
                     String var14 = var17[var19].getId();
                     break;
                  }
               }
            }

            if (var12 != null) {
               this.addModuleDescriptors(var12, (Module)null, var16, WebLogicModuleType.WLDF);
               var12.setVirtualJarFile(this.vjf);
            }

            this.ctx.setDeployableApplication(var12);
         }

      }
   }

   private EditableDeployableObject processModules(WebLogicDeployableObjectFactory var1, Module var2) throws ToolFailureException {
      try {
         EditableDeployableObject var3 = null;
         ModuleType var4 = WebLogicModuleType.getTypeFromString(var2.getType());
         DescriptorBean[] var5 = var2.getDescriptors();
         if (var4.equals(ModuleType.EJB)) {
            return this.processEJBModule(var1, var2);
         } else if (var4.equals(ModuleType.WAR)) {
            return this.processWARModule(var1, var2);
         } else if (var4.equals(WebLogicModuleType.CONFIG)) {
            return this.processCustomModule(var1, var2);
         } else if (var4.equals(WebLogicModuleType.CAR)) {
            return this.processCARModule(var1, var2);
         } else {
            if (var4.equals(WebLogicModuleType.JDBC)) {
               var3 = var1.createDeployableObject(var2.getId(), (String)null, var4);
            } else if (var4.equals(WebLogicModuleType.JMS)) {
               var3 = var1.createDeployableObject(var2.getId(), (String)null, var4);
            } else {
               if (!var4.equals(WebLogicModuleType.RAR)) {
                  if (var4.equals(WebLogicModuleType.WLDF)) {
                     return null;
                  }

                  return null;
               }

               var3 = var1.createDeployableObject(var2.getId(), (String)null, var4);
            }

            this.addModuleDescriptors(var3, var2, var5, var4);
            var3.setClassLoader(this.appCtx.getAppClassLoader());
            var3.setVirtualJarFile(this.vjf);
            return var3;
         }
      } catch (IOException var6) {
         return null;
      }
   }

   private EditableDeployableObject processEJBModule(WebLogicDeployableObjectFactory var1, Module var2) throws ToolFailureException {
      ModuleType var3 = WebLogicModuleType.getTypeFromString(var2.getType());
      if (!var3.equals(ModuleType.EJB)) {
         return null;
      } else if (!(var2 instanceof ModuleListenerInvoker)) {
         throw new ToolFailureException("unknown module");
      } else {
         Module var4 = ((ModuleListenerInvoker)var2).getDelegate();
         if (!(var4 instanceof EJBModule)) {
            throw new ToolFailureException("unknown module");
         } else {
            EJBModule var5 = (EJBModule)var4;

            try {
               EditableDeployableObject var6 = var1.createDeployableObject(var5.getURI(), var5.getAltDD(), var3);
               DescriptorBean[] var7 = var5.getDescriptors();
               this.addModuleDescriptors(var6, var2, var7, var3);
               MultiClassFinder var8 = new MultiClassFinder();
               String var9 = this.appCtx.getApplicationFileManager().getSourcePath(var5.getURI()).getParent();
               var8.addFinder(new ClasspathClassFinder2(var9));
               if (this.appClassLoader != null) {
                  this.appClassLoader.addClassFinder(var8);
                  var6.setClassLoader(new GenericClassLoader(this.appClassLoader));
               } else {
                  var6.setClassLoader(new GenericClassLoader(var8));
               }

               File var10 = this.appCtx.getApplicationFileManager().getSourcePath(var5.getURI());
               VirtualJarFile var11 = VirtualJarFactory.createVirtualJar(var10);
               var6.setVirtualJarFile(var11);
               return var6;
            } catch (IOException var12) {
               throw new ToolFailureException("unable process EJB module", var12);
            }
         }
      }
   }

   private EditableDeployableObject processWARModule(WebLogicDeployableObjectFactory var1, Module var2) throws ToolFailureException {
      ModuleType var3 = WebLogicModuleType.getTypeFromString(var2.getType());
      if (!var3.equals(ModuleType.WAR)) {
         return null;
      } else if (!(var2 instanceof ModuleListenerInvoker)) {
         throw new ToolFailureException("unknown module");
      } else {
         Module var4 = ((ModuleListenerInvoker)var2).getDelegate();
         DescriptorBean var10;
         MultiClassFinder var11;
         File var12;
         File var13;
         boolean var14;
         VirtualJarFile var15;
         ExplodedJar var16;
         SplitDirectoryInfo var17;
         String[] var18;
         War var19;
         CompositeWebAppFinder var28;
         LibraryManager var34;
         GenericClassLoader var35;
         VirtualJarFile var36;
         if (var4 instanceof ScopedModuleDriver) {
            Module var5 = ((ScopedModuleDriver)var4).getDelegate();
            Map var6 = ((ScopedModuleDriver)var4).getDescriptorMappings();
            if (var5 instanceof WebAppModule) {
               WebAppModule var25 = (WebAppModule)var5;

               try {
                  EditableDeployableObject var26 = var1.createDeployableObject(var25.getModuleURI(), (String)null, var3);
                  WeblogicWebAppBean var27 = null;
                  DescriptorBean var32;
                  DescriptorBean var33;
                  if (var6 != null) {
                     Iterator var29 = var6.keySet().iterator();

                     while(var29.hasNext()) {
                        String var30 = (String)var29.next();
                        var32 = (DescriptorBean)var6.get(var30);
                        var33 = ((Descriptor)var32.getDescriptor().clone()).getRootBean();
                        var26.addRootBean(var30, var33, var3);
                        if (var30.equals("WEB-INF/web.xml")) {
                           var26.setRootBean(var33);
                        } else if (var30.equals("WEB-INF/weblogic.xml")) {
                           var27 = (WeblogicWebAppBean)var33;
                        }
                     }
                  } else {
                     var10 = (DescriptorBean)var25.getWebAppBean();
                     DescriptorBean var31 = ((Descriptor)var10.getDescriptor().clone()).getRootBean();
                     var26.setRootBean(var31);
                     var26.addRootBean("WEB-INF/web.xml", var31, var3);
                     var32 = (DescriptorBean)var25.getWlWebAppBean();
                     if (var32 != null) {
                        var33 = ((Descriptor)var32.getDescriptor().clone()).getRootBean();
                        var26.addRootBean("WEB-INF/weblogic.xml", var33, var3);
                        var27 = (WeblogicWebAppBean)var33;
                     }
                  }

                  var28 = new CompositeWebAppFinder();
                  var11 = new MultiClassFinder();
                  var12 = new File(this.baseDir, PathUtils.generateTempPath((String)null, this.ctx.getLightWeightAppName(), var25.getModuleURI()));
                  var13 = this.appCtx.getApplicationFileManager().getSourcePath(var25.getModuleURI());
                  var14 = !var13.isDirectory() && WarDetector.instance.suffixed(var13.getName());
                  var15 = this.appCtx.getApplicationFileManager().getVirtualJarFile(var25.getModuleURI());
                  var16 = null;
                  if (var14) {
                     var16 = new ExplodedJar(var25.getModuleURI(), var12, var15.getRootFiles()[0], War.WAR_CLASSPATH_INFO);
                  } else {
                     var16 = new ExplodedJar(var25.getModuleURI(), var12, var15.getRootFiles(), War.WAR_CLASSPATH_INFO, JarCopyFilter.NOCOPY_FILTER);
                  }

                  var28.addFinder(var16.getClassFinder());
                  var11.addFinder(new ApplicationViewerFlow.ApplicationResourceFinder(var25.getModuleURI(), var16.getClassFinder()));
                  var17 = this.appCtx.getSplitDirectoryInfo();
                  if (var17 != null) {
                     var18 = var17.getWebAppClasses(var25.getModuleURI());
                     if (var18 != null && var18.length > 0) {
                        var28.addFinder(new ClasspathClassFinder2(StringUtils.join(var18, File.pathSeparator)));
                     }
                  }

                  if (null != var27) {
                     var34 = new LibraryManager(WebAppLibraryUtils.getLibraryReferencer(var25.getModuleURI()));
                     this.initWebAppLibraryManager(var34, var27, var25.getModuleURI());
                     if (var34.hasReferencedLibraries()) {
                        var19 = new War(var25.getModuleURI());
                        WebAppLibraryUtils.extractWebAppLibraries(var34, var19, var12);
                        var28.addLibraryFinder(var19.getClassFinder());
                        var11.addFinder(var19.getResourceFinder("/"));
                     }
                  }

                  if (this.appClassLoader != null) {
                     var35 = new GenericClassLoader(var28, this.appClassLoader);
                     var26.setClassLoader(var35);
                  } else {
                     var35 = new GenericClassLoader(var28);
                     var26.setClassLoader(var35);
                  }

                  var26.setResourceFinder(var11);
                  if (var14) {
                     var36 = VirtualJarFactory.createVirtualJar(new JarFile(var13));
                     var26.setVirtualJarFile(var36);
                  } else {
                     var26.setVirtualJarFile(var15);
                  }

                  return var26;
               } catch (IOException var20) {
                  throw new ToolFailureException("unable process WAR module", var20);
               } catch (Exception var21) {
                  throw new ToolFailureException("unable process WAR module", var21);
               }
            }
         }

         if (var4 instanceof WebAppModule) {
            WebAppModule var23 = (WebAppModule)var4;

            try {
               EditableDeployableObject var24 = var1.createDeployableObject(var23.getModuleURI(), (String)null, var3);
               DescriptorBean var7 = (DescriptorBean)var23.getWebAppBean();
               DescriptorBean var8 = ((Descriptor)var7.getDescriptor().clone()).getRootBean();
               var24.setRootBean(var8);
               var24.addRootBean("WEB-INF/web.xml", var8, var3);
               DescriptorBean var9 = (DescriptorBean)var23.getWlWebAppBean();
               if (var9 != null) {
                  var10 = ((Descriptor)var9.getDescriptor().clone()).getRootBean();
                  var24.addRootBean("WEB-INF/weblogic.xml", var10, var3);
               }

               var28 = new CompositeWebAppFinder();
               var11 = new MultiClassFinder();
               var12 = new File(this.baseDir, PathUtils.generateTempPath((String)null, this.ctx.getLightWeightAppName(), var23.getModuleURI()));
               var13 = this.appCtx.getApplicationFileManager().getSourcePath(var23.getModuleURI());
               var14 = !var13.isDirectory() && WarDetector.instance.suffixed(var13.getName());
               var15 = this.appCtx.getApplicationFileManager().getVirtualJarFile(var23.getModuleURI());
               var16 = null;
               if (var14) {
                  var16 = new ExplodedJar(var23.getModuleURI(), var12, var15.getRootFiles()[0], War.WAR_CLASSPATH_INFO);
               } else {
                  var16 = new ExplodedJar(var23.getModuleURI(), var12, var15.getRootFiles(), War.WAR_CLASSPATH_INFO, JarCopyFilter.NOCOPY_FILTER);
               }

               var28.addFinder(var16.getClassFinder());
               var11.addFinder(new ApplicationViewerFlow.ApplicationResourceFinder(var23.getModuleURI(), var16.getClassFinder()));
               var17 = this.appCtx.getSplitDirectoryInfo();
               if (var17 != null) {
                  var18 = var17.getWebAppClasses(var23.getModuleURI());
                  if (var18 != null && var18.length > 0) {
                     var28.addFinder(new ClasspathClassFinder2(StringUtils.join(var18, File.pathSeparator)));
                  }
               }

               if (null != var9) {
                  var34 = new LibraryManager(WebAppLibraryUtils.getLibraryReferencer(var23.getModuleURI()));
                  this.initWebAppLibraryManager(var34, (WeblogicWebAppBean)var9, var23.getModuleURI());
                  if (var34.hasReferencedLibraries()) {
                     var19 = new War(var23.getModuleURI());
                     WebAppLibraryUtils.extractWebAppLibraries(var34, var19, var12);
                     var28.addLibraryFinder(var19.getClassFinder());
                     var11.addFinder(var19.getResourceFinder("/"));
                  }
               }

               if (this.appClassLoader != null) {
                  var35 = new GenericClassLoader(var28, this.appClassLoader);
                  var24.setClassLoader(var35);
               } else {
                  var35 = new GenericClassLoader(var28);
                  var24.setClassLoader(var35);
               }

               var24.setResourceFinder(var11);
               if (var14) {
                  var36 = VirtualJarFactory.createVirtualJar(new JarFile(var13));
                  var24.setVirtualJarFile(var36);
               } else {
                  var24.setVirtualJarFile(var15);
               }

               return var24;
            } catch (IOException var22) {
               throw new ToolFailureException("unable process WAR module", var22);
            }
         } else {
            throw new ToolFailureException("unable process WAR module");
         }
      }
   }

   private EditableDeployableObject processCustomModule(WebLogicDeployableObjectFactory var1, Module var2) throws ToolFailureException {
      ModuleType var3 = WebLogicModuleType.getTypeFromString(var2.getType());
      if (!var3.equals(WebLogicModuleType.CONFIG)) {
         return null;
      } else if (!(var2 instanceof ModuleListenerInvoker)) {
         throw new ToolFailureException("unknown module");
      } else {
         Module var4 = ((ModuleListenerInvoker)var2).getDelegate();
         if (var4 instanceof DefaultModule) {
            DefaultModule var5 = (DefaultModule)var4;
            Map var6 = var5.getDescriptorMappings();
            if (var6 != null) {
               Iterator var7 = var6.keySet().iterator();

               while(var7.hasNext()) {
                  String var8 = (String)var7.next();
                  DescriptorBean var9 = (DescriptorBean)var6.get(var8);
                  DescriptorBean var10 = ((Descriptor)var9.getDescriptor().clone()).getRootBean();
                  this.deployableApplication.addRootBean(var8, var10, var3);
               }
            }
         } else if (var4.getDescriptors() != null && var4.getDescriptors().length > 0) {
            DescriptorBean var11 = ((Descriptor)var4.getDescriptors()[0].getDescriptor().clone()).getRootBean();
            this.deployableApplication.addRootBean(var4.getId(), var11, var3);
         }

         return null;
      }
   }

   private EditableDeployableObject processCARModule(WebLogicDeployableObjectFactory var1, Module var2) throws ToolFailureException {
      ModuleType var3 = WebLogicModuleType.getTypeFromString(var2.getType());
      if (!var3.equals(WebLogicModuleType.CAR)) {
         return null;
      } else if (!(var2 instanceof ModuleListenerInvoker)) {
         throw new ToolFailureException("unknown module");
      } else {
         Module var4 = ((ModuleListenerInvoker)var2).getDelegate();
         if (!(var4 instanceof AppClientModule)) {
            throw new ToolFailureException("unknown module");
         } else {
            AppClientModule var5 = (AppClientModule)var4;
            if (var5.getDescriptors() != null && var5.getDescriptors().length > 0) {
               DescriptorBean var6 = ((Descriptor)var5.getDescriptors()[0].getDescriptor().clone()).getRootBean();
               this.deployableApplication.addRootBean(var5.getId(), var6, var3);
            }

            return null;
         }
      }
   }

   private void addModuleDescriptors(EditableDeployableObject var1, Module var2, DescriptorBean[] var3, ModuleType var4) throws ToolFailureException {
      if (var3 != null) {
         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5] != null) {
               DescriptorBean var6 = ((Descriptor)var3[var5].getDescriptor().clone()).getRootBean();
               if (var6 instanceof WebAppBean) {
                  var1.setRootBean(var6);
                  var1.addRootBean("WEB-INF/web.xml", var6, var4);
               } else if (var6 instanceof WeblogicWebAppBean) {
                  var1.addRootBean("WEB-INF/weblogic.xml", var6, var4);
               } else if (var6 instanceof EjbJarBean) {
                  var1.setRootBean(var6);
                  var1.addRootBean("META-INF/ejb-jar.xml", var6, var4);
               } else if (var6 instanceof WeblogicEjbJarBean) {
                  var1.addRootBean("META-INF/weblogic-ejb-jar.xml", var6, var4);
               } else if (var6 instanceof ConnectorBean) {
                  var1.setRootBean(var6);
                  var1.addRootBean("META-INF/ra.xml", var6, var4);
               } else if (var6 instanceof WeblogicConnectorBean) {
                  var1.addRootBean("META-INF/weblogic-ra.xml", var6, var4);
               } else if (var6 instanceof ApplicationClientBean) {
                  var1.setRootBean(var6);
                  var1.addRootBean("META-INF/application-client.xml", var6, var4);
               } else if (var6 instanceof WeblogicApplicationClientBean) {
                  var1.addRootBean("META-INF/weblogic-application-client.xml", var6, var4);
               } else if (var6 instanceof WeblogicRdbmsJarBean) {
                  var1.addRootBean("META-INF/weblogic-cmp-rdbms-jar.xml", var6, var4);
               } else if (var6 instanceof WLDFResourceBean) {
                  var1.addRootBean("META-INF/weblogic-diagnostics.xml", var6, var4);
               } else if (var6 instanceof PersistenceBean) {
                  var1.addRootBean("META-INF/persistence.xml", var6, var4);
               } else if (var6 instanceof PersistenceConfigurationBean) {
                  var1.addRootBean("META-INF/persistence-configuration.xml", var6, var4);
               } else if (var6 instanceof WebservicesBean) {
                  if (var4.equals(ModuleType.WAR)) {
                     var1.addRootBean("WEB-INF/webservices.xml", var6, var4);
                  }

                  if (var4.equals(ModuleType.EJB)) {
                     var1.addRootBean("META-INF/webservices.xml", var6, var4);
                  }
               } else if (var6 instanceof WeblogicWebservicesBean) {
                  if (var4.equals(ModuleType.WAR)) {
                     var1.addRootBean("WEB-INF/weblogic-webservices.xml", var6, var4);
                  }

                  if (var4.equals(ModuleType.EJB)) {
                     var1.addRootBean("META-INF/weblogic-webservices.xml", var6, var4);
                  }
               } else if (var6 instanceof WebservicePolicyRefBean) {
                  if (var4.equals(ModuleType.WAR)) {
                     var1.addRootBean("WEB-INF/weblogic-webservices-policy.xml", var6, var4);
                  }

                  if (var4.equals(ModuleType.EJB)) {
                     var1.addRootBean("META-INF/weblogic-webservices-policy.xml", var6, var4);
                  }
               } else if (var6 instanceof JMSBean) {
                  var1.addRootBean(var2.getId(), var6, var4);
                  var1.setRootBean(var6);
               } else {
                  if (!(var6 instanceof JDBCDataSourceBean)) {
                     throw new ToolFailureException("unable process unknown descriptors");
                  }

                  var1.addRootBean(var2.getId(), var6, var4);
                  var1.setRootBean(var6);
               }
            }
         }

      }
   }

   public void cleanup() {
      if (this.appClassLoader != null) {
         this.appClassLoader.close();
      }

      if (this.baseDir.exists()) {
         FileUtils.remove(this.baseDir);
      }

   }
}
