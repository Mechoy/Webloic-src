package weblogic.work;

public class ContextWrap implements Work {
   private final InheritableThreadContext context;
   private final Runnable action;
   private final Runnable cancelAction;
   private final Runnable overloadAction;

   public ContextWrap(Runnable var1) {
      this((Runnable)var1, (Runnable)null);
   }

   public ContextWrap(Runnable var1, Runnable var2) {
      this(var1, var2, (Runnable)null);
   }

   public ContextWrap(Runnable var1, Runnable var2, Runnable var3) {
      this.action = var1;
      this.overloadAction = var2;
      this.cancelAction = var3;
      this.context = InheritableThreadContext.getContext();
   }

   private ContextWrap(InheritableThreadContext var1, Runnable var2) {
      this.action = var2;
      this.context = var1;
      this.overloadAction = null;
      this.cancelAction = null;
   }

   public final void run() {
      this.context.push();

      try {
         this.action.run();
      } finally {
         this.context.pop();
      }

   }

   public Runnable overloadAction(String var1) {
      return this.overloadAction == null ? null : new ContextWrap(this.context, this.overloadAction);
   }

   public Runnable cancel(String var1) {
      return this.cancelAction == null ? null : new ContextWrap(this.context, this.cancelAction);
   }
}
