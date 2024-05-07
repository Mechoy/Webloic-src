package weblogic.wsee.tools.clientgen.callback;

import java.util.Iterator;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.tools.wsdlc.WsdlcUtils;
import weblogic.wsee.tools.wsdlc.jaxrpc.AddressInfo;
import weblogic.wsee.util.jspgen.ScriptException;

public class CallbackImpl extends CallbackBase {
   public void generate() throws ScriptException {
      this.out.print("package ");
      this.out.print(this.jws.getBinding().getPackageName());
      this.out.print(";");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("import javax.jws.WebService;");
      this.out.print("\n");
      this.out.print("import weblogic.jws.*;");
      this.out.print("\n");
      this.out.print("import javax.jws.soap.SOAPBinding;");
      this.out.print("\n");
      String var1 = this.jws.getServiceClassName();
      String var2 = this.jws.getImplClassName();
      this.out.print("\n");
      this.out.print("/**");
      this.out.print("\n");
      this.out.print(" * ");
      this.out.print(var2);
      this.out.print(" class implements web service endpoint interface ");
      this.out.print(var1);
      this.out.print(" * ");
      this.out.print(this.jws.isCallback81());
      this.out.print(" */");
      this.out.print("\n");
      if (this.jws.isCallback81() || this.jws.isUpgradedJWS()) {
         this.out.print("@weblogic.jws.wlw.UseWLW81BindingTypes()");
         this.out.print("\n");
         this.out.print("\n");
         this.out.print("@weblogic.wsee.tools.clientgen.callback.ClientSideCallbackService()");
      }

      this.out.print("\n");
      this.out.print("@WebService(");
      this.out.print("\n");
      this.out.print("  serviceName=\"");
      this.out.print(this.jws.isCallback81() ? ((AddressInfo)this.jws.getAddressInfos().get(0)).getPortName() : this.jws.getService().getName().getLocalPart());
      this.out.print("\",");
      this.out.print("\n");
      this.out.print("  endpointInterface=\"");
      this.out.print(this.jws.getBinding().getPackageName());
      this.out.print(".");
      this.out.print(var1);
      this.out.print("\",");
      this.out.print("\n");
      this.out.print("  targetNamespace=\"");
      this.out.print(this.jws.getService().getDefinitions().getTargetNamespace());
      this.out.print("\")");
      this.out.print("\n");
      this.out.print("@weblogic.jws.WLDeployment(deploymentListener={\"weblogic.wsee.conversation.ConversationCallbackDeploymentListener\",");
      this.out.print("\n");
      this.out.print("\"weblogic.wsee.callback.CallbackServiceDeploymentListener\"})");
      this.out.print("\n");
      this.out.print("@SOAPBinding(style=");
      this.out.print(this.jws.getSOAPBindingInfo().getStyle());
      this.out.print(", use=");
      this.out.print(this.jws.getSOAPBindingInfo().getUse());
      this.out.print(")");
      this.out.print("\n");
      this.out.print("\n");
      Iterator var3 = this.jws.getAddressInfos().iterator();

      while(var3.hasNext()) {
         AddressInfo var4 = (AddressInfo)var3.next();
         String var5 = this.getServiceUri(var4.getPortName());
         if (var4.getProtocol().equalsIgnoreCase("http")) {
            this.out.print("\n");
            this.out.print("@WLHttpTransport(");
            this.out.print("\n");
            this.out.print("  serviceUri=\"");
            this.out.print(var5);
            this.out.print("\",");
            this.out.print("\n");
            this.out.print("  portName=\"");
            this.out.print(var4.getPortName());
            this.out.print("\")");
            this.out.print("\n");
         } else if (var4.getProtocol().equalsIgnoreCase("https")) {
            this.out.print("\n");
            this.out.print("@WLHttpsTransport(");
            this.out.print("\n");
            this.out.print("  serviceUri=\"");
            this.out.print(var5);
            this.out.print("\",");
            this.out.print("\n");
            this.out.print("  portName=\"");
            this.out.print(var4.getPortName());
            this.out.print("\")");
            this.out.print("\n");
         } else if (var4.getProtocol().equalsIgnoreCase("jms")) {
            this.out.print("\n");
            this.out.print("@WLJmsTransport(");
            this.out.print("\n");
            this.out.print("  serviceUri=\"");
            this.out.print(var5);
            this.out.print("\",");
            this.out.print("\n");
            this.out.print("  portName=\"");
            this.out.print(var4.getPortName());
            this.out.print("\",");
            this.out.print("\n");
            this.out.print("  queue=\"");
            this.out.print(this.getCallbackQueue());
            this.out.print("\")");
            this.out.print("\n");
         }
      }

      this.out.print("\n");
      this.out.print("public class ");
      this.out.print(var2);
      this.out.print(" implements ");
      this.out.print(var1);
      this.out.print(", javax.xml.rpc.server.ServiceLifecycle");
      this.out.print("\n");
      this.out.print("{");
      this.out.print("\n");
      this.out.print("  private javax.xml.rpc.server.ServletEndpointContext _context;");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("  public void init(Object context) {");
      this.out.print("\n");
      this.out.print("    this._context = (javax.xml.rpc.server.ServletEndpointContext)context;");
      this.out.print("\n");
      this.out.print("  }");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("    public void destroy(){");
      this.out.print("\n");
      this.out.print("    }");
      this.out.print("\n");
      JsMethod[] var7 = this.jws.getBinding().getMethods();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         JsMethod var9 = var7[var8];
         String var6 = var9.getReturnType().getType();
         this.out.print("\n");
         this.out.print("  public ");
         this.out.print(var6);
         this.out.print(" ");
         this.out.print(var9.getMethodName());
         this.out.print("(");
         this.out.print(var9.getArgumentString());
         this.out.print(")");
         this.out.print("\n");
         this.out.print("    ");
         this.out.print(WsdlcUtils.throwException(var9));
         this.out.print("  {");
         this.out.print("\n");
         this.out.print("    ");
         this.out.print("   try {");
         this.out.print("\n");
         this.out.print("     weblogic.wsee.message.WlMessageContext wlmc = (weblogic.wsee.message.WlMessageContext)_context.getMessageContext();");
         this.out.print("\n");
         this.out.print("     String serviceURI = (String)wlmc.getProperty(");
         this.out.print("\n");
         this.out.print("       weblogic.wsee.async.AsyncConstants.ENCLOSING_JWS_SERVICEURI);");
         this.out.print("\n");
         this.out.print("     if (serviceURI == null) throw new javax.xml.rpc.JAXRPCException(\"no service URI specified\");");
         this.out.print("\n");
         this.out.print("\n");
         this.out.print("     weblogic.wsee.ws.WsRegistry registry = weblogic.wsee.ws.WsRegistry.instance();");
         this.out.print("\n");
         this.out.print("     java.lang.String _appver = (java.lang.String)wlmc.getProperty(weblogic.wsee.callback.CallbackConstants.CALLBACK_APPVERSIONID_PROPERTY);");
         this.out.print("\n");
         this.out.print("     weblogic.wsee.ws.WsPort wsPort = registry.lookup(serviceURI, _appver);");
         this.out.print("\n");
         this.out.print("     if (wsPort == null) throw new javax.xml.rpc.JAXRPCException(\"no port found for URI \" + serviceURI + (_appver == null ? \"\" : \"version \" + _appver));");
         this.out.print("\n");
         this.out.print("     weblogic.wsee.component.pojo.JavaClassComponent component = (weblogic.wsee.component.pojo.JavaClassComponent) ((weblogic.wsee.ws.WsSkel) wsPort.getEndpoint()).getComponent();");
         this.out.print("\n");
         this.out.print("\n");
         this.out.print("     java.lang.Class _targetClass = component.getTarget().getClass();");
         this.out.print("\n");
         this.out.print("     java.lang.String _methodName = (java.lang.String)");
         this.out.print("\n");
         this.out.print("       wlmc.getProperty(weblogic.wsee.callback.CallbackConstants.CALLBACK_METHOD_PROPERTY);");
         this.out.print("\n");
         this.out.print("     if (_methodName == null) throw new javax.xml.rpc.JAXRPCException(\"no callback method found\");");
         this.out.print("\n");
         this.out.print("     java.lang.Class[] _types = ");
         this.out.print(this.argTypes(var9));
         this.out.print(";");
         this.out.print("\n");
         this.out.print("     java.lang.Object[] _values = ");
         this.out.print(this.argValues(var9));
         this.out.print(";");
         this.out.print("\n");
         this.out.print("     weblogic.wsee.jws.container.Request request = new");
         this.out.print("\n");
         this.out.print("     weblogic.wsee.jws.container.Request(_targetClass, _methodName, _types, _values);");
         this.out.print("\n");
         this.out.print("\n");
         this.out.print("     Object _retVal = weblogic.wsee.util.DirectInvokeUtil.invoke(serviceURI, request,");
         this.out.print("\n");
         this.out.print("       (String)wlmc.getProperty(weblogic.wsee.conversation.ConversationHandler.CONVERSATION_ID),");
         this.out.print("\n");
         this.out.print("       _context);");
         this.out.print("\n");
         this.out.print("     ");
         this.out.print(this.returnValue(var9));
         this.out.print("   } catch (javax.xml.rpc.JAXRPCException jrpce) {");
         this.out.print("\n");
         this.out.print("     throw jrpce;");
         this.out.print("\n");
         this.out.print("   } catch (Throwable e) {");
         this.out.print("\n");
         this.out.print("     throw new javax.xml.rpc.JAXRPCException(e);");
         this.out.print("\n");
         this.out.print("   }");
         this.out.print("  }");
      }

      this.out.print("}");
   }
}