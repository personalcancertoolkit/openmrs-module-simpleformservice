simpleformservice.encounter_retreiver = {
    // simpleformservice.encounter_retreiver.promise_encounters_for_encounter_type(encounter_type, person_id)
    promise_encounters_for_encounter_type : function(encounter_type, person_id){
        return new Promise((resolve, reject)=>{
            var get_string = encounter_type;
            if(typeof person_id !== "undefined") get_string += ":" + person_id;
            jq.get('/openmrs/ws/simpleformservice/api/get_encounters/'+ get_string, function (response) {
                console.log("encounters returned: ");
                console.log(response);
                resolve(response);
            }); 
        });
    }
}