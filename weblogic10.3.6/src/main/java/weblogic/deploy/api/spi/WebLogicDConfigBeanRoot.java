package weblogic.deploy.api.spi;

import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.descriptor.DescriptorBean;

public interface WebLogicDConfigBeanRoot extends DConfigBeanRoot, WebLogicDConfigBean {
   int DEPENDENCY = 0;
   int DECLARATION = 1;
   int CONFIGURABLE = 2;
   int CHANGABLE = 3;
   int DYNAMIC = 4;

   boolean isSchemaBased();

   String getDConfigName();

   boolean hasDD();

   boolean isExternal();

   void setExternal(boolean var1);

   DConfigBean[] getSecondaryDescriptors();

   DescriptorSupport getDescriptorSupport();

   DConfigBean getDConfigBean(DDBeanRoot var1, DescriptorSupport var2) throws ConfigurationException;

   void export(int var1) throws IllegalArgumentException;

   void export(DescriptorBean var1, String[] var2) throws IllegalArgumentException;

   String getUri();

   void close();
}
