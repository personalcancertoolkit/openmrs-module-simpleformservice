/*
    Since the full submission handler deals with promises, the user may wish to forgoe dealing with promises and creating their custom handler to respond to the promises returned by the submission handler.
    
    For that case, they can use the simple_submission object.
    
    This wrapper enables the user to call simple_submission.submit_encounter(encounter_type, on_success_function, on_error_message) and 
        - trigger `on_success_function` if the promise resolves
            - or reload the page if  `on_success_function` is undefined
        - alert(on_error_message)  
            - or alert("Please ensure all questions are answered and answered correctly.") if on_error_message is undefined
*/

simpleformservice.simple_permission_manager = {
    give_permission : function(permission_data, on_success_function, on_error_message){
        if(typeof on_success_function === "undefined") on_success_function = function(){location.reload()}
        if(typeof on_error_message === "undefined") on_error_message = "Sorry, there has been some error granting permissions.";
        
        var errored = false;
        if(typeof permission_data.granted_to_person_uuid == "undefined"){ 
            console.error("permission_data.granted_to_person_uuid must be defined for simpleformservice.simple_permission_manager.give_permission");
            errored = true;
        }
        if(typeof permission_data.encounter_type == "undefined"){ 
            console.error("permission_data.encounter_type must be defined for simpleformservice.simple_permission_manager.give_permission");
            errored = true;
        }
        if(typeof permission_data.permission_type == "undefined"){ 
            console.error("permission_data.permission_type must be defined for simpleformservice.simple_permission_manager.give_permission");
            errored = true;
        } else if(["create", "read", "delete"].indexOf(permission_data.permission_type) == -1){
            console.error("permission_data.permission_type must be either `create`, `read`, or `delete` for simpleformservice.simple_permission_manager.give_permission");
            console.log("is actually : " + permission_data.permission_type)
            errored = true;
        }
        if(errored == true){
            return false;
        }
        
        var promise_to_attempt = new Promise((resolve, reject)=>{
                console.log("Sending data to server");
                console.log(permission_data);
                // send the data to the server and resolve on the response
                jq.post("/openmrs/ws/simpleformservice/api/give_data_access", {
                    json : JSON.stringify(permission_data),
                }, function (response){
                    console.log("Request Responded");
                    console.log(response);
                    resolve(response);  
                });
            })
        var promise_to_respond_to_attempt = promise_to_attempt
            .then((server_response)=>{
                on_success_function(server_response);
            })
            .catch((errors)=>{
                console.log(errors);
                alert(on_error_message);
            })
    },
    
    retreive_data_access : function(permission_data, on_success_function, on_error_message){
        if(typeof on_success_function === "undefined") on_success_function = function(permissions){ return permissions }
        if(typeof on_error_message === "undefined") on_error_message = "Sorry, there has been some retreiving your permissions.";
        
        var encounter_type = (typeof permission_data === "undefined")? "" : permission_data.encounter_type;
        var request_url = "/openmrs/ws/simpleformservice/api/retrieve_data_access/" + encounter_type;
        
        var promise_to_attempt = new Promise((resolve, reject)=>{
                console.log("Retreiving permission data from server");
                // send the data to the server and resolve on the response
                jq.get(request_url, {}, function (response){
                    console.log("Request Responded");
                    console.log(response);
                    resolve(response);  
                });
            });
        var promise_to_respond_to_attempt = promise_to_attempt
            .then((server_response)=>{
                console.log("promise_to_respond_to_attempt's then is being triggered")
                return on_success_function(server_response);
            })
            .catch((errors)=>{
                console.log(errors);
                alert(on_error_message);
            })
        return promise_to_respond_to_attempt;
    },
}