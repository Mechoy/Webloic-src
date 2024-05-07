package weblogic.jdbc.rowset;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.rowset.spi.SyncFactory;
import weblogic.xml.stream.CharacterData;
import weblogic.xml.stream.EndElement;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.util.TypeFilter;

public final class WebRowSetReader implements XMLSchemaConstants {
   private static final boolean DEBUG = false;
   private CachedRowSetImpl rowSet;
   private CachedRowSetMetaData metaData;
   private XMLInputStreamFactory factory = XMLInputStreamFactory.newInstance();
   private ArrayList keys = new ArrayList();

   public WebRowSetReader(CachedRowSetImpl var1) throws SQLException {
      this.rowSet = var1;
      this.metaData = (CachedRowSetMetaData)var1.getMetaData();
   }

   public void loadXML(XMLInputStream var1) throws IOException, SQLException {
      XMLInputStream var2 = this.factory.newInputStream(var1, new TypeFilter(22));
      this.parseRowSet(var2);
   }

   private void parseRowSet(XMLInputStream var1) throws IOException, SQLException {
      boolean var2 = this.metaData.getColumnCount() > 0;
      var1.next();

      while(var1.hasNext()) {
         String var3 = this.getName(var1.peek());
         if ("webRowSet".equals(var3)) {
            var1.next();
            break;
         }

         if ("properties".equals(var3)) {
            this.parseProperties(var1);
         } else if ("metadata".equals(var3)) {
            this.parseMetaData(var1);
            var2 = true;
         } else {
            if (!"data".equals(var3)) {
               throw new IOException("XML document has incorrect format. The current element is " + var3);
            }

            if (!var2) {
               throw new IOException("metadata must be established before data get parsed.");
            }

            this.parseData(var1);
         }
      }

   }

   private int parseSimpleElements4Int(XMLInputStream var1) throws IOException, SQLException {
      return Integer.parseInt(this.parseSimpleElements(var1));
   }

   private boolean parseSimpleElements4Boolean(XMLInputStream var1) throws IOException, SQLException {
      return new Boolean(this.parseSimpleElements(var1));
   }

   private String parseSimpleElements(XMLInputStream var1) throws IOException, SQLException {
      String var2 = null;
      String var3 = null;
      XMLEvent var4 = var1.next();
      if (!(var4 instanceof StartElement)) {
         throw new IOException("XML document has incorrect format.");
      } else {
         var2 = this.getName(var4);
         var4 = var1.next();
         if (var4 instanceof CharacterData) {
            var3 = ((CharacterData)var4).getContent();
            var4 = var1.next();
            if (!(var4 instanceof EndElement) || !var2.equals(this.getName(var4))) {
               throw new IOException("XML document has incorrect format. The current element is " + this.getName(var4));
            }
         } else if (!(var4 instanceof EndElement) || !var2.equals(this.getName(var4))) {
            throw new IOException("XML document has incorrect format. The current element is " + this.getName(var4));
         }

         return var3;
      }
   }

   private void parseProperties(XMLInputStream var1) throws IOException, SQLException {
      var1.next();

      while(var1.hasNext()) {
         String var2 = this.getName(var1.peek());
         if ("properties".equals(var2)) {
            var1.next();
            break;
         }

         if (!"map".equals(var2)) {
            if ("key-columns".equals(var2)) {
               var1.next();

               while(var1.hasNext()) {
                  var2 = this.getName(var1.peek());
                  if ("key-columns".equals(var2)) {
                     var1.next();
                     break;
                  }

                  if (!"column".equals(var2)) {
                     throw new IOException("XML document has incorrect format. The current element is " + var2);
                  }

                  this.keys.add(this.parseSimpleElements(var1));
               }
            } else if ("sync-provider".equals(var2)) {
               var1.next();

               while(var1.hasNext()) {
                  var2 = this.getName(var1.peek());
                  if ("sync-provider".equals(var2)) {
                     var1.next();
                     break;
                  }

                  if ("sync-provider-name".equals(var2)) {
                     String var7 = this.parseSimpleElements(var1);

                     try {
                        Class.forName(var7);
                        SyncFactory.registerProvider(var7);
                        this.rowSet.setSyncProvider(var7);
                     } catch (ClassNotFoundException var5) {
                     }
                  } else if ("sync-provider-vendor".equals(var2)) {
                     this.parseSimpleElements(var1);
                  } else if ("sync-provider-version".equals(var2)) {
                     this.parseSimpleElements(var1);
                  } else if ("sync-provider-grade".equals(var2)) {
                     this.parseSimpleElements(var1);
                  } else {
                     if (!"data-source-lock".equals(var2)) {
                        throw new IOException("XML document has incorrect format. The current element is " + var2);
                     }

                     this.rowSet.getSyncProvider().setDataSourceLock(this.parseSimpleElements4Int(var1));
                  }
               }
            } else if ("command".equals(var2)) {
               this.rowSet.setCommand(this.parseSimpleElements(var1));
            } else if ("concurrency".equals(var2)) {
               this.rowSet.setConcurrency(this.parseSimpleElements4Int(var1));
            } else if ("datasource".equals(var2)) {
               this.rowSet.setDataSourceName(this.parseSimpleElements(var1));
            } else if ("escape-processing".equals(var2)) {
               this.rowSet.setEscapeProcessing(this.parseSimpleElements4Boolean(var1));
            } else if ("fetch-direction".equals(var2)) {
               this.rowSet.setFetchDirection(this.parseSimpleElements4Int(var1));
            } else if ("fetch-size".equals(var2)) {
               this.rowSet.setFetchSize(this.parseSimpleElements4Int(var1));
            } else if ("isolation-level".equals(var2)) {
               this.rowSet.setTransactionIsolation(this.parseSimpleElements4Int(var1));
            } else if ("max-field-size".equals(var2)) {
               this.rowSet.setMaxFieldSize(this.parseSimpleElements4Int(var1));
            } else if ("max-rows".equals(var2)) {
               this.rowSet.setMaxRows(this.parseSimpleElements4Int(var1));
            } else if ("query-timeout".equals(var2)) {
               this.rowSet.setQueryTimeout(this.parseSimpleElements4Int(var1));
            } else if ("read-only".equals(var2)) {
               this.rowSet.setReadOnlyInternal(this.parseSimpleElements4Boolean(var1));
            } else if ("rowset-type".equals(var2)) {
               this.rowSet.setType(this.parseSimpleElements4Int(var1));
            } else if ("show-deleted".equals(var2)) {
               this.rowSet.setShowDeleted(this.parseSimpleElements4Boolean(var1));
            } else if ("table-name".equals(var2)) {
               this.rowSet.setTableName(this.parseSimpleElements(var1));
            } else {
               if (!"url".equals(var2)) {
                  throw new IOException("XML document has incorrect format. The current element is " + var2);
               }

               this.rowSet.setUrl(this.parseSimpleElements(var1));
            }
         } else {
            HashMap var3 = new HashMap();
            var1.next();

            while(var1.hasNext()) {
               var2 = this.getName(var1.peek());
               if ("map".equals(var2)) {
                  var1.next();
                  break;
               }

               if (!"type".equals(var2)) {
                  throw new IOException("XML document has incorrect format. The current element is " + var2);
               }

               try {
                  var3.put(this.parseSimpleElements(var1), Class.forName(this.parseSimpleElements(var1)));
               } catch (ClassNotFoundException var6) {
               }
            }

            this.rowSet.setTypeMap(var3);
         }
      }

   }

   private void parseMetaData(XMLInputStream var1) throws IOException, SQLException {
      int var2 = 0;
      int var3 = 0;
      var1.next();

      while(var1.hasNext()) {
         String var4 = this.getName(var1.peek());
         if ("metadata".equals(var4)) {
            var1.next();
            break;
         }

         if ("column-count".equals(var4)) {
            var2 = this.parseSimpleElements4Int(var1);
            this.metaData.setColumnCountInternal(var2);
         } else {
            if (!"column-definition".equals(var4)) {
               throw new IOException("XML document has incorrect format. The current element is " + var4);
            }

            this.parseColumnDef(var1);
            ++var3;
            if (var3 > var2) {
               throw new IOException("column-count " + var2 + " doesn't match with the number of column-definition.");
            }
         }
      }

      int var7 = this.keys.size();
      if (var7 > 0) {
         int[] var5 = new int[var7];

         for(int var6 = 0; var6 < var7; ++var6) {
            var5[var6] = this.metaData.findColumn((String)this.keys.get(var6));
         }

         this.rowSet.setKeyColumns(var5);
      }

   }

   private void parseColumnDef(XMLInputStream var1) throws IOException, SQLException {
      var1.next();
      int var2 = 1;

      while(var1.hasNext()) {
         String var3 = this.getName(var1.peek());
         if ("column-definition".equals(var3)) {
            var1.next();
            break;
         }

         if ("column-index".equals(var3)) {
            var2 = this.parseSimpleElements4Int(var1);
         } else if ("auto-increment".equals(var3)) {
            this.metaData.setAutoIncrement(var2, this.parseSimpleElements4Boolean(var1));
         } else if ("definitely-writable".equals(var3)) {
            this.metaData.setDefinitelyWritable(var2, this.parseSimpleElements4Boolean(var1));
         } else if ("case-sensitive".equals(var3)) {
            this.metaData.setCaseSensitive(var2, this.parseSimpleElements4Boolean(var1));
         } else if ("currency".equals(var3)) {
            this.metaData.setCurrency(var2, this.parseSimpleElements4Boolean(var1));
         } else if ("nullable".equals(var3)) {
            this.metaData.setNullable(var2, this.parseSimpleElements4Int(var1));
         } else if ("signed".equals(var3)) {
            this.metaData.setSigned(var2, this.parseSimpleElements4Boolean(var1));
         } else if ("searchable".equals(var3)) {
            this.metaData.setSearchable(var2, this.parseSimpleElements4Boolean(var1));
         } else if ("column-display-size".equals(var3)) {
            this.metaData.setColumnDisplaySize(var2, this.parseSimpleElements4Int(var1));
         } else if ("column-label".equals(var3)) {
            this.metaData.setColumnLabel(var2, this.parseSimpleElements(var1));
         } else if ("column-name".equals(var3)) {
            this.metaData.setColumnName(var2, this.parseSimpleElements(var1));
         } else if ("column-class-name".equals(var3)) {
            this.metaData.setColumnClassName(var2, this.parseSimpleElements(var1));
         } else if ("schema-name".equals(var3)) {
            this.metaData.setSchemaName(var2, this.parseSimpleElements(var1));
         } else if ("column-precision".equals(var3)) {
            this.metaData.setPrecision(var2, this.parseSimpleElements4Int(var1));
         } else if ("column-scale".equals(var3)) {
            this.metaData.setScale(var2, this.parseSimpleElements4Int(var1));
         } else if ("table-name".equals(var3)) {
            this.metaData.setTableName(var2, this.parseSimpleElements(var1));
         } else if ("catalog-name".equals(var3)) {
            this.metaData.setCatalogName(var2, this.parseSimpleElements(var1));
         } else if ("column-type".equals(var3)) {
            this.metaData.setColumnType(var2, this.parseSimpleElements4Int(var1));
         } else {
            if (!"column-type-name".equals(var3)) {
               throw new IOException("XML document has incorrect format. The current element is " + var3);
            }

            this.metaData.setColumnTypeName(var2, this.parseSimpleElements(var1));
         }
      }

   }

   private CachedRow parseRow(XMLInputStream var1) throws IOException, SQLException {
      CachedRow var2 = new CachedRow(this.metaData);
      String var3 = this.getName(var1.next());
      int var4 = 0;

      while(var1.hasNext()) {
         String var5 = this.getName(var1.peek());
         if (var5.equals(var3)) {
            var1.next();
            break;
         }

         Object var6;
         if ("columnValue".equals(var5)) {
            ++var4;
            var6 = TypeMapper.getJavaValue(this.metaData.getColumnType(var4), this.parseSimpleElements(var1));
            var2.setOriginal(var4, var6);
         } else {
            if (!"updateValue".equals(var5)) {
               throw new IOException("XML document has incorrect format. The current element is " + var5);
            }

            var6 = TypeMapper.getJavaValue(this.metaData.getColumnType(var4), this.parseSimpleElements(var1));
            var2.updateColumn(var4, var6);
         }
      }

      return var2;
   }

   private void parseData(XMLInputStream var1) throws IOException, SQLException {
      ArrayList var2 = new ArrayList();
      CachedRow var3 = null;
      var1.next();

      for(; var1.hasNext(); var2.add(var3)) {
         String var4 = this.getName(var1.peek());
         if ("data".equals(var4)) {
            var1.next();
            break;
         }

         if ("currentRow".equals(var4)) {
            var3 = this.parseRow(var1);
            var3.setUpdatedRow(false);
            var3.setDeletedRow(false);
            var3.setInsertRow(false);
         } else if ("insertRow".equals(var4)) {
            var3 = this.parseRow(var1);
            var3.setInsertRow(true);
            var3.setDeletedRow(false);
         } else if ("deleteRow".equals(var4)) {
            var3 = this.parseRow(var1);
            var3.setDeletedRow(true);
            var3.setInsertRow(false);
         } else {
            if (!"modifyRow".equals(var4)) {
               throw new IOException("XML document has incorrect format. The current element is " + var4);
            }

            var3 = this.parseRow(var1);
            var3.setUpdatedRow(true);
            var3.setDeletedRow(false);
            var3.setInsertRow(false);
         }
      }

      this.rowSet.getCachedRows().addAll(var2);
   }

   private String getName(XMLEvent var1) {
      return var1.getName().getLocalName();
   }
}
