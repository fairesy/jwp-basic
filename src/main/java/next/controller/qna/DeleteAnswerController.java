package next.controller.qna;

//import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.fasterxml.jackson.databind.ObjectMapper;

import next.dao.AnswerDao;
//import next.model.Answer;
import core.mvc.Controller;

public class DeleteAnswerController implements Controller{
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		long answerId = Long.parseLong(req.getParameter("answerId"));
		AnswerDao answerDao = new AnswerDao();
		answerDao.delete(answerId);
		
//		ObjectMapper mapper = new ObjectMapper();
//		resp.setContentType("application/json;charset=UTF-8");
//		PrintWriter out = resp.getWriter();
//		out.print(answerId);
		
		return null;
	}
}
