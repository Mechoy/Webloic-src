package weblogic.management.provider.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ClientAccess;

public class ClientAccessImpl implements ClientAccess {
   ClientAccessImpl() {
   }

   public DomainMBean getDomain(boolean var1) throws IOException, XMLStreamException {
      return this.getDomain("config/config.xml", var1);
   }

   public DomainMBean getDomain(String var1, boolean var2) throws IOException, XMLStreamException {
      return (DomainMBean)DescriptorManagerHelper.loadDescriptor(var1, var2, false, (List)null).getRootBean();
   }

   public void saveDomain(DomainMBean var1, OutputStream var2) throws IOException {
      AbstractDescriptorBean var3 = (AbstractDescriptorBean)var1;
      DescriptorManagerHelper.saveDescriptor(var3.getDescriptor(), var2);
   }

   public void saveDomainDirectory(DomainMBean var1, String var2) throws IOException {
      AbstractDescriptorBean var3 = (AbstractDescriptorBean)var1;
      DescriptorHelper.saveDescriptorTree(var3.getDescriptor(), false, var2, "UTF-8");
   }
}
