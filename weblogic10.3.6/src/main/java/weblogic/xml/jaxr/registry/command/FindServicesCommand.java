package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Key;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class FindServicesCommand extends BulkQueryCommand {
   private Key m_orgKey;
   private Collection m_findQualifiers;
   private Collection m_namePatterns;
   private Collection m_classifications;
   private Collection m_specifications;

   public FindServicesCommand(RegistryServiceImpl var1, Key var2, Collection var3, Collection var4, Collection var5, Collection var6) {
      super(var1);
      this.m_orgKey = var2;
      this.m_findQualifiers = var3;
      this.m_namePatterns = var4;
      this.m_classifications = var5;
      this.m_specifications = var6;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().findServices(this.m_orgKey, this.m_findQualifiers, this.m_namePatterns, this.m_classifications, this.m_specifications);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_orgKey, this.m_findQualifiers, this.m_namePatterns, this.m_classifications, this.m_specifications};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_orgKey", "m_findQualifiers", "m_namePatterns", "m_classifications", "m_specifications"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
