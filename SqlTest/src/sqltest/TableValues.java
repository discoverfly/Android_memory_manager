
package sqltest;

import javax.swing.table.AbstractTableModel;
import java.util.*;
public class TableValues extends AbstractTableModel {
	private ArrayList usersList=new ArrayList();
	public TableValues(){
		UserDAO userdao=new UserDAO();
		usersList=userdao.getUsersList();
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		if(usersList!=null){
			return usersList.size();
		}
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		if(usersList!=null){
			return ((ArrayList)usersList.get(0)).size();
		}
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return ((ArrayList)usersList.get(rowIndex)).get(columnIndex);
	}

}
