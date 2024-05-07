package weblogic.wsee.ws.dispatch.client;

import java.util.Collection;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.wsee.codec.CodecException;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.ws.dispatch.DispatcherImpl;
import weblogic.wsee.wsdl.WsdlBindingMessage;

public class CodecHandler extends GenericHandler {
   private static final boolean verbose;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -4896595801665155178L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.wsee.ws.dispatch.client.CodecHandler");
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public boolean handleRequest(MessageContext var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      if (var10000) {
         Object[] var3 = null;
         if (_WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low.isArgumentsCaptureNeeded()) {
            var3 = new Object[]{this, var1};
         }

         DynamicJoinPoint var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var14, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var12 = false;

      try {
         var12 = true;
         this.encode(var1);
         var10000 = true;
         var12 = false;
      } finally {
         if (var12) {
            boolean var15 = false;
            if (var8) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low, var9, var10);
            }

         }
      }

      if (var8) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low, var9, var10);
      }

      return var10000;
   }

   private void encode(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();
      WsdlBindingMessage var4 = this.getEncodeMessage(var3);

      try {
         ((DispatcherImpl)var3).getCodec().encode(var1, var4, var3.getWsMethod(), var3.getInParams());
      } catch (CodecException var6) {
         throw new InvocationException("[Client CodecHandler]Failed to encode", var6);
      }
   }

   private final WsdlBindingMessage getEncodeMessage(Dispatcher var1) {
      int var2 = var1.getOperation().getType();
      return var2 != 0 && var2 != 1 ? var1.getBindingOperation().getOutput() : var1.getBindingOperation().getInput();
   }

   public boolean handleResponse(MessageContext var1) {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      if (var10000) {
         Object[] var4 = null;
         if (_WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1};
         }

         DynamicJoinPoint var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var15, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var13 = false;

      try {
         var13 = true;
         if (verbose) {
            Verbose.log((Object)"Calling handle Response on codec handler");
         }

         if (var1.containsProperty("weblogic.wsee.client.validate_response")) {
            Boolean var2 = Boolean.valueOf((String)var1.getProperty("weblogic.wsee.client.validate_response"));
            var1.setProperty("weblogic.wsee.soap.validating_decoder", var2);
         }

         this.decode(var1);
         var10000 = true;
         var13 = false;
      } finally {
         if (var13) {
            boolean var16 = false;
            if (var9) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low, var10, var11);
            }

         }
      }

      if (var9) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low, var10, var11);
      }

      return var10000;
   }

   public boolean handleFault(MessageContext var1) {
      if (verbose) {
         Verbose.log((Object)"Calling handle fault on codec handler");
      }

      this.decode(var1);
      return true;
   }

   private void decode(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();
      if (var2.hasFault()) {
         if (verbose) {
            Verbose.log((Object)"Message context contain fault");
         }

         this.decodeFault(var3, var2);
      } else {
         if (verbose) {
            Verbose.log((Object)"Decoding message context");
         }

         this.decodeOutput(var3, var1);
      }

   }

   private void decodeFault(Dispatcher var1, WlMessageContext var2) {
      Collection var3 = var1.getBindingOperation().getFaults().values();

      try {
         ((DispatcherImpl)var1).getCodec().decodeFault(var2, var3, var1.getWsMethod());
      } catch (Throwable var5) {
         throw new InvocationException("[Client CodecHandler]Failed to decode fault", var5);
      }
   }

   private void decodeOutput(Dispatcher var1, MessageContext var2) {
      WsdlBindingMessage var3 = this.getDecodeMessage(var1);

      try {
         ((DispatcherImpl)var1).getCodec().decode(var2, var3, var1.getWsMethod(), var1.getOutParams());
      } catch (CodecException var5) {
         throw new InvocationException("[Client CodecHandler]Failed to decode", var5);
      }
   }

   private final WsdlBindingMessage getDecodeMessage(Dispatcher var1) {
      int var2 = var1.getOperation().getType();
      if (!$assertionsDisabled && var2 != 0 && var2 != 2) {
         throw new AssertionError();
      } else {
         return var2 == 0 ? var1.getBindingOperation().getOutput() : var1.getBindingOperation().getInput();
      }
   }

   public QName[] getHeaders() {
      return new QName[0];
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

   static {
      _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low");
      _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "CodecHandler.java", "weblogic.wsee.ws.dispatch.client.CodecHandler", "handleRequest", "(Ljavax/xml/rpc/handler/MessageContext;)Z", 45, InstrumentationSupport.makeMap(new String[]{"Webservices_JAXRPC_Diagnostic_Client_Request_Action_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("messageContext", "weblogic.diagnostics.instrumentation.gathering.WSSoapMessageContextSendingRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "CodecHandler.java", "weblogic.wsee.ws.dispatch.client.CodecHandler", "handleResponse", "(Ljavax/xml/rpc/handler/MessageContext;)Z", 75, InstrumentationSupport.makeMap(new String[]{"Webservices_JAXRPC_Diagnostic_Client_Response_Action_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("messageContext", "weblogic.diagnostics.instrumentation.gathering.WSSoapMessageContextNotSendingRenderer", false, true)})}), (boolean)0);
      $assertionsDisabled = !CodecHandler.class.desiredAssertionStatus();
      verbose = Verbose.isVerbose(CodecHandler.class);
   }
}
