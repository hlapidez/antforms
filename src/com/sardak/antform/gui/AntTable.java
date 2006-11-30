/*
 * Created on May 23, 2005
 */
package com.sardak.antform.gui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author patmartin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AntTable extends JTable {

    /**
     * @param rowData
     * @param columnNames
     */
    public AntTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    /**
     * This method will set each column's width in order to fit their content (and header) width.
     */
    public void bestFitColumns() {
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // The header renderer is the same for each column. Grab it outside the loop.
        TableCellRenderer pHeaderRenderer = getTableHeader().getDefaultRenderer();

        // Make a pass for each column in the display. Find the max of the the
        // width of the header and each cell and set the width of the column
        // accordingly.
        TableColumn pTableColumn = null;
        for (int colNum = 0; colNum < getModel().getColumnCount(); colNum++) {
            // Initialize the width to the width of the header.
            pTableColumn = getColumnModel().getColumn(colNum);
            Component pHeaderComponent = pHeaderRenderer.getTableCellRendererComponent(null, pTableColumn.getHeaderValue(), false, false, 0, 0);
            int columnWidth = pHeaderComponent.getPreferredSize().width;

            // Make a pass for each cell in the column. Compare the width of each
            // cell to the columns width, and if the current cell is wider, use it
            // for the columns width.
            for (int rowNum = 0; rowNum < getRowCount(); rowNum++) {
                Component pCellComponent = getDefaultRenderer(getColumnClass(colNum)).getTableCellRendererComponent(this,
                        getModel().getValueAt(rowNum, colNum), false, false, rowNum, colNum);
                int cellWidth = pCellComponent.getPreferredSize().width;
                columnWidth = Math.max(columnWidth, cellWidth);
            }

            columnWidth = columnWidth + 5;
            pTableColumn.setPreferredWidth(columnWidth);
        }
    }
}
