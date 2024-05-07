package weblogic.wsee.tools.jws;

import java.io.File;
import java.util.List;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.wsee.tools.anttasks.JwscTask;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;

public interface ModuleInfo {
   JwsBuildContext getJwsBuildContext();

   boolean isWsdlOnly();

   boolean isGenerateWsdl();

   boolean isGenerateDescriptors();

   void setGenerateDescriptors(boolean var1);

   File getOutputDir();

   WebservicesBean getWebServicesBean();

   void setWebServicesBean(WebservicesBean var1);

   WeblogicWebservicesBean getWeblogicWebservicesBean();

   void setWeblogicWebservicesBean(WeblogicWebservicesBean var1);

   WebAppBean getWebAppBean();

   void setWebAppBean(WebAppBean var1);

   WeblogicWebAppBean getWeblogicWebAppBean();

   void setWeblogicWebAppBean(WeblogicWebAppBean var1);

   WebservicePolicyRefBean getWebservicePolicyRefBean();

   void setWebservicePolicyRefBean(WebservicePolicyRefBean var1);

   List<WebServiceDecl> getWebServices();

   boolean isJaxRPCWrappedArrayStyle();

   boolean isUpperCasePropName();

   boolean isLocalElementDefaultRequired();

   boolean isLocalElementDefaultNillable();

   JwscTask getOwningTask();
}
