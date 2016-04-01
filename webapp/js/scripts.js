$(".answerWrite input[type=submit]").click(addAnswer);

function addAnswer(e) {
  e.preventDefault();

  var queryString = $("form[name=answer]").serialize();

  $.ajax({
    type : 'post',
    url : '/api/qna/addAnswer',
    data : queryString,
    dataType : 'json',
    error: onError,
    success : onSuccess,
  });
}
function onSuccess(json, status){
	var answer = json.answer;
	var answerTemplate = $("#answerTemplate").html();
	var template = answerTemplate.format(answer.writer, new Date(answer.createdDate), answer.contents, answer.answerId, answer.answerId);
	
	var currentCount = parseInt($(".qna-comment-count > strong").text());
	$(".qna-comment-count > strong").text(currentCount+1);
	$(".qna-comment-slipp-articles").prepend(template);
	$(".answerWrite #writer, .answerWrite #contents").val("");
}

$(".form-delete-answer").on("click", "button", deleteAnswer);

function deleteAnswer(e){
	e.preventDefault();
	var queryString = $("form[name=delete]").serialize();
	
	var $targetAnswer = $(e.target).closest("article");
	var currentCount = parseInt($(".qna-comment-count > strong").text());
	$(".qna-comment-count > strong").text(currentCount-1);
	
	$.ajax({
		type : 'post',
		url : '/api/qna/deleteAnswer',
		data : queryString,
		dataType : 'json',
		error : onError,
		success : function(json, status, targetAnswer){
			$targetAnswer.remove();
		}
	});
}

$(".form-delete-question").on("click", "button", deleteQuestion);
function deleteQuestion(e){
	e.preventDefault();
	var queryString = $("form").serialize();
	$.ajax({
		type : 'post',
		url : '/api/qna/deleteQuestion',
		data : queryString,
		error : onError,
		success : function(json, status){
//			if(json.result.length !== 0){
//				alert(json.result);
//			}
		}
	});
}

$("#modify-question").on("click", modifyQuestion);

function modifyQuestion(e){
	e.preventDefault();
	$.ajax({
		type : 'post',
		url : '/api/qna/updateQuestion',
		data : query
	});
}


function onError(xhr, status) {
  alert("error");
}

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};