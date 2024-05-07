package weblogic.wsee.policy.checker;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.mc.internal.MCSupported;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlPort;

public class PolicyLevelChecker {
   private WsService wsService;
   private QName portName;
   private NormalizedExpression methodsPolicies;
   private NormalizedExpression classPolicies;
   private static final Class[] JAXWS_ASSERTIONS = new Class[]{MCSupported.class};
   private static final Logger LOGGER = Logger.getLogger(PolicyLevelChecker.class.getName());

   public PolicyLevelChecker(WsService var1, QName var2) {
      this.wsService = var1;
      this.portName = var2;
      this.methodsPolicies = this.getMethodsLevelPolicies();
      this.classPolicies = this.getClassLevelPolicies();
   }

   public void doJAXWSDefaultCheck() throws WebServiceException {
      Class[] var1 = JAXWS_ASSERTIONS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Class var4 = var1[var3];
         if (!this.isValidate(var4)) {
            throw new WebServiceException(this.errorMsg(var4.getSimpleName()));
         }
      }

   }

   public boolean isValidate(PolicyAssertion var1) {
      Set var2 = this.methodsPolicies.getPolicyAlternatives(var1);
      if (var2 != null && var2.size() > 0) {
         Set var3 = this.classPolicies.getPolicyAlternatives(var1);
         if (var3 != null && var3.size() > 0) {
            return false;
         }
      }

      return true;
   }

   public boolean isValidate(Class var1) {
      return !this.methodsPolicies.containsPolicyAssertion(var1) || this.classPolicies.containsPolicyAssertion(var1);
   }

   private String errorMsg(String var1) {
      return "Policy contained assertion [" + var1 + "] is deployed at method level, it can only deployed at class level!";
   }

   private NormalizedExpression getMethodsLevelPolicies() {
      NormalizedExpression var1 = new NormalizedExpression();
      WsdlPort var2 = this.wsService.getPort(this.portName.getLocalPart()).getWsdlPort();
      Iterator var3 = var2.getBinding().getOperations().values().iterator();

      while(var3.hasNext()) {
         WsdlBindingOperation var4 = (WsdlBindingOperation)var3.next();

         try {
            NormalizedExpression var5 = WsdlPolicySubject.getOperationPolicySubject(this.wsService.getPolicyServer(), var4, Collections.EMPTY_MAP);
            if (var5 != null) {
               var1 = PolicyMath.merge(var1, var5);
            }
         } catch (PolicyException var6) {
            if (LOGGER.getLevel() == Level.WARNING) {
               LOGGER.warning(var6.getMessage());
            }
         }
      }

      return var1;
   }

   private NormalizedExpression getClassLevelPolicies() {
      NormalizedExpression var1 = new NormalizedExpression();
      WsPort var2 = this.wsService.getPort(this.portName.getLocalPart());
      PolicyServer var3 = this.wsService.getPolicyServer();
      if (var3 == null) {
         return var1;
      } else {
         try {
            var1 = var3.getEndpointPolicy(var2.getWsdlPort());
         } catch (PolicyException var5) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Exception getting policy for " + var2.getEndpoint());
            }
         }

         return var1;
      }
   }
}
