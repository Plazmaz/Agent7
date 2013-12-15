package me.dylan.Agent7.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.VulnerabilityData;

public class FrameResult extends JFrame {
	private static final long serialVersionUID = -4313840838204628064L;
	private static JTable table = new JTable(1, 5) {
		private static final long serialVersionUID = 8320884203325177588L;

		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row,
				int column) {
			Component c = super.prepareRenderer(renderer, row, column);
			if (c instanceof JComponent) {
				JComponent jc = (JComponent) c;
				jc.setToolTipText((String) getValueAt(row, column));
			}
			return c;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	JScrollPane dataContainer = new JScrollPane();
	private static JTableHeader header = new JTableHeader();
	public static String[] headerNames = new String[] { "Exploit Type",
			"Vector String", "Method", "Parameter", "URL(Mouse over)" };

	public static void urgent(VulnerabilityData data) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Vector<String>(data.getData()));
		table.setModel(model);
		table.repaint();
		Agent7.logLine("Found vulnerability. Data: page=" + data.getPage()
				+ " method=" + data.getMethod() + " form_name="+data.getName()+" parameter="
				+data.getParameter());
	}

	public FrameResult() {
		super("Results");
		setResizable(false);
		setSize(580, 200);
	}

	public void init() {
		header.setSize(dataContainer.getWidth(), header.getHeight());
		header.setBackground(Colors.infoBackground1);
		header.setForeground(Colors.infoColor);
		table.setBackground(Colors.infoBackground1);
		table.setForeground(Colors.infoColor);
		for (String name : headerNames) {
			TableColumn column = new TableColumn();
			column.setHeaderValue(name);
			column.setResizable(false);

			column.setWidth((int) (column.getWidth() * 1.5));
			header.getColumnModel().addColumn(column);
		}
		header.setReorderingAllowed(false);
		table.setTableHeader(header);
		dataContainer.setViewportView(table);
		JPanel scrollContainer = new JPanel();
		scrollContainer.setLayout(new BorderLayout());
		scrollContainer.add(dataContainer);
		add(scrollContainer);
		setVisible(true);
	}

}
