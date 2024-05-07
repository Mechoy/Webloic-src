package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class DeleteServicesCommand extends BulkQueryCommand {
   private Collection m_serviceKeys;

   public DeleteServicesCommand(RegistryServiceImpl var1, Collection var2) {
      super(var1);
      this.m_serviceKeys = var2;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().deleteServices(this.m_serviceKeys);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_serviceKeys};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_serviceKeys"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
