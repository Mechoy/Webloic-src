package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.client.ServiceCreationInterceptor;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import javax.xml.ws.WebServiceFeature;

public class ServiceCreationInterceptorFeature extends WebServiceFeature {
   private static final String ID = ServiceCreationInterceptorFeature.class.getName();
   private Set<WeakReference<ServiceCreationInterceptor>> scis = new HashSet();

   public String getID() {
      return ID;
   }

   public Set<WeakReference<ServiceCreationInterceptor>> getInterceptors() {
      return this.scis;
   }
}
