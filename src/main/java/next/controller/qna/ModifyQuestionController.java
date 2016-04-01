package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.QuestionDao;
import next.model.Question;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;

public class ModifyQuestionController extends AbstractController {
	private QuestionDao questionDao = new QuestionDao();
	private Question question;
	
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
		Long questionId = Long.parseLong(req.getParameter("questionId"));
		
		question = questionDao.findById(questionId);
		
		ModelAndView mav = jspView("/qna/modify.jsp");
		mav.addObject("question", question);
		return mav;
	}
}
