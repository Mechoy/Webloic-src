package weblogic.xml.jaxr.registry.command;

import java.util.Collection;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Key;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class FindServiceBindingsCommand extends BulkQueryCommand {
   private Key m_serviceKey;
   private Collection m_findQualifiers;
   private Collection m_classifications;
   private Collection m_specifications;

   public FindServiceBindingsCommand(RegistryServiceImpl var1, Key var2, Collection var3, Collection var4, Collection var5) {
      super(var1);
      this.m_serviceKey = var2;
      this.m_findQualifiers = var3;
      this.m_classifications = var4;
      this.m_specifications = var5;
   }

   public BulkResponse execute() throws JAXRException {
      return this.getRegistryServiceImpl().getRegistryProxy().findServiceBindings(this.m_serviceKey, this.m_findQualifiers, this.m_classifications, this.m_specifications);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_serviceKey, this.m_findQualifiers, this.m_classifications, this.m_specifications};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_serviceKey", "m_findQualifiers", "m_classifications", "m_specifications"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
