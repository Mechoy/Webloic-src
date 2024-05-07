package weblogic.ejb.container.utils.validation;

import java.util.EventObject;

public final class ValidationEvent extends EventObject {
   private static final long serialVersionUID = 8541977237353603168L;
   protected boolean state;
   protected String errKey;
   protected String errText;

   public ValidationEvent(ValidatableWithNotify var1, boolean var2, String var3, String var4) {
      super(var1);
      this.state = var2;
      this.errKey = var3;
      this.errText = var4;
   }

   public boolean isValid() {
      return this.state;
   }

   public String getErrorKey() {
      return this.errKey;
   }

   public String getErrorText() {
      return this.errText;
   }

   public boolean equals(Object var1) {
      ValidationEvent var2 = (ValidationEvent)var1;
      return this.source.equals(var2.source) && this.state == var2.state && this.errKey.equals(var2.errKey);
   }

   public int hashCode() {
      return this.source.hashCode() ^ (this.state ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode()) ^ this.errText.hashCode();
   }

   public String toString() {
      return "ValidationEvent: obj = " + this.source.getClass().getName() + "[" + System.identityHashCode(this.source) + "], key = " + this.errKey + ", msg = " + this.errText + "\n";
   }
}
