package weblogic.wsee.tools.jws.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.JWSProcessor;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.WebServiceInfo;
import weblogic.wsee.util.Verbose;

class CompositeProcessor implements JWSProcessor {
   private static final boolean verbose = Verbose.isVerbose(CompositeProcessor.class);
   private List<JWSProcessor> processors = new ArrayList();

   public void init(ModuleInfo var1) throws WsBuildException {
      JWSProcessor var3;
      for(Iterator var2 = this.processors.iterator(); var2.hasNext(); var3.init(var1)) {
         var3 = (JWSProcessor)var2.next();
         if (verbose) {
            Verbose.log((Object)("Initialzing..." + var3.getClass().getName()));
         }
      }

   }

   void addProcessor(JWSProcessor var1) {
      this.processors.add(var1);
   }

   public void process(WebServiceInfo var1) throws WsBuildException {
      JWSProcessor var3;
      for(Iterator var2 = this.processors.iterator(); var2.hasNext(); var3.process(var1)) {
         var3 = (JWSProcessor)var2.next();
         if (verbose) {
            Verbose.log((Object)("Processing..." + var3.getClass().getName()));
         }
      }

   }

   public void finish() throws WsBuildException {
      JWSProcessor var2;
      for(Iterator var1 = this.processors.iterator(); var1.hasNext(); var2.finish()) {
         var2 = (JWSProcessor)var1.next();
         if (verbose) {
            Verbose.log((Object)("Finishing..." + var2.getClass().getName()));
         }
      }

   }
}
