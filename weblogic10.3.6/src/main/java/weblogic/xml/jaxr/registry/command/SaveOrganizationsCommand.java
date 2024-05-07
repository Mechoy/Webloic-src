package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class SaveOrganizationsCommand extends BulkQueryCommand {
   private Collection m_organizations;

   public SaveOrganizationsCommand(RegistryServiceImpl var1, Collection var2) {
      super(var1);
      this.m_organizations = var2;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().saveOrganizations(this.m_organizations);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_organizations};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_organizations"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
