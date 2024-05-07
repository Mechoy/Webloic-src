package weblogic.diagnostics.context;

import java.io.IOException;

public interface DiagnosticContextModifier {
   byte[] getContext() throws IOException;

   void setContext(byte[] var1) throws IOException;

   void setContextId(String var1);

   public static final class Factory {
      public static DiagnosticContextModifier getInstance() {
         return DiagnosticContextModifierImpl.getInstance();
      }
   }
}
