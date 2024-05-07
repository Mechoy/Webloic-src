package weblogic.tools.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import weblogic.servlet.jsp.BeanUtils;
import weblogic.utils.StringUtils;

public class BeanGrid extends ModelPanel implements ActionListener, ListSelectionListener, MouseListener {
   private static MarathonTextFormatter fmt = new MarathonTextFormatter();
   private boolean m_onlyAcceptUniqueEntries;
   private JTable table;
   private BeanTableModel model;
   private DefaultTableColumnModel columnModel;
   private DefaultListSelectionModel selectionModel;
   private JButton deleteB;
   private JButton addB;
   private JButton editB;
   private JScrollPane tableScrollPane;
   private Border m_border;
   private boolean m_createBorder;
   private String m_editorDialogTitle;
   Container buttonPane;
   Class bc;
   BeanInfo bi;
   PropertyDescriptor[] pds;
   Object[] beans;
   String[] props;
   String[] titles;
   IBeanRowEditor bre;

   public BeanGrid(Object[] var1) {
      this(var1, (IBeanRowEditor)null);
   }

   public BeanGrid(Object[] var1, IBeanRowEditor var2) {
      this(var1, (String[])null, (String[])null, (String[])null, var2);
   }

   public BeanGrid(Object[] var1, String[] var2, String[] var3) {
      this(var1, var2, var3, (String[])null);
   }

   public BeanGrid(Object[] var1, String[] var2, String[] var3, String[] var4) {
      this(var1, var2, var3, var4, (IBeanRowEditor)null);
   }

   public BeanGrid(Object[] var1, String[][] var2, IBeanRowEditor var3) {
      this.m_onlyAcceptUniqueEntries = false;
      this.m_border = new EmptyBorder(2, 2, 2, 2);
      this.m_createBorder = false;
      this.m_editorDialogTitle = null;
      this.buttonPane = null;
      String[][] var4 = this.invertArray(var2);
      this.init(var1, var4[0], var4[1], var4[2], var3);
   }

   public BeanGrid(Object[] var1, String[] var2, String[] var3, String[] var4, IBeanRowEditor var5) {
      this.m_onlyAcceptUniqueEntries = false;
      this.m_border = new EmptyBorder(2, 2, 2, 2);
      this.m_createBorder = false;
      this.m_editorDialogTitle = null;
      this.buttonPane = null;
      this.init(var1, var2, var3, var4, var5);
   }

   public void setEditorDialogTitle(String var1) {
      this.m_editorDialogTitle = var1;
   }

   public void setOnlyAcceptUniqueEntries(boolean var1) {
      this.m_onlyAcceptUniqueEntries = var1;
   }

   public boolean alreadyExists(Object var1) {
      for(int var2 = 0; var2 < this.beans.length; ++var2) {
         if (null != var1 && var1.equals(this.beans[var2])) {
            return true;
         }
      }

      return false;
   }

   public void cleanup() {
      this.bi = null;
      this.beans = null;
      this.bre = null;
   }

   public void setEditable(boolean var1) {
      this.model.setEditable(var1);
      this.table.setShowVerticalLines(false);
      this.table.setBackground(Color.lightGray);
   }

   public JTable getTable() {
      return this.table;
   }

   private void init(Object[] var1, String[] var2, String[] var3, String[] var4, IBeanRowEditor var5) {
      this.beans = var1;
      this.bre = var5;
      if (this.m_createBorder) {
         this.setBorder(this.m_border);
      }

      if (var1 != null) {
         this.bc = var1.getClass().getComponentType();
         if (this.bc.isArray()) {
            throw new RuntimeException("multi-dimensional array");
         } else if (BeanUtils.isStringConvertible(this.bc)) {
            throw new RuntimeException("use PrimitiveGrid for primitive types");
         } else {
            try {
               this.bi = Introspector.getBeanInfo(this.bc);
            } catch (IntrospectionException var8) {
               throw new RuntimeException(var8.toString());
            }

            this.pds = this.bi.getPropertyDescriptors();
            this.props = var2;
            this.fixPDs();
            int var6;
            if (this.props == null) {
               this.props = new String[this.pds.length];

               for(var6 = 0; var6 < this.pds.length; ++var6) {
                  this.props[var6] = this.pds[var6].getName();
               }
            }

            this.titles = var3;
            if (this.titles == null) {
               this.titles = new String[this.props.length];

               for(var6 = 0; var6 < this.titles.length; ++var6) {
                  this.titles[var6] = StringUtils.ucfirst(this.props[var6]);
               }
            }

            this.makeTable();

            for(var6 = 0; var4 != null && var6 < var4.length; ++var6) {
               this.table.putClientProperty("wl.helpanchor." + var6, var4[var6]);
            }

            for(var6 = 0; var6 < this.titles.length; ++var6) {
               TableColumn var7 = this.columnModel.getColumn(var6);
               var7.setHeaderValue(uncolon(this.titles[var6]));
            }

            var6 = this.table.getRowCount() * this.table.getRowHeight();
            int var9 = this.table.getColumnModel().getTotalColumnWidth();
            this.table.setPreferredScrollableViewportSize(new Dimension(var9, var6));
            this.tableScrollPane = new JScrollPane(this.table);
            this.tableScrollPane.setBorder((Border)null);
            this.setLayout(new BorderLayout());
            this.add(this.tableScrollPane, "Center");
            this.buttonPane = this.makeButtonPane();
            this.add(this.buttonPane, "South");
         }
      }
   }

   private static String uncolon(String var0) {
      boolean var1 = false;
      int var2;
      if (var0 != null && (var2 = var0.length()) != 0) {
         if (var0.charAt(var2 - 1) == ':') {
            var0 = var0.substring(0, var2 - 1);
         }

         return var0;
      } else {
         return var0;
      }
   }

   private String[][] invertArray(String[][] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].length != 3) {
            throw new IllegalArgumentException("expected row length 3, not " + var1[var2].length);
         }
      }

      String[][] var4 = new String[3][var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var4[0][var3] = var1[var3][0];
         var4[1][var3] = var1[var3][1];
         var4[2][var3] = var1[var3][2];
      }

      return var4;
   }

   public void setParentInfo(Object var1, String var2) {
      this.model.setParentInfo(var1, var2);
   }

   public void setParentAdder(Object var1, String var2) {
      this.model.setParentAdder(var1, var2);
   }

   public void setConstrained(String var1, Object[] var2) {
      this.setConstrained(var1, var2, false);
   }

   public void setConstrained(String var1, Object[] var2, boolean var3) {
      JComboBox var4 = new JComboBox(var2);
      var4.setEditable(var3);
      DefaultCellEditor var5 = new DefaultCellEditor(var4);
      TableColumn var6 = this.table.getColumn(uncolon(var1));
      var6.setCellEditor(var5);
   }

   public Object[] getBeans() {
      return this.beans;
   }

   public void setBeans(Object[] var1) {
      Class var2 = this.beans.getClass().getComponentType();
      Class var3 = var1.getClass().getComponentType();
      if (!var2.isAssignableFrom(var3)) {
         String var4 = var3.getName();
         String var5 = var2.getName();
         throw new IllegalArgumentException("type mismatch: new=" + var4 + " old=" + var5);
      } else {
         this.beans = (Object[])((Object[])var1.clone());
         this.model.setBeans(var1);
         this.model.fireTableDataChanged();
      }
   }

   public boolean isEditable(int var1) {
      return this.model.isCellEditable(0, var1);
   }

   public void setEditable(int var1, boolean var2) {
      this.model.setEditable(var1, var2);
   }

   public void modelToUI() {
   }

   public void uiToModel() {
   }

   private void fixPDs() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.pds.length; ++var2) {
         if (this.pds[var2].getReadMethod() != null) {
            var1.add(this.pds[var2]);
         }
      }

      this.pds = new PropertyDescriptor[var1.size()];
      var1.toArray(this.pds);
   }

   private void makeTable() {
      this.model = new BeanTableModel(this.bc, this.bi, this.pds, this.beans, this.props, this.titles);
      this.table = new JTable();
      this.table.addMouseListener(this);
      this.table.setBorder((Border)null);
      DefaultTableCellRenderer var1 = (DefaultTableCellRenderer)this.table.getTableHeader().getDefaultRenderer();
      var1.setHorizontalAlignment(2);
      this.table.setModel(this.model);
      this.columnModel = (DefaultTableColumnModel)this.table.getColumnModel();
      this.selectionModel = (DefaultListSelectionModel)this.table.getSelectionModel();
      this.selectionModel.addListSelectionListener(this);
      DefaultListSelectionModel var10001 = this.selectionModel;
      this.selectionModel.setSelectionMode(0);
   }

   private Container makeButtonPane() {
      JPanel var1 = new JPanel(new GridBagLayout());
      GridBagConstraints var2 = new GridBagConstraints();
      var2.insets = new Insets(5, 0, 5, 6);
      var2.gridwidth = 1;
      var2.gridheight = 1;
      var2.gridx = 0;
      var2.gridy = 0;
      var2.fill = 0;
      var2.anchor = 13;
      var2.weightx = var2.weighty = 0.0;
      var2.weightx = 1.0;
      this.addB = new JButton(fmt.getAddEllipsis());
      var1.add(this.addB, var2);
      var2.weightx = 0.0;
      this.addB.addActionListener(this);
      ++var2.gridx;
      this.editB = new JButton(fmt.getEditEllipsis());
      var1.add(this.editB, var2);
      this.editB.addActionListener(this);
      ++var2.gridx;
      this.deleteB = new JButton(fmt.getDelete());
      var1.add(this.deleteB, var2);
      this.deleteB.addActionListener(this);
      this.editB.setEnabled(false);
      this.deleteB.setEnabled(false);
      var1.setBorder((Border)null);
      return var1;
   }

   public JButton getAddButton() {
      return this.addB;
   }

   public JButton getEditButton() {
      return this.editB;
   }

   public JButton getDeleteButton() {
      return this.deleteB;
   }

   private static void p(String var0) {
      System.err.println(var0);
   }

   private void deleteBean(Object var1) {
      try {
         Method var2 = var1.getClass().getMethod("onDelete");
         var2.invoke(var1);
      } catch (IllegalAccessException var3) {
         var3.printStackTrace();
      } catch (InvocationTargetException var4) {
         var4.printStackTrace();
      } catch (NoSuchMethodException var5) {
         System.out.println("Warning:  couldn't find onDelete() on " + var1.getClass());
      }

   }

   public void valueChanged(ListSelectionEvent var1) {
      int var2 = this.selectionModel.getLeadSelectionIndex();
      int var3 = this.table.getRowCount();
      if (this.selectionModel.isSelectionEmpty()) {
         this.editB.setEnabled(false);
         this.deleteB.setEnabled(false);
      } else {
         this.editB.setEnabled(true & this.bre != null);
         this.deleteB.setEnabled(true);
      }

   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.getClickCount() >= 2) {
         int var2 = this.table.getSelectedRow();
         if (this.beans != null && var2 >= 0 && var2 < this.beans.length) {
            this.editObject(this.beans[var2]);
         }

      }
   }

   public void mousePressed(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1) {
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   public void actionPerformed(ActionEvent var1) {
      int var2 = this.selectionModel.getLeadSelectionIndex();
      Object var3 = var1.getSource();
      int var4 = this.table.getRowCount();
      if (var3 == this.deleteB) {
         if (var2 < 0 || var2 >= var4) {
            return;
         }

         this.doDelete(var2);
         this.beans = this.model.getBeans();
         --var4;
         if (var2 < var4) {
            this.selectionModel.setSelectionInterval(var2, var2);
         } else if (var4 > 0) {
            this.selectionModel.setSelectionInterval(var2 - 1, var2 - 1);
         }

         if (this.model.getRowCount() == 0) {
            this.editB.setEnabled(false);
            this.deleteB.setEnabled(false);
         }
      } else if (var3 == this.addB) {
         Object var5 = this.doAdd();
         if (var5 == null) {
            return;
         }

         this.model.addRow(var5);
         this.beans = this.model.getBeans();
      } else if (var3 == this.editB) {
         this.editObject(this.beans[var2]);
      }

   }

   protected void editObject(Object var1) {
      if (this.bre != null) {
         RowEditorDialog var2 = null;
         var2 = new RowEditorDialog(this.getEnclosingFrame(), "", true, this.bre);
         var2.editObject(var1);
         this.model.fireTableDataChanged();
      }

   }

   public void doDelete(int var1) {
      this.deleteBean(this.beans[var1]);
      this.model.removeRow(var1);
   }

   public Object doAdd() {
      if (this.bre == null) {
         return null;
      } else {
         RowEditorDialog var1 = new RowEditorDialog(this.getEnclosingFrame(), this.m_editorDialogTitle, true, this.bre);
         Object var2 = var1.addObject();

         while(var2 != null && this.m_onlyAcceptUniqueEntries && this.alreadyExists(var2)) {
            String var3 = fmt.getEntryAlreadyExists();
            String var4 = fmt.getIllegalEntry();
            JOptionPane.showMessageDialog(this, var3, var4, 0);
            var1.editObject(var2);
            if (var1.wasCancelled()) {
               var2 = null;
            }
         }

         return var2;
      }
   }

   protected Frame getEnclosingFrame() {
      Container var1 = this.getParent();

      while(!(var1 instanceof Frame)) {
         var1 = var1.getParent();
         if (var1 == null) {
            return null;
         }
      }

      return (Frame)var1;
   }

   public Dimension getMinimumSize() {
      Dimension var1 = this.tableScrollPane.getPreferredSize();
      Dimension var2 = this.buttonPane.getMinimumSize();
      Dimension var3 = this.buttonPane.getMinimumSize();
      return var3;
   }

   public Dimension getPreferredSize() {
      return this.getMinimumSize();
   }
}
