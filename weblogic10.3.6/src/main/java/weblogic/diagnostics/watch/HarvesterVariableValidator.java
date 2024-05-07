package weblogic.diagnostics.watch;

import weblogic.diagnostics.query.UnknownVariableException;
import weblogic.diagnostics.query.VariableIndexResolver;

public final class HarvesterVariableValidator implements VariableIndexResolver {
   String watchName = null;
   private int indexCounter = 0;

   HarvesterVariableValidator(String var1) {
      this.watchName = var1;
   }

   public int getVariableIndex(String var1) throws UnknownVariableException {
      HarvesterVariablesParser.parse(var1, this.watchName);
      return this.indexCounter++;
   }
}
