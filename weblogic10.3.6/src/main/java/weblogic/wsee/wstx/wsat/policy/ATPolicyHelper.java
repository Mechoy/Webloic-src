package weblogic.wsee.wstx.wsat.policy;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.Transactional.TransactionFlowType;
import weblogic.wsee.wstx.wsat.Transactional.Version;

public class ATPolicyHelper {
   public static TransactionalFeature buildFeatureFromWsdl(WsdlPort var0, PolicyServer var1) {
      TransactionalFeature var2 = null;

      try {
         Iterator var3 = var0.getBinding().getOperations().values().iterator();

         while(true) {
            String var5;
            Set var7;
            do {
               if (!var3.hasNext()) {
                  return var2;
               }

               WsdlBindingOperation var4 = (WsdlBindingOperation)var3.next();
               var5 = var4.getName().getLocalPart();
               NormalizedExpression var6 = WsdlPolicySubject.getOperationPolicySubject(var1, var4, Collections.EMPTY_MAP);
               var7 = var6.getPolicyAlternatives(ATAssertion.class);
            } while(var7 == null);

            boolean var8 = false;
            Iterator var9 = var7.iterator();

            label59:
            while(var9.hasNext()) {
               PolicyAlternative var10 = (PolicyAlternative)var9.next();
               Set var11 = var10.getAssertions(ATAssertion.class);
               Iterator var12 = var11.iterator();

               while(true) {
                  PolicyAssertion var13;
                  do {
                     if (!var12.hasNext()) {
                        continue label59;
                     }

                     var13 = (PolicyAssertion)var12.next();
                  } while(!(var13 instanceof ATAssertion));

                  if (var2 == null) {
                     var2 = new TransactionalFeature();
                     var2.setExplicitMode(true);
                  }

                  QName var14 = var13.getName();
                  Transactional.Version var15 = Version.forNamespaceUri(var14.getNamespaceURI());
                  if (var8 && var15 != var2.getVersion()) {
                     var2.setVersion(Version.DEFAULT);
                  } else {
                     var2.setVersion(var15);
                  }

                  var2.setFlowType(var5, toFlowType(var13.isOptional()));
                  var2.setEnabled(var5, true);
                  var2.setEnabled(true);
                  var8 = true;
               }
            }

            if (!var8 && var2 != null) {
               var2.setEnabled(var5, false);
            }
         }
      } catch (Exception var16) {
         throw new WebServiceException(var16);
      }
   }

   private static Transactional.TransactionFlowType toFlowType(boolean var0) {
      return var0 ? TransactionFlowType.SUPPORTS : TransactionFlowType.MANDATORY;
   }
}
