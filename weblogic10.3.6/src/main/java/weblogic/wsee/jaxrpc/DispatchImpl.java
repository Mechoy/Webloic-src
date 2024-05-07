package weblogic.wsee.jaxrpc;

import com.bea.xml.XmlException;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.RuntimeBindingsBuilder;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class DispatchImpl implements Dispatch {
   private static final boolean verbose = Verbose.isVerbose(DispatchImpl.class);
   private static final QName any = new QName("http://www.w3.org/2001/XMLSchema", "any");
   private Call call;
   private JAXRPCContext context;

   public DispatchImpl(QName var1, ServiceImpl var2) throws ServiceException {
      this.call = var2.createCall();
      this.setRuntimeBindings();
      this.call.setProperty("javax.xml.rpc.soap.operation.style", "document");
      this.call.setOperationName(new QName("invoke"));
      this.call.setReturnType(any, SOAPElement.class);
      this.call.addParameter("parameters", any, SOAPElement.class, ParameterMode.IN);
   }

   private void setRuntimeBindings() throws ServiceException {
      RuntimeBindingsBuilder var1 = RuntimeBindingsBuilder.Factory.newInstance();

      try {
         RuntimeBindings var2 = var1.createGenericRuntimeBindings();
         this.call.setProperty("weblogic.wsee.bind.runtimeBindingProvider", var2);
      } catch (IOException var3) {
         throw new ServiceException("Failed to create runtime bindings", var3);
      } catch (XmlException var4) {
         throw new ServiceException("Failed to create runtime bindings", var4);
      }
   }

   public JAXRPCContext getContext() {
      if (this.context == null) {
         this.context = new JAXRPCContext(this.call);
      }

      return this.context;
   }

   public Object invoke(Object var1) throws RemoteException {
      return this.call.invoke(new Object[]{var1});
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
