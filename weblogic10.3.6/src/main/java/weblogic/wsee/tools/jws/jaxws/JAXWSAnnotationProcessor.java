package weblogic.wsee.tools.jws.jaxws;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.wsee.deploy.WSEEAnnotationProcessor;
import weblogic.wsee.deploy.WSEEBaseModule;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;

public final class JAXWSAnnotationProcessor {
   private static final boolean verbose = Verbose.isVerbose(JAXWSAnnotationProcessor.class);
   private final WSEEAnnotationProcessor inner_proccessor = new WSEEAnnotationProcessor();

   public void process(Collection<Object> var1, ClassLoader var2, Map<String, Class> var3, boolean var4) throws WsException {
      if (var3 != null && !var3.isEmpty()) {
         WebservicesBean var5 = getOrCreateWebServicesBean(var1);
         WeblogicWebservicesBean var6 = getOrCreateWeblogicWebServicesBean(var1);
         Object var7 = var4 ? new WSEEWebModule(var3) : new WSEEEJBModule(var3);
         this.inner_proccessor.process(var5, var6, (WSEEBaseModule)var7, var2);
      }
   }

   private static final WebservicesBean getOrCreateWebServicesBean(Collection<Object> var0) {
      WebservicesBean var1 = null;
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var3 instanceof WebservicesBean) {
            var1 = (WebservicesBean)var3;
            break;
         }
      }

      if (var1 == null) {
         if (verbose) {
            Verbose.log((Object)"Creating web services bean.");
         }

         EditableDescriptorManager var4 = new EditableDescriptorManager();
         var1 = (WebservicesBean)var4.createDescriptorRoot(WebservicesBean.class).getRootBean();
         var1.setVersion("1.2");
         var0.add(var1);
      }

      return var1;
   }

   private static WeblogicWebservicesBean getOrCreateWeblogicWebServicesBean(Collection<Object> var0) {
      WeblogicWebservicesBean var1 = null;
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var3 instanceof WeblogicWebservicesBean) {
            var1 = (WeblogicWebservicesBean)var3;
            break;
         }
      }

      if (var1 == null) {
         if (verbose) {
            Verbose.log((Object)"Creating weblogic web services bean.");
         }

         EditableDescriptorManager var4 = new EditableDescriptorManager();
         var1 = (WeblogicWebservicesBean)var4.createDescriptorRoot(WeblogicWebservicesBean.class).getRootBean();
         var1.setVersion("1.2");
         var0.add(var1);
      }

      return var1;
   }

   private static final Class<?> loadJwsClass(ClassLoader var0, String var1) {
      try {
         return var0.loadClass(var1);
      } catch (ClassNotFoundException var3) {
         throw new AssertionError(var3);
      }
   }
}
