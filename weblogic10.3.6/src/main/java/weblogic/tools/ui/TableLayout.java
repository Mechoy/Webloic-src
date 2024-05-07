package weblogic.tools.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;

public class TableLayout implements LayoutManager2, Serializable, TableLayoutConstants {
   protected static final double[][] defaultSize = new double[][]{new double[0], new double[0]};
   protected double[] columnSpec;
   protected double[] rowSpec;
   protected int[] columnSize;
   protected int[] rowSize;
   protected int[] columnOffset;
   protected int[] rowOffset;
   protected LinkedList list;
   protected boolean dirty;
   protected int oldWidth;
   protected int oldHeight;

   public TableLayout() {
      this(defaultSize);
   }

   public TableLayout(double[][] var1) {
      double[] var2;
      double[] var3;
      if (var1 != null && var1.length == 2) {
         var2 = var1[0];
         var3 = var1[1];
         this.columnSpec = new double[var2.length];
         this.rowSpec = new double[var3.length];
         System.arraycopy(var2, 0, this.columnSpec, 0, this.columnSpec.length);
         System.arraycopy(var3, 0, this.rowSpec, 0, this.rowSpec.length);

         int var4;
         for(var4 = 0; var4 < this.columnSpec.length; ++var4) {
            if (this.columnSpec[var4] < 0.0 && this.columnSpec[var4] != -1.0 && this.columnSpec[var4] != -2.0 && this.columnSpec[var4] != -3.0) {
               this.columnSpec[var4] = 0.0;
            }
         }

         for(var4 = 0; var4 < this.rowSpec.length; ++var4) {
            if (this.rowSpec[var4] < 0.0 && this.rowSpec[var4] != -1.0 && this.rowSpec[var4] != -2.0 && this.rowSpec[var4] != -3.0) {
               this.rowSpec[var4] = 0.0;
            }
         }
      } else {
         var2 = new double[]{-1.0};
         var3 = new double[]{-1.0};
         this.setColumn(var2);
         this.setRow(var3);
      }

      this.list = new LinkedList();
      this.dirty = true;
   }

   public TableLayoutConstraints getConstraints(Component var1) {
      ListIterator var2 = this.list.listIterator(0);

      Entry var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Entry)var2.next();
      } while(var3.component != var1);

      return new TableLayoutConstraints(var3.col1, var3.row1, var3.col2, var3.row2, var3.hAlign, var3.vAlign);
   }

   public void setConstraints(Component var1, TableLayoutConstraints var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Parameter component cannot be null.");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Parameter constraint cannot be null.");
      } else {
         ListIterator var3 = this.list.listIterator(0);

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if (var4.component == var1) {
               var3.set(new Entry(var1, var2));
            }
         }

      }
   }

   public void setColumn(double[] var1) {
      this.columnSpec = new double[var1.length];
      System.arraycopy(var1, 0, this.columnSpec, 0, this.columnSpec.length);

      for(int var2 = 0; var2 < this.columnSpec.length; ++var2) {
         if (this.columnSpec[var2] < 0.0 && this.columnSpec[var2] != -1.0 && this.columnSpec[var2] != -2.0 && this.columnSpec[var2] != -3.0) {
            this.columnSpec[var2] = 0.0;
         }
      }

      this.dirty = true;
   }

   public void setRow(double[] var1) {
      this.rowSpec = new double[var1.length];
      System.arraycopy(var1, 0, this.rowSpec, 0, this.rowSpec.length);

      for(int var2 = 0; var2 < this.rowSpec.length; ++var2) {
         if (this.rowSpec[var2] < 0.0 && this.rowSpec[var2] != -1.0 && this.rowSpec[var2] != -2.0 && this.rowSpec[var2] != -3.0) {
            this.rowSpec[var2] = 0.0;
         }
      }

      this.dirty = true;
   }

   public void setColumn(int var1, double var2) {
      if (var2 < 0.0 && var2 != -1.0 && var2 != -2.0 && var2 != -3.0) {
         var2 = 0.0;
      }

      this.columnSpec[var1] = var2;
      this.dirty = true;
   }

   public void setRow(int var1, double var2) {
      if (var2 < 0.0 && var2 != -1.0 && var2 != -2.0 && var2 != -3.0) {
         var2 = 0.0;
      }

      this.rowSpec[var1] = var2;
      this.dirty = true;
   }

   public double[] getColumn() {
      double[] var1 = new double[this.columnSpec.length];
      System.arraycopy(this.columnSpec, 0, var1, 0, var1.length);
      return var1;
   }

   public double[] getRow() {
      double[] var1 = new double[this.rowSpec.length];
      System.arraycopy(this.rowSpec, 0, var1, 0, var1.length);
      return var1;
   }

   public double getColumn(int var1) {
      return this.columnSpec[var1];
   }

   public double getRow(int var1) {
      return this.rowSpec[var1];
   }

   public int getNumColumn() {
      return this.columnSpec.length;
   }

   public int getNumRow() {
      return this.rowSpec.length;
   }

   public void insertColumn(int var1, double var2) {
      if (var1 >= 0 && var1 <= this.columnSpec.length) {
         if (var2 < 0.0 && var2 != -1.0 && var2 != -2.0 && var2 != -3.0) {
            var2 = 0.0;
         }

         double[] var4 = new double[this.columnSpec.length + 1];
         System.arraycopy(this.columnSpec, 0, var4, 0, var1);
         System.arraycopy(this.columnSpec, var1, var4, var1 + 1, this.columnSpec.length - var1);
         var4[var1] = var2;
         this.columnSpec = var4;
         ListIterator var5 = this.list.listIterator(0);

         while(var5.hasNext()) {
            Entry var6 = (Entry)var5.next();
            if (var6.col1 >= var1) {
               ++var6.col1;
            }

            if (var6.col2 >= var1) {
               ++var6.col2;
            }
         }

         this.dirty = true;
      } else {
         throw new IllegalArgumentException("Parameter i is invalid.  i = " + var1 + ".  Valid range is [0, " + this.columnSpec.length + "].");
      }
   }

   public void insertRow(int var1, double var2) {
      if (var1 >= 0 && var1 <= this.rowSpec.length) {
         if (var2 < 0.0 && var2 != -1.0 && var2 != -2.0 && var2 != -3.0) {
            var2 = 0.0;
         }

         double[] var4 = new double[this.rowSpec.length + 1];
         System.arraycopy(this.rowSpec, 0, var4, 0, var1);
         System.arraycopy(this.rowSpec, var1, var4, var1 + 1, this.rowSpec.length - var1);
         var4[var1] = var2;
         this.rowSpec = var4;
         ListIterator var5 = this.list.listIterator(0);

         while(var5.hasNext()) {
            Entry var6 = (Entry)var5.next();
            if (var6.row1 >= var1) {
               ++var6.row1;
            }

            if (var6.row2 >= var1) {
               ++var6.row2;
            }
         }

         this.dirty = true;
      } else {
         throw new IllegalArgumentException("Parameter i is invalid.  i = " + var1 + ".  Valid range is [0, " + this.rowSpec.length + "].");
      }
   }

   public void deleteColumn(int var1) {
      if (var1 >= 0 && var1 < this.columnSpec.length) {
         double[] var2 = new double[this.columnSpec.length - 1];
         System.arraycopy(this.columnSpec, 0, var2, 0, var1);
         System.arraycopy(this.columnSpec, var1 + 1, var2, var1, this.columnSpec.length - var1 - 1);
         this.columnSpec = var2;
         ListIterator var3 = this.list.listIterator(0);

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if (var4.col1 >= var1) {
               --var4.col1;
            }

            if (var4.col2 >= var1) {
               --var4.col2;
            }
         }

         this.dirty = true;
      } else {
         throw new IllegalArgumentException("Parameter i is invalid.  i = " + var1 + ".  Valid range is [0, " + (this.columnSpec.length - 1) + "].");
      }
   }

   public void deleteRow(int var1) {
      if (var1 >= 0 && var1 < this.rowSpec.length) {
         double[] var2 = new double[this.rowSpec.length - 1];
         System.arraycopy(this.rowSpec, 0, var2, 0, var1);
         System.arraycopy(this.rowSpec, var1 + 1, var2, var1, this.rowSpec.length - var1 - 1);
         this.rowSpec = var2;
         ListIterator var3 = this.list.listIterator(0);

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if (var4.row1 >= var1) {
               --var4.row1;
            }

            if (var4.row2 >= var1) {
               --var4.row2;
            }
         }

         this.dirty = true;
      } else {
         throw new IllegalArgumentException("Parameter i is invalid.  i = " + var1 + ".  Valid range is [0, " + (this.rowSpec.length - 1) + "].");
      }
   }

   public String toString() {
      String var2 = "TableLayout {{";
      int var1;
      if (this.columnSpec.length > 0) {
         for(var1 = 0; var1 < this.columnSpec.length - 1; ++var1) {
            var2 = var2 + this.columnSpec[var1] + ", ";
         }

         var2 = var2 + this.columnSpec[this.columnSpec.length - 1] + "}, {";
      } else {
         var2 = var2 + "}, {";
      }

      if (this.rowSpec.length > 0) {
         for(var1 = 0; var1 < this.rowSpec.length - 1; ++var1) {
            var2 = var2 + this.rowSpec[var1] + ", ";
         }

         var2 = var2 + this.rowSpec[this.rowSpec.length - 1] + "}}";
      } else {
         var2 = var2 + "}}";
      }

      return var2;
   }

   public void drawGrid(Container var1, Graphics var2) {
      Dimension var4 = var1.getSize();
      if (this.dirty || var4.width != this.oldWidth || var4.height != this.oldHeight) {
         this.calculateSize(var1);
      }

      int var5 = 0;

      for(int var6 = 0; var6 < this.rowSize.length; ++var6) {
         int var7 = 0;

         for(int var8 = 0; var8 < this.columnSize.length; ++var8) {
            Color var9 = new Color((int)(Math.random() * 1.6777215E7));
            var2.setColor(var9);
            var2.fillRect(var7, var5, this.columnSize[var8], this.rowSize[var6]);
            var7 += this.columnSize[var8];
         }

         var5 += this.rowSize[var6];
      }

   }

   public boolean hidden() {
      boolean var1 = false;
      ListIterator var2 = this.list.listIterator(0);

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (var3.row1 < 0 || var3.col1 < 0 || var3.row2 > this.rowSpec.length || var3.col2 > this.columnSpec.length) {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   public boolean overlapping() {
      int var1 = this.list.size();
      if (var1 == 0) {
         return false;
      } else {
         boolean var2 = false;
         Entry[] var3 = (Entry[])((Entry[])this.list.toArray(new Entry[var1]));

         for(int var4 = 1; var4 < var1; ++var4) {
            for(int var5 = var4 - 1; var5 >= 0; --var5) {
               if (var3[var5].col1 >= var3[var4].col1 && var3[var5].col1 <= var3[var4].col2 && var3[var5].row1 >= var3[var4].row1 && var3[var5].row1 <= var3[var4].row2 || var3[var5].col2 >= var3[var4].col1 && var3[var5].col2 <= var3[var4].col2 && var3[var5].row2 >= var3[var4].row1 && var3[var5].row2 <= var3[var4].row2) {
                  var2 = true;
                  break;
               }
            }
         }

         return var2;
      }
   }

   protected void calculateSize(Container var1) {
      int var3 = this.columnSpec.length;
      int var4 = this.rowSpec.length;
      this.columnSize = new int[var3];
      this.rowSize = new int[var4];
      Insets var5 = var1.getInsets();
      Dimension var6 = var1.getSize();
      int var7 = var6.width - var5.left - var5.right;
      int var8 = var6.height - var5.top - var5.bottom;
      int var9 = var7;
      int var10 = var8;

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         if (this.columnSpec[var2] >= 1.0 || this.columnSpec[var2] == 0.0) {
            this.columnSize[var2] = (int)(this.columnSpec[var2] + 0.5);
            var9 -= this.columnSize[var2];
         }
      }

      for(var2 = 0; var2 < var4; ++var2) {
         if (this.rowSpec[var2] >= 1.0 || this.rowSpec[var2] == 0.0) {
            this.rowSize[var2] = (int)(this.rowSpec[var2] + 0.5);
            var10 -= this.rowSize[var2];
         }
      }

      int var11;
      ListIterator var12;
      Entry var13;
      Dimension var14;
      int var15;
      for(var2 = 0; var2 < var3; ++var2) {
         if (this.columnSpec[var2] == -2.0 || this.columnSpec[var2] == -3.0) {
            var11 = 0;
            var12 = this.list.listIterator(0);

            while(var12.hasNext()) {
               var13 = (Entry)var12.next();
               if (var13.col1 == var2 && var13.col2 == var2) {
                  var14 = this.columnSpec[var2] == -2.0 ? var13.component.getPreferredSize() : var13.component.getMinimumSize();
                  var15 = var14 == null ? 0 : var14.width;
                  if (var11 < var15) {
                     var11 = var15;
                  }
               }
            }

            this.columnSize[var2] = var11;
            var9 -= var11;
         }
      }

      for(var2 = 0; var2 < var4; ++var2) {
         if (this.rowSpec[var2] == -2.0 || this.rowSpec[var2] == -3.0) {
            var11 = 0;
            var12 = this.list.listIterator(0);

            while(var12.hasNext()) {
               var13 = (Entry)var12.next();
               if (var13.row1 == var2 && var13.row2 == var2) {
                  var14 = this.rowSpec[var2] == -2.0 ? var13.component.getPreferredSize() : var13.component.getMinimumSize();
                  var15 = var14 == null ? 0 : var14.height;
                  if (var11 < var15) {
                     var11 = var15;
                  }
               }
            }

            this.rowSize[var2] = var11;
            var10 -= var11;
         }
      }

      var11 = var9;
      int var17 = var10;
      if (var9 < 0) {
         var11 = 0;
      }

      if (var10 < 0) {
         var17 = 0;
      }

      for(var2 = 0; var2 < var3; ++var2) {
         if (this.columnSpec[var2] > 0.0 && this.columnSpec[var2] < 1.0) {
            this.columnSize[var2] = (int)(this.columnSpec[var2] * (double)var11 + 0.5);
            var9 -= this.columnSize[var2];
         }
      }

      for(var2 = 0; var2 < var4; ++var2) {
         if (this.rowSpec[var2] > 0.0 && this.rowSpec[var2] < 1.0) {
            this.rowSize[var2] = (int)(this.rowSpec[var2] * (double)var17 + 0.5);
            var10 -= this.rowSize[var2];
         }
      }

      if (var9 < 0) {
         var9 = 0;
      }

      if (var10 < 0) {
         var10 = 0;
      }

      int var18 = 0;
      int var19 = 0;

      for(var2 = 0; var2 < var3; ++var2) {
         if (this.columnSpec[var2] == -1.0) {
            ++var18;
         }
      }

      for(var2 = 0; var2 < var4; ++var2) {
         if (this.rowSpec[var2] == -1.0) {
            ++var19;
         }
      }

      var15 = var9;
      int var16 = var10;

      for(var2 = 0; var2 < var3; ++var2) {
         if (this.columnSpec[var2] == -1.0) {
            this.columnSize[var2] = var9 / var18;
            var15 -= this.columnSize[var2];
         }
      }

      for(var2 = 0; var2 < var4; ++var2) {
         if (this.rowSpec[var2] == -1.0) {
            this.rowSize[var2] = var10 / var19;
            var16 -= this.rowSize[var2];
         }
      }

      int[] var10000;
      for(var2 = var3 - 1; var2 >= 0; --var2) {
         if (this.columnSpec[var2] == -1.0) {
            var10000 = this.columnSize;
            var10000[var2] += var15;
            break;
         }
      }

      for(var2 = var4 - 1; var2 >= 0; --var2) {
         if (this.rowSpec[var2] == -1.0) {
            var10000 = this.rowSize;
            var10000[var2] += var16;
            break;
         }
      }

      this.columnOffset = new int[var3 + 1];
      this.columnOffset[0] = var5.left;

      for(var2 = 0; var2 < var3; ++var2) {
         this.columnOffset[var2 + 1] = this.columnOffset[var2] + this.columnSize[var2];
      }

      this.rowOffset = new int[var4 + 1];
      this.rowOffset[0] = var5.top;

      for(var2 = 0; var2 < var4; ++var2) {
         this.rowOffset[var2 + 1] = this.rowOffset[var2] + this.rowSize[var2];
      }

      this.dirty = false;
      this.oldWidth = var7;
      this.oldHeight = var8;
   }

   public void layoutContainer(Container var1) {
      Dimension var6 = var1.getSize();
      if (this.dirty || var6.width != this.oldWidth || var6.height != this.oldHeight) {
         this.calculateSize(var1);
      }

      Component[] var7 = var1.getComponents();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         try {
            ListIterator var9 = this.list.listIterator(0);

            Entry var10;
            for(var10 = null; var9.hasNext(); var10 = null) {
               var10 = (Entry)var9.next();
               if (var10.component == var7[var8]) {
                  break;
               }
            }

            if (var10 == null) {
               break;
            }

            int var2;
            int var3;
            int var4;
            int var5;
            if (!var10.singleCell) {
               var2 = this.columnOffset[var10.col1];
               var3 = this.rowOffset[var10.row1];
               var4 = this.columnOffset[var10.col2 + 1] - this.columnOffset[var10.col1];
               var5 = this.rowOffset[var10.row2 + 1] - this.rowOffset[var10.row1];
            } else {
               int var11 = 0;
               int var12 = 0;
               if (var10.hAlign != 2 || var10.vAlign != 2) {
                  Dimension var13 = var7[var8].getPreferredSize();
                  var11 = var13.width;
                  var12 = var13.height;
               }

               int var16 = this.columnSize[var10.col1];
               int var14 = this.rowSize[var10.row1];
               if (var10.hAlign != 2 && var16 >= var11) {
                  var4 = var11;
               } else {
                  var4 = var16;
               }

               switch (var10.hAlign) {
                  case 0:
                     var2 = this.columnOffset[var10.col1];
                     break;
                  case 1:
                     var2 = this.columnOffset[var10.col1] + (var16 - var4 >> 1);
                     break;
                  case 2:
                     var2 = this.columnOffset[var10.col1];
                     break;
                  case 3:
                     var2 = this.columnOffset[var10.col1 + 1] - var4;
                     break;
                  default:
                     var2 = 0;
               }

               if (var10.vAlign != 2 && var14 >= var12) {
                  var5 = var12;
               } else {
                  var5 = var14;
               }

               switch (var10.vAlign) {
                  case 0:
                     var3 = this.rowOffset[var10.row1];
                     break;
                  case 1:
                     var3 = this.rowOffset[var10.row1] + (var14 - var5 >> 1);
                     break;
                  case 2:
                     var3 = this.rowOffset[var10.row1];
                     break;
                  case 3:
                     var3 = this.rowOffset[var10.row1 + 1] - var5;
                     break;
                  default:
                     var3 = 0;
               }
            }

            var7[var8].setBounds(var2, var3, var4, var5);
         } catch (Exception var15) {
         }
      }

   }

   public Dimension preferredLayoutSize(Container var1) {
      int var3 = 0;
      int var4 = 0;
      double var7 = 1.0;
      double var9 = 1.0;
      int var11 = 0;
      int var12 = 0;

      int var6;
      for(var6 = 0; var6 < this.columnSpec.length; ++var6) {
         if (this.columnSpec[var6] > 0.0 && this.columnSpec[var6] < 1.0) {
            var7 -= this.columnSpec[var6];
         } else if (this.columnSpec[var6] == -1.0) {
            ++var11;
         }
      }

      for(var6 = 0; var6 < this.rowSpec.length; ++var6) {
         if (this.rowSpec[var6] > 0.0 && this.rowSpec[var6] < 1.0) {
            var9 -= this.rowSpec[var6];
         } else if (this.rowSpec[var6] == -1.0) {
            ++var12;
         }
      }

      if (var11 > 1) {
         var7 /= (double)var11;
      }

      if (var12 > 1) {
         var9 /= (double)var12;
      }

      if (var7 < 0.0) {
         var7 = 0.0;
      }

      if (var9 < 0.0) {
         var9 = 0.0;
      }

      int[] var13 = new int[this.columnSpec.length];

      ListIterator var15;
      Entry var16;
      int var18;
      for(var6 = 0; var6 < this.columnSpec.length; ++var6) {
         if (this.columnSpec[var6] == -2.0 || this.columnSpec[var6] == -3.0) {
            int var14 = 0;
            var15 = this.list.listIterator(0);

            while(var15.hasNext()) {
               var16 = (Entry)var15.next();
               if (var16.col1 == var6 && var16.col2 == var6) {
                  Dimension var17 = this.columnSpec[var6] == -2.0 ? var16.component.getPreferredSize() : var16.component.getMinimumSize();
                  var18 = var17 == null ? 0 : var17.width;
                  if (var14 < var18) {
                     var14 = var18;
                  }
               }
            }

            var13[var6] = var14;
         }
      }

      int[] var23 = new int[this.rowSpec.length];

      for(var6 = 0; var6 < this.rowSpec.length; ++var6) {
         if (this.rowSpec[var6] == -2.0 || this.rowSpec[var6] == -3.0) {
            int var24 = 0;
            ListIterator var25 = this.list.listIterator(0);

            while(var25.hasNext()) {
               Entry var27 = (Entry)var25.next();
               if (var27.row1 == var6 && var27.row1 == var6) {
                  Dimension var29 = this.rowSpec[var6] == -2.0 ? var27.component.getPreferredSize() : var27.component.getMinimumSize();
                  int var19 = var29 == null ? 0 : var29.height;
                  if (var24 < var19) {
                     var24 = var19;
                  }
               }
            }

            var23[var6] += var24;
         }
      }

      var15 = this.list.listIterator(0);

      while(true) {
         int var28;
         do {
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var15.hasNext()) {
                              int var26 = var3;

                              for(var6 = 0; var6 < this.columnSpec.length; ++var6) {
                                 if (this.columnSpec[var6] >= 1.0) {
                                    var26 += (int)(this.columnSpec[var6] + 0.5);
                                 } else if (this.columnSpec[var6] == -2.0 || this.columnSpec[var6] == -3.0) {
                                    var26 += var13[var6];
                                 }
                              }

                              var28 = var4;

                              for(var6 = 0; var6 < this.rowSpec.length; ++var6) {
                                 if (this.rowSpec[var6] >= 1.0) {
                                    var28 += (int)(this.rowSpec[var6] + 0.5);
                                 } else if (this.rowSpec[var6] == -2.0 || this.rowSpec[var6] == -3.0) {
                                    var28 += var23[var6];
                                 }
                              }

                              Insets var31 = var1.getInsets();
                              var26 += var31.left + var31.right;
                              var28 += var31.top + var31.bottom;
                              ppp("DIMENSION OF THE PANEL:" + var26 + " " + var28);
                              return new Dimension(var26, var28);
                           }

                           var16 = (Entry)var15.next();
                        } while(var16.col1 < 0);
                     } while(var16.col1 >= this.columnSpec.length);
                  } while(var16.col2 >= this.columnSpec.length);
               } while(var16.row1 < 0);
            } while(var16.row1 >= this.rowSpec.length);
         } while(var16.row2 >= this.rowSpec.length);

         Dimension var2 = var16.component.getPreferredSize();
         var28 = var2.width;
         var18 = var2.height;

         for(var6 = var16.col1; var6 <= var16.col2; ++var6) {
            if (this.columnSpec[var6] >= 1.0) {
               var28 = (int)((double)var28 - this.columnSpec[var6]);
            } else if (this.columnSpec[var6] == -2.0 || this.columnSpec[var6] == -3.0) {
               var28 -= var13[var6];
            }
         }

         for(var6 = var16.row1; var6 <= var16.row2; ++var6) {
            if (this.rowSpec[var6] >= 1.0) {
               var18 = (int)((double)var18 - this.rowSpec[var6]);
            } else if (this.rowSpec[var6] == -2.0 || this.rowSpec[var6] == -3.0) {
               var18 -= var23[var6];
            }
         }

         double var30 = 0.0;

         for(var6 = var16.col1; var6 <= var16.col2; ++var6) {
            if (this.columnSpec[var6] > 0.0 && this.columnSpec[var6] < 1.0) {
               var30 += this.columnSpec[var6];
            } else if (this.columnSpec[var6] == -1.0 && var7 != 0.0) {
               var30 += var7;
            }
         }

         int var5;
         if (var30 == 0.0) {
            var5 = 0;
         } else {
            var5 = (int)((double)var28 / var30 + 0.5);
         }

         if (var3 < var5) {
            var3 = var5;
         }

         double var21 = 0.0;

         for(var6 = var16.row1; var6 <= var16.row2; ++var6) {
            if (this.rowSpec[var6] > 0.0 && this.rowSpec[var6] < 1.0) {
               var21 += this.rowSpec[var6];
            } else if (this.rowSpec[var6] == -1.0 && var9 != 0.0) {
               var21 += var9;
            }
         }

         if (var21 == 0.0) {
            var5 = 0;
         } else {
            var5 = (int)((double)var18 / var21 + 0.5);
         }

         if (var4 < var5) {
            var4 = var5;
         }
      }
   }

   public Dimension minimumLayoutSize(Container var1) {
      int var3 = 0;
      int var4 = 0;
      boolean var5 = false;
      boolean var6 = false;
      double var9 = 1.0;
      double var11 = 1.0;
      int var13 = 0;
      int var14 = 0;

      int var8;
      for(var8 = 0; var8 < this.columnSpec.length; ++var8) {
         if (this.columnSpec[var8] > 0.0 && this.columnSpec[var8] < 1.0) {
            var9 -= this.columnSpec[var8];
         } else if (this.columnSpec[var8] == -1.0) {
            ++var13;
         }
      }

      for(var8 = 0; var8 < this.rowSpec.length; ++var8) {
         if (this.rowSpec[var8] > 0.0 && this.rowSpec[var8] < 1.0) {
            var11 -= this.rowSpec[var8];
         } else if (this.rowSpec[var8] == -1.0) {
            ++var14;
         }
      }

      if (var13 > 1) {
         var9 /= (double)var13;
      }

      if (var14 > 1) {
         var11 /= (double)var14;
      }

      if (var9 < 0.0) {
         var9 = 0.0;
      }

      if (var11 < 0.0) {
         var11 = 0.0;
      }

      ListIterator var15 = this.list.listIterator(0);

      while(true) {
         Entry var16;
         int var17;
         int var18;
         do {
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var15.hasNext()) {
                              int var23 = var3;

                              for(var8 = 0; var8 < this.columnSpec.length; ++var8) {
                                 if (this.columnSpec[var8] >= 1.0) {
                                    var23 += (int)(this.columnSpec[var8] + 0.5);
                                 } else if (this.columnSpec[var8] == -2.0 || this.columnSpec[var8] == -3.0) {
                                    var17 = 0;
                                    var15 = this.list.listIterator(0);

                                    while(var15.hasNext()) {
                                       Entry var24 = (Entry)var15.next();
                                       if (var24.col1 == var8 && var24.col2 == var8) {
                                          Dimension var26 = this.columnSpec[var8] == -2.0 ? var24.component.getPreferredSize() : var24.component.getMinimumSize();
                                          int var20 = var26 == null ? 0 : var26.width;
                                          if (var17 < var20) {
                                             var17 = var20;
                                          }
                                       }
                                    }

                                    var23 += var17;
                                 }
                              }

                              var17 = var4;

                              for(var8 = 0; var8 < this.rowSpec.length; ++var8) {
                                 if (this.rowSpec[var8] >= 1.0) {
                                    var17 += (int)(this.rowSpec[var8] + 0.5);
                                 } else if (this.rowSpec[var8] == -2.0 || this.rowSpec[var8] == -3.0) {
                                    var18 = 0;
                                    var15 = this.list.listIterator(0);

                                    while(var15.hasNext()) {
                                       Entry var27 = (Entry)var15.next();
                                       if (var27.row1 == var8 && var27.row1 == var8) {
                                          Dimension var28 = this.rowSpec[var8] == -2.0 ? var27.component.getPreferredSize() : var27.component.getMinimumSize();
                                          int var29 = var28 == null ? 0 : var28.height;
                                          if (var18 < var29) {
                                             var18 = var29;
                                          }
                                       }
                                    }

                                    var17 += var18;
                                 }
                              }

                              Insets var25 = var1.getInsets();
                              var23 += var25.left + var25.right;
                              var17 += var25.top + var25.bottom;
                              return new Dimension(var23, var17);
                           }

                           var16 = (Entry)var15.next();
                        } while(var16.col1 < 0);
                     } while(var16.col1 >= this.columnSpec.length);
                  } while(var16.col2 >= this.columnSpec.length);
               } while(var16.row1 < 0);
            } while(var16.row1 >= this.rowSpec.length);
         } while(var16.row2 >= this.rowSpec.length);

         Dimension var2 = var16.component.getMinimumSize();
         var17 = var2.width;
         var18 = var2.height;

         for(var8 = var16.col1; var8 <= var16.col2; ++var8) {
            if (this.columnSpec[var8] >= 1.0) {
               var17 = (int)((double)var17 - this.columnSpec[var8]);
            }
         }

         for(var8 = var16.row1; var8 <= var16.row2; ++var8) {
            if (this.rowSpec[var8] >= 1.0) {
               var18 = (int)((double)var18 - this.rowSpec[var8]);
            }
         }

         double var19 = 0.0;

         for(var8 = var16.col1; var8 <= var16.col2; ++var8) {
            if (this.columnSpec[var8] > 0.0 && this.columnSpec[var8] < 1.0) {
               var19 += this.columnSpec[var8];
            } else if (this.columnSpec[var8] == -1.0 && var9 != 0.0) {
               var19 += var9;
            }
         }

         int var7;
         if (var19 == 0.0) {
            var7 = 0;
         } else {
            var7 = (int)((double)var17 / var19 + 0.5);
         }

         if (var3 < var7) {
            var3 = var7;
         }

         double var21 = 0.0;

         for(var8 = var16.row1; var8 <= var16.row2; ++var8) {
            if (this.rowSpec[var8] > 0.0 && this.rowSpec[var8] < 1.0) {
               var21 += this.rowSpec[var8];
            } else if (this.rowSpec[var8] == -1.0 && var11 != 0.0) {
               var21 += var11;
            }
         }

         if (var21 == 0.0) {
            var7 = 0;
         } else {
            var7 = (int)((double)var18 / var21 + 0.5);
         }

         if (var4 < var7) {
            var4 = var7;
         }
      }
   }

   public void addLayoutComponent(String var1, Component var2) {
      this.addLayoutComponent((Component)var2, (Object)var1);
   }

   public void addLayoutComponent(Component var1, Object var2) {
      if (var2 instanceof String) {
         ppp("ADDING " + var1.getClass() + " CONSTRAINT:" + var2);
         TableLayoutConstraints var3 = new TableLayoutConstraints((String)var2);
         this.list.add(new Entry(var1, (TableLayoutConstraints)var3));
      } else {
         if (!(var2 instanceof TableLayoutConstraints)) {
            if (var2 == null) {
               throw new IllegalArgumentException("No constraint for the component");
            }

            throw new IllegalArgumentException("Cannot accept a constraint of class " + var2.getClass());
         }

         this.list.add(new Entry(var1, (TableLayoutConstraints)var2));
      }

   }

   public void removeLayoutComponent(Component var1) {
      this.list.remove(var1);
   }

   public Dimension maximumLayoutSize(Container var1) {
      return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
   }

   public float getLayoutAlignmentX(Container var1) {
      return 0.5F;
   }

   public float getLayoutAlignmentY(Container var1) {
      return 0.5F;
   }

   public void invalidateLayout(Container var1) {
      this.dirty = true;
   }

   private static void ppp(String var0) {
      System.out.println("[TableLayout] " + var0);
   }

   protected class Entry extends TableLayoutConstraints {
      protected Component component;
      protected boolean singleCell;

      public Entry(Component var2, TableLayoutConstraints var3) {
         super(var3.col1, var3.row1, var3.col2, var3.row2, var3.hAlign, var3.vAlign);
         this.singleCell = this.row1 == this.row2 && this.col1 == this.col2;
         this.component = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = false;
         if (var1 instanceof Component) {
            Component var3 = (Component)var1;
            var2 = this.component == var3;
         }

         return var2;
      }
   }
}
