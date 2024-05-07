package weblogic.wsee.policy.deployment;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;

public class WsdlPolicySubject {
   private WsdlDefinitions definitions;
   PolicyWsdlExtension definitionsExt;
   private Map cachedEffectivePolicies;
   private boolean effectivePolicyMapBuilt;
   private PolicyServer ps;

   public WsdlPolicySubject(WsdlDefinitions var1) {
      this(var1, (PolicyServer)null);
   }

   public WsdlPolicySubject(WsdlDefinitions var1, PolicyServer var2) {
      this.cachedEffectivePolicies = new HashMap();
      this.effectivePolicyMapBuilt = false;
      this.definitions = var1;
      this.definitionsExt = (PolicyWsdlExtension)var1.getExtension("Policy");
      if (this.definitionsExt == null) {
         this.definitionsExt = new PolicyWsdlExtension();
         this.definitions.putExtension(this.definitionsExt);
      }

      findPolicyWsdlExtension(var1, this.definitionsExt);
      this.ps = var2;
   }

   public Map getAllEffectivePolicies() throws PolicyException {
      if (!this.effectivePolicyMapBuilt) {
         this.calculateAllEffectivePolicies();
         this.effectivePolicyMapBuilt = true;
      }

      return this.cachedEffectivePolicies;
   }

   public void addPolicy(PolicyStatement var1) {
      this.definitionsExt.addPolicy(var1);
   }

   public Map getPolicies() {
      return this.definitionsExt.getPolicies();
   }

   public boolean policyExists(PolicyStatement var1) {
      String var2 = var1.getId();
      if (var2 != null) {
         return this.getPolicies().get(var2) != null;
      } else {
         return false;
      }
   }

   public boolean policyExists(URI var1) {
      if (!var1.isAbsolute() && var1.getFragment() != null) {
         return this.getPolicies().get(var1.getFragment()) != null;
      } else {
         return true;
      }
   }

   public NormalizedExpression getServicePolicySubject(QName var1) throws PolicyException {
      NormalizedExpression var2 = null;
      WsdlService var3 = (WsdlService)this.definitions.getServices().get(var1);
      if (var3 != null) {
         String var4 = var1.getLocalPart();
         var2 = (NormalizedExpression)this.cachedEffectivePolicies.get(var4);
         if (var2 == null) {
            var2 = getEffectivePolicyFromWsdlExtensible(this.ps, var3, (PolicyURIs)null, this.getPolicies());
            this.cachedEffectivePolicies.put(var4, var2);
         }
      }

      return var2;
   }

   public NormalizedExpression getEndpointPolicySubject(QName var1, QName var2) throws PolicyException {
      NormalizedExpression var3 = null;
      WsdlService var4 = (WsdlService)this.definitions.getServices().get(var1);
      if (var4 != null) {
         WsdlPort var5 = (WsdlPort)var4.getPorts().get(var2);
         if (var5 != null) {
            String var6 = var1.getLocalPart() + var2.getLocalPart();
            var3 = (NormalizedExpression)this.cachedEffectivePolicies.get(var6);
            if (var3 == null) {
               var3 = getEndpointPolicySubject(this.ps, var5, this.getPolicies());
               this.cachedEffectivePolicies.put(var6, var3);
            }
         }
      }

      return var3;
   }

   public NormalizedExpression getOperationPolicySubject(QName var1, QName var2, QName var3) throws PolicyException {
      NormalizedExpression var4 = null;
      WsdlService var5 = (WsdlService)this.definitions.getServices().get(var1);
      if (var5 != null) {
         WsdlPort var6 = (WsdlPort)var5.getPorts().get(var2);
         if (var6 != null) {
            WsdlOperation var7 = (WsdlOperation)var6.getPortType().getOperations().get(var3);
            WsdlBindingOperation var8 = (WsdlBindingOperation)var6.getBinding().getOperations().get(var3);
            if (var7 != null && var8 != null) {
               String var9 = var1.getLocalPart() + var2.getLocalPart() + var3.getLocalPart();
               var4 = (NormalizedExpression)this.cachedEffectivePolicies.get(var9);
               if (var4 == null) {
                  var4 = getOperationPolicySubject(this.ps, var7, var8, this.getPolicies());
                  this.cachedEffectivePolicies.put(var9, var4);
               }
            }
         }
      }

      return var4;
   }

   public NormalizedExpression getInputMessagePolicySubject(QName var1, QName var2, QName var3) throws PolicyException {
      NormalizedExpression var4 = null;
      WsdlService var5 = (WsdlService)this.definitions.getServices().get(var1);
      if (var5 != null) {
         WsdlPort var6 = (WsdlPort)var5.getPorts().get(var2);
         if (var6 != null) {
            WsdlOperation var7 = (WsdlOperation)var6.getPortType().getOperations().get(var3);
            WsdlBindingOperation var8 = (WsdlBindingOperation)var6.getBinding().getOperations().get(var3);
            if (var7 != null && var8 != null) {
               WsdlMessage var9 = var7.getInput();
               if (var9 != null) {
                  String var10 = var1.getLocalPart() + var2.getLocalPart() + var3.getLocalPart() + "Input";
                  var4 = (NormalizedExpression)this.cachedEffectivePolicies.get(var10);
                  if (var4 == null) {
                     var4 = getMessagePolicySubject(this.ps, var9, var8.getInput(), var7.getInputPolicyUris(), this.getPolicies());
                     this.cachedEffectivePolicies.put(var10, var4);
                  }
               }
            }
         }
      }

      return var4;
   }

   public NormalizedExpression getOutputMessagePolicySubject(QName var1, QName var2, QName var3) throws PolicyException {
      NormalizedExpression var4 = null;
      WsdlService var5 = (WsdlService)this.definitions.getServices().get(var1);
      if (var5 != null) {
         WsdlPort var6 = (WsdlPort)var5.getPorts().get(var2);
         if (var6 != null) {
            WsdlOperation var7 = (WsdlOperation)var6.getPortType().getOperations().get(var3);
            WsdlBindingOperation var8 = (WsdlBindingOperation)var6.getBinding().getOperations().get(var3);
            if (var7 != null && var8 != null) {
               WsdlMessage var9 = var7.getOutput();
               if (var9 != null) {
                  String var10 = var1.getLocalPart() + var2.getLocalPart() + var3.getLocalPart() + "Output";
                  var4 = (NormalizedExpression)this.cachedEffectivePolicies.get(var10);
                  if (var4 == null) {
                     var4 = getMessagePolicySubject(this.ps, var9, var8.getOutput(), var7.getOutputPolicyUris(), this.getPolicies());
                     this.cachedEffectivePolicies.put(var10, var4);
                  }
               }
            }
         }
      }

      return var4;
   }

   public NormalizedExpression getFaultMessagePolicySubject(QName var1, QName var2, QName var3, String var4) throws PolicyException {
      NormalizedExpression var5 = null;
      WsdlService var6 = (WsdlService)this.definitions.getServices().get(var1);
      if (var6 != null) {
         WsdlPort var7 = (WsdlPort)var6.getPorts().get(var2);
         if (var7 != null) {
            WsdlOperation var8 = (WsdlOperation)var7.getPortType().getOperations().get(var3);
            WsdlBindingOperation var9 = (WsdlBindingOperation)var7.getBinding().getOperations().get(var3);
            if (var8 != null && var9 != null) {
               WsdlMessage var10 = (WsdlMessage)var8.getFaults().get(var4);
               if (var10 != null) {
                  String var11 = var1.getLocalPart() + var2.getLocalPart() + var3.getLocalPart() + var4;
                  var5 = (NormalizedExpression)this.cachedEffectivePolicies.get(var11);
                  if (var5 == null) {
                     var5 = getMessagePolicySubject(this.ps, var10, (WsdlBindingMessage)var9.getFaults().get(var4), var8.getFaultPolicyUris(var4), this.getPolicies());
                     this.cachedEffectivePolicies.put(var11, var5);
                  }
               }
            }
         }
      }

      return var5;
   }

   public static NormalizedExpression getEndpointPolicySubject(WsdlPort var0, Map var1) throws PolicyException {
      return getEndpointPolicySubject((PolicyServer)null, (WsdlPort)var0, var1);
   }

   public static NormalizedExpression getEndpointPolicySubject(PolicyServer var0, WsdlPort var1, Map var2) throws PolicyException {
      NormalizedExpression var3 = getEffectivePolicyFromWsdlExtensible(var0, var1, var1.getPolicyUris(), var2);
      WsdlBinding var4 = var1.getBinding();
      NormalizedExpression var5 = getEffectivePolicyFromWsdlExtensible(var0, var4, var4.getPolicyUris(), var2);
      WsdlPortType var6 = var1.getPortType();
      NormalizedExpression var7 = PolicyWsdlExtension.getEffectivePolicy(var0, var2, var6.getPolicyUris());
      NormalizedExpression var8 = PolicyMath.merge(var3, var5);
      var8 = PolicyMath.merge(var8, var7);
      return var8;
   }

   public static NormalizedExpression getEndpointPolicySubject(PolicyServer var0, WsdlBinding var1, Map var2) throws PolicyException {
      NormalizedExpression var3 = getEffectivePolicyFromWsdlExtensible(var0, var1, var1.getPolicyUris(), var2);
      WsdlPortType var4 = var1.getPortType();
      NormalizedExpression var5 = PolicyWsdlExtension.getEffectivePolicy(var0, var2, var4.getPolicyUris());
      NormalizedExpression var6 = PolicyMath.merge(var3, var5);
      return var6;
   }

   public static NormalizedExpression getOperationPolicySubject(WsdlOperation var0, WsdlBindingOperation var1, Map var2) throws PolicyException {
      return getOperationPolicySubject((PolicyServer)null, var0, var1, var2);
   }

   public static NormalizedExpression getOperationPolicySubject(PolicyServer var0, WsdlOperation var1, WsdlBindingOperation var2, Map var3) throws PolicyException {
      NormalizedExpression var4 = getEffectivePolicyFromWsdlExtensible(var0, var2, var2.getPolicyUris(), var3);
      NormalizedExpression var5 = getEffectivePolicyFromWsdlExtensible(var0, var1, var1.getPolicyUris(), var3);
      return PolicyMath.merge(var4, var5);
   }

   public static NormalizedExpression getOperationPolicySubject(PolicyServer var0, WsdlBindingOperation var1, Map var2) throws PolicyException {
      NormalizedExpression var3 = getEffectivePolicyFromWsdlExtensible(var0, var1, var1.getPolicyUris(), var2);
      return var3;
   }

   public static NormalizedExpression getMessagePolicySubject(WsdlMessage var0, WsdlBindingMessage var1, PolicyURIs var2, Map var3) throws PolicyException {
      return getMessagePolicySubject((PolicyServer)null, var0, var1, var2, var3);
   }

   public static NormalizedExpression getMessagePolicySubject(PolicyServer var0, WsdlMessage var1, WsdlBindingMessage var2, PolicyURIs var3, Map var4) throws PolicyException {
      NormalizedExpression var5 = NormalizedExpression.createUnitializedExpression();
      if (var2 != null) {
         var5 = PolicyMath.merge(var5, getEffectivePolicyFromWsdlExtensible(var0, var2, var2.getPolicyUris(), var4));
      }

      if (var1 != null) {
         var5 = PolicyMath.merge(var5, getEffectivePolicyFromWsdlExtensible(var0, var1, var1.getPolicyUris(), var4));
      }

      if (var3 != null) {
         var5 = PolicyMath.merge(var5, PolicyWsdlExtension.getEffectivePolicy(var0, var4, var3));
      }

      return var5;
   }

   private static NormalizedExpression getEffectivePolicyFromWsdlExtensible(PolicyServer var0, WsdlExtensible var1, PolicyURIs var2, Map var3) throws PolicyException {
      PolicyWsdlExtension var4 = (PolicyWsdlExtension)var1.getExtension("Policy");
      NormalizedExpression var5 = PolicyWsdlExtension.getEffectivePolicy(var4 == null ? null : var4.getPolicies());
      PolicyReferenceWsdlExtension var6 = (PolicyReferenceWsdlExtension)var1.getExtension("PolicyReference");
      if (var6 != null) {
         var5 = PolicyMath.merge(var5, var6.getEffectivePolicy(var0, var3));
      }

      if (var2 != null) {
         var5 = PolicyMath.merge(var5, PolicyWsdlExtension.getEffectivePolicy(var0, var3, var2));
      }

      return var5;
   }

   private void calculateAllEffectivePolicies() throws PolicyException {
      Iterator var1 = this.definitions.getServices().values().iterator();

      while(var1.hasNext()) {
         WsdlService var2 = (WsdlService)var1.next();
         QName var3 = var2.getName();
         this.getServicePolicySubject(var3);
         Iterator var4 = var2.getPorts().values().iterator();

         while(var4.hasNext()) {
            WsdlPort var5 = (WsdlPort)var4.next();
            QName var6 = var5.getName();
            this.getEndpointPolicySubject(var3, var6);
            Iterator var7 = var5.getPortType().getOperations().values().iterator();

            while(var7.hasNext()) {
               WsdlOperation var8 = (WsdlOperation)var7.next();
               QName var9 = var8.getName();
               this.getOperationPolicySubject(var3, var6, var9);
               this.getInputMessagePolicySubject(var3, var6, var9);
               this.getOutputMessagePolicySubject(var3, var6, var9);
               Iterator var10 = var8.getFaults().values().iterator();

               while(var10.hasNext()) {
                  String var11 = ((WsdlMessage)var10.next()).getName().getLocalPart();
                  this.getFaultMessagePolicySubject(var3, var6, var9, var11);
               }
            }
         }
      }

   }

   private static void findPolicyWsdlExtension(WsdlDefinitions var0, PolicyWsdlExtension var1) {
      List var2 = var0.getImportedWsdlDefinitions();

      WsdlDefinitions var4;
      for(Iterator var3 = var2.iterator(); var3.hasNext(); findPolicyWsdlExtension(var4, var1)) {
         var4 = (WsdlDefinitions)var3.next();
         PolicyWsdlExtension var5 = (PolicyWsdlExtension)var4.getExtension("Policy");
         if (var5 != null) {
            Map var6 = var5.getPolicies();
            if (var1.hasPolicies()) {
               Iterator var7 = var6.values().iterator();

               while(var7.hasNext()) {
                  PolicyStatement var8 = (PolicyStatement)var7.next();
                  if (!var1.policyExists(var8)) {
                     var1.addPolicy(var8);
                  }
               }
            } else {
               var1.addAllPolicies(var6);
            }
         }
      }

   }
}
