package weblogic.jdbc.rowset;

import java.util.Comparator;

public interface SortedRowSet {
   void setSorter(Comparator var1);

   Comparator getSorter();
}
