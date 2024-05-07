package weblogic.wsee.ws.dispatch.server;

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
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.ws.dispatch.DispatcherImpl;
import weblogic.wsee.wsdl.WsdlBindingMessage;

public class CodecHandler extends GenericHandler {
   public static final String SERVICE_SPECIFIC_EXCEPTION = "weblogic.wsee.service_specific_exception";
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = -7246783455502741366L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.wsee.ws.dispatch.server.CodecHandler");
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public boolean handleRequest(MessageContext var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      if (var10000) {
         Object[] var3 = null;
         if (_WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low.isArgumentsCaptureNeeded()) {
            var3 = new Object[]{this, var1};
         }

         DynamicJoinPoint var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var14, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var12 = false;

      try {
         var12 = true;
         this.decode(var1);
         var10000 = true;
         var12 = false;
      } finally {
         if (var12) {
            boolean var15 = false;
            if (var8) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low, var9, var10);
            }

         }
      }

      if (var8) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low, var9, var10);
      }

      return var10000;
   }

   public boolean handleResponse(MessageContext var1) {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      if (var10000) {
         Object[] var4 = null;
         if (_WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1};
         }

         DynamicJoinPoint var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var15, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var13 = false;

      try {
         var13 = true;
         Throwable var2 = (Throwable)var1.getProperty("weblogic.wsee.service_specific_exception");
         if (var2 != null) {
            this.encodeFault(var1, var2);
         } else {
            this.encode(var1);
         }

         var10000 = true;
         var13 = false;
      } finally {
         if (var13) {
            boolean var16 = false;
            if (var9) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low, var10, var11);
            }

         }
      }

      if (var9) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low, var10, var11);
      }

      return var10000;
   }

   public boolean handleFault(MessageContext var1) {
      Throwable var2 = (Throwable)var1.getProperty("weblogic.wsee.service_specific_exception");
      if (var2 != null) {
         this.encodeFault(var1, var2);
      }

      return true;
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   private void encodeFault(MessageContext var1, Throwable var2) {
      Dispatcher var3 = this.getDispatcher(var1);

      try {
         boolean var4 = ((DispatcherImpl)var3).getCodec().encodeFault(var1, var3.getWsMethod(), var2);
         if (!var4) {
            throw new InvocationException("[Server CodecHandler] Failed to encode fault ", var2);
         }
      } catch (CodecException var5) {
         throw new InvocationException("[Server CodecHandler] Failed to encode ", var5);
      }

      var1.removeProperty("weblogic.wsee.service_specific_exception");
   }

   private void encode(MessageContext var1) {
      Dispatcher var2 = this.getDispatcher(var1);

      try {
         WsdlBindingMessage var3 = this.getEncodeMessage(var2);
         ((DispatcherImpl)var2).getCodec().encode(var1, var3, var2.getWsMethod(), var2.getOutParams());
      } catch (CodecException var4) {
         throw new InvocationException("[Server CodecHandler] Failed to encode ", var4);
      }
   }

   private Dispatcher getDispatcher(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();
      return var3;
   }

   private final WsdlBindingMessage getEncodeMessage(Dispatcher var1) {
      int var2 = var1.getOperation().getType();
      if (!$assertionsDisabled && var2 != 0 && var2 != 2) {
         throw new AssertionError();
      } else {
         return var2 == 0 ? var1.getBindingOperation().getOutput() : var1.getBindingOperation().getInput();
      }
   }

   private void decode(MessageContext var1) {
      Dispatcher var2 = this.getDispatcher(var1);

      try {
         WsdlBindingMessage var3 = this.getDecodeMessage(var2);
         ((DispatcherImpl)var2).getCodec().decode(var1, var3, var2.getWsMethod(), var2.getInParams());
      } catch (CodecException var4) {
         throw new InvocationException("[Server CodecHandler] Failed to decode ", var4);
      }
   }

   private final WsdlBindingMessage getDecodeMessage(Dispatcher var1) {
      int var2 = var1.getOperation().getType();
      return var2 != 0 && var2 != 1 ? var1.getBindingOperation().getOutput() : var1.getBindingOperation().getInput();
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
      _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low");
      _WLDF$INST_FLD_Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "CodecHandler.java", "weblogic.wsee.ws.dispatch.server.CodecHandler", "handleRequest", "(Ljavax/xml/rpc/handler/MessageContext;)Z", 40, InstrumentationSupport.makeMap(new String[]{"Webservices_JAXRPC_Diagnostic_Request_Action_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("messageContext", "weblogic.diagnostics.instrumentation.gathering.WSSoapMessageContextNotSendingRenderer", false, true)})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "CodecHandler.java", "weblogic.wsee.ws.dispatch.server.CodecHandler", "handleResponse", "(Ljavax/xml/rpc/handler/MessageContext;)Z", 45, InstrumentationSupport.makeMap(new String[]{"Webservices_JAXRPC_Diagnostic_Response_Action_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("messageContext", "weblogic.diagnostics.instrumentation.gathering.WSSoapMessageContextSendingRenderer", false, true)})}), (boolean)0);
      $assertionsDisabled = !CodecHandler.class.desiredAssertionStatus();
   }
}
