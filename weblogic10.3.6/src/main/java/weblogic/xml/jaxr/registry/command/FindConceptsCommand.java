package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class FindConceptsCommand extends BulkQueryCommand {
   private Collection m_findQualifiers;
   private Collection m_namePatterns;
   private Collection m_classifications;
   private Collection m_externalIdentifiers;
   private Collection m_externalLinks;

   public FindConceptsCommand(RegistryServiceImpl var1, Collection var2, Collection var3, Collection var4, Collection var5, Collection var6) {
      super(var1);
      this.m_findQualifiers = var2;
      this.m_namePatterns = var3;
      this.m_classifications = var4;
      this.m_externalIdentifiers = var5;
      this.m_externalLinks = var6;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().findConcepts(this.m_findQualifiers, this.m_namePatterns, this.m_classifications, this.m_externalIdentifiers, this.m_externalLinks);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_findQualifiers, this.m_namePatterns, this.m_classifications, this.m_externalIdentifiers, this.m_externalLinks};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_findQualifiers", "m_namePatterns", "m_classifications", "m_externalIdentifiers", "m_externalLinks"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
