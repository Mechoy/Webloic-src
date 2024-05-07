package weblogic.wsee.tools.jws.jaxws;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.sun.istack.ws.AnnotationProcessorFactoryImpl;
import com.sun.tools.ws.ant.Apt;
import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.server.ContainerResolver;
import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.api.wsdl.writer.WSDLGeneratorExtension;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.model.AbstractSEIModelImpl;
import com.sun.xml.ws.model.RuntimeModeler;
import com.sun.xml.ws.util.ServiceFinder;
import com.sun.xml.ws.wsdl.writer.WSDLGenerator;
import com.sun.xml.ws.wsdl.writer.WSDLResolver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Holder;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.j2ee.descriptor.wl.WebserviceAddressBean;
import weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.jws.security.UserDataConstraint;
import weblogic.utils.FileUtils;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.tools.TempDirManager;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.anttasks.AntUtil;
import weblogic.wsee.tools.anttasks.JwscTask;
import weblogic.wsee.tools.jws.JWSProcessor;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.WebServiceInfo;
import weblogic.wsee.tools.jws.decl.PolicyDecl;
import weblogic.wsee.tools.jws.decl.PolicyDeclBuilder;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.logging.BuildListenerLogger;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.util.JamUtil;

public class JAXWSProcessor implements JWSProcessor {
   private final List<WebServiceDecl> webServices = new ArrayList();
   private Project project;
   private ModuleInfo moduleInfo;
   private static final String TRANSPORT_GUARANTEE_INTEGRAL = "INTEGRAL";
   private static final String TRANSPORT_GUARANTEE_CONFIDENTIAL = "CONFIDENTIAL";

   public void init(ModuleInfo var1) throws WsBuildException {
      this.moduleInfo = var1;
      this.project = new Project();
      this.project.setBasedir(var1.getOutputDir().getAbsolutePath());
      this.project.addBuildListener(new BuildListenerLogger(var1.getJwsBuildContext().getLogger()));
   }

   private Path createPath(String[] var1) {
      Path var2 = new Path(this.project);
      if (var1 != null) {
         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            var2.createPathElement().setPath(var6);
         }
      }

      return var2;
   }

   public void process(WebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().getType() == WebServiceType.JAXWS) {
         this.webServices.add(var1.getWebService());
      }
   }

   public void finish() throws WsBuildException {
      if (!this.webServices.isEmpty()) {
         this.moduleInfo.getJwsBuildContext().getLogger().log(EventLevel.INFO, "Processing " + this.webServices.size() + " JAX-WS web services...");
         TempDirManager var1 = new TempDirManager(this.project);

         try {
            Iterator var3;
            WebServiceDecl var4;
            if (this.moduleInfo.isWsdlOnly()) {
               File var2 = var1.createTempDir("apt", this.moduleInfo.getOutputDir());
               this.runApt(var2, true);
               var3 = this.webServices.iterator();

               while(var3.hasNext()) {
                  var4 = (WebServiceDecl)var3.next();
                  this.generateWsdl(this.moduleInfo.getOutputDir(), var2, var4);
               }
            } else {
               boolean var21 = this.moduleInfo.isGenerateWsdl();
               var3 = this.webServices.iterator();

               while(var3.hasNext()) {
                  var4 = (WebServiceDecl)var3.next();
                  if (var4.isGenerateWsdl()) {
                     var21 = true;
                  }
               }

               this.runApt(this.moduleInfo.getOutputDir(), var21);
               boolean var22 = this.moduleInfo.isGenerateDescriptors();
               Iterator var23 = this.webServices.iterator();

               WebServiceDecl var5;
               while(var23.hasNext()) {
                  var5 = (WebServiceDecl)var23.next();
                  if (var5.isGenerateDescriptors()) {
                     var22 = true;
                  }

                  if (var5.isEjb() && this.moduleInfo.getJwsBuildContext().getProperties().get("jwsc.contextPathOverride") != null) {
                     var22 = true;
                     this.moduleInfo.setGenerateDescriptors(true);
                  }
               }

               if (var22) {
                  WebservicesBean var24 = this.moduleInfo.getWebServicesBean();
                  if (var24 == null) {
                     EditableDescriptorManager var25 = new EditableDescriptorManager();
                     var24 = (WebservicesBean)var25.createDescriptorRoot(WebservicesBean.class).getRootBean();
                     this.moduleInfo.setWebServicesBean(var24);
                  }

                  var24.setVersion("1.2");
                  Iterator var26 = this.webServices.iterator();

                  label255:
                  while(true) {
                     WebServiceDecl var6;
                     do {
                        if (!var26.hasNext()) {
                           WeblogicWebservicesBean var27 = this.moduleInfo.getWeblogicWebservicesBean();
                           if (var27 == null) {
                              EditableDescriptorManager var28 = new EditableDescriptorManager();
                              var27 = (WeblogicWebservicesBean)var28.createDescriptorRoot(WeblogicWebservicesBean.class).getRootBean();
                              this.moduleInfo.setWeblogicWebservicesBean(var27);
                           }

                           Iterator var29 = this.webServices.iterator();

                           while(true) {
                              WebServiceDecl var7;
                              do {
                                 if (!var29.hasNext()) {
                                    WebservicePolicyRefBean var30 = this.moduleInfo.getWebservicePolicyRefBean();
                                    if (var30 == null) {
                                       EditableDescriptorManager var31 = new EditableDescriptorManager();
                                       var30 = (WebservicePolicyRefBean)var31.createDescriptorRoot(WebservicePolicyRefBean.class).getRootBean();
                                       this.moduleInfo.setWebservicePolicyRefBean(var30);
                                    }
                                    break label255;
                                 }

                                 var7 = (WebServiceDecl)var29.next();
                              } while(!this.moduleInfo.isGenerateDescriptors() && !var7.isGenerateDescriptors());

                              WebserviceDescriptionBean var8 = var27.createWebserviceDescription();
                              fillWebservice(var7, var8);
                           }
                        }

                        var6 = (WebServiceDecl)var26.next();
                     } while(!this.moduleInfo.isGenerateDescriptors() && !var6.isGenerateDescriptors());

                     fillWebservice(var6, var24);
                  }
               }

               var23 = this.webServices.iterator();

               while(true) {
                  do {
                     if (!var23.hasNext()) {
                        return;
                     }

                     var5 = (WebServiceDecl)var23.next();
                     File var32 = new File(this.moduleInfo.getOutputDir(), "policies");
                     var32.mkdirs();

                     try {
                        List var33 = PolicyDeclBuilder.build(var5.getSourceFile(), var5.getJClass());
                        copyPolicies(var5, var32, var33.iterator());
                        List var34 = getWebMethods(var5.getJClass());
                        Iterator var9 = var34.iterator();

                        while(var9.hasNext()) {
                           JMethod var10 = (JMethod)var9.next();
                           List var11 = PolicyDeclBuilder.build(var5.getSourceFile(), var10);
                           copyPolicies(var5, var32, var11.iterator());
                        }
                     } catch (IOException var17) {
                        throw new WsBuildException(var17);
                     }
                  } while(!this.moduleInfo.isGenerateWsdl() && !var5.isGenerateWsdl());

                  this.generateWsdl(this.moduleInfo.getOutputDir(), var5);
               }
            }
         } catch (IOException var18) {
            throw new WsBuildException("Error creating temp directory", var18);
         } catch (Throwable var19) {
            throw new WsBuildException("Error processing JAX-WS web services", var19);
         } finally {
            var1.cleanup();
         }

      }
   }

   private void generateWsdl(File var1, WebServiceDecl var2) {
      this.generateWsdl(var1, (File)null, var2);
   }

   private void generateWsdl(final File var1, File var2, WebServiceDecl var3) {
      Path var4 = this.createClassPath(var1, (Holder)null);
      if (var2 != null) {
         var4.createPathElement().setLocation(var2);
      }

      ClassLoader var5 = this.createClassLoader(var4);
      final ArrayList var6 = new ArrayList();
      boolean var21 = false;

      try {
         var21 = true;
         Class var7 = Class.forName(var3.getJClass().getQualifiedName(), true, var5);
         BindingID var8 = BindingID.parse(var3.getProtocolBinding());
         RuntimeModeler var9 = new RuntimeModeler(var7, var3.getServiceQName(), var8);
         var9.setClassLoader(var5);
         if (var3.getPortQName() != null) {
            var9.setPortName(var3.getPortQName());
         }

         AbstractSEIModelImpl var10 = var9.buildRuntimeModel();
         WebServiceFeatureList var11 = new WebServiceFeatureList(var7);
         WLSProvider.parseAnnotations(var11, var7, true);
         WSDLGenerator var12 = new WSDLGenerator(var10, new WSDLResolver() {
            private File toFile(String var1x) {
               return new File(var1, var1x);
            }

            private Result toResult(File var1x) {
               try {
                  FileOutputStream var3 = new FileOutputStream(var1x);
                  var6.add(var3);
                  StreamResult var2 = new StreamResult(var3);
                  var2.setSystemId(var1x.getPath().replace('\\', '/'));
                  return var2;
               } catch (FileNotFoundException var4) {
                  throw new BuildException(var4);
               }
            }

            public Result getWSDL(String var1x) {
               File var2 = this.toFile(var1x);
               return this.toResult(var2);
            }

            public Result getSchemaOutput(String var1x, String var2) {
               if (var1x == null) {
                  return null;
               } else {
                  File var3 = this.toFile(var2);
                  return this.toResult(var3);
               }
            }

            public Result getAbstractWSDL(Holder<String> var1x) {
               return this.toResult(this.toFile((String)var1x.value));
            }

            public Result getSchemaOutput(String var1x, Holder<String> var2) {
               return this.getSchemaOutput(var1x, (String)var2.value);
            }
         }, var8.createBinding(var11.toArray()), ContainerResolver.getInstance().getContainer(), var7, (WSDLGeneratorExtension[])ServiceFinder.find(WSDLGeneratorExtension.class).toArray()) {
            protected String mangleName(String var1) {
               return var1;
            }
         };
         var12.doGeneration();
         var21 = false;
      } catch (ClassNotFoundException var24) {
         throw new BuildException(var24);
      } finally {
         if (var21) {
            Iterator var14 = var6.iterator();

            while(var14.hasNext()) {
               FileOutputStream var15 = (FileOutputStream)var14.next();

               try {
                  var15.close();
               } catch (IOException var22) {
               }
            }

         }
      }

      Iterator var26 = var6.iterator();

      while(var26.hasNext()) {
         FileOutputStream var27 = (FileOutputStream)var26.next();

         try {
            var27.close();
         } catch (IOException var23) {
         }
      }

   }

   private ClassLoader createClassLoader(Path var1) throws BuildException {
      try {
         ArrayList var2 = new ArrayList();
         String[] var3 = var1.list();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            var2.add(this.project.resolveFile(var6).toURI().toURL());
         }

         return new URLClassLoader((URL[])var2.toArray(new URL[var2.size()]), JAXWSProcessor.class.getClassLoader());
      } catch (MalformedURLException var7) {
         throw new BuildException(var7);
      }
   }

   private Path createClassPath(File var1, Holder<Boolean> var2) {
      Path var3 = this.createPath(this.moduleInfo.getJwsBuildContext().getClasspath());
      var3 = var3.concatSystemClasspath();
      var3.createPathElement().setLocation(var1);
      Iterator var4 = this.moduleInfo.getJwsBuildContext().getClientGenOutputDirs().iterator();

      while(var4.hasNext()) {
         File var5 = (File)var4.next();
         var3.createPathElement().setLocation(var5);
      }

      var4 = this.webServices.iterator();

      while(var4.hasNext()) {
         WebServiceDecl var6 = (WebServiceDecl)var4.next();
         if (var6.getCowReader() != null) {
            var3.createPathElement().setLocation(var6.getCowReader().getCowFile());
         } else if (var2 != null) {
            var2.value = true;
         }
      }

      return var3;
   }

   private void runApt(File var1, boolean var2) {
      Path var3 = this.createPath(this.moduleInfo.getJwsBuildContext().getSourcepath());
      var3.createPathElement().setLocation(this.moduleInfo.getOutputDir());
      Holder var4 = new Holder();
      var4.value = false;
      Path var5 = this.createClassPath(var1, var4);
      if ((Boolean)var4.value) {
         Apt var6 = new Apt();
         var6.setProject(this.project);
         var6.setClasspath(var5);
         var6.setDestdir(var1);
         var6.setSourcedestdir(var1);
         var6.setFactory(AnnotationProcessorFactoryImpl.class.getName());
         var6.setNocompile(!var2);
         var6.setSourcepath(var3);
         var6.setEncoding(this.moduleInfo.getJwsBuildContext().getSrcEncoding());
         if (this.moduleInfo.getOwningTask() != null) {
            JwscTask var7 = this.moduleInfo.getOwningTask();
            if (var7.getFork()) {
               String var8 = var7.getMemoryInitialSize();
               if (var8 != null) {
                  var6.createJvmarg().setValue("-Xms" + var8);
               }

               String var9 = var7.getMemoryMaximumSize();
               if (var9 != null) {
                  var6.createJvmarg().setValue("-Xmx" + var9);
               }
            }

            var6.setVerbose(var7.getVerbose());
            var6.setDebug(var7.getDebug());
            if (var7.getDebugLevel() != null) {
               var6.setDebuglevel(var7.getDebugLevel());
            }
         }

         var6.setFork(true);
         Iterator var10 = AntUtil.getFileSets(this.getSourceFiles(), this.project).iterator();

         while(var10.hasNext()) {
            FileSet var12 = (FileSet)var10.next();
            var6.addConfiguredSource(var12);
         }

         Apt.Option var11 = var6.createOption();
         var11.setKey("doNotOverWrite");
         var11.setValue("true");
         Apt.Option var13 = var6.createOption();
         var13.setKey("ignoreNoWebServiceFoundWarning");
         var13.setValue("true");
         var6.execute();
      }

   }

   private File[] getSourceFiles() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.webServices.iterator();

      while(var2.hasNext()) {
         WebServiceDecl var3 = (WebServiceDecl)var2.next();
         var1.add(var3.getSourceFile());
      }

      return (File[])var1.toArray(new File[var1.size()]);
   }

   private static void fillWebservice(WebServiceDecl var0, WebservicesBean var1) {
      weblogic.j2ee.descriptor.WebserviceDescriptionBean var2 = var1.createWebserviceDescription();
      var2.setWebserviceDescriptionName(var0.getServiceName() == null ? var0.getJClass().getQualifiedName() : var0.getServiceName());
      String var3 = getWsdlFile(var0);
      if (var3 != null && var3.length() > 0 && (var0.isGenerateWsdl() || var0.getCowReader() != null)) {
         var2.setWsdlFile(var3);
      }

      Iterator var4 = var0.getDDPorts();

      while(var4.hasNext()) {
         PortDecl var5 = (PortDecl)var4.next();
         PortComponentBean var6 = var2.createPortComponent();
         var6.setPortComponentName(var5.getPortName());
         var6.setWsdlPort(new QName(var0.getTargetNamespace(), var5.getPortName()));
         String var7 = var0.getEndPointInterface();
         if (var7 != null && var7.length() > 0) {
            var6.setServiceEndpointInterface(var7);
         }

         ServiceImplBeanBean var8 = var6.createServiceImplBean();
         if (var0.isEjb()) {
            var8.setEjbLink(var0.getDeployedName());
         } else {
            var8.setServletLink(var0.getDeployedName() + var5.getProtocol());
         }
      }

   }

   private static String getRoot(WebServiceDecl var0) {
      return var0.isEjb() ? "META-INF/" : "WEB-INF/";
   }

   private static String getWsdlFile(WebServiceDecl var0) {
      if (var0.getCowReader() == null) {
         return getRoot(var0) + var0.getWsdlFile();
      } else {
         String var1 = var0.getWsdlLocation();
         if (var1 != null && var0.isEjb() && var1.charAt(0) == '/') {
            var1 = var1.substring(1);
         }

         return var1;
      }
   }

   private static void fillWebservice(WebServiceDecl var0, WebserviceDescriptionBean var1) {
      Iterator var2 = var0.getDDPorts();

      while(var2.hasNext()) {
         PortDecl var3 = (PortDecl)var2.next();
         weblogic.j2ee.descriptor.wl.PortComponentBean var4 = var1.createPortComponent();
         var1.setWebserviceDescriptionName(var0.getServiceName() != null ? var0.getServiceName() : var0.getJClass().getQualifiedName());
         var1.setWebserviceType(var0.getType().toString());
         fillPortComponent(var0, var3, var4);
      }

   }

   private static void fillPortComponent(WebServiceDecl var0, PortDecl var1, weblogic.j2ee.descriptor.wl.PortComponentBean var2) {
      UserDataConstraint.Transport var3 = var0.getWebServiceSecurityDecl().getTransport();
      if (var3 == UserDataConstraint.Transport.INTEGRAL) {
         var2.setTransportGuarantee("INTEGRAL");
      } else if (var3 == UserDataConstraint.Transport.CONFIDENTIAL) {
         var2.setTransportGuarantee("CONFIDENTIAL");
      }

      var2.setPortComponentName(var1.getPortName());
      WebserviceAddressBean var4 = var2.createServiceEndpointAddress();
      var4.setWebserviceContextpath(var1.getContextPath());
      var4.setWebserviceServiceuri(var1.getServiceUri());
   }

   private static void copyPolicies(WebServiceDecl var0, File var1, Iterator<PolicyDecl> var2) throws IOException, WsBuildException {
      while(var2.hasNext()) {
         PolicyDecl var3 = (PolicyDecl)var2.next();
         if (!var3.isBuiltInPolicy() && !var3.isAttachToWsdl() && var3.isRelativeUri()) {
            URL var4 = var3.getPolicyURI().toURL();
            InputStream var5 = null;

            try {
               var5 = var4.openStream();
               FileUtils.writeToFile(var5, new File(var1, var3.getUri()));
            } finally {
               if (var5 != null) {
                  var5.close();
               }

            }
         }
      }

   }

   private static boolean isWebMethod(JMethod var0) {
      return var0.getAnnotation(WebMethod.class) != null;
   }

   private static boolean isExcluded(JAnnotatedElement var0) {
      JAnnotation var1 = var0.getAnnotation(WebMethod.class);
      return var1 != null ? JamUtil.getAnnotationBooleanValue(var1, "exclude", false) : false;
   }

   private static List<JMethod> getWebMethods(JClass var0) {
      ArrayList var1;
      for(var1 = new ArrayList(); var0 != null && !isExcluded(var0); var0 = var0.getSuperclass()) {
         JMethod[] var2 = var0.getDeclaredMethods();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            JMethod var5 = var2[var4];
            if (isWebMethod(var5) && !isExcluded(var5)) {
               var1.add(var5);
            }
         }
      }

      return var1;
   }

   static {
      System.setProperty(XMLStreamWriterFactory.class.getName() + ".woodstox", "true");
      System.setProperty(XMLStreamReaderFactory.class.getName() + ".woodstox", "true");
   }
}
