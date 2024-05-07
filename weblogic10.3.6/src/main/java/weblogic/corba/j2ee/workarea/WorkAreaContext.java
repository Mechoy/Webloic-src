package weblogic.corba.j2ee.workarea;

import weblogic.iiop.EndPoint;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.ServiceContext;
import weblogic.workarea.WorkContextInput;
import weblogic.workarea.WorkContextOutput;

public class WorkAreaContext extends ServiceContext {
   private final ContextInputImpl inputStream;

   public WorkAreaContext(WorkContextOutput var1) {
      super(1111834891);
      this.inputStream = null;
      ContextOutputImpl var2 = (ContextOutputImpl)var1;
      this.context_data = var2.getDelegate().getBuffer();
      var2.getDelegate().close();
   }

   public static WorkContextOutput createOutputStream(EndPoint var0) {
      return new ContextOutputImpl(var0);
   }

   public WorkAreaContext(IIOPInputStream var1) {
      super(1111834891);
      this.inputStream = new ContextInputImpl(new IIOPInputStream(var1));
   }

   public WorkContextInput getInputStream() {
      if (this.inputStream == null) {
         throw new IllegalStateException("WorkAreaContext used in wrong context.");
      } else {
         return this.inputStream;
      }
   }

   public String toString() {
      return "WorkAreaContext";
   }
}
