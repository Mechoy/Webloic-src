package weblogic.servlet.internal;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import weblogic.management.runtime.PageFlowError;

public final class PageFlowErrorImpl implements PageFlowError, Serializable {
   private static final long serialVersionUID = 1L;
   private long _timestamp;
   private String _stackTrace;
   private String _message;

   public PageFlowErrorImpl(Throwable var1) {
      assert var1 != null;

      this._timestamp = System.currentTimeMillis();
      this._stackTrace = stringify(var1);
      this._message = var1.getLocalizedMessage();
      if (this._message == null) {
         this._message = var1.getMessage();
      }

   }

   public long getTimeStamp() {
      return this._timestamp;
   }

   public String getStackTraceAsString() {
      return this._stackTrace;
   }

   public String getMessage() {
      return this._message;
   }

   private static String stringify(Throwable var0) {
      StringWriter var1 = new StringWriter();
      var0.printStackTrace(new PrintWriter(var1));
      return var1.toString();
   }
}
