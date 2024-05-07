package weblogic.jdbc.rowset;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.Joinable;
import weblogic.xml.stream.XMLInputStream;

public class JoinRowSetImpl extends CachedRowSetImpl implements JoinRowSet {
   private static final long serialVersionUID = 7370178015207304595L;
   private ArrayList rowSets = new ArrayList();
   private String where = null;
   private int joinType = 1;

   public void populate(ResultSetMetaData var1) throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public void populate(ResultSet var1) throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public void execute() throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public void execute(Connection var1) throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public String executeAndGuessTableName() throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public boolean executeAndGuessTableNameAndPrimaryKeys() throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public void loadXML(XMLInputStream var1) throws IOException, SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   private void validate(CachedRow var1, int[] var2, CachedRow var3, int[] var4) throws SQLException {
      if (var2.length != var4.length) {
         throw new SQLException("The number of match columns in two Joinables are not the same.");
      } else {
         for(int var5 = 0; var5 < var2.length; ++var5) {
            this.isJoinable(var1.getColumn(var2[var5]).getClass(), var3.getColumn(var4[var5]).getClass());
         }

      }
   }

   public void addRowSet(Joinable var1) throws SQLException {
      CachedRowSetImpl var2 = null;
      if (var1 instanceof CachedRowSetImpl) {
         var2 = (CachedRowSetImpl)var1;
      } else {
         if (!(var1 instanceof RowSet)) {
            throw new SQLException(var1 + " can not be added into JoinRowSet.");
         }

         var2 = new CachedRowSetImpl();
         var2.populate((ResultSet)((RowSet)var1));
         var2.setMatchColumn(var1.getMatchColumnIndexes());
      }

      if (this.rowSets.size() == 0) {
         super.populate(var2, 1);
         this.setMatchColumn(var2.getMatchColumnIndexes());
      } else {
         CachedRowSetMetaData var3 = this.metaData;
         CachedRowSetMetaData var4 = (CachedRowSetMetaData)var2.getMetaData();
         int[] var5 = var3.getMatchColumns();
         int[] var6 = var4.getMatchColumns();
         if (this.rows.size() != 0 && var2.rows.size() != 0) {
            this.validate((CachedRow)this.rows.get(0), var5, (CachedRow)var2.rows.get(0), var6);
         }

         ArrayList var7 = new ArrayList(this.rows);
         ArrayList var8 = new ArrayList(var2.rows);
         ArrayList var9 = null;
         if (this.joinType == 0) {
            var9 = this.crossJoin(var7, var8);
         } else {
            Collections.sort(var7, new JoinSorter(var5, var5));
            Collections.sort(var8, new JoinSorter(var6, var6));
            if (this.joinType == 1) {
               var9 = this.innerJoin(var7, var8, new JoinSorter(var5, var6));
            } else if (this.joinType == 2) {
               var9 = this.leftJoin(var7, var8, new JoinSorter(var5, var6));
            } else if (this.joinType == 3) {
               var9 = this.rightJoin(var7, var8, new JoinSorter(var5, var6));
            } else if (this.joinType == 4) {
               var9 = this.fullJoin(var7, var8, new JoinSorter(var5, var6));
            }
         }

         int var10 = this.metaData.getColumnCount();
         this.metaData.addColumns(var2.getMetaData());
         ArrayList var11 = new ArrayList();

         int var12;
         for(var12 = 0; var12 < var9.size(); ++var12) {
            MatchInfo var13 = (MatchInfo)var9.get(var12);
            CachedRow var14 = var13.getLeft();
            CachedRow var15 = var13.getRight();
            CachedRow var16 = new CachedRow(this.metaData);
            var16.copyFrom(var14);
            var16.copyFrom(var10, var15);
            var11.add(var16);
         }

         this.allrows = var11;
         this.filter();

         for(var12 = 0; var12 < var5.length; ++var12) {
            if (this.where != null) {
               this.where = this.where + " AND ";
            }

            this.where = var3.getQualifiedColumnName(var5[var12]) + "=" + var4.getQualifiedColumnName(var6[var12]);
         }
      }

      this.rowSets.add(var1);
   }

   public void addRowSet(RowSet var1, int var2) throws SQLException {
      CachedRowSetImpl var3 = null;
      if (var1 instanceof CachedRowSetImpl) {
         var3 = (CachedRowSetImpl)var1;
      } else {
         if (!(var1 instanceof RowSet)) {
            throw new SQLException(var1 + " can not be added into JoinRowSet.");
         }

         var3 = new CachedRowSetImpl();
         var3.populate((ResultSet)var1);
      }

      var3.setMatchColumn(new int[]{var2});
      this.addRowSet(var3);
   }

   public void addRowSet(RowSet var1, String var2) throws SQLException {
      CachedRowSetImpl var3 = null;
      if (var1 instanceof CachedRowSetImpl) {
         var3 = (CachedRowSetImpl)var1;
      } else {
         if (!(var1 instanceof RowSet)) {
            throw new SQLException(var1 + " can not be added into JoinRowSet.");
         }

         var3 = new CachedRowSetImpl();
         var3.populate((ResultSet)var1);
      }

      var3.setMatchColumn(new String[]{var2});
      this.addRowSet(var3);
   }

   public void addRowSet(RowSet[] var1, int[] var2) throws SQLException {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.addRowSet(var1[var3], var2[var3]);
      }

   }

   public void addRowSet(RowSet[] var1, String[] var2) throws SQLException {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.addRowSet(var1[var3], var2[var3]);
      }

   }

   public Collection getRowSets() throws SQLException {
      return this.rowSets;
   }

   public String[] getRowSetNames() throws SQLException {
      String[] var1 = new String[this.rowSets.size()];
      Iterator var2 = this.rowSets.iterator();

      for(int var3 = 0; var2.hasNext(); var1[var3++] = ((CachedRowSetImpl)var2.next()).getTableName()) {
      }

      return var1;
   }

   public CachedRowSet toCachedRowSet() throws SQLException {
      CachedRowSetImpl var1 = new CachedRowSetImpl();
      var1.populate((ResultSet)this);
      return var1;
   }

   public boolean supportsCrossJoin() {
      return true;
   }

   public boolean supportsInnerJoin() {
      return true;
   }

   public boolean supportsLeftOuterJoin() {
      return true;
   }

   public boolean supportsRightOuterJoin() {
      return true;
   }

   public boolean supportsFullJoin() {
      return true;
   }

   public int getJoinType() {
      return this.joinType;
   }

   public void setJoinType(int var1) throws SQLException {
      this.joinType = var1;
   }

   public String getWhereClause() throws SQLException {
      return this.where;
   }

   ArrayList crossJoin(ArrayList var1, ArrayList var2) {
      ArrayList var3 = new ArrayList();
      boolean var4 = false;

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         for(int var6 = 0; var6 < var2.size(); ++var6) {
            var3.add(new MatchInfo((CachedRow)var1.get(var5), (CachedRow)var2.get(var6)));
         }
      }

      return var3;
   }

   ArrayList innerJoin(ArrayList var1, ArrayList var2, JoinSorter var3) {
      ArrayList var4 = new ArrayList();
      if (var1 != null && var2 != null && var1.size() > 0 && var2.size() > 0) {
         int var5 = 0;
         int var7 = 0;
         ArrayList var6;
         ArrayList var8;
         if (var3.compare(var1.get(var1.size() - 1), var2.get(var2.size() - 1)) < 0) {
            var6 = var1;
            var8 = var2;
            var3.exchange();
         } else {
            var6 = var2;
            var8 = var1;
         }

         while(var5 < var6.size() && var7 < var8.size()) {
            int var9;
            for(var9 = var3.compare(var8.get(var7), var6.get(var5)); var9 < 0; var9 = var3.compare(var8.get(var7), var6.get(var5))) {
               ++var7;
            }

            if (var9 != 0) {
               ++var5;
            } else {
               ArrayList var10 = new ArrayList();
               ArrayList var11 = new ArrayList();

               do {
                  var10.add(var8.get(var7));
                  ++var7;
               } while(var7 < var8.size() && var3.compare(var8.get(var7), var6.get(var5)) == 0);

               do {
                  var11.add(var6.get(var5));
                  ++var5;
               } while(var5 < var6.size() && var3.compare(var8.get(var7 - 1), var6.get(var5)) == 0);

               if (var1 == var8) {
                  var4.addAll(this.crossJoin(var10, var11));
               } else {
                  var4.addAll(this.crossJoin(var11, var10));
               }
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   ArrayList leftJoin(ArrayList var1, ArrayList var2, JoinSorter var3) {
      ArrayList var4 = new ArrayList();
      if (var1 != null && var1.size() > 0) {
         int var5;
         if (var2 != null && var2.size() > 0) {
            var5 = 0;
            int var7 = 0;
            ArrayList var6;
            ArrayList var8;
            if (var3.compare(var1.get(var1.size() - 1), var2.get(var2.size() - 1)) < 0) {
               var6 = var1;
               var8 = var2;
               var3.exchange();
            } else {
               var6 = var2;
               var8 = var1;
            }

            while(var5 < var6.size() && var7 < var8.size()) {
               int var9;
               for(var9 = var3.compare(var8.get(var7), var6.get(var5)); var9 < 0; var9 = var3.compare(var8.get(var7), var6.get(var5))) {
                  if (var8 == var1) {
                     var4.add(new MatchInfo((CachedRow)var8.get(var7), (CachedRow)null));
                  }

                  ++var7;
               }

               if (var9 != 0) {
                  if (var6 == var1) {
                     var4.add(new MatchInfo((CachedRow)var6.get(var5), (CachedRow)null));
                  }

                  ++var5;
               } else {
                  ArrayList var10 = new ArrayList();
                  ArrayList var11 = new ArrayList();

                  do {
                     var10.add(var8.get(var7));
                     ++var7;
                  } while(var7 < var8.size() && var3.compare(var8.get(var7), var6.get(var5)) == 0);

                  do {
                     var11.add(var6.get(var5));
                     ++var5;
                  } while(var5 < var6.size() && var3.compare(var8.get(var7 - 1), var6.get(var5)) == 0);

                  if (var1 == var8) {
                     var4.addAll(this.crossJoin(var10, var11));
                  } else {
                     var4.addAll(this.crossJoin(var11, var10));
                  }
               }
            }

            if (var6 == var1) {
               while(var5 < var6.size()) {
                  var4.add(new MatchInfo((CachedRow)var6.get(var5++), (CachedRow)null));
               }
            } else {
               while(var7 < var8.size()) {
                  var4.add(new MatchInfo((CachedRow)var8.get(var7++), (CachedRow)null));
               }
            }

            return var4;
         } else {
            for(var5 = 0; var5 < var1.size(); ++var5) {
               var4.add(new MatchInfo((CachedRow)var1.get(var5), (CachedRow)null));
            }

            return var4;
         }
      } else {
         return var4;
      }
   }

   ArrayList rightJoin(ArrayList var1, ArrayList var2, JoinSorter var3) {
      ArrayList var4 = new ArrayList();
      if (var2 != null && var2.size() > 0) {
         int var5;
         if (var1 != null && var1.size() > 0) {
            var5 = 0;
            int var7 = 0;
            ArrayList var6;
            ArrayList var8;
            if (var3.compare(var1.get(var1.size() - 1), var2.get(var2.size() - 1)) < 0) {
               var6 = var1;
               var8 = var2;
               var3.exchange();
            } else {
               var6 = var2;
               var8 = var1;
            }

            while(var5 < var6.size() && var7 < var8.size()) {
               int var9;
               for(var9 = var3.compare(var8.get(var7), var6.get(var5)); var9 < 0; var9 = var3.compare(var8.get(var7), var6.get(var5))) {
                  if (var8 == var2) {
                     var4.add(new MatchInfo((CachedRow)null, (CachedRow)var8.get(var7)));
                  }

                  ++var7;
               }

               if (var9 != 0) {
                  if (var6 == var2) {
                     var4.add(new MatchInfo((CachedRow)null, (CachedRow)var6.get(var5)));
                  }

                  ++var5;
               } else {
                  ArrayList var10 = new ArrayList();
                  ArrayList var11 = new ArrayList();

                  do {
                     var10.add(var8.get(var7));
                     ++var7;
                  } while(var7 < var8.size() && var3.compare(var8.get(var7), var6.get(var5)) == 0);

                  do {
                     var11.add(var6.get(var5));
                     ++var5;
                  } while(var5 < var6.size() && var3.compare(var8.get(var7 - 1), var6.get(var5)) == 0);

                  if (var1 == var8) {
                     var4.addAll(this.crossJoin(var10, var11));
                  } else {
                     var4.addAll(this.crossJoin(var11, var10));
                  }
               }
            }

            if (var6 == var2) {
               while(var5 < var6.size()) {
                  var4.add(new MatchInfo((CachedRow)null, (CachedRow)var6.get(var5++)));
               }
            } else {
               while(var7 < var8.size()) {
                  var4.add(new MatchInfo((CachedRow)null, (CachedRow)var8.get(var7++)));
               }
            }

            return var4;
         } else {
            for(var5 = 0; var5 < var2.size(); ++var5) {
               var4.add(new MatchInfo((CachedRow)null, (CachedRow)var2.get(var5)));
            }

            return var4;
         }
      } else {
         return var4;
      }
   }

   ArrayList fullJoin(ArrayList var1, ArrayList var2, JoinSorter var3) {
      ArrayList var4 = new ArrayList();
      int var5;
      if (var2 != null && var2.size() > 0) {
         if (var1 != null && var1.size() > 0) {
            var5 = 0;
            int var7 = 0;
            ArrayList var6;
            ArrayList var8;
            if (var3.compare(var1.get(var1.size() - 1), var2.get(var2.size() - 1)) < 0) {
               var6 = var1;
               var8 = var2;
               var3.exchange();
            } else {
               var6 = var2;
               var8 = var1;
            }

            while(var5 < var6.size() && var7 < var8.size()) {
               int var9;
               for(var9 = var3.compare(var8.get(var7), var6.get(var5)); var9 < 0; var9 = var3.compare(var8.get(var7), var6.get(var5))) {
                  if (var8 == var1) {
                     var4.add(new MatchInfo((CachedRow)var8.get(var7), (CachedRow)null));
                  } else {
                     var4.add(new MatchInfo((CachedRow)null, (CachedRow)var8.get(var7)));
                  }

                  ++var7;
               }

               if (var9 != 0) {
                  if (var6 == var1) {
                     var4.add(new MatchInfo((CachedRow)var6.get(var5), (CachedRow)null));
                  } else {
                     var4.add(new MatchInfo((CachedRow)null, (CachedRow)var6.get(var5)));
                  }

                  ++var5;
               } else {
                  ArrayList var10 = new ArrayList();
                  ArrayList var11 = new ArrayList();

                  do {
                     var10.add(var8.get(var7));
                     ++var7;
                  } while(var7 < var8.size() && var3.compare(var8.get(var7), var6.get(var5)) == 0);

                  do {
                     var11.add(var6.get(var5));
                     ++var5;
                  } while(var5 < var6.size() && var3.compare(var8.get(var7 - 1), var6.get(var5)) == 0);

                  if (var1 == var8) {
                     var4.addAll(this.crossJoin(var10, var11));
                  } else {
                     var4.addAll(this.crossJoin(var11, var10));
                  }
               }
            }

            if (var6 == var1) {
               while(var5 < var6.size()) {
                  var4.add(new MatchInfo((CachedRow)var6.get(var5++), (CachedRow)null));
               }

               while(var7 < var8.size()) {
                  var4.add(new MatchInfo((CachedRow)null, (CachedRow)var8.get(var7++)));
               }
            } else {
               while(var5 < var6.size()) {
                  var4.add(new MatchInfo((CachedRow)null, (CachedRow)var6.get(var5++)));
               }

               while(var7 < var8.size()) {
                  var4.add(new MatchInfo((CachedRow)var8.get(var7++), (CachedRow)null));
               }
            }

            return var4;
         } else {
            for(var5 = 0; var5 < var2.size(); ++var5) {
               var4.add(new MatchInfo((CachedRow)null, (CachedRow)var2.get(var5)));
            }

            return var4;
         }
      } else {
         if (var1 != null) {
            for(var5 = 0; var5 < var1.size(); ++var5) {
               var4.add(new MatchInfo((CachedRow)var1.get(var5), (CachedRow)null));
            }
         }

         return var4;
      }
   }

   public boolean previousPage() throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public boolean nextPage() throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public void populate(ResultSet var1, int var2) throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public void rowSetPopulated(RowSetEvent var1, int var2) throws SQLException {
      throw new SQLException("This is not supported for JoinRowSet");
   }

   public <T> T getObject(int var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public <T> T getObject(String var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   class JoinSorter implements Comparator {
      int[] leftCols = null;
      int[] rightCols = null;

      public JoinSorter(int[] var2, int[] var3) {
         this.leftCols = var2;
         this.rightCols = var3;
      }

      void exchange() {
         int[] var1 = this.leftCols;
         this.leftCols = this.rightCols;
         this.rightCols = var1;
      }

      public final int compare(Object var1, Object var2) {
         boolean var5 = false;

         for(int var6 = 0; var6 < this.leftCols.length; ++var6) {
            Object var3;
            Object var4;
            try {
               var3 = ((CachedRow)var1).getColumn(this.leftCols[var6]);
               var4 = ((CachedRow)var2).getColumn(this.rightCols[var6]);
            } catch (Throwable var9) {
               throw new RuntimeException("Failed to retrieve the object to compare.");
            }

            try {
               if (var3 == null && var4 == null) {
                  return 0;
               }

               if (var3 == null) {
                  return -1;
               }

               if (var4 == null) {
                  return 1;
               }

               Class var7 = var3.getClass();
               Class var8 = var4.getClass();
               int var11;
               if (var7 == var8 && Comparable.class.isAssignableFrom(var7)) {
                  var11 = ((Comparable)var3).compareTo((Comparable)var4);
               } else if (!Number.class.isAssignableFrom(var7) && !Number.class.isAssignableFrom(var8)) {
                  if (!Date.class.isAssignableFrom(var7) && !Date.class.isAssignableFrom(var8) && !Time.class.isAssignableFrom(var7) && !Time.class.isAssignableFrom(var8) && !Timestamp.class.isAssignableFrom(var7) && !Timestamp.class.isAssignableFrom(var8)) {
                     if (!Boolean.class.isAssignableFrom(var7) && !Boolean.class.isAssignableFrom(var8)) {
                        throw new RuntimeException(var7 + " can not be joined with " + var8);
                     }

                     if (String.class.isAssignableFrom(var7)) {
                        var3 = new Boolean(var3.toString().trim());
                     }

                     if (String.class.isAssignableFrom(var8)) {
                        var4 = new Boolean(var4.toString().trim());
                     }

                     var11 = ((Comparable)var3).compareTo((Comparable)var4);
                  } else {
                     if (String.class.isAssignableFrom(var7)) {
                        var3 = Date.valueOf((String)var3);
                     }

                     if (String.class.isAssignableFrom(var8)) {
                        var4 = Date.valueOf((String)var4);
                     }

                     var11 = ((Comparable)var3).compareTo((Comparable)var4);
                  }
               } else {
                  if (Number.class.isAssignableFrom(var7) || String.class.isAssignableFrom(var7)) {
                     var3 = new BigDecimal(var3.toString().trim());
                  }

                  if (Number.class.isAssignableFrom(var8) || String.class.isAssignableFrom(var8)) {
                     var4 = new BigDecimal(var4.toString().trim());
                  }

                  var11 = ((Comparable)var3).compareTo((Comparable)var4);
               }

               if (var11 != 0) {
                  return var11;
               }
            } catch (Throwable var10) {
               throw new RuntimeException(var3.getClass() + " can not be joined with " + var4.getClass());
            }
         }

         return 0;
      }
   }

   class MatchInfo {
      CachedRow left;
      CachedRow right;

      MatchInfo(CachedRow var2, CachedRow var3) {
         this.left = var2;
         this.right = var3;
      }

      CachedRow getLeft() {
         return this.left;
      }

      CachedRow getRight() {
         return this.right;
      }
   }
}
