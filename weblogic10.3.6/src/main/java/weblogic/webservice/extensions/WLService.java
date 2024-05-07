package weblogic.webservice.extensions;

import javax.xml.rpc.Service;
import weblogic.webservice.context.ContextNotFoundException;
import weblogic.webservice.context.WebServiceContext;

/** @deprecated */
public interface WLService extends Service {
   WebServiceContext context();

   WebServiceContext joinContext() throws ContextNotFoundException;
}
