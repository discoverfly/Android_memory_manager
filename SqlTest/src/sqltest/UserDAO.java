package sqltest;

import java.sql.*;
import java.util.*;

public class UserDAO {

    public ArrayList getUsersList() {
        Connection conn = DBUtil.getConnection();
        String sql = "select * from user";
        ArrayList users = new ArrayList();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ArrayList user = new ArrayList();
                //user.add(rs.getInt("id"));
                user.add(rs.getString("name"));
                user.add(rs.getString("password"));
                users.add(user);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return users;
    }

    public Boolean getUser(String username, String password) {
        boolean isExist = false;
        Connection conn = DBUtil.getConnection();
        String sql = "select * from user where name=? and password=? ";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            System.out.println(pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                isExist = true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isExist;
    }
}
