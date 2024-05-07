package weblogic.management.scripting;

import java.io.Serializable;
import java.util.Date;
import javax.management.MBeanException;
import weblogic.management.scripting.utils.ErrorInformation;
import weblogic.management.scripting.utils.WLSTUtil;

public class ExceptionHandler implements Serializable {
   WLScriptContext ctx = null;
   private static final String ERR_MSG = "Error occured while performing ";
   private static final String USE_DUMPSTACK = " \nUse dumpStack() to view the full stacktrace";

   ExceptionHandler(WLScriptContext ctx) {
      this.ctx = ctx;
   }

   void handleException(ErrorInformation ei) throws ScriptException {
      String msg = ei.getMessage();
      this.ctx.stackTrace = ei.getError();
      Date dte = new Date();
      this.ctx.timeAtError = dte.toString();
      if (ei.getError() != null && ei.getError().getMessage() != null) {
         msg = msg + " : " + ei.getError().getMessage();
      }

      String s = this.getRealMessage(ei.getError());
      if (s != null) {
         msg = msg + " " + s;
      }

      if (this.ctx.debug || WLSTUtil.scriptMode && !this.ctx.hideDumpStack) {
         String cmdType = this.ctx.commandType;
         this.ctx.dumpStack();
         this.ctx.commandType = cmdType;
      }

      this.ctx.theErrorMessage = msg;
      if (ei.getError() == null) {
         if (this.ctx.redirecting) {
            this.ctx.println("Error occured while performing " + this.ctx.commandType + " : " + msg + this.ctx.commandType);
         }

         throw new ScriptException("Error occured while performing " + this.ctx.commandType + " : " + msg, this.ctx.commandType);
      } else {
         if (this.ctx.redirecting) {
            this.ctx.println("Error occured while performing " + this.ctx.commandType + " : " + msg + " \nUse dumpStack() to view the full stacktrace" + " : ");
            this.ctx.println(ei.getError() + this.ctx.commandType);
         }

         throw new ScriptException("Error occured while performing " + this.ctx.commandType + " : " + msg + " \nUse dumpStack() to view the full stacktrace", ei.getError(), this.ctx.commandType);
      }
   }

   public void handleException(ErrorInformation ei, String cmdName) throws ScriptException {
      String msg = ei.getMessage();
      if (ei.getError() != null && ei.getError().getMessage() != null) {
         msg = msg + ei.getError().getMessage();
      }

      this.ctx.theErrorMessage = msg;
      if (this.ctx.debug || WLSTUtil.scriptMode && !this.ctx.hideDumpStack) {
         this.ctx.dumpStack();
      }

      if (ei.getError() == null) {
         if (this.ctx.redirecting) {
            this.ctx.println("Error occured while performing " + cmdName + " : " + msg + cmdName);
         }

         throw new ScriptException("Error occured while performing " + cmdName + " : " + msg, cmdName);
      } else {
         if (this.ctx.redirecting) {
            this.ctx.println("Error occured while performing " + cmdName + " : " + msg + " \nUse dumpStack() to view the full stacktrace" + " : ");
            this.ctx.println(ei.getError() + cmdName);
         }

         throw new ScriptException("Error occured while performing " + cmdName + " : " + msg + " \nUse dumpStack() to view the full stacktrace", ei.getError(), cmdName);
      }
   }

   private String getRealMessage(Throwable th) {
      if (th instanceof MBeanException) {
         MBeanException ex = (MBeanException)th;
         if (ex.getCause() != null && ex.getCause().getMessage() != null) {
            return ex.getCause().getMessage();
         }
      }

      return null;
   }
}
