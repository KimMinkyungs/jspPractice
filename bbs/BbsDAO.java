package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {
		try{
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public String getDate() {
		String SQL = "SELECT NOW()";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		}catch (Exception e){
			e.printStackTrace();
		} 		finally {
	          if (rs != null) try { rs.close(); } catch(Exception e) {}
	      }
		
		return ""; // ������ ���̽� ����
	}
	
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) +1;
			}
			return 1; // ���簡 ù��° �Խù�
		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
	          if (rs != null) try { rs.close(); } catch(Exception e) {}
	      }
		
		return -1; // ������ ���̽� ����
	}
	
	public int write(String bbsTitle, String userID, String bbsContent, String fileName, String fileRealName){
		String SQL = "INSERT INTO BBS VALUES (?,?,?,?,?,?,?,?)";
		//String SQL = "INSERT INTO BBS (getNext, bbsTitle, userID, getDate(), bbsContent) VALUES (?,?,?,?,?,?)";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			pstmt.setString(7, fileName);
			pstmt.setString(8, fileRealName);
			return pstmt.executeUpdate();
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
	          if (conn != null) try { conn.close(); } catch(Exception e) {}
	      }
		return -1; // ������ ���̽� ����
		
	}
	
	public ArrayList<Bbs> getList(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1 ) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				bbs.setFileName(rs.getString(7));
				bbs.setFileRealName(rs.getString(8));	
				list.add(bbs);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
	          if (conn != null) try { conn.close(); } catch(Exception e) {}
	      }
		return list;
	}
	
	public boolean nextPage(int pageNumber){
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1 ) * 10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
	          if (rs != null) try { rs.close(); } catch(Exception e) {}
	          if (conn != null) try { conn.close(); } catch(Exception e) {}
	      }
		return false;
	
	}
	
	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public int upload(String fileName, String fileRealName, int bbsID) 	{
		try {
			String SQL = "UPDATE BBS SET fileName = ?, fileRealName =? WHERE bbsID= ?";
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fileName);
			pstmt.setString(2, fileRealName);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
	          if (conn != null) try { conn.close(); } catch(Exception e) {}
	      }
		return -1; 
	}

	public int update(int bbsID, String bbsTitle, String bbsContent ){
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
	          if (conn != null) try { conn.close(); } catch(Exception e) {}
	      }
		return -1; // ������ ���̽� ����
		
	}
	
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
	          if (conn != null) try { conn.close(); } catch(Exception e) {}
	      }
		return -1; // ������ ���̽� ����
		
	}
	
	public int getCount(){
		int count = 0;
		String SQL = "select count(*) from BBS";
	try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()){
			count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return count; // �� ���ڵ� �� ����
	}
	
}