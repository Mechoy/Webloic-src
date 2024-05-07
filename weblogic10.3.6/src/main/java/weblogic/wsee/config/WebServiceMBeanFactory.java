package weblogic.wsee.config;

import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.wsee.server.ServerUtil;

public class WebServiceMBeanFactory {
   private static WebServiceMBean _instance = new WebServiceMBeanImpl();

   public static WebServiceMBean getInstance() {
      return KernelStatus.isServer() ? ServerUtil.getWebServiceMBean() : _instance;
   }
}
