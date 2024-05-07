package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class FindAssociationsCommand extends BulkQueryCommand {
   private Collection m_findQualifiers;
   private String m_sourceObjectId;
   private String m_targetObjectId;
   private Collection m_associationTypes;

   public FindAssociationsCommand(RegistryServiceImpl var1, Collection var2, String var3, String var4, Collection var5) {
      super(var1);
      this.m_findQualifiers = var2;
      this.m_sourceObjectId = var3;
      this.m_targetObjectId = var4;
      this.m_associationTypes = var5;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().findAssociations(this.m_findQualifiers, this.m_sourceObjectId, this.m_targetObjectId, this.m_associationTypes);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_findQualifiers, this.m_sourceObjectId, this.m_targetObjectId, this.m_associationTypes};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_findQualifiers", "m_sourceObjectId", "m_targetObjectId", "m_associationTypes"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
