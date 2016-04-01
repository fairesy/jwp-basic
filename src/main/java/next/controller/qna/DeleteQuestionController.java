package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.QuestionDao;
import next.model.Question;
import next.model.Result;
import core.jdbc.DataAccessException;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;

public class DeleteQuestionController extends AbstractController {

	private QuestionDao questionDao = new QuestionDao();
	
	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav;
		long questionId = Long.parseLong(request.getParameter("questionId"));
		
		Question targetQuestion = questionDao.findById(questionId);
		int answerCount = targetQuestion.getCountOfComment();
		if(answerCount == 0){
			questionDao.delete(questionId);
			mav = jspView("redirect:/");
//			try{
//				questionDao.delete(questionId);
////				mav.addObject("result", Result.ok());
//				mav = jspView("redirect:/");
//			}catch(DataAccessException e){
//				mav = jsonView();
//				mav.addObject("result", Result.fail(e.getMessage()));
//			}
		}else{
			mav = jsonView();
			mav.addObject("result", "답변이 1개 이상 있는 질문은 삭제할 수 없습니다!");
		}
		return mav;
	}
}

