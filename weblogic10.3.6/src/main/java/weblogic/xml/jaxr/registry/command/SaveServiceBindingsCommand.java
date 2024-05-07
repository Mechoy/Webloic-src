package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class SaveServiceBindingsCommand extends BulkQueryCommand {
   private Collection m_bindings;

   public SaveServiceBindingsCommand(RegistryServiceImpl var1, Collection var2) {
      super(var1);
      this.m_bindings = var2;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().saveServiceBindings(this.m_bindings);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_bindings};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_bindings"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
