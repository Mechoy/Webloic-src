package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class SaveAssociationsCommand extends BulkQueryCommand {
   private Collection m_associations;
   private boolean m_replace;

   public SaveAssociationsCommand(RegistryServiceImpl var1, Collection var2, boolean var3) {
      super(var1);
      this.m_associations = var2;
      this.m_replace = var3;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().saveAssociations(this.m_associations, this.m_replace);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_associations, new Boolean(this.m_replace)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_associations", "m_replace"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
