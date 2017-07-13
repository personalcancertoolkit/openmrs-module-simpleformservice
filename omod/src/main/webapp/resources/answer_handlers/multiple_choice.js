simpleformservice.answer_handlers["multiple_choice"] = {
    hl7_datatype_abbreviation : "ST", // use free text because multiple choice can have any type of value the user wants
    
    grab_answer : function(element){
        if(!(element instanceof jQuery)) element = jq(element) // ensure element is jquery element
        
        // get value from simpleanswer
        var value = jq(element).find("input[type='radio']:checked").val();
        if(value == undefined) value = null;
        console.log(value);
        
        // throw error if required and not set
        var concept = element.attr("concept");
        var required = element.find("simpleanswer").attr("required");
        if(required == "true" || required == "required") required = true;
        if(value === null && required === true) {
            throw {
                message : "required but null",
                concept : concept
            };
        }
        
        if(value === null) value = "null"; // convert to string for user to handle if they want.
        
        // return data on succes
        return {
            datatype : this.hl7_datatype_abbreviation,
            value : value,
        }
    } 
    
}