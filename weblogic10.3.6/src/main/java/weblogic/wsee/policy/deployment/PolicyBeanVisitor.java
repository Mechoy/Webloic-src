package weblogic.wsee.policy.deployment;

import java.lang.reflect.Method;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.OperationPolicyBean;
import weblogic.j2ee.descriptor.wl.PortPolicyBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.j2ee.descriptor.wl.WsPolicyBean;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.wsee.jws.JWSVisitor;

public class PolicyBeanVisitor implements JWSVisitor {
   WebservicePolicyRefBean bean;

   public PolicyBeanVisitor() {
      EditableDescriptorManager var1 = new EditableDescriptorManager();
      this.bean = (WebservicePolicyRefBean)var1.createDescriptorRoot(WebservicePolicyRefBean.class).getRootBean();
   }

   public WebservicePolicyRefBean getPolicyRefBean() {
      return this.bean;
   }

   public void visitClass(JWSVisitor.JWSClass var1) {
      Class var2 = var1.getServiceImpl();
      Policy var3 = (Policy)var2.getAnnotation(Policy.class);
      Policies var4 = (Policies)var2.getAnnotation(Policies.class);
      if (var3 != null || var4 != null) {
         PortPolicyBean var5 = this.bean.createPortPolicy();
         var5.setPortName(var1.getPortName().getLocalPart());
         if (var4 != null) {
            createWspolicyBean(var5, var4.value());
         } else if (var3 != null) {
            createWspolicyBean(var5, var3);
         }

      }
   }

   public void visitMethod(JWSVisitor.WsMethod var1) {
      Method var2 = var1.getImplMethod();
      Policy var3 = (Policy)var2.getAnnotation(Policy.class);
      Policies var4 = (Policies)var2.getAnnotation(Policies.class);
      if (var3 != null || var4 != null) {
         OperationPolicyBean var5 = this.bean.createOperationPolicy();
         var5.setOperationName(var1.getOperationName());
         if (var4 != null) {
            createWspolicyBean(var5, var4.value());
         } else if (var3 != null) {
            createWspolicyBean(var5, var3);
         }

      }
   }

   private static void createWspolicyBean(PortPolicyBean var0, Policy... var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Policy var3 = var1[var2];
         if (var3 != null) {
            WsPolicyBean var4 = var0.createWsPolicy();
            var4.setUri(var3.uri());
            var4.setDirection(var3.direction().toString());
         }
      }

   }

   private static void createWspolicyBean(OperationPolicyBean var0, Policy... var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Policy var3 = var1[var2];
         if (var3 != null) {
            WsPolicyBean var4 = var0.createWsPolicy();
            var4.setUri(var3.uri());
            var4.setDirection(var3.direction().toString());
         }
      }

   }
}
