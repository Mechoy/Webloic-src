package weblogic.wsee.tools.jws.wsdl;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.jws.Policy.Direction;
import weblogic.wsee.policy.deployment.PolicyDeployUtils;
import weblogic.wsee.policy.deployment.UsingPolicy;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyExpression;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.BuiltinPolicyFinder;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability.policy.RMAssertion;
import weblogic.wsee.reliability.policy.ReliabilityPolicyAssertionsFactory;
import weblogic.wsee.reliability.policy11.RM11Assertion;
import weblogic.wsee.security.policy.SecurityPolicyCustomizer;
import weblogic.wsee.security.policy12.assertions.TransportBinding;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.PolicyDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public class PolicyAnnotator extends JAXRPCProcessor {
   private WsdlPolicySubject policySubject;
   private JAXRPCWebServiceInfo webServiceInfo = null;

   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      this.webServiceInfo = var1;
      this.policySubject = new WsdlPolicySubject(var1.getDefinitions());
      Iterator var2 = this.listPorts(var1.getDefinitions());
      if (var2 == null) {
         this.moduleInfo.getJwsBuildContext().getLogger().log(EventLevel.DEBUG, "FIXME: Unable to find port. Policy annotator need to handle this case.");
      } else {
         assert var2 != null;

         while(var2.hasNext()) {
            this.processPort((WsdlPort)var2.next());
         }

      }
   }

   private void processPort(WsdlPort var1) throws WsBuildException {
      boolean var2 = false;
      var2 |= this.processClassScopedAnnotations(this.webServiceInfo.getWebService(), var1);

      for(Iterator var3 = this.webServiceInfo.getWebService().getWebMethods(); var3.hasNext(); var2 |= this.processMethodScopedAnnotations((WebMethodDecl)var3.next(), var1)) {
      }

      if (var2) {
         this.webServiceInfo.getDefinitions().putExtension(new UsingPolicy(true));
      }

   }

   private boolean processClassScopedAnnotations(WebServiceSEIDecl var1, WsdlPort var2) throws WsBuildException {
      boolean var3 = false;
      WsdlBinding var4 = var2.getBinding();
      if (var4 != null) {
         Iterator var5 = var1.getPoilices();

         while(var5.hasNext()) {
            var3 = true;
            PolicyDecl var6 = (PolicyDecl)var5.next();
            this.processPolicyAnnotation(var6, var4.getOperations().values().iterator(), true, var2);
         }
      }

      return var3;
   }

   private boolean processMethodScopedAnnotations(WebMethodDecl var1, WsdlPort var2) throws WsBuildException {
      boolean var3 = false;
      this.moduleInfo.getJwsBuildContext().getLogger().log(EventLevel.DEBUG, "processMethodScopedAnnotations(...," + var1.getMethodName() + ",...)");
      Iterator var4 = var1.getPoilices();
      if (var4.hasNext()) {
         WsdlBindingOperation var5 = lookupWsdlBindingOperation(this.webServiceInfo.getDefinitions(), var1, var2);
         ArrayList var6 = new ArrayList();
         var6.add(var5);

         while(var4.hasNext()) {
            var3 = true;
            this.processPolicyAnnotation((PolicyDecl)var4.next(), var6.iterator(), false, var2);
         }
      }

      return var3;
   }

   private void processPolicyAnnotation(PolicyDecl var1, Iterator var2, boolean var3, WsdlPort var4) throws WsBuildException {
      URI var5 = var1.getPolicyURI();

      assert var5 != null;

      PolicyStatement var6 = null;

      try {
         if (var1.isAttachToWsdl()) {
            var6 = this.getPolicyStatement(var1);
            if (SecurityPolicyCustomizer.isSecurityPolicyAbstract(var1.getUri(), var6)) {
               throw new WsBuildException("Abstract policy can not be attached to wsdl during build time.");
            }

            if (this.policySubject.policyExists(var6)) {
               var5 = new URI("#" + var6.getId());
            } else {
               String var7 = PolicyDeployUtils.getXPointerId(var1.getUri());
               var6.setId(var7);
               var5 = new URI("#" + var7);
               this.policySubject.addPolicy(var6);
            }
         } else if (var1.isRelativeUri()) {
            var5 = new URI("policy:" + var1.getUriWithoutExt());
         }
      } catch (Exception var8) {
         throw new WsBuildException(var8);
      }

      while(var2.hasNext()) {
         WsdlBindingOperation var9 = (WsdlBindingOperation)var2.next();
         if (var1.getDirection() == Direction.inbound) {
            PolicyDeployUtils.addPolicyReferenceToWsdlExtensible(var9.getInput(), var5);
            if (this.checkRMPolicy(var6, var1)) {
               this.addOptionalRMPolicy(var6, var1, var4);
            }
         } else if (var1.getDirection() == Direction.outbound) {
            PolicyDeployUtils.addPolicyReferenceToWsdlExtensible(var9.getOutput(), var5);
            if (this.checkRMPolicy(var6, var1)) {
               this.addOptionalRMPolicy(var6, var1, var4);
            }
         } else if (var1.getDirection() == Direction.both && this.checkRMPolicy(var6, var1) && !var3) {
            PolicyDeployUtils.addPolicyReferenceToWsdlExtensible(var9.getInput(), var5);
            PolicyDeployUtils.addPolicyReferenceToWsdlExtensible(var9.getOutput(), var5);
            this.addOptionalRMPolicy(var6, var1, var4);
         } else {
            if (var3) {
               if (!this.checkRMPolicy(var6, var1) && !PolicyDeployUtils.isCannedMtomPolicy(var1)) {
                  PolicyDeployUtils.addPolicyURIToPortType(var4.getPortType(), var5);
               } else {
                  PolicyDeployUtils.addPolicyReferenceToWsdlExtensible(var4.getBinding(), var5);
               }
               break;
            }

            if (!var3 && this.checkTransportBindingPolicy(var6, var1)) {
               throw new WsBuildException("TransportBinding assertion MUST apply to Endpoint Policy Subject");
            }

            PolicyDeployUtils.addPolicyReferenceToWsdlExtensible(var9, var5);
         }
      }

   }

   private PolicyStatement getPolicyStatement(PolicyDecl var1) throws WsBuildException {
      try {
         PolicyStatement var2;
         if (var1.isBuiltInPolicy()) {
            String var3 = var1.getBuiltInUriWithoutPrefix();
            var2 = BuiltinPolicyFinder.getInstance().findPolicy(var3, (String)null);
         } else {
            InputStream var5 = var1.getPolicyURI().toURL().openConnection().getInputStream();
            var2 = PolicyFinder.readPolicyFromStream(var1.getUri(), var5, true);
         }

         return var2;
      } catch (Exception var4) {
         throw new WsBuildException(var4);
      }
   }

   private boolean checkRMPolicy(PolicyStatement var1, PolicyDecl var2) throws WsBuildException {
      if (var2.isBuiltInPolicy()) {
         String var3 = var2.getPolicyURI().toString();
         return var3.indexOf("policy:DefaultReliability") >= 0 || var3.indexOf("policy:LongRunningReliability") >= 0;
      } else {
         if (var1 == null) {
            var1 = this.getPolicyStatement(var2);
         }

         try {
            return ReliabilityPolicyAssertionsFactory.hasRMPolicy(var1.normalize());
         } catch (Exception var4) {
            throw new WsBuildException(var4);
         }
      }
   }

   private boolean checkTransportBindingPolicy(PolicyStatement var1, PolicyDecl var2) throws WsBuildException {
      if (var2.isBuiltInPolicy()) {
         String var5 = var2.getPolicyURI().toString();
         return var5.indexOf("Https") >= 0 && var5.indexOf("Saml") < 0;
      } else {
         if (var1 == null) {
            var1 = this.getPolicyStatement(var2);
         }

         try {
            NormalizedExpression var3 = var1.normalize();
            return var3.containsPolicyAssertion(TransportBinding.class);
         } catch (Exception var4) {
            throw new WsBuildException(var4);
         }
      }
   }

   private static WsdlBindingOperation lookupWsdlBindingOperation(WsdlDefinitions var0, WebMethodDecl var1, WsdlPort var2) throws WsBuildException {
      QName var3 = new QName(var0.getTargetNamespace(), var1.getName());
      WsdlBindingOperation var4 = (WsdlBindingOperation)var2.getBinding().getOperations().get(var3);
      if (var4 == null) {
         throw new WsBuildException("Could not locate WSDL operation corresponding to method '" + var1.getMethodName());
      } else {
         return var4;
      }
   }

   private Iterator listPorts(WsdlDefinitions var1) {
      Iterator var2 = var1.getServices().values().iterator();
      if (!var2.hasNext()) {
         return null;
      } else {
         assert var2.hasNext();

         WsdlService var3 = (WsdlService)var2.next();
         return var3.getPorts().values().iterator();
      }
   }

   private void addOptionalRMPolicy(PolicyStatement var1, PolicyDecl var2, WsdlPort var3) throws WsBuildException {
      if (var1 == null) {
         var1 = this.getPolicyStatement(var2);
      }

      try {
         if (SecurityPolicyCustomizer.isSecurityPolicyAbstract(var2.getUri(), var1)) {
            throw new WsBuildException("Abstract policy can not be attached to wsdl during build time.");
         } else {
            NormalizedExpression var4 = WsdlPolicySubject.getEndpointPolicySubject(new PolicyServer(this.policySubject.getPolicies()), var3, this.policySubject.getPolicies());
            Set var5 = var4.getPolicyAlternatives(RM11Assertion.class);
            Iterator var6;
            Object var7;
            Iterator var8;
            Object var9;
            if (var5 != null) {
               var6 = var5.iterator();

               while(var6.hasNext()) {
                  var7 = var6.next();
                  var8 = ((PolicyAlternative)var7).getAssertions(RM11Assertion.class).iterator();

                  while(var8.hasNext()) {
                     var9 = var8.next();
                     if (!((PolicyAssertion)var9).isOptional()) {
                        throw new WsBuildException("Web Services Reliable Messaging policy must be optional at class level when policy is attached at method level.");
                     }
                  }
               }
            }

            var5 = var4.getPolicyAlternatives(RMAssertion.class);
            if (var5 != null) {
               var6 = var5.iterator();

               while(var6.hasNext()) {
                  var7 = var6.next();
                  var8 = ((PolicyAlternative)var7).getAssertions(RMAssertion.class).iterator();

                  while(var8.hasNext()) {
                     var9 = var8.next();
                     if (!((PolicyAssertion)var9).isOptional()) {
                        throw new WsBuildException("Web Services Reliable Messaging policy must be optional at class level when policy is attached at method level.");
                     }
                  }
               }
            }

            String var12 = PolicyDeployUtils.getXPointerId("Optional" + var1.getId());
            PolicyStatement var13 = PolicyStatement.createPolicyStatement(var12);
            if (!this.policySubject.policyExists(var13)) {
               var8 = var1.normalize().getPolicyAlternatives(RM11Assertion.class).iterator();

               Iterator var14;
               while(var8.hasNext()) {
                  var14 = ((PolicyAlternative)var8.next()).getAssertions(RM11Assertion.class).iterator();

                  while(var14.hasNext()) {
                     RM11Assertion var10 = ReliabilityPolicyAssertionsFactory.copyRM11Assertion((RM11Assertion)var14.next());
                     var10.setOptional(true);
                     var13.addExpression(PolicyExpression.createTerminal(var10));
                  }
               }

               var8 = var1.normalize().getPolicyAlternatives(RMAssertion.class).iterator();

               while(var8.hasNext()) {
                  var14 = ((PolicyAlternative)var8.next()).getAssertions(RMAssertion.class).iterator();

                  while(var14.hasNext()) {
                     RMAssertion var15 = ReliabilityPolicyAssertionsFactory.copyRMAssertion((RMAssertion)var14.next());
                     var15.setOptional(true);
                     var13.addExpression(PolicyExpression.createTerminal(var15));
                  }
               }

               this.policySubject.addPolicy(var13);
            }

            PolicyDeployUtils.addPolicyReferenceToWsdlExtensible(var3.getBinding(), new URI("#" + var12));
         }
      } catch (Exception var11) {
         throw new WsBuildException(var11);
      }
   }
}
