simpleformservice.answer_handlers["boolean"] = {
    hl7_datatype_abbreviation : "BIT",
    
    grab_answer : function(element){
        if(!(element instanceof jQuery)) element = jq(element) // ensure element is jquery element
        
        // get value from simpleanswer
        var value = jq(element).find("input[type='radio']:checked").val();
        if(typeof value === "undefined") value = null;
        if(typeof value === "string") value = value.toLowerCase();
        if(value == "yes" || value == "true" || value === true) value = "true";
        if(value == "no" || value == "false" || value === false) value = "false";
        
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
        
        // return data on succes
        return {
            datatype : this.hl7_datatype_abbreviation,
            value : value,
        }
    } 
    
}