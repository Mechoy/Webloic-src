package weblogic.tools.ui;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class TableLayoutConstraints implements TableLayoutConstants {
   public int col1;
   public int row1;
   public int col2;
   public int row2;
   public int hAlign;
   public int vAlign;

   public TableLayoutConstraints() {
      this.col1 = this.row1 = this.col2 = this.col2 = 0;
      this.hAlign = this.vAlign = 2;
   }

   public TableLayoutConstraints(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, ", ");
      this.col1 = 0;
      this.row1 = 0;
      this.col2 = 0;
      this.row2 = 0;
      this.hAlign = 2;
      this.vAlign = 2;
      String var3 = null;

      try {
         var3 = var2.nextToken();
         this.col1 = new Integer(var3);
         this.col2 = this.col1;
         var3 = var2.nextToken();
         this.row1 = new Integer(var3);
         this.row2 = this.row1;
         var3 = var2.nextToken();
         this.col2 = new Integer(var3);
         var3 = var2.nextToken();
         this.row2 = new Integer(var3);
      } catch (NoSuchElementException var7) {
      } catch (NumberFormatException var8) {
         try {
            if (var3.equalsIgnoreCase("L")) {
               this.hAlign = 0;
            } else if (var3.equalsIgnoreCase("C")) {
               this.hAlign = 1;
            } else if (var3.equalsIgnoreCase("F")) {
               this.hAlign = 2;
            } else if (var3.equalsIgnoreCase("R")) {
               this.hAlign = 3;
            }

            var3 = var2.nextToken();
            if (var3.equalsIgnoreCase("T")) {
               this.vAlign = 0;
            } else if (var3.equalsIgnoreCase("C")) {
               this.vAlign = 1;
            } else if (var3.equalsIgnoreCase("F")) {
               this.vAlign = 2;
            } else if (var3.equalsIgnoreCase("B")) {
               this.vAlign = 3;
            }
         } catch (NoSuchElementException var6) {
         }
      }

      if (this.row2 < this.row1) {
         this.row2 = this.row1;
      }

      if (this.col2 < this.col1) {
         this.col2 = this.col1;
      }

   }

   public TableLayoutConstraints(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.col1 = var1;
      this.row1 = var2;
      this.col2 = var3;
      this.row2 = var4;
      if (var5 >= 0 && var5 <= 3) {
         this.hAlign = var5;
      } else {
         this.hAlign = 2;
      }

      if (var6 >= 0 && var6 <= 3) {
         this.vAlign = var6;
      } else {
         this.vAlign = 2;
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.row1);
      var1.append(", ");
      var1.append(this.col1);
      var1.append(", ");
      if (this.row1 == this.row2 && this.col1 == this.col2) {
         char[] var2 = new char[]{'L', 'C', 'F', 'R'};
         char[] var3 = new char[]{'T', 'C', 'F', 'B'};
         var1.append(var2[this.hAlign]);
         var1.append(", ");
         var1.append(var3[this.vAlign]);
      } else {
         var1.append(this.row2);
         var1.append(", ");
         var1.append(this.col2);
      }

      return var1.toString();
   }
}
