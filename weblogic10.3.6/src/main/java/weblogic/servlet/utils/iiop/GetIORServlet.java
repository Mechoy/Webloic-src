package weblogic.servlet.utils.iiop;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.corba.cos.naming.RootNamingContextImpl;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.iiop.IOR;

public final class GetIORServlet extends HttpServlet {
   static final long serialVersionUID = -4327199715232441045L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.utils.iiop.GetIORServlet");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public void doGet(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      boolean var11;
      boolean var10000 = var11 = _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      Object var10 = null;
      if (var10000) {
         Object[] var6 = null;
         if (_WLDF$INST_FLD_Servlet_Request_Action_Around_Medium.isArgumentsCaptureNeeded()) {
            var6 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var6, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium;
         DiagnosticAction[] var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         IOR var3 = null;
         var3 = RootNamingContextImpl.getRootNamingContext().getIOR();
         PrintWriter var4 = var2.getWriter();
         var4.println("host IOR:");
         var4.println(var3.stringify());
      } finally {
         if (var11) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium, var12, var13);
         }

      }

   }

   static {
      _WLDF$INST_FLD_Servlet_Request_Action_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Request_Action_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "GetIORServlet.java", "weblogic.servlet.utils.iiop.GetIORServlet", "doGet", "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V", 14, InstrumentationSupport.makeMap(new String[]{"Servlet_Request_Action_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), null})}), (boolean)0);
   }
}
