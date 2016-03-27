String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

var Answer = (function(){
		
	function init(){
		$(".answerWrite > form > input").on("click", addAnswer);
		$(".qna-comment-slipp-articles").on("click", ".form-delete > button", deleteAnswer);
//		$(".form-delete > button").on("click", deleteAnswer);
	}
	
	function addAnswer(event){
		event.preventDefault();
		var queryString = $("form[name=answer]").serialize();
		
		$.ajax({
			type : "post",
			url : "/api/qna/addanswer",
			data : queryString,
			dataType : "json",
			success : addAnswerToDom,
			error : onError
		});
	}
	
	function deleteAnswer(event){
		event.preventDefault();
		var queryString = $("form.form-delete").serialize();
		var targetAnswer = $(event.target).closest(".article");
		$.ajax({
			type : "post",
			url : "/api/qna/deleteAnswer",
			data : queryString,
			error : onError,
			success : function(data, status){
				deleteAnswerFromDom(data, status, targetAnswer);
			}
		});
	}
	
	function onError(){
		alert("AJAX error!");
	}
	
	function deleteAnswerFromDom(response, status, targetAnswer){
		console.log("response : " +response);
		console.log("status : " +status);
		console.log(targetAnswer);
		targetAnswer.remove();
	}
	
	function addAnswerToDom(json, status){
		console.log(json);
		
		var answerTemplate = $("#answerTemplate").html();
		var template = answerTemplate.format(json.writer, new Date(json.createdDate), json.contents, json.answerId, json
		.answerId); 
		$(".qna-comment-slipp-articles").prepend(template);
		
		$("input#writer").val("");
		$("textarea#contents").val("");
	}
	
	return {
		init : init
	}
})();

$(document).ready(function(){
	Answer.init();
})
	
