package weblogic.diagnostics.context;

import weblogic.workarea.WorkContext;

public interface DiagnosticContext extends WorkContext, DiagnosticContextConstants {
   String DIAGNOSTIC_CONTEXT_NAME = "weblogic.diagnostics.DiagnosticContext";

   String getContextId();

   void setDye(byte var1, boolean var2) throws InvalidDyeException;

   boolean isDyedWith(byte var1) throws InvalidDyeException;

   void setDyeVector(long var1);

   long getDyeVector();

   String getPayload();

   void setPayload(String var1);
}
