package weblogic.wsee.callback.controls;

public class ControlCallbackData {
   private static final long serialVersionUID = 1L;
   public static final String CONTROL_CALLBACK_DATA_PROP = "weblogic.wsee.callback.controls.ControlCallbackData";
   private Object controlHandle = null;
   private Object[] args = null;
   private String serviceUri = null;
   private Object eventRef = null;

   public ControlCallbackData(Object var1, Object var2, Object[] var3, String var4) {
      this.controlHandle = var1;
      this.eventRef = var2;
      this.args = var3;
      this.serviceUri = var4;
   }

   public Object getControlHandle() {
      return this.controlHandle;
   }

   public Object[] getArgs() {
      return this.args;
   }

   public Object getEventRef() {
      return this.eventRef;
   }

   public String getServiceUri() {
      return this.serviceUri;
   }
}
