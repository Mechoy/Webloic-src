package weblogic.application.compiler.flow;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.compiler.CompilerCtx;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.utils.compiler.ToolFailureException;

public final class WriteDescriptorsFlow extends WriteInferredDescriptorFlow {
   public WriteDescriptorsFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      this.removeLibRefs();
      this.writeDescriptors();
      super.compile();
   }

   public void cleanup() {
   }

   private void removeLibRefs() throws ToolFailureException {
      if (this.ctx.getWLApplicationDD() != null && this.ctx.getWLApplicationDD().getLibraryRefs() != null) {
         WeblogicApplicationBean var1 = this.ctx.getWLApplicationDD();
         LibraryRefBean[] var2 = this.ctx.getWLApplicationDD().getLibraryRefs();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.destroyLibraryRef(var2[var3]);
         }

      }
   }

   private void writeDescriptors() throws ToolFailureException {
      try {
         ApplicationDescriptor var1 = this.ctx.getApplicationDescriptor();
         if (var1 != null) {
            var1.writeDescriptors(this.ctx.getOutputDir());
         }

      } catch (IOException var2) {
         throw new RuntimeException(var2);
      } catch (XMLStreamException var3) {
         throw new RuntimeException(var3);
      }
   }
}
