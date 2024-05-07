package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class DeleteAssociationsCommand extends BulkQueryCommand {
   private Collection m_associationKeys;

   public DeleteAssociationsCommand(RegistryServiceImpl var1, Collection var2) {
      super(var1);
      this.m_associationKeys = var2;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().deleteAssociations(this.m_associationKeys);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_associationKeys};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_associationKeys"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
