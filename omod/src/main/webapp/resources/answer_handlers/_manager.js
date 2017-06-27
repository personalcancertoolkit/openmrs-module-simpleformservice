/*
    This object handles retreiving and returning answer handlers
    This is the part of the functionality that is responsible for keeping the front end service light. 
        - It only loads managers when they are used. Thus regardless of how many handlers are defined, the client will be minimally burdened.
        - This object does everything in promises.
        
    All answer_handlers that are loaded automatically repopulate the answer_handlers property of the simpleformservice object, replacing their paths with references to the actual object
*/

simpleformservice.manager_of_answer_handlers = {
    
    promise_answer_handler_for_type : function(type){
        var desired_handler = simpleformservice.answer_handlers[type];
        if(typeof desired_handler === "undefined") {
            alert("DEV ERROR: promise answer handler undefined for type (" + type + ")");   
            return false;
        }
        
        if(typeof desired_handler === "string"){
            // handler has not been loaded yet or started to be loaded, return a promise that loads the handler /and/ passes the handler through resolve
            
            // create promise which loads the handler
            var promise_desired_handler = new Promise((resolve, reject)=>{
                //console.log("running promise of grabbing answer handler for " + desired_handler);
                var script_url = this.resource_root + "/answer_handlers/" + desired_handler; 
                var script = document.createElement('script');
                script.setAttribute("src", script_url);
                script.onload = function(){
                    //console.log("answer handler for " + type + " loaded");
                    resolve("success");
                }.bind(this);
                document.getElementsByTagName('head')[0].appendChild(script);
            })
                
            // set the handler to reference promise, to prevent many loads of the script
            simpleformservice.answer_handlers[type] = promise_desired_handler;
            
            return promise_desired_handler.then((data)=>{
                return new Promise((resolve, reject)=>{
                    resolve(simpleformservice.answer_handlers[type]);
                })
            })
        }
        
        // if it is already promised, then just wait for that promise to load
        if(Promise.resolve(desired_handler) == desired_handler){ // if it is a promise
            return desired_handler.then((data)=>{
                return new Promise((resolve, reject)=>{
                    resolve(simpleformservice.answer_handlers[type]);
                })
            })
        }
        
        if(typeof desired_handler === "object"){
            // handler has been loaded, return a promise that passes the handler through resolve
            return new Promise((resolve, reject)=>{
                resolve(simpleformservice.answer_handlers[type]);
            })
        }
        
        alert("DEV ERROR: unknown case occured for type (" + type + ")")
        return false;
    },
    
    
}