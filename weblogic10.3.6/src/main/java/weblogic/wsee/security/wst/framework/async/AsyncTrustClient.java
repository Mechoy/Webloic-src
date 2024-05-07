package weblogic.wsee.security.wst.framework.async;

import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.binding.BindingImpl;
import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Holder;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service.Mode;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.util.EndpointAddressUtil;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss11.internal.WSS11Constants;

public class AsyncTrustClient {
   public static void process(MessageContext var0, String var1, String var2, boolean var3) {
      try {
         _process(var0, var1, var2);
      } catch (MalformedURLException var14) {
         throw new WebServiceException(var14);
      } catch (InterruptedException var15) {
         throw new WebServiceException(var15);
      } catch (IOException var16) {
         throw new WebServiceException(var16);
      } catch (ExecutionException var17) {
         if (var17.getCause() != null) {
            throw new WebServiceException(var17.getCause());
         }

         throw new WebServiceException(var17);
      } catch (WebServiceException var18) {
         throw var18;
      } catch (Throwable var19) {
         throw new WebServiceException(var19);
      } finally {
         PolicyContext.setPolicyIgnored(false);
      }

   }

   private static void _process(MessageContext var0, String var1, String var2) throws InterruptedException, ExecutionException, IOException {
      WsdlPort var3 = AsyncTrustClientHelper.getWsdlPort(var0);
      QName var4 = var3.getName();
      WSService var5 = AsyncTrustClientHelper.getWSService(var0);
      boolean var6 = false;
      Dispatch var7 = null;
      if (AsyncTrustClientHelper.isAsyncClientTransportFeatureEnabled(var0)) {
         AsyncClientTransportFeature var8 = AsyncTrustClientHelper.getAsyncClientTransportFeatureOnTrust(var0, var1);
         var6 = var8 == AsyncTrustClientHelper.getAsyncWebServiceFeature(var0);
         ((AsyncClientTransportFeature)var8).addAsyncEndpointListener(new AsyncClientTransportFeature.AsyncEndpointListener() {
            public void endpointSet(AsyncClientTransportFeature var1) {
               Collections.addAll(((BindingImpl)var1.getEndpoint().getBinding()).getKnownHeaders(), new QName[]{WSSConstants.SECURITY_QNAME, WSS11Constants.ENC_HEADER_QNAME});
            }

            public void endpointPublished(AsyncClientTransportFeature var1) {
            }

            public void endpointDisposed(AsyncClientTransportFeature var1) {
            }
         });
         var7 = var5.createDispatch(var4, SOAPMessage.class, Mode.MESSAGE, new WebServiceFeature[]{var8});
         Iterator var9 = var0.getPropertyNames();

         while(var9.hasNext()) {
            String var10 = (String)var9.next();
            var7.getRequestContext().put(var10, var0.getProperty(var10));
         }
      }

      String var16 = EndpointAddressUtil.getEndpointAddress(var0);
      if (var1 != null && !var1.equals("") && var16 != null && !var16.equals("") && !EndpointAddressUtil.getProtocolFromEndpointAddress(var16).equals(var1) && var1.equals("https")) {
         throw new MalformedURLException("Policy on STS endpoint requires " + var1 + " transport, but the endpoint address url of STS specified on client side doesn't comply with it :" + var16);
      } else {
         var7.getRequestContext().put("javax.xml.ws.service.endpoint.address", var16);
         String var17 = (String)var0.getProperty("weblogic.wsee.addressing.Action");
         var7.getRequestContext().put("javax.xml.ws.soap.http.soapaction.uri", var17);
         Collections.addAll(((BindingImpl)var7.getBinding()).getKnownHeaders(), new QName[]{WSSConstants.SECURITY_QNAME, WSS11Constants.ENC_HEADER_QNAME});

         try {
            SOAPMessage var18 = ((SOAPMessageContext)var0).getMessage();
            SOAPMessage var11 = invokeAsync(var18, var7);
            AsyncTrustClientHelper.setupCookies(var0, var11.getMimeHeaders());
            ((SOAPMessageContext)var0).setMessage(var11);
         } finally {
            if (!var6 && var7 instanceof Closeable) {
               ((Closeable)var7).close();
            }

         }

      }
   }

   private static SOAPMessage invokeAsync(SOAPMessage var0, Dispatch<SOAPMessage> var1) throws InterruptedException, ExecutionException {
      final Holder var2 = new Holder();
      final Semaphore var3 = new Semaphore(0);
      var1.invokeAsync(var0, new AsyncHandler<SOAPMessage>() {
         public void handleResponse(Response<SOAPMessage> var1) {
            try {
               var2.value = var1.get();
            } catch (Throwable var7) {
               var2.value = var7;
            } finally {
               var3.release();
            }

         }
      });
      if (var3.tryAcquire(1L, TimeUnit.HOURS)) {
         if (var2.value instanceof SOAPMessage) {
            return (SOAPMessage)var2.value;
         } else if (var2.value instanceof InterruptedException) {
            throw (InterruptedException)var2.value;
         } else if (var2.value instanceof ExecutionException) {
            throw (ExecutionException)var2.value;
         } else {
            throw new WebServiceException((Throwable)var2.value);
         }
      } else {
         throw new WebServiceException("Asynchronous trust client can not get asynchronous response within a given waiting time.");
      }
   }
}
