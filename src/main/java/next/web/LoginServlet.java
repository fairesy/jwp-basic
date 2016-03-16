package next.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import next.model.User;
import core.db.DataBase;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		User user = DataBase.findUserById(userId);
		if(user != null){
			if(user.getPassword().equals(password)){
				HttpSession session = req.getSession(); 
				session.setAttribute("user", user);
				
				resp.sendRedirect("/index.jsp");
			}else{
				resp.sendRedirect("/user/login_failed.html");
			}
		}else{
			resp.sendRedirect("/user/login_failed.html");
		}
	}
}
