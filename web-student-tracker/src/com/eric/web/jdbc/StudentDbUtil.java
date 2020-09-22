package com.eric.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private DataSource dataSource;

	public StudentDbUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	} 
	
	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>(); 
		
		Connection myConn = null; 
		Statement myStmt = null; 
		ResultSet myRs = null; 
		
		try {
			// get a connection 
			myConn = dataSource.getConnection(); 
			
			// create sql statement
			String sql = "SELECT * FROM STUDENT ORDER BY last_name"; 
			myStmt = myConn.createStatement(); 
			
			// execute query
			myRs = myStmt.executeQuery(sql); 
			
			// Process the result set 
			while (myRs.next()) {
				int id = myRs.getInt("id"); 
				String firstName = myRs.getString("first_name"); 
				String lastName = myRs.getString("last_name"); 
				String email = myRs.getString("email"); 
				
				Student tempStudent = new Student(id, firstName, lastName, email); 
				
				students.add(tempStudent); 
				
			}				
			return students; 			
		
		} finally {
			close(myConn, myStmt, myRs); 			
		}			
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		// TODO Auto-generated method stub
		try {
			if(myRs != null) {
				myRs.close();
			}
			if(myStmt != null) {
				myStmt.close();
			}
			if(myConn != null) {
				myConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
			
		}		
	}

	public void addStudent(Student theStudent) throws Exception {
		// TODO Auto-generated method stub
		Connection myConn = null; 
		PreparedStatement myStmt = null; 
		try {
			myConn = dataSource.getConnection(); 
			
			//Create sql for insert 
			String sql = "insert into student " + "(first_name, last_name, email)" + "value (?, ?, ?)"; 
			
			myStmt = myConn.prepareStatement(sql); 
			
			// set the param values for student 
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			//execute sql
			myStmt.execute(); 			
		} finally {
			// clean JDBC objects
			close(myConn, myStmt, null); 			
		}	
		
	}

	public Student getStudent(String theStudentId) throws Exception{
		// TODO Auto-generated method stub
		
		Student theStudent = null; 
		Connection myConn = null; 
		PreparedStatement myStmt = null; 
		ResultSet myRs = null; 
		int studentId; 
		
		try {
			studentId = Integer.parseInt(theStudentId); 
			myConn = dataSource.getConnection(); 
			String sql = "Select * From Student Where id=?"; 
			myStmt = myConn.prepareStatement(sql); 
			myStmt.setInt(1,studentId);
			myRs = myStmt.executeQuery(); 
			if(myRs.next()) {
				String firstName = myRs.getString("first_name"); 
				String lastName = myRs.getString("last_name"); 
				String email = myRs.getString("email"); 
				theStudent = new Student(studentId, firstName, lastName, email); 
			} else {
				throw new Exception("Cound not find student id: " + studentId); 
			}
			
			
		} finally {
			close(myConn, myStmt, myRs); 			
		}
		
		
		return theStudent;
	}

	public void updateStudent(Student theStudent) throws Exception {
		// TODO Auto-generated method stub
		Connection myConn = null; 
		PreparedStatement myStmt = null; 
		
		
		try {
			// get db connection 
			myConn = dataSource.getConnection(); 
			
			// create sql update statement 
			String sql = "UPDATE Student SET first_name=?, last_name=?, email=? WHERE id=?"; 
			
			// prepare statement 
			myStmt = myConn.prepareStatement(sql); 
			
			// set params 
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute sql statement 
			myStmt.execute(); 
			
		} 
		finally {
			close(myConn, myStmt, null);			
		}		
	}

	public void deleteStudent(String theStudentId) throws Exception {
		// TODO Auto-generated method stub
		Connection myConn = null; 
		PreparedStatement myStmt = null; 
		
		try {
			// Convert student id to int
			int id = Integer.parseInt(theStudentId); 
			
			// get connection to database 
			myConn = dataSource.getConnection(); 
			
			// create sql to delete student 
			String sql = "DELETE FROM Student WHERE id=?"; 
			
			// prepare statement 
			myStmt = myConn.prepareStatement(sql); 
			
			// set params 
			myStmt.setInt(1, id);
			
			// execute sql statement
			myStmt.execute(); 
			
		} 
		finally {
			// clean up JDBC code 
			close(myConn, myStmt, null); 
		}
		
	}
}
