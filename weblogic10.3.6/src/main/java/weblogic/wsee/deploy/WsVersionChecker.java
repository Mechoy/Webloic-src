package weblogic.wsee.deploy;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryProvider;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.jws.Callback;
import weblogic.jws.Conversation;
import weblogic.jws.Conversational;
import weblogic.jws.ServiceClient;
import weblogic.jws.WLJmsTransport;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability.policy.ReliabilityPolicyAssertionsFactory;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;

public class WsVersionChecker {
   private static boolean verbose = Verbose.isVerbose(WsVersionChecker.class);
   private static boolean DEBUG = false;
   private static boolean EXTRADEBUG = false;
   private static final String CONTROL_ANNOTATION = "org.apache.beehive.controls.api.bean.Control";
   private static final String ASYNCEJBLIB_REFERENCE_NAME = "bea_wls_async_response";

   private static void p(String var0) {
      System.out.println(var0);
      System.out.flush();
   }

   private static boolean referencesAsyncEJB(ApplicationContextInternal var0) {
      LibraryProvider var1 = var0.getLibraryProvider((String)null);
      String var2 = var0.getAppDeploymentMBean().getName();
      if (var1 != null) {
         Library[] var3 = var1.getReferencedLibraries();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getName().equals("bea_wls_async_response")) {
               if (verbose) {
                  Verbose.log((Object)("Application " + var2 + " references the async response EJB"));
               }

               return true;
            }
         }
      }

      if (verbose) {
         Verbose.log((Object)("Application " + var2 + " does not reference the async response EJB"));
      }

      return false;
   }

   private static void printAllAnnotations(Class var0, PrintWriter var1) {
      Annotation[] var2 = var0.getAnnotations();
      var1.println("Annotations for class " + var0.getName());
      if (var2.length != 0) {
         var1.println("Class Annotations: ");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.println("\nclass annotation[" + var3 + "]");
            var1.println("\t" + var2[var3]);
         }
      }

      var1.println("\n\n");
      var1.println("Method Annotations:");
      Method[] var8 = var0.getMethods();
      if (var8.length == 0) {
         var1.println("\tNo methods");
      } else {
         for(int var4 = 0; var4 < var8.length; ++var4) {
            Annotation[] var5 = var8[var4].getAnnotations();
            if (var5.length != 0) {
               var1.println("\nMethod " + var8[var4].getName());

               for(int var6 = 0; var6 < var5.length; ++var6) {
                  var1.println("\n\tMethod annotation[" + var6 + "]");
                  var1.println("\t" + var5[var6]);
               }
            }
         }
      }

      var1.println("\n\n");
      var1.println("Field Annotations:");
      Field[] var9 = var0.getFields();
      if (var9.length == 0) {
         var1.println("\tNo fields");
      } else {
         for(int var10 = 0; var10 < var9.length; ++var10) {
            Annotation[] var11 = var9[var10].getAnnotations();
            if (var11.length != 0) {
               var1.println("\nField " + var9[var10].getName());

               for(int var7 = 0; var7 < var11.length; ++var7) {
                  var1.println("\n\tField annotation[" + var7 + "]");
                  var1.println("\t" + var2[var7]);
               }
            }
         }
      }

   }

   public static boolean check901SBSCompatibility(ApplicationContextInternal var0, Map var1, WebservicesBean var2, boolean var3) throws PolicyException {
      if (verbose) {
         Verbose.log((Object)"Checking 901 restricted SBS compliance");
      }

      assert !var1.isEmpty() : "deploy info map is empty";

      Iterator var4 = var1.values().iterator();

      while(var4.hasNext()) {
         DeployInfo var5 = (DeployInfo)var4.next();
         Class var6 = var5.getJwsClass();
         String var7 = var6.getName();
         if (DEBUG) {
            p("Analyzing .. " + var7);
         }

         Annotation[] var8 = var6.getAnnotations();

         assert var8.length > 0 : "No annotations in " + var7;

         if (EXTRADEBUG) {
            printAllAnnotations(var6, new PrintWriter(System.out, true));
         }

         if (isConversational(var6)) {
            if (DEBUG || verbose) {
               Verbose.log((Object)(var7 + " has conversation annotations"));
            }

            return false;
         }

         if (hasServiceClient(var6) && referencesAsyncEJB(var0)) {
            if (DEBUG || verbose) {
               Verbose.log((Object)(var7 + " has service client annotations and also references async library "));
            }

            return false;
         }

         if (hasControl(var6)) {
            if (DEBUG || verbose) {
               Verbose.log((Object)(var7 + " has control annotations"));
            }

            return false;
         }

         if (hasJMSTransport(var6)) {
            if (DEBUG || verbose) {
               Verbose.log((Object)(var7 + " uses JMS transport"));
            }

            return false;
         }
      }

      if (DEBUG) {
         p("Now analyzing Webservice descriptions for RM policies");
      }

      WebserviceDescriptionBean[] var18 = var2.getWebserviceDescriptions();

      for(int var19 = 0; var19 < var18.length; ++var19) {
         if (DEBUG) {
            p("Analyzing description " + var18[var19].getWebserviceDescriptionName());
         }

         PortComponentBean[] var20 = var18[var19].getPortComponents();
         if (DEBUG) {
            p("Port components = " + var20.length);
         }

         label147:
         for(int var21 = 0; var21 < var20.length; ++var21) {
            String var22;
            if (var3) {
               var22 = var20[var21].getServiceImplBean().getServletLink();
               if (DEBUG) {
                  p("servlet name = " + var22);
               }
            } else {
               var22 = var20[var21].getServiceImplBean().getEjbLink();
               if (DEBUG) {
                  p("ejb name = " + var22);
               }
            }

            DeployInfo var9 = (DeployInfo)var1.get(var22);
            if (var9 != null) {
               WsdlDefinitions var10 = var9.getWsdlDef();
               WsdlPort var11 = (WsdlPort)var10.getPorts().get(var9.getPortComp().getWsdlPort());
               Iterator var12 = var11.getPortType().getOperations().values().iterator();

               while(true) {
                  WsdlOperation var13;
                  do {
                     if (!var12.hasNext()) {
                        continue label147;
                     }

                     var13 = (WsdlOperation)var12.next();
                     WsdlBindingOperation var14 = (WsdlBindingOperation)var11.getBinding().getOperations().get(var13.getName());
                     WssPolicyContext var15 = new WssPolicyContext(var9.getWssConfigMBeanName());
                     PolicyServer var16 = var15.getPolicyServer();
                     NormalizedExpression var17 = PolicyContext.getRequestEffectivePolicy(var11, var13, var14, var16, var16.getCachedPolicies());
                     if (ReliabilityPolicyAssertionsFactory.hasRMPolicy(var17)) {
                        if (DEBUG || verbose) {
                           Verbose.log((Object)(var11.getService() + ":" + var13.getName() + " has RM policy assertions"));
                        }

                        return false;
                     }
                  } while(!DEBUG && !verbose);

                  Verbose.log((Object)(var11.getService() + ":" + var13.getName() + " does not have RM policy assertions"));
               }
            }
         }
      }

      return true;
   }

   public static boolean isConversational(Class var0) {
      boolean var1 = false;
      if (var0 == null) {
         return false;
      } else {
         if (var0.getAnnotation(Conversational.class) != null) {
            if (DEBUG) {
               p("Class " + var0.getName() + " has conversational annotations");
            }

            var1 = true;
         } else {
            Method[] var2 = var0.getMethods();
            var1 = hasAnnotation(var2, (Class)Conversation.class);
         }

         return var1;
      }
   }

   private static boolean hasAnnotation(AnnotatedElement[] var0, Class var1) {
      if (var0 == null) {
         return false;
      } else {
         AnnotatedElement[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            AnnotatedElement var5 = var2[var4];
            if (var5.getAnnotation(var1) != null) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean hasAnnotation(AnnotatedElement[] var0, String var1) {
      try {
         Class var2 = Thread.currentThread().getContextClassLoader().loadClass(var1);
         return hasAnnotation(var0, var2);
      } catch (ClassNotFoundException var3) {
         return false;
      }
   }

   private static boolean hasJMSTransport(Class var0) {
      return hasAnnotation(new AnnotatedElement[]{var0}, WLJmsTransport.class);
   }

   private static boolean hasServiceClient(Class var0) {
      return hasAnnotation(var0.getFields(), (Class)ServiceClient.class);
   }

   private static boolean hasCallback(Class var0) {
      return hasAnnotation(var0.getFields(), (Class)Callback.class);
   }

   private static boolean hasControl(Class var0) {
      return hasAnnotation(var0.getFields(), (String)"org.apache.beehive.controls.api.bean.Control");
   }
}
