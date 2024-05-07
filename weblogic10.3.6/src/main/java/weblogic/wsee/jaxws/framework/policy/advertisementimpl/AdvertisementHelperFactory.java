package weblogic.wsee.jaxws.framework.policy.advertisementimpl;

import javax.xml.ws.WebServiceException;

public class AdvertisementHelperFactory {
   private static final String HELPER_CLASS_NAME = "weblogic.wsee.jaxws.framework.policy.advertisementimpl.AdvertisementHelperImpl";
   private static final String ORAWSDL_TEST_CLASS_NAME = "javax.wsdl.WSDLException";

   public static AdvertisementHelper getAdvertisementHelper() {
      ClassLoader var0 = Thread.currentThread().getContextClassLoader();

      try {
         Class.forName("javax.wsdl.WSDLException", false, var0);
         Class var1 = var0.loadClass("weblogic.wsee.jaxws.framework.policy.advertisementimpl.AdvertisementHelperImpl");
         return (AdvertisementHelper)var1.newInstance();
      } catch (ClassNotFoundException var2) {
         return null;
      } catch (InstantiationException var3) {
         throw new WebServiceException(var3);
      } catch (IllegalAccessException var4) {
         throw new WebServiceException(var4);
      }
   }
}
