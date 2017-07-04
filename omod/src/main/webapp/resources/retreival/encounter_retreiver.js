simpleformservice.encounter_retreiver = {
    promise_encounters_for_encounter_type : function(encounter_type){
        return new Promise((resolve, reject)=>{
            //Load all possible guidelines that user can choose to create a new reminder from
            jq.get('/openmrs/ws/simpleformservice/api/get_encounters/'+ encounter_type, function (response) {
                // console.log(reminderData);
                console.log(response);
                resolve(response);
            });
        })
    }
}