package weblogic.wsee.async;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.rpc.JAXRPCException;
import weblogic.jws.WLDeployment;
import weblogic.jws.WLHttpTransport;
import weblogic.jws.WLHttpsTransport;
import weblogic.jws.WLJmsTransport;
import weblogic.wsee.deploy.VersioningHelper;
import weblogic.wsee.jws.container.Request;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.DirectInvokeUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsSkel;

@WebService(
   name = "AsyncResponseServicePortType",
   serviceName = "AsyncResponseService",
   targetNamespace = "http://www.bea.com/async/AsyncResponseService"
)
@SOAPBinding(
   style = Style.DOCUMENT,
   use = Use.LITERAL
)
@WLHttpTransport(
   portName = "AsyncResponseService",
   contextPath = "_async",
   serviceUri = "AsyncResponseService"
)
@WLHttpsTransport(
   portName = "AsyncResponseServiceHttps",
   contextPath = "_async",
   serviceUri = "AsyncResponseServiceHttps"
)
@WLJmsTransport(
   portName = "AsyncResponseServiceJms",
   contextPath = "_async",
   serviceUri = "AsyncResponseServiceJms"
)
@WLDeployment(
   deploymentListener = {"weblogic.wsee.async.AsyncResponseServiceDeploymentListener"}
)
public class AsyncResponseBean extends AbstractAsyncResponseBean {
   private static final boolean verbose = Verbose.isVerbose(AsyncResponseBean.class);

   protected void handleResult(String var1, WlMessageContext var2, Object var3) throws JAXRPCException {
      AsyncInvokeState var4 = (AsyncInvokeState)var2.getProperty("weblogic.wsee.async.invoke.state");
      if (verbose) {
         Verbose.log((Object)("Async response bean handleResult: ais msg id = " + var4.getMessageId()));
      }

      if (var4 != null) {
         AsyncUtil.SavedServiceInfo var5 = AsyncUtil.getSavedServiceInfo(var4, verbose);
         ClassLoader var6 = ((WsSkel)var5.wsPort.getEndpoint()).getClassLoader();
         Thread var7 = Thread.currentThread();
         ClassLoader var8 = var7.getContextClassLoader();

         try {
            var7.setContextClassLoader(var6);
            String var9 = (String)var4.getMessageContext().getProperty("weblogic.wsee.enclosing.classname");
            String var10 = AsyncUtil.getAsyncResponseMethodName(var4.getMessageContext());
            Class[] var11 = AsyncUtil.getAsyncResponseMethodParams(var4.getMessageContext());
            Object[] var12;
            if (var11.length == 1) {
               var12 = new Object[]{var5.apc};
            } else {
               var12 = new Object[]{var5.apc, var3};
            }

            Request var13 = new Request(var9, var10, var11, var12);
            DirectInvokeUtil.invoke(var5.uri, var13, (String)var2.getProperty("weblogic.wsee.conversation.ConversationId"), this._context);
            if (var5.version != null) {
               String var14 = (String)var4.getAsyncPostCallContext().getProperty("weblogic.wsee.async.appname");
               VersioningHelper.updateCount(var14, var5.version, -1);
            }
         } catch (JAXRPCException var20) {
            throw var20;
         } catch (IllegalStateException var21) {
         } catch (Throwable var22) {
            throw new JAXRPCException(var22.toString(), var22);
         } finally {
            var7.setContextClassLoader(var8);
         }

      }
   }

   protected void handleFault(String var1, WlMessageContext var2, Object var3) throws JAXRPCException {
      AsyncInvokeState var4 = (AsyncInvokeState)var2.getProperty("weblogic.wsee.async.invoke.state");
      if (verbose) {
         Verbose.log((Object)("Async response bean handleFault: ais msg id = " + var4.getMessageId()));
      }

      if (var4 != null) {
         AsyncUtil.SavedServiceInfo var5 = AsyncUtil.getSavedServiceInfo(var4, verbose);
         ClassLoader var6 = ((WsSkel)var5.wsPort.getEndpoint()).getClassLoader();
         Thread var7 = Thread.currentThread();
         ClassLoader var8 = var7.getContextClassLoader();

         try {
            var7.setContextClassLoader(var6);
            String var9 = (String)var4.getMessageContext().getProperty("weblogic.wsee.enclosing.classname");
            String var10 = AsyncUtil.getAsyncFailureMethodName(var4.getMessageContext());
            Class[] var11 = AsyncUtil.getAsyncFailureMethodParams();
            Object[] var12 = new Object[]{var5.apc, var3};
            Request var13 = new Request(var9, var10, var11, var12);
            DirectInvokeUtil.invoke(var5.uri, var13, (String)var2.getProperty("weblogic.wsee.conversation.ConversationId"), this._context);
            if (var5.version != null) {
               String var14 = (String)var4.getAsyncPostCallContext().getProperty("weblogic.wsee.async.appname");
               VersioningHelper.updateCount(var14, var5.version, -1);
            }
         } catch (JAXRPCException var20) {
            throw var20;
         } catch (IllegalStateException var21) {
         } catch (Throwable var22) {
            throw new JAXRPCException(var22);
         } finally {
            var7.setContextClassLoader(var8);
         }

      }
   }
}
