package weblogic.xml.jaxr.registry.command;

import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.BaseJAXRObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public abstract class BulkQueryCommand extends BaseJAXRObject {
   private RegistryServiceImpl m_registryServiceImpl;

   public BulkQueryCommand(RegistryServiceImpl var1) {
      this.m_registryServiceImpl = var1;
   }

   public RegistryServiceImpl getRegistryServiceImpl() {
      return this.m_registryServiceImpl;
   }

   public abstract BulkResponse execute() throws JAXRException;

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_registryServiceImpl};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_registryServiceImpl"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
