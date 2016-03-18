package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.QuestionDao;
import next.dao.UserDao;

public class HomeController implements Controller {
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		UserDao userDao = new UserDao();
		QuestionDao questionDao = new QuestionDao();
		req.setAttribute("users", userDao.findAll());
		req.setAttribute("questions", questionDao.finaAll());
		return "index.jsp";
	}
}
