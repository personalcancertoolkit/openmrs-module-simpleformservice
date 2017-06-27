simpleformservice.submission_handler = {
    manager_of_answer_handlers : null, 
    
    
    
    submit_encounter : function(encounter_type){
        
        // 1) Find the <simpleform> where simpleform[encounter_type = encounter_type]
        var simpleform = jq(document).find("simpleform[encounter_type='"+encounter_type+"']:first"); // `:first` because restrict submission to one form at a time. Otherwise too ambiguous in scope.

        // 2) find all simplequestions in simpleform
        var simplequestions = simpleform.find("simplequestion");
        
        // 3) for each simple question, promise observation data of form {concept : simplequestion.attr(concept), value : value, datatype:datatype}
        var promises_of_observation_data = [];
        for(var i = 0; i < simplequestions.length; i++){
            var this_question = simplequestions[i];
            var concept_promise = this.promise_concept_from_question(this_question);
            var answer_promise = this.promise_answer_from_question(this_question)
            if(answer_promise === false) return false; // alert message already sent, returning false is sufficient 
            var promise_data = Promise.all([concept_promise, answer_promise])
                .then((data_list)=>{
                    var concept = data_list[0];
                    var answer = data_list[1];
                    var value = answer.value;
                    var datatype = answer.datatype;
                    var observation = {
                        concept : concept,
                        value : value,
                        datatype : datatype,
                    }
                    //console.log(observation);
                    return observation;
                })
                .catch((err)=>{
                    // catch the error and pass it to Promise.all
                    return Promise.resolve({error: err})
                });
            promises_of_observation_data.push(promise_data);
        }
        
        // 4) once all promises with data are resolved, promise to build the full data to send or to evaluate errors if they exist
        var promise_to_build_data = Promise.all(promises_of_observation_data)
            .then((list_of_observation_data)=>{
                //console.log(list_of_observation_data);
                
                // find all objects which have error as key
                var errors = [];
                for(var i = 0; i < list_of_observation_data.length; i++){
                    var this_obs_data = (list_of_observation_data[i]);
                    var these_keys =  Object.keys(this_obs_data);
                    if(these_keys.indexOf('error') > -1) errors.push(this_obs_data);
                }
                
                // if length of error key objects > 0, throw error
                if(errors.length > 0) return Promise.reject(errors);
                
                // else, submit the data
                var submission_data = {
                    encounter_type : encounter_type,
                    observations : list_of_observation_data,
                }
                //console.log("Full observations grabbed for encounter_type " + encounter_type);
                return Promise.resolve(submission_data);
            })
        
        // 5) if resolved, return a promise which sends the data to the server and resolves with submission data
        //    if rejected, resolve with the error data
        var promise_to_submit_data_to_server = promise_to_build_data
            .then((data)=>{
                console.log("Sending data to server");
                console.log(data);
                return new Promise((resolve, reject)=>{
                    // send the data to the server and resolve on the response
                    resolve("Success!");  
                })
            })
            .catch((err)=>{// this catch could be placed earlier, but then we would not be able to return it to user.
                            // TODO - make this rejection resolve with full error list of concepts
                //console.log("Answer handlers rejected the input. Data has been passed.");
                return Promise.reject(err);
            });
        
        
        // return the promise to the user
        return promise_to_submit_data_to_server;
    },
    
    promise_concept_from_question : function(element){
        if(!(element instanceof jQuery)) element = jq(element) // ensure element is jquery element
        var concept = (element.attr("concept"));
        // note, this could be done without a promise but in order to make the code more generalized it is done as a promise
        var promise_concept = new Promise((resolve, reject)=>{
            return resolve(concept); 
        })
        return promise_concept;
    },
    
    promise_answer_from_question : function(element){
        if(!(element instanceof jQuery)) element = jq(element) // ensure element is jquery element
        var answer = element.find("simpleanswer");
        var type = answer.attr("type");
        
        // ensure that the answer_handler for this type is defined
        var promised_answer_handler = this.manager_of_answer_handlers.promise_answer_handler_for_type(type);
        if(promised_answer_handler === false) return false;
        
        // return answer 
        return promised_answer_handler.then((answer_handler)=>{
            var answer = answer_handler.grab_answer(element);
            return answer;
        })
    },
    
}