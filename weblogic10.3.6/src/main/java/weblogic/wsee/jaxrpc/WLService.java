package weblogic.wsee.jaxrpc;

import java.io.InputStream;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import weblogic.wsee.context.ContextNotFoundException;
import weblogic.wsee.context.WebServiceContext;

public interface WLService extends Service {
   WebServiceContext context();

   WebServiceContext joinContext() throws ContextNotFoundException;

   Dispatch createDispatch(QName var1) throws ServiceException;

   void loadPolicy(String var1, String var2, InputStream[] var3, InputStream[] var4) throws ServiceException;
}
