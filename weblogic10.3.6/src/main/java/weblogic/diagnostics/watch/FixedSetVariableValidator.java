package weblogic.diagnostics.watch;

import java.util.HashSet;
import java.util.Set;
import weblogic.diagnostics.accessor.ColumnInfo;
import weblogic.diagnostics.archive.ArchiveConstants;
import weblogic.diagnostics.query.UnknownVariableException;
import weblogic.diagnostics.query.VariableIndexResolver;

final class FixedSetVariableValidator implements VariableIndexResolver {
   private Set variableSet = new HashSet();
   private int indexCounter = 0;

   public FixedSetVariableValidator(int var1) {
      ColumnInfo[] var2 = ArchiveConstants.getColumns(var1);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.variableSet.add(var2[var3].getColumnName());
      }

   }

   public int getVariableIndex(String var1) throws UnknownVariableException {
      if (!this.variableSet.contains(var1)) {
         throw new UnknownVariableException(var1);
      } else {
         return this.indexCounter++;
      }
   }
}
