package weblogic.tools.ui;

import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JComboBox;

public class SortedComboBox extends JComboBox implements Comparator {
   public SortedComboBox() {
   }

   public SortedComboBox(Object[] var1) {
      this.sort(var1);

      for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            super.addItem(var1[var2]);
         }
      }

   }

   private void sort(Object[] var1) {
      Arrays.sort(var1, this);
   }

   public int compare(Object var1, Object var2) {
      String var3 = "";
      if (var1 != null) {
         var3 = var1.toString();
      }

      String var4 = "";
      if (var2 != null) {
         var4 = var2.toString();
      }

      return var3.compareTo(var4);
   }

   public boolean equals(Object var1) {
      return var1 == this;
   }
}
