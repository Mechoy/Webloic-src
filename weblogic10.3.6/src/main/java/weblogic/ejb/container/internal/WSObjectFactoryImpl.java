package weblogic.ejb.container.internal;

import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.spi.BaseWSObjectIntf;
import weblogic.ejb.spi.WSObjectFactory;
import weblogic.utils.Debug;

public class WSObjectFactoryImpl implements WSObjectFactory {
   private final Class wsoClass;
   private final BeanManager beanManager;
   private final ClientDrivenBeanInfo cdBeanInfo;

   public WSObjectFactoryImpl(BeanManager var1, ClientDrivenBeanInfo var2) {
      this.beanManager = var1;
      this.cdBeanInfo = var2;
      this.wsoClass = this.cdBeanInfo.getWebserviceObjectClass();
   }

   public BaseWSObjectIntf create() {
      Debug.assertion(this.wsoClass != null, "webservice object class is NULL !");
      BaseWSLocalObject var1 = null;

      try {
         var1 = (BaseWSLocalObject)this.wsoClass.newInstance();
      } catch (Exception var3) {
         throw new AssertionError("Exception attempting to create new webservice object class '" + this.wsoClass.getName() + "'  " + var3.getMessage());
      }

      var1.setBeanManager(this.beanManager);
      var1.setBeanInfo(this.cdBeanInfo);
      return var1;
   }
}
