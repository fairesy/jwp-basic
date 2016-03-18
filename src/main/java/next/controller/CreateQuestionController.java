package next.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.QuestionDao;
import next.model.Question;
import core.mvc.Controller;

public class CreateQuestionController implements Controller {
	private static final AtomicInteger count = new AtomicInteger(10);
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Question question = new Question(count.getAndIncrement(), 
				req.getParameter("writer"), 
				req.getParameter("title"), 
				req.getParameter("contents"),
				new Timestamp(Calendar.getInstance().getTime().getTime()),
				0);
		QuestionDao questionDao = new QuestionDao();
		questionDao.insert(question);
		return "redirect:/";
	}
}
