package com.eric.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil; 
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource; 
	
	

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		try {
			
			
			// list the students
			studentDbUtil = new StudentDbUtil(dataSource); 
			
		} catch (Exception exc) {
			throw new ServletException (exc); 
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// read the command 
			String theCommand = request.getParameter("command"); 
			
			if(theCommand == null) {
				theCommand = "LIST"; 				
			}
			
			switch (theCommand) {			
			case "LIST":
				listStudent(request, response); 
				break;
			case "LOAD": 
            	loadStudent(request, response); 
            	break; 	
			case "UPDATE": 
            	updateStudent(request, response); 
            	break;
			case "DELETE": 
				deleteStudent(request, response); 
				break; 
			default: 
				listStudent(request, response); 				
			}
			
			// List the students
			listStudent(request, response); 
		} catch (Exception exc) 
		{
			throw new ServletException(exc); 			
		}
	}
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Read student id from form data
		String theStudentId = request.getParameter("studentId"); 
		
		
		// delete student from database 
		studentDbUtil.deleteStudent(theStudentId); 
		// send them back to list-student page
		listStudent(request, response); 
		
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// TODO Auto-generated method stub
		// REad student info
		int id = Integer.parseInt(request.getParameter("studentId")); 
		String firstName = request.getParameter("firstName"); 
		String lastName = request.getParameter("lastName"); 
		String email = request.getParameter("email"); 	
		
		// create a new student object 
		Student theStudent = new Student(id, firstName, lastName, email); 
		
		// perform update on database 
		studentDbUtil.updateStudent(theStudent); 
		
		// send them back to the list student page 
		listStudent(request, response); 
		
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // read the "command" parameter
            String theCommand = request.getParameter("command");
                    
            // route to the appropriate method
            switch (theCommand) {
                            
            case "ADD":
                addStudent(request, response);
                break;
             
            default:
                listStudent(request, response);
            }
                
        }
        catch (Exception exc) {
            throw new ServletException(exc);
        }
        
    }



	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id from form data
		String theStudentId = request.getParameter("studentId"); 
		
		// get student from database (db util)
		Student theStudent = studentDbUtil.getStudent(theStudentId); 
		
		// place student in request attribute 
		request.setAttribute("THE_STUDENT", theStudent); 
		
		// send to jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp"); 
		dispatcher.forward(request, response); 
		
	}



	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		// read student info from form data
		String firstName = request.getParameter("firstName"); 
		String lastName = request.getParameter("lastName"); 
		String email = request.getParameter("email"); 
		
		// create a new student object 
		Student theStudent = new Student(firstName, lastName, email); 
		// add the student to the database 
		studentDbUtil.addStudent(theStudent); 
		
		// send back to main page 
		// listStudent(request, response); 
		response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");
		
	}



	private void listStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Student> students = studentDbUtil.getStudents(); 
		
		request.setAttribute("STUDENT_LIST", students);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp"); 
		dispatcher.forward(request, response);
	}
	
	

}
