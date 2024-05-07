package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ORJoinData {
   private boolean useNewParser = true;
   Expr orTerm;
   Vector orVector;
   Map orCmpFieldJoinMap;
   Map orSQLTableGenSymbolMap;
   Set orSQLTableGenTableSet;

   public ORJoinData() {
      this.orVector = new Vector();
      this.orCmpFieldJoinMap = new HashMap();
      this.orSQLTableGenSymbolMap = new HashMap();
      this.orSQLTableGenTableSet = new HashSet();
   }

   public ORJoinData(Expr var1) {
      this.orTerm = var1;
      this.orVector = new Vector();
      this.orCmpFieldJoinMap = new HashMap();
      this.orSQLTableGenSymbolMap = new HashMap();
      this.orSQLTableGenTableSet = new HashSet();
   }

   public Expr getOrTerm() {
      return this.orTerm;
   }

   public Vector getOrVector() {
      return this.orVector;
   }

   public Map getOrCmpFieldJoinMap() {
      return this.orCmpFieldJoinMap;
   }

   public void addOrSQLTableGenInfo(String var1, String var2) {
      this.addOrSQLTableGenSymbolMap(var1, var2);
      this.addOrSQLTableGenTableSet(var2);
   }

   public Map getOrSQLTableGenSymbolMap() {
      return this.orSQLTableGenSymbolMap;
   }

   public void addOrSQLTableGenSymbolMap(String var1, String var2) {
      this.orSQLTableGenSymbolMap.put(var1, var2);
   }

   public Set getOrSQLTableGenTableSet() {
      return this.orSQLTableGenTableSet;
   }

   public void addOrSQLTableGenTableSet(String var1) {
      this.orSQLTableGenTableSet.add(var1);
   }
}
