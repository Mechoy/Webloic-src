package weblogic.wsee.jaxws;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.xml.ws.WebServiceFeature;

public class EndpointCreationInterceptorFeature extends WebServiceFeature {
   private static final String ID = EndpointCreationInterceptorFeature.class.getName();
   private Set<WeakReference<EndpointCreationInterceptor>> ecis = Collections.synchronizedSet(new HashSet());

   public String getID() {
      return ID;
   }

   public Set<WeakReference<EndpointCreationInterceptor>> getInterceptors() {
      return this.ecis;
   }
}
