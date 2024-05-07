package weblogic.wsee.tools.jws.policy;

import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;

public class WebServicePolicyProcessor extends JAXRPCProcessor {
   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (!this.moduleInfo.isWsdlOnly()) {
         WebservicePolicyRefBean var2 = this.moduleInfo.getWebservicePolicyRefBean();
         if (var2 == null) {
            EditableDescriptorManager var3 = new EditableDescriptorManager();
            var2 = (WebservicePolicyRefBean)var3.createDescriptorRoot(WebservicePolicyRefBean.class).getRootBean();
            this.moduleInfo.setWebservicePolicyRefBean(var2);
         }

      }
   }
}
