package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class FindCallerAssociationsCommand extends BulkQueryCommand {
   private Collection m_findQualifiers;
   private Boolean m_confirmedByCaller;
   private Boolean m_confirmedByOtherParty;
   private Collection m_associationTypes;

   public FindCallerAssociationsCommand(RegistryServiceImpl var1, Collection var2, Boolean var3, Boolean var4, Collection var5) {
      super(var1);
      this.m_findQualifiers = var2;
      this.m_confirmedByCaller = var3;
      this.m_confirmedByOtherParty = var4;
      this.m_associationTypes = var5;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().findCallerAssociations(this.m_findQualifiers, this.m_confirmedByCaller, this.m_confirmedByOtherParty, this.m_associationTypes);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_findQualifiers, this.m_confirmedByCaller, this.m_confirmedByOtherParty, this.m_associationTypes};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_findQualifiers", "m_confirmedByCaller", "m_confirmedByOtherParty", "m_associationTypes"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
