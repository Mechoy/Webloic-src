package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class DeleteConceptsCommand extends BulkQueryCommand {
   private Collection m_conceptKeys;

   public DeleteConceptsCommand(RegistryServiceImpl var1, Collection var2) {
      super(var1);
      this.m_conceptKeys = var2;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().deleteConcepts(this.m_conceptKeys);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_conceptKeys};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_conceptKeys"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
