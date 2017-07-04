/*
    Since the full submission handler deals with promises, the user may wish to forgoe dealing with promises and creating their custom handler to respond to the promises returned by the submission handler.
    
    For that case, they can use the simple_submission object.
    
    This wrapper enables the user to call simple_submission.submit_encounter(encounter_type, on_success_function, on_error_message) and 
        - trigger `on_success_function` if the promise resolves
            - or reload the page if  `on_success_function` is undefined
        - alert(on_error_message)  
            - or alert("Please ensure all questions are answered and answered correctly.") if on_error_message is undefined
*/

simpleformservice.simple_submission = {
    submission_handler : null,
    
    submit_encounter : function(encounter_type, on_success_function, on_error_message){
        if(typeof on_success_function === "undefined") on_success_function = function(){location.reload()}
        if(typeof on_error_message === "undefined") on_error_message = "Please ensure all questions are answered and answered correctly.";
        
        var promise_to_attempt_submission = this.submission_handler.submit_encounter(encounter_type);
        var promise_to_respond_to_attempt = promise_to_attempt_submission
            .then((server_response)=>{
                on_success_function(server_response);
            })
            .catch((errors)=>{
                alert(on_error_message);
            })
    }
}