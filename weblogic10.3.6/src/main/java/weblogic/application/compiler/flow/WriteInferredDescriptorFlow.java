package weblogic.application.compiler.flow;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.compiler.CompilerCtx;
import weblogic.utils.compiler.ToolFailureException;

public class WriteInferredDescriptorFlow extends CompilerFlow {
   public WriteInferredDescriptorFlow(CompilerCtx var1) {
      super(var1);
   }

   public void cleanup() throws ToolFailureException {
   }

   public void compile() throws ToolFailureException {
      if (this.ctx.isWriteInferredDescriptors()) {
         this.writeInferredApplicationDescriptor();
      }

   }

   private void writeInferredApplicationDescriptor() throws ToolFailureException {
      try {
         ApplicationDescriptor var1 = this.ctx.getApplicationDescriptor();
         if (var1 != null) {
            var1.writeInferredApplicationDescriptor(this.ctx.getOutputDir());
         }

      } catch (IOException var2) {
         throw new RuntimeException(var2);
      } catch (XMLStreamException var3) {
         throw new RuntimeException(var3);
      }
   }
}
