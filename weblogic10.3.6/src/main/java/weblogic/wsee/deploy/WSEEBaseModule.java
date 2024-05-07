package weblogic.wsee.deploy;

import java.util.Map;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;

public abstract class WSEEBaseModule {
   protected abstract String getLinkName(ServiceImplBeanBean var1);

   protected abstract void setLinkName(ServiceImplBeanBean var1, String var2);

   protected abstract Map<String, Class> getLinkMap();

   protected abstract EnvEntryBean[] getEnvEntries(ServiceImplBeanBean var1);
}
