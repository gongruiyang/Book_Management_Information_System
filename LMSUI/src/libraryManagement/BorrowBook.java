package libraryManagement;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BorrowBook extends JFrame{
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model = null;
	private JTable table = null;
	static final String driverName = "com.mysql.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost:3306/dbbooksmanagement?useUnicode=true&characterEncoding=utf8";
	static final String user = "root";
	static final String password = "root";
	
	public BorrowBook() throws ClassNotFoundException {
		super("BorrowBook");
		this.setLayout(new BorderLayout());
		String[][] datas = {};
		String[] titles = {"书名", "ISBN","作者","图书状态" };
		model = new DefaultTableModel(datas, titles);
		table = new JTable(model);
		this.setTitle("借阅图书");
		Font font = new Font("宋体",Font.PLAIN,35);
		
		//数据库取数据
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url,user,password);
			stmt = conn.createStatement();
			String sql = "select * from tbbook";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				model.addRow(new Object[] 
						{ rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4)});
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(rs != null) {
				try {
					rs.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt != null){
				try{
					stmt.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}	
		}
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		
		JButton borrow = new JButton(" 借阅 ");
		JButton back =   new JButton(" 返回 ");
		
		borrow.setFont(font);
		back.setFont(font);
		
		borrow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				int selectRow = table.getSelectedRow();
				
				if(selectRow != -1) {
					String borrowName = (String) model.getValueAt(selectRow, 0);
					String borrowISBN = (String) model.getValueAt(selectRow, 1);
					String borrowAuthor = (String) model.getValueAt(selectRow, 2);
					model.removeRow(selectRow);
					
					Connection conn = null;
					Statement stmt = null;
					try {
						Class.forName(driverName);
						conn = DriverManager.getConnection(url,user,password);
						stmt = conn.createStatement();
						String sql1 = "delete from tbbook where ISBN = '"+borrowISBN+"'";
						String sql2 = "insert into tbborrow(bookName,bookISBN,bookAuthor) values('"+borrowName+"','"+borrowISBN+"','"+borrowAuthor+"')";
						int count1 = stmt.executeUpdate(sql1);
						int count2 = stmt.executeUpdate(sql2);
						if(count1 == 1 && count2 == 1) {
							JDialog jd = new JDialog();
							jd.setSize(400,100);
							jd.setLayout(new BorderLayout());
							JLabel wrongInf = new JLabel("借书成功!");
							wrongInf.setFont(font);
							jd.add( wrongInf,BorderLayout.CENTER );
							jd.setVisible(true);
		                	jd.setLocationRelativeTo(null);
						}else {
							JDialog jd = new JDialog();
							jd.setSize(400,100);
							jd.setLayout(new BorderLayout());
							JLabel wrongInf = new JLabel("借书失败!");
							wrongInf.setFont(font);
							jd.add( wrongInf,BorderLayout.CENTER );
							jd.setVisible(true);
		                	jd.setLocationRelativeTo(null);
						}
					}catch(SQLException | ClassNotFoundException e1){
						e1.printStackTrace();
					}finally{
						if(stmt != null){
							try{
								stmt.close();
							}catch(SQLException e1){
								e1.printStackTrace();
							}
						}
						if(conn != null){
							try{
								conn.close();
							}catch(SQLException e1){
								e1.printStackTrace();
							}
						}	
					}
				}else {
					JDialog jd = new JDialog();
                	jd.setSize(100,60);
                	jd.setLayout(new BorderLayout());
                	jd.add( new JLabel("请选择一条记录"),BorderLayout.CENTER );
                	jd.setVisible(true);
                	jd.setLocationRelativeTo(null);
				}
			}
		});
		
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dispose();
			}
		});
		
		jp.add(borrow);
		jp.add(back);
		this.add(jp,BorderLayout.SOUTH);
		this.add(new JScrollPane(table),BorderLayout.CENTER);
		this.setResizable(false);
		setSize(600, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		
	}
	public static void main(String[] args) throws ClassNotFoundException {
		// TODO 自动生成的方法存根
		new BorrowBook();
	}

}
