// define structure simpleformservice
var simpleformservice = {
    resource_root : null,
    
    // permissions
    simple_permission_manager : null,
    
    // data
    submission_handler : null,
    simple_submission : null,
    encounter_retreiver : null,
    answer_handlers : null,
    manager_of_answer_handlers : null,
    
    promise : {
        simple_permission_manager : null,    // used to promise simple_permission_manager loaded
        submission_handler : null,           // used to promise submission_handler loaded
        manager_of_answer_handlers : null,   // "" manager_of_answer_handlers ""
        simple_submission : null,            // "" simple_submission ""
    }
}

// set resource root
simpleformservice.resource_root = "/openmrs/ms/uiframework/resource/simpleformservice/",
    
// set answer handler locations
simpleformservice.answer_handlers = { // note, the relative paths get replaced with the actual objects when they are initialized.
    "boolean" : "boolean.js",  
    "multiple_choice" : "multiple_choice.js",
}


// promise to load simple_permission_manager 
simpleformservice.promise.simple_permission_manager = new Promise((resolve, reject)=>{
    var script_url = simpleformservice.resource_root + "/permissions/simple_permission_manager.js";
    var script = document.createElement('script');
    script.setAttribute("src", script_url);
    script.onload = function(){
        resolve("success");
    };
    document.getElementsByTagName('head')[0].appendChild(script);
})


// promise to load manager of answer handlers
simpleformservice.promise.manager_of_answer_handlers = new Promise((resolve, reject)=>{
    var script_url = simpleformservice.resource_root + "/answer_handlers/_manager.js";
    var script = document.createElement('script');
    script.setAttribute("src", script_url);
    script.onload = function(){
        simpleformservice.manager_of_answer_handlers.resource_root = simpleformservice.resource_root;
        resolve("success");
    };
    document.getElementsByTagName('head')[0].appendChild(script);
})

// promise to load submission handler /after/ manager_of_answer_handlers
simpleformservice.promise.submission_handler = 
    simpleformservice.promise.manager_of_answer_handlers
    .then((data)=>{
        return new Promise((resolve, reject)=>{
            var script_url = simpleformservice.resource_root + "submission/submission_handler.js";
            var script = document.createElement('script');
            script.setAttribute("src", script_url);
            script.onload = function(){
                simpleformservice.submission_handler.manager_of_answer_handlers = simpleformservice.manager_of_answer_handlers;
                resolve("success");
            };
            document.getElementsByTagName('head')[0].appendChild(script);
        })
    });


// promise to load simple submission wrapper /after/ submission_handler
simpleformservice.promise.simple_submission = 
    simpleformservice.promise.submission_handler
    .then((data)=>{
        return new Promise((resolve, reject)=>{
            var script_url = simpleformservice.resource_root + "submission/simple_submission.js";
            var script = document.createElement('script');
            script.setAttribute("src", script_url);
            script.onload = function(){
                simpleformservice.simple_submission.submission_handler = simpleformservice.submission_handler;
                resolve("success");
            };
            document.getElementsByTagName('head')[0].appendChild(script);
        })
    });