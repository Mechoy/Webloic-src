package weblogic.xml.jaxr.registry.infomodel;

import java.io.Serializable;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.InternationalString;
import weblogic.xml.jaxr.registry.BaseJAXRObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRLogger;

public abstract class BaseInfoModelObject extends BaseJAXRObject implements Serializable {
   private static final long serialVersionUID = -1L;
   private transient RegistryServiceImpl m_registryServiceImpl;

   public BaseInfoModelObject(RegistryServiceImpl var1) {
      this.m_registryServiceImpl = var1;
   }

   protected RegistryServiceImpl getRegistryService() {
      return this.m_registryServiceImpl;
   }

   protected InternationalString getEmptyInternationalString() {
      return new InternationalStringImpl(this.getRegistryService());
   }

   protected JAXRLogger getLogger() {
      return this.m_registryServiceImpl.getLogger();
   }

   protected void checkCapability(int var1) throws JAXRException {
      this.checkCapability(this.m_registryServiceImpl, var1);
   }
}
