package weblogic.servlet.http;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Servlet;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.StubLifecycleHelper;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public class AsyncServletSupportImpl implements AsyncServletSupport {
   static final long serialVersionUID = 9061902273868226534L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.http.AsyncServletSupportImpl");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public void notify(RequestResponseKey var1, Object var2) throws IOException {
      ServletRequestImpl var3 = ServletRequestImpl.getOriginalRequest(var1.getRequest());
      ClassLoader var4 = null;

      try {
         var4 = var3.getContext().pushEnvironment(Thread.currentThread());
         synchronized(var1) {
            if (!var1.isValid()) {
               return;
            }

            if (var1.isCallDoResponse()) {
               ServletStubImpl var6 = var3.getServletStub();
               Servlet var7 = var6.getLifecycleHelper().getServlet();
               AbstractAsyncServlet var10000 = (AbstractAsyncServlet)var7;
               RequestResponseKey var10001 = var1;
               Object var10002 = var2;
               Object var10 = _WLDF$INST_JPFLD_0;
               boolean var16;
               boolean var10003 = var16 = _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium.isEnabledAndNotDyeFiltered();
               DiagnosticAction[] var17 = null;
               DiagnosticActionState[] var18 = null;
               Object var15 = null;
               if (var10003) {
                  Object[] var11 = null;
                  if (_WLDF$INST_FLD_Servlet_Async_Action_Around_Medium.isArgumentsCaptureNeeded()) {
                     var11 = InstrumentationSupport.toSensitive(3);
                  }

                  Object var32 = var10 = InstrumentationSupport.createDynamicJoinPoint((JoinPoint)var10, var11, (Object)null);
                  DelegatingMonitor var10004 = _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium;
                  DiagnosticAction[] var10005 = var17 = var10004.getActions();
                  InstrumentationSupport.preProcess((JoinPoint)var32, var10004, var10005, var18 = InstrumentationSupport.getActionStates(var10005));
               }

               boolean var27 = false;

               try {
                  var27 = true;
                  var10000.doResponse(var10001, var10002);
                  if (var16) {
                     InstrumentationSupport.postProcess((JoinPoint)var10, _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium, var17, var18);
                     var27 = false;
                  } else {
                     var27 = false;
                  }
               } finally {
                  if (var27) {
                     if (var16) {
                        InstrumentationSupport.postProcess((JoinPoint)var10, _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium, var17, var18);
                     }

                  }
               }
            }

            var1.closeResponse();
         }
      } catch (Throwable var30) {
         var3.getContext().handleThrowableFromInvocation(var30, var3, (ServletResponseImpl)var1.getResponse());
      } finally {
         WebAppServletContext.popEnvironment(Thread.currentThread(), var4);
      }

   }

   public void timeout(final RequestResponseKey var1) {
      final ServletStubImpl var2 = ServletRequestImpl.getOriginalRequest(var1.getRequest()).getServletStub();
      if (var1.isValid()) {
         WorkManagerFactory.getInstance().getDefault().schedule(new WorkAdapter() {
            static final long serialVersionUID = -7864110671009275502L;
            public static final String _WLDF$INST_VERSION = "9.0.0";
            // $FF: synthetic field
            static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.http.AsyncServletSupportImpl$1");
            public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium;
            public static final JoinPoint _WLDF$INST_JPFLD_0;

            public void run() {
               try {
                  synchronized(var1) {
                     if (var1.isValid()) {
                        StubLifecycleHelper var2x = var2.getLifecycleHelper();
                        AbstractAsyncServlet var3 = null;
                        if (var2x != null) {
                           var3 = (AbstractAsyncServlet)var2x.getServlet();
                        }

                        if (var3 != null) {
                           AbstractAsyncServlet var10000 = var3;
                           RequestResponseKey var10001 = var1;
                           Object var5 = _WLDF$INST_JPFLD_0;
                           boolean var11;
                           boolean var10002 = var11 = _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium.isEnabledAndNotDyeFiltered();
                           DiagnosticAction[] var12 = null;
                           DiagnosticActionState[] var13 = null;
                           Object var10 = null;
                           if (var10002) {
                              Object[] var6 = null;
                              if (_WLDF$INST_FLD_Servlet_Async_Action_Around_Medium.isArgumentsCaptureNeeded()) {
                                 var6 = InstrumentationSupport.toSensitive(2);
                              }

                              Object var21 = var5 = InstrumentationSupport.createDynamicJoinPoint((JoinPoint)var5, var6, (Object)null);
                              DelegatingMonitor var10003 = _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium;
                              DiagnosticAction[] var10004 = var12 = var10003.getActions();
                              InstrumentationSupport.preProcess((JoinPoint)var21, var10003, var10004, var13 = InstrumentationSupport.getActionStates(var10004));
                           }

                           boolean var17 = false;

                           try {
                              var17 = true;
                              var10000.doTimeout(var10001);
                              if (var11) {
                                 InstrumentationSupport.postProcess((JoinPoint)var5, _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium, var12, var13);
                                 var17 = false;
                              } else {
                                 var17 = false;
                              }
                           } finally {
                              if (var17) {
                                 if (var11) {
                                    InstrumentationSupport.postProcess((JoinPoint)var5, _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium, var12, var13);
                                 }

                              }
                           }
                        } else {
                           var1.getResponse().setStatus(503);
                        }

                        var1.closeResponse();
                     }
                  }
               } catch (Exception var20) {
                  throw (RuntimeException)(new RuntimeException("AbstractAsyncServlet timeout failed")).initCause(var20);
               }
            }

            static {
               _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Async_Action_Around_Medium");
               _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "AsyncServletSupportImpl.java", "weblogic.servlet.http.AbstractAsyncServlet", "doTimeout", "(Lweblogic/servlet/http/RequestResponseKey;)V", 69, (Map)null, (boolean)0);
            }
         });
      }
   }

   static {
      _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Async_Action_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "AsyncServletSupportImpl.java", "weblogic.servlet.http.AbstractAsyncServlet", "doResponse", "(Lweblogic/servlet/http/RequestResponseKey;Ljava/lang/Object;)V", 34, (Map)null, (boolean)0);
   }
}
