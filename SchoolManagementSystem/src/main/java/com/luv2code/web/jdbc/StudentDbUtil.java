package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception {
		
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			//Get a connection to the database
			myConn = dataSource.getConnection();
		
			//Create a SQL statements
			String sql = "select * from student order by first_name";
			myStmt = myConn.createStatement();
		
			//Execute SQL query
			myRs = myStmt.executeQuery(sql);
			
			//Process the result set
			while (myRs.next()) {
				//Retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				//Create new student object
				Student tempStudent = new Student(id, firstName, lastName, email);
				
				//Add it to the list of students
				students.add(tempStudent);
			}
			return students;
		}
		finally {
			//Close JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//Get db connection
			myConn = dataSource.getConnection();
			
			//Create sql for insert
			String sql = "insert into student "
					   + "(first_name, last_name, email) "
					   + "values (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			//Set the parameter values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			//Execute sql insert
			myStmt.execute();
		}
		
		finally {
			//Clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public Student getStudent(String theStudentId) throws Exception {

		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			//Convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			//Get connection to database
			myConn = dataSource.getConnection();
			
			//Create sql to get selected student
			String sql = "select * from student where id=?";
			
			//Create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//Set parameter
			myStmt.setInt(1, studentId);
			
			//Execute statement
			myRs = myStmt.executeQuery();
			
			//Retrieve data from result set row
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//Use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			
			else {
				throw new Exception("Could not find student id: " + studentId);
			}				
			
			return theStudent;
		}
		
		finally {
			//Clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public void updateStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			//Get db connection
			myConn = dataSource.getConnection();
			
			//Create SQL update statement
			String sql = "update student "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			//Prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//Set parameter
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			//Execute SQL statement
			myStmt.execute();
		}
		
		finally {
			//Clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			//Convert student id to int
			int studentId = Integer.parseInt(theStudentId);
			
			//Get connection to database
			myConn = dataSource.getConnection();
			
			//Create sql to delete student
			String sql = "delete from student where id=?";
			
			//Prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//Set parameter
			myStmt.setInt(1, studentId);
			
			//Execute sql statement
			myStmt.execute();
		}
		
		finally {
			//Clean up JDBC code
			close(myConn, myStmt, null);
		}	
	}
}